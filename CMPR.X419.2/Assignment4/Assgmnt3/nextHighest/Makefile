CFLAGS =	-Wall -fmessage-length=0 -g

OBJS =		nextHighest.o utils.o

LIBS =

TARGET =	nextHighest

$(TARGET):	$(OBJS)
	$(CC) -o $(TARGET) $(OBJS) $(LIBS)

all:	$(TARGET)

clean:
	rm -f $(OBJS) $(TARGET)
