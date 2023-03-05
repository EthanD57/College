
.data

# Constants used to convert ASCII values into tile/object types.
# 0x41 is 'A' and 0x61 is 'a' but the assembler is Too Dumb to allow me to write that
.eqv TILE_OFFSET_CONSTANT 0x41
.eqv OBJ_OFFSET_CONSTANT 0x61

# this is the actual map represented in ascii
level_data:
	.ascii "HHHHOGOGOGOGOO"  # 0
	.ascii "WWWWWWWWWWWWWW"  # 1
	.ascii "WWWWWWWWWWWWWW"  # 2
	.ascii "WWWWWWWWWWWWWW"  # 3
	.ascii "WWWWWWWWWWWWWW"  # 4
	.ascii "WWWWWWWWWWWWWW"  # 5
	.ascii "GGGGGGGGGGGGGG"  # 6
	.ascii "PPPPPPPPPPPPPP"  # 7
	.ascii "PPPPPPPPPPPPPP"  # 8
	.ascii "PPPPPPPPPPPPPP"  # 9
	.ascii "PPPPPPPPPPPPPP"  # 10
	.ascii "HHHHGGGGGGGGGG"  # 11

# object spawning data
spawn_data:
	.ascii "     g g g g  "  # 0
	.ascii "  ll          "  # 1
	.ascii "        cc    "  # 2
	.ascii "    ll     ll "  # 3
	.ascii "  cc   cc     "  # 4
	.ascii "   ll         "  # 5
	.ascii "              "  # 6
	.ascii "  f   f       "  # 7
	.ascii "        f f   "  # 8
	.ascii "    s   s     "  # 9
	.ascii "       s      "  # 10
	.ascii "              "  # 11

# this maps ascii chars to TILE_TYPEs (which are just number constants)
# index 0 is really index 0x41 (CAPTIAL A)
char_to_tile: .byte
	0             # A
	0             # B
	0             # C
	0             # D
	0             # E
	0             # F
	TILE_GRASS    # G
	TILE_HUD      # H
	0             # I
	0             # J
	0             # K
	0             # L
	0             # M
	0             # N
	TILE_OUCH     # O
	TILE_PAVEMENT # P
	0             # Q
	0             # R
	0             # S
	0             # T
	0             # U
	0             # V
	TILE_WATER    # W
	0             # X
	0             # Y
	0             # Z

# this maps ascii chars to OBJ_TYPES (which are just number constants)
# index 0 is really index 0x61 (lowercase a)
char_to_obj: .byte
	0            # a
	0            # b
	OBJ_CROC     # c
	0            # d
	0            # e
	OBJ_CAR_FAST # f
	OBJ_GOAL     # g
	0            # h
	0            # i
	0            # j
	0            # k
	OBJ_LOG      # l
	0            # m
	0            # n
	0            # o
	OBJ_PLAYER   # p
	0            # q
	0            # r
	OBJ_CAR_SLOW # s
	0            # t
	0            # u
	0            # v
	0            # w
	0            # x
	0            # y
	0            # z

.text

load_map:
enter s0, s1
	# loop over map and write data into datamap

	# s0 is our current row
	li s0, 0
	_row_loop:
		# s1 is our current column, we want to reinitialize to
		# 0 for every row
		li s1, 0
		_col_loop:
			# t0 = (row * #tiles per row) + col
			mul t0, s0, MAP_WIDTH
			add t0, t0, s1

			# get ascii character, convert to to tile_type, and store into tilemap
			lb  t1, level_data(t0)
			sub t2, t1, TILE_OFFSET_CONSTANT
			lb  t2, char_to_tile(t2)
			sb  t2, tilemap(t0)
		inc s1
		blt s1, MAP_WIDTH, _col_loop
	# go to next row
	inc s0
	blt s0, MAP_HEIGHT, _row_loop

	jal load_objs
leave s0, s1

load_objs:
enter s0, s1
	# initialize player variables
	li t0, PLAYER_START_X
	sb t0, player_x
	li t0, PLAYER_START_Y
	sb t0, player_y
	li t0, OBJ_PLAYER
	sb t0, object_type
	sb zero, player_timer

	# set cur_num_objs to 1 so calls to add_obj have the appropriate num
	li t0, 1
	sw t0, cur_num_objs

	# s0 is our current row
	li s0, 0
	_map_row_loop:
		# s1 is our current column
		li s1, 0
		_map_col_loop:

			# t2 = (row * #tiles per row) + col
			mul t2, s0, MAP_WIDTH
			add t2, t2, s1

			# load ascii byte we are looking at
			lb a0, spawn_data(t2)

			# if its a space this is not an obj, so ignore it
			beq a0, ' ' , _not_an_obj

				# convert ascii to obj_type and add the object
				sub a0, a0, OBJ_OFFSET_CONSTANT
				lb  a0, char_to_obj(a0)

				move a1, s0
				move a2, s1
				jal add_obj
			_not_an_obj:
		inc s1
		blt s1, MAP_WIDTH, _map_col_loop
	# go to next row
	inc s0
	blt s0, MAP_HEIGHT, _map_row_loop
leave s0, s1

