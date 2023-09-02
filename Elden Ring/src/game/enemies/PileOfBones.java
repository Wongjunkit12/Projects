package game.enemies;

import edu.monash.fit2099.engine.actions.Action;
import edu.monash.fit2099.engine.actions.ActionList;
import edu.monash.fit2099.engine.actions.DoNothingAction;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.displays.Display;
import edu.monash.fit2099.engine.positions.GameMap;
import edu.monash.fit2099.engine.positions.Location;
import game.actions.DespawnAction;
import game.utils.Despawnable;
import game.utils.Spawnable;
import game.utils.Status;

/**
 * A class that represents the Pile of Bones the Undead turn into. Inherits from Undead.
 * Created by:
 * @author Bryan Wong
 * Modified by:
 *
 * @version 1.0
 * @see Action
 * @see ActionList
 * @see DoNothingAction
 * @see Actor
 * @see Display
 * @see GameMap
 * @see Location
 * @see DespawnAction
 * @see Despawnable
 * @see Spawnable
 * @see Status
 */


public class PileOfBones extends Undead implements Spawnable, Despawnable {
    /**
     * Undead Actor that turned into a Pile of Bones, ready to be resurrected.
     * */
    private final Actor RESURRECTABLE_ENEMY;
    /**
     * Number of turns before the Pile Of Bones resurrect into an Undead again.
     * */
    private int turnsToResurrect;

    /**
     * PileOfBones Constructor.
     * Calls parent class to set inherited data attributes.
     * Sets despawning chance to be 0% as Pile of Bones cannot despawn.
     * @param resurrectableEnemy: The resurrectableEnemy that turned into the Pile of Bones, ready to resurrect after 3 turns.
     * @param turnsToResurrect: The number of turns it takes to ressurect the Undead again.
     */
    public PileOfBones(Actor resurrectableEnemy, int turnsToResurrect) {
        // Call the Undead parent class with its respective data attributes.
        // Get the weapon in the Undead inventory and put it as the weapon parameter.
        super("Pile of Bones", 'X', 1, 100, 0, 35, 892, resurrectableEnemy.getWeaponInventory().get(0));

        this.RESURRECTABLE_ENEMY = resurrectableEnemy;    // Set the Undead Enemy to re-resurrect to the input undeadEnemy.

        setTurnsToResurrect(turnsToResurrect);      // Set the turnsToResurrect of Undead.

        // Toggle the portable boolean as the PileOfBones can drop the weapon when killed.
        this.getWeaponInventory().get(0).togglePortability();
    }

    /**
     * A method to return the RESURRECTABLE_ENEMY data attribute.
     * @return the RESURRECTABLE_ENEMY instance.
     */
    public Actor getRESURRECTABLE_ENEMY() {
        return RESURRECTABLE_ENEMY;
    }

    /**
     * A method to return the turnsToResurrect data attribute.
     * @return the number of turns before the Undead resurrects.
     */
    public int getTurnsToResurrect() {
        return turnsToResurrect;
    }

    /**
     * A method to set the turnsToResurrect data attribute.
     * @param turnsToResurrect: The number of turns before the Undead resurrects.
     */
    public void setTurnsToResurrect(int turnsToResurrect) {
        this.turnsToResurrect = turnsToResurrect;
    }

    /**
     * At each turn, decrement the turnsToResurrect by one. Then at turnsToResurrect == 0, call the .spawn() and despawn()
     * method to resurrrect the Undead from a Pile of Bones.
     *
     * @param actions: Collection of possible Actions for this Enemy.
     * @param lastAction: The Action this Enemy took last turn. Can do interesting things in conjunction with Action.getNextAction().
     * @param map: The map containing the Enemy.
     * @param display: The I/O object to which messages may be written.
     * @return the valid action that can be performed in that iteration or null if no valid action is found.
     */
    @Override
    public Action playTurn(ActionList actions, Action lastAction, GameMap map, Display display) {
        setTurnsToResurrect(getTurnsToResurrect() - 1);     // Decrement the turnsToResurrect by one

        // If the number of turns to resurrect is 0 (time to respawn), then resurrect the undead.
        if (getTurnsToResurrect() == 0) {
            // Toggle the portable boolean as the RESURRECTABLE_ENEMY cannot drop the Weapon when they are killed,
            // only the Pile Of Bones can.
            this.getWeaponInventory().get(0).togglePortability();

            // remove the DEAD capability for the RESURRECTABLE_ENEMY as the RESURRECTABLE_ENEMY has resurrected
            RESURRECTABLE_ENEMY.removeCapability(Status.DEAD);

            Location location = map.locationOf(this);   // Get the location of the PileOfBones.

            // Despawn the PileOfBones so that the RESURRECTABLE_ENEMY can be spawned in.
            if (despawn()) {
                new DespawnAction().execute(this, map);
            }
            // Spawn the RESURRECTABLE_ENEMY at the PileOfBones current location.
            this.spawn(location);       // Call the spawn method to resurrect the Undead_Enemy
            display.println(String.format("Pile of Bones resurrects %s.", getRESURRECTABLE_ENEMY()));
        }
        return new DoNothingAction();       //  Do nothing constantly as Pile OfBones cannot do anything.
    }

    /**
     * A method to resurrect the Undead by spawning it back into the map.
     * Will only spawn once the turnsToResurrect becomes 0.
     * @param location: The location of the Ground to spawn the Undead.
     */
    @Override
    public void spawn(Location location) {
        // Resets the resurrectable Enemy health back to maximum.
        getRESURRECTABLE_ENEMY().increaseMaxHp(0);
        // Add the resurrectable Enemy to the GameMap at the Location given.
        location.map().at(location.x(), location.y()).addActor(getRESURRECTABLE_ENEMY());
    }

    /**
     * Despawn the PileOfBones before resurrecting the Undead Enemy.
     * @return true always as PileOfBones will be removed from the map before resurrection.
     */
    @Override
    public boolean despawn() {
        return true;
    }
}
