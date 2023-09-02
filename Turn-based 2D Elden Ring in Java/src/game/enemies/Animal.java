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

public abstract class Animal extends Enemy{
    /**
     * Animal Constructor.
     * Calls parent class to set inherited data attributes.
     * Sets type to "Animal"
     *
     * @param name: The name of the Animal.
     * @param displayChar: The character that will represent the Animal in the display.
     * @param hitPoints: The Animal starting hit points.
     * @param spawningChance: The percentage chance the Enemy can spawn every turn.
     * @param despawningChance: The percentage chance the Animal can despawn every turn.
     * @param runeMin: The minimum number of Runes given by the Animal when killed by the Player.
     * @param runeMax: The maximum number of Runes given by the Animal when killed by the Player.
     * @param weapon: The Weapon Item the Animal uses to attack.
     */
    public Animal(String name, char displayChar, int hitPoints, int spawningChance, int despawningChance, int runeMin, int runeMax, WeaponItem weapon) {
        // Set the Enemy data attributes of the Animal by calling the parent constructor.
        super(name, displayChar, hitPoints, spawningChance, despawningChance, "Animal", runeMin, runeMax, weapon);

        // Add a capability signifying that Animal is not hostile to other Animal types.
        this.addCapability(EnemyStatus.NOT_HOSTILE_TO_ANIMAL);
    }

    /**
     * The Animal can be attacked by any Actor that has the HOSTILE_TO_ENEMY capability except for those with the
     * NOT_HOSTILE_TO_ANIMAL capability.
     *
     * @param otherActor: The Actor that might be performing attack
     * @param direction: String representing the direction of the other Actor
     * @param map: current GameMap
     * @return the list of allowable actions that can be performed by the other Actor to this Animal.
     */
    @Override
    public ActionList allowableActions(Actor otherActor, String direction, GameMap map) {
        // Create an ActionList containing a list of allowable actions to be performed on this Animal.
        ActionList actions = new ActionList();

        // If the other Actor does not have the capability NOT_HOSTILE_TO_ANIMAL and has capability HOSTILE_TO_ENEMY,
        // meaning the Actor can attack this Animal Enemy.
        // Enemies that are not hostile to Animal (like the Animal themselves) are unable to attack the Animal.
        if (!otherActor.hasCapability(EnemyStatus.NOT_HOSTILE_TO_ANIMAL) && otherActor.hasCapability(EnemyStatus.HOSTILE_TO_ENEMY)) {
            // Call the getAttackTypes utility method which will get the ActionList of all performable Attack types on this Animal.
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
