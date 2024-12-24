package com.fierydragon.components;

import javafx.scene.layout.GridPane;

/**
 * An Interface that enforces the click method for all Interactable classes.
 * Created by:
 * @author Bryan Wong
 * Modified by:
 * @version 1.0
 */

public interface Interactable {
    /**
     * A method to be overridden by its child classes to implement functionality for its own.
     * @param grid: The ChitCard GridPane object.
     */
    void interact(GridPane grid);
}
