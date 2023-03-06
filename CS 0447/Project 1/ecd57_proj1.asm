# Ethan Defilippi
# ecd57

# need this early included so we have constants for declaring arrays in data seg
.include "game_constants.asm"
.data

# set to 1 to make it impossible to get a game over!
.eqv GRADER_MODE 0

# player's score and number of lives
score: .word 0
lives: .word 3

# boolean (1 means the game is over)
game_over: .word 0

# how many active objects there are. this many slots of the below arrays represent
# active objects.
cur_num_objs: .word 0

# Object arrays. These are parallel arrays. The player object is in slot 0,
# so the "player_x", "player_y", "player_timer" etc. labels are pointing to the
# same place as slot 0 of those arrays.

object_type: .byte 0:MAX_NUM_OBJECTS
player_x:
object_x: .byte 0:MAX_NUM_OBJECTS
player_y:
object_y: .byte 0:MAX_NUM_OBJECTS
player_timer:
object_timer: .byte 0:MAX_NUM_OBJECTS
player_delay:
object_delay: .byte 0: MAX_NUM_OBJECTS
player_vel:
object_vel: .byte 0:MAX_NUM_OBJECTS

# this is the 2d array for our map
tilemap: .byte 0:MAP_SIZE

.text

#-------------------------------------------------------------------------------------------------
# include AFTER our initial data segment stuff for easier memory debugging

.include "display_2227_0611.asm"
.include "map.asm"
.include "textures.asm"
.include "obj.asm"

#-------------------------------------------------------------------------------------------------

.globl main
main:
	# this populates the tilemap array and the object arrays
	jal load_map

	# do...
	_game_loop:
		jal check_input
		jal update_all
		jal draw_all
		jal display_update_and_clear
		jal wait_for_next_frame
	# ...while(!game_over)
	lw t0, game_over
	beq t0, 0, _game_loop

	# show the game over screen and exit
	jal show_game_over
syscall_exit

#-------------------------------------------------------------------------------------------------

show_game_over:
enter
	jal display_update_and_clear

	li   a0, 5
	li   a1, 10
	lstr a2, "GAME OVER"
	li   a3, COLOR_YELLOW
	jal  display_draw_colored_text

	li   a0, 5
	li   a1, 30
	lstr a2, "SCORE: "
	jal  display_draw_text

	li   a0, 41
	li   a1, 30
	lw   a2, score
	jal  display_draw_int

	jal display_update
leave

#-------------------------------------------------------------------------------------------------

update_all:
enter
	jal obj_move_all
	jal maybe_spawn_object
	jal player_collision
	jal offscreen_obj_removal
leave

#-------------------------------------------------------------------------------------------------

draw_all:
enter
	jal draw_tilemap
	jal obj_draw_all
	jal draw_hud
leave

#-------------------------------------------------------------------------------------------------

draw_tilemap:
enter s0, s1

	li s0, 0 #ROW
	row_loop:

		li s1, 0  #COLUMN
		col_loop:
		
			mul a0, s1, 5
			sub a0, a0, 3

			mul a1, s0, 5
			add a1, a1, 4

			mul t0, s0, MAP_WIDTH
			add t0, t0, s1

			lb t0, tilemap(t0)

			mul t0, t0, 4
			lw a2, texture_atlas(t0)
			
			jal display_blit_5x5_trans
			
			inc s1
			blt s1, MAP_WIDTH, col_loop
		inc s0
		blt s0, MAP_HEIGHT, row_loop


leave s0, s1

#-------------------------------------------------------------------------------------------------

draw_hud:
enter s0

	li a0, 0
	li a1, 4
	lw a2, score
	jal display_draw_int

	heart_loop: 

		mul a0, s0, 5
		li a1, 59
		la a2, tex_heart
		jal display_blit_5x5_trans
		inc s0
	lw t0, lives
	blt s0, t0, heart_loop

leave s0

#-------------------------------------------------------------------------------------------------

obj_draw_all:
enter s0

	lw s0, cur_num_objs
	dec s0
	
	object_loop:
		
		lb a0, object_x(s0)
		lb a1, object_y(s0)
		lb a2, object_type(s0)
		mul a2, a2, 4
		lw a2, obj_textures(a2)

		jal display_blit_5x5_trans
		
		beq s0, 0, Exit 
		dec s0
	j object_loop            
Exit:

leave s0

#-------------------------------------------------------------------------------------------------

