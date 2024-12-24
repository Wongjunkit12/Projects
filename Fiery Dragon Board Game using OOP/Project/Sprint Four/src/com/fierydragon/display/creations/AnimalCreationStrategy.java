package com.fierydragon.display.creations;

import com.fierydragon.display.animals.Animal;

/**
 * Strategy Design Pattern to create the Animal.
 * Created by:
 * @author Bryan Wong
 * Modified by:
 * @version 1.0
 * @see Animal
 * @see CreationStrategy
 */

public abstract class AnimalCreationStrategy implements CreationStrategy {
    /**
     * Overridden method to create a new Animal type.
     * @return the Animal object created.
     */
    @Override
    public abstract Animal createDisplayComponent();
}
