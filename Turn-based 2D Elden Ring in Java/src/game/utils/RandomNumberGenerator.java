package game.utils;

import java.util.Random;

/**
 * A random number generator
 * Created by:
 * @author Adrian Kristanto
 * Modified by:
 * @author Bryan Wong
 * @version 1.0
 */

public class RandomNumberGenerator {
    /**
     * An instance of itself so that only one RandomNumberGenerator instance is created. Default set to null.
     */
    private static RandomNumberGenerator randomNumberGenerator = null;

    /**
     * Method to randomly generate a number from 0 inclusive to bound inclusive.
     * @param bound: The maximum number to generate a random number from, from 0 inclusive to bound inclusive.
     * @return a random number from 0 inclusive to bound inclusive.
     */
    public static int getRandomInt(int bound) {
        return bound > 0 ? new Random().nextInt(bound + 1) : 0;
    }

    /**
     * Method to randomly generate a number from lower bound inclusive to bound inclusive.
     * @param lowerBound: The minimum number to generate a random number from.
     * @param upperBound: The maximum number to generate a random number from.
     * @return a random number from lower bound inclusive to upper bound inclusive.
     */
    public static int getRandomInt(int lowerBound, int upperBound) {
        int range = upperBound - lowerBound + 1;
        return new Random().nextInt(range) + lowerBound;
    }

    /**
     * Returns the instance of the randomNumberGenerator stored as a data attribute.
     * If one has not been created yet, create a new instance of randomNumberGenerator.
     * @return the randomNumberGenerator instance.
     */
    public static RandomNumberGenerator getInstance() {
        // If an instance of randomNumberGenerator has not been created yet.
        if (randomNumberGenerator == null) {
            // Create a new instance of randomNumberGenerator.
            randomNumberGenerator = new RandomNumberGenerator();
        }
        // Return the randomNumberGenerator instance, either newly created or stored currently as a data attribute.
        return randomNumberGenerator;
    }
}
