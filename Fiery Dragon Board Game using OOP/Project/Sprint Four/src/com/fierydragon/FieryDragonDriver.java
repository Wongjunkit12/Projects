package com.fierydragon;

import com.fierydragon.components.Cave;
import com.fierydragon.display.creations.*;
import com.fierydragon.movement.MovementManager;
import com.fierydragon.utils.Constants;
import com.fierydragon.utils.TurnManager;
import com.fierydragon.volcano.GameBoard;
import com.fierydragon.volcano.GameManager;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.*;
import java.util.*;

public class FieryDragonDriver extends Application {
    /**
     * Minimum number of players/dragons in the game
     * */
    private int MIN_PLAYERS = 2;

    /**
     * Maximum number of players/dragons in the game
     * */
    private int MAX_PLAYERS = 4;

    /**
     * If no number of players assigned, default number of players/dragons in the game
     * */
    private int DEFAULT_NUM_OF_PLAYERS = 4;

    /**
     * Stage for the scene
     * */
    private Stage stage;

    /**
     * Main Grid where all the Board Components will be drawn
     * */
    private GridPane boardGrid;

    /**
     * Grid where the Chit Cards will be drawn before adding it to the Main Grid
     * */
    private GridPane chitCardGrid;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        stage = primaryStage;
        // Create the two Grids required.
        boardGrid = new GridPane();
        chitCardGrid = new GridPane();

        // Create a new Game Board instance
        GameBoard gameBoard = new GameBoard(0, 0, Color.WHITE, "White", Constants.NUM_SQUARES, Constants.NUM_CARDS);

        // Define the animal creation strategies for each Animal.
        BabyDragonCreationStrategy babyDragonCreationStrategy = new BabyDragonCreationStrategy();
        BatCreationStrategy batCreationStrategy = new BatCreationStrategy();
        SalamanderCreationStrategy salamanderCreationStrategy = new SalamanderCreationStrategy();
        SpiderCreationStrategy spiderCreationStrategy = new SpiderCreationStrategy();

        // List of all the Display Animal Creation Strategies.
        List<CreationStrategy> allDisplayCreationStrategies = List.of(babyDragonCreationStrategy, batCreationStrategy, salamanderCreationStrategy, spiderCreationStrategy);
        // Create the list of all Animal Creation Strategies for all 4 cut Volcano Cards.
        List<List<CreationStrategy>> cutCardDisplayCreationStrategies = new java.util.ArrayList<>(List.of(
            List.of(babyDragonCreationStrategy, batCreationStrategy, spiderCreationStrategy),
            List.of(salamanderCreationStrategy, spiderCreationStrategy, batCreationStrategy),
            List.of(spiderCreationStrategy, salamanderCreationStrategy, babyDragonCreationStrategy),
            List.of(batCreationStrategy, spiderCreationStrategy, babyDragonCreationStrategy)
        ));

        // Create the list of all Animal Creation Strategies for all 4 uncut Volcano Cards.
        List<List<CreationStrategy>> cardDisplayCreationStrategies = new ArrayList<>(List.of(
            List.of(spiderCreationStrategy, batCreationStrategy, salamanderCreationStrategy),
            List.of(babyDragonCreationStrategy, salamanderCreationStrategy, batCreationStrategy),
            List.of(batCreationStrategy, babyDragonCreationStrategy, salamanderCreationStrategy),
            List.of(salamanderCreationStrategy, babyDragonCreationStrategy, spiderCreationStrategy)
        ));

        // Create a list of all the Caves with their colour.
        List<Cave> caves = new ArrayList<>();
        caves.add(new Cave(Color.LIGHTCORAL, "Red", salamanderCreationStrategy.createDisplayComponent()));
        caves.add(new Cave(Color.LIGHTGREEN, "Green", babyDragonCreationStrategy.createDisplayComponent()));
        caves.add(new Cave(Color.LIGHTBLUE, "Blue", batCreationStrategy.createDisplayComponent()));
        caves.add(new Cave(Color.KHAKI, "Yellow", spiderCreationStrategy.createDisplayComponent()));

        // Shuffle the order of the Card Creation Strategies multiple times so no game is the same.
        Random random =  new Random();
        for (int i = 0; i < 5; i++) {
            Collections.shuffle(cutCardDisplayCreationStrategies, random);
        }
        for (int i = 0; i < 5; i++) {
            Collections.shuffle(cardDisplayCreationStrategies, random);
        }
        // Shuffle the order of the caves too.
        for (int i = 0; i < 5; i++) {
            Collections.shuffle(caves, random);
        }

        // Call the cardSetup method to setup the Volcano Cards in the Game Board.
        gameBoard.cardSetup(cutCardDisplayCreationStrategies, cardDisplayCreationStrategies, caves);
        // Call the chitCardSetup method to setup the Chit Cards in the Game Board.
        gameBoard.chitCardSetup(allDisplayCreationStrategies, Constants.NUM_CHIT_CARDS, Constants.NUM_PIRATES, Constants.NUM_KNIGHTS);
        // Setup the Dragon and their respective Cave, setting the turn order in a clockwise direction starting from the top Dragon.
        // Can set 2 or 4 number of Dragons for 2 or 4 numbers of Players.
        int numOfDragons = readNumOfPlayers();
        gameBoard.dragonSetup(numOfDragons, caves);
        // Add the caves into the Game Board
        gameBoard.setCaves(caves);

