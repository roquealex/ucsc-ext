CXXFLAGS =	-std=c++11 -g -Wall -fmessage-length=0

OBJS =	main.o utils.o

LIBS =

TARGET =	main

$(TARGET):	$(OBJS)
		$(CXX) -o $(TARGET) $(OBJS) $(LIBS)

all:	$(TARGET)


#util.o: ../util/util.cpp
#		$(CXX) $(CXXFLAGS) -c $(<) -o $(@)

clean:
		rm -f $(OBJS) $(TARGET)Makefile

