package game.environments;

import edu.monash.fit2099.engine.positions.Location;

/**
 * A class that inherits from SpawnableGround, in which it spawns the Undead type Enemy.
 * Created by:
 * @author Bryan Wong
 * Modified by:
 * @author Po Han Tay
 * @version 1.0
 * @see Location
 */

public class Graveyard extends SpawnableGround {
    /**
     * Graveyard Constructor.
     * Calls parent class to set inherited data attributes.
     * Sets the display character to 'n', name to "Graveyard".
     * Set the spawning limit to the maximum integer value.
     */
    public Graveyard() {
        // Call the parent constructor class with the specific data attributes the Graveyard has.
        super('n', "Graveyard", Integer.MAX_VALUE);
    }

    /**
     * A method to spawn the Undead on the SpawnableGround.
     * @param location: The location of the Ground.
     */
    @Override
    public void spawn(Location location) {
        // If the limit to spawn the Enemy are more than 0, one can be spawn.
        // Call the createUndead method inside the spawningFactory to spawn in an Undead if applicable.
        if (getSpawningLimit() > 0) {
            // Decrement the spawningLimit if an Undead successfully spawned, don't do so if none spawned.
            this.setSpawningLimit(this.getSpawningFactory().createUndead(location.map(), location) ? (getSpawningLimit() - 1) : getSpawningLimit());
        }
    }
}
