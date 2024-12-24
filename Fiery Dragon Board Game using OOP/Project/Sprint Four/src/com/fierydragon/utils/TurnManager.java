package com.fierydragon.utils;


import com.fierydragon.pieces.Dragon;
import com.fierydragon.pieces.DragonIterator;
import com.fierydragon.volcano.GameBoard;
import com.fierydragon.volcano.GameManager;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.animation.PauseTransition;
import javafx.geometry.Pos;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.Optional;

/**
 * A TurnManager Singleton Design Pattern to manage whose turn is it.
 * Created by:
 * @author Bryan Wong
 * Modified by: Vincent Tanuwidjaja, Po Han Tay
 * @version 1.0
 * @see Dragon
 * @see DragonIterator
 */

public class TurnManager {
    /**
     * An instance of itself so that only one TurnManager instance is created. Default set to null.
     */
    private static TurnManager turnManager = null;
    /**
     * The boardGrid object to display turn indicator
     */
    private GridPane boardGrid;
    /**
     * An instance of GameBoard so that TurnManager can reset the ChitCards.
     */
    private GameBoard gameBoard;
    /**
     * The Iterator class to iterate over each Dragon/Player.
     */
    private DragonIterator dragonIterator;
    /**
     * A dummy boolean as placeholder representing whether a win condition has been met.
     */
    private boolean winCondition;
    /**
     * A boolean object denoting if the game is paused (when changing turns)
     */
    private boolean isPaused;

    /**
     * TurnManager Constructor.
     * Creates a DragonIterator instance based on the input list of Dragons.
     */
    public TurnManager() {
        // Create a new DragonIterator instance based on the input List of dragons.
       setDragonIterator(new DragonIterator());
       setWinCondition(false);  // Set false to default as its not implemented.
    }

    /**
     * Update the Turn Indicator at the top of the screen.
     * @param grid: The GridPane object.
     */
    public void updateTurnIndicator(GridPane grid) {
        // Find the existing turn indicator by its ID or another distinguishing property
        Text turnIndicator = (Text) grid.lookup("#turnIndicator");

        // If none exist, create it and add it to the Grid.
        if (turnIndicator == null) {
            // Initialize the turn indicator Text node
            turnIndicator = new Text();
            turnIndicator.setId("turnIndicator");       // Set an ID for easy lookup
            turnIndicator.setFont(Font.font("Arial", FontWeight.BOLD, 20));
            turnIndicator.setTextAlignment(TextAlignment.CENTER);

            // Get the current dragon's color.
            String currentDragonColor = TurnManager.getInstance().getCurrentDragon().getColourString();
            turnIndicator.setText(String.format("%s DRAGON'S TURN", currentDragonColor.toUpperCase()));
            turnIndicator.setFill(Color.BLACK);

            // Create a StackPane to overlay the Text
            StackPane overlay = new StackPane();
            overlay.getChildren().add(turnIndicator);

            // Set alignment for the Text within the StackPane
            StackPane.setAlignment(turnIndicator, Pos.CENTER);

            // Add the StackPane with the Text to the GridPane
            grid.add(overlay, 0, 0, grid.getColumnCount(), 1);
        }
        else {
            // Get the current dragon's properties.
            Dragon currentDragon = TurnManager.getInstance().getCurrentDragon();
            String currentDragonColor = currentDragon.getColourString();
            boolean currentDragonStunned = currentDragon.isStunned();

            // Update the turn indicator's text based on if it is stunned from being attacked.
            String stunnedText = currentDragonStunned ? " (STUNNED)" : "";
            turnIndicator.setText(String.format("%s DRAGON'S TURN%s", currentDragonColor, stunnedText));
        }
    }

    /**
     * Get the current Dragon whose turn it is.
     * @return the Dragon whose turn it is.
     */
    public Dragon getCurrentDragon() {
        return dragonIterator.getCurrent();
    }

