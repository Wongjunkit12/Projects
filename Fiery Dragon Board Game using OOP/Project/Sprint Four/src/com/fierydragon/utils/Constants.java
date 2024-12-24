package com.fierydragon.utils;

/**
 * A class representing containing all the Constants used in the project.
 * Created by:
 * @author Bryan Wong
 * Modified by: Po Han Tay
 * @version 1.0
 */

public class Constants {
    /**
     * The width of the Square.
     */
    public static final int SQUARE_WIDTH = 75;
    /**
     * The height of the Square.
     */
    public static final int SQUARE_HEIGHT = 75;
    /**
     * The number of Volcano Cards the Game Board has.
     */
    public static final int NUM_CARDS = 8;
    /**
     * The number of Squares each Volcano Card has.
     */
    public static final int NUM_SQUARES = 3;
    /**
     * The maximum number of Animals types in a Game Board.
     */
    public static final int NUM_ANIMAL_TYPES = 4;
    /**
     * The starting x coordinates of each card
     */
    public static final int[] startingXCoords = {2, 4, 8, 8, 8, 6, 2, 2};
    /**
     * The starting y coordinates of each card
     */
    public static final int[] startingYCoords = {2, 2, 2, 4, 8, 8, 8, 6};
    /**
     * The maximum number of Animals each type can have on the Game Board.
     */
    public static final int MAX_NUM_ANIMALS = (NUM_CARDS * NUM_SQUARES) / NUM_ANIMAL_TYPES;
    /**
     * The number of Caves.
     */
    public static final int NUM_CAVES = 4;
    /**
     * The number of Chit Cards.
     */
    public static final int NUM_CHIT_CARDS = 17;
    /**
     * The number of columns of the Chit Card
     */
    public static final int COLUMNS = 4;
    /**
     * The number of Pirate Chit Cards.
     */
    public static final int NUM_PIRATES = 4;
    /**
     * The number of Knight Chit Cards.
     */
    public static final int NUM_KNIGHTS = 1;
    /**
     * The maximum number of animals allowed on a Chit Card.
     */
    public static final int MAX_ANIMAL_CHIT_CARDS = 3;
    /**
     * The maximum number of pirates allowed on a Chit Card.
     */
    public static final int MAX_PIRATE = 2;
    /**
     * The maximum number of knights allowed on a Chit Card.
     */
    public static final int MAX_KNIGHT = 1;
}
