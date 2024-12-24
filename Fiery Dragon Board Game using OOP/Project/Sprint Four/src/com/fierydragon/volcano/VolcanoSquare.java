package com.fierydragon.volcano;

import com.fierydragon.components.Cave;
import com.fierydragon.components.UIComponent;
import com.fierydragon.display.Displayable;
import com.fierydragon.display.creations.BabyDragonCreationStrategy;
import com.fierydragon.utils.Constants;
import javafx.geometry.Pos;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

/**
 * A class representing the Volcano Square which is part of the Game Board. Inherits from Square.
 * Created by:
 * @author Bryan Wong
 * Modified by: Vincent Tanuwidjaja, Po Han Tay
 * @version 1.0
 * @see Cave
 * @see Displayable
 * @see UIComponent
 * @see Constants
 * @see Square
 */

public class VolcanoSquare extends Square {
    /**
     * A boolean to determine if the Square is cut or not.
     */
    private boolean isCut;
    /**
     * The Instance of the Cave if one exist.
     */
    private Cave cave;

    /**
     * VolcanoSquare Constructor.
     * Calls the super class to set the inherited attributes.
     * @param colour: The Color of the VolcanoSquare.
     * @param colourString: The String representation of the colour of the VolcanoSquare.
     * @param displayable: The Displayable to display on the VolcanoSquare. In this case it's an Animal.
     */
    public VolcanoSquare(Color colour, String colourString, Displayable displayable) {
        // Call the parent constructor and set the x, y and colour.
        super(colour, colourString, displayable, "Volcano Square");
        // Set whether the square is cut or not and set the Displayed Animal.
        setCut(false);      // False if no cut provided.
    }

    /**
     * VolcanoSquare Constructor.
     * Calls the super class to set the inherited attributes.
     * Overloaded constructor to create for cut Squares.
     * @param colour: The Color of the Square.
     * @param colourString: The String representation of
     * @param displayedAnimal: The Animal to display on the Square.
     * @param cave: The Cave the Square is attached to. Will only exist if isCut.
     */
    public VolcanoSquare(Color colour, String colourString, Displayable displayedAnimal, Cave cave) {
        // Call the parent constructor and set the x, y and colour.
        super(colour, colourString, displayedAnimal, "VolcanoSquare");
        setIsCave(false);
        // Set whether the square is cut along with their Cave instance. and set the Displayed Animal.
        setCut(true);       // True if cave provided.
        setCave(cave);      // Set the Cave instance.
    }

    /**
     * The overridden display method that will display the VolcanoSquare in the UI.
     * @param boardGrid: The GameBoard GridPane object
     * @param chitCardGrid: The ChitCard GridPane object.
     */
    @Override
    public void render(GridPane boardGrid, GridPane chitCardGrid) {
        // Create a rectangle for the VolcanoSquare with a black border.
        Rectangle squareRect = new Rectangle(Constants.SQUARE_WIDTH, Constants.SQUARE_HEIGHT);
        squareRect.setFill(this.getColour());
        squareRect.setStroke(Color.BLACK); // Set the border color to black.

        // Create a StackPane to hold the rectangle and text.
        StackPane stack = new StackPane();
        stack.getChildren().add(squareRect);

        // Add the number of Animals and their displayed character to the VolcanoSquare.
        Text text = new Text(this.getDisplay().getName() + " (" + this.getDisplay().getDisplay() + ")");
        // Set the colour of the displayable
        text.setFill(Color.BLACK);
        // Set the Font of the displayed Animal string representation.
        text.setFont(Font.font("Arial", FontWeight.BOLD, 8.5));

        // Add the text on top of the rectangle in the StackPane
        stack.getChildren().add(text);

        // Position the text at the bottom of the StackPane
        StackPane.setAlignment(text, Pos.BOTTOM_CENTER);

        // Add the StackPane to the grid
        boardGrid.add(stack, this.getX(), this.getY());

        // If there exist a cave, render it.
        if (this.getCave() != null) {
            this.getCave().render(boardGrid, chitCardGrid);
        }
    }

