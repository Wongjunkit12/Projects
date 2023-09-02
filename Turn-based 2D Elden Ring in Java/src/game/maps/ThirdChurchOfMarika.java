package game.maps;

import edu.monash.fit2099.engine.positions.FancyGroundFactory;
import edu.monash.fit2099.engine.positions.World;
import game.environments.Dirt;
import game.environments.Floor;
import game.environments.Wall;

/**
 * A class representing the Third Church of Marika Map.
 * Created by:
 * @author Bryan Wong
 * Modified by:
 *
 * @version 1.0
 * @see FancyGroundFactory
 * @see World
 * @see Dirt
 * @see Floor
 * @see Wall
 */

public class ThirdChurchOfMarika extends Maps {
    /**
     * ThirdChurchOfMarika constructor. Will create a GameMap instance for Third Church of Marika GameMap.
     * Will create a new GameMap instance and add it to the world.
     * @param world: The Elden Ring World class instance.
     */
    public ThirdChurchOfMarika(World world) {
        super(new FancyGroundFactory(new Dirt(), new Floor(), new Wall()), MapDisplay.THIRD_CHURCH_OF_MARIKA, world);
    }
}
