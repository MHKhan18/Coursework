# Name        : sort.c
# Author      : Mohammad Khan
# Date        : 6/1/20
# Pledge      : I pledge my honor that I have abided by the Stevens Honor System.


CC     = gcc
C_FILE = $(wildcard *.c)
TARGET = $(patsubst %.c,%,$(C_FILE))
CFLAGS = -g -Wall -Werror -pedantic-errors

all:
	$(CC) $(CFLAGS) $(C_FILE) -o $(TARGET)
clean:
	rm -f $(TARGET) $(TARGET).exe
