package game.enemies.limgrave.west;

import game.behaviours.AttackBehaviour;
import game.behaviours.WanderBehaviour;
import game.enemies.EnemyStatus;
import game.enemies.Undead;
import game.weapons.Grossmesser;

/**
 * A class that represents the Heavy Skeletal Swordsman. Inherits from Undead.
 * Created by:
 * @author Bryan Wong
 * Modified by:
 *
 * @version 1.0
 * @see AttackBehaviour
 * @see WanderBehaviour
 * @see EnemyStatus
 * @see Undead
 * @see Grossmesser
 */

public class HeavySkeletalSwordsman extends Undead {
    /**
     * HeavySkeletalSwordsman Constructor.
     * Calls parent class to set inherited data attributes.
     * Sets despawning chance to be 10%.
     */
    public HeavySkeletalSwordsman() {
        // Set the Enemy and Undead data attributes of the Heavy Skeletal Swordsman by calling the parent constructor.
        super("Heavy Skeletal Swordsman", 'q', 153, 27, 10, 0, 0, new Grossmesser());

        // Add the HOSTILE_TO_ENEMY capability meaning it can be attacked by other Enemies with certain conditions.
        this.addCapability(EnemyStatus.HOSTILE_TO_ENEMY);
        // Add a capability signifying that Heavy Skeletal Swordsman is resurrectable.
        this.addCapability(EnemyStatus.RESURRECTABLE);

        // Add Attacking and Wandering behaviours to Heavy Skeletal Swordsman.
        super.getBehaviours().put(0, new AttackBehaviour());
        super.getBehaviours().put(999, new WanderBehaviour());

        // Toggle the portable boolean for Grossmesser as they cannot drop it until the Pile of Bones is destroyed.
        this.getWeaponInventory().get(0).togglePortability();
    }
}
