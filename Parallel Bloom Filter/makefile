CC = gcc
CFLAGS = -lm
OMPFLAGS = -fopenmp

all: serial parallel

serial: SerialBloomFilter.c
	$(CC) -o serial SerialBloomFilter.c $(CFLAGS)

parallel: ParallelBloomFilter.c
	$(CC) -o parallel ParallelBloomFilter.c $(OMPFLAGS) $(CFLAGS)

run:
	./serial
	@echo ""
	./parallel
	@echo ""

clean :
	rm -f serial parallel