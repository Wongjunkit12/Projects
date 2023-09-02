package game.maps;

import edu.monash.fit2099.engine.positions.FancyGroundFactory;
import edu.monash.fit2099.engine.positions.GameMap;
import edu.monash.fit2099.engine.positions.World;

import java.util.List;

/**
 * An abstract class representing the creation of a Maps instance for new Maps. Will contain the GameMap data attribute for each
 * Maps.
 * Created by:
 * @author Bryan Wong
 * Modified by:
 *
 * @version 1.0
 * @see FancyGroundFactory
 * @see GameMap
 * @see World
 */

public abstract class Maps {
    /**
     * The GameMap of this Map.
     */
    private GameMap gameMap;

    /**
     * Maps constructor. Will create a GameMap instance based on the fancyGroundFactory and mapDisplay inputted.
     * Will create a new GameMap instance and add it to the world.
     * @param fancyGroundFactory: The FancyGroundFactory of the GameMap.
     * @param mapDisplay: The List of Strings used to display the GameMap to console.
     * @param world: The Elden Ring World class instance.
     */
    public Maps(FancyGroundFactory fancyGroundFactory, List<String> mapDisplay, World world) {
        // Create the GameMap by calling the GameMap constructor and input the given fancyGroundFactory and mapDisplay.
        // Set the new GameMap instance to the gameMap instance variable.
        setGameMap(new GameMap(fancyGroundFactory, mapDisplay));

        // Add the GameMap to the World.
        world.addGameMap(gameMap);
    }

    /**
     * A method to return the gameMap data attribute.
     * @return the GameMap of this Map.
     */
    public GameMap getGameMap() {
        return gameMap;
    }

    /**
     * A method to set the gameMap data attribute.
     * @param gameMap: The GameMap of this Map.
     */
    public void setGameMap(GameMap gameMap) {
        this.gameMap = gameMap;
    }
}
