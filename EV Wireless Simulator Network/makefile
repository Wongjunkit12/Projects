all: compile run

compile: assignment02.c
	mpicc -Wall -o Assignment02_Output assignment02.c -lpthread -lmpi -lm -fstack-protector-all

run:
	mpirun -oversubscribe -np 7 Assignment02_Output 2 3
	
clean :
	rm -f Assignment02_Output
	rm -f log.txt
	clear