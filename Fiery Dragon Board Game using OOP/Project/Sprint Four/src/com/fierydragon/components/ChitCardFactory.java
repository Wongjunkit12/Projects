package com.fierydragon.components;

import com.fierydragon.display.creations.CreationStrategy;
import com.fierydragon.utils.Constants;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;

/**
 * A Factory Design Pattern that creates the ChitC/Dragon Cards in the center of the Volcano.
 * Created by:
 * @author Bryan Wong
 * Modified by: Po Han Tay
 * @version 1.0
 * @see UIComponent
 * @see CreationStrategy
 * @see Constants
 * @see DisplayComponentFactory
 */

public class ChitCardFactory implements DisplayComponentFactory {
    /**
     * An instance of itself so that only one ChitCardFactory instance is created. Default set to null.
     */
    private static ChitCardFactory chitCardFactory = null;
    /**
     * An integer representing the maximum number of Chit Cards available.
     */
    private int maxNumChitCards;
    /**
     * An integer representing the maximum number of Pirate Chit Cards.
     */
    private int numOfPirates;
    /**
     * An integer representing the maximum number of Knight Chit Cards.
     */
    private int numOfKnights;

    /**
     * ChitCard Constructor.
     * Add all the possible ChitCards to the chitCardList, 3 Baby Dragons, 3 Bat, 3 Salamander, 3 Spider, 4 Dragon Pirate
     * and 1 Knight.
     * @param maxNumChitCards: The maximum number of Chit Cards available.
     * @param numOfPirates: The maximum number of Pirate Chit Cards available.
     * @param numOfKnights: The maximum number of Knight Chit Cards available.
     */
    public ChitCardFactory(int maxNumChitCards, int numOfPirates, int numOfKnights) {
        // Set the instance variables of the ChitCardFactory.
        setMaxNumChitCards(maxNumChitCards);
        setNumOfPirates(numOfPirates);
        setNumOfKnights(numOfKnights);
    }

    /**
     * The overridden createComponent method
     * @param creationStrategies: The list of CreationStrategies to be used to create the Chit Cards.
     * @return the list of ChitCard objects created.
     */
    @Override
    public List<UIComponent> createComponents(List<CreationStrategy> creationStrategies) {
        List<UIComponent> chitCards = new ArrayList<>();
        // Add all the Animal Chit Cards.
        for (CreationStrategy creationStrategy : creationStrategies) {
            // Will add in 1, 2, ... up to the maximum allowed of animals on a chit card.
            for (int j = 1; j <= Constants.MAX_ANIMAL_CHIT_CARDS; j++) {
                // Call the createDisplayComponent in creationStrategy to create the specific Displayable on the Chit Card.
                // Add it to the chitCards list to be returned.
                chitCards.add(new ChitCard(Color.GREY, "Grey", creationStrategy.createDisplayComponent(), j));
            }
        }
        // Return the populated Chit Card list.
        return chitCards;
    }

    /**
     * A method to return the maxNumChitCards data attribute.
     * @return the maximum number of Chit Cards available.
     */
    public int getMaxNumChitCards() {
        return maxNumChitCards;
    }

    /**
     * A method to return the numOfPirates data attribute.
     * @return the maximum number of Pirate Chit Cards available.
     */
    public int getNumOfPirates() {
        return numOfPirates;
    }

    /**
     * A method to return the numOfKnights data attribute.
     * @return the maximum number of Knight Chit Cards available.
     */
    public int getNumOfKnights() {
        return numOfKnights;
    }

    /**
     * A method to set the maxNumChitCards data attribute.
     * @param maxNumChitCards: The maximum number of Chit Cards available.
     */
    public void setMaxNumChitCards(int maxNumChitCards) {
        this.maxNumChitCards = maxNumChitCards;
    }

    /**
     * A method to set the numOfPirates data attribute.
     * @param numOfPirates: The maximum number of Pirate Chit Cards available.
     */
    public void setNumOfPirates(int numOfPirates) {
        this.numOfPirates = numOfPirates;
    }

    /**
     * A method to set the numOfKnights data attribute.
     * @param numOfKnights: The maximum number of Knight Chit Cards available.
     */
    public void setNumOfKnights(int numOfKnights) {
        this.numOfKnights = numOfKnights;
    }

    /**
     * Returns the instance of the chitCardFactory stored as a data attribute.
     * If one has not been created yet, create a new instance of chitCardFactory.
     * @param maxNumChitCards: The maximum number of Chit Cards available.
     * @param numOfPirates: The maximum number of Pirate Chit Cards available.
     * @param numOfKnights: The maximum number of Knight Chit Cards available.
     * @return the chitCardFactory instance.
     */
    public static ChitCardFactory getInstance(int maxNumChitCards, int numOfPirates, int numOfKnights) {
        // If an instance of chitCardFactory has not been created yet.
        if (chitCardFactory == null) {
            // Create a new instance of chitCardFactory.
            chitCardFactory = new ChitCardFactory(maxNumChitCards, numOfPirates, numOfKnights);
        }
        // Return the chitCardFactory instance, either newly created or stored currently as a data attribute.
        return chitCardFactory;
    }
}
