package game.enemies.limgrave.east;

import game.behaviours.AttackBehaviour;
import game.behaviours.WanderBehaviour;
import game.enemies.EnemyStatus;
import game.enemies.Marine;
import game.weapons.GiantCrayfishPincer;

/**
 * A class that represents the Giant Crayfish. Inherits from Marine.
 * Created by:
 * @author Po Han Tay
 * Modified by:
 * @author Bryan Wong
 * @version 1.0
 * @see AttackBehaviour
 * @see WanderBehaviour
 * @see EnemyStatus
 * @see Marine
 * @see GiantCrayfishPincer
 */

public class GiantCrayfish extends Marine {
    /**
     * GiantCrayfish Constructor.
     * Calls parent class to set inherited data attributes.
     * Sets despawning chance to be 10%.
     */
    public GiantCrayfish() {
        // Set the Enemy and Marine data attributes of the Giant Crayfish by calling the parent constructor.
        super("Giant Crayfish", 'R', 4803, 1, 10, 500, 2374, new GiantCrayfishPincer("CrayfishPincer", '>', 527, "pincers", 100));

        // Add the HOSTILE_TO_ENEMY capability meaning it can be attacked by other Enemies with certain conditions.
        this.addCapability(EnemyStatus.HOSTILE_TO_ENEMY);
        // Add the CAN_AREA_ATTACK capability meaning it can perform an area attack.
        this.addCapability(EnemyStatus.CAN_AREA_ATTACK);

        // Add Attacking and Wandering behaviours to Giant Crayfish.
        super.getBehaviours().put(0, new AttackBehaviour());
        super.getBehaviours().put(999, new WanderBehaviour());
    }
}
