package com.fierydragon.movement;

import com.fierydragon.pieces.Dragon;
import com.fierydragon.utils.TurnManager;
import com.fierydragon.volcano.Card;
import com.fierydragon.volcano.GameManager;
import com.fierydragon.volcano.Square;
import com.fierydragon.volcano.VolcanoSquare;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.util.*;

/**
 * An Iterator Design Pattern that iterates over the GameBoard to determine movement, holding a collections of Squares and
 * Dragons to determine which Dragon is at which Square.
 * Created by:
 * @author Bryan Wong
 * Modified by: Po Han Tay, Vincent Tanuwidjaja
 * @version 1.0
 * @see Card
 * @see Dragon
 * @see Square
 * @see VolcanoSquare
 */

public class DragonSquareIterator implements Iterable<Square> {
    /**
     * A List of Squares in the GameBoard.
     */
    List<Square> squares;
    /**
     * A mapping of Squares to the Dragons.
     */
    private final Map<Square, Dragon> squareToDragon;
    /**
     * A mapping of Dragons to the Square.
     */
    private final Map<Dragon, Square> dragonToSquare;
    /**
     * An integer representing the index of the Iterator.
     */
    private int index;

    /**
     * DragonSquareIterator Constructor.
     */
    public DragonSquareIterator() {
        // Create a list of movable squares for the movement of Dragon.
        this.squares = new ArrayList<>();
        // Create new HashMaps for the mapping.
        this.squareToDragon = new HashMap<>();
        this.dragonToSquare = new HashMap<>();
        setIndex(0);    // Default set the index to zero.
    }

    /**
     * Add each movable Squares into the list.
     * @param card: The Card containing all the Squares to be placed.
     */
    public void addSquare(Card card) {
        // Add all Squares into the list.
        for (VolcanoSquare square : card.getSquares()) {
            getSquares().add(square);

            // Checks if there is a Cave or not. If yes, add it into the list as well.
            if (square.getCave() != null) {
                getSquares().add(square.getCave());
                getSquares().add(square);   // Add the current square for the movement of other Dragon
            }
        }
    }

    /**
     * Remove the Square from the list.
     * @param squareToRemove: The Square to remove.
     */
    public void removeSquare(Square squareToRemove) {
        // Remove the square from the list.
        this.getSquares().remove(squareToRemove);
    }

    /**
     * Add a Dragon at the given Square.
     * @param dragon: the Dragon to place.
     * @param square: The Square to place the Dragon.
     * @throws IllegalArgumentException if the Dragon is already placed or there is already a Dragon at the target Square.
     */
    public void add(Dragon dragon, Square square) {
        // Throw exception if a Dragon already exist or if there is a Dragon at the target Square.
        if(dragonToSquare.containsKey(dragon))
            throw new IllegalArgumentException();
        if(squareToDragon.containsKey(square))
            throw new IllegalArgumentException();

        // Put the dragon and square in the HashMaps.
        dragonToSquare.put(dragon, square);
        squareToDragon.put(square, dragon);
    }

    /**
     * Moves an existing Dragon to a new Square.
     * @param dragon: The Dragon to move.
     * @param newSquare: The Dragon's destination.
     */
    public void move(Dragon dragon, Square newSquare) {
        // Only move if there is no Dragon at the new Square.
        if(!isDragonAt(newSquare)) {
            // Get the current Square the Dragon is at and remove it from the HashMap.
            Square oldSquare = dragonToSquare.get(dragon);
            squareToDragon.remove(oldSquare);
            // Place the Dragon at the new Square.
            dragonToSquare.put(dragon, newSquare);
            squareToDragon.put(newSquare, dragon);
            // Set the dragon new coordinates.
            dragon.setCoordinates(newSquare.getX(), newSquare.getY());
            // Update the Dragon position on the grid board
            dragon.render(GameManager.getInstance().getBoardGrid(), GameManager.getInstance().getChitCardGrid());
        }
        // Else if there's a Dragon at the new Square, attack the Dragon and force it to skip a turn.
        else {
            Dragon attackedDragon = squareToDragon.get(newSquare);
            attackedDragon.setStunned(true);        // Stun the attacked Dragon.
            // Create an alert about the attack.
            Alert alert = new Alert(Alert.AlertType.NONE);
            alert.getDialogPane().getButtonTypes().add(ButtonType.OK);
            alert.setTitle(String.format("%s Dragon Attacked %s Dragon", dragon.getColourString(), attackedDragon.getColourString()));
            alert.setContentText(String.format("%s Dragon is stunned and skips a turn to recover.", attackedDragon.getColourString()));
            alert.showAndWait();
            // Go to the next Dragon's turn.
            TurnManager.getInstance().nextTurn();
        }
    }

    /**
     * Clear all the lists in the DragonSquareIterator.
     */
    public void clearAll() {
        // Clear all lists.
        this.getSquareToDragon().clear();
        this.getDragonToSquare().clear();
        this.getSquares().clear();
    }

