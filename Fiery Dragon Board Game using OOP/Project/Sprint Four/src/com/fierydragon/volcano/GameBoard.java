package com.fierydragon.volcano;

import com.fierydragon.components.Cave;
import com.fierydragon.components.ChitCard;
import com.fierydragon.components.ChitCardFactory;
import com.fierydragon.components.UIComponent;
import com.fierydragon.display.animals.Knight;
import com.fierydragon.display.animals.PirateDragon;
import com.fierydragon.display.creations.CreationStrategy;
import com.fierydragon.movement.DragonSquareIterator;
import com.fierydragon.movement.MovementManager;
import com.fierydragon.pieces.Dragon;
import com.fierydragon.pieces.DragonFactory;
import com.fierydragon.utils.Constants;
import com.fierydragon.utils.TurnManager;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A class representing the Fiery Dragon Game Board. Created using Composite Design Pattern.
 * Created by:
 * @author Bryan Wong
 * Modified by: Po Han Tay, Vincent Tanuwidjaja
 * @version 1.0
 * @see Cave
 * @see ChitCard
 * @see ChitCardFactory
 * @see Dragon
 * @see DragonFactory
 * @see UIComponent
 * @see PirateDragon
 * @see CreationStrategy
 * @see Constants
 */

public class GameBoard extends UIComponent {
    /**
     * The maximum number of Squares the Card can have.
     */
    private int maxNumberOfSquares;
    /**
     * The maximum number of Cards the Game Board can have.
     */
    private int maxNumberOfCards;
    /**
     * The list of Cards.
     */
    private List<UIComponent> cards;
    /**
     * The list of ChitCards.
     */
    private List<UIComponent> chitCards;
    /**
     * The list of Caves.
     */
    private List<Cave> caves;
    /**
     * The list of Dragons.
     */
    private List<Dragon> dragons;

    /**
     * GameBoard Constructor.
     * Calls parent constructor to set parent attributes.
     * @param x: The x-coordinate of the GameBoard.
     * @param y: The y-coordinate of the GameBoard.
     * @param colour: The Color of the GameBoard.
     * @param colourString: The String representation of the colour of the GameBoard.
     * @param maxNumberOfSquares: The maximum number of Squares the Card can have.
     * @param maxNumberOfCards: The maximum number of Cards the Game Board can have.
     */
    public GameBoard(int x, int y, Color colour, String colourString, int maxNumberOfSquares, int maxNumberOfCards) {
        // Set the parent attributes by calling the parent constructor, set the maximum number of squares and cards.
        super(x, y, colour, colourString, "Game Board");
        setMaxNumberOfSquares(maxNumberOfSquares);
        setMaxNumberOfCards(maxNumberOfCards);
        // Initialise the Cards and ChitCard list.
        setCards(new ArrayList<>());
        setChitCards(new ArrayList<>());
        setCaves(new ArrayList<>());

        TurnManager.getInstance().setGameBoard(this);
    }

    /**
     * Setups the Volcano Path by placing all the Volcano Cards around the Volcano. For now, it will be a square for simplicity’s sake.
     * @param cutCardDisplayCreationStrategies: A nested list of all display creation strategies for the cut Volcano Card Game Board setup.
     * @param cardDisplayCreationStrategies: A nested list of all display creation strategies for the uncut Volcano Card Game Board setup.
     * @param caves: A list of all caves to create.
     */
    public void cardSetup(List<List<CreationStrategy>> cutCardDisplayCreationStrategies, List<List<CreationStrategy>> cardDisplayCreationStrategies, List<Cave> caves) {
        // Loop through the nested Animal Creation list and build each card, adding it ot the Game Board.
        int i = 0, j = 0, k = 0;
        for (CardPlacement cardPlacement : CardPlacement.values()) {
            CardBuilder cardBuilder;
            // Alternates the addition of the cut and uncut cards. Add uncut Cards every even number.
            if (i % 2 == 0) {
                cardBuilder = new CardBuilder(Color.BEIGE, "Beige", Constants.NUM_SQUARES, cardDisplayCreationStrategies.get(j), cardPlacement);
                j++;
            }
            // Add cut Cards every odd number.
            else {
                cardBuilder = new CardBuilder(Color.BEIGE, "Beige", Constants.NUM_SQUARES, cutCardDisplayCreationStrategies.get(k), cardPlacement, caves.get(k));
                k++;
            }
            // Build the Card using the CardBuilder and add it into the Game Board.
            Card card = cardBuilder.build();
            // Set the starting coordinates for the GameBoard.
            card.setCoordinates(Constants.startingXCoords[i], Constants.startingYCoords[i]);
            // Determine the placement of Squares.
            card.placeSquares(card.getCardPlacement());
            // Add the card to have the Squares become a moveable location.
            MovementManager.getInstance().getDragonSquareIterator().addSquare(card);
            // Add it to the card array list.
            this.addCard(card);
            i++;
        }
    }