    /**
     * Moves the turn to the next Player/Dragon and get their Dragon instance.
     * @return the Dragon who is the next.
     */
    public void nextTurn() {
        // Create a pause to give player time to see what Displayable
        // is on the ChitCard before changing turns
        PauseTransition pause = new PauseTransition(Duration.seconds(1));
        setPaused(true);
        pause.setOnFinished(event -> {
            // After the pause, reset chit cards
            gameBoard.resetChitCards();
            Dragon nextDragon = dragonIterator.next();
            updateTurnIndicator(this.boardGrid);
            setPaused(false);
            // If a turn has pass, the Dragon recovers.
            if (nextDragon.isStunned()) {
                nextDragon.setStunned(false);
                nextTurn();
            }
        });
        // Start the pause transition
        pause.play();
    }

    /**
     * Displays a winning alert message to the users playing. Allows the users to restart or close the application
     * @param dragon: The Dragon that won the game.
     */
    public void win(Dragon dragon) {
        // Create alert message to display that win condition has been met
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Game Over");
        alert.setHeaderText(String.format("%s Dragon wins!", dragon.getHomeCave().getColourString()));

        // Create buttons for restart and close the application
        ButtonType restartButton = new ButtonType("Restart");
        ButtonType closeButton = new ButtonType("Exit", ButtonBar.ButtonData.CANCEL_CLOSE);
        alert.getButtonTypes().setAll(restartButton, closeButton);

        // Display the alert message
        Optional<ButtonType> result = alert.showAndWait();

        // Checks if the user clicks the Restart or Exit button
        if (result.isPresent() && result.get() == restartButton) {
            Stage stage = GameManager.getInstance().getFieryDragonDriver().getPrimaryStage();
            GameManager.getInstance().getFieryDragonDriver().cleanup();
            GameManager.getInstance().getFieryDragonDriver().restart(stage);
        } else {
            Platform.exit();
        }
    }

    /**
     * A method to return the winCondition list data attribute.
     * @return the placeholder representing whether a win condition has been met.
     */
    public boolean isWinCondition() {
        return winCondition;
    }

    /**
     * A method to return the dragonSquareIterator data attribute.
     * @return the DragonIterator instance.
     */
    public DragonIterator getDragonIterator() {
        return dragonIterator;
    }

    /**
     * A method to set the dragonSquareIterator data attribute.
     * @param dragonIterator: The DragonIterator instance.
     */
    public void setDragonIterator(DragonIterator dragonIterator) {
        this.dragonIterator = dragonIterator;
    }

    /**
     * A method to set the winCondition list data attribute.
     * @param winCondition: The placeholder representing whether a win condition has been met.
     */
    public void setWinCondition(boolean winCondition) {
        this.winCondition = winCondition;
    }

    /**
     * A method to set the gameBoard data attribute.
     * @param gameBoard: The gameBoard object to be set.
     */
    public void setGameBoard(GameBoard gameBoard) {
        this.gameBoard = gameBoard;
    }

    /**
     * A method to get the isPaused data attribute.
     */
    public boolean isPaused() {
        return isPaused;
    }

    /**
     * A method to set the boardGrid data attribute.
     * @param boardGrid: The boardGrid object to be set.
     */
    public void addBoardGrid(GridPane boardGrid) {
        this.boardGrid = boardGrid;
    }

    /**
     * A method to set the isPaused data attribute.
     * @param paused: The paused boolean object to be set.
     */
    public void setPaused(boolean paused) {
        isPaused = paused;
    }

    /**
     * Returns the instance of the turnManager stored as a data attribute.
     * If one has not been created yet, create a new instance of turnManager.
     * @return the turnManager instance.
     */
    public static TurnManager getInstance() {
        if (turnManager == null) {
            turnManager = new TurnManager();
        }
        return turnManager;
    }

    /**
     * Reset the TurnManager instance.
     */
    public void reset() {
        turnManager = new TurnManager();
    }
}