    /**
     * Locates and returns the index of the matching Square in the list.
     * @param square: The square the Dragon is currently at.
     * @return the index of the current square in the list.
     */
    public int locateSquare(Square square) {
        int num = squares.indexOf(square);
        return num;
    }

    /**
     * Returns true if the particular Dragon exists in the system.
     * @param dragon: The Dragon to look for.
     * @return true if and only if Dragon is somewhere in the system.
     */
    public boolean contains(Dragon dragon) {
        return dragonToSquare.containsKey(dragon);
    }

    /**
     * Returns true if a Dragon is at the given Square.
     * @param square: The Square to check.
     * @return true if and only if a Dragon is at the given Square.
     */
    public boolean isDragonAt(Square square) {
        return squareToDragon.containsKey(square);
    }

    /**
     * Returns a reference to the Dragon at the given Square, if there is one.
     * @param square: The Square to check.
     * @return a reference to the Dragon, or null if there isn't one.
     */
    public Dragon getDragonAt(Square square) {
        return squareToDragon.get(square);
    }

    /**
     * Returns a reference to the Square containing the given Dragon.
     * @param dragon: The Dragon to look for.
     * @return the Square containing Dragon.
     */
    public Square locationOf(Dragon dragon) {
        return dragonToSquare.get(dragon);
    }

    /**
     * A method to return the List of Squares data attribute.
     * @return the list of Squares in the GameBoard.
     */
    public List<Square> getSquares() {
        return squares;
    }

    /**
     * A method to return the Square according to the index.
     * @return the Square in the GameBoard.
     */
    public Square getSquare(int index) {
        return getSquares().get(index);
    }

    /**
     * A method to return the Square according to the coordinates.
     * @return the Square in the GameBoard.
     */
    public Square getSquareByCoords(int x, int y) {
        // Iterate through each Square in the GameBoard.
        for (Square square : getSquares()) {
            // Checks if the Square's coordinates match the input coordinates.
            if (square.getX() == x && square.getY() == y) {
                return square; // Return the Square if coordinates matched.
            }
        }

        return null; // Return null if no Squares coordinates matched.
    }

    /**
     * A method to return the Map of Square to Dragon data attribute.
     * @return the mapping of Squares to the Dragons.
     */
    public Map<Square, Dragon> getSquareToDragon() {
        return squareToDragon;
    }

    /**
     * A method to return the Map of Square to Dragon data attribute.
     * @return the mapping of Squares to the Dragons.
     */
    public Map<Dragon, Square> getDragonToSquare() {
        return dragonToSquare;
    }

    /**
     * A method to return the index data attribute.
     * @return the index of the Iterator.
     */
    public int getIndex() {
        return index;
    }

    /**
     * A method to set the index data attribute.
     * @param index: The index of the Iterator.
     */
    public void setIndex(int index) {
        this.index = index;
    }

    /**
     * Class to allow iterating over all Squares in the Game Board
     * This allows Dragons to move to new Squares.
     */
    class SquareIterator implements Iterator<Square> {
        /**
         * A List of all Squares in the GameBoard.
         */
        List<Square> squares;

        /**
         * SquareIterator Constructor.
         * @param squares: The list of all Squares in the GameBoard to iterate over.
         */
        public SquareIterator(List<Square> squares) {
            this.squares = new ArrayList<>();
            // Concatenate the squares list to the one created above.
            this.squares.addAll(squares);
        }

        /**
         * Overridden hasNext method.
         * @return True if there exist a next element in the Iterator.
         */
        @Override
        public boolean hasNext() {
            return index < squares.size();
        }

        /**
         * Overridden next method.
         * Iterate forwards through the Iterator.
         * @return the Square instance on the next position.
         */
        @Override
        public Square next() {
            // Change the index to the next one, returning the square on the next one.
            index += 1;

            // If reached last square, reset to the front of the list again.
            if (!hasNext()) {
                index = 0;      // Reset the position to the beginning if at the end of the list
            }

            return squares.get(index);
        }

        /**
         * Checks if there exists a previous Square.
         * @return True if there exist a previous element in the Iterator.
         */
        public boolean hasPrev() {
            return index > -1;
        }

        /**
         * Iterate backwards through the Iterator.
         * @return the Square instance on the previous position.
         */
        public Square prev() {
            // Change the index to the previous one.
            index -= 1;

            // If reached first square, reset to the last of the list again.
            if (!hasPrev()) {
                index = squares.size() - 1;      // Reset the position to the end if at the start of the list
            }

            return squares.get(index);
        }

        /**
         * A method to return the squares list data attribute.
         * @return the list all Squares in the GameBoard.
         */
        public List<Square> getSquares() {
            return squares;
        }
    }

    /**
     * Overwrites the iterator method with the Iterator I created.
     */
    @Override
    public Iterator<Square> iterator() {
        return new SquareIterator(squares);
    }
}
