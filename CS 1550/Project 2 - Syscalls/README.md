# Project 2 - Syscalls

These are files for an edited linux kernel that allows a crude form of kernel-level messaging to be done between users. You must rebuild the kernel to use these new files. 

Usage:
1. Copy sys.c to the linux/kernel source code directory.
2. Copy syscalls.h to linux include/linux directory.
3. Copy syscall_32.tbl and syscall_64.tbl to the linux/arch/x86/entry/syscalls directory.
4. Copy unistd.h to the linux/include/uapi/asm-generic directory.
5. Copy osmsg.c to any linux directory.
6. Rebuild the linux kernel to accept the new files added and boot into it
7. Run the osmsg.c file to send messages between users. The usage info will be displayed if you type "./osmsg" without any arguments.

You can see the exact changes to each file within the git commit history. Osmsg.c was entirely written by me. 
