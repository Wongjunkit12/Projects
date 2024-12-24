package com.fierydragon.components;

import com.fierydragon.utils.Saveable;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;

/**
 * An abstract class that represents a UIComponent that is displayed in JavaFX.
 * Created by:
 * @author Bryan Wong
 * Modified by:
 * @version 1.0
 */

public abstract class UIComponent implements Saveable {
    /**
     * The x and y coordinates of the UIComponent.
     */
    private int x, y;
    /**
     * The colour of the UIComponent. Will use the Color object.
     */
    private Color colour;
    /**
     * The string representation of the Colour of the UIComponent.
     */
    private String colourString;
    /**
     * The name of the UIComponent.
     */
    private String name;

    /**
     * UIComponent Constructor.
     * Sets the x and y coordinates and the Color of the UIComponent.
     * @param x: The x-coordinate of the UIComponent.
     * @param y: The y-coordinate of the UIComponent.
     * @param colour: The Color of the UIComponent.
     * @param name: The name of the UIComponent.
     */
    public UIComponent(int x, int y, Color colour, String colourString, String name) {
        // Set the UIComponent data attributes.
        setX(x);
        setY(y);
        setColour(colour);
        setColourString(colourString);
        setName(name);
    }

    /**
     * Template Method Design Pattern. Subclasses will overwrite it to have its own functionality in rendering.
     * @param boardGrid: The BoardGame GridPane object.
     * @param chitCardGrid: The ChitCard GridPane object.
     */
    public abstract void render(GridPane boardGrid, GridPane chitCardGrid);

    /**
     * A method to return the x data attribute.
     * @return the x-coordinate of the UIComponent.
     */
    public int getX() {
        return x;
    }

    /**
     * A method to return the y data attribute.
     * @return the y-coordinate of the UIComponent.
     */
    public int getY() {
        return y;
    }

    /**
     * A method to return the colour data attribute.
     * @return the Color of the UIComponent.
     */
    public Color getColour() {
        return colour;
    }

    /**
     * A method to return the colourString data attribute.
     * @return the String representation of the color of the UIComponent.
     */
    public String getColourString() {
        return colourString;
    }

    /**
     * A method to return the name data attribute.
     * @return the name of the UIComponent.
     */
    public String getName() {
        return name;
    }

    /**
     * A method to set the x data attribute.
     * @param x: The x-coordinate of the UIComponent.
     */
    public void setX(int x) {
        this.x = x;
    }

    /**
     * A method to set the y data attribute.
     * @param y: The y-coordinate of the UIComponent.
     */
    public void setY(int y) {
        this.y = y;
    }

    /**
     * This method sets the x and y coordinates of the UIComponent based on the parameters given.
     * @param x: The x-coordinate of the UIComponent.
     * @param y: The y-coordinate of the UIComponent.
     */
    public void setCoordinates(int x, int y) {
        // Set the x and y coordinates of the UI Component.
        this.setX(x);
        this.setY(y);
    }

    /**
     * A method to set the colour data attribute.
     * @param colour: The Color of the UIComponent.
     */
    public void setColour(Color colour) {
        this.colour = colour;
    }

    /**
     * A method to set the colourString data attribute.
     * @param colourString: The String representation of the color of the UIComponent.
     */
    public void setColourString(String colourString) {
        this.colourString = colourString;
    }

    /**
     * A method to set the name data attribute.
     * @param name: The name of the UIComponent.
     */
    public void setName(String name) {
        this.name = name;
    }
}
