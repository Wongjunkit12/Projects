#include <ctype.h>
#include <math.h>
#include <stdbool.h>
#include <stdio.h>
#include <stdint.h>
#include <stdlib.h>
#include <string.h>
#include <time.h>

#define INITIAL_M_SIZE 1000
#define MAX_FALSE_POSITIVE_RATE 0.05
#define HASH_FUNCTIONS 3
#define MAX_WORD_LENGTH 100


int readFile(const char *filename, int fileLength, char ***pUniqueWordsList) {
	/**
	* This function reads the contents of a specified text file and identifies unique words in it. It allocates memory 
	* to store these unique words and returns the count of unique words found. The unique words are stored as C-style 
	* strings (char arrays) in a dynamically allocated array and are converted to lowercase for case-insensitive comparison.
	*
	* @param filename: The name of the text file to be read.
	* @param fileLength: The estimated maximum number of unique words in the file.
	* @param pUniqueWordsList: A pointer to a char** that will store the array of unique words.
	*
	* @return: The number of unique words found in the file. If an error occurs, it returns -1.
	*/
	FILE *file = fopen(filename, "r");		// Open the file for reading.
	
	// If no file found, print error and exit code.
	if (file == NULL) {
		// Print an error message if the file cannot be opened.
		perror("Error opening file");
		return -1;		// Return -1 to indicate an error.
	}

	char word[MAX_WORD_LENGTH];			// Declare a character array to store a word (assumes a maximum word length).
	int count = 0;						// The number of unique words in the file.
	char **pUniqueWords = NULL;			// Declare an array to store unique words. Will be an array of strings.

	// Allocate memory for pUniqueWords based on fileLength.
	pUniqueWords = (char**)malloc(fileLength * sizeof(char *));

	// Continue looping until end of file.
	while (fscanf(file, "%s", word) == 1) {
		bool isUnique = true;		// Boolean variable to determine if word is unique or not. True by default.
		int i = 0;					// Initialise i (loop control variable) to zero.

		// Loop through the pUniqueWords list to determine if the current word is unique or not.
		while ((i < count) && (isUnique)) {
			// Compare the current word with each word in the pUniqueWords array. If found, set isUnique to false to exit while loop.
            if (strcmp(word, pUniqueWords[i]) == 0) {
                isUnique = false;
            }

			i++;	// Increment i by one to continue looping.
        }

		// If the word is unique, add it to the pUniqueWords list.
		if (isUnique) {
			pUniqueWords[count] = strdup(word);		// Duplicate the word and add it the to pUniqueWords list.
			count++;								// Increment count by one due to unique word found.
		}
	}

	fclose(file);		// Close the file.

	// Resize pUniqueWords and remove all the blank cells.
	pUniqueWords = realloc(pUniqueWords, count * sizeof(char *));

	// Update the pUniqueWordsList pointer to the unique word list.
	*pUniqueWordsList = pUniqueWords;

	// Return the number of unique words found.
	return count;
}

int initialiseBitArray(int n, int m, int **bitarray) {
	/**
	* This function initialize a Bloom filter's bit array by calculating the optimal size of the bit array based on the desired
	* false positive rate and the expected number of elements to be inserted into the Bloom filter. 
	* It then allocates memory for the bit array and sets it to zero.
	*
	* @param n: The expected or maximum number of elements to be inserted into the Bloom filter.
	* @param m: The calculated optimal size of the bit array, returned by the function.
	* @param bitarray: A pointer to the allocated bit array. The function assigns the memory
	*                  address of the bit array to this pointer.
	*
	* @return: The size of the allocated bit array (m).
	*/
	int k = HASH_FUNCTIONS;				// k will be the same as HASH FUNCTIONS.
	double fpr = 0;						// Initalise False Positive Rate to 0 initally.

	// Calculate the False Positive Rate based on input n.
	fpr = (pow((1 - pow((1 - (1.0/m)), (n * k))), k)) / 100;

	// Calculate optimum m.
	m = (int) ceil(-(n * log(fpr)) / (pow(log(2), 2)));

	// Allocate memory for bitarray based on m.
	*bitarray = (int *)calloc(m, sizeof(int));

	return m;		// Return size of the bitarray.
}

