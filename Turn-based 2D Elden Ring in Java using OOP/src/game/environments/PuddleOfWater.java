package game.environments;

import edu.monash.fit2099.engine.positions.Location;

/**
 * A class that inherits from SpawnableGround, in which it spawns the Marine type Enemy.
 * Created by:
 * @author Wah Yang Tan
 * Modified by:
 * @author Po Han Tay
 * @version 1.0
 * @see Location
 */

public class PuddleOfWater extends SpawnableGround {
    /**
     * PuddleOfWater Constructor.
     * Calls parent class to set inherited data attributes.
     * Sets the display character to '~', name to "PuddleOfWater".
     * Sets the spawning limit to the maximum integer value.
     */
    public PuddleOfWater() {
        // Call the parent constructor class with the specific data attributes the PuddleOfWater has.
        super('~', "PuddleOfWater", Integer.MAX_VALUE);
    }

    /**
     * A method to spawn the Marine on the SpawnableGround.
     * @param location: The location of the Ground.
     */
    @Override
    public void spawn(Location location) {
        // If the limit to spawn the Enemy are more than 0, one can be spawn.
        // Call the createMarine method inside the spawningFactory to spawn in a Marine if applicable.
        if (getSpawningLimit() > 0) {
            // Decrement the spawningLimit if a Marine successfully spawned, don't do so if none spawned.
            this.setSpawningLimit(this.getSpawningFactory().createMarine(location.map(), location) ? (getSpawningLimit() - 1) : getSpawningLimit());
        }
    }
}
