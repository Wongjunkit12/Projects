package com.fierydragon.display.creations;

import com.fierydragon.display.animals.Animal;
import com.fierydragon.display.animals.Salamander;

/**
 * Strategy dDesign Pattern to create the Salamander Animal.
 * Created by:
 * @author Bryan Wong
 * Modified by:
 * @version 1.0
 * @see Animal
 * @see CreationStrategy
 * @see Salamander
 */

public class SalamanderCreationStrategy extends AnimalCreationStrategy {
    /**
     * Overridden method to create a new Salamander.
     * @return the Salamander object created.
     */
    @Override
    public Animal createDisplayComponent() {
        return new Salamander();
    }
}
