package game.enemies.limgrave.west;

import game.behaviours.AttackBehaviour;
import game.behaviours.WanderBehaviour;
import game.enemies.EnemyStatus;
import game.enemies.Marine;
import game.weapons.GiantCrabPincer;

/**
 * A class that represents the Giant Crab. Inherits from Marine.
 * Created by:
 * @author Wah Yang Tan
 * Modified by:
 * @author Po Han Tay, Bryan Wong
 * @version 1.0
 * @see AttackBehaviour
 * @see WanderBehaviour
 * @see EnemyStatus
 * @see Marine
 * @see GiantCrabPincer
 */

public class GiantCrab extends Marine {
    /**
     * GiantCrab Constructor.
     * Calls parent class to set inherited data attributes.
     * Sets despawning chance to be 10%.
     */
    public GiantCrab() {
        // Set the Enemy and Marine data attributes of the Giant Crab by calling the parent constructor.
        super("Giant Crab", 'C', 407, 2, 10, 318, 4961, new GiantCrabPincer("CrabPincer", '<', 208, "pincers", 90));

        // Add the HOSTILE_TO_ENEMY capability meaning it can be attacked by other Enemies with certain conditions.
        this.addCapability(EnemyStatus.HOSTILE_TO_ENEMY);
        // Add the CAN_AREA_ATTACK capability meaning it can perform an area attack.
        this.addCapability(EnemyStatus.CAN_AREA_ATTACK);

        // Add Attacking and Wandering behaviours to Giant Crab.
        super.getBehaviours().put(0, new AttackBehaviour());
        super.getBehaviours().put(999, new WanderBehaviour());
    }
}
