package com.fierydragon.display.creations;

import com.fierydragon.display.animals.Animal;
import com.fierydragon.display.animals.Spider;

/**
 * Strategy Design Pattern to create the Spider Animal.
 * Created by:
 * @author Bryan Wong
 * Modified by:
 * @version 1.0
 * @see Animal
 * @see CreationStrategy
 * @see Spider
 */

public class SpiderCreationStrategy extends AnimalCreationStrategy {
    /**
     * Overridden method to create a new Spider.
     * @return the Spider object created.
     */
    @Override
    public Animal createDisplayComponent() {
        return new Spider();
    }
}
