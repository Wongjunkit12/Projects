package com.fierydragon.movement;

import com.fierydragon.components.ChitCard;
import com.fierydragon.pieces.Dragon;
import com.fierydragon.utils.TurnManager;
import com.fierydragon.volcano.Square;

import java.util.Iterator;

/**
 * Class represents the logic of how the Dragon Piece will move forward in the game board
 * Created by:
 * @author Po Han Tay
 * Modified by: Vincent Tanuwidjaja
 * @version 1.0
 * @see ChitCard
 * @see Dragon
 * @see MovementStrategy
 */

public class ForwardMovement implements MovementStrategy {

    /**
     * Moves the Dragon Piece in a clockwise direction based on the number of characters
     * @param chitCard: The Chit Card that triggered the move.
     * @param currentDragon: The current Dragon to be moved.
     * */
    @Override
    public void createMovement(ChitCard chitCard, Dragon currentDragon) {
        // Get the dragonSquareIterator instance.
        DragonSquareIterator dragonSquareIterator = MovementManager.getInstance().getDragonSquareIterator();
        // Get the squareIterator instance.
        Iterator<Square> squareIterator = dragonSquareIterator.iterator();

        // Get the current location of the Dragon
        Square oldSquare = dragonSquareIterator.locationOf(currentDragon);
        // Set the dragonSquareIterator index to the current square from its list
        dragonSquareIterator.setIndex(dragonSquareIterator.locateSquare(oldSquare));

        // Set the new location variable before executing the movement
        Square newLocation = dragonSquareIterator.locationOf(currentDragon);
        // Get the number of steps the Dragon will take
        int numSteps = chitCard.getNumOfDisplayables();

        // Execute the forward movement based on the number of Animal Displayables on the chosen Chit Card
        while (numSteps > 0) {
            newLocation = squareIterator.next(); // Get the next location

            // Checks if the next location is a Cave or not
            if (newLocation.isCave()) {
                // If yes, check if the Cave belongs to the current Dragon in turn
                if (newLocation.getX() == currentDragon.getHomeCave().getX() && newLocation.getY() == currentDragon.getHomeCave().getY()) {
                    // If yes, check for win condition
                    if (!currentDragon.hasPassedCave()) {
                        numSteps += 2; // Make sure it does not go back in its cave after it has just exited
                        currentDragon.setHasPassedCave(true); // Set the Dragon to pass their own Cave
                    // If the Dragon only has 1 step left, it means they win
                    } else if (numSteps == 1) {
                        // Move the Dragon Position to the Cave
                        dragonSquareIterator.move(currentDragon, newLocation);
                        TurnManager.getInstance().win(currentDragon); // Activate the win condition
                        return;
                    // Otherwise, they overshoot their own Cave
                    } else {
                        // Cancel their turn
                        TurnManager.getInstance().nextTurn();
                        return;
                    }
                // If no, move along the other Dragon's Cave
                } else {
                    numSteps += 2; // Add 2 more steps to counter the reduced steps from entering and exiting the cave
                }
            // Checks if the Dragon has passed their own cave or not
            } else if (!currentDragon.hasPassedCave()) {
                // If no, compare the next location with the square outside its Cave by obtaining it
                Square homeCave = dragonSquareIterator.getSquareByCoords(currentDragon.getHomeCave().getX(), currentDragon.getHomeCave().getY());
                int homeCaveIndex = dragonSquareIterator.locateSquare(homeCave);
                Square hasPassedCaveSquare = dragonSquareIterator.getSquare(homeCaveIndex + 2);

                // Checks if Dragon has passed their square outside its Cave
                if (hasPassedCaveSquare.getX() == newLocation.getX() && hasPassedCaveSquare.getY() == newLocation.getY()) {
                    currentDragon.setHasPassedCave(true); // Set the Dragon to pass their own Cave
                }
            }

            numSteps -= 1; // Decrement number of steps to take
        }

        // Update the Dragon position
        dragonSquareIterator.move(currentDragon, newLocation);
    }
}
