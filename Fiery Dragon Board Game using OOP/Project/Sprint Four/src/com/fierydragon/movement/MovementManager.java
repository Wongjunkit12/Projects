package com.fierydragon.movement;

/**
 * A MovementManager Singleton Design Pattern to manage movement of Dragons
 * Created by:
 * @author Bryan Wong
 * Modified by: Po Han Tay
 * @version 1.0
 * @see DragonSquareIterator
 */

public class MovementManager {
    /**
     * An instance of itself so that only one MovementManager instance is created. Default set to null.
     */
    private static MovementManager movementManager = null;

    /**
     * The Iterator class to iterate over the Squares and the Dragons they are placed on.
     */
    private DragonSquareIterator dragonSquareIterator;

    /**
     * MovementManager Constructor.
     * Creates a DragonSquareIterator instance based on the input list of Dragons.
     */
    public MovementManager() {
        // Create a new DragonSquareIterator instance based on the input List of dragons.
        setDragonSquareIterator(new DragonSquareIterator());
    }

    /**
     * A method to return the dragonSquareIterator data attribute.
     * @return the DragonSquareIterator instance.
     */
    public DragonSquareIterator getDragonSquareIterator() {
        return dragonSquareIterator;
    }

    /**
     * A method to set the dragonSquareIterator data attribute.
     * @param dragonSquareIterator: The DragonSquareIterator instance.
     */
    public void setDragonSquareIterator(DragonSquareIterator dragonSquareIterator) {
        this.dragonSquareIterator = dragonSquareIterator;
    }

    /**
     * Returns the instance of the movementManager stored as a data attribute.
     * If one has not been created yet, create a new instance of movementManager.
     * @return the movementManager instance.
     */
    public static MovementManager getInstance() {
        if (movementManager == null) {
            movementManager = new MovementManager();
        }
        return movementManager;
    }

    /**
     * Reset the MovementManager instance.
     */
    public void reset() {
        movementManager = new MovementManager();
    }
}
