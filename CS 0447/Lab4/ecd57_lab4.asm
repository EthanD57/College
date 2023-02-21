# Ethan Defilippi
# ECD57

.include "lab4_include.asm"

.eqv NUM_DOTS 3

.data
	dotX: .word 10, 30, 50
	dotY: .word 20, 30, 40
	curDot: .word 0
.text
.globl main
main:
	# when done at the beginning of the program, clears the display
	# because the display RAM is all 0s (black) right now.
	jal display_update_and_clear

	_loop:
		jal check_input
		jal wrap_dot_position
		jal draw_dots

		jal display_update_and_clear
		jal sleep
	j _loop

	li v0, 10
	syscall

#-----------------------------------------
# new functions go here!

draw_dots:
		push ra
		push s0

			li s0, 0
            la t1, dotX						#Loads the address of the dotX and dotY array
			la t2, dotY						
            loop:

                mul t1, s0, 4

                lw a0, dotX(t1)
				lw a1, dotY(t1)				#Loads the x and y coordinates of the current dot
				lw a2, curDot

				beq a2, s0, _left
   				beq a2,	s0, _middle			#Checks if the current dot needs to be red
    			beq a2, s0, _right

				li a2, COLOR_WHITE

			_continue:
				jal display_set_pixel			#Sets the pixel to the correct color

                addi s0, s0, 1 					#Increments the current dot

                blt s0, NUM_DOTS, loop			
				j _end
		
		_left:
			li a2, COLOR_RED				
			j _continue
		
		_middle:
			li a2, COLOR_RED
			j _continue

		_right:
			li a2, COLOR_RED
			j _continue
		_end:
		pop s0
		pop ra
	jr ra

#-----------------------------------------

check_input:
		push ra

			jal input_get_keys_held		#Gets the keys that are currently being held down

			and t0, v0, KEY_Z			#Checks if the user has pressed the Z key
			beq t0, 0, _endif_z
				li t0, 0
				sw t0, curDot
			_endif_z:
			
			and t0, v0, KEY_X			#Checks if the user has pressed the X key
			beq t0, 0, _endif_x
				li t0, 1
				sw t0, curDot
			_endif_x:

			and t0, v0, KEY_C			#Checks if the user has pressed the C key
			beq t0, 0, _endif_c				
				li t0, 2
				sw t0, curDot
			_endif_c:

			lw t9, curDot
			mul t9, t9, 4				#DONT CHANGE THIS REGISTER

			and t0, v0, KEY_R
			beq t0, 0, _endif_r			#This checks if the user has pressed the right arrow key
				lw t0, dotX(t9)			
				addi t0, t0, 1
				sw t0, dotX(t9)
			_endif_r:

			and t0, v0, KEY_U
			beq t0, 0, _endif_u			#This checks if the user has pressed the up arrow key
				lw t0, dotY(t9)
				sub t0, t0, 1
				sw t0, dotY(t9)
			_endif_u:

			and t0, v0, KEY_L			#This checks if the user has pressed the left arrow key
			beq t0, 0, _endif_l
				lw t0, dotX(t9)
				sub t0, t0, 1
				sw t0, dotX(t9)
			_endif_l:

			and t0, v0, KEY_D			#This checks if the user has pressed the down arrow key
			beq t0, 0, _endif_d
				lw t0, dotY(t9)
				addi t0, t0, 1
				sw t0, dotY(t9)
			_endif_d:


		pop ra
	jr ra

#-----------------------------------------

wrap_dot_position:		#This function wraps the dot position around the screen
		push ra

		lw t0, curDot
		mul t0, t0, 4
		lw t1, dotX(t0)			#Wrapping x position			
		and t2, t1, 63
		sw t2, dotX(t0)

		lw t1, dotY(t0)	
		and t2, t1, 63			#Wrapping y position
		sw t2, dotY(t0)

		pop ra
	jr ra

