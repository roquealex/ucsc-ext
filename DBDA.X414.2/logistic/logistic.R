library(caret)
set.seed(1)
short.iris <- iris[51:150,]
pairs(short.iris,col=ifelse(short.iris$Species==short.iris$Species[1],"red","blue"))
trainIndex <- createDataPartition(short.iris$Species, p=0.8, list=FALSE,times=1)
train <- short.iris[trainIndex,]
test <- short.iris[-trainIndex,]
lrm <- glm(Species~Petal.Length+Petal.Width, data=train, family = binomial())
summary(lrm)
plot(x=train$Petal.Length,y=train$Petal.Width, col=ifelse(train$Species==train$Species[1],"red","blue"))
abline(a=-(lrm$coefficients["(Intercept)"]/lrm$coefficients["Petal.Width"]),b=-(lrm$coefficients["Petal.Length"]/lrm$coefficients["Petal.Width"]))
pred<-predict(lrm,subset(test,select=c("Petal.Length", "Petal.Width")))
pred.flower <- ifelse(pred<0,"versicolor","virginica")
cm <- confusionMatrix(pred.flower,reference=as.character(test$Species),positive="virginica")
print(cm)
