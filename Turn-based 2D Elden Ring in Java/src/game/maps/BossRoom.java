package game.maps;

import edu.monash.fit2099.engine.positions.FancyGroundFactory;
import edu.monash.fit2099.engine.positions.World;
import game.environments.Cliff;
import game.environments.Dirt;
import game.environments.SummonSign;

/**
 * A class representing the Boss Room Map.
 * Created by:
 * @author Bryan Wong
 * Modified by:
 *
 * @version 1.0
 * @see FancyGroundFactory
 * @see World
 * @see Cliff
 * @see Dirt
 * @see SummonSign
 */

public class BossRoom extends Maps {
    /**
     * BossRoom constructor. Will create a GameMap instance for Boss Room GameMap.
     * Will create a new GameMap instance and add it to the world.
     * @param world: The Elden Ring World class instance.
     */
    public BossRoom(World world) {
        super(new FancyGroundFactory(new Cliff(), new Dirt(), new SummonSign()), MapDisplay.BOSS_ROOM, world);
    }
}
