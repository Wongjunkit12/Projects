# A program to count the number of multiplies of n (excluding itself) in an array without using global variables and only using stack.
# Author: Bryan Wong Jun Kit
# Last Modified: 10/3/2022	

# Global Functions
.globl	main
.globl	get_multiplies

# Data section to store variables
.data
	length_input:	.asciiz "Array length: "
	n_input:		.asciiz "Enter the value of n: "
	int_input:	.asciiz "Enter num: "
	return:		.asciiz "\nThe number of multiples of "
	return2:		.asciiz " is: "
	new_line:	.asciiz "\n"
	
# Instruction set begins here
.text
	# Link and jump into main function
	jal main 		# Jump to main and save address in $ra
	
	# Exit program
	addi $v0, $0, 10		# v0 = 10 to terminate proram
	syscall
	
	###########################################################################
	# Stack diagram for main function
	# Arguement 1 (my_list address) at -20($fp)	<---- $sp
	# Arguement 2 (n) at -16($fp)
	# Address of my_list into -12($fp)
	# n at -8($fp)
	# i at -4($fp)
	# Saved $fp at ($fp)	<---- $fp
	# Saved $ra at 4($fp)
	# Original $sp address
	###########################################################################
	
	# def main():
	main:
		# Set $fp and make space to store $ra and $fp in the stack
		addi $fp, $sp, 0		# Copies $sp to $fp
		addi $sp, $sp, -8	# Allocates 8 bytes of space
		
		# Store $ra and $fp in the stack
		sw $ra, 4($sp)		# Saves $ra
		sw $fp, ($sp)		# Saves $fp
		
		# Copy $sp to $fp
		addi $fp, $sp, 0		# $fp = $sp
		
		# Allocate space for local variables
		addi $sp, $sp, -12	# 3 local variables space allocated. (4 * 3) = 12
		
		# Print "Array length: "
		addi $v0, $0, 4		# Print string
		la   $a0, length_input	# Print length_input
		syscall 
	
		# Read the length of the array
		addi $v0, $0, 5		# Read integer
		syscall 
		addi $t2, $v0, 0		# Store length into t2
	
		# Creating the my_list array
		addi $v0, $0, 9		# Allocate space for array in heap
		sll  $t1, $t2, 2		# t1 = length of array * 2^2
		addi $a0, $t1, 4		# a0 = (length of array * 2^2) + 4
		syscall
		sw   $v0, -12($fp)	# Pointer of array stored in stack. my_list = address of array
		lw   $t0, -12($fp)	# Load pointer my_list into t0
		sw   $t2, ($t0)		# Length of array stored in first slot of array
		
		# Print "Enter the value of n: "
		addi $v0, $0, 4		# Print string
		la   $a0, n_input	# Print n_input
		syscall
	
		# Input value of n and store it into stack
		addi $v0, $0, 5		# Read integer
		syscall
		sw   $v0, -8($fp)	# Store input as n into stack.
		
		# Initilize i in stack
		addi $t0, $0, 0		# i = 0
		sw   $t0, -4($fp)	# Store i into stack
		
		# For loop to enter array elements.
		for:	
			# Loop condition to exit the loop or to continue
			lw  $t0, -4($fp)		# t0 = i
			lw  $t1, -12($fp)	# t1 = address of my_list
			lw  $t2, ($t1)		# t2 = length of my_list
			beq $t0, $t2, exit_loop	# If i == length, jump to exit. Else continue to next instruction
					
			# Print "Enter num: "
			addi $v0, $0, 4		# Print string
			la   $a0, int_input	# Print int_input
			syscall
			
			# Store the inputted value into the array
			lw   $t0, -4($fp)	# t0 = i
			lw   $t1, -12($fp)	# t1 = address of my_list
			sll  $t0, $t0, 2		# t0 = i * 2^2 / multiplying i by 4
			add  $t1, $t1, $t0	# t1 = my_list + (i * 4)
			addi $v0, $0, 5		# Read input
			syscall
			sw   $v0, 4($t1)		# Stores content in v0 into my_list[i]
			
			# Increment i by 1
			lw   $t0, -4($fp)	# t0 = i
			addi $t0, $t0, 1		# i += 1
			sw   $t0, -4($fp)	# Store incremented i by one back into the stack
			j for			# Jump back to for loop to continue looping
		
		# Exit for loop
		exit_loop:
		
			# Print return
			addi $v0, $0, 4		# Print string
			la   $a0, return		# Print return string
			syscall
		
			# Print n
			addi $v0, $0, 1		# Print integer
			lw   $a0, -8($fp)	# Print n by loading it into a0 from the stack
			syscall
		
			# Print return2
			addi $v0, $0, 4		# Print string
			la   $a0, return2	# Print return2 string
			syscall
		
			# Calls get_multiplies(my_list, n) function
			# Push 2 * 4 = 8 bytes of arguements into stack
			addi $sp, $sp, -8	# Allocate addition 8 bytes of space in stack for 2 arguements
		
			# arg1 = my_list
			lw $t0, -12($fp)		# Load my_list into t0
			sw $t0, ($sp)		# Store my_list address into arg1
		
			# arg2 = n
			lw $t0, -8($fp)		# Load n into t0
			sw $t0, 4($sp)		# Store n into arg2
		
			# Link and goto get_multiplies function
			jal get_multiplies	# Jump to get_multiples and save address in $ra
		
			# Remove arguements from stack 
			addi $sp, $sp, 8
		
			# Print result
			addi $a0, $v0, 0		# a0 = v0. v0 is the returned value from the get_multiplies function. Count is printed
			addi $v0, $0, 1		# Print integer
			syscall
		
			# Print new line
			addi $v0, $0 ,4		# Print string
			la   $a0, new_line	# Print new line
			syscall
		
			# Remove local variables
			addi $sp, $sp, 12	# Pop my_list address, n and i off the stack
		
			# Restores $fp and $ra and remove them from stack
			lw $fp, ($sp)		# $fp = $sp. restores $fp
			lw $ra, 4($sp)		# Restores $ra
			addi $sp, $sp, 8		# Push $fp and $ra off the stack
		
			# Exit from main function and return back to caller
			jr $ra			# Jump back to main function using saved return address in $r
	
	##################################################	
	# Stack diagram for get_multiplies function
	# count at -8($fp)	<---- $sp
	# i at -4($fp)
	# Saved $fp at ($fp)	<---- $fp
	# Saved $ra at 4($fp)
	# Arguement 1 (my_list address) at 8($fp)
	# Arguement 2 (n) at 12($fp)
	##################################################
									
	# def get_multiplies
	get_multiplies:
		# Make space and store $ra and $fp in the stack
		addi $sp, $sp, -8	# Allocates 8 bytes of space
		sw   $ra, 4($sp)		# Saves $ra in stack
		sw   $fp, ($sp)		# Saves $fp in stack
		
		# Copy $sp to $fp
		addi $fp, $sp, 0		# $fp = $sp
			
		# Allocate space for local variables
		addi $sp, $sp, -8	# 2 local variables space allocated. (4 * 2) = 8
			
		# Intialise local variables
		addi $t0, $0, 0		# Count = 0
		sw   $t0, -8($fp)	# Store count into stack
		addi $t0, $0, 0		# i = 0
		sw   $t0, -4($fp)	# Store i into stack
		
		for2:	
			# Loop condition to exit the loop or to continue iterating
			lw  $t0, -4($fp)		 # t0 = i
			lw  $t1, 8($fp)		 # t1 = address of my_list
			lw  $t2, ($t1)		 # t2 = length of my_list
			beq $t0, $t2, exit_loop_2 # If i == length, jump to exit. Else continue to next instruction
			
			# If statement
			if:
				# my_list[i] % n
				lw   $t0, 12($fp)	# t0 = n
				lw   $t1, 8($fp)		# t1 = address of array my_list
				lw   $t2, -4($fp)	# t2 = i
				sll  $t2, $t2, 2		# t2 = (i * 2^2), to get index / Multiply i by 4
				add  $t1, $t1, $t2	# t1 = my_list + (i * 4)
				lw   $t2, 4($t1)		# Load the contents of the array at list[i] into t2
				div  $t2, $t0		# my_list[i] % n
				mfhi $t4			# t4 = remainder of (my_list[i] / n)
		
				# If remainder != 0, jump to the i_counter
				bne $t4, $0, i_counter	# Jump to i_counter if remainder not 0, else continue to next instruction
					
				# my_list[i] != n 
				lw   $t0, 12($fp)	# t0 = n
				lw   $t1, 8($fp)		# t1 = address of array my_list
				lw   $t2, -4($fp)	# t2 = i
				sll  $t2, $t2, 2		# t2 = (i * 2^2), to get index / Multiply i by 4
				add  $t1, $t1, $t2	# t1 = my_list + (i * 4)
				lw   $t2, 4($t1)		# Load the contents of array at list[i] into t2
				beq  $t2, $t0, i_counter	# Jump to i_counter if list[i] = n. Else continue to next instruction
		
				# Increment count by 1
				lw   $t0, -8($fp)	# t0 = count
				addi $t0, $t0, 1		# Increment count by 1
				sw   $t0, -8($fp)	# Store the count + 1 into count
		
		i_counter:
			# Increment i by 1
			lw   $t0, -4($fp)	# t0 = i
			addi $t0, $t0, 1		# i += 1
			sw   $t0, -4($fp)	# Store i + 1 back into i
			j for2			# Jump back to the for loop to continue looping
		
		# Exit for loop
		exit_loop_2:
			
			# Return results stored in v0
			lw $v0, -8($fp)		# v0 = count to be returned on function exit
		
			# Remove local variables
			addi $sp, $sp, 8		# Pop count and i off stack
		
			# Restores $fp and $ra and remove them from stack
			lw $fp, ($sp)		# $fp = $sp. restores $fp
			lw $ra, 4($sp)		# Restores $ra
			addi $sp, $sp, 8		# Push $fp and $ra off the stack
		
			# Return back to main function
			jr $ra			# Jump back to main function using saved return address in $ra
