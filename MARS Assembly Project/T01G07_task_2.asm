# A program to count the number of multiplies of n (excluding itself) in an array using gloval variables.
# Author: Toh Thien Yew
# Last Modified: 9/3/2022

#data section to store variables
.data
	size_input:	.asciiz "Enter array length: "
	n_input:		.asciiz "Enter n: "
	int_input:	.asciiz "Enter the value: "
	return:		.asciiz "\nThe number of multiples (excluding itself) = "
	new_line:	.asciiz "\n"
	
	size:		.word 0
	the_list:	.word 0
	n:		.word 0
	count:		.word 0
	i:		.word 0
	
#instructions set begins here
.text 				
	#print "Enter array length: "
	addi $v0, $0, 4		#print string
	la   $a0, size_input	#print size_input
	syscall 
	
	#let size = int(input("Enter array length: "))
	addi $v0, $0, 5		#read integer
	syscall 
	sw   $v0, size		#store in size
	
	#creating an array
	addi $v0, $0, 9		#allocate space
	lw   $t0, size		
	sll  $t1, $t0, 2		#size * 2^2
	addi $a0, $t1, 4		#(size *  2^2) + 4
	syscall
	sw   $v0, the_list	#pointer to array
	sw   $t0, ($v0)		#length of array stored in first slot of array
	
	#print "Enter n: "
	addi $v0, $0, 4		#print string
	la   $a0, n_input	#print n_input
	syscall
	
	#let n = int(input("Enter n: "))
	addi $v0, $0, 5		#read integer
	syscall
	sw   $v0, n		#store in n
	
	#for loop to enter array elements.
	for:	
		#loop conditions
		lw  $t0, i		#t0 = i
		lw  $t1, size		#t1 = size
		beq $t0, $t1, exit	#if i == size, jump to exit. Else continue to next instruction
					
		#print "Enter the value: "
		addi $v0, $0, 4		#print string
		la   $a0, int_input	#print int_input
		syscall
		
		#the_list[i] = int_input
		lw   $t0, the_list	#t0 = address of array the_list	
		lw   $t1, i		#t1 = i
		sll  $t1, $t1, 2		#i * 2^2 / multiplying i by 4
		add  $t0, $t0, $t1	#the_list + (i * 4)
		addi $t0, $t0, 4		#add 4 to (the_list + i * 4)
		addi $v0, $0, 5		#read input
		syscall
		sw   $v0, ($t0)		#stores content in v0 into the_list[i]
		
		#if the_list[i] % n == 0 and the_list[i] != n:
		if:
			#the_list[i] % n
			lw   $t0, n		#t0 = n
			lw   $t1, the_list	#t1 = address of array the_list
			lw   $t2, i		#t2 = i
			sll  $t2, $t2, 2		#i * 2^2, to get index / Multiply i by 4
			add  $t1, $t1, $t2	#the_list + (i * 4)
			lw   $t3, 4($t1)		#load the contents of the array at list[i]
			div  $t3, $t0		#the_list[i] % n
			mfhi $t5			#t5 = remainder of (the_list[i] / n)
		
			#to check if remainder == 0
			bne $t5, $0, i_counter	#jump to i_counter if remainder != 0. Else continue to next instruction
					
			#check if list[i] != n 
			lw   $t0, n		#t0 = n
			lw   $t1, the_list	#t1 = address of array the_list
			lw   $t2, i		#t2 = i
			sll  $t2, $t2, 2		#i * 2^2, to get index / Multiply i by 4
			add  $t1, $t1, $t2	#the_list + (i * 4)
			lw   $t3, 4($t1)		#load the contents of array at list[i]
			beq  $t3, $t0, i_counter	#Jump to i_counter if list[i] == n
		
			#increment count by 1
			lw   $t0, count		#t0 = count
			addi $t0, $t0, 1		#increment count by 1
			sw   $t0, count 		#store the count + 1 into count
	
	#i += 1	
	i_counter:
		#to increment i by 1
		lw   $t0, i		#t0 = i
		addi $t0, $t0, 1		#i += 1
		sw   $t0, i		#store incremented i by one back into i
		j for			#jump back to the for loop to continue looping
	
	#exitted for loop	
	exit:	
		#print return
		addi $v0, $0, 4		#print "\nThe number of multiples (excluding itself) = "
		la   $a0, return		#print return
		syscall
		
		#print i_counter
		addi $v0, $0, 1		#print integer
		lw   $a0, count		#print count
		syscall
		
		#print new line
		addi $v0, $0, 4		#print string
		la   $a0, new_line	#print new_line
		syscall
		
		#exit the program
		addi $v0, $0, 10		#terminate the program
		syscall