// uint32_t is a numeric type that guarantees 32 bits. The value is unsigned, meaning that the range of values goes from 0 to 2^(32 - 1).

uint32_t jenkinsHashFunction(const char *key, int length) {
	/**
	* This functions calculates a 32-bit hash value for a given input key using the Jenkins Hash function. It iterates over 
	* each character in the input key. For each character, it adds the character's value to the current hash value. After 
	* adding the character, the hash value undergoes a series of bitwise mixing operations using predefined constants 
	* (C_1, C_2, C_3, C_4, C_5) to ensure better distribution of hash values.
	*
	* @param key: The input key, represented as a string.
	* @param length: The length of the input key (number of characters) to be considered for hashing.
	* @return: A 32-bit unsigned integer representing the computed hash value for the input key.
	*/
	// Constant values used during the hashing process.
	const int C_1 = 10;
	const int C_2 = 6;
	const int C_3 = 3;
	const int C_4 = 11;
	const int C_5 = 15;

    uint32_t hash = 0;          		// Initialize the hash value to zero.

	// Loop through the input key until the end of the key is reached.
	for (int i = 0; i < length; i++) {
        hash += (uint32_t) key[i];  	// Add the current byte of the key to the hash value and increment 'i'.

        // Mix the bits of the hash value using bitwise operations and constants:
        hash += hash << C_1;   			// Left-shift the hash value by 10 bits and add it to the hash.
        hash ^= hash >> C_2;   			// Right-shift the hash value by 6 bits and XOR it with the hash.
    }

	// Additional mixing hashing operations.
    hash += hash << C_3;       			// Perform another left-shift by 3 bits and add it to the hash.
    hash ^= hash >> C_4;     			// Perform another right-shift by 11 bits and XOR it with the hash.

    hash += hash << C_5;   				// Perform a final left-shift by 15 bits and add it to the hash.

    return hash;                		// Return the computed hash value.
}

uint32_t fowlerNollVoHash(const char *key, int length) {
	/**
	* Calculates a 32-bit hash value for a given input key using the Fowler-Noll-Vo (FNV) Hash function.
	*
	* The function initializes the hash value with an initial constant value (2166136261U) and uses
	* a prime constant (FNV_PRIME = 16777619U) for multiplication. It then iterates through each byte
	* of the input key. For each byte, it performs the following operations:

	* 1. XOR: The current byte of the input data is XORed with the current hash value.
	*    This step helps in introducing the characteristics of the input data into the hash value.
	*
	* 2. Multiply: The hash value is then multiplied by the FNV prime constant (FNV_PRIME).
	*    Multiplication by a prime constant helps ensure a better distribution of hash values.
	*
	* This process is repeated for each byte in the input key, leading to a final computed hash value
	* that encapsulates the characteristics of the entire key.

	* @param key: The input key, represented as a string.
	* @param length: The length of the input key (number of characters) to be considered for hashing.
	* @return: A 32-bit unsigned integer representing the computed hash value for the input key.
	*/
    const uint32_t FNV_PRIME = 16777619U;    	// Prime constant for multiplication.
    uint32_t hash = 2166136261U;				// Initialize the hash with a constant value.

    // Iterate through each byte of the input data
    for (int i = 0; i < length; i++) {
        hash ^= (uint32_t) key[i];	// XOR the current byte of the input data with the hash value
        hash *= FNV_PRIME;			// Multiply the hash value by the FNV prime constant
    }

    return hash;		// Return the final computed hash value
}

