package com.fierydragon.pieces;


import com.fierydragon.components.Cave;
import com.fierydragon.movement.MovementManager;
import com.fierydragon.utils.TurnManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * A Factory Design Pattern that o will create the Dragons based on the number of Players (2-4) and link them to their respective
 * Caves.
 * Created by:
 * @author Bryan Wong
 * Modified by:
 * @version 1.0
 * @see Dragon
 * @see Cave
 * @see TurnManager
 */

public class DragonFactory {
    /**
     * An instance of itself so that only one DragonFactory instance is created. Default set to null.
     */
    private static DragonFactory dragonFactory = null;
    /**
     * An integer representing the number of Players/Dragons.
     */
    private int numberOfDragons;

    /**
     * DragonFactory Constructor.
     * @param numberOfDragons: The number of Players/Dragons.
     */
    public DragonFactory(int numberOfDragons) {
        setNumberOfDragons(numberOfDragons);
    }

    /**
     * A method to create the Dragons based on the number of Players playing. Will attach their respective Caves to them.
     * @param caves: The list of Caves in the GameBoard.
     */
    public List<Dragon> createDragons(List<Cave> caves) {
        // Create empty list of dragons to store the dragon players.
        List<Dragon> dragons = new ArrayList<>();
        // If there are two players, setOpposite to true to set the Dragon to be at the opposite of the board.
        boolean setOpposite = false;
        if (this.getNumberOfDragons() <= 2) {
            setOpposite = true;
        }

        // If need to set at opposite row, choose to randomly placed vertical wise or horizontal wise.
        // Set horizontal to true if its horizontal. If it false means its vertical.
        boolean horizontal = true;
        if (setOpposite) {
            // Create a Random class.
            Random rand = new Random();
            // Obtain a number between [0 - 1].
            int n = rand.nextInt(2) + 1;
            // Choose to be place the Dragons opposite on horizontal-wise or vertical-wise.
            horizontal = switch (n) {
                case 1 -> true;
                case 2 -> false;
                default -> horizontal;
            };
        }

        int index = 0;
        // Create all the dragons and their respective caves.
        for (int i = 0; i < getNumberOfDragons(); i++) {
            // If opposite then modify the index based on horizontal or not.
            if (setOpposite) {
                if (horizontal) {
                    index = i * 2;
                }
                else {
                    index = (i * 2) + 1;
                }
            }
            // Else just set index as i as normal.
            else {
                index = i;
            }

            // Create a new Dragon instance with their respective Cave.
            Dragon dragon = new Dragon(caves.get(index));
            // Set the x and y coordinates to be the same as their Cave.
            dragon.setCoordinates(dragon.getHomeCave().getX(), dragon.getHomeCave().getY());
            // Add the Dragons to the List.
            dragons.add(dragon);
            // Add the Dragon and their respective home Cave into the Dragon to Square Iterator.
            MovementManager.getInstance().getDragonSquareIterator().add(dragon, caves.get(index));
            // Add the dragon to the turn order list in a clockwise direction.
            TurnManager.getInstance().getDragonIterator().add(dragon);
        }

        return dragons;     // Return the list of dragons.
    }

    /**
     * A method to return the numberOfDragons data attribute.
     * @return the number of Players/Dragons.
     */
    public int getNumberOfDragons() {
        return numberOfDragons;
    }

    /**
     * A method to set the numberOfDragons data attribute.
     * @param numberOfDragons: The number of Players/Dragons.
     */
    public void setNumberOfDragons(int numberOfDragons) {
        this.numberOfDragons = numberOfDragons;
    }

    /**
     * Returns the instance of the DragonFactory stored as a data attribute.
     * If one has not been created yet, create a new instance of DragonFactory.
     * @param numberOfDragons The number of Players/Dragon playing the Fiery Dragon Board Game.
     * @return the DragonFactory instance.
     */
    public static DragonFactory getInstance(int numberOfDragons) {
        // If an instance of DragonFactory has not been created yet.
        if (dragonFactory == null) {
            // Create a new instance of dragonFactory.
            dragonFactory = new DragonFactory(numberOfDragons);
        }
        // Return the dragonFactory instance, either newly created or stored currently as a data attribute.
        return dragonFactory;
    }
}
