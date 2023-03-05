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
		# jal check_input
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
	# jal obj_move_all
	jal maybe_spawn_object
	# jal player_collision
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
	blt s0, 3, heart_loop

leave s0

#-------------------------------------------------------------------------------------------------

obj_draw_all:
enter s0

	lw s0, cur_num_objs
	dec s0
	
	Loop:
		bgez s0, Exit 
		
		lb a0, object_x(s0)
		lb a1, object_y(s0)
		lw a2, object_type(s0) 
		mul a2, a2, 4
		lb a2, obj_textures(a2)

		jal display_blit_5x5_trans

		dec s0
	j Loop            
Exit:

leave s0
