package com.fierydragon.volcano;

import com.fierydragon.components.Cave;
import com.fierydragon.display.Displayable;
import com.fierydragon.display.creations.CreationStrategy;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;


public class CardBuilder {
    /**
     * The Card object to build.
     */
    private Card card;
    /**
     * A boolean to determine if the Card is cut or not.
     */
    private boolean isCut;
    /**
     * The Instance of the Cave if one exist.
     */
    private Cave cave;
    /**
     * The list of displayCreationStrategies.
     */
    private List<CreationStrategy> displayCreationStrategies;

    /**
     * CardBuilder Constructor.
     * Creates a new Card with the inputs.
     * Creates the Animals being displayed on the Card based on the Strategy order provided.
     * @param colour: The Color of the Card.
     * @param colourString: The String representation of the colour of the Card.
     * @param numberOfSquares: The number of squares a Card has.
     * @param displayCreationStrategies: The list of Creation Strategies for uncut Volcano Cards.
     * @param cardPlacement: The placement of the cards.
     */
    public CardBuilder(Color colour, String colourString, int numberOfSquares, List<CreationStrategy> displayCreationStrategies, CardPlacement cardPlacement) {
        // Set the parent attributes and other attributes.
        setCard(new Card(colour, colourString, numberOfSquares, cardPlacement));
        setCut(false);      // Default set to false.
        setCreationStrategy(new ArrayList<>(displayCreationStrategies));
    }

    /**
     * Overloaded CardBuilder Constructor.
     * Creates a new Card with the inputs.
     * Creates the Animals being displayed on the Card based on the Strategy order provided.
     * @param colour: The Color of the Card.
     * @param colourString: The String representation of the colour of the Card.
     * @param numberOfSquares: The number of squares a Card has.
     * @param displayCreationStrategies: The list of Creation Strategies for cut Volcano Cards.
     * @param cardPlacement: The placement of the cards.
     * @param cave: The cave instance if one exists in the card. For cut Volcano Cards only.
     */
    public CardBuilder(Color colour, String colourString, int numberOfSquares, List<CreationStrategy> displayCreationStrategies, CardPlacement cardPlacement, Cave cave) {
        // Set the parent attributes and other attributes.
        setCard(new Card(colour, colourString, numberOfSquares, cardPlacement));
        setCut(false);      // Default set to false.
        setCave(cave);
        setCreationStrategy(new ArrayList<>(displayCreationStrategies));
    }

    /**
     * Populate the Card with the Animal based on the Creation Strategies given
     */
    public VolcanoSquare buildSquares() {
        // Get the first Animal to create.
        CreationStrategy displayCreationStrategy = displayCreationStrategies.remove(0);
        Displayable display = displayCreationStrategy.createDisplayComponent();

        // Create a VolcanoSquare with the Cave if it's cut, without it otherwise.
        VolcanoSquare square = ((isCut()) ? new VolcanoSquare(getCard().getColour(), getCard().getColourString(), display, getCave()) : new VolcanoSquare(getCard().getColour(), getCard().getColourString(), display));
        return square;
    }

    /**
     * Build the Card based on the maximum number of Squares it can have and the displayCreationStrategies.
     * @return the built Card, populated with Squares and Animals.
     */
    public Card build() {
        int i = 0;
        // Build each Square for the Card.
        while (i < card.getNumOfSquares()) {
            // If there is a cave, set the Cut to true for the middle Square.
            setCut((i == card.getNumOfSquares() - 2) && (cave != null));
            // Build the squares.
            VolcanoSquare square = buildSquares();
            card.addSquare(square);
            i++;
        }

        return card;
    }

    /**
     * A method to return the Card data attribute.
     * @return the Card object the Builder is building.
     */
    public Card getCard() {
        return card;
    }

    /**
     * A method to return the isCut data attribute.
     * @return true if the Card is cut, false otherwise.
     */
    public boolean isCut() {
        return isCut;
    }

    /**
     * A method to return the cave data attribute.
     * @return the Cave instance variable attached to this Card.
     */
    public Cave getCave() {
        return cave;
    }

    /**
     * A method to return the displayCreationStrategies list data attribute.
     * @return the list of displayCreationStrategies.
     */
    public List<CreationStrategy> getDisplayCreationStrategies() {
        return displayCreationStrategies;
    }

    /**
     * A method to set the Card data attribute.
     * @param card: The Card object the Builder is building.
     */
    public void setCard(Card card) {
        this.card = card;
    }

    /**
     * A method to set the isCut data attribute.
     * @param cut: True if the Card is cut, false otherwise.
     */
    public void setCut(boolean cut) {
        isCut = cut;
    }

    /**
     * A method to set the cave data attribute.
     * @param cave: The Cave instance variable attached to this Card.
     */
    public void setCave(Cave cave) {
        this.cave = cave;
    }

    /**
     * A method to set the displayCreationStrategies list data attribute.
     * @param displayCreationStrategies: The list of displayCreationStrategies.
     */
    public void setCreationStrategy(List<CreationStrategy> displayCreationStrategies) {
        this.displayCreationStrategies = displayCreationStrategies;
    }
}
