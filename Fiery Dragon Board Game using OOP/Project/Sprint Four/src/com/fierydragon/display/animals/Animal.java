package com.fierydragon.display.animals;

import com.fierydragon.display.Displayable;
import com.fierydragon.components.UIComponent;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

/**
 * A class representing the Animal Display on Chit Cards and the Volcano Squares.
 * Created by:
 * @author Bryan Wong
 * Modified by:
 * @version 1.0
 * @see Displayable
 * @see UIComponent
 */

public abstract class Animal extends UIComponent implements Displayable {
    /**
     * The string representing the Displayable on the Game Board and the Chit Cards. Will be an Animal in this case.
     */
    private String display;

    /**
     * The string representing the Displayable's name.
     */
    private String name;

    /**
     * Animal Constructor.
     * Calls the super class to set the inherited attributes.
     * Set the string representing the Animal.
     * @param colour: The Color of the Animal.
     * @param colourString: The String representation of the colour of the Animal.
     * @param animalString: The String representation of the Animal.
     * @param animalName: The name of the animal.
     */
    public Animal(Color colour, String colourString, String animalString, String animalName) {
        // Calls the parent constructor and set x and y default to 0 first as dummy values.
        super(0, 0, colour, colourString, animalName);
        // Set the display string and name for the animal.
        setDisplay(animalString);
        setName(animalName);
    }

    /**
     * The overridden render method that will render the Animal.
     * @param boardGrid: The GameBoard GridPane object
     * @param chitCardGrid: The ChitCard GridPane object.
     */
    @Override
    public void render(GridPane boardGrid, GridPane chitCardGrid) {
        // Add the  Animal string.
        Text text = new Text(this.getDisplay());
        boardGrid.add(text, this.getX(), this.getY());
    }

    /**
     * Overridden save method to save the Animal itself.
     * @return the string savedData of the Animal.
     */
    @Override
    public String save() {
        // Append the Animal data.
        StringBuilder builder = new StringBuilder();
        builder.append("animalDisplay: ");
        builder.append(this.getDisplay());
        builder.append(", ");
        builder.append("animalName: ");
        builder.append(this.getName());

        // Return the constructed saved String.
        return builder.toString();
    }

    /**
     * Overridden load method to load the Animal itself.
     * @param savedData: The saved data to load.
     */
    @Override
    public void load(String savedData) {
        // Split the savedData with the delimiter being the comma followed by an optional space.
        String[] datas = savedData.split(",\\s*");
        // Loop through each saved Square data and load it.
        for (String data : datas) {
            // Split the line into its corresponding key-value pair.
            String[] keyValue = data.split(":\\s*", 2);
            String key = keyValue[0].trim();
            String value = keyValue[1].trim();

            // Load the corresponding data into their respective attributes.
            switch (key) {
                case "animalDisplay":
                    this.setDisplay(value);
                    break;
                case "animalName":
                    this.setName(value);
                    break;
            }
        }
    }

    /**
     * A method to return the display data attribute.
     * @return the string representing the Animal.
     */
    @Override
    public String getDisplay() {
        return display;
    }

    /**
     * A method to set the display data attribute.
     * @param display: the string representing the Display.
     */
    @Override
    public void setDisplay(String display) {
        this.display = display;
    }

    /**
     * A method to return the display's name.
     * @return the string representing the Animal's name.
     */
    @Override
    public String getName() {
        return name;
    }

    /**
     * A method to set the display's name.
     * @param name: the string representing the display's name.
     */
    @Override
    public void setName(String name) {
        this.name = name;
    }
}
