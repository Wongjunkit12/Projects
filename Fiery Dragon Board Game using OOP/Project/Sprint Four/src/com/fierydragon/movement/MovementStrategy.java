package com.fierydragon.movement;

import com.fierydragon.components.ChitCard;
import com.fierydragon.pieces.Dragon;

/**
 * An interface that sets the movement of the Dragon Piece (forward or backward)
 * Created by:
 * @author Po Han Tay
 * Modified by:
 * @version 1.0
 * @see ChitCard
 * @see Dragon
 */

public interface MovementStrategy {

    /**
     * Move the current Dragon Piece based on the chosen ChitCard around the board.Must be implemented by any class that
     * implements this interface
     * @param chitCard: The Chit Card that triggered the move.
     * @param currentDragon: The current Dragon to be moved.
     * */
    void createMovement(ChitCard chitCard, Dragon currentDragon);

}
