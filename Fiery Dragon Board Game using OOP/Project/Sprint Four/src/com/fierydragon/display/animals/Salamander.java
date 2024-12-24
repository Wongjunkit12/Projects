package com.fierydragon.display.animals;

import javafx.scene.paint.Color;

/**
 * A class representing the Salamander Display on Chit Cards and the Volcano Squares. Inherits from Animal.
 * Created by:
 * @author Bryan Wong
 * Modified by:
 * @version 1.0
 * @see Animal
 */

public class Salamander extends Animal {
    /**
     * Salamander Constructor.
     * Calls the super class to set the inherited attributes.
     * Set the String representing the Salamander.
     */
    public Salamander() {
        super(Color.BLACK, "Black","SA", "Salamander");
    }
}
