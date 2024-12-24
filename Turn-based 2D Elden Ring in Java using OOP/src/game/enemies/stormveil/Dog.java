package game.enemies.stormveil;

import game.behaviours.AttackBehaviour;
import game.behaviours.WanderBehaviour;
import game.enemies.EnemyStatus;
import game.enemies.StormveilEnemy;
import game.weapons.DogTeeth;

/**
 * A class that represents the Dog. Inherits from StormveilEnemy.
 * Created by:
 * @author Wah Yang Tan
 * Modified by:
 * @author Po Han Tay, Bryan Wong
 * @version 1.0
 * @see AttackBehaviour
 * @see WanderBehaviour
 * @see EnemyStatus
 * @see StormveilEnemy
 * @see DogTeeth
 */

public class Dog extends StormveilEnemy {
    /**
     * Dog Constructor.
     * Calls parent class to set inherited data attributes.
     * Sets despawning chance to be 10%.
     */
    public Dog() {
        // Set the Enemy and Animal data attributes of the Lone Wolf by calling the parent constructor.
        super("Dog", 'a', 104, 37, 10, 52, 1390, new DogTeeth("DogTeeth", '>', 101,  "bites", 93));

        // Add the HOSTILE_TO_ENEMY capability meaning it can be attacked by other Enemies with certain conditions.
        this.addCapability(EnemyStatus.HOSTILE_TO_ENEMY);

        // Add Attacking and Wandering behaviours to Dog.
        super.getBehaviours().put(0, new AttackBehaviour());
        super.getBehaviours().put(999, new WanderBehaviour());
    }
}
