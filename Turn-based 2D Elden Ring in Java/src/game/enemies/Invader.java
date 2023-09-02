package game.enemies;

import edu.monash.fit2099.engine.actions.ActionList;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.positions.GameMap;
import game.behaviours.AttackBehaviour;
import game.behaviours.WanderBehaviour;
import game.characters.playable_characters.classes.Classes;
import game.utils.ResetManager;
import game.utils.Utils;
import game.weapons.Club;

/**
 * A class that represents the Invader. Inherits from Enemy.
 * Created by:
 * @author Po Han Tay
 * Modified by:
 * @author Bryan Wong
 * @version 1.0
 * @see ActionList
 * @see Actor
 * @see GameMap
 * @see AttackBehaviour
 * @see WanderBehaviour
 * @see Classes
 * @see ResetManager
 * @see Utils
 * @see Club
 */

public class Invader extends Enemy {
    /**
     * The class of the Invader.
     */
    private Classes invaderClass;

    /**
     * Invader Constructor.
     * Calls parent class to set inherited data attributes.
     * Sets hitpoints to 300 and Weapon to dummy Club initially until Class is chosen.
     * Sets spawningChance to be 100% as they can guarantee spawn provided the Player summons them and despawningChance
     * to 0 as they cannot despawn naturally.
     * @param invaderClass: The Class type the Invader has.
     */
    public Invader(Classes invaderClass) {
        // Set the Enemy data attributes of the Invader by calling the parent constructor.
        super("Invader", 'ඞ', 300, 100, 0, "Summonee", 1358, 5578, new Club());

        // Set the Invader Class type, along with its health and Weapon.
        this.setInvaderClass(invaderClass);

        // Add capability to indicate they can be summoned.
        this.addCapability(EnemyStatus.SUMMONABLE);
        // Add capability to indicate they cannot be attacked nor attack by other Invaders.
        this.addCapability(EnemyStatus.NOT_HOSTILE_TO_INVADER);
        // Add the HOSTILE_TO_ENEMY capability meaning it can be attacked by other Enemies with certain conditions.
        this.addCapability(EnemyStatus.HOSTILE_TO_ENEMY);

        // Add Attacking and Wandering behaviours to Invader.
        super.getBehaviours().put(0, new AttackBehaviour());
        super.getBehaviours().put(999, new WanderBehaviour());

        // Remove the Invader from the restResettables List.
        ResetManager.getInstance().removeRestResettable(this);
    }

    /**
     * A method to return the invaderClass data attribute.
     * @return the Classes object representing which Class the Invader is.
     */
    public Classes getInvaderClass() {
        return invaderClass;
    }

    /**
     * A method to set the invaderClass data attribute.
     * @param invaderClass: The Classes object representing which Class the Invader is.
     */
    public void setInvaderClass(Classes invaderClass) {
        this.invaderClass = invaderClass;
        // Change the starting HP of the Invader.
        this.resetMaxHp(invaderClass.getStartingHP());
        // Replace the starting Weapon of the Invader.
        this.removeWeaponFromInventory(this.getWeaponInventory().get(0));
        this.addWeaponToInventory(invaderClass.getStartingWeapon());
    }


    /**
     * The Invader can be attacked by any Actor that has the HOSTILE_TO_ENEMY capability except for those with the
     * NOT_HOSTILE_TO_INVADER capability.
     *
     * @param otherActor: The Actor that might be performing attack
     * @param direction: String representing the direction of the other Actor
     * @param map: current GameMap
     * @return the list of allowable actions that can be performed by the other Actor to this Invader.
     */
    @Override
    public ActionList allowableActions(Actor otherActor, String direction, GameMap map) {
        // Create an ActionList containing a list of allowable actions to be performed on this Invader.
        ActionList actions = new ActionList();

        // If the other Actor does not have the capability NOT_HOSTILE_TO_INVADER and has capability HOSTILE_TO_ENEMY,
        // meaning the Actor can attack this Invader.
        // Enemies that are not hostile to Invader are unable to attack the Invader.
        if (!otherActor.hasCapability(EnemyStatus.NOT_HOSTILE_TO_INVADER) && otherActor.hasCapability(EnemyStatus.HOSTILE_TO_ENEMY)) {
            // Call the getAttackTypes utility method which will get the ActionList of all performable Attack types on this Invader.
            actions = Utils.getAttackTypes(this, otherActor, direction);
        }
        return actions;
    }
}
