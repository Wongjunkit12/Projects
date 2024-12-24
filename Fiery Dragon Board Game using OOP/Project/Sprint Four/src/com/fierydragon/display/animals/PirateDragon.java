package com.fierydragon.display.animals;

import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

/**
 * A class representing the Pirate Dragon Display on Chit Cards. Inherits from Animal.
 * Created by:
 * @author Bryan Wong
 * Modified by:
 * @version 1.0
 * @see Animal
 */

public class PirateDragon extends Animal {
    /**
     * PirateDragon Constructor.
     * Calls the super class to set the inherited attributes.
     * Set the string representing the PirateDragon.
     */
    public PirateDragon() {
        super(Color.BLACK, "Black","PD", "Pirate Dragon");
    }

    /**
     * The overridden render method that will render the PirateDragon.
     * @param boardGrid: The GameBoard GridPane object
     * @param chitCardGrid: The ChitCard GridPane object.
     */
    @Override
    public void render(GridPane boardGrid, GridPane chitCardGrid) {
        // Add the  Animal string.
        Text text = new Text(this.getDisplay());
        chitCardGrid.add(text, this.getX(), this.getY());
    }
}
