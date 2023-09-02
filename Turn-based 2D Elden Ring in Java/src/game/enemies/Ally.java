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
 * A class that represents the Ally. Inherits from Enemy.
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

public class Ally extends Enemy {
    /**
     * The class of the Ally.
     */
    private Classes allyClass;

    /**
     * Ally Constructor.
     * Calls parent class to set inherited data attributes.
     * Sets hitpoints to 300 and Weapon to dummy Club initially until Class is chosen.
     * Sets spawningChance to be 100% as they can guarantee spawn provided the Player summons them and despawningChance
     * to 0 as they cannot despawn naturally.
     * @param allyClass: The Class type the Ally has.
     */
    public Ally(Classes allyClass) {
        // Set the Enemy data attributes of the Ally by calling the parent constructor.
        super("Ally", 'A', 300, 100, 0, "Summonee", 0, 0, new Club());

        // Set the Ally Class type, along with its health and Weapon.
        this.setAllyClass(allyClass);

        // Add capability to indicate they can be summoned.
        this.addCapability(EnemyStatus.SUMMONABLE);
        // Add capability to indicate they cannot be attacked nor attack by other Allies.
        this.addCapability(EnemyStatus.NOT_HOSTILE_TO_ALLY);
        // Add the HOSTILE_TO_ENEMY capability meaning it can be attacked by other Enemies with certain conditions.
        this.addCapability(EnemyStatus.HOSTILE_TO_ENEMY);
        // Remove the HOSTILE_TO_PLAYER capability as Ally cannot attack Player.
        this.removeCapability(EnemyStatus.HOSTILE_TO_PLAYER);


        // Add Attacking and Wandering behaviours to
        super.getBehaviours().put(0, new AttackBehaviour());
        super.getBehaviours().put(999, new WanderBehaviour());

        // Remove the Ally from the restResettables List.
        ResetManager.getInstance().removeRestResettable(this);
    }

    /**
     * A method to return the allyClass data attribute.
     * @return the Classes object representing which Class the Ally is.
     */
    public Classes getAllyClass() {
        return allyClass;
    }

    /**
     * A method to set the allyClass data attribute.
     * @param allyClass: The Classes object representing which Class the Ally is.
     */
    public void setAllyClass(Classes allyClass) {
        this.allyClass = allyClass;
        // Change the starting HP of the Ally.
        this.resetMaxHp(allyClass.getStartingHP());
        // Replace the starting Weapon of the Ally.
        this.removeWeaponFromInventory(this.getWeaponInventory().get(0));
        this.addWeaponToInventory(allyClass.getStartingWeapon());
    }

    /**
     * The Allu can be attacked by any Actor that has the HOSTILE_TO_ENEMY capability except for those with the
     * NOT_HOSTILE_TO_ALLY capability.
     *
     * @param otherActor: The Actor that might be performing attack
     * @param direction: String representing the direction of the other Actor
     * @param map: current GameMap
     * @return the list of allowable actions that can be performed by the other Actor to this Ally.
     */
    @Override
    public ActionList allowableActions(Actor otherActor, String direction, GameMap map) {
        // Create an ActionList containing a list of allowable actions to be performed on this Ally.
        ActionList actions = new ActionList();

        // If the other Actor does not have the capability NOT_HOSTILE_TO_ALLY and has capability HOSTILE_TO_ENEMY,
        // meaning the Actor can attack this Ally.
        // Enemies that are not hostile to Ally are unable to attack the Ally.
        if (!otherActor.hasCapability(EnemyStatus.NOT_HOSTILE_TO_ALLY) && otherActor.hasCapability(EnemyStatus.HOSTILE_TO_ENEMY)) {
            // Call the getAttackTypes utility method which will get the ActionList of all performable Attack types on this Ally.
            actions = Utils.getAttackTypes(this, otherActor, direction);
        }
        return actions;
    }
}
