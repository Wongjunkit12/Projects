package com.fierydragon.display.animals;

import javafx.scene.paint.Color;

/**
 * A class representing the Baby Dragon Display on Chit Cards and the Volcano Squares. Inherits from Animal.
 * Created by:
 * @author Bryan Wong
 * Modified by:
 * @version 1.0
 * @see Animal
 */

public class BabyDragon extends Animal {
    /**
     * BabyDragon Constructor.
     * Calls the super class to set the inherited attributes.
     * Set the string representing the BabyDragon.
     */
    public BabyDragon() {
        super(Color.BLACK, "Black", "BD", "Baby Dragon");
    }
}
