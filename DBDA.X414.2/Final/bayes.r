# Data set:
#https://archive.ics.uci.edu/ml/datasets/sms+spam+collection
# sample is just the first 10 to test the algorithm

smsfile <- "SMSSpamCollection"
#smsfile <- "sample"
smsds <- read.table(smsfile,sep="\t",stringsAsFactors = FALSE,quote="",col.names=c("Class","Text"))
smsds$ID <- seq.int(nrow(smsds))
smsds$isSpam <- smsds$Class=="spam"
smsds$Class <- factor(smsds$Class)
str(smsds)

#train <- smsds

# Train and testing
total<- nrow(smsds)
top80 <- total *0.8
low20 <- total - top80

train <- head(smsds,top80)
test <- tail(smsds,low20)


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

vectorizer = vocab_vectorizer(vocab)

dtm_train = create_dtm(it_train, vectorizer)

str(dtm_train)
dim(dtm_train)

dtm_train_matrix <- as.matrix(dtm_train)

#dtm_train_matrix_spam <- dtm_train_matrix[train$Class=="spam"]

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

nbClassify <- function(vec2Classify,p0Vec,p1Vec,pClass1) {
  p1 <- sum(vec2Classify * p1Vec) + log(pClass1)
  p0 <- sum(vec2Classify * p0Vec) + log(1.0 - pClass1)
  if (p1 > p0) {
    return(TRUE)
  } else {
    return(FALSE)
  }

}

#testing

it_test = test$Text %>% 
  prep_fun %>% tok_fun %>% 
  # turn off progressbar because it won't look nice in rmd
  itoken(ids = test$ID, progressbar = FALSE)

dtm_test = create_dtm(it_test, vectorizer)


#test <- train
#dtm_test_matrix <- dtm_train_matrix
dtm_test_matrix <- as.matrix(dtm_test)

numTestDocs <- nrow(dtm_test_matrix)
results <- test$isSpam;
for ( i in 1:numTestDocs) {
  testvec <- dtm_test_matrix[i,]
  results[i] <- nbClassify(testvec,p0Vect,p1Vect,pSpam)
  #results[i] <- FALSE
  print(results[i])
}

library(caret)

cm <- confusionMatrix(results,reference=test$isSpam)

print(cm)

#Reference
#Prediction FALSE TRUE
#FALSE   956    6
#TRUE     14  139