    /**
     * Overridden save method to save the VolcanoSquare itself.
     * @return the string savedData of the GameBoard.
     */
    @Override
    public String save() {
        // Append the VolcanoSquare data.
        StringBuilder builder = new StringBuilder();
        builder.append("volcanoX: ");
        builder.append(this.getX());
        builder.append(", ");
        builder.append("volcanoY: ");
        builder.append(this.getY());
        builder.append(", ");
        builder.append("volcanoIsCave: ");
        builder.append(isCave() ? "true" : "false");
        builder.append(", ");
        builder.append("volcanoIsCut: ");
        builder.append(isCut() ? "true" : "false");
        builder.append(", ");
        builder.append("volcanoDisplayableName: ");
        builder.append(getDisplay().getName());
        builder.append(", ");
        builder.append("volcanoDisplayable: ");
        builder.append(getDisplay().getDisplay());
        // Save the cave if there's one attached to this.
        if (this.getCave() != null) {
            builder.append(", ");
            builder.append("volcanoCaveX: ");
            builder.append(this.getCave().getX());
            builder.append(", ");
            builder.append("volcanoCaveY: ");
            builder.append(this.getCave().getY());
            builder.append(", ");
            builder.append("volcanoCaveColour: ");
            builder.append(this.getCave().getColour().toString());
            builder.append(", ");
            builder.append("volcanoCaveColourName: ");
            builder.append(this.getCave().getColourString());
            builder.append(", ");
            builder.append("volcanoCaveDisplayableName: ");
            builder.append(this.getCave().getDisplay().getName());
            builder.append(", ");
            builder.append("volcanoCaveDisplayable: ");
            builder.append(this.getCave().getDisplay().getDisplay());
        }

        // Return the constructed saved String.
        return builder.toString();
    }

    /**
     * Overridden load method to load the VolcanoSquare itself.
     * @param savedData: The saved data to load.
     */
    @Override
    public void load(String savedData) {
        // Remove all '{' and '}'.
        String cleanedData = savedData.replace("{", "").replace("}", "");

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
                case "volcanoX":
                    this.setX(Integer.parseInt(value));
                    break;
                case "volcanoY":
                    this.setY(Integer.parseInt(value));
                    break;
                case "volcanoIsCave":
                    this.setIsCave(Boolean.parseBoolean(value));
                    break;
                case "volcanoIsCut":
                    this.setCut(Boolean.parseBoolean(value));
                    // Create a new cave if the Volcano is cut. If it's not, remove that cave.
                    this.setCave(isCut() ? new Cave(null, "", new BabyDragonCreationStrategy().createDisplayComponent()) : null);
                    break;
                case "volcanoDisplayableName":
                    this.getDisplay().setName(value);
                    break;
                case "volcanoDisplayable":
                    this.getDisplay().setDisplay(value);
                    break;
                case "volcanoCaveX":
                    this.getCave().setX(Integer.parseInt(value));
                    break;
                case "volcanoCaveY":
                    this.getCave().setY(Integer.parseInt(value));
                    break;
                case "volcanoCaveColour":
                    this.getCave().setColour(Color.valueOf(value));
                    break;
                case "volcanoCaveColourName":
                    this.getCave().setColourString(value);
                    break;
                case "volcanoCaveDisplayableName":
                    this.getCave().getDisplay().setName(value);
                    break;
                case "volcanoCaveDisplayable":
                    this.getCave().getDisplay().setDisplay(value);
                    break;
            }
        }
    }

    /**
     * A method to return the isCut data attribute.
     * @return true if the VolcanoSquare is cut, false otherwise.
     */
    public boolean isCut() {
        return isCut;
    }

    /**
     * A method to return the cave data attribute.
     * @return the Cave instance variable attached to this VolcanoSquare.
     */
    public Cave getCave() {
        return cave;
    }

    /**
     * A method to set the isCut data attribute.
     * @param cut: True if the VolcanoSquare is cut, false otherwise.
     */
    public void setCut(boolean cut) {
        isCut = cut;
    }

    /**
     * A method to set the cave data attribute.
     * @param cave: The Cave instance variable attached to this VolcanoSquare.
     */
    public void setCave(Cave cave) {
        this.cave = cave;
    }
}
