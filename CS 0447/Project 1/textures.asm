
.data

# Array of pointers to the textures for each tile or object, in order
texture_atlas: .word
	tex_grass    # TILE_GRASS
	tex_pavement # TILE_PAVEMENT
	tex_water    # TILE_WATER
	tex_ouch     # TILE_OUCH
	tex_hud      # TILE_HUD

# Same but for objects
obj_textures: .word
	tex_player    # OBJ_PLAYER
	tex_goal      # OBJ_GOAL
	tex_car_slow  # OBJ_CAR_SLOW
	tex_car_fast  # OBJ_CAR_FAST
	tex_log       # OBJ_LOG
	tex_croc      # OBJ_CROC

#---------------------------------------------------------------

tex_grass: .byte
	12 12 12 12 12
	12 12 12 12 12
	12 12 12 12 12
	12 12 12 12 12
	12 12 12 12 12

tex_pavement: .byte
	 0 0 0 0 0
	 0 0 0 0 0
	 3 0 3 0 3
	 0 0 0 0 0
	 0 0 0 0 0

tex_water: .byte
	 5  5  5  5  5
	 5  5  5  5  5
	 5  5  5  5  5
	 5  5  5  5  5
	 5  5  5  5  5

tex_ouch: .byte
	 1  12 1  12 1
	 12 1  12 1  12
	 1  12 1  12 1
	 12 1  12 1  12
	 1  12 1  12 1

tex_hud: .byte
	 -1 -1 -1 -1 -1
	 -1 -1 -1 -1 -1
	 -1 -1 -1 -1 -1
	 -1 -1 -1 -1 -1
	 -1 -1 -1 -1 -1

#---------------------------------------------------------------

tex_player: .byte
	 4  -1  -1  -1  4
	 4  14  3  14  4
	 -1  3  3  3  -1
	 -1  4  3  4  -1
	 4  -1  -1  -1  4

tex_goal: .byte
	4  -1  -1  -1  4
	 -1  4  3  4  -1
	 -1  3  3  3  -1
	 4  1  3  1  4
	 4  -1  -1  -1  4

tex_car_slow: .byte
	15 -1 -1 15 -1
	9 9 9 9 -1
	9 9 9 9 -1
	9 9 9 9 -1
	15 -1 -1 15 -1

tex_car_fast: .byte
	 -1  -1  15  -1  15
	 -1  1  1  1  1
	 6  6  6  6  6
	 -1  1  1  1  1
	 -1  -1  15  -1  15

tex_log: .byte
	10 10 10 9 10
	10 9 10 10 10
	10 10 9 10 10
	10 10 10 10 10
	10 9 10 9 10

tex_croc: .byte
	12 -1 -1 -1 12
	-1 7 12 12 -1
	-1 12 12 12 -1
	-1 7 12 12 -1
	12 -1 -1 -1 12

#---------------------------------------------------------------

tex_heart: .byte
	-1  1 -1  1 -1
	 1  1  1  1  1
	 1  1  1  1  1
	-1  1  1  1 -1
	-1 -1  1 -1 -1