package game.maps;

import edu.monash.fit2099.engine.positions.FancyGroundFactory;
import edu.monash.fit2099.engine.positions.World;
import game.environments.Floor;
import game.environments.Wall;

/**
 * A class representing the Roundtable Hold Map.
 * Created by:
 * @author Bryan Wong
 * Modified by:

 * @version 1.0
 * @see FancyGroundFactory
 * @see World
 * @see Floor
 * @see Wall
 */

public class RoundtableHold extends Maps {
    /**
     * RoundtableHold constructor. Will create a GameMap instance for Roundtable Hold GameMap.
     * Will create a new GameMap instance and add it to the world.
     * @param world: The Elden Ring World class instance.
     */
    public RoundtableHold(World world) {
        super(new FancyGroundFactory(new Floor(), new Wall()), MapDisplay.ROUNDTABLE_HOLD, world);
    }
}
