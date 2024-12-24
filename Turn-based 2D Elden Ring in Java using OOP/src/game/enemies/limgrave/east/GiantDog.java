package game.enemies.limgrave.east;

import game.behaviours.AttackBehaviour;
import game.behaviours.WanderBehaviour;
import game.enemies.Animal;
import game.enemies.EnemyStatus;
import game.weapons.GiantDogHead;

/**
 * A class that represents the Giant Dog. Inherits from Animal.
 * BEHOLD, DOG!
 * Created by:
 * @author Po Han Tay
 * Modified by:
 * @author Bryan Wong
 * @version 1.0
 * @see AttackBehaviour
 * @see WanderBehaviour
 * @see Animal
 * @see EnemyStatus
 * @see GiantDogHead
 */

public class GiantDog extends Animal {
    /**
     * GiantDog Constructor.
     * Calls parent class to set inherited data attributes.
     * Sets despawning chance to be 10%.
     */
    public GiantDog() {
        // Set the Enemy and Animal data attributes of the Giant Dog by calling the parent constructor.
        super("Giant Dog", 'G', 693, 4, 10, 313, 1808, new GiantDogHead("DogHead", 'D', 314,  "bites", 90));

        // Add the HOSTILE_TO_ENEMY capability meaning it can be attacked by other Enemies with certain conditions.
        this.addCapability(EnemyStatus.HOSTILE_TO_ENEMY);
        // Add the CAN_AREA_ATTACK capability meaning it can perform an area attack.
        this.addCapability(EnemyStatus.CAN_AREA_ATTACK);

        // Add Attacking and Wandering behaviours to Giant Dog.
        super.getBehaviours().put(0, new AttackBehaviour());
        super.getBehaviours().put(999, new WanderBehaviour());
    }
}
