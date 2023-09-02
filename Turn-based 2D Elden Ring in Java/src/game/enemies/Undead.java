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
 * @author Bryan Wong
 * Modified by:
 * @author Po Han Tay
 * @version 1.0
 * @see ActionList
 * @see Actor
 * @see GameMap
 * @see WeaponItem
 * @see AttackAction
 * @see Status
 * @see Utils
 */

public abstract class Undead extends Enemy {
    /**
     * Undead Constructor.
     * Calls parent class to set inherited data attributes.
     * Sets type to "Undead"
     *
     * @param name: The name of the Undead.
     * @param displayChar: The character that will represent the Undead in the display.
     * @param hitPoints: The Undead starting hit points.
     * @param spawningChance: The percentage chance the Enemy can spawn every turn.
     * @param despawningChance: The percentage chance the Undead can despawn every turn.
     * @param runeMin: The minimum number of Runes given by the Undead when killed by the Player.
     * @param runeMax: The maximum number of Runes given by the Undead when killed by the Player.
     * @param weapon: The weapon the Undead uses to attack.
     */
    public Undead(String name, char displayChar, int hitPoints, int spawningChance, int despawningChance,  int runeMin, int runeMax, WeaponItem weapon) {
        // Set the Enemy data attributes of the Undead by calling the parent constructor.
        super(name, displayChar, hitPoints, spawningChance, despawningChance,"Undead", runeMin, runeMax, weapon);

        // Add a capability signifying that Undead is not hostile to other Undead types.
        this.addCapability(EnemyStatus.NOT_HOSTILE_TO_UNDEAD);
    }

    /**
     * The Undead can be attacked by any Actor that has the HOSTILE_TO_ENEMY capability except for those with the
     * NOT_HOSTILE_TO_UNDEAD capability.
     *
     * @param otherActor: The Actor that might be performing attack
     * @param direction: String representing the direction of the other Actor
     * @param map: current GameMap
     * @return the list of allowable actions that can be performed by the other Actor to this Undead.
     */
    @Override
    public ActionList allowableActions(Actor otherActor, String direction, GameMap map) {
        // Create an ActionList containing a list of allowable actions to be performed on this Undead.
        ActionList actions = new ActionList();

        // If the other Actor does not have the capability NOT_HOSTILE_TO_UNDEAD and has capability HOSTILE_TO_ENEMY,
        // meaning the Actor can attack this Undead Enemy.
        // Enemies that are not hostile to Undead (like the Undead themselves) are unable to attack the Undead.
        if (!otherActor.hasCapability(EnemyStatus.NOT_HOSTILE_TO_UNDEAD) && otherActor.hasCapability(EnemyStatus.HOSTILE_TO_ENEMY)) {
            // Call the getAttackTypes utility method which will get the ActionList of all performable Attack types on this Undead.
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
