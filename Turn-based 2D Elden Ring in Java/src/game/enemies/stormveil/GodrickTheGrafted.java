package game.enemies.stormveil;

import edu.monash.fit2099.engine.actions.Action;
import edu.monash.fit2099.engine.actions.ActionList;
import edu.monash.fit2099.engine.displays.Display;
import edu.monash.fit2099.engine.positions.GameMap;
import edu.monash.fit2099.engine.positions.Location;
import game.behaviours.AttackBehaviour;
import game.behaviours.WanderBehaviour;
import game.enemies.EnemyStatus;
import game.enemies.StormveilEnemy;
import game.items.RemembranceOfTheGrafted;
import game.utils.Status;
import game.weapons.AxeOfGodrick;
import game.weapons.GraftedDragon;

/**
 * A class that represents the Boss - Godrick the Grafted. Inherits from StormveilEnemy.
 * Created by:
 * @author Bryan Wong
 * Modified by:
 *
 * @version 1.0
 * @see Action
 * @see ActionList
 * @see Display
 * @see GameMap
 * @see Location
 * @see AttackBehaviour
 * @see WanderBehaviour
 * @see EnemyStatus
 * @see StormveilEnemy
 * @see RemembranceOfTheGrafted
 * @see Status
 * @see AxeOfGodrick
 * @see GraftedDragon
 */

public class GodrickTheGrafted extends StormveilEnemy {
    /**
     * The x-coordinate of Godrick The Grafted spawning Location.
     */
    private final int X_SPAWN_LOCATION = 24;
    /**
     * The y-coordinate of Godrick The Grafted spawning Location.
     */
    private final int Y_SPAWN_LOCATION = 4;
    /**
     * The GameMap Godrick The Grafted spawns in.
     */
    private GameMap spawningMap;

    /**
     * GodrickTheGrafted Constructor.
     * Calls parent class to set inherited data attributes.
     * Sets despawning chance to be 0% as boss cannot despawn.
     * @param spawningMap: The GameMap in which Godrick The Grafted spawns.
     */
    public GodrickTheGrafted(GameMap spawningMap) {
        // Set the Enemy and StormveilEnemy data attributes of the Godrick The Grafted by calling the parent constructor.
        // Starts off with am Axe of Godrick as it's starting weapon.
        // Sets runeMin and runeMax to 20000 as Godrick the Grafted.
        super("Godrick The Grafted", 'Y', 6080, 100, 0, 20000, 20000, new AxeOfGodrick());
        // Set the GameMap where Godrick The Gradted spawns in.
        this.setSpawningMap(spawningMap);

        // Add a capability DEMIGOD to signify this is a Demigod.
        this.addCapability(EnemyStatus.DEMIGOD);
        // Add the HOSTILE_TO_ENEMY capability meaning it can be attacked by other Enemies with certain conditions.
        this.addCapability(EnemyStatus.HOSTILE_TO_ENEMY);
        // Add the FIRST_PHASE capability signifying the Boss is at it's first phase now.
        this.addCapability(EnemyStatus.FIRST_PHASE);

        // Add Attacking and Wandering behaviours to Godrick The Grafted.
        super.getBehaviours().put(0, new AttackBehaviour());
        super.getBehaviours().put(999, new WanderBehaviour());

        // Toggle the Axe of Godrick portability to prevent it from being dropped when Godrick the Grafted dies.
        this.getWeaponInventory().get(0).togglePortability();
        // Add a RemembranceOfTheGrafted Item to its inventory to be dropped when killed.
        this.addItemToInventory(new RemembranceOfTheGrafted());
    }

    /**
     * A method to return the spawningMap data attribute.
     * @return the GameMap Godrick The Grafted spawns in.
     */
    public GameMap getSpawningMap() {
        return spawningMap;
    }

    /**
     * A method to set the spawningMap data attribute.
     * @param spawningMap: The GameMap Godrick The Grafted spawns in.
     */
    public void setSpawningMap(GameMap spawningMap) {
        this.spawningMap = spawningMap;
    }

    /**
     * At each turn, checks HP to determine which stage the Boss is currently in. If <= 50% of his HP, remove the Axe of Godrick
     * and replace it with a Grafted Dragon WeaponItem in its inventory.
     * Calls the super.playTurn after the phase checks to initiate its turn.
     *
     * @param actions: Collection of possible Actions for this Enemy.
     * @param lastAction: The Action this Enemy took last turn. Can do interesting things in conjunction with Action.getNextAction().
     * @param map: The map containing the Enemy.
     * @param display: The I/O object to which messages may be written.
     * @return the valid action that can be performed in that iteration or null if no valid action is found.
     */
    @Override
    public Action playTurn(ActionList actions, Action lastAction, GameMap map, Display display) {
        // The trigger for the second phase which is 50% of it's maximum health.
        int secondPhaseTrigger = this.getMaxHp() / 2;

        // If the current health is less then or equal to 50% of its maximum, trigger the second phase of the Boss.
        if (this.hasCapability(EnemyStatus.FIRST_PHASE) && this.hitPoints <= secondPhaseTrigger) {
            // Remove the Axe of Godrick from its inventory.
            this.removeWeaponFromInventory(this.getWeaponInventory().get(0));
            // Replace it with a Grafted Dragon.
            this.addWeaponToInventory(new GraftedDragon());
            // Toggle the Grafted Dragon portability to prevent it from being dropped when Godrick the Grafted dies.
            this.getWeaponInventory().get(0).togglePortability();
            // Remove the FIRST_PHASE capability to signify that the Boss is now in it's second phase.
            this.removeCapability(EnemyStatus.FIRST_PHASE);
        }
        // Call and return the parent playTurn method to continue the Godrick The Grafted's turn.
        return super.playTurn(actions, lastAction, map, display);
    }

    /**
     * A method to remove the Enemy from the Game Map once Player rests or dies.
     * Godrick will not be removed, instead be moved back to its spawning Location with full health.
     * @param map: The Game Map.
     * @param location: THe location of the Last Site of Lost Grace the Player rested at.
     */
    @Override
    public void reset(GameMap map, Location location) {
        // If Godrick the Gradted has not been slain yet, move him back to starting position when resting.
        if (!this.hasCapability(Status.DEAD)) {
            // Resets the Godrick The Grafted health back to maximum.
            this.resetMaxHp(getMaxHp());
            // Only moves Godrick The Grafted if there isn't an Actor at his spawn Location.
            if (!getSpawningMap().at(X_SPAWN_LOCATION, Y_SPAWN_LOCATION).containsAnActor()) {
                // Move Godrick The Grafted back to his spawn point.
                map.moveActor(this, getSpawningMap().at(X_SPAWN_LOCATION, Y_SPAWN_LOCATION));
            }
        }
    }
}
