package com.fierydragon.pieces;

import com.fierydragon.components.Cave;
import com.fierydragon.components.ChitCard;
import com.fierydragon.components.UIComponent;
import com.fierydragon.movement.*;
import com.fierydragon.utils.Constants;
import com.fierydragon.utils.TurnManager;
import com.fierydragon.volcano.Square;
import com.fierydragon.volcano.VolcanoSquare;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

/**
 * A class representing the Dragon Piece/Token. Inherits from Piece.
 * Created by:
 * @author Bryan Wong
 * Modified by: Vincent Tanuwidjaja, Po Han Tay
 * @version 1.0
 * @see Cave
 * @see ChitCard
 * @see UIComponent
 * @see Constants
 * @see TurnManager
 * @see VolcanoSquare
 */


public class Dragon extends UIComponent implements Moveable {
    /**
     * The home Cave of the Dragon.
     */
    private Cave homeCave;

    /**
     * Circle object that will be drawn on the grid
     * */
    private Circle dragon;

    /**
     * Boolean object denoting if Dragon has moved beyond the square outside its cave
     * */
    private boolean hasPassedCave = false;
    /**
     * Boolean object denoting if Dragon is stunned and its turn is skipped
     * */
    private boolean stunned = false;
    /**
     * The Dragon constructor.
     * Calls the super class to set the inherited attributes.
     * @param cave: The home Cave of the Dragon piece/token.
     */
    public Dragon(Cave cave) {
        // Call the parent constructor to set the parent attributes and the home cave.
        super(cave.getX(), cave.getY(), cave.getColour(), cave.getColourString(), "Dragon");
        setHomeCave(cave);
    }

    /**
     * The overridden move method that will move the Dragon Piece based on the Square they're standing on.
     * @param chitCard: The Chit Card that triggered the move.
     */
    @Override
    public void move(ChitCard chitCard) {
        // Get the dragonSquareIterator instance.
        DragonSquareIterator dragonSquareIterator = MovementManager.getInstance().getDragonSquareIterator();
        // Get the current Square the Dragon is standing on.
        Square currentSquare = dragonSquareIterator.locationOf(this);
        // Get the status on whether the chosen Chit Card Displayable is a Pirate Dragon or Knight
        boolean notVolSquDisp = chitCard.getDisplay().getDisplay().equals("PD") || chitCard.getDisplay().getDisplay().equals("KN");

        // If the Displayable from the Chit Card is not a Pirate Dragon or Knight and
        // the Displayable from the Chit Card is not matching to the one on the Square, progress to the next turn
        if (!notVolSquDisp && (!chitCard.getDisplay().getDisplay().equals(currentSquare.getDisplay().getDisplay()))) {
            TurnManager.getInstance().nextTurn();   // Execute the next turn.
        // If the Displayable from the Chit Card is a Pirate Dragon or Knight and the Dragon is not in a Cave, move backwards
        } else if (notVolSquDisp && !currentSquare.isCave()) {
            MovementStrategy movement = new BackwardMovement();
            movement.createMovement(chitCard, this);   // Execute the backward movement.
        // If the Displayable from the Chit Card is matching to the one on the Square, move forwards
        } else if (chitCard.getDisplay().getDisplay().equals(currentSquare.getDisplay().getDisplay())) {
            MovementStrategy movement = new ForwardMovement();
            movement.createMovement(chitCard, this);   // Execute the forward movement.
        }
    }

    /**
     * The overridden render method that will render the Dragon Piece in the UI.
     * @param boardGrid: The GameBoard GridPane object
     * @param chitCardGrid: The ChitCard GridPane object.
     */
    @Override
    public void render(GridPane boardGrid, GridPane chitCardGrid) {
        // Remove the existing Circle if it exists.
        if (dragon != null) {
            boardGrid.getChildren().remove(dragon);
        }

        // Create a new Circle to represent a dragon and fill it up with the colour and border is black.
        dragon = new Circle((double) Constants.SQUARE_WIDTH / 4);
        dragon.setFill(this.getColour());
        dragon.setStroke(Color.BLACK);
        dragon.setStrokeWidth(3);
        // Centralise the dragon.
        dragon.setTranslateX( (double) Constants.SQUARE_WIDTH / 4);
        // Add it to the grid.
        boardGrid.add(dragon, this.getX(), this.getY());
    }

    /**
     * Overridden save method to save the Dragon itself.
     * @return the string savedData of the Dragon.
     */
    @Override
    public String save() {
        // Append the Card data.
        StringBuilder builder = new StringBuilder();
        builder.append("dragonX: ");
        builder.append(this.getX());
        builder.append(", ");
        builder.append("dragonY: ");
        builder.append(this.getY());
        builder.append(", ");
        builder.append("dragonColour: ");
        builder.append(this.getColour().toString());
        builder.append(", ");
        builder.append("dragonColourName: ");
        builder.append(this.getColourString());
        builder.append(", ");
        builder.append("dragonHasPassedCave: ");
        builder.append(this.hasPassedCave());
        builder.append(", ");
        builder.append("dragonStunned: ");
        builder.append(this.isStunned());

        // Return the constructed saved String.
        return builder.toString();
    }

    /**
     * Overridden load method to load the Dragon itself.
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
                case "dragonX":
                    this.setX(Integer.parseInt(value));
                    break;
                case "dragonY":
                    this.setY(Integer.parseInt(value));
                    break;
                case "dragonColour":
                    this.setColour(Color.valueOf(value));
                    break;
                case "dragonColourName":
                    this.setColourString(value);
                    break;
                case "dragonHasPassedCave":
                    this.setHasPassedCave(Boolean.parseBoolean(value));
                    break;
                case "dragonStunned":
                    this.setStunned(Boolean.parseBoolean(value));
                    break;
            }
        }
    }

    /**
     * A method to return the homeCave data attribute.
     * @return the home Cave of the Dragon.
     */
    public Cave getHomeCave() {
        return homeCave;
    }

    /**
     * A method to set the homeCave data attribute.
     * @param homeCave: The home Cave of the Dragon.
     */
    public void setHomeCave(Cave homeCave) {
        this.homeCave = homeCave;
    }

    /**
     * A method to get the hasPassedCave data attribute.
     */
    public boolean hasPassedCave() {
        return hasPassedCave;
    }

    /**
     * A method to set the hasPassedCave data attribute to true.
     */
    public void setHasPassedCave(boolean hasPassedCave) {
        this.hasPassedCave = hasPassedCave;
    }

    /**
     * A method to set the stunned data attribute.
     * @param stunned: Whether the dragon is stunned.
     */
    public void setStunned(boolean stunned) {
        this.stunned = stunned;
    }

    /**
     * A method to check the stunned data attribute.
     */
    public boolean isStunned() {
        return stunned;
    }
}