uint32_t hash_djb2(const char* key, int length) {
	/**
	* Calculates a 32-bit hash value for a given input key using the DJB2 Hash function.
	*
	* The function initializes the hash value with an initial constant value (5381), which has been
	* chosen for its effectiveness in minimizing collisions. It then iterates through each character
	* in the input key. For each character, it performs the following operations:
	*
	* 1. Left Shift and Addition: The current hash value is left-shifted by 5 bits and then added to
	*    itself. This operation effectively multiplies the hash value by 33 (which is 32 + 1) and
	*    adds the ASCII value of the current character. The result is used to update the hash value.
	*
	* This process is repeated for each character in the input key, resulting in a final computed
	* hash value that captures the characteristics of the entire key.
	*
	* @param key: The input key, represented as a string.
	* @param length: The length of the input key (number of characters) to be considered for hashing.
	* @return: A 32-bit unsigned integer representing the computed hash value for the input key.
	*/
    uint32_t hash = 5381; 	// Initial hash value, which is set at 5381 as it has the fewest collisions.
    int c; 					// Variable to hold each character of the input string

    // Iterate through each character in the input string
    while ((c = *key++)) {
        // Update the hash value using the DJBX33A formula: hash * 33 + character
        hash = ((hash << 5) + hash) + c; // Equivalent to: hash = hash * 33 + c
    }

    // Return the final computed hash value as a 32-bit unsigned integer
    return hash;
}

void insertKey(const char* key, int m, int *bitarray) {
	/**
	* Inserts a given key into a bit array using multiple hash functions. Eemploys multiple hash 
	* functions to generate different hash values for the input key and sets the corresponding bits in the bit array to 1 
	* to indicate the presence of the key.
	*
	* @param key: The input key to be inserted, represented as a string.
	* @param m: The size of the bit array, indicating the number of bits in the array.
	* @param bitarray: The bit array where the bits corresponding to the hash values of the key will be set to 1.
	* @return: void (no return value).
	*/
	// Calculate the hash value.
	uint32_t hashValue1 = jenkinsHashFunction(key, strlen(key));
	uint32_t hashValue2 = fowlerNollVoHash(key, strlen(key));
	uint32_t hashValue3 = hash_djb2(key, strlen(key));

	// Flip the bit array at the index by one.
	bitarray[hashValue1 % m] = 1;
	bitarray[hashValue2 % m] = 1;
	bitarray[hashValue3 % m] = 1;
}

bool lookUpKey(const char* key, int m, int *bitarray) {
	/**
	* Looks up a given key in a bit array using multiple hash functions. It employs multiple hash functions to generate
	* different hash values for the input key and checks if the corresponding bits in the bit array are set to 1, 
	* which would indicate a potential presence of the key.
	*
	* @param key: The input key to be looked up, representing a string.
	* @param m: The size of the bit array, indicating the number of bits in the array.
	* @param bitarray: The bit array where the bits corresponding to the hash values of the key are set to 1 and the rest are 0.
	* @return: A boolean value indicating whether the key is likely to be present.
	*/
	// Calculate the hash value.
	uint32_t hashValue1 = jenkinsHashFunction(key, strlen(key));
	uint32_t hashValue2 = fowlerNollVoHash(key, strlen(key));
	uint32_t hashValue3 = hash_djb2(key, strlen(key));

	// If the key exists inside the bitarray (all ones), then it's a most likely yes.
	if (bitarray[hashValue1 % m] == 1 && bitarray[hashValue2 % m] == 1 && bitarray[hashValue3 % m] == 1) {
		return true;
	}
	// Else it doesn't exist (at least one zero) so it's a no.
	else {
		return false;
	}
}

