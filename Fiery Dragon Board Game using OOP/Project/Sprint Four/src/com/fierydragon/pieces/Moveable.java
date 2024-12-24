package com.fierydragon.pieces;

import com.fierydragon.components.ChitCard;

/**
 * An interface that enforces the move method to its subclasses.
 * Created by:
 * @author Bryan Wong
 * Modified by:
 * @version 1.0
 * @see ChitCard
 */

public interface Moveable {
    /**
     * A method meant to simulate movement for its children. Enforces it.
     * @param chitCard: The Chit Card that triggered the move.
     */
    void move(ChitCard chitCard);
}
