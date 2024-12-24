package com.fierydragon.volcano;


import com.fierydragon.display.Displayable;
import com.fierydragon.components.UIComponent;
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
 * A class representing the Square which is part of the Game Board. Inherits from UIComponent.
 * Created by:
 * @author Bryan Wong
 * Modified by: Vincent Tanuwidjaja, Po Han Tay
 * @version 1.0
 * @see Displayable
 * @see UIComponent
 */


public abstract class Square extends UIComponent {
    /**
     * The Display being displayed on the Card. Will be an Animal in this case.
     */
    private Displayable display;
    /**
     * A boolean to determine if the Square is a Cave or not.
     */
    private boolean isCave;

    /**
     * VolcanoSquare Constructor.
     * Calls the super class to set the inherited attributes.
     * @param colour: The Color of the VolcanoSquare.
     * @param colourString: The String representation of the colour of the Square.
     * @param displayable: The Displayable to display on the Square. In this case it's an Animal.
     * @param name: The name of the Square.
     */
    public Square(Color colour, String colourString, Displayable displayable, String name) {
        // Call the parent constructor and set the x, y and colour.
        super(0, 0, colour, colourString, name);
        // Set whether the square is cut or not and set the Displayed Animal.
        setDisplay(displayable);
    }

    /**
     * The overridden display method that will display the Square in the UI.
     * @param boardGrid: The GameBoard GridPane object
     * @param chitCardGrid: The ChitCard GridPane object.
     */
    @Override
    public void render(GridPane boardGrid, GridPane chitCardGrid) {
        // Create a rectangle for the square with a black border.
        Rectangle squareRect = new Rectangle(Constants.SQUARE_WIDTH, Constants.SQUARE_HEIGHT);
        squareRect.setFill(this.getColour());
        squareRect.setStroke(Color.BLACK); // Set the border color to black.

        // Create a StackPane to hold the rectangle and text.
        StackPane stack = new StackPane();
        stack.getChildren().add(squareRect);

        // Add the number of Animals and their displayed character to the Square.
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
    }

    /**
     * A method to return the display data attribute.
     * @return the Displayable that the Square is displaying.
     */
    public Displayable getDisplay() {
        return display;
    }

    /**
     * A method to get if the Square is a Cave or not.
     * @return True if it's a Cave, false otherwise.
     */
    public boolean isCave() {
        return isCave;
    }

    /**
     * A method to set the display data attribute.
     * @param display: The Displayable that the Square is displaying.
     */
    public void setDisplay(Displayable display) {
        this.display = display;
    }

    /**
     * A method to get if the Square is a Cave or not.
     * @param isCave: True if it's a Cave, false otherwise.
     */
    public void setIsCave(boolean isCave) {
        this.isCave = isCave;
    }
}
