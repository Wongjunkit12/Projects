package com.fierydragon.movement;

import com.fierydragon.components.ChitCard;
import com.fierydragon.pieces.Dragon;
import com.fierydragon.utils.TurnManager;
import com.fierydragon.volcano.GameManager;
import com.fierydragon.volcano.Square;
import com.fierydragon.volcano.VolcanoSquare;

import java.util.Iterator;

/**
 * Class represents the logic of how the Dragon Piece will move backward in the game board
 * Created by:
 * @author Po Han Tay
 * Modified by: Vincent Tanuwidjaja
 * @version 1.0
 * @see ChitCard
 * @see Dragon
 * @see MovementStrategy
 */

public class BackwardMovement implements MovementStrategy {

    /**
     * Moves the Dragon Piece in a counter-clockwise direction based on the number of characters
     * @param chitCard: The Chit Card that triggered the move.
     * @param currentDragon: The current Dragon to be moved.
     * */
    @Override
    public void createMovement(ChitCard chitCard, Dragon currentDragon) {
        // Get the dragonSquareIterator instance.
        DragonSquareIterator dragonSquareIterator = MovementManager.getInstance().getDragonSquareIterator();
        // Get the current location of the Dragon
        Square oldSquare = dragonSquareIterator.locationOf(currentDragon);
        // Set the dragonSquareIterator index to the current square from its list
        dragonSquareIterator.setIndex(dragonSquareIterator.locateSquare(oldSquare));
        // Set the new location variable before executing the movement
        Square newLocation = dragonSquareIterator.locationOf(currentDragon);

        // If the Chit Card Displayable is a Knight, move the Dragon token to the nearest free Cave behind it
        if (chitCard.getDisplay().getDisplay().equals("KN")) {
            newLocation = moveBackToFreeCave(dragonSquareIterator, currentDragon, newLocation);
        // Otherwise, move backwards according to the number of Displayables on the Chit Card
        } else {
            newLocation = moveBack(dragonSquareIterator, currentDragon, newLocation, chitCard);
        }

        // Update the Dragon position
        dragonSquareIterator.move(currentDragon, newLocation);
    }

    /**
     * Moves the Dragon backward based on the number of steps.
     * @param dragonSquareIterator: Iterator for the dragon tokens and squares.
     * @param currentDragon: The current Dragon.
     * @param newLocation: The new location for the Dragon to move.
     * @param chitCard: The chosen Chit Card.
     * @return The new Square location for the Dragon to move to.
     */
    public Square moveBack(DragonSquareIterator dragonSquareIterator, Dragon currentDragon, Square newLocation, ChitCard chitCard) {
        // Get the squareIterator instance.
        Iterator<Square> squareIterator = dragonSquareIterator.iterator();

        // Get the number of steps the Dragon will take
        int numSteps = chitCard.getNumOfDisplayables();

        // Execute the backward movement based on the number of Animal Displayables on the chosen Chit Card
        while (numSteps > 0) {
            newLocation = ((DragonSquareIterator.SquareIterator) squareIterator).prev(); // Get the next location

            // Checks if the next location is another Dragon's Cave
            if (newLocation.isCave() && !(newLocation.getX() == currentDragon.getHomeCave().getX() && newLocation.getY() == currentDragon.getHomeCave().getY())) {
                // If yes, move along the other Dragon's Cave
                numSteps += 2; // Add 2 more steps to counter the reduced steps from entering and exiting the cave
                // Checks if the Dragon has passed their Cave or not
            } else if (currentDragon.hasPassedCave()) {
                // If yes, compare the next location with the square outside its Cave by obtaining it
                Square homeCave = dragonSquareIterator.getSquareByCoords(currentDragon.getHomeCave().getX(), currentDragon.getHomeCave().getY());
                int homeCaveIndex = dragonSquareIterator.locateSquare(homeCave);
                Square behindPassedCaveSquare = dragonSquareIterator.getSquare(homeCaveIndex + 1);

                // Checks if Dragon has passed their square outside its Cave
                if (behindPassedCaveSquare.getX() == newLocation.getX() && behindPassedCaveSquare.getY() == newLocation.getY()) {
                    currentDragon.setHasPassedCave(false); // Set the Dragon to not pass their own Cave
                }
                // If the Dragon has not pass their own Cave
            } else if (!currentDragon.hasPassedCave()) {
                newLocation = currentDragon.getHomeCave(); // Set the next location to its own Cave
            }

            numSteps -= 1; // Decrement number of steps to take
        }

        return newLocation;
    }

    /**
     * Moves the Dragon backward to the nearest unoccupied Cave.
     * @param dragonSquareIterator: Iterator for the dragon tokens and squares.
     * @param currentDragon: The current Dragon.
     * @param newLocation: The new location for the Dragon to move.
     * @return The nearest unoccupied Cave location for the Dragon to move to.
     */
    public Square moveBackToFreeCave(DragonSquareIterator dragonSquareIterator, Dragon currentDragon, Square newLocation) {
        // Get the squareIterator instance.
        Iterator<Square> squareIterator = dragonSquareIterator.iterator();
        // Boolean flag to check if the Dragon token is at an unoccupied Cave.
        boolean atFreeCave = false;

        // Move backwards continuously until the Dragon token is at an unoccupied Cave
        while (!atFreeCave) {
            newLocation = ((DragonSquareIterator.SquareIterator) squareIterator).prev(); // Get the next location
            // Get the current location of the Dragon
            Square currentSquare = dragonSquareIterator.locationOf(currentDragon);

            // Checks if this next location is an unoccupied Cave
            if (newLocation.isCave() && !dragonSquareIterator.isDragonAt(newLocation)) {
                atFreeCave = true; // Set flag to true if it is
            // Checks if the current Square is a Volcano Square
            } else if (!currentSquare.isCave()) {
                VolcanoSquare volcanoSquare = (VolcanoSquare) currentSquare;

                // If the Dragon is outside its Cave, move it back to its Cave if its unoccupied
                if (volcanoSquare.getCave() != null
                        && (volcanoSquare.getCave().getX() == currentDragon.getHomeCave().getX()
                            && volcanoSquare.getCave().getY() == currentDragon.getHomeCave().getY())
                        && !dragonSquareIterator.isDragonAt(currentDragon.getHomeCave())) {
                    newLocation = currentDragon.getHomeCave();
                    atFreeCave = true;
                }
            }
        }

        return newLocation;
    }
}