    /**
     * Setups all the Chit/Dragon Cards by using the displayCreationStrategies list and the ChitCardFactory singleton.
     * @param displayCreationStrategies: A list of all the display Animal Creation Strategies to create and display.
     * @param maxNumChitCards: The maximum number of Chit Cards available.
     * @param numOfPirates: The maximum number of Pirate Chit Cards available.
     * @param numOfKnights: The maximum number of Knight Chit Cards available.
     */
    public void chitCardSetup(List<CreationStrategy> displayCreationStrategies, int maxNumChitCards, int numOfPirates, int numOfKnights) {
        // Use the ChitCardFactory to construct all the Animal Chit Cards.
        ChitCardFactory chitCardFactory = ChitCardFactory.getInstance(maxNumChitCards, numOfPirates, numOfKnights);
        chitCards.addAll(chitCardFactory.createComponents(displayCreationStrategies));

        // Add all the Dragon Pirate Chit Cards.
        int j = 1;
        for (int i = 0; i < numOfPirates; i++) {
            // Add 2 1 Pirate Dragon Chit Card and 2 2 Pirates Dragon Chit Card.
            if (i == numOfPirates / Constants.MAX_PIRATE) {
                j++;
            }
            chitCards.add(new ChitCard(Color.GREY, "Grey", new PirateDragon(), j));
        }

        // Add all the Knight Chit Cards.
        int k = 1;
        for (int i = 0; i < numOfKnights; i++) {
            chitCards.add(new ChitCard(Color.GREY, "Grey", new Knight(), k));
        }

        // Shuffle the chitCards multiple time to make it random.
        for (int i = 0; i < 5; i++) {
            Collections.shuffle(chitCards);
        }

        // Set the coordinates of each Chit Card to display in a 4x4 Grid.
        int i = 0, index = 0;
        // Loop through each Chit Card
        while (index < chitCards.size()) {
            UIComponent chitCard = chitCards.get(index);  // Get the Chit Card.
            j = index % Constants.COLUMNS;  // Calculate the column
            chitCard.setCoordinates(j, i);  // Set its coordinates
            index++;
            if (j == Constants.COLUMNS - 1) {
                i++;  // Move to the next row after every pre-defined column value
            }
        }
    }

    /**
     * Setup the all the Dragons.
     * @param numOfDragons: The number of Dragons in the Game.
     * @param caves: A list of all caves.
     */
    public void dragonSetup(int numOfDragons, List<Cave> caves) {
        // Create the Dragons based on the number of Players in the Game and set the list for that.
        this.setDragons(DragonFactory.getInstance(numOfDragons).createDragons(caves));
    }

