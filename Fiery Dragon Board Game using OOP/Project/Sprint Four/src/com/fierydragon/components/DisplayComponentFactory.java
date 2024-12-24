package com.fierydragon.components;

import com.fierydragon.display.creations.CreationStrategy;

import java.util.List;

/**
 * An interface that enforces the createComponent method to create a UI Component.
 * Created by:
 * @author Bryan Wong
 * Modified by:
 * @version 1.0
 * @see UIComponent
 */

public interface DisplayComponentFactory {
    /**
     * Enforces this method for all its children classes.
     * @param creationStrategies: The List of CreationStrategies to use to create.
     * @return UIComponent object and its subclass.
     */
    List<UIComponent> createComponents(List<CreationStrategy> creationStrategies);
}
