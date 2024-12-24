package com.fierydragon.utils;

/**
 * An interface that enforces the saving method to its subclasses.
 * Created by:
 * @author Bryan Wong
 * Modified by:
 * @version 1.0
 */

public interface Saveable {
    /**
     * A method to save the state of the game.
     * @return the game state to save.
     */
    String save();

    /**
     * A method to load the state of the game.
     * @param savedData: The saved data to load.
     */
    void load(String savedData);
}
