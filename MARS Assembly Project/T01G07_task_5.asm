# A program to find the index of the target inputted in an array via recursive call and binary search algorithm.
# Author: Tong Jet Kit
# Last Modified: 11/3/2022

# global functions
.globl	main
.globl	binary_search

# data section to store variables
.data
	length_msg :	.asciiz "Array length: "
	enter_msg :	.asciiz "Enter num: "
	target_msg:	.asciiz "Enter target: "
	nl:		.asciiz "\n"
	
# instruction set begins here
.text	
	# jump to main function and save address into $ra
	jal main			# jump to main
	
	# terminate the program
	addi $v0, $0, 10		# v0 = 10 to exit program
	syscall	
	
	# def main():
	
	#############################################
	# stack diagram for main
	# address of array at -12($fp)	<---- $sp
	# i at -8($fp)
	# index at -4($fp)
	# saved #fp at ($fp)		< ----- $fp
	# saved $ra at 4($fp)
	# original $sp address
	#############################################
	
	# def main()
	main:	
		# save $sp into $fp and allocate space for $fp and $ra
		add  $fp, $0, $sp	# fp = $sp
		addi $sp, $sp, -8	# allocates 8 bytes of space
		
		# store $ra and $fp in the stack
		sw $ra, 4($sp)		# saves $ra
		sw $fp, ($sp)		# saves $fp
		
		# copy $sp to $fp
		addi $fp, $sp, 0         # $fp = $sp
		
		# allocate space for local variable
		addi $sp, $sp, -12
		
		# print "Array length: "
		addi $v0, $0, 4		# print string
		la   $a0, length_msg	# print length_msg
		syscall
	
		# obtain array length input
		addi $v0, $0, 5		# input integer
		syscall
		add  $t0, $0, $v0 	# $t0 = arr.length
	
		# creating array
		addi $v0, $0, 9		# allocate space
		sll  $t1, $t0, 2  	# array length * 2^2
		addi $a0, $t1, 4  	# $a0 = (arr.length * 4)+ 4 = total allocated space
		syscall
		sw   $t0, ($v0) 		# length of array stored in first slot of array
		sw   $v0, -12($fp)	# store address into the stack
	
		# intializing the i counter in the loop and store it into the stack
		addi $t1, $0, 0 		# $t1 = i = 0
		sw   $t1, -8($fp) 	# store i counter in into the stack
		
		# store index = 0 into the stack
		sw  $0, -4($fp)		# store index into the stack
	
		# initializing the array	
		for :   
			# loop conditions
			lw  $t0, -12($fp)	# t0 = address of array
			lw  $t1, ($t0)		# t1 = length of array
			lw  $t0, -8($fp)		# t0 = i
			beq $t0, $t1, exitloop	# if $t0(counter) == $t1(size), exit the loop
			
			# print "Enter num: "
			addi $v0, $0, 4		# print string
			la   $a0, enter_msg	# print int_input
			syscall
		
			# find the address of the index
			lw   $t0, -8($fp)	# t0 = i
			lw   $t2, -12($fp) 	# $t2 = address of array
			sll  $t0, $t0, 2 	# $t0 = i * 2^2
			add  $t2, $t2, $t0 	# $t2 = address + i * 2^2
			addi $t2, $t2, 4 	# $t2 = (address + i* 2^2) + 4
			addi $v0, $0, 5		# Read input
			syscall
			sw   $v0, ($t2) 	# store number
			
			# increase counter
			lw   $t0, -8($fp)	# $t0 = i
			addi $t0, $t0, 1		# $t0 = i+1
			sw   $t0, -8($fp)	# store the incremented i into the stack
			j for 			# jump back to the loop
			
		exitloop:
			# print "Enter target: "
			addi $v0, $0, 4 		# print String
			la   $a0, target_msg	# print target_msg
			syscall
	
			# obtain target input
			addi $v0, $0, 5 		# read integer($v0 = target)
			syscall

			# allocate space for argument and store arguments
			addi $sp, $sp, -16	# allocate 4 argument spaces
			lw   $t1, -12($fp)	# $t1 = address of array
			sw   $t1, 12($sp) 	# argument_1 = address of array
			add  $t0, $0, $v0	# $t0 = target
			sw   $t0, 8($sp) 	# argument_2 = target
			addi $t0, $0, 0		# $t0 = low
			sw   $t0, 4($sp) 	# argument_3 = low
			lw   $t0, ($t1)		# $t0 = length of array
			addi $t0, $t0, -1	# $t0 = hi = len(array)-1
			sw   $t0, ($sp)		# argument_4 = high
				
			# start the binary search
			jal binary_search
			
			# remove arguements from stack 
			addi $sp, $sp, 16	
			
			# store returned value into index in the stack
			sw $v0, -4($fp)		# store target index into -4($fp) which is the index
			
			# print out the target index
			addi $v0, $0, 1		# print interger
			lw   $a0, -4($fp)	# $a0 = index
			syscall			# print integer
			
			# print "\n"
			addi $v0, $0, 4		# print string
			la   $a0, nl		# print new line
			syscall
			
			# remove local variables
			addi $sp, $sp, 12	# pop array, i and index off the stack
		
			# restores $fp and $ra and remove them from stack
			lw $fp, ($sp)		# $fp = $sp. restores $fp
			lw $ra, 4($sp)		# restores $ra
			addi $sp, $sp, 8		# push $fp and $ra off the stackk
		
			# exit from main function and return back to caller
			jr $ra			# jump back to main function using saved return address in $ra
	
	###########################################
	# stack diagram for binary_search
	# mid at -4($fp)		<--- $sp
	# saved $fp at ($fp)	<--- $fp
	# saved $ra at 4($fp)
	# hi at 8($fp)
	# low at 12($fp)
	# target at 16($fp)
	# address of array 20($fp)
	###########################################
	
	# def binary_search(the_list, target, low, high):
	binary_search: 
		# allocate space to store $fp and $ra
		addi $sp, $sp, -8
		sw   $ra, 4($sp)
		sw   $fp, ($sp)
			
		# copy $sp to $fp and allocate space for local variable
		addi $fp, $sp, 0		# $fp = $sp
		
		# if-then 
		if:
			# if low > high:
			lw  $t0, 12($fp)	# $t0 = low
			lw  $t1, 8($fp)		# $t1 = high
			slt $t2, $t1, $t0	# $t2 = 1 if low > high, else $t2 = 0 if low < high
		
			# $t2 = 0 if low < high
			beq $t2, $0, else	# if low < high, jump to else
			
			# return -1		
			addi $v0, $0, -1		# v0 = -1 to be returned
		
			# restores $fp and $ra and remove them from stack
			lw $fp, ($sp)		# $fp = $sp. restores $fp
			lw $ra, 4($sp)		# restores $ra
			addi $sp, $sp, 8		# push $fp and $ra off the stackk
		
			# exit from binary_search function and return back to caller
			jr $ra			# jump back to caller using return address
		
		# else if the if statement above is not executed	
		else:
			# mid = (high + low) // 2
			lw   $t0, 12($fp)	# $t0 = low
			lw   $t1, 8($fp)		# $t1 = high
			add  $t2, $t1, $t0	# $t2 = $t0+$t1 = high + low
			srl  $t2, $t2, 1		# t2 / 2^1 ((high + low) / 2)
			addi $sp, $sp, -4	# allocate 4 bytes of space to store mid
			sw   $t2, -4($fp)	# mid = quotient = $t2
			
			nested_if:	
				# if list[mid] == target
				# get list[mid] element first
				lw   $t0, 20($fp) 		# $t0 = address of array
				lw   $t1, -4($fp)		# $t1 = mid
				sll  $t1, $t1, 2			# $t1 = mid * 2^2
				add  $t2, $t0, $t1		# $t2 = address + (mid*2^2)
				addi $t2, $t2, 4			# $t2 = address + (mid*2^2)+ 4 = index address
				lw   $t3, ($t2)			# $t3 = list[mid] element
				lw   $t4, 16($fp)		# $t4 = target
				bne  $t3, $t4, nested_elif	# if $t3 != $t4, jump to nested_elif
				
				# return mid
				lw $v0, -4($fp)		# v0 = mid to be returned
		
				# remove local variables
				addi $sp, $sp, 4		# pop mid
		
				# restores $fp and $ra and remove them from stack
				lw $fp, ($sp)		# $fp = $sp. restores $fp
				lw $ra, 4($sp)		# restores $ra
				addi $sp, $sp, 8		# push $fp and $ra off the stackk
		
				# exit from binary_search function and return back to caller
				jr $ra			# jump back to caller using return address
					
			nested_elif: 	
				# if the_list[mid] > target
				lw   $t0, 20($fp) 		# $t0 = address of array
				lw   $t1, -4($fp)		# $t1 = mid
				sll  $t1, $t1, 2			# $t1 = mid * 2^2
				add  $t2, $t0, $t1		# $t2 = address + (mid*2^2)
				addi $t2, $t2, 4			# $t2 = address + (mid*2^2)+ 4 = index address
				lw   $t3, ($t2)			# $t3 = list[mid] element
				lw   $t4, 16($fp)		# $t4 = target
				slt  $t5, $t3, $t4		# $t5 = 1 if list[mid] < target otherwise $t5 = 0
				bne  $t5, $0, nested_else		# if list[mid] < target jump to nested_else
				
				# recurion call for binary search if list[mid] > target
				addi $sp, $sp, -16 	# allocate space for argument
				lw   $t0, 20($fp)	# $t0 = address of array
				sw   $t0, 12($sp)	# argument_1 = address of array
				lw   $t0, 16($fp)	# $t0 = target
				sw   $t0, 8($sp)		# argument_2 = target
				lw   $t0, 12($fp)	# $t0 = low
				sw   $t0, 4($sp)		# argument_3 = low
				lw   $t0, -4($fp)	# $t0 = mid
				addi $t0, $t0, -1	# $t0 = mid-1
				sw   $t0, ($sp)		# argument_4 = high = mid-1
				
				# recursive call
				jal binary_search	# call binary_search
				
				addi $sp, $sp, 16	# deallocate arguments
				sw   $v0, -4($fp)	# mid = target index
				addi $sp, $sp, 4		# Restore $fp and $ra
				lw   $fp, 0($sp) 	# restore $fp
				lw   $ra, 4($sp)		# restore $ra
				addi $sp, $sp, 8 	# deallocte saved $fp and $ra
				jr   $ra 		# return to caller
				
			nested_else:
				# recursion call for binary search if list[mid] < target
				addi $sp, $sp, -16 	# allocate space for argument
				lw   $t0, 20($fp)	# $t0 = address of array
				sw   $t0, 12($sp)	# argument_1 = address of array
				lw   $t0, 16($fp)	# $t0 = target
				sw   $t0, 8($sp)		# argument_2 = target
				lw   $t0, -4($fp)	# $t0 = mid
				addi $t0, $t0, 1		# $t0 = mid+1
				sw   $t0, 4($sp)		# argument_3 = low = mid + 1
				lw   $t0, 8($fp)		# $t0 = high
				sw   $t0, ($sp)		# argument_4 = high 
				
				# recursive call  
				jal binary_search	# call binary_search
				
				# deallocate arguements and return mid to caller. Restores stored $fp and $ra
				addi $sp, $sp, 16	# deallocate arguments
				sw   $v0, -4($fp)	# mid = target index
				addi $sp, $sp, 4		# restore $fp and $ra
				lw   $fp, 0($sp) 	# restore $fp
				lw   $ra, 4($sp)		# restore $ra
				addi $sp, $sp, 8 	# deallocate saved $fp and $ra
				jr   $ra 		# return to caller