double query(const char* filename, int m, int n,int *bitarray, char **totalUnique) {
	/**
	* This function reads words from an input file, performs lookups in a bit array to check for
	* word presence using a Bloom Filter, and writes the results to an output file (query.txt). 
	* It is used to check the likelihood of word existence in a dataset represented by the bit array.
	*
	* @param filename: The name of the input file to be queried.
	* @param m: The size of the bit array, indicating the number of bits in the array.
	* @param bitarray: The bit array where the bits corresponding to hash values of words are set to 1.
	*/
	FILE *file1 = fopen(filename, "r");			// Open the file for reading.
	FILE *file2 = fopen("output.txt", "w");		// Open the file for writing.

	char word[MAX_WORD_LENGTH];			// Declare a character array to store a word (assumes a maximum word length).
	int found;							// Integer value indicating if word is found in the bitarray. 1 if high probabilty, 0 if no.
	int result;							// The query result. 1 means probably found and 0 means not found.
	bool hasFound;						// Boolean to determine if the word is within the totalUnique string array.
	int i;								// Declare loop counter i.
	double falsePositiveCount = 0.0;	// An integer value representing the number of false positives.

	// Continue looping until end of file.
	while (fscanf(file1, "%s %d", word, &result) != EOF) {
		// Set hasFound to false to signify word has not been found yet.
		hasFound = false;
		i = 0;
		// Lookup the word in the bitarray and get the result. 1 if high probability, 0 for not matched.
		found = lookUpKey(word, m, bitarray);

		// Loop through the totalUnique list to determine if the current word exist or not.
		while ((i < n) && (!hasFound)) {
			// Compare the current word with each word in the totalUnique array. If found, set hasFound to true to exit while loop.
            if (strcmp(word, totalUnique[i]) == 0) {
                hasFound = true;
            }

			i++;	// Increment i by one to continue looping.
        }

		// If the word has not been inserted into the bitarray yet it's "found", meaning it's a false positive.
		if (!hasFound && found) {
			falsePositiveCount++;		// Increment the falsePositiveCount by one.
		}
		
		// Write the query word and 0 or 1 to the output file2.
		fprintf(file2, "%s %d\n", word, found);
	}

	// Close the files.
	fclose(file1);
	fclose(file2);

	// Delete the original query file and rename the output.txt file to the filename.
    remove(filename);
    rename("output.txt", filename);

	return falsePositiveCount;		// Return the falsePositiveCount to caller.
}

