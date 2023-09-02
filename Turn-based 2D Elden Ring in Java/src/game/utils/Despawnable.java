package game.utils;

/**
 * A Despawnable interface that enforces a .despawn() method for despawning Actors.
 * Created by:
 * @author Bryan Wong
 * Modified by:
 *
 * @version 1.0
 */

public interface Despawnable {
    /**
     * A method which will despawn the Actor based on despawningChance every turn.
     * @return true if Enemy despawns and false otherwise.
     */
    boolean despawn();
}