        // Render the UI.
        gameBoard.render(boardGrid, chitCardGrid);
        gameBoard.renderChitCard(boardGrid, chitCardGrid);
        gameBoard.renderButtons(boardGrid);

        // Add boardGrid to TurnManager to enable pausing.
        TurnManager.getInstance().addBoardGrid(boardGrid);

        // Add the chitCardGrid to the boardGrid
        boardGrid.add(chitCardGrid, 4, 4, 5, 5);

        // Create the scene and set the scene.
        Scene scene = new Scene(boardGrid);
        GameManager.getInstance().setDriver(this);
        stage.setScene(scene);
        stage.setTitle("Fiery Dragon");
        stage.show();
    }

    /**
     * The method to save the entire game of Fiery Dragon.
     * Will call the GameBoard save() method.
     * @param gameBoard: The GameBoard instance to save.
     * @param filePath: The path to the save file.
     * */
    public void saveGame(GameBoard gameBoard, String filePath) throws IOException {
        // Save the whole GameBoard.
        String savedData = gameBoard.save();

        // Create the file and its parent directories if they don't exist
        File file = new File(filePath);
        file.getParentFile().mkdirs(); // Create parent directories if needed
        // Write the saved game state to the file.
        BufferedWriter writer = new BufferedWriter(new FileWriter(filePath));
        writer.write(savedData);
        writer.close();
    }

    /**
     * The method to load the entire game of Fiery Dragon.
     * Will call the GameBoard load() method.
     * @param gameBoard: The GameBoard instance to load.
     * @param filePath: The path to the save file.
     * */
    public void loadGame(GameBoard gameBoard, String filePath) throws IOException {
        // Read the saved game data from the file
        String savedData;
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            StringBuilder builder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                builder.append(line).append("\n");
            }
            savedData = builder.toString();
        }

        // Reset the GridPane and load the GameBoard state.
        GameManager.getInstance().getBoardGrid().getChildren().clear();
        // Read the ChitCard grid to the GridPane.
        GameManager.getInstance().getBoardGrid().add(chitCardGrid, 4, 4, 5, 5);

        gameBoard.load(savedData);
        // Rerender the two Grids.
        gameBoard.render(GameManager.getInstance().getBoardGrid(), GameManager.getInstance().getChitCardGrid());
        gameBoard.renderChitCard(GameManager.getInstance().getBoardGrid(), GameManager.getInstance().getChitCardGrid());
        gameBoard.renderButtons(GameManager.getInstance().getBoardGrid());
    }

    /**
     * Restarts the game
     * */
    public void restart(Stage primaryStage) {
        GameManager.getInstance().reset();
        start(primaryStage);
    }

    /**
     * Cleans up the references and resets them before restarting the game
     * */
    public void cleanup() {
        setBoardGrid(null);
        setChitCardGrid(null);
        MovementManager.getInstance().reset();
        TurnManager.getInstance().reset();
    }

    /**
     * Gets the stage for the scene
     * @return The Stage for the scene
     * */
    public Stage getPrimaryStage() {
        return stage;
    }

    /**
     * Gets the main grid of game board
     * @return The Grid Pane of the main game board
     * */
    public GridPane getBoardGrid() {
        return boardGrid;
    }

    /**
     * A method to set the main grid of game board.
     * @param boardGrid: The Grid Pane of the main game board.
     */
    public void setBoardGrid(GridPane boardGrid) {
        this.boardGrid = boardGrid;
    }

    /**
     * Gets the chit cards grid of game board
     * @return The Grid Pane of the chit cards area
     * */
    public GridPane getChitCardGrid() {
        return chitCardGrid;
    }

    /**
     * A method to set the chit cards grid of game board.
     * @param chitCardGrid: The chit cards grid of game board.
     */
    public void setChitCardGrid(GridPane chitCardGrid) {
        this.chitCardGrid = chitCardGrid;
    }

    /**
     * Reads the NUM_OF_PLAYERS property from the game_config.properties file,
     * and if not found, uses a default value of 2.
     * @return The number of players
     */
    public int readNumOfPlayers() {
        Properties properties = new Properties();
        try (FileInputStream fis = new FileInputStream("Project/Sprint Four/src/com/fierydragon/game_config.properties")) {
            properties.load(fis);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Get the NUM_OF_PLAYERS property, and if not found, use default value
        int numOfPlayers = Integer.parseInt(properties.getProperty("NUM_OF_PLAYERS", String.valueOf(DEFAULT_NUM_OF_PLAYERS)));

        if (numOfPlayers < MIN_PLAYERS || numOfPlayers > MAX_PLAYERS) {
            throw new RuntimeException("Number of players should be between " + MIN_PLAYERS + " and " + MAX_PLAYERS);
        }

        return numOfPlayers;
    }
}
