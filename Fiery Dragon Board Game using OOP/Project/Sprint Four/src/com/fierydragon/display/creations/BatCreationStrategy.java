package com.fierydragon.display.creations;

import com.fierydragon.display.animals.Animal;
import com.fierydragon.display.animals.Bat;

/**
 * Strategy Design Pattern to create the Bat Animal.
 * Created by:
 * @author Bryan Wong
 * Modified by:
 * @version 1.0
 * @see Animal
 * @see CreationStrategy
 * @see Bat
 */

public class BatCreationStrategy extends AnimalCreationStrategy {
    /**
     * Overridden method to create a new Bat.
     * @return the Bat object created.
     */
    @Override
    public Animal createDisplayComponent() {
        return new Bat();
    }
}
