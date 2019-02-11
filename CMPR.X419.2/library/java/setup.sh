export JAVA_HOME=$(/usr/libexec/java_home -v 1.8)
export JUNIT_HOME=$PWD
# Suggested:
#export JUNIT_HOME = /Library/JUNIT
# 4.12
#export CLASSPATH = $CLASSPATH:$JUNIT_HOME/junit4.12.jar:.
# Dowloaded beta:
export CLASSPATH=$JUNIT_HOME/junit-4.13-beta-2.jar:$JUNIT_HOME/hamcrest-2.1.jar:.
