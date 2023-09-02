# A program to sort an array from smallest to largest using insertion sort algorithm
# Author: Teoh Tian Zhi
# Last Modified: 13/3/2022

# global function 
.globl	main
.globl	insertion_sort

# data section to store variables
.data
	length_msg :	.asciiz "Array length: "
	enter_msg :	.asciiz "Enter num: "
	nl:		.asciiz "\n"
	space:		.asciiz " "
	
# instruction set begins here
.text	
	# jump to main function and save address into $ra
	jal main			# jump to main
	
	# terminate the program
	addi $v0, $0, 10		# v0 = 10 to exit program
	syscall	
	
	# def main():
	main:	
###########################################################
#stack diagram for def main()
#	#content					address   
#	address of array		<--$sp		-8($fp1)
#	i					-4($fp1)
#	$fp			<-- $fp1		0($fp1)
#	$ra (jal main)				4($fp1)
#	sp first address		<--$fp
###########################################################
		# save $sp into $fp and allocate space for $fp and $ra
		add  $fp, $0, $sp	# fp = $sp
		addi $sp, $sp, -8	# allocates 8 bytes of space
		
		# store $ra and $fp in the stack
		sw $ra, 4($sp)		# saves $ra
		sw $fp, ($sp)		# saves $fp
		
		# copy $sp to $fp
		addi $fp, $sp, 0		# $fp = $sp
		
		# allocate space for local variable
		addi $sp, $sp, -8
		
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
		sw  $t0, ($v0) 		# length of array stored in first slot of array
		sw  $v0, -8($fp)		# store address into the stack
	
		# intializing the i counter in the loop and store it into the stack
		addi $t1, $0, 0 		# $t1 = i = 0
		sw   $t1, -4($fp) 	# store i counter in into the stack
		
	
		# initializing the array	
		for :   
			# loop conditions
			lw  $t0, -8($fp)	# t0 = address of array
			lw  $t1, ($t0)		# t1 = length of array
			lw  $t0, -4($fp)		# t0 = i
			beq $t0, $t1, exitloop	# if $t0(counter) == $t1(size), exit the loop
			
			# print "Enter num: "
			addi $v0, $0, 4		# print string
			la   $a0, enter_msg	# print int_input
			syscall
		
			# find the address of the index
			lw   $t0, -4($fp)	# t0 = i
			lw   $t2, -8($fp) 	# $t2 = address of array
			sll  $t0, $t0, 2 	# $t0 = i * 2^2
			add  $t2, $t2, $t0 	# $t2 = address + i * 2^2
			addi $t2, $t2, 4 	# $t2 = (address + i* 2^2) + 4
			addi $v0, $0, 5		# Read input
			syscall
			sw   $v0, ($t2) 		# store number
			
			# increase counter
			lw   $t0, -4($fp)	# $t0 = i
			addi $t0, $t0, 1		# $t0 = i+1
			sw   $t0, -4($fp)	# store the incremented i into the stack
			j for 			# jump back to the loop
			
		exitloop:
			# call insertion_sort function and push arguements into stack
			addi $sp, $sp, -4	# allocate addition 4 bytes of space in stack for 1 arguement
			
			# arg1 = arr
			lw $t0, -8($fp)
			sw $t0, ($sp)
			
			# jump into insertion_sort function			
			jal insertion_sort
			
			# remove arguements from stack 
			addi $sp, $sp, 4		# only one argument
			
			#reset i to 0 for printing
			lw $t0, -4($fp)		# $t0=i
			add $t0, $0, $0		# $t0=0+0
			sw $t0, -4($fp)		# store it back in stack
			
		# printing for loop to print array contents
		for_print:
			# for loop condition
			lw $t1, -4($fp)			# load the i to $t0
			lw $t0, -8($fp)			# load the address of array to $t1
			lw $t0, ($t0)			# $t0=array length
			slt $t0, $t1, $t0 	 	# if i< length $t1< $t0   $t0=1 else = 0
			beq $t0, $0, exit_for_print	# if $t0=0 then i no longer < length so exit for print
				
			# find list[i]
			lw $t1, -4($fp)			# load the i to $t0
			lw $t0, -8($fp)			# load the address of array to $t1
			sll $t1, $t1, 2			# $t1 = i * 2^2
			add $t0, $t1, $t0		# $t0 = address + i * 2^2		
			addi $t0, $t0, 4			# $t0= $t0 +4 
			lw $t1, ($t0)			# load the content of [i] to $t1
			
			# print the contents of list[i]
			addi $v0, $0, 1			# print int
			add $a0, $t1, $0   		# $a0=$t1
			syscall
				
			# print space for end = " "
			addi $v0, $0, 4  		# print int
			la   $a0, space			# $a0=space
			syscall
			
			#increment
			lw $t0, -4($fp)			# $t0=i
			addi $t0, $t0, 1			# $t0+=1
			sw $t0, -4($fp)			# store it back in stack
			
			# continue looping
			j for_print
			
			# exit for_print loop
			exit_for_print:	
				# remove local variables
				addi $sp, $sp, 8		# pop array, i the stack
			
				# restores $fp and $ra and remove them from stack
				lw $fp, ($sp)		# $fp = $sp. restores $fp
				lw $ra, 4($sp)		# restores $ra
				addi $sp, $sp, 8		# push $fp and $ra off the stackk
		
				# exit from main function and return back to caller
				jr $ra			# jump back to main function using saved return address in $ra
		
		#def insertion:
		insertion_sort:
			# initialise the $fp for insertion
			addi $sp, $sp, -8 	# allocate space to store the $fp from main and $ra for jal insertion_sort and also the address of array
			sw   $ra, 4($sp) 	# store the $ra for jal insertion 
			sw   $fp, ($sp) 	 	# store the $fp to the stack
			
			# copy $sp to $fp
			addi $fp, $sp, 0		# $fp = $sp
			
			addi $sp, $sp, -16	# allocate 4 space for 4 local variable in insertion_sort
			
			# length=len(my_list)
			lw $t0, 8($fp)		# $t0 = addr of array
			lw $t1, ($t0)		# length of array is $t1
			sw $t1, -16($fp)		# store the length
			
			# initialise the i=1
			addi $t0, $0, 1		# for the insertion_sirt for loop the i start with 1
			sw   $t0, -12($fp)	# store the i
			
			# declare the key
			addi $t0, $0, 0   	# key is still unknown so we initialise it as 0
			sw   $t0, -8($fp)	# store the key	

			# declare the j
			addi $t0, $0, 0   	# j is still unknown so we initialise it as 0
			sw   $t0, -4($fp)	# store the j	
			
			# for: but use different label as for was already used above at main
			insertion_sort_for:
