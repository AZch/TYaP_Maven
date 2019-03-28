.text  
.global main 
.type main, @function
main:  
.LFB0:  
.cfi_startproc  
pushq %rbp 
.cfi_def_cfa_offset 16 
.cfi_offset 6, -16
movq %rsp, %rbp
.cfi_def_cfa_register 6 
movl $45, -24(%rbp)
movl $1337, -20(%rbp)
movl 24(%rbp), %edx
addl $4, %edx
subl $0, %edx
imull $3, %edx
subl $160, %edx
addl $99, %edx
movl %edx, -16(%rbp)
movl $40, -24(%rbp)
movl 16(%rbp), %edx
addl $15, %edx
movl $0, %ecx
cmpl %edx, %ecx
jle .L25 
movl $7, -12(%rbp)
movl 12(%rbp), %edx
addl $10, %edx
movl %edx, -8(%rbp)
jmp .L26 
.L25:  
movl $228, -20(%rbp)
.L26:  
movl 24(%rbp), %edx
movl %edx, -16(%rbp)
nop  
popq %rbp 
.cfi_def_cfa 7, 8
ret  
.cfi_endproc  
.LFE0:  
.size main, .-main
.ident "GCC: (Ubuntu 7.3.0-27ubuntu1~18.04) 7.3.0" 
.section .note.GNU-stack,"", @progbits