int main() {
	// Name of files to read.
	char *fileNames[3] = {"MOBY_DICK.txt", "LITTLE_WOMEN.txt", "SHAKESPEARE.txt"};
	int fileLengths[3] = {215724, 195467, 965465};
	// List of varying sizes of queries.
	char *queryNames[3] = {"query_small.txt", "query_medium.txt", "query_large.txt"};
	int queryLengths[3] = {25000, 50000, 75000};

	char **ppUniqueWordLists[3] = {0};	// Array to store pointers to unique word lists.
	int uniqueWordListLength[3] = {0};	// Array to store the lengths of unique word lists.
	int *bitarray = NULL;				// Bitarray to be used in bloom filter.
	char **totalUnique = NULL;			// Array containing all the words to insert.

	int i;						// Declare i as loop counters.	
	int n = 0;					// Total count of unique words across all files.
	int m = INITIAL_M_SIZE;		// m initially set to 1000.
	double falsePositiveCount;	// The number of False Positives done by the Bloom Filter.
	double accuracy;			// The accuracy of the  Bloom Filter.

	// Query Name and length chosen.
	char *queryName = queryNames[2];
	int queryLength = queryLengths[2];

	// Measure the computational time.
	struct timespec start, end, startComp, endComp; 
	double time_taken; 

	// Get current clock time for the start.
	clock_gettime(CLOCK_MONOTONIC, &start);
    // Get current clock time for startComp.
	clock_gettime(CLOCK_MONOTONIC, &startComp);

	// For loop to read each file and calculate the total number of unique words n.
	for (i = 0; i < 3; i++) {
		// Call the readFile function to read words from files and get the number of unique words in that file and the 
		// list of unique words.
		uniqueWordListLength[i] = readFile(fileNames[i], fileLengths[i], &ppUniqueWordLists[i]);
		n += uniqueWordListLength[i];	// Increment n to calculate the sum of unique words across all files.
	}

	// Allocate enough memory to hold the total unique words from each file.
	totalUnique = (char**)malloc(n * sizeof(char *));

	// Copy the elements from each nested array to the totalUnique array to flatten it into one array.
	memcpy(totalUnique, ppUniqueWordLists[0], uniqueWordListLength[0] * sizeof(char *));
	memcpy(totalUnique + uniqueWordListLength[0], ppUniqueWordLists[1], uniqueWordListLength[1] * sizeof(char *));
	memcpy(totalUnique + uniqueWordListLength[0] + uniqueWordListLength[1], ppUniqueWordLists[2], uniqueWordListLength[2] * sizeof(char *));

	// Get the clock current time again
	// Subtract endComp from startComp to get the CPU time used for reading and counting the unique number of words part.
	clock_gettime(CLOCK_MONOTONIC, &endComp); 
	time_taken = (endComp.tv_sec - startComp.tv_sec) * 1e9; 
    time_taken = (time_taken + (endComp.tv_nsec - startComp.tv_nsec)) * 1e-9; 

	// Print it out to console.
	printf("Process time(s) for counting unique number of words: %lf\n", time_taken);

	m = initialiseBitArray(n, m, &bitarray);	// Call the initalsiedBitArray function and pass in n and the bitarray address.

	// Get current clock time for startComp.
	clock_gettime(CLOCK_MONOTONIC, &startComp);

	// For loop to insert all unique words into the BloomFilter bitarray.
	for (i = 0; i < n; i++) {
		insertKey(totalUnique[i], m, bitarray);
	}

	// Get the clock current time again
	// Subtract endComp from startComp to get the CPU time used for the insertion part.
	clock_gettime(CLOCK_MONOTONIC, &endComp); 
	time_taken = (endComp.tv_sec - startComp.tv_sec) * 1e9; 
    time_taken = (time_taken + (endComp.tv_nsec - startComp.tv_nsec)) * 1e-9; 

	// Print it out to console.
	printf("Process time for inserting: %lf\n", time_taken);

	// Get current clock time for startComp.
	clock_gettime(CLOCK_MONOTONIC, &startComp);

	// Call the query method with the query file name to perform lookup on each query. Get the number of false positives.
	falsePositiveCount = query(queryName, m, n, bitarray, totalUnique);

	// Calculate the accuracy of the Bloom Filter.
	accuracy = 100 - (falsePositiveCount / queryLength);

	// Get the clock current time again
	// Subtract endComp from startComp to get the CPU time used for the query part.
	clock_gettime(CLOCK_MONOTONIC, &endComp); 
	time_taken = (endComp.tv_sec - startComp.tv_sec) * 1e9; 
    time_taken = (time_taken + (endComp.tv_nsec - startComp.tv_nsec)) * 1e-9; 

	// Print it out to console.
	printf("Process time for lookup query: %lf\n", time_taken);

	// Clean up memory allocated for total unique words list.
	for (i = 0; i < n; i++) {
		free(totalUnique[i]);
	}

	// Clean up memory allocated for unique word lists.
	for (i = 0; i < 3; i++) {
		free(ppUniqueWordLists[i]);	
	}

	// Clean up memory allocated for bloom filter bitarray.
	free(bitarray);

    // Get the clock current time again
	// Subtract end from start to get the CPU time used overall.
	clock_gettime(CLOCK_MONOTONIC, &end); 
	time_taken = (end.tv_sec - start.tv_sec) * 1e9; 
    time_taken = (time_taken + (end.tv_nsec - start.tv_nsec)) * 1e-9; 

    // Print it out to console.
	printf("Overall process time: %lf\n", time_taken);

	// Print the accuracy of the Bloom Filter
	printf("Accuracy of Bloom Filter: %f\n", accuracy);

	return 0;
}