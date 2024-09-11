# Compiler
CC = gcc

# Compiler flags
CFLAGS = -Wall -g -Iinclude

# Target executable name
TARGET = graphics_driver

# Source files
SRC1 = graphics_driver.c
SRC2 = gLibrary.c

# Object files
OBJ1 = $(SRC1:.c=.o)
OBJ2 = $(SRC2:.c=.o)

# Default target
all: $(TARGET)

# Linking the target executable
$(TARGET): $(OBJ1) $(OBJ2)
	$(CC) $(CFLAGS) -o $(TARGET) $(OBJ1) $(OBJ2)

# Compiling source files
$(OBJ1): $(SRC1)
	$(CC) $(CFLAGS) -c $(SRC1)

$(OBJ2): $(SRC2)
	$(CC) $(CFLAGS) -c $(SRC2)

# Run the program
run: all
	./$(TARGET)

# Clean up
clean:
	rm -f $(OBJ1) $(OBJ2) $(TARGET)
