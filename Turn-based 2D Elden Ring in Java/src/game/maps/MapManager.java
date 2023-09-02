package game.maps;

import edu.monash.fit2099.engine.items.Item;
import edu.monash.fit2099.engine.positions.GameMap;
import edu.monash.fit2099.engine.positions.Location;
import game.environments.GoldenFogDoor;
import game.environments.GroundStatus;
import game.environments.SiteOfLostGrace;
import game.items.GoldenRunes;
import game.utils.RandomNumberGenerator;

import java.lang.reflect.Constructor;
import java.util.ArrayList;

/**
 * A Map Manager class which manages the switching of GameMaps and the Golden Fog Doors used to travel in between them.
 * Created by:
 * @author Bryan Wong
 * Modified by:
 *
 * @version 1.0
 * @see Item
 * @see GameMap
 * @see GoldenFogDoor
 * @see GroundStatus
 * @see SiteOfLostGrace
 * @see GoldenRunes
 * @see RandomNumberGenerator
 */

public class MapManager {
    /**
     * The GameMap object to manage.
     */
    private GameMap gameMap;
    /**
     * The ArrayList of activated Sites of Lost Grace.
     */
    private ArrayList<SiteOfLostGrace> activatedSites = new ArrayList<SiteOfLostGrace>();
    /**
     * An instance of itself so that only one MapManager instance is created. Default set to null.
     */
    private static MapManager mapManager = null;

    /**
     * MapManager Constructor.
     * @param gameMap: The GameMap to manage.
     */
    public MapManager(GameMap gameMap) {
        updateGameMap(gameMap);
    }

    /**
     * A method to return the gameMap data attribute.
     * @return the GameMap object to manage.
     */
    public GameMap getGameMap() {
        return gameMap;
    }

    /**
     * A method to return the activatedSites data attribute.
     * @return the ArrayList of activated Sites of Lost Grace.
     */
    public ArrayList<SiteOfLostGrace> getActivatedSites() {
        return activatedSites;
    }

    /**
     * A method to set the gameMap data attribute.
     * @param gameMap: The GameMap object to manage.
     */
    public void updateGameMap(GameMap gameMap) {
        this.gameMap = gameMap;
    }

    /**
     * A method to add the activated Site of Lost Grace to the activatedSites List.
     * @param activatedSite: The Site of Lost Grace that was activated.
     */
    public void addActivatedSite(SiteOfLostGrace activatedSite) {
        this.activatedSites.add(activatedSite);
    }

    /**
     * Returns the instance of the mapManager stored as a data attribute.
     * If one has not been created yet, create a new instance of mapManager.
     * @param gameMap: The GameMap object to manage..
     * @return the mapManager instance.
     */
    public static MapManager getInstance(GameMap gameMap) {
        // If an instance of mapManager has not been created yet.
        // Create a new instance of MapManager and pass the GameMap object to managed into it.
        if (mapManager == null) {
            // Create a new instance of mapManager.
            mapManager = new MapManager(gameMap);
        }
        // Else update the GameMap object to manage in the MapManager.
        else {
            mapManager.updateGameMap(gameMap);
        }
        // Return the mapManager instance, either newly created or stored currently as a data attribute.
        return mapManager;
    }

    /**
     * A method to create random amount of Golden Runes and scatter them around the GameMap.
     * @param goldenRunes: The GoldenRunes object to generate.
     * @param minimum: The minimum amount of GoldenRunes to generate in a GameMap.
     * @param maximum: The maximum amount of GoldenRunes to generate in a GameMap.
     */
    public void generateGoldenRunes(GoldenRunes goldenRunes, int minimum, int maximum) {
        try {
            // Get the constructor for the GoldenRunes.
            Class<?> cls = goldenRunes.getClass();
            Constructor<?> constructor;
            constructor = cls.getConstructor();

            // Generate a random amount of Golden Runes for the map from minimum to maximum.
            int amountToGenerate = RandomNumberGenerator.getRandomInt(minimum, maximum);

            int i = 0;
            while (i < amountToGenerate) {
                // Generate random x and y coordinates to place the Golden Runes randomly.
                int xCoord = RandomNumberGenerator.getRandomInt(getGameMap().getXRange().min(), getGameMap().getXRange().max());
                int yCoord = RandomNumberGenerator.getRandomInt(getGameMap().getYRange().min(), getGameMap().getYRange().max());

                // If the Ground can add the Golden Runes and the said Ground has no Items on it,
                // add the GoldenRunes to that Location and increment i by one.
                if (gameMap.at(xCoord, yCoord).getGround().hasCapability(GroundStatus.CAN_GENERATE_GOLDEN_RUNES) &&
                        gameMap.at(xCoord, yCoord).getItems().isEmpty()) {
                    // Add the Golden Runes to that Location. Upcasts it to Item.
                    gameMap.at(xCoord, yCoord).addItem((Item) constructor.newInstance());
                    i++;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * A method to create a Site of Lost Grace in a map.
     * @param name: The name of the Site of Lost Grace.
     * @param xCoord: The x-coordinate of the current Site of Lost Grace in the GameMap.
     * @param yCoord: The y-coordinate of the current Site of Lost Grace in the GameMap.
     * @return the newly created Site of Lost Grace.
     */
    public SiteOfLostGrace createSiteOfLostGrace(String name, int xCoord, int yCoord) {
        // Create a Site of Lost Grace object and add it to the GameMap.
        SiteOfLostGrace siteOfLostGrace = new SiteOfLostGrace(name);
        this.getGameMap().at(xCoord, yCoord).setGround(siteOfLostGrace);
        return siteOfLostGrace;       // Return the newly created SiteOfLostGrace.
    }

    /**
     * A method to create a Golden Fog Door in one map, connecting it to a Golden Fog Door in another map.
     * @param mapName: The name of the GameMap destionation.
     * @param mapSource: The GameMap of the current Golden Fog Door.
     * @param xCoord: The x-coordinate of the current Golden Fog Door in the GameMap.
     * @param yCoord: The y-coordinate of the current Golden Fog Door in the GameMap.
     * @return the newly created Golden Fog Door.
     */
    public GoldenFogDoor createGoldenFogDoors(String mapName, GameMap mapSource, int xCoord, int yCoord) {
        // Create a Golden Fog Door object and add it to the GameMap.
        GoldenFogDoor goldenFogDoor = new GoldenFogDoor(mapName, getGameMap());
        mapSource.at(xCoord, yCoord).setGround(goldenFogDoor);
        return goldenFogDoor;       // Return the newly created GoldenFogDoor.
    }

    /**
     * A method to connect two Golden Fog Doors together via their Location.
     * @param goldenFogDoor: The Golden Fog Door at the source.
     * @param destination: The destination the Golden Fog Door goes to.
     */
    public static void connectGoldenFogDoors(GoldenFogDoor goldenFogDoor, Location destination) {
        // Connect the goldenFogDoor destination.
        goldenFogDoor.setGoldenDoorDestination(destination);
    }
}
