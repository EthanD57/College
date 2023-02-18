##########################################################################################
# DON'T MODIFY THIS FILE, YOU WON'T TURN IT IN
##########################################################################################

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

# constants!
.eqv DISPLAY_CTRL      0xFFFF0000
.eqv DISPLAY_KEYS      0xFFFF0004
.eqv DISPLAY_BASE      0xFFFF0008
.eqv COLOR_BLACK       0
.eqv COLOR_RED         1
.eqv COLOR_ORANGE      2
.eqv COLOR_YELLOW      3
.eqv COLOR_GREEN       4
.eqv COLOR_BLUE        5
.eqv COLOR_MAGENTA     6
.eqv COLOR_WHITE       7
.eqv COLOR_DARK_GREY   8
.eqv COLOR_DARK_GRAY   8
.eqv COLOR_BRICK       9
.eqv COLOR_BROWN       10
.eqv COLOR_TAN         11
.eqv COLOR_DARK_GREEN  12
.eqv COLOR_DARK_BLUE   13
.eqv COLOR_PURPLE      14
.eqv COLOR_LIGHT_GREY  15
.eqv COLOR_LIGHT_GRAY  15
.eqv KEY_U             0x01
.eqv KEY_D             0x02
.eqv KEY_L             0x04
.eqv KEY_R             0x08
.eqv KEY_B             0x10
.eqv KEY_Z             0x20
.eqv KEY_X             0x40
.eqv KEY_C             0x80

# -------------------------------------------------------------------------------------------------
# returns a bitwise OR of the key constants, indicating which keys are being held down.

input_get_keys_held:
	lw v0, DISPLAY_KEYS
jr ra

# ------------------------------------------------

display_update_and_clear:
	# ahh, another feature: storing 1 into DISPLAY_CTRL updates
	# the display *and* clears out the display RAM to black, so that
	# you don't have to.
	li t0, 1
	sw t0, DISPLAY_CTRL
jr ra

# ------------------------------------------------

sleep:
	# syscall 32 is sleep. this sleeps for 16 milliseconds,
	# making this loop run ~60 times per second instead of
	# "as fast as possible".
	li a0, 16
	li v0, 32
	syscall
jr ra

# -------------------------------------------------------------------------------------------------
# sets 1 pixel to a given color.
# (0, 0) is in the top LEFT, and Y increases DOWNWARDS!
# crashes if either coordinate is negative or above 63.
# arguments:
#	a0 = x
#	a1 = y
#	a2 = color (use one of the constants above)

display_set_pixel:
	tlti a0, 0  # you passed a negative x coordinate.
	tgei a0, 64 # you passed a too-large x coordinate.
	tlti a1, 0  # you passed a negative y coordinate.
	tgei a1, 64 # you passed a too-large y coordinate.

	mul t0, a1, 64
	add t0, t0, a0
	add t0, t0, DISPLAY_BASE
	sb  a2, (t0)
jr  ra


##I DID NOT WRITE THIS FILE. THIS WAS PROVIDED BY THE INSTRUCTOR (Jarrett Billingsley) FOR CS 0447 . I ONLY WROTE THE CODE IN THE MAIN FUNCTION.##