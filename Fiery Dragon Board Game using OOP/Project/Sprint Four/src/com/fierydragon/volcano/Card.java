package com.fierydragon.volcano;

import com.fierydragon.components.UIComponent;
import com.fierydragon.display.creations.BabyDragonCreationStrategy;
import com.fierydragon.movement.DragonSquareIterator;
import com.fierydragon.movement.MovementManager;
import javafx.scene.control.Alert;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;

/**
 * A class representing the Volcano Square which is part of the Game Board. Inherits from UIComponent.
 * Composite Design Pattern.
 * Created by:
 * @author Bryan Wong
 * Modified by: Po Han Tay, Vincent Tanuwidjaja
 * @version 1.0
 * @see UIComponent
 * @see VolcanoSquare
 */

public class Card extends UIComponent {
    /**
     * The number of Squares the Card have.
     */
    private int numberOfSquares;
    /**
     * The placement of the Card.
     */
    private CardPlacement cardPlacement;
    /**
     * List of Squares the Card is composed of.
     */
    private List<VolcanoSquare> volcanoSquares;

    /**
     * Card Constructor.
     * Calls the super class to set the inherited attributes.
     * @param colour: The Color of the Card.
     * @param colourString: The String representation of the colour of Card.
     * @param numberOfSquares: The number of Squares the Card have.
     * @param cardPlacement: The placement of the Card.
     */
    public Card(Color colour, String colourString, int numberOfSquares, CardPlacement cardPlacement) {
        // Call parent constructor, set the data attributes and create empty ArrayList for the squares data attribute.
        super(0, 0, colour, colourString, "Card");
        setNumOfSquares(numberOfSquares);
        setCardPlacement(cardPlacement);
        setSquares(new ArrayList<>());
    }

    /**
     * This method creates placement of the Squares in the Game Board.
     * @param cardPlacement: The enumeration depicting the placement of the card.
     */
    public void placeSquares(CardPlacement cardPlacement) {
        // If in top left corner.
        if (cardPlacement == CardPlacement.NORTHWEST) {
            getSquares().get(0).setCoordinates(this.getX(), this.getY() + 1);
            getSquares().get(1).setCoordinates(this.getX(), this.getY());
            getSquares().get(2).setCoordinates(this.getX() + 1, this.getY());
        }
        // If in top right corner.
        else if (cardPlacement == CardPlacement.NORTHEAST) {
            getSquares().get(0).setCoordinates(this.getX() - 1, this.getY());
            getSquares().get(1).setCoordinates(this.getX(), this.getY());
            getSquares().get(2).setCoordinates(this.getX(), this.getY() + 1);
        }
        // If in bottom right corner.
        else if (cardPlacement == CardPlacement.SOUTHEAST) {
            getSquares().get(0).setCoordinates(this.getX(), this.getY() - 1);
            getSquares().get(1).setCoordinates(this.getX(), this.getY());
            getSquares().get(2).setCoordinates(this.getX() - 1, this.getY());
        }
        // If in bottom left corner.
        else if (cardPlacement == CardPlacement.SOUTHWEST) {
            getSquares().get(0).setCoordinates(this.getX() + 1, this.getY());
            getSquares().get(1).setCoordinates(this.getX(), this.getY());
            getSquares().get(2).setCoordinates(this.getX(), this.getY() - 1);
        }
        // If top
        else if (cardPlacement == CardPlacement.NORTH) {
            // Loop through the numberOfSquares and create a Square for each one.
            for (int i = 0; i < 3; i++) {
                getSquares().get(i).setCoordinates(this.getX() + i, this.getY());
            }
            // Set the cave coordinates.
            getSquares().get(1).getCave().setCoordinates(this.getX() + 1, this.getY() - 1);
        }
        // If bottom
        else if (cardPlacement == CardPlacement.SOUTH) {
            // Loop through the numberOfSquares and create a Square for each one.
            for (int i = 0; i < 3; i++) {
                getSquares().get(i).setCoordinates(this.getX() - i, this.getY());
            }
            // Set the cave coordinates.
            getSquares().get(1).getCave().setCoordinates(this.getX() - 1, this.getY() + 1);
        }
        // If right
        else if (cardPlacement == CardPlacement.EAST) {
            // Loop through the numberOfSquares and create a Square for each one.
            for (int i = 0; i < getNumOfSquares(); i++) {
                getSquares().get(i).setCoordinates(this.getX(), this.getY() + i);
            }
            // Set the cave coordinates.
            getSquares().get(1).getCave().setCoordinates(this.getX() + 1, this.getY() + 1);
        }
        // If left
        else if (cardPlacement == CardPlacement.WEST) {
            // Loop through the numberOfSquares and create a Square for each one.
            for (int i = 0; i < getNumOfSquares(); i++) {
                getSquares().get(i).setCoordinates(this.getX(), this.getY() - i);
            }
            // Set the cave coordinates.
            getSquares().get(1).getCave().setCoordinates(this.getX() - 1, this.getY() - 1);
        }
    }


    /**
     * The overridden render method that will render the Volcano Card in the UI.
     * @param boardGrid: The GameBoard GridPane object
     * @param chitCardGrid: The ChitCard GridPane object.
     */
    @Override
    public void render(GridPane boardGrid, GridPane chitCardGrid) {
        // Loop through the squares array and display them in the UI.
        for (VolcanoSquare volcanoSquare : volcanoSquares) {
            // Call the square display method to display each individual square.
            volcanoSquare.render(boardGrid, chitCardGrid);
        }
    }

    /**
     * Overridden save method to save the Card itself.
     * @return the string savedData of the Card.
     */
    @Override
    public String save() {
        // Append the Card data.
        StringBuilder builder = new StringBuilder();
        // Append a '[' to signify one Card.
        builder.append("[");
        builder.append("cardX: ");
        builder.append(this.getX());
        builder.append(", ");
        builder.append("cardY: ");
        builder.append(this.getY());
        builder.append(", ");
        builder.append("cardPlacement: ");
        builder.append(this.getCardPlacement().toString());
        builder.append(", ");
        builder.append("cardNumOfSquares: ");
        builder.append(getNumOfSquares());
        builder.append(". ");
        // Loop through each volcanoSquare and append its saved data
        for (int i = 0; i < volcanoSquares.size(); i++) {
            // Append a '{' to signify one Square.
            builder.append("{");
            builder.append(volcanoSquares.get(i).save());

            // Append a '}' to signify one Square.
            builder.append("}");

            // If there are more than one Square per Card and not on the last Square, append a comma to it.
            if (this.numberOfSquares > 1 && i < volcanoSquares.size() - 1) {
                builder.append(", ");
            }
        }
        // Append a ']' to signify one Card.
        builder.append("]");

        // Return the constructed saved String.
        return builder.toString();
    }

    /**
     * Overridden load method to load the Card itself.
     * @param savedData: The saved data to load.
     */
    @Override
    public void load(String savedData) {
        // Remove all '[' and ']'.
        String cleanedData = savedData.replace("[", "").replace("]", "");

        // Split the number of Squares per card and the Squares by a period
        String[] cardStrings = cleanedData.split("\\.\\s*");
        // Split the Card information by a comma
        String[] cardInfoStrings = cardStrings[0].split(",\\s*");

        for (String data : cardInfoStrings) {
            // Split the line into its corresponding key-value pair.
            String[] keyValue = data.split(":\\s*", 2);
            String key = keyValue[0].trim();
            String value = keyValue[1].trim();

            // Load the corresponding data into their respective attributes.
            switch (key) {
                case "cardX":
                    this.setX(Integer.parseInt(value));
                    break;
                case "cardY":
                    this.setY(Integer.parseInt(value));
                    break;
                case "cardPlacement":
                    this.setCardPlacement(CardPlacement.valueOf(value));
                    break;
                case "cardNumOfSquares":
                    this.setNumOfSquares(Integer.parseInt(value));
                    break;
            }
        }

        // Split the squares by a closing round bracket followed by a comma and an optional space
        String[] squareStrings = cardStrings[1].split("},\\s*");

        // Check if the number of squares are matching. If no, return an alert
        if (getNumOfSquares() != squareStrings.length) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Incorrect Card Settings");
            alert.setContentText("One of the Cards doesn't have the matching number of Volcano Squares.");
            alert.showAndWait();
            return;
        }

        // Get the difference to determine whether Squares were added (positive) or removed (negative)
        int difference = getNumOfSquares() - volcanoSquares.size();

        // If there were Squares that were removed
        if (difference < 0) {
            // Create a list to store all the VolcanoSquares to be removed.
            List<VolcanoSquare> volSquRemove = new ArrayList<>(volcanoSquares);

            // Loop through each square String.
            for (String squareString : squareStrings) {
                // Find the index of "volcanoX" and "volcanoY"
                int xIndex = squareString.indexOf("volcanoX");
                int yIndex = squareString.indexOf("volcanoY");

                // Extract the substrings after "volcanoX" and "volcanoY"
                String xSubstring = squareString.substring(xIndex + "volcanoX".length() + 2);
                String ySubstring = squareString.substring(yIndex + "volcanoY".length() + 2);

                // Convert the substrings to integers
                int volcanoX = Integer.parseInt(xSubstring.split(",")[0].trim());
                int volcanoY = Integer.parseInt(ySubstring.split(",")[0].trim());

                volSquRemove.removeIf(volSqu -> volcanoX == volSqu.getX() && volcanoY == volSqu.getY());
            }

            // Get the DragonSquareIterator object.
            DragonSquareIterator dragonSquareIterator = MovementManager.getInstance().getDragonSquareIterator();

            // Removes the VolcanoSquares from the Game.
            for (VolcanoSquare volSqu : volSquRemove) {
                // Remove the VolcanoSquares from the Card list and the DragonSquareIterator.
                volcanoSquares.remove(volSqu);
                dragonSquareIterator.removeSquare(volSqu);
            }
        // If there were Squares that were added
        } else if (difference > 0) {
            // Get the DragonSquareIterator object.
            DragonSquareIterator dragonSquareIterator = MovementManager.getInstance().getDragonSquareIterator();
            // Get the index of the first VolcanoSquares in the Card
            int index = dragonSquareIterator.locateSquare(volcanoSquares.get(0));

            // Get the index to add the new Volcano Square(s) in.
            for (VolcanoSquare volcanoSquare : volcanoSquares) {
                index++;    // Increment the index by 1 for each VolcanoSquare the Card has.
            }

            // Create VolcanoSquares to be added
            for (int i = 0; i < difference; i++) {
                VolcanoSquare newSquare = new VolcanoSquare(this.getColour(), this.getColourString(), new BabyDragonCreationStrategy().createDisplayComponent());
                volcanoSquares.add(newSquare); // Add to the volcanoSquares list
                dragonSquareIterator.getSquares().add(index, newSquare); // Add to the Game Board at that index
                index++; // Increment the index by 1
            }
        }

        // Loop through each square String.
        for (int i = 0; i < squareStrings.length; i++) {
            // Call the respective square loading method to load.
            volcanoSquares.get(i).load(squareStrings[i]);
        }
    }

    /**
     * A method to return the numberOfSquares data attribute.
     * @return the number of Squares the Card have.
     */
    public int getNumOfSquares() {
        return numberOfSquares;
    }

    /**
     * A method to set the cardPlacement data attribute.
     * @return the placement of the Card.
     */
    public CardPlacement getCardPlacement() {
        return cardPlacement;
    }

    /**
     * A method to return the squares list data attribute.
     * @return the list of Squares the Card is composed of.
     */
    public List<VolcanoSquare> getSquares() {
        return volcanoSquares;
    }

    /**
     * A method to set the numberOfSquares data attribute.
     * @param numberOfSquares: The number of Squares the Card have.
     */
    public void setNumOfSquares(int numberOfSquares) {
        this.numberOfSquares = numberOfSquares;
    }

    /**
     * A method to set the cardPlacement data attribute.
     * @param cardPlacement: The placement of the Card.
     */
    public void setCardPlacement(CardPlacement cardPlacement) {
        this.cardPlacement = cardPlacement;
    }

    /**
     * A method to set the squares list data attribute.
     * @param volcanoSquares: The list of Squares the Card is composed of.
     */
    public void setSquares(List<VolcanoSquare> volcanoSquares) {
        this.volcanoSquares = volcanoSquares;
    }

    /**
     * A method to add a square to the squares list data attribute.
     * @param volcanoSquare: The Square object to add to the squares list.
     */
    public void addSquare(VolcanoSquare volcanoSquare) {
        getSquares().add(volcanoSquare);
    }
}
