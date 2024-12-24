package com.fierydragon.pieces;

import com.fierydragon.utils.TurnManager;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class DragonIterator implements Iterator<Dragon> {
    /**
     * A List of Dragons representing all the Players.
     */
    List<Dragon> dragons;
    /**
     * An integer representing the current position of the Iterator.
     */
    private int currentPosition;

    /**
     * DragonIterator Constructor.
     */
    public DragonIterator() {
        this.dragons = new ArrayList<>();
    }

    /**
     * Overridden hasNext method.
     * @return True if win condition has not been met yet and there exist a next element in the Iterator.
     */
    @Override
    public boolean hasNext() {
        return !TurnManager.getInstance().isWinCondition() && currentPosition < dragons.size();
    }

    /**
     * Overridden next method.
     * If win condition has not been met yet, continue iterating through the Iterator.
     * @return the Dragon instance on the next position.
     */
    @Override
    public Dragon next() {
        // If reached last dragon and game has not been won yet, reset to the front of the list again.
        if (!hasNext()) {
            currentPosition = 0;    // Reset the position to the beginning if at the end of the list
        }
        // Change the currentPosition to the next one, returning the dragon on the next one.
        currentPosition = (currentPosition + 1) % dragons.size();

        return dragons.get(currentPosition);
    }

    /**
     * Add a Dragon to the dragons list
     * @param dragon: the Dragon to add.
     */
    public void add(Dragon dragon) {
        dragons.add(dragon);
    }

    /**
     * A method to get the current player's turn Dragon token.
     * @return the Dragon to whose turn it is.
     */
    public Dragon getCurrent() {
        return dragons.get(getCurrentPosition());
    }

    /**
     * A method to return the dragons list data attribute.
     * @return the list of Dragons representing all the Players.
     */
    public List<Dragon> getDragons() {
        return dragons;
    }

    /**
     * A method to return the currentPosition list data attribute.
     * @return the current position of the Iterator.
     */
    public int getCurrentPosition() {
        return currentPosition;
    }

    /**
     * A method to set the currentPosition list data attribute.
     * @param currentPosition: The current position of the Iterator.
     */
    public void setCurrentPosition(int currentPosition) {
        this.currentPosition = currentPosition;
    }
}
