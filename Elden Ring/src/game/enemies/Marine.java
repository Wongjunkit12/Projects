package game.enemies;

import edu.monash.fit2099.engine.actions.ActionList;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.positions.GameMap;
import edu.monash.fit2099.engine.weapons.WeaponItem;
import game.actions.AttackAction;
import game.utils.Status;
import game.utils.Utils;

/**
 * An abstract class that represents the type of Enemy that can attack the Player. Inherits from Enemy.
 * Created by:
 * @author Po Han Tay
 * Modified by:
 * @author Bryan Wong
 * @version 1.0
 * @see ActionList
 * @see Actor
 * @see GameMap
 * @see WeaponItem
 * @see AttackAction
 * @see Status
 * @see Utils
 */

public abstract class Marine extends Enemy{
    /**
     * Marine Constructor.
     * Calls parent class to set inherited data attributes.
     * Sets type to "Marine"
     *
     * @param name: The name of the Marine.
     * @param displayChar: The character that will represent the Marine in the display.
     * @param hitPoints: The Marine starting hit points.
     * @param spawningChance: The percentage chance the Enemy can spawn every turn.
     * @param despawningChance: The percentage chance the Marine can despawn every turn.
     * @param runeMin: The minimum number of Runes given by the Marine when killed by the Player.
     * @param runeMax: The maximum number of Runes given by the Marine when killed by the Player.
     * @param weapon: The Weapon Item the Marine uses to attack.
     */
    public Marine(String name, char displayChar, int hitPoints, int spawningChance, int despawningChance, int runeMin, int runeMax, WeaponItem weapon) {
        // Set the Enemy data attributes of the Marine by calling the parent constructor.
        super(name, displayChar, hitPoints, spawningChance, despawningChance, "Marine", runeMin, runeMax, weapon);

        // Add a capability signifying that Marine is not hostile to other Marine types.
        this.addCapability(EnemyStatus.NOT_HOSTILE_TO_MARINE);
    }

    /**
     * The Marine can be attacked by any Actor that has the HOSTILE_TO_ENEMY capability except for those with the
     * NOT_HOSTILE_TO_MARINE capability.
     *
     * @param otherActor: The Actor that might be performing attack
     * @param direction: String representing the direction of the other Actor
     * @param map: current GameMap
     * @return the list of allowable actions that can be performed by the other Actor to this Marine.
     */
    @Override
    public ActionList allowableActions(Actor otherActor, String direction, GameMap map) {
        // Create an ActionList containing a list of allowable actions to be performed on this Marine.
        ActionList actions = new ActionList();

        // If the other Actor does not have the capability NOT_HOSTILE_TO_MARINE and has capability HOSTILE_TO_ENEMY,
        // meaning the Actor can attack this Marine Enemy.
        // Enemies that are not hostile to Marine (like the Marine themselves) are unable to attack the Marine.
        if (!otherActor.hasCapability(EnemyStatus.NOT_HOSTILE_TO_MARINE) && otherActor.hasCapability(EnemyStatus.HOSTILE_TO_ENEMY)) {
            // Call the getAttackTypes utility method which will get the ActionList of all performable Attack types on this Marine.
            actions = Utils.getAttackTypes(this, otherActor, direction);

            // If the Actor is the Player, create an AttackAction using IntrinsicWeapon as Player can punch and use Weapons.
            if (otherActor.hasCapability(Status.PLAYER)) {
                // Add an AttackAction using IntrinsicWeapon to the current ActionList.
                actions.add(new AttackAction(this, direction));
            }
        }
        return actions;
    }
}
