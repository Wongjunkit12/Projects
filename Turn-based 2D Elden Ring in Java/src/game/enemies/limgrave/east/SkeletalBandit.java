package game.enemies.limgrave.east;

import game.behaviours.AttackBehaviour;
import game.behaviours.WanderBehaviour;
import game.enemies.EnemyStatus;
import game.enemies.Undead;
import game.weapons.Scimitar;

/**
 * A class that represents the Skeletal Bandit. Inherits from Undead.
 * Created by:
 * @author Po Han Tay
 * Modified by:
 * @author Bryan Wong
 * @version 1.0
 * @see AttackBehaviour
 * @see WanderBehaviour
 * @see EnemyStatus
 * @see Undead
 * @see Scimitar
 */

public class SkeletalBandit extends Undead {
    /**
     * SkeletalBandit Constructor.
     * Calls parent class to set inherited data attributes.
     * Sets despawning chance to be 10%.
     */
    public SkeletalBandit() {
        // Set the Enemy and Undead data attributes of the Skeletal Bandit by calling the parent constructor.
        super("Skeletal Bandit", 'b', 184, 27,10, 0, 0, new Scimitar());

        // Add the HOSTILE_TO_ENEMY capability meaning it can be attacked by other Enemies with certain conditions.
        this.addCapability(EnemyStatus.HOSTILE_TO_ENEMY);
        // Add a capability signifying that Skeletal Bandit is resurrectable.
        this.addCapability(EnemyStatus.RESURRECTABLE);

        // Add Attacking and Wandering behaviours to Skeletal Bandit.
        super.getBehaviours().put(0, new AttackBehaviour());
        super.getBehaviours().put(999, new WanderBehaviour());

        // Toggle the portable boolean for Scimitar as they cannot drop it until the Pile of Bones is destroyed.
        this.getWeaponInventory().get(0).togglePortability();
    }
}
