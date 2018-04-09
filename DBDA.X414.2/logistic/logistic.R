library(caret)
set.seed(1)

# Removing setosa
short.iris <- iris[51:150,]

# Graph all the vars
pairs(short.iris,col=ifelse(short.iris$Species==short.iris$Species[1],"red","blue"))

# Separate in training and testing
trainIndex <- createDataPartition(short.iris$Species, p=0.8, list=FALSE,times=1)
train <- short.iris[trainIndex,]
test <- short.iris[-trainIndex,]

# Training logistic regression model Species is a function of Petal.Length and Petal.Width
lrm <- glm(Species~Petal.Length+Petal.Width, data=train, family = binomial())
summary(lrm)
# scatter plot with division line from the logistic regression model
plot(x=train$Petal.Length,y=train$Petal.Width, col=ifelse(train$Species==train$Species[1],"red","blue"))
abline(a=-(lrm$coefficients["(Intercept)"]/lrm$coefficients["Petal.Width"]),b=-(lrm$coefficients["Petal.Length"]/lrm$coefficients["Petal.Width"]))

# Predicting new values from the testing set only Petal.Length and Petal.Width are used
pred<-predict(lrm,subset(test,select=c("Petal.Length", "Petal.Width")))
# The model returns a negative number for versicolor and a positive number for virginica
pred.flower <- ifelse(pred<0,"versicolor","virginica")
# Confusion matrix
cm <- confusionMatrix(pred.flower,reference=as.character(test$Species),positive="virginica")
print(cm)
