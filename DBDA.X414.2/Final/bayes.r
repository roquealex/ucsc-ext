# Data set:
#https://archive.ics.uci.edu/ml/datasets/sms+spam+collection
# sample is just the first 10 to test the algorithm
library(text2vec)
#library(data.table)
library(magrittr)
library(caret)
library(glmnet)

readSmsSpamSource <- function(smsfile) {
  smsds <- read.table(smsfile,sep="\t",stringsAsFactors = FALSE,quote="",col.names=c("Class","Text"))
  smsds$ID <- seq.int(nrow(smsds))
  smsds$isSpam <- smsds$Class=="spam"
  smsds$Class <- factor(smsds$Class)
  return(smsds)
}


divideTrainAndTest <- function(dataset) {
  # Train and testing

  # Caret:
  trainIndex <- createDataPartition(smsds$isSpam, p=0.8, list=FALSE,times=1)
  train<- smsds[trainIndex,]
  test<- smsds[-trainIndex,]
  
  return(list("train"=train,"test"=test))
}

createVocabularyAndDTMs <- function(train,test) {
  ## Coppied from web
  prep_fun <- tolower
  tok_fun <- word_tokenizer
  
  it_train <- itoken(train$Text, 
                     preprocessor = prep_fun, 
                     tokenizer = tok_fun, 
                     ids = train$ID, 
                     progressbar = FALSE)
  vocab <- create_vocabulary(it_train)
  
  #vocab
  
  #### Create Training Document Term Matrix ####
  vectorizer <- vocab_vectorizer(vocab)
  
  dtm_train <- create_dtm(it_train, vectorizer)
  
  
  #### Create Testing Document Term Matrix ####
  
  it_test = test$Text %>% 
    prep_fun %>% tok_fun %>% 
    itoken(ids = test$ID, progressbar = FALSE)
  
  dtm_test = create_dtm(it_test, vectorizer)
  
  return(list("vocab"=vocab,"trainDTM"=dtm_train,"testDTM"=dtm_test))
}

trainNaiveBayes <- function(train.matrix, train.class) {
  posSum <- colSums(train.matrix[train.class,])
  negSum <- colSums(train.matrix[!train.class,])
  posTotal <- sum(posSum)
  negTotal <- sum(negSum)
  # Applying laplace smoothing to avoid 0 probability adding 1 count to each word
  posSum <- posSum + 1
  negSum <- negSum + 1
  # extra 2 in the denuminator
  posTotal <- posTotal + 2 
  negTotal <- negTotal + 2
  pPos <- sum(train.class)/length(train.class)
  pPosVectLog <- log(posSum/posTotal)
  pNegVectLog <- log(negSum/negTotal)
  return(list("pPos"=pPos, "pPosVectLog"=pPosVectLog, "pNegVectLog"=pNegVectLog))
}

predictNaiveBayes <- function(nbModel,input.vector) {
  pPosGI <- sum(input.vector * nbModel$pPosVectLog) + log(nbModel$pPos)
  pNegGI <- sum(input.vector * nbModel$pNegVectLog) + log(1.0 - nbModel$pPos)
  return (pPosGI > pNegGI)
}

trainLogisticRegression <- function(train.dtm, train.class) {
  NFOLDS <- 4
  
  logReg <- cv.glmnet(x = train.dtm, y = train.class, 
                                 family = 'binomial', 
                                 # L1 penalty
                                 alpha = 1,
                                 # interested in the area under ROC curve
                                 type.measure = "auc",
                                 # 5-fold cross-validation
                                 nfolds = NFOLDS,
                                 # high value is less accurate, but has faster training
                                 thresh = 1e-3,
                                 # again lower number of iterations for faster training
                                 maxit = 1e3)
  return(logReg)
}

predictLogisticRegression <- function(lr.model,test.dtm) {
  pred <- predict(lr.model, test.dtm, type = 'response')[,1]
  return(pred)
}

# This is just debug code, the report and presentation is based on this code.
# If you want to execute change the FALSE to TRUE
if (FALSE) {

#### Main ####

set.seed(1L)

#### Read the data source ####

smsds <- readSmsSpamSource("SMSSpamCollection")

#### Divide the data set in training and testing ####

ds <- divideTrainAndTest(smsds)
train <- ds$train
test <- ds$test

#### Create Vocabulary and DTMs ####

textVec <- createVocabularyAndDTMs(train,test)
# Our naive bayes works with the matrix alone
dtm_train_matrix <- as.matrix(textVec$trainDTM)
dtm_test_matrix <- as.matrix(textVec$testDTM)

#dtm_train_matrix <- as.matrix(dtm_train)
#dtm_test_matrix <- as.matrix(dtm_test)


#### Train the Model ####

nbModel <- trainNaiveBayes(dtm_train_matrix,train$isSpam)


#### Apply the model to the testing set ####

numTestDocs <- nrow(dtm_test_matrix)
results <- logical(numTestDocs)

for ( i in 1:numTestDocs) {
  testvec <- dtm_test_matrix[i,]
  results[i] <- predictNaiveBayes(nbModel,testvec)
  print(results[i])
}

#### Models Performance ####

cm <- confusionMatrix(results,reference=test$isSpam,positive="TRUE")

print(cm)

#Reference
#Prediction FALSE TRUE
#FALSE   956    6
#TRUE     14  139
#
#Accuracy : 0.9821         
#95% CI : (0.9724, 0.989)
#No Information Rate : 0.87           
#P-Value [Acc > NIR] : <2e-16         
#
#Kappa : 0.9225         
#Mcnemar's Test P-Value : 0.1175         
#
#Sensitivity : 0.9586         
#Specificity : 0.9856         
#Pos Pred Value : 0.9085         
#Neg Pred Value : 0.9938         
#Prevalence : 0.1300         
#Detection Rate : 0.1247         
#Detection Prevalence : 0.1372         
#Balanced Accuracy : 0.9721         
#
#'Positive' Class : TRUE   

# Caret partition:
#Reference
#Prediction FALSE TRUE
#FALSE   956    8
#TRUE      9  141
#
#Accuracy : 0.9847   

#all(resultsOld==results)
#all(nbModel$pPosVectLog==p1Vect)

#> nrow(test)
#[1] 1115
#> nrow(train)
#[1] 4459

#> sum(smsds$isSpam)/length(smsds$isSpam)
#[1] 0.1340151

# Logistic Regression
modelLR <- trainLogisticRegression(textVec$trainDTM, train$isSpam)

resultsLR <- predictLogisticRegression(modelLR,textVec$testDTM)

cmLR <- confusionMatrix(resultsLR>0.5,reference=test$isSpam,positive="TRUE")
print(cmLR)

}
