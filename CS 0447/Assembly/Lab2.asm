# Ethan Defilippi
# Ecd57 

# preserves a0, v0
.macro print_str %str
	.data
	print_str_message: .asciiz %str
	.text
	push a0
	push v0
	la a0, print_str_message
	li v0, 4
	syscall
	pop v0
	pop a0
.end_macro

.data 
    display: .word 0
.text

.global main
main: 

	print_str "Hello! Thank you for using my calculator! \n"

    _loop:

    # Prints the output of a0
    lw a0, display
    li v0, 1
    syscall

    print_str "\n"

    # Prints the operation prompt
    print_str "Operation (=,+,-,*,/,c,q): "

    # Reads the operation
    li v0, 12
    syscall

    # Checks the operation
    beq v0, 'q', _quit
    beq v0, 'c', _clear
    beq v0, '/', _div
    beq v0, '*', _mult
    beq v0, '-', _sub
    beq v0, '+', _add
    beq v0, '=', _equal
    print_str "\nNot an operator! \n"


     j _loop

    ##End
    li v0, 10
    syscall

##Adds
_add:
    print_str "\nValue: "
    li v0, 5
    syscall
    add a0, a0, v0
    sw a0, display
    j _loop

##Subtracts
_sub:
        print_str "\nValue: "
    li v0, 5
    syscall
    sub a0, a0, v0
    sw a0, display
    j _loop

##Multiplies
_mult:
        print_str "\nValue: "
    li v0, 5
    syscall
    mul a0, a0, v0
    sw a0, display
    j _loop

##Divides
_div:
        print_str "\nValue: "
    li v0, 5
    syscall
    beq v0, 0, _error
    div a0, a0, v0
    sw a0, display
    j _loop

##Equals
_equal:
    print_str "\nValue: "
    li v0, 5
    syscall
    sw v0, display
    j _loop

##Clears the display number
_clear:
    li a0, 0
    sw a0, display
    print_str " \n"
    j _loop

##Errors go here
_error:
    print_str "You cannot divide by zero! \n"
    j _loop

##Quits the program
_quit:
    print_str "\nThank you for doing some MATH with me! \n"
   
    ##End
    li v0, 10
    syscall

##Not an operation error call
_notOp:
    print_str "\nThat is not an operation! \n"
    j _loop
    
    

         