    /**
     * Overridden save method to save the GameBoard itself.
     * @return the string savedData of the GameBoard.
     */
    @Override
    public String save() {
        StringBuilder builder = new StringBuilder();
        builder.append("numberOfCards: ");
        builder.append(this.getMaxNumberOfCards());
        builder.append("\n");

        // Save the Cards' data.
        builder.append("volcanoCards: ");
        // Loop through each Card and append its saved data
        for (int i = 0; i < cards.size(); i++) {
            builder.append(cards.get(i).save());
            // If not on the last Card, append a comma to it.
            if (i < cards.size() - 1) {
                builder.append(", ");
            }
        }
        builder.append("\n");

        // Save the Chit Cards' data.
        builder.append("chitCards: ");
        // Loop through each ChitCards and append its saved data
        for (int i = 0; i < chitCards.size(); i++) {
            // Append a '[' to signify one Chit.
            builder.append("[");
            builder.append(chitCards.get(i).save());
            // Append a '[' to signify one Chit.
            builder.append("]");
            // If not on the last Card, append a comma to it.
            if (i < chitCards.size() - 1) {
                builder.append(", ");
            }
        }
        builder.append("\n");

        // Save the Caves' data.
        builder.append("caves: ");
        // Loop through each Cave and append its saved data
        for (int i = 0; i < caves.size(); i++) {
            // Append a '[' to signify one Cave.
            builder.append("[");
            builder.append(caves.get(i).save());
            // Append a '[' to signify one Cave.
            builder.append("]");
            // If not on the last Cave, append a comma to it.
            if (i < caves.size() - 1) {
                builder.append(", ");
            }
        }
        builder.append("\n");

        // Save the Dragons' data.
        builder.append("dragons: ");
        // Loop through each Dragon and append its saved data
        for (int i = 0; i < dragons.size(); i++) {
            // Append a '[' to signify one Dragon.
            builder.append("[");
            builder.append(dragons.get(i).save());
            // Append a '[' to signify one Dragon.
            builder.append("]");
            // If not on the last Dragon, append a comma to it.
            if (i < dragons.size() - 1) {
                builder.append(", ");
            }
        }
        builder.append("\n");

        // Save the turn order.
        builder.append("currentTurn: ");
        builder.append(TurnManager.getInstance().getDragonIterator().getCurrentPosition());

        // Return the constructed saved String.
        return builder.toString();
    }

