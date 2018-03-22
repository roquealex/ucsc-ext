# Data set:
#https://archive.ics.uci.edu/ml/datasets/sms+spam+collection
# sample is just the first 10 to test the algorithm
library(text2vec)
#library(data.table)
library(magrittr)
library(caret)

## R version ##
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

# Based on the book, to be deleted in favor of the previos:
nbClassify <- function(vec2Classify,p0Vec,p1Vec,pClass1) {
  p1 <- sum(vec2Classify * p1Vec) + log(pClass1)
  p0 <- sum(vec2Classify * p0Vec) + log(1.0 - pClass1)
  if (p1 > p0) {
    return(TRUE)
  } else {
    return(FALSE)
  }

}

set.seed(1L)

#### Read the data source ####

smsfile <- "SMSSpamCollection"
#smsfile <- "sample"
smsds <- read.table(smsfile,sep="\t",stringsAsFactors = FALSE,quote="",col.names=c("Class","Text"))
smsds$ID <- seq.int(nrow(smsds))
smsds$isSpam <- smsds$Class=="spam"
smsds$Class <- factor(smsds$Class)
str(smsds)

#### Divide the data set in training and testing ####

#train <- smsds
#test <- smsds

# Train and testing
total<- nrow(smsds)
top80 <- total *0.8
low20 <- total - top80

train <- head(smsds,top80)
test <- tail(smsds,low20)

#### Create Vocabulary ####

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

str(dtm_train)
dim(dtm_train)

# Our naive bayes works with the matrix alone
dtm_train_matrix <- as.matrix(dtm_train)

#dtm_train_matrix_spam <- dtm_train_matrix[train$Class=="spam"]

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

#### Create Testing Document Term Matrix ####

it_test = test$Text %>% 
  prep_fun %>% tok_fun %>% 
  # turn off progressbar because it won't look nice in rmd
  itoken(ids = test$ID, progressbar = FALSE)

dtm_test = create_dtm(it_test, vectorizer)

dtm_test_matrix <- as.matrix(dtm_test)

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

cm <- confusionMatrix(results,reference=test$isSpam)

print(cm)

#Reference
#Prediction FALSE TRUE
#FALSE   956    6
#TRUE     14  139

#all(resultsOld==results)
#all(nbModel$pPosVectLog==p1Vect)

