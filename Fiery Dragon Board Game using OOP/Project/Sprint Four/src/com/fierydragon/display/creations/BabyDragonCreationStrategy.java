package com.fierydragon.display.creations;

import com.fierydragon.display.animals.Animal;
import com.fierydragon.display.animals.BabyDragon;

/**
 * Strategy Design Pattern to create the Baby Dragon Animal.
 * Created by:
 * @author Bryan Wong
 * Modified by:
 * @version 1.0
 * @see Animal
 * @see CreationStrategy
 * @see BabyDragon
 */

public class BabyDragonCreationStrategy extends AnimalCreationStrategy {
    /**
     * Overridden method to create a new BabyDragon.
     * @return the BabyDragon object created.
     */
    @Override
    public Animal createDisplayComponent() {
        return new BabyDragon();
    }
}
