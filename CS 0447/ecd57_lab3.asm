# Ethan Defilippi
# ECD57

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

# -------------------------------------------
.eqv ARR_LENGTH 5
.data
	arr: .word 100, 200, 300, 400, 500
	message: .asciiz "testing!" 
.text
# -------------------------------------------
.globl main
main:
    jal input_arr
    jal print_arr
    jal print_chars
	#exit()
	li v0, 10
	syscall
# -------------------------------------------

# ------------------------------------------
input_arr:
        push ra
            
            li t0, 0
            loop:
                print_str "Enter a number: "

                li v0, 5
                syscall                 ##Read in a number

                mul t1, t0, 4
                sw v0, arr(t1)

                addi t0, t0, 1      ##Increment the index
                blt t0, ARR_LENGTH, loop
            
        pop ra
    jr ra

# -------------------------------------------
print_arr: 
        push ra
            li t0, 0
            la t1, arr
            loop2:

                print_str "arr[" 
                ## print the index
                move a0, t0
                li v0, 1            ##Print the index
                syscall

                print_str "] = " 

                mul t1, t0, 4

                lw a0, arr(t1)
                li v0, 1            ##Print the value
                syscall

                print_str "\n"

                addi t0, t0, 1          ##Increment the index
                blt t0, ARR_LENGTH, loop2

        pop ra
    jr ra

# -------------------------------------------
print_chars:
        push ra
            ##print out the characters in the message
            la t1, message
            li t0, 0
            looploop:

                lb t0, 0(t1) 

                beqz t0, end  ##If It finds a 0, loop over  

                move a0, t0     
                li v0, 11     ##Print char  
                syscall  

                print_str "\n"  ##Line break

                addi t1, t1, 1 ##INCREMENT TIME

                j looploop  ##Idk why but these loops all needed unique names

            end:  

        pop ra
    jr ra

# -------------------------------------------


