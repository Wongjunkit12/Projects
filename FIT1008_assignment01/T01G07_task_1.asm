# A program to check whether the inputted number can be divided by either an inputted first divisor or an inputted second divisor
# Author: Teoh Tian Zhi, Bryan Wong Jun Kit
# Last Modified: 7/3/2022

# Data section to store variables
.data
	number:			.word 0
	first_divisor:		.word 0
	second_divisor:		.word 0
	divisors:		.word 0
	number_input:		.asciiz "Enter the number: "
	first_divisor_input:	.asciiz "Enter the first divisor: "
	second_divisor_input:	.asciiz "Enter the second divisor: "
	divisors_output:		.asciiz "\nDivisors: "
	new_line:		.asciiz "\n"
	
# Instructions set begins here
.text
	# Printing the statement for number
	addi $v0, $0, 4			# v0 = 4 to print string
	la   $a0, number_input		# Load address of number_input into a0 to be printed
	syscall
	
	# Read the integer input and store it in number
	addi $v0, $0, 5			# v0 = 5 to read integer input
	syscall
	sw   $v0, number			# Store it in number under .data
	
	# Printing the statement for first_divisor
	addi $v0, $0, 4			# v0 = 4 to print string
	la   $a0, first_divisor_input	# Load address of first_divisor_input into a0 to be printed
	syscall
	
	# Read the integer input and store it in first_divsor
	addi $v0, $0, 5			# v0 = 5 to read integer input
	syscall
	sw   $v0, first_divisor		# Store it in first_divisor under .data
	
	# Printing the statement for second_divisor
	addi $v0, $0, 4			# v0 = 4 to print string
	la   $a0, second_divisor_input	# Load address of second_divisor_input into a0 to be printed
	syscall
	
	# Read the integer input and store it in second_divisor
	addi $v0, $0, 5			# v0 = 5 to read integer input
	syscall
	sw $v0, second_divisor		# Store it in second_divisor under .data
	
	# Set divisorss to 0
	addi $t0, $0, 0			# t0 = 0
	sw   $t0, divisors		# Set value of divisorss to be 0
	
	# If (number % first_divisor == 0 and number % second_divisor == 0)
	if:  
		# First condition (number % first_divisor == 0)  
		lw   $t0, number		# t0 = number
		lw   $t1, first_divisor	# t1 = first divisors
		div  $t0, $t1		# number / first_divisor 
		mfhi $t2			# t2 = number % first_divisor as mlfhi contain the remainder
		bne  $t2, $0, elif	# If remainder != 0 the jump to elseif otherwise continue to next instruction
		
		# Second condition (number % second_divisor == 0)
		lw   $t0, number		# t0 = number
		lw   $t1, second_divisor	# t1 = second_divosor
		div  $t0, $t1		# number / second_divisor
		mfhi $t2			# t2 = number % second_divisor as mlfhi contain the remainder
		bne  $t2, $0, elif	# If remainder != 0 the jump to elseif otherwise continue to next instruction

		# Set divisors to 2 if statements above are both true
		lw   $t0, divisors	# t0 = divisors
		addi $t0, $0, 2		# divisors = 2 + 0
		sw   $t0, divisors	# Store t0 = 2 back to divisors so divisors = 2
		j end_if			# Jump to end_if and skip elif and else
		
	# Else if (number % first_divisor == 0 or number % second_divisor == 0)
	elif:
		# First condition (number % first_divisor == 0) 
		lw   $t0, number		# t0 = number
		lw   $t1, first_divisor	# t1 = first divisors
		div  $t0, $t1		# number / first_divisor
		mfhi $t2			# t2 = number % first_divisor as mlfhi contain the remainder
		beq  $t2, $0, elif_block	# If remainder == 0, jump to elif_block.
		
		# Second condition (number % second_divisor == 0)
		lw   $t0, number		# t0 = number
		lw   $t1, second_divisor	# t1 = second_divosor
		div  $t0, $t1		# number / second_divisor
		mfhi $t2			# t2 = number % second_divisor as mfhi contain the remainder
		beq  $t2, $0, elif_block	# If remainder == 0, jump to elif_block. Else, continue to next instruction
		j else			# Jump to else
		
		# Set divisors to 1 if either of the statements above is true
		elif_block: 		
			lw    $t0, divisors	# t0 = divisors
			addi  $t0, $0, 1		# divisors = 1 + 0
			sw    $t0, divisors	# Store t0 into divisors so divisors = 1   
			j end_if			# Jump to end_if
	
	# Else if none of the two statements above are executed
	else:
		# Set divisors to 0
		lw   $t0, divisors	# t0 = divisors
		addi $t0, $0, 0		# divisors = 0 + 0
		sw   $t0, divisors	# divisors = 0
	
	# Exit if statement and print divisors_outputs before terminating the program
	end_if:
		# Print divisors_output
		addi $v0, $0, 4		 # v0 = 4 to print string
		la   $a0, divisors_output # Load address of divisors_output into a0 to print string
		syscall
	
		# Print 	divisors
		addi $v0, $0, 1		# v0 = 1 to print integer
		lw   $a0, divisors	# Load divisors into a0 to print divisors
		syscall
	
		# Print new line
		addi $v0, $0, 4		# v0 = 4 to print string
		la   $a0, new_line	# Load address of new_line into a0 to print new line
		syscall
		
		# Exit the program
		addi $v0, $0, 10		# v0 = 10 to exit the program
		syscall
