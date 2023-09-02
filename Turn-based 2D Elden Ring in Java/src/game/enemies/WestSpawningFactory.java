package game.enemies;

import edu.monash.fit2099.engine.positions.GameMap;
import edu.monash.fit2099.engine.positions.Location;
import game.enemies.limgrave.west.GiantCrab;
import game.enemies.limgrave.west.HeavySkeletalSwordsman;
import game.enemies.limgrave.west.LoneWolf;
import game.enemies.stormveil.Dog;
import game.enemies.stormveil.GodrickSoldier;

/**
 * A class that is used as a Factory to spawn certain actors on the West side of the map.
 * Implements SpawningFactory interface.
 * Created by:
 * @author Po Han Tay
 * Modified by:
 * @author Bryan Wong
 * @version 1.0
 * @see GameMap
 * @see Location
 * @see GiantCrab
 * @see HeavySkeletalSwordsman
 * @see LoneWolf
 * @see Dog
 * @see GodrickSoldier
 */

public class WestSpawningFactory implements SpawningFactory {
    /**
     * A method used to spawn a LoneWolf for West side Gust of Wind.
     * @param gameMap: The GameMap the SpawnableGround is located in.
     * @param location: The Location of the SpawnableGround.
     * @return true if an Enemy has been spawned, false otherwise.
     */
    @Override
    public boolean createAnimal(GameMap gameMap, Location location) {
        // Create a LoneWolf to spawn at that Location.
        Enemy enemyToSpawn = new LoneWolf();
        // Call the spawnEnemy to add the LoneWolf to the GameMap at the specified Location.
        return this.spawnEnemy(enemyToSpawn, gameMap, location);
    }

    /**
     * A method used to spawn a GiantCrab for West side Puddle of Water.
     * @param gameMap: The GameMap the SpawnableGround is located in.
     * @param location: The Location of the SpawnableGround.
     * @return true if an Enemy has been spawned, false otherwise.
     */
    @Override
    public boolean createMarine(GameMap gameMap, Location location) {
        // Create a GiantCrab to spawn at that Location.
        Enemy enemyToSpawn = new GiantCrab();
        // Call the spawnEnemy to add the GiantCrab to the GameMap at the specified Location.
        return this.spawnEnemy(enemyToSpawn, gameMap, location);
    }

    /**
     * A method used to spawn a HeavySkeletalSwordsman for West side Graveyard.
     * @param gameMap: The GameMap the SpawnableGround is located in.
     * @param location: The Location of the SpawnableGround.
     * @return true if an Enemy has been spawned, false otherwise.
     */
    @Override
    public boolean createUndead(GameMap gameMap, Location location) {
        // Create a HeavySkeletalSwordsman to spawn at that Location.
        Enemy enemyToSpawn = new HeavySkeletalSwordsman();
        // Call the spawnEnemy to add the HeavySkeletalSwordsman to the GameMap at the specified Location.
        return this.spawnEnemy(enemyToSpawn, gameMap, location);
    }

    /**
     * A method used to spawn a Dog for West side Cage.
     * @param gameMap: The GameMap the SpawnableGround is located in.
     * @param location: The Location of the SpawnableGround.
     * @return true if an Enemy has been spawned, false otherwise.
     */
    @Override
    public boolean createDog(GameMap gameMap, Location location) {
        // Create a Dog to spawn at that Location.
        Enemy enemyToSpawn = new Dog();
        // Call the spawnEnemy to add the Dog to the GameMap at the specified Location.
        return this.spawnEnemy(enemyToSpawn, gameMap, location);
    }

    /**
     * A method used to spawn a GodrickSoldier for West side Barrack.
     * @param gameMap: The GameMap the SpawnableGround is located in.
     * @param location: The Location of the SpawnableGround.
     * @return true if an Enemy has been spawned, false otherwise.
     */
    @Override
    public boolean createSoldier(GameMap gameMap, Location location) {
        // Create a GodrickSoldier to spawn at that Location.
        Enemy enemyToSpawn = new GodrickSoldier();
        // Call the spawnEnemy to add the GodrickSoldier to the GameMap at the specified Location.
        return this.spawnEnemy(enemyToSpawn, gameMap, location);
    }

    /**
     * A method used to spawn a Guest from another Realm for West side Summon Sign..
     * @param gameMap: The GameMap the SpawnableGround is located in.
     * @param location: The Location of the SpawnableGround.
     * @param summonedEnemy: The summoned Guest from another Realm.
     * @return true if an Enemy has been spawned, false otherwise.
     */
    @Override
    public boolean createSummonable(GameMap gameMap, Location location, Enemy summonedEnemy) {
        // Call the spawnEnemy to add the summonedEnemy to the GameMap at the specified Location.
        return this.spawnEnemy(summonedEnemy, gameMap, location);
    }
}
