package com.fierydragon.display.creations;

import com.fierydragon.display.Displayable;

/**
 * An interface using Strategy Design Pattern to create a Displayable.
 * Created by:
 * @author Bryan Wong
 * Modified by:
 * @version 1.0
 * @see Displayable
 */

public interface CreationStrategy {
    /**
     * A method that creates the Display Component. To be overridden by its children.
     * @return the Displayable object created.
     */
    Displayable createDisplayComponent();
}