    /**
     * Overridden load method to load the GameBoard itself.
     * @param savedData: The saved data to load.
     */
    @Override
    public void load(String savedData) {
        // Clear the DragonSquareIterator lists.
        MovementManager.getInstance().getDragonSquareIterator().clearAll();

        // Get the saved data, split each new line into an element in a list.
        String[] lines = savedData.split("\n");

        // Loop through each line of the read save file.
        for (String line : lines) {
            // Split the line into its corresponding key-value pair.
            String[] keyValue = line.split(":\\s*", 2);
            String key = keyValue[0].trim();
            String value = keyValue[1].trim();

            // Load the corresponding data into their respective attributes.
            switch (key) {
                case "numberOfCards":
                    setMaxNumberOfCards(Integer.parseInt(value));
                    break;
                case "volcanoCards":
                    // Split the cards by a closing square bracket followed by a comma and an optional space
                    String[] cardStrings = value.split("],\\s*");

                    // Check if the number of Cards are matching. If no, return an alert
                    if (this.getMaxNumberOfCards() != cardStrings.length) {
                        Alert alert = new Alert(Alert.AlertType.WARNING);
                        alert.setTitle("Incorrect Card Settings");
                        alert.setContentText("The number of Volcano Cards doesn't match. Please update it.");
                        alert.showAndWait();
                        return;
                    }

                    // Get the difference to determine whether Cards were added (positive) or removed (negative)
                    int difference = this.getMaxNumberOfCards() - cards.size();

                    // If there were Card that were removed
                    if (difference < 0) {
                        // Create a list to store all the Cards to be removed.
                        List<UIComponent> cardRemove = new ArrayList<>(cards);

                        // Loop through each square String.
                        for (String cardString : cardStrings) {
                            // Find the index of "cardX" and "cardY"
                            int xIndex = cardString.indexOf("cardX");
                            int yIndex = cardString.indexOf("cardY");

                            // Extract the substrings after "cardX" and "cardY"
                            String xSubstring = cardString.substring(xIndex + "cardX".length() + 2);
                            String ySubstring = cardString.substring(yIndex + "cardY".length() + 2);

                            // Convert the substrings to integers
                            int cardX = Integer.parseInt(xSubstring.split(",")[0].trim());
                            int cardY = Integer.parseInt(ySubstring.split(",")[0].trim());

                            cardRemove.removeIf(card -> cardX == card.getX() && cardY == card.getY());
                        }

                        // Removes the Cards from the Game.
                        for (UIComponent card : cardRemove) {
                            Card volCard = (Card) card;
                            // Remove the VolcanoSquares from the Card list.
                            cards.remove(volCard);
                        }

                    // If there were Cards that were added
                    } else if (difference > 0) {
                        // Add in the remaining number of Cards
                        for (int i = 0; i < difference; i++) {
                            cards.add(new Card(Color.BEIGE, "Beige", 0, null));
                        }
                    }

                    // Loop through each card String.
                    for (int i = 0; i < cardStrings.length; i++) {
                        // Call the respective card loading method to load.
                        cards.get(i).load(cardStrings[i]);
                    }
                    break;
                case "chitCards":
                    // Split the cards by a closing square bracket followed by a comma and an optional space
                    String[] chitCardStrings = value.split("],\\s*");
                    // Loop through each chitCard String.
                    for (int i = 0; i < chitCardStrings.length; i++) {
                        // Call the respective chitCard loading method to load.
                        chitCards.get(i).load(chitCardStrings[i]);
                    }
                    break;
                case "caves":
                    // Split the caves by a closing square bracket followed by a comma and an optional space
                    String[] caveStrings = value.split("],\\s*");
                    // Loop through each cave String.
                    for (int i = 0; i < caveStrings.length; i++) {
                        // Call the respective cave loading method to load.
                        caves.get(i).load(caveStrings[i]);
                    }
                    break;
                case "dragons":
                    // Split the dragons by a closing square bracket followed by a comma and an optional space
                    String[] dragonStrings = value.split("],\\s*");
                    // Loop through each dragon String.
                    for (int i = 0; i < dragonStrings.length; i++) {
                        // Call the respective dragon loading method to load.
                        dragons.get(i).load(dragonStrings[i]);
                    }
                    break;
                case "currentTurn":
                    TurnManager.getInstance().getDragonIterator().setCurrentPosition(Integer.parseInt(value));
                    break;
            }
        }

        // Repopulate the DragonSquareIterator squares list.
        for (UIComponent card : cards) {
            MovementManager.getInstance().getDragonSquareIterator().addSquare((Card) card);
        }
        // Repopulate the DragonSquareIterator dragonToSquare and vice versa map.
        for (int i = 0; i < this.getDragons().size(); i++) {
            DragonSquareIterator dragonSquareIterator = MovementManager.getInstance().getDragonSquareIterator();
            Dragon dragon = this.getDragons().get(i);

            // Add the Dragon and their respective home Cave into the Dragon to Square Iterator.
            dragonSquareIterator.add(dragon, dragonSquareIterator.getSquareByCoords(dragon.getX(), dragon.getY()));
        }
    }

    /**
     * The overridden display method that will display the Game Board in the UI.
     * @param boardGrid: The GameBoard GridPane object
     * @param chitCardGrid: The ChitCard GridPane object.
     */
    @Override
    public void render(GridPane boardGrid, GridPane chitCardGrid) {
        // Loop through each Card and display them.
        for (UIComponent card : cards) {
            card.render(boardGrid, chitCardGrid);
        }
        // Loop through each Dragon and display them.
        for (Dragon dragon : dragons) {
            dragon.render(boardGrid, chitCardGrid);
        }

        // Update the Turn Indicator.
        TurnManager.getInstance().updateTurnIndicator(boardGrid);
    }

    /**
     * Renders the ChitCards in the UI.
     * @param boardGrid: The GameBoard GridPane object
     * @param chitCardGrid: The ChitCard GridPane object.
     */
    public void renderChitCard(GridPane boardGrid, GridPane chitCardGrid) {
        // Loop through each ChitCard and renders them.
        for (UIComponent chitCard : getChitCards()) {
            chitCard.render(boardGrid, chitCardGrid);
        }
    }

