package game.utils;

import edu.monash.fit2099.engine.positions.Location;

/**
 * A Spawnable interface that enforces a .spawn() method for spawning Actors.
 * Created by:
 * @author Bryan Wong
 * Modified by:
 *
 * @version 1.0
 * @see Location
 */

public interface Spawnable {
    /**
     * A method which will spawn the Actor on the SpawnableGround based on spawningChance every turn.
     * @param location: The location of the Ground to spawn the Actor.
     */
    void spawn(Location location);
}
