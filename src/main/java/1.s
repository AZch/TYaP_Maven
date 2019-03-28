	.file	"1.c"
	.text
	.globl	globPerem
	.data
	.align 4
	.type	globPerem, @object
	.size	globPerem, 4
globPerem:
	.long	40
	.comm	secPeremennia,4,4
	.comm	m,8,8
	.section	.rodata
.LC0:
	.string	"%d"
	.text
	.globl	main
	.type	main, @function
main:
.LFB0:
	.cfi_startproc
	pushq	%rbp
	.cfi_def_cfa_offset 16
	.cfi_offset 6, -16
	movq	%rsp, %rbp
	.cfi_def_cfa_register 6
	subq	$32, %rsp
	movl	$4, -20(%rbp)
	movl	-20(%rbp), %eax
	movl	%eax, %esi
	leaq	.LC0(%rip), %rdi
	movl	$0, %eax
	call	printf@PLT
	movl	$5, -16(%rbp)
	movb	$99, -22(%rbp)
	movl	-20(%rbp), %eax
	cmpl	-16(%rbp), %eax
	jle	.L2
	movl	-20(%rbp), %eax
	imull	-16(%rbp), %eax
	movl	%eax, -8(%rbp)
	subl	$1, -8(%rbp)
	jmp	.L3
.L2:
	movl	-16(%rbp), %edx
	movl	%edx, %eax
	sall	$2, %eax
	addl	%edx, %eax
	addl	%eax, %eax
	movl	%eax, -12(%rbp)
	addl	$11, -12(%rbp)
.L3:
	movb	$97, -21(%rbp)
	movb	$109, -21(%rbp)
	movl	$57, -4(%rbp)
	nop
	leave
	.cfi_def_cfa 7, 8
	ret
	.cfi_endproc
.LFE0:
	.size	main, .-main
	.ident	"GCC: (Ubuntu 7.3.0-27ubuntu1~18.04) 7.3.0"
	.section	.note.GNU-stack,"",@progbits
