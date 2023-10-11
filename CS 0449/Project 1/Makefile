
CC = gcc
CFLAGS = -O -Wall -Werror -Wno-error=uninitialized -m64 -g
LIBS = -lm

%.o: %.c
	$(CC) $(CFLAGS) $< -c

cbm2bmp: cbm2bmp.c headers.h metadata.h
	gcc -Wall -o cbm2bmp cbm2bmp.c headers.h metadata.h

clean:
	rm -f *.o cbm2bmp *~
	rm -rf *.dSYM
	rm -f *.bmp
