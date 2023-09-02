package game.environments;

import edu.monash.fit2099.engine.positions.Ground;
import edu.monash.fit2099.engine.positions.Location;
import game.enemies.EastSpawningFactory;
import game.enemies.SpawningFactory;
import game.enemies.WestSpawningFactory;
import game.utils.Spawnable;

/**
 * An abstract class that represents a Ground that can spawn Enemies.
 * Created by:
 * @author Bryan Wong
 * Modified by:
 *
 * @version 1.0
 * @see Ground
 * @see Location
 * @see EastSpawningFactory
 * @see SpawningFactory
 * @see WestSpawningFactory
 * @see Spawnable
 */

public abstract class SpawnableGround extends Ground implements Spawnable {
    /**
     * A String signifying the name of the Ground.
     */
    private String name;
    /**
     * An integer representing the limit of spawnable Enemies this Ground can spawn.
     */
    private int spawningLimit;
    /**
     * A SpawningFactory instance representing the factory to spawn certain Enemies on certain side of the map.
     * Default set to null.
     */
    private SpawningFactory spawningFactory = null;

    /**
     * SpawnableGround Constructor.
     * Will call setters to set data attributes of SpawnableGround using parameters given.
     * Calls parent class to set inherited data attributes.
     * Default create an empty ArrayList for spawnedEnemies at creation.
     *
     * @param displayChar: Character to display for this type of terrain.
     * @param name: String that signifies the name of the Ground.
     * @param spawningLimit: Integer representing the limit of spawnable Enemies this Ground can spawn.
     */
    public SpawnableGround(char displayChar, String name, int spawningLimit) {
        super(displayChar);                     // Set the display character of the Ground.
        // Set name and spawningLimit of the SpawnableGround.
        setName(name);
        setSpawningLimit(spawningLimit);
    }

    /**
     * A method to return the name data attribute.
     * @return the name of the Ground.
     */
    public String getName() {
        return name;
    }

    /**
     * A method to return the spawningLimit data attribute.
     * @return the limit of spawnable Enemies this Ground can spawn.
     */
    public int getSpawningLimit() {
        return spawningLimit;
    }

    /**
     * A method to return the spawningFactory data attribute.
     * @return the SpawningFactory instance representing the factory to spawn certain Enemies on certain side of the map.
     */
    public SpawningFactory getSpawningFactory() {
        return spawningFactory;
    }

    /**
     * A method to set the name data attribute.
     * @param name: The String which signifies the name of the Ground.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * A method to set the spawningLimit data attribute.
     * @param spawningLimit: The limit of spawnable Enemies this Ground can spawn.
     */
    public void setSpawningLimit(int spawningLimit) {
        this.spawningLimit = spawningLimit;
    }

    /**
     * A method to set the spawningFactory data attribute.
     * @param spawningFactory: The the SpawningFactory instance representing the factory to spawn certain Enemies on certain side of the map.
     */
    public void setSpawningFactory(SpawningFactory spawningFactory) {
        this.spawningFactory = spawningFactory;
    }

    /**
     * SpawnableGround can also experience the joy of time.
     * Check whether its on the West or East side of the map to spawn different Enemies.
     * Will call spawn method every tick and decide whether to spawn the Enemy or not.
     * @param location The location of the Ground to spawn the Enemy.
     */
    @Override
    public void tick(Location location) {
        // If the SpawnableGround is on the West side of the map, set the spawningFactor to the WestSpawningFactory instance
        // if they haven't been set yet (spawningFactory is null).
        if ((this.getSpawningFactory() == null) && (location.x() <= (location.map().getXRange().max() / 2))) {
            // Set SpawningFactory instance to the West Spawning Factory.
            this.setSpawningFactory(new WestSpawningFactory());
        }
        // Else if the SpawnableGround is on the East side of the map, set the spawningFactor to the EastSpawningFactory instance
        // if they haven't been set yet (spawningFactory is null).
        else if ((this.getSpawningFactory() == null) && (location.x() > (location.map().getXRange().max() / 2))) {
            // Set SpawningFactory instance to the East Spawning Factory.
            this.setSpawningFactory(new EastSpawningFactory());
        }
        // Call the spawn method while passing in the location and the SpawningFactory instance.
        this.spawn(location);
    }
}
