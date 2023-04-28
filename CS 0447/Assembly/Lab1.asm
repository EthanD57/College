#Ethan Defilippi ECD57
.data 
    x: .word 0
    y: .word 0
.text

.global main
main: 
    
    ##Loading intermediates into registers
    li t0, 0xF00D
    li t1, 2
    li t2, 3

    ##Moving values between registers
    move a0, t0
    move v0, t1
    move t2, zero

    # print 123
    li a0, 123
    li v0, 1
    syscall

    ##Add a line break
    li a0, '\n'
    li v0, 11
    syscall

    # print 456
    li a0, 456
    li v0, 1
    syscall

    ##Add a line break
    li a0, '\n'
    li v0, 11
    syscall

    ##Taking user input
    li v0, 5
    syscall

    ##Storing user input   
    sw v0, x

    ##Taking user input v2
    li v0, 5
    syscall

    ##Storing user input v2
    sw v0, y

    ##Loading values from memory and printing the sum
    lw s6, x
    add a0, a0, s6
    lw s6, y
    add a0, a0, s6
    li v0, 1
    syscall

    ##End
    li v0, 10
    syscall



