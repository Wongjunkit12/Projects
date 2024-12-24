package game.maps;

import edu.monash.fit2099.engine.positions.FancyGroundFactory;
import edu.monash.fit2099.engine.positions.World;
import game.environments.*;

/**
 * A class representing the Stormveil Castle Map.
 * Maps.
 * Created by:
 * @author Bryan Wong
 * Modified by:
 *
 * @version 1.0
 * @see FancyGroundFactory
 * @see World
 * @see Cliff
 * @see Dirt
 * @see Floor
 * @see Wall
 */

public class StormveilCastle extends Maps {
    /**
     * StormveilCastle constructor. Will create a GameMap instance for Stormveil Castle GameMap.
     * Will create a new GameMap instance and add it to the world.
     * @param world: The Elden Ring World class instance.
     */
    public StormveilCastle(World world) {
        // Call the parent constructor to create and set the GameMap for this Map.
        // Pass in the Stormveil Castle FancyGroundFactory and MapDisplay to create the GameMap.
        super(new FancyGroundFactory( new Cliff(), new Dirt(), new Floor(), new GustOfWind(), new Cage(), new Barrack(), new Wall()), MapDisplay.STORMVEIL_CASTLE, world);
    }
}
