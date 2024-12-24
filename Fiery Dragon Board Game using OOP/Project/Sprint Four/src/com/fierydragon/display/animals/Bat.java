package com.fierydragon.display.animals;

import javafx.scene.paint.Color;

/**
 * A class representing the Bat Display on Chit Cards and the Volcano Squares. Inherits from Animal.
 * Created by:
 * @author Bryan Wong
 * Modified by:
 * @version 1.0
 * @see Animal
 */

public class Bat extends Animal {
    /**
     * Bat Constructor.
     * Calls the super class to set the inherited attributes.
     * Set the String representing the Bat.
     */
    public Bat() {
        super(Color.BLACK,  "Black", "BA", "Bat");
    }
}
