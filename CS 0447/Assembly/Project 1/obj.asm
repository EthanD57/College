
.data

# indexed by obj type, says how many frames there are between movements.
objtype_delay: .byte
	0      # OBJ_PLAYER
	0      # OBJ_GOAL
	15     # OBJ_CAR_SLOW - always moves right
	5      # OBJ_CAR_FAST - always moves left
	15     # OBJ_LOG - always moves right
	5      # OBJ_CROC - always moves left

# when an object moves (after the delay above), this is how far and in what direction.
objtype_vel: .byte
	0     # OBJ_PLAYER (does not have a velocity)
	0     # OBJ_GOAL
	1     # OBJ_CAR_SLOW - always moves right
	-1    # OBJ_CAR_FAST - always moves left
	1     # OBJ_LOG - always moves right
	-1    # OBJ_CROC - always moves left

# how many sprites are there on this row
# convinience data structure for knowing when to spawn new objs
row_occupancy:
.byte
	-1
	2
	2
	4
	4
	2
	-1
	2
	2
	2
	2
	-1

# starting x location for each row when spawning new objs
row_spawn_location:
.byte
	-1
	0
	13
	0
	13
	0
	-1
	13
	13
	0
	0
	-1

# what type of object spawns on each row, or -1 for "objects don't spawn"
row_type_data:
	-1,
	OBJ_LOG,
	OBJ_CROC,
	OBJ_LOG,
	OBJ_CROC,
	OBJ_LOG,
	-1,
	OBJ_CAR_FAST,
	OBJ_CAR_FAST,
	OBJ_CAR_SLOW,
	OBJ_CAR_SLOW,
	-1

.text

#-------------------------------------------------------------------------------------------------

# add an object to the obj array
# a0 - type of obj to add
# a1 - row to add on
# a2 - col to add on
# THIS WILL NOT ADD AN OBJ IF DOING DO WOULD TAKE US OVER THE MAX NUMBER OF OBJS
add_obj:
enter
	tlti a0, 0

	# if cur_num_objs < MAX_NUM_OBJECTS...
	lw t0, cur_num_objs
	bge t0, MAX_NUM_OBJECTS, _cannot_add_obj
		# t0 = current number of objects (which becomes index into arrays)

		# row_occupancy[a1]++
		lb  t1, row_occupancy(a1)
		inc t1
		sb  t1, row_occupancy(a1)

		# object_type[t0] = a0
		sb a0, object_type(t0)

		# object_x[t0] = a2 * 5 - 3
		mul a2, a2, 5
		sub a2, a2, 3
		sb  a2, object_x(t0)

		# object_y[t0] = a1 * 5 + 4
		mul a1, a1, 5
		add a1, a1, 4
		sb  a1, object_y(t0)

		# set timer and delay based on type
		# object_delay[t0] = object_timer[t0] = objtype_delay[a0]
		lb t1, objtype_delay(a0)
		sb t1, object_timer(t0)
		sb t1, object_delay(t0)

		# set velocity based on type
		# object_vel[t0] = objtype_vel[a0]
		lb t1, objtype_vel(a0)
		sb t1, object_vel(t0)

		# and finally increment the current number of objects
		add t0, t0, 1
		sw  t0, cur_num_objs
	_cannot_add_obj:
leave

#-------------------------------------------------------------------------------------------------

# removes an object that is currently live, usually because it has moved off screen
# this is O(n) but because we have a max number of objs, it should be O(1)-ish
# a0 - index into the obj array that we want to remove
remove_obj:
enter s0, s1
	# row_occupancy[object_y[a0] / 5]--, saturate at 0
	lb   t0, object_y(a0)
	div  t0, t0, 5
	lb   t1, row_occupancy(t0)
	dec  t1
	maxi t1, t1, 0
	sb   t1, row_occupancy(t0)

	# shift all values down one slot in the array
	move s0, a0
	lw s1, cur_num_objs
	_rem_loop:
		add t1, s0, 1

		# object_type[s0] = object_type[s0 + 1]
		lb t0, object_type(t1)
		sb t0, object_type(s0)

		# object_x[s0] = object_x[s0 + 1]
		lb t0, object_x(t1)
		sb t0, object_x(s0)

		# object_y[s0] = object_y[s0 + 1]
		lb t0, object_y(t1)
		sb t0, object_y(s0)

		# object_timer[s0] = object_timer[s0 + 1]
		lb t0, object_timer(t1)
		sb t0, object_timer(s0)

		# object_delay[s0] = object_delay[s0 + 1]
		lb t0, object_delay(t1)
		sb t0, object_delay(s0)

		# object_vel[s0] = object_vel[s0 + 1]
		lb t0, object_vel(t1)
		sb t0, object_vel(s0)
	add s0, s0, 1
	ble s0, s1, _rem_loop

	# cur_num_objs--
	lw  t0, cur_num_objs
	dec t0
	sw  t0, cur_num_objs