obj_move_all:
enter s0

	li s0, 0
	
	move_loop: 

		lb t0, object_timer(s0)
		dec t0
		sb t0, object_timer(s0)

		bgt t0, 0, not_zero

		lb t0, object_x(s0)
		lb t1, object_vel(s0)
		add t0, t0, t1
		sb t0, object_x(s0)

		lb t0, object_delay(s0)
		sb t0, object_timer(s0)

		not_zero:
		inc s0
		lw t0, cur_num_objs
		blt s0, t0, move_loop

leave s0

#-------------------------------------------------------------------------------------------------

check_input:
	enter 

	jal input_get_keys_pressed

	beq v0, KEY_L, _left
	beq v0, KEY_R, _right
	beq v0, KEY_U, _up
	beq v0, KEY_D, _down
	j _break

	_left:
		lb t0, player_x
		ble t0, PLAYER_MIN_X, _notL
		sub t0, t0, PLAYER_VELOCITY
		sb t0, player_x
		j _done

	_notL:
		j _done

	_right:
		lb t0, player_x
		bge t0, PLAYER_MAX_X, _notR
		add t0, t0, PLAYER_VELOCITY
		sb t0, player_x
		j _done

	_notR:
		j _done

	_up:
		lb t0, player_y
		ble t0, PLAYER_MIN_Y, _notU
		sub t0, t0, PLAYER_VELOCITY
		sb t0, player_y
		j _done

	_notU:
		j _done

	_down:
		lb t0, player_y
		bge t0, PLAYER_MAX_Y, _notD
		add t0, t0, PLAYER_VELOCITY
		sb t0, player_y
		j _done

	_notD:
		j _done

	_done:	
		sb zero, player_delay
		sb zero, player_vel
		sb zero, player_timer
		j _break

	_break:
		leave

#-------------------------------------------------------------------------------------------------

player_collision:
enter s0, s1

	lb t0, player_x
	lb t1, player_y

		div t0, t0, 5
		inc t0

		div t1, t1, 5

		mul t1, t1, MAP_WIDTH
		add t1, t1, t0

		lb s0, tilemap(t1)

		bne s0, TILE_OUCH, _notOuchTile
		jal kill_player
		j _return 

	li s1, 0
	_notOuchTile:
		
			lb a0, player_x
			lb a1, player_y
			lb a2, object_x(s1)
			lb a3, object_y(s1)
			jal bounds_check

			bne v0, 1, _touchLoop

			lb t0, object_type(s1)

			beq t0, OBJ_CAR_FAST, _kill
			beq t0, OBJ_CAR_SLOW, _kill

			beq t0, OBJ_LOG, _ride_on_it
			beq t0, OBJ_CROC, _ride_on_it

			beq t0, OBJ_GOAL, _GOAL

			j _touchLoop
		

		_ride_on_it:
		move a0, s1
		jal player_move_with_object
		j _return

		_kill:
		jal kill_player
		j _return

		_GOAL:
		move a0, s1
		jal player_get_goal
		j _return

	_touchLoop:
		inc s1
		lw t0, cur_num_objs
		blt s1, t0, _notOuchTile

	bne s0, TILE_WATER, _return
	jal kill_player

_return: 
leave s0, s1

#-------------------------------------------------------------------------------------------------

kill_player: 
enter

	lw t0, lives
	ble t0, 0, _RIP
	dec t0
	beq t0, 0, _RIP
	sb t0, lives
	j _notRIP

	_RIP:
	beq zero, GRADER_MODE, _notGrader
	j _notRIP

	_notGrader:
	li t0, 1
	sb t0, game_over
	j _notRIP

	_notRIP:
	li t0, PLAYER_START_X
	sb t0, player_x
	li t0, PLAYER_START_Y
	sb t0, player_y
	sb zero, player_delay
	sb zero, player_vel
	sb zero, player_timer

_killExit:
leave

#-------------------------------------------------------------------------------------------------

player_get_goal:
enter

	jal remove_obj
	lw t0, score
	add t0, t0, GOAL_SCORE
	sw t0, score
	blt t0, MAX_SCORE, not_game_win

	li t0, 1
	sw t0, game_over

	not_game_win:

	li t0, PLAYER_START_X
	sb t0, player_x
	li t0, PLAYER_START_Y
	sb t0, player_y
	sb zero, player_delay
	sb zero, player_vel
	sb zero, player_timer
leave