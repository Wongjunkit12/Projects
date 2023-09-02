package game.environments;

import edu.monash.fit2099.engine.positions.Location;

/**
 * A class that inherits from SpawnableGround, in which it spawns the Animal type Enemy.
 * Created by:
 * @author Wah Yang Tan
 * Modified by:
 * @author Po Han Tay
 * @version 1.0
 * @see Location
 */

public class Cage extends SpawnableGround {
    /**
     * Cage Constructor
     * Calls parent class to set inherited data attributes.
     * Sets the display character to '<', name to "Cage".
     * Set the spawning limit to the maximum integer value.
     */
    public Cage() {
        // Call the parent constructor class with the specific data attributes the Cage has.
         super('<', "Cage", Integer.MAX_VALUE);
    }

    /**
     * A method to spawn the Dog on the SpawnableGround.
     * @param location: The location of the Ground.
     */
    @Override
    public void spawn(Location location) {
        // If the limit to spawn the Enemy are more than 0, one can be spawn.
        // Call the createDog method inside the spawningFactory to spawn in a Dog if applicable.
        if (getSpawningLimit() > 0) {
            // Decrement the spawningLimit if a Dog successfully spawned, don't do so if none spawned.
            this.setSpawningLimit(this.getSpawningFactory().createDog(location.map(), location) ? (getSpawningLimit() - 1) : getSpawningLimit());
        }
    }
}
