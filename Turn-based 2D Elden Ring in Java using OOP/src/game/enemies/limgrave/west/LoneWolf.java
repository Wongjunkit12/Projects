package game.enemies.limgrave.west;

import game.behaviours.AttackBehaviour;
import game.behaviours.WanderBehaviour;
import game.enemies.EnemyStatus;
import game.enemies.Animal;
import game.weapons.LoneWolfTeeth;

/**
 * A class that represents the Lone Wolf. Inherits from Animal.
 * BEHOLD, DOG!
 * Created by:
 * @author Adrian Kristanto
 * Modified by:
 * @author Po Han Tay, Bryan Wong
 * @version 1.0
 * @see AttackBehaviour
 * @see WanderBehaviour
 * @see EnemyStatus
 * @see Animal
 * @see LoneWolfTeeth
 */

public class LoneWolf extends Animal {
    /**
     * LoneWolf Constructor.
     * Calls parent class to set inherited data attributes.
     * Sets despawning chance to be 10%.
     */
    public LoneWolf() {
        // Set the Enemy and Animal data attributes of the Lone Wolf by calling the parent constructor.
        super("Lone Wolf", 'h', 102, 33, 10, 55, 1470, new LoneWolfTeeth("WolfTeeth", '^', 97, "bites", 95));

        // Add the HOSTILE_TO_ENEMY capability meaning it can be attacked by other Enemies with certain conditions.
        this.addCapability(EnemyStatus.HOSTILE_TO_ENEMY);

        // Add Attacking and Wandering behaviours to Lone Wolf.
        super.getBehaviours().put(0, new AttackBehaviour());
        super.getBehaviours().put(999, new WanderBehaviour());
    }
}
