package com.fierydragon.volcano;

import javafx.stage.FileChooser;
import com.fierydragon.FieryDragonDriver;
import javafx.scene.control.Alert;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.*;
import java.net.URISyntaxException;

/**
 * A GameManager Singleton Design Pattern to manage the game state and
 * Created by:
 * @author Po Han Tay
 * Modified by:
 * @version 1.0
 */

public class GameManager {
    /**
     * An instance of itself so that only one GameManager instance is created. Default set to null.
     */
    private static GameManager gameManager = null;

    /**
     * The current game board scene of the application
     * */
    private FieryDragonDriver fieryDragonDriver;

   /**
     * The method to save the entire game of Fiery Dragon.
     * @param gameBoard: The GameBoard instance to save.
     * @param saveFileName: The name of the save file.
     * */
    public void saveGame(GameBoard gameBoard, String saveFileName) {
        try {
            // Get the path of the JAR file
            String jarPath = new File(GameManager.class.getProtectionDomain().getCodeSource().getLocation().toURI()).getParent();

            // Create the save folder path relative to the JAR file
            File saveFolder = new File(jarPath, "save");

            // Create the save folder if it does not exist
            if (!saveFolder.exists()) {
                saveFolder.mkdir();
            }
            // Get the count of existing files in the "save" folder
            int fileCount = saveFolder.list().length;

            // Generate the save file name with an incrementing counter
            String fileName = String.format("%s%d", saveFileName, fileCount + 1);

            // Construct the full file path
            String filePath = new File(saveFolder, fileName).getAbsolutePath();

            // Save the game
            fieryDragonDriver.saveGame(gameBoard, filePath);
        } catch (IOException | URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * The method to load the entire game of Fiery Dragon.
     * @param gameBoard: The GameBoard instance to load.
     * */
    public void loadGame(GameBoard gameBoard) {
        try {
            // Determine the directory of the running JAR file
            File jarFile = new File(GameManager.class.getProtectionDomain().getCodeSource().getLocation().toURI());
            File jarDir = jarFile.getParentFile();

            // Set the initial directory to the "save" folder within the JAR directory
            File saveFolder = new File(jarDir, "save");

            // Create a FileChooser for selecting files
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Select a file to load");

            // Set the initial directory of the FileChooser
            fileChooser.setInitialDirectory(saveFolder);

            // Show the file dialog and get the selected file
            File selectedFile = fileChooser.showOpenDialog(new Stage());

            if (selectedFile != null) {
                // Load the game using the selected file
                String filePath = selectedFile.getAbsolutePath();
                fieryDragonDriver.loadGame(gameBoard, filePath);
            } else {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("No Files Selected");
                alert.setContentText("Please add only one file in the 'save' folder.");
                alert.showAndWait();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Set the driver to update the game board's state
     * @param fieryDragonDriver: The driver to be updated
     * */
    public void setDriver(FieryDragonDriver fieryDragonDriver) {
        this.fieryDragonDriver = fieryDragonDriver;
    }

    /**
     * Get the Driver object of the Fiery Dragon game
     * @return the Driver object of the Fiery Dragon game
     * */
    public FieryDragonDriver getFieryDragonDriver() {
        return fieryDragonDriver;
    }

    /**
     * Get the main grid of game board
     * @return The Grid Pane of the main game board
     * */
    public GridPane getBoardGrid() {
        return fieryDragonDriver.getBoardGrid();
    }

    /**
     * Gets the chit cards grid of game board
     * @return The Grid Pane of the chit cards area
     * */
    public GridPane getChitCardGrid() {
        return fieryDragonDriver.getChitCardGrid();
    }

    /**
     * Set the main grid of game board
     * @param newPane: The Grid Pane of the main game board
     * */
    public void setBoardGrid(GridPane newPane) {
        fieryDragonDriver.setBoardGrid(newPane);
    }

    /**
     * Returns the instance of the gameManager stored as a data attribute.
     * If one has not been created yet, create a new instance of gameManager.
     * @return the gameManager instance.
     */
    public static GameManager getInstance() {
        if (gameManager == null) {
            gameManager = new GameManager();
        }
        return gameManager;
    }

    /**
     * Reset the GameManager instance.
     */
    public void reset() {
        gameManager = new GameManager();
    }
}
