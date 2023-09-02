package game.utils;

import edu.monash.fit2099.engine.positions.GameMap;
import game.environments.SiteOfLostGrace;

import java.util.ArrayList;
import java.util.List;

/**
 * A reset manager class that manages a list of resettables.
 * Created by:
 * @author Adrian Kristanto
 * Modified by:
 * @author Bryan Wong, Po Han Tay.
 * @version 1.0
 * @see GameMap
 * @see SiteOfLostGrace
 */

public class ResetManager {
    /**
     * A list containing Resettables that only resets when resting.
     */
    private List<Resettable> restResettables;
    /**
     * A list containing Resettables that only resets when dead.
     */
    private List<Resettable> deadResettables;
    /**
     * The Game Map.
     */
    private GameMap gameMap;
    /**
     * The SiteOfLostGrace object of the last Site Of Lost Grace the Player rest at.
     */
    private SiteOfLostGrace lastLostGrace;
    /**
     * An integer representing the price of upgrading the Player's max HP.
     */
    private int upgradePrice;
    /**
     * An instance of itself so that only one ResetManager instance is created. Default set to null.
     */
    private static ResetManager resetManager = null;

    /**
     * Constructor for ResetManager.
     * Will create an empty restResettables ArrayList at start by default.
     */
    private ResetManager() {
        // Create empty ArrayLists for Resettables.
        setRestResettables(new ArrayList<Resettable>());
        setDeadResettables(new ArrayList<Resettable>());

        setUpgradePrice(100);   // By default set the upgradePrice to be 100 at start.
    }

    /**
     * A method to return the restResettables data attribute.
     * @return the restResettables ArrayList.
     */
    public List<Resettable> getRestResettables() {
        return restResettables;
    }

    /**
     * A method to return the deadResettables data attribute.
     * @return the deadResettables ArrayList.
     */
    public List<Resettable> getDeadResettables() {
        return deadResettables;
    }

    /**
     * A method to return the gameMap data attribute.
     * @return the Game Map.
     */
    public GameMap getGameMap() {
        return gameMap;
    }

    /**
     * A method to return the lastLostGrace data attribute.
     * @return the SiteOfLostGrace object of the last Site of Lost Grace the Player rests on.
     */
    public SiteOfLostGrace getLastLostGrace() {
        return lastLostGrace;
    }

    /**
     * A method to return the upgradePrice data attribute.
     * @return the price of upgrading the Player's max HP.
     */
    public int getUpgradePrice() {
        return upgradePrice;
    }

    /**
     * A method to set the restResettables data attribute.
     * @param restResettables: The ArrayList of Resettables to set the restResettables data attribute to.
     */
    public void setRestResettables(List<Resettable> restResettables) {
        this.restResettables = restResettables;
    }

    /**
     * A method to set the deadResettables data attribute.
     * @param deadResettables: The ArrayList of Resettables to set the deadResettables data attribute to.
     */
    public void setDeadResettables(List<Resettable> deadResettables) {
        this.deadResettables = deadResettables;
    }

    /**
     * A method to set the Game Map.
     * @param gameMap: The Game Map.
     */
    public void setGameMap(GameMap gameMap) {
        this.gameMap = gameMap;
    }

    /**
     * A method to set the lastLostGrace data attribute.
     * @param lastLostGrace: The SiteOfLostGrace object of the last Site of Lost Grace the Player rests on.
     */
    public void setLastLostGrace(SiteOfLostGrace lastLostGrace) {
        this.lastLostGrace = lastLostGrace;
    }

    /**
     * A method to set the upgradePrice data attribute.
     * @param upgradePrice: The price of upgrading the Player's max HP.
     */
    public void setUpgradePrice(int upgradePrice) {
        this.upgradePrice = upgradePrice;
    }

    /**
     * Returns the instance of the resetManager stored as a data attribute.
     * If one has not been created yet, create a new instance of resetManager.
     * @return the resetManager instance.
     */
    public static ResetManager getInstance() {
        // If an instance of resetManager has not been created yet.
        if (resetManager == null) {
            // Create a new instance of resetManager.
            resetManager = new ResetManager();
        }
        // Return the resetManager instance, either newly created or stored currently as a data attribute.
        return resetManager;
    }

    /**
     * Loops through the restResettables ArrayList and execute each .reset() method in all of them.
     */
    public void runRestResettables() {
        // Loops through the restResettables ArrayList and call each Resettable .reset() method.
        for (Resettable restResettable : getRestResettables()) {
            // Call the reset method of each Resettable.
            restResettable.reset(getGameMap(), getLastLostGrace().getCurrentLocation());
        }
    }

    /**
     * Loops through the deadResettables ArrayList and execute each .reset() method in all of them.
     */
    public void runDeadResettables() {
        // Loops through the deadResettables ArrayList and call each Resettable .reset() method.
        for (Resettable deadResettable : getDeadResettables()) {
            // Call the reset method of each Resettable.
            deadResettable.reset(getGameMap(), getLastLostGrace().getCurrentLocation());
        }
    }

    /**
     * Add the Resettable object to the restResettables ArrayList.
     * @param restResettable: The Resettable object to add to the restResettables ArrayList.
     */
    public void registerRestResettable(Resettable restResettable) {
        restResettables.add(restResettable);        // Add the restResettable to the restResettable ArrayList.
    }

    /**
     * Remove the Resttable object from the restResettables ArrayList.
     * @param restResettable: The Resettable object to remove from the restResettables ArrayList.
     */
    public void removeRestResettable(Resettable restResettable) {
        restResettables.remove(restResettable);     // Remove the restResettable from the restResettables ArrayList.
    }

    /**
     * Add the Resettable object to the deadResettables ArrayList.
     * @param deadResettable: The Resettable object to add to the deadResettables ArrayList.
     */
    public void registerDeadResettable(Resettable deadResettable) {
        deadResettables.add(deadResettable);        // Add the deadResettable to the deadResettables ArrayList.
    }

    /**
     * Remove the Resttable object from the deadResettables ArrayList.
     * @param deadResettable: The Resettable object to remove from the deadResettables ArrayList.
     */
    public void removeDeadResettable(Resettable deadResettable) {
        deadResettables.remove(deadResettable);     // Remove the deadResettable from the deadResettables ArrayList.
    }
}
