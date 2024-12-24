package com.fierydragon.display.animals;

import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

/**
 * A class representing the Knight Display on Chit Cards. Inherits from Animal.
 * Created by:
 * @author Po Han Tay
 * Modified by:
 * @version 1.0
 * @see Animal
 */

public class Knight extends Animal {
    /**
     * Knight Constructor.
     * Calls the super class to set the inherited attributes.
     * Set the String representing the Knight.
     */
    public Knight() {
        super(Color.BLACK,  "Black", "KN", "Knight");
    }

    /**
     * The overridden render method that will render the Knight.
     * @param boardGrid: The GameBoard GridPane object
     * @param chitCardGrid: The ChitCard GridPane object.
     */
    @Override
    public void render(GridPane boardGrid, GridPane chitCardGrid) {
        // Add the Animal string.
        Text text = new Text(this.getDisplay());
        chitCardGrid.add(text, this.getX(), this.getY());
    }
}
