package com.example.bookstoreapp2;

// Import random module.
import java.util.Random;

// A class that generates random String or numbers.
public abstract class RandomGenerator {
    // All possible lower case alphabets to generate.
    private static final String lowerCase = "abcdefghijklmnopqrstuvwxyz";
    // All possible upper case alphabets to generate.
    private static final String upperCase = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    // All possible numbers to generate.
    private static final String numbers = "0123456789";
    // The randomString to generator with,
    private static final String randomString = lowerCase + upperCase + numbers;
    // The Random class to be used to generate random stuff.
    private static Random random = new Random();

    // Generates a random string with the length of it being the input length.
    public static String createRandomString(int length) {
        char[] randomStringResult;                  // List of characters to hold the final randomly generated ISBN.
        // Create an array of characters with the length being the input length.
        randomStringResult = new char[length];

        // Loop through the length of the randomStringResult array and pick a random character to add from the randomString above.
        // May add lowercase/uppercase alphabets and numbers at random.
        for (int i = 0; i < randomStringResult.length; i++) {
            // Pick a character at random to add to the randomStringResult array.
            randomStringResult[i] = randomString.charAt(random.nextInt(randomString.length()));
        }
        // Return the array converted to a String to get a random string.
        return new String(randomStringResult);
    }

    // Create random number from lower bound to upper bound inclusive.
    public static int createRandomNumber(int lowerBound, int upperBound) {
        // Get the range between the upper and lower bound.
        int range = upperBound - lowerBound + 1;
        // Return the randomly generated number from lower bound to upper bound inclusive.
        return random.nextInt(range) + lowerBound;
    }
}

