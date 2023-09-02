package game.enemies;

import edu.monash.fit2099.engine.positions.GameMap;
import edu.monash.fit2099.engine.positions.Location;
import game.enemies.limgrave.east.GiantCrayfish;
import game.enemies.limgrave.east.GiantDog;
import game.enemies.limgrave.east.SkeletalBandit;
import game.enemies.stormveil.Dog;
import game.enemies.stormveil.GodrickSoldier;

/**
 * A class that is used as a Factory to spawn certain actors on the East side of the map.
 * Implements SpawningFactory interface.
 * Created by:
 * @author Po Han Tay
 * Modified by:
 * @author Bryan Wong
 * @version 1.0
 * @see GameMap
 * @see Location
 * @see GiantCrayfish
 * @see GiantDog
 * @see SkeletalBandit
 * @see Dog
 * @see GodrickSoldier
 */

public class EastSpawningFactory implements SpawningFactory {
    /**
     * A method used to spawn a GiantDog for East side Gust of Wind.
     * @param gameMap: The GameMap the SpawnableGround is located in.
     * @param location: The Location of the SpawnableGround.
     * @return true if an Enemy has been spawned, false otherwise.
     */
    @Override
    public boolean createAnimal(GameMap gameMap, Location location) {
        // Create a GiantDog to spawn at that Location.
        Enemy enemyToSpawn = new GiantDog();
        // Call the spawnEnemy to add the GiantDog to the GameMap at the specified Location.
        return this.spawnEnemy(enemyToSpawn, gameMap, location);
    }

    /**
     * A method used to spawn a GiantCrayfish for East side Puddle of Water.
     * @param gameMap: The GameMap the SpawnableGround is located in.
     * @param location: The Location of the SpawnableGround.
     * @return true if an Enemy has been spawned, false otherwise.
     */
    @Override
    public boolean createMarine(GameMap gameMap, Location location) {
        // Create a GiantCrayfish to spawn at that Location.
        Enemy enemyToSpawn = new GiantCrayfish();
        // Call the spawnEnemy to add the GiantCrayfish to the GameMap at the specified Location.
        return this.spawnEnemy(enemyToSpawn, gameMap, location);
    }

    /**
     * A method used to spawn a SkeletalBandit for East side Graveyard.
     * @param gameMap: The GameMap the SpawnableGround is located in.
     * @param location: The Location of the SpawnableGround.
     * @return true if an Enemy has been spawned, false otherwise.
     */
    @Override
    public boolean createUndead(GameMap gameMap, Location location) {
        // Create a SkeletalBandit to spawn at that Location.
        Enemy enemyToSpawn = new SkeletalBandit();
        // Call the spawnEnemy to add the SkeletalBandit to the GameMap at the specified Location.
        return this.spawnEnemy(enemyToSpawn, gameMap, location);
    }

    /**
     * A method used to spawn a Dog for East side Cage.
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
     * A method used to spawn a GodrickSoldier for East side Barrack.
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
     * A method used to spawn a Guest from another Realm for East side Summon Sign.
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