    /**
     * Renders the Buttons in the UI.
     * @param boardGrid: The GameBoard GridPane object
     */
    public void renderButtons(GridPane boardGrid) {
        // Create a button for saving the game
        Button saveButton = new Button("Save Game");
        // Set the mouse click event to trigger the saveGame method in GameManager.
        saveButton.setOnMouseClicked(e -> {
            // Call the saveGame method
            GameManager.getInstance().saveGame(this, "save");
        });
        saveButton.setPrefHeight(Constants.SQUARE_HEIGHT);  // Set preferred height

        // Create a button for loading the game
        Button loadButton = new Button("Loading Game");
        // Set the mouse click event to trigger the loadGame method in GameManager.
        loadButton.setOnMouseClicked(e -> {
            // Call the loadGame method
            GameManager.getInstance().loadGame(this);
        });
        loadButton.setPrefHeight(Constants.SQUARE_HEIGHT); // Set preferred height

        // Add the button to the bottom left corner of the boardGrid
        boardGrid.add(saveButton, 0, boardGrid.getRowCount());

        // Add the button to the bottom right corner of the boardGrid
        boardGrid.add(loadButton, boardGrid.getColumnCount(), boardGrid.getRowCount() - 1);
    }

    /**
     * A method to return the maxNumberOfSquares data attribute.
     * @return the maximum number of Squares the Card can have.
     */
    public int getMaxNumberOfSquares() {
        return maxNumberOfSquares;
    }

    /**
     * A method to return the maxNumberOfCards data attribute.
     * @return the maximum number of Cards the Game Board can have.
     */
    public int getMaxNumberOfCards() {
        return maxNumberOfCards;
    }

    /**
     * A method to return the Card list data attribute.
     * @return the list of Cards that Game Board is composed of.
     */
    public List<UIComponent> getCards() {
        return cards;
    }

    /**
     * A method to return the ChitCard list data attribute.
     * @return the list of ChitCards the Game Board has.
     */
    public List<UIComponent> getChitCards() {
        return chitCards;
    }

    /**
     * A method to return the Caves list data attribute.
     * @return the list of Caves the Game Board has.
     */
    public List<Cave> getCaves() {
        return caves;
    }

    /**
     * A method to return the Dragons list data attribute.
     * @return the list of Dragons/Players the Game has.
     */
    public List<Dragon> getDragons() {
        return dragons;
    }

    /**
     * A method to set the maxNumberOfSquares data attribute.
     * @param maxNumberOfSquares: The maximum number of Squares the Card can have.
     */
    public void setMaxNumberOfSquares(int maxNumberOfSquares) {
        this.maxNumberOfSquares = maxNumberOfSquares;
    }

    /**
     * A method to set the setMaxNumberOfCards data attribute.
     * @param maxNumberOfCards: The maximum number of Cards the Game Board can have.
     */
    public void setMaxNumberOfCards(int maxNumberOfCards) {
        this.maxNumberOfCards = maxNumberOfCards;
    }

    /**
     * A method to set the Card list data attribute.
     * @param cards: The list of Cards that Game Board is composed of.
     */
    public void setCards(List<UIComponent> cards) {
        this.cards = cards;
    }

    /**
     * A method to set the ChitCard list data attribute.
     * @param chitCards: The list of Chit Cards that Game Board is composed of.
     */
    public void setChitCards(List<UIComponent> chitCards) {
        this.chitCards = chitCards;
    }

    /**
     * A method to set the Caves list data attribute.
     * @param caves: The list of Caves that Game Board is composed of.
     */
    public void setCaves(List<Cave> caves) {
        this.caves = caves;
    }

    /**
     * A method to return the Dragons list data attribute.
     * @param dragons: The list of Dragons/Players the Game has.
     */
    public void setDragons(List<Dragon> dragons) {
        this.dragons = dragons;
    }

    /**
     * A method to add a Card to the cards list data attribute.
     * @param card: The Card to add to the cards list.
     */
    public void addCard(UIComponent card) {
        getCards().add(card);
    }

    /**
     * A method to add a ChitCard to the cards list data attribute.
     * @param chitCard: The ChitCard to add to the cards list.
     */
    public void addChitCard(UIComponent chitCard) {
        getChitCards().add(chitCard);
    }

    /**
     * A method to add a ChitCard to the cards list data attribute.
     * @param dragon: The Dragon to add to the cards list.
     */
    public void addDragon(Dragon dragon) {
        getDragons().add(dragon);
    }

    public void resetChitCards() {
        for (UIComponent chitCard : chitCards) {
            ((ChitCard) chitCard).reset();
        }
    }
}