leave s0, s1

#-------------------------------------------------------------------------------------------------

# collision check for two axis aligned square bounding boxes
# @args
# a0 - x_0
# a1 - y_0
# a2 - x_1
# a3 - y_1
# @return
# v0 - bool representing if the two boxes are overlapping
bounds_check:
enter
	# this requires us to be okay with the assumption that all bounding boxes are 5x5
	add t0, a0, 5
	add t1, a1, 5
	add t2, a2, 5
	add t3, a3, 5

	# collision if this is all true
	# a0 < (t2) &&
	bge a0, t2, _no_collision
	# (t0) > a2 &&
	ble t0, a2, _no_collision
	# a1 < (t3) &&
	bge a1, t3, _no_collision
	# (t1) > a3
	ble t1, a3, _no_collision
		# if we make it here there is a collision!
		li v0, 1
	j _return
	_no_collision:
		li v0, 0
	_return:
leave

#-------------------------------------------------------------------------------------------------

# @arg a0 -> the object we are colliding with
player_move_with_object:
enter
	# give the player the same velocity as the object they are colliding with
	# and sync their countdown timer with this object

	lb t1, object_delay(a0)
	sb t1, player_delay
	lb t1, object_vel(a0)
	sb t1, player_vel
	lb t1, object_timer(a0)
	sb t1, player_timer
leave

#-------------------------------------------------------------------------------------------------

maybe_spawn_object:
enter s0, s1
	# look at every row
	li s0, 0
	_spawn_loop:

		# if there are >= 0 && < 6 objects alive on this row...
		lb t0, row_occupancy(s0)
		blt t0, 0, _end_spawn_loop
		bge t0, MAX_OBJECTS_ON_ROW, _end_spawn_loop

			# spawn object here
			# a0 - type of obj to add
			# a1 - row to add on
			# a2 - col to add on
			lb   a0, row_type_data(s0)

			# don't spawn invalid objects.
			blt  a0, 0, _end_spawn_loop
				move a1, s0
				lb   a2, row_spawn_location(s0)
				jal  add_obj
	_end_spawn_loop:
	add s0, s0, 1
	blt s0, MAP_HEIGHT, _spawn_loop
leave s0, s1

#-------------------------------------------------------------------------------------------------

offscreen_obj_removal:
enter s0, s1

	# s1 is our counter for how many objects are getting removed
	li s1, 0

	# loop over all objects, pushing indexes of any that went offscreen onto the stack.
	# this isn't "the most elegant" way of doing it, but hey, YOU try implementing a
	# variable-sized data structure in assembly! the stack is right there! whatever!

	# NOTE: it is important to iterate up from 1 here because we need to be popping items off the
	# removal stack in DESCENDING order, since removal shifts everything after it left
	li s0, 1
	_removal_detection_loop:
		# if we just moved off screen, remove this object
		lb  t0, object_x(s0)
		ble t0, OBJ_MIN_X, _offscreen
		bgt t0, OBJ_MAX_X, _offscreen
		j _no_remove
		_offscreen:
			push s0 # haha breaking ALL THE RULES
			inc s1
		_no_remove:
	inc s0
	lw t0, cur_num_objs
	ble s0, t0, _removal_detection_loop

	beq s1, 0, _no_offscreen

	_removal_loop:
		pop a0 # WOOOOOOOOOOOOOO
		jal remove_obj
	dec s1
	bgt s1, 0, _removal_loop

_no_offscreen:
leave s0, s1