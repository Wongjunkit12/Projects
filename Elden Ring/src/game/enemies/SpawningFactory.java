package game.enemies;

import edu.monash.fit2099.engine.positions.GameMap;
import edu.monash.fit2099.engine.positions.Location;
import game.utils.RandomNumberGenerator;

/**
 * An interface which allows the spawning of different Enemies on different side of the GameMap.
 * Created by:
 * @author Bryan Wong
 * Modified by:
 * @author Po Han Tay
 * @version 1.0
 * @see GameMap
 * @see Location
 * @see RandomNumberGenerator
 */

public interface SpawningFactory {
    /**
     * A method used to spawn an Enemy at the given Location. A default method inherited from its subclasses.
     * @param enemyToSpawn: The Enemy to spawned at that Location.
     * @param gameMap: The GameMap the SpawnableGround is located in.
     * @param location: The Location of the SpawnableGround.
     * @return true if an Enemy has been spawned, false otherwise.
     */
    default boolean spawnEnemy(Enemy enemyToSpawn, GameMap gameMap, Location location) {
        // If the randomly generated number is less than or equal to the Enemy spawning chance and if there's no Actor
        // at that Location, spawn the Enemy on that Location.
        if ((RandomNumberGenerator.getRandomInt(1, 100) <= enemyToSpawn.getSpawningChance()) &&
            !location.containsAnActor()) {
            // Add the Enemy to the SpawnableGround Location.
            gameMap.at(location.x(), location.y()).addActor(enemyToSpawn);

            // Return true to indicate an Enemy has been successfully spawn.
            return true;
        }
        // Else if the Enemy failed to spawn, return false.
        else {
            // Return false if no Enemy can be spawned.
            return false;
        }
    }

    /**
     * A method used to spawn an Animal Enemy type based on certain side of the map.
     * @param gameMap: The GameMap the SpawnableGround is located in.
     * @param location: The Location of the SpawnableGround.
     * @return true if an Enemy has been spawned, false otherwise.
     */
    boolean createAnimal(GameMap gameMap, Location location);

    /**
     * A method used to spawn a Marine Enemy type based on certain side of the map.
     * @param gameMap: The GameMap the SpawnableGround is located in.
     * @param location: The Location of the SpawnableGround.
     * @return true if an Enemy has been spawned, false otherwise.
     */
    boolean createMarine(GameMap gameMap, Location location);

    /**
     * A method used to spawn an Undead Enemy type based on certain side of the map.
     * @param gameMap: The GameMap the SpawnableGround is located in.
     * @param location: The Location of the SpawnableGround.
     * @return true if an Enemy has been spawned, false otherwise.
     */
    boolean createUndead(GameMap gameMap, Location location);

    /**
     * A method used to spawn a Dog Enemy.
     * @param gameMap: The GameMap the SpawnableGround is located in.
     * @param location: The Location of the SpawnableGround.
     * @return true if an Enemy has been spawned, false otherwise.
     */
    boolean createDog(GameMap gameMap, Location location);

    /**
     * A method used to spawn a Godrick Soldier Enemy.
     * @param gameMap: The GameMap the SpawnableGround is located in.
     * @param location: The Location of the SpawnableGround.
     * @return true if an Enemy has been spawned, false otherwise.
     */
    boolean createSoldier(GameMap gameMap, Location location);

    /**
     * A method used to spawn a Guest from another Realm.
     * @param gameMap: The GameMap the SpawnableGround is located in.
     * @param location: The Location of the SpawnableGround.
     * @param summonedEnemy: The summoned Guest from another Realm.
     * @return true if an Enemy has been spawned, false otherwise.
     */
    boolean createSummonable(GameMap gameMap, Location location, Enemy summonedEnemy);
}
