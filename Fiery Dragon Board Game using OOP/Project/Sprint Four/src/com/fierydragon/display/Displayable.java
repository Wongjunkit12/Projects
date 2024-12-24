package com.fierydragon.display;

/**
 * An interface in which all its children are "Displayable" on a ChitCard or a Volcano Square. Enforces getters and setters for the
 * display String.
 * Uses Adapter Design Pattern to link together all Displayables. For now they are only Animals.
 * Allow the usage of CreationStrategy to create Displayables.
 * Created by:
 * @author Bryan Wong
 * Modified by:
 * @version 1.0
 */

public interface Displayable {
    /**
     * A method to return the display data attribute.
     * @return the string representing the display.
     */
    String getDisplay();

    /**
     * A method to set the display data attribute.
     * @param display: the string representing the display.
     */
    void setDisplay(String display);

    /**
     * A method to return the display's name.
     * @return the string representing the display's name.
     */
    String getName();

    /**
     * A method to set the display's name.
     * @param name: the string representing the display's name.
     */
    void setName(String name);
}
