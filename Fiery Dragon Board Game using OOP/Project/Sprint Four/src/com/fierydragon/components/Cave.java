package com.fierydragon.components;

import com.fierydragon.display.Displayable;
import com.fierydragon.volcano.Square;
import javafx.scene.paint.Color;

/**
 * A class representing the Cave of the Dragon. Inherits from UIComponent.
 * Created by:
 * @author Bryan Wong
 * Modified by: Vincent Tanuwidjaja, Po Han Tay
 * @version 1.0
 * @see UIComponent
 */

public class Cave extends Square {
    /**
     * Cave Constructor.
     * Calls the super class to set the inherited attributes.
     * @param colour: The Color of the Square.
     * @param colourString: The String representation of the colour of the Cave.
     * @param display: The Displayable to display.
     */
    public Cave(Color colour, String colourString, Displayable display) {
        // Set the data attributes.
        super(colour, colourString, display, "Cave");
        setIsCave(true);
    }

    /**
     * Overridden save method to save the Care itself.
     * @return the string savedData of the Cave.
     */
    @Override
    public String save() {
        // Append the Cave data.
        StringBuilder builder = new StringBuilder();
        builder.append("caveX: ");
        builder.append(this.getX());
        builder.append(", ");
        builder.append("caveY: ");
        builder.append(this.getY());
        builder.append(", ");
        builder.append("caveIsCave: ");
        builder.append(this.isCave());
        builder.append(", ");
        builder.append("caveColour: ");
        builder.append(this.getColour().toString());
        builder.append(", ");
        builder.append("caveColourName: ");
        builder.append(this.getColourString());
        builder.append(", ");
        builder.append("caveDisplayableName: ");
        builder.append(this.getDisplay().getName());
        builder.append(", ");
        builder.append("caveDisplayable: ");
        builder.append(this.getDisplay().getDisplay());

        // Return the constructed saved String.
        return builder.toString();
    }

    /**
     * Overridden load method to load the Cave itself.
     * @param savedData: The saved data to load.
     */
    @Override
    public void load(String savedData) {
        // Remove all '[' and ']'.
        String cleanedData = savedData.replace("[", "").replace("]", "");
        // Split the savedData with the delimiter being the comma followed by an optional space.
        String[] datas = cleanedData.split(",\\s*");
        // Loop through each saved Square data and load it.
        for (String data : datas) {
            // Split the line into its corresponding key-value pair.
            String[] keyValue = data.split(":\\s*", 2);
            String key = keyValue[0].trim();
            String value = keyValue[1].trim();

            // Load the corresponding data into their respective attributes.
            switch (key) {
                case "caveX":
                    this.setX(Integer.parseInt(value));
                    break;
                case "caveY":
                    this.setY(Integer.parseInt(value));
                    break;
                case "caveIsCave":
                    this.setIsCave(Boolean.parseBoolean(value));
                    break;
                case "caveColour":
                    this.setColour(Color.valueOf(value));
                    break;
                case "caveColourName":
                    this.setColourString(value);
                    break;
                case "caveDisplayableName":
                    this.getDisplay().setName(value);
                    break;
                case "caveDisplayable":
                    this.getDisplay().setDisplay(value);
                    break;
            }
        }
    }
}
