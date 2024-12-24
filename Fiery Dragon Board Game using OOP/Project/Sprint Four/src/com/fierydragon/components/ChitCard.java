package com.fierydragon.components;

import com.fierydragon.display.Displayable;
import com.fierydragon.pieces.Dragon;
import com.fierydragon.utils.Constants;
import com.fierydragon.utils.TurnManager;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

/**
 * A class representing the Chit Card/Dragon Cards in the center of the Volcano.
 * Created by:
 * @author Bryan Wong
 * Modified by: Vincent Tanuwidjaja, Po Han Tay
 * @version 1.0
 * @see Displayable
 * @see UIComponent
 * @see Constants
 * @see Interactable
 */

public class ChitCard extends UIComponent implements Interactable {
    /**
     * The displayable being displayed on the ChitCard.
     */
    private Displayable display;
    /**
     * The number of Displayables on the ChitCard.
     */
    private int numOfDisplayables;
    /**
     * The boolean indicating if the card has been flipped or not.
     */
    private boolean flipped = false;
    /**
     * The Text of the ChitCard.
     */
    private Text text;

    /**
     * ChitCard Constructor.
     * Calls the super class to set the inherited attributes.
     * @param colour: The Color of the ChitCard.
     * @param colourString: The String representation of the colour of the ChitCard.
     * @param display: The displayed character to show.
     * @param numOfDisplayables: The number of Displayables on the ChitCard.
     */
    public ChitCard(Color colour, String colourString, Displayable display, int numOfDisplayables) {
        // Calls the parent constructor to set the parent attributes.
        super(0, 0, colour, colourString, "Chit Card");
        setDisplay(display);
        setNumOfDisplayables(numOfDisplayables);
    }

    /**
     * Overridden interact method.
     * Will use the TurnManager to get the current Dragon and call its move method.
     * @param grid: The ChitCard GridPane object.
     */
    @Override
    public void interact(GridPane grid) {
        // Do not do anything if the Chit Card is already flipped or if the game is paused.
        if (isFlipped() || TurnManager.getInstance().isPaused()) {
            return;
        }
        // Set the flipped variable to true and make the text visible.
        text.setVisible(true);
        setFlipped(true);

        // Get the current Dragon from Turn Manager and invoke its move method.
        Dragon currentDragon = TurnManager.getInstance().getCurrentDragon();
        currentDragon.move(this);
    }

    /**
     * The overridden display method that will display the Chit Card in the UI.
     * @param boardGrid: The GameBoard GridPane object
     * @param chitCardGrid: The ChitCard GridPane object.
     */
    @Override
    public void render(GridPane boardGrid, GridPane chitCardGrid) {
        // Create a rectangle for the square with a black border.
        Rectangle squareRect = new Rectangle(Constants.SQUARE_WIDTH - 20, Constants.SQUARE_HEIGHT - 20);
        squareRect.setFill(getColour());
        squareRect.setStroke(Color.BLACK); // Set the border color to black.

        // Set the mouse click event to trigger the updateTurnIndicator method
        squareRect.setOnMouseClicked(e -> {
            // Call the interact method
            this.interact(chitCardGrid);
        });

        // Add the squareRect to the grid
        chitCardGrid.add(squareRect, getX(), getY());

        // Add the Displayable's displayed character to the Square.
        text = new Text(String.format("%d %s", getNumOfDisplayables(), getDisplay().getDisplay()));
        // Set the colour of the displayable.
        text.setFill(Color.BLACK);
        // Set visibility of text to be based on flipped variable.
        text.setVisible(isFlipped());
        // Set the Font of the displayable string representation.
        text.setFont(Font.font("Arial", 15));
        // Make the text be in the center
        text.setTranslateX(12);

        chitCardGrid.add(text, getX(), getY());
    }

    /**
     * Overridden save method to save the ChitCard itself.
     * @return the string savedData of the ChitCard.
     */
    @Override
    public String save() {
        // Append the Card data.
        StringBuilder builder = new StringBuilder();
        builder.append("chitCardDisplayName: ");
        builder.append(this.getDisplay().getName());
        builder.append(", ");
        builder.append("chitCardDisplayable: ");
        builder.append(this.getDisplay().getDisplay());
        builder.append(", ");
        builder.append("chitCardNumOfDisplayables: ");
        builder.append(this.getNumOfDisplayables());
        builder.append(", ");
        builder.append("chitCardFlipped: ");
        builder.append(this.isFlipped());
        builder.append(", ");
        builder.append("chitCardText: ");
        builder.append(this.getText().getText());

        // Return the constructed saved String.
        return builder.toString();
    }

    /**
     * Overridden load method to load the ChitCard itself.
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
                case "chitCardDisplayName":
                    this.getDisplay().setName(value);
                    break;
                case "chitCardDisplayable":
                    this.getDisplay().setDisplay(value);
                    break;
                case "chitCardNumOfDisplayables":
                    this.setNumOfDisplayables(Integer.parseInt(value));
                    break;
                case "chitCardFlipped":
                    this.setFlipped(Boolean.parseBoolean(value));
                    break;
                case "chitCardText":
                    this.setText(value);
                    break;
            }
        }
    }

    /**
     * A method to return the display data attribute.
     * @return the Displayable that the ChitCard is displaying.
     */
    public Displayable getDisplay() {
        return display;
    }

    /**
     * A method to return the numOfDisplayables data attribute.
     * @return the number of Displayables on the ChitCard.
     */
    public int getNumOfDisplayables() {
        return numOfDisplayables;
    }

    /**
     * A method to return the isFlipped data attribute.
     * @return the boolean indicating if the ChitCard is flipped or not.
     */
    public boolean isFlipped() {
        return flipped;
    }

    /**
     * A method to return the Text data attribute.
     * @return the Text object of the ChitCard.
     */
    public Text getText() {
        return text;
    }

    /**
     * A method to set the display data attribute.
     * @param display: The Displayable that the ChitCard is displaying.
     */
    public void setDisplay(Displayable display) {
        this.display = display;
    }


    /**
     * A method to set the numOfDisplayables data attribute.
     * @param numOfDisplayables: The number of Displayables on the ChitCard.
     */
    public void setNumOfDisplayables(int numOfDisplayables) {
        this.numOfDisplayables = numOfDisplayables;
    }

    /**
     * A method to set the flipped data attribute.
     * @param flipped: The boolean indicating if the ChitCard is flipped or not.
     */
    public void setFlipped(boolean flipped) {
        this.flipped = flipped;
    }

    /**
     * A method to set the Text data attribute.
     * @param text: The string to set the text to.
     */
    public void setText(String text) {
        this.text.setText(text);
    }

    /**
     * A method to reset the flipped data attribute to false.
     */
    public void reset() {
        this.flipped = false;
        text.setVisible(false);
    }
}
