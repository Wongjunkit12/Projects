package game.environments;

import edu.monash.fit2099.engine.positions.Location;

/**
 * A class that inherits from SpawnableGround, in which it spawns the Animal type Enemy.
 * Created by:
 * @author Wah Yang Tan
 * Modified by:
 * @author Po Han Tay, Bryan Wong
 * @version 1.0
 * @see Location
 */

public class GustOfWind extends SpawnableGround {
    /**
     * GustOfWind Constructor.
     * Calls parent class to set inherited data attributes.
     * Sets the display character to '&', name to "GustOfWind.
     * Set the spawning limit to the maximum integer value.
     */
    public GustOfWind() {
        // Call the parent constructor class with the specific data attributes the GustOfWind has.
        super('&', "GustOfWind", Integer.MAX_VALUE);
    }

    /**
     * A method to spawn the Animal on the SpawnableGround.
     * @param location: The location of the Ground.
     */
    @Override
    public void spawn(Location location) {
        // If the limit to spawn the Enemy are more than 0, one can be spawn.
        // Call the createAnimal method inside the spawningFactory to spawn in an Animal if applicable.
        if (getSpawningLimit() > 0) {
            // Decrement the spawningLimit if an Animal successfully spawned, don't do so if none spawned.
            this.setSpawningLimit(this.getSpawningFactory().createAnimal(location.map(), location) ? (getSpawningLimit() - 1) : getSpawningLimit());
        }
    }
}