###############################################################
#stack diagram for def insertion_sort(my_list):
#	content						address
#	length				<-- $sp		-16($fp2)
#	i						-12($fp2)
#	key						-8($fp2)
#	j						-4($fp2)
#	$fp1(the $fp thats in main)	<-- $fp2 	0($fp2)
#	$ra (jal main)					4($fp2)
#	address of array (argument for insertion)		8($fp2)        
###############################################################	
		
				lw $t0, 8($fp)		# $t0 = load the address of array
				lw $t1, ($t0)		# $t1= length of array
				lw $t0, -12($fp)		# load the i to $t0
				
				# loop condition check
				slt $t0, $t0, $t1  			# i < length  $t0 =1 else $t0=0
				beq $t0, $0, exit_insertion_sort_for	# if $t0=0 means i >=length then jump to exit for loop
				
				# content of for loop
				# key = my_list[i]
				lw   $t0, 8($fp)  	# load the address of array
				lw   $t1, -12($fp)	# load the i to $t0
				sll  $t1, $t1, 2		# $t1 = i * 2^2
				add  $t0, $t1, $t0	# $t0 = address + i * 2^2		
				addi $t0, $t0, 4		# $t0= $t0 +4 
				lw   $t1, ($t0)		# load the content of [i] to $t1
				sw   $t1, -8($fp)	# store the key to stack
				
				# j=i-1
				lw   $t1, -12($fp)	# load the i to $t0
				addi $t1, $t1, -1	# $t1= $t1-1  j=i-1
				sw   $t1, -4($fp)
				
				# while loop
				insertion_sort_while:
					# whileloop condition
					# j>=0
					lw  $t0, -4($fp)				# load the j to $t0
					slt $t0, $t0, $0  			# if j<0 means j nolonger j>=0
					bne $t0, $0, exit_insertion_sort_while	# if $t0=0 means j already<0 so exist the while loop
					
					# key < the_list[j]
					# find the_list[j]
					lw   $t0, 8($fp)		# load the address of array
					lw   $t1, -4($fp)	# load the j to $t0
					sll  $t1, $t1, 2		# $t1 = j * 2^2
					add  $t0, $t1, $t0	# $t0 = address + j * 2^2		
					addi $t0, $t0, 4		# $t0= $t0 +4 
					lw   $t1, ($t0)		# load the content of [j] to $t1
					
					# load the key
					lw $t0, -8($fp)  	# load key to $t0
					
					# compare
					slt $t0, $t0, $t1  			# if key<list[j], then $t0=1, else $t0=0
					beq $t0, $0, exit_insertion_sort_while	# if $t0=0 means list[j]<0 so exist the while loop
					
					# while loop content
					# the_list[j + 1] = the_list[j]
					# find the_list[j]
					lw   $t0, 8($fp)		# load the address of array
					lw   $t1, -4($fp)	# load the j to $t1
					sll  $t1, $t1, 2		# $t1 = j * 2^2
					add  $t0, $t1, $t0	# $t0 = address + j * 2^2		
					addi $t0, $t0, 4		# $t0= address+(j*2^2) +4 
					lw   $t1, ($t0)		# load the content of [j] to $t1

					
					# store the j and j+1 to each others location (swap place)
					# [j] to [j+1] address
					lw   $t0, 8($fp)		# load the address of array
					lw   $t3, -4($fp)	# load the j to $t3
					addi $t3, $t3, 1		# j=j+1
					sll  $t3, $t3, 2		# $t3 = (j+1) * 2^2
					add  $t0, $t3, $t0	# $t0 = address + (j+1) * 2^2		
					addi $t0, $t0, 4		# $t0= $t0 +4 
					sw   $t1, ($t0)		# store j+1
					
					# j-=1 increment
					lw    $t0,-4($fp)	#load j to $t0
					addi, $t0,$t0,-1		# j-=1
					sw    $t0, -4($fp)	# store the changed j back
		
					j insertion_sort_while	# jump back to while loop to continue looping
					
					exit_insertion_sort_while:
						# key=[j+1] reassign key
						# find the_list[j+1]
						lw   $t0, 8($fp)		# load the address of array
						lw   $t2, -4($fp)	# load the j to $t2
						addi $t2, $t2, 1		# j=j+1
						sll  $t2, $t2, 2		# $t2 = j * 2^2
						add  $t0, $t2, $t0	# $t0 = address + j * 2^2		
						addi $t0, $t0, 4		# $t0= $t0 +4 
						lw   $t2, -8($fp)  	# load the key
						sw   $t2, ($t0)		# store it to the [j+1] address
						
						# increment for i
						lw   $t0, -12($fp)	# load the i to $t0
						addi $t0, $t0, 1		# i+=1
						sw   $t0, -12($fp)	# store it back after increment
						
						# jump back to for loop to continue looping for loop
						j insertion_sort_for
						
				exit_insertion_sort_for:
					# remove local variables
					addi $sp, $sp, 16   	# remove that 4 local variable j, key, length and i
					
					# restores $fp and $ra and remove them from stack
					lw   $fp, ($sp)		# $fp = $sp. restores $fp
					lw   $ra, 4($sp)		# restores $ra
					addi $sp, $sp, 8		# push $fp and $ra off the stackk
					
					jr $ra #return to the caller of jal insertion_sort
					
		
####################################################################################################
#stack diagram for def insertion_sort(my_list):
#	content						address
#	length				<-- $sp		-16($fp2)
#	i						-12($fp2)    
#	key						-8($fp2)
#	j						-4($fp2)
#	$fp1(the $fp thats in main)	<-- $fp2		0($fp2)
#	$ra (jal main)					4($fp2)
#	address of array (argument for insertion)		8($fp2)  
#	address of array					-8($fp1)
#	i						-4($fp1)
#	$fp				<-- $fp1		0($fp1)/when need to use it used ($sp)
#	$ra (jal main)					4($fp1)/when need to use it used 4($sp)
#	sp first address			<-- $fp          
####################################################################################################			
