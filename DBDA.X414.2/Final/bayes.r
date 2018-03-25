# Data set:
#https://archive.ics.uci.edu/ml/datasets/sms+spam+collection
# sample is just the first 10 to test the algorithm
library(text2vec)
#library(data.table)
library(magrittr)
library(caret)

readSmsSpamSource <- function(smsfile) {
  smsds <- read.table(smsfile,sep="\t",stringsAsFactors = FALSE,quote="",col.names=c("Class","Text"))
  # There are some duplicated:
  #dups <- duplicated(smsds)
  #smsds <- smsds[!dups,]
  smsds$ID <- seq.int(nrow(smsds))
  smsds$isSpam <- smsds$Class=="spam"
  smsds$Class <- factor(smsds$Class)
  return(smsds)
}

divideTrainAndTest <- function(dataset) {
  #train <- smsds
  #test <- smsds
  
  # Train and testing
  total<- nrow(smsds)
  top80 <- as.integer(total *0.8)
  low20 <- total - top80
  
  train <- head(smsds,top80)
  test <- tail(smsds,low20)
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
  
  vocab
  
  #### Create Training Document Term Matrix ####
  vectorizer = vocab_vectorizer(vocab)
  
  dtm_train = create_dtm(it_train, vectorizer)
  
  
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

# Based on the book, to be deleted in favor of the previous:
nbClassify <- function(vec2Classify,p0Vec,p1Vec,pClass1) {
  p1 <- sum(vec2Classify * p1Vec) + log(pClass1)
  p0 <- sum(vec2Classify * p0Vec) + log(1.0 - pClass1)
  if (p1 > p0) {
    return(TRUE)
  } else {
    return(FALSE)
  }

}

# This is just debug code, the report and presentation is based on this code.
# If you want to execute change the FALSE to TRUE
if (TRUE) {

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

#### Start of book function ####
#nrow(dtm_train_matrix)
numTrainDocs <- nrow(dtm_train_matrix)
numWords <- ncol(dtm_train_matrix)

pSpam <- sum(train$Class=="spam")/(numTrainDocs)
pHam <- sum(train$Class=="ham")/(numTrainDocs)
p0Num = matrix(1.0,nrow=1,ncol=numWords)
p1Num = matrix(1.0,nrow=1,ncol=numWords)
colnames(p0Num) <- colnames(dtm_train_matrix)
colnames(p1Num) <- colnames(dtm_train_matrix)
p0Denom <- 2.0
p1Denom <- 2.0

for ( i in 1:numTrainDocs) {
  if (train$Class[i]=="spam") {
    print(sprintf("Spam: %d",i))
    p1Num <- p1Num + dtm_train_matrix[i,]
    p1Denom <- p1Denom + sum(dtm_train_matrix[i,])
    print("finish")
  } else {
    print(sprintf("Ham: %d",i))
    p0Num <- p0Num + dtm_train_matrix[i,]
    p0Denom <- p0Denom + sum(dtm_train_matrix[i,])
  }
}

p1Vect <- log(p1Num/p1Denom) 
p0Vect <- log(p0Num/p0Denom) 
#### End of book function ####

nbModel <- trainNaiveBayes(dtm_train_matrix,train$isSpam)


#### Apply the model to the testing set ####

numTestDocs <- nrow(dtm_test_matrix)
resultsOld <- logical(numTestDocs)
results <- logical(numTestDocs)

for ( i in 1:numTestDocs) {
  testvec <- dtm_test_matrix[i,]
  resultsOld[i] <- nbClassify(testvec,p0Vect,p1Vect,pSpam)
  results[i] <- predictNaiveBayes(nbModel,testvec)
  print(results[i])
}

# Doesn't work
#results<- sapply(dtm_test_matrix,function(testvec){
#  result <- nbClassify(testvec,p0Vect,p1Vect,pSpam)
#  print(result)
#  return(result)
#})

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


#all(resultsOld==results)
#all(nbModel$pPosVectLog==p1Vect)

#> nrow(test)
#[1] 1115
#> nrow(train)
#[1] 4459

}