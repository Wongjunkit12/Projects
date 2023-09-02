package game.enemies.stormveil;

import game.behaviours.AttackBehaviour;
import game.behaviours.WanderBehaviour;
import game.enemies.EnemyStatus;
import game.enemies.StormveilEnemy;
import game.weapons.Club;

/**
 * A class that represents the GodrickSoldier. Inherits from StormveilEnemy.
 * Created by:
 * @author Wah Yang Tan
 * Modified by:
 * @author Po Han Tay, Bryan Wong
 * @version 1.0
 * @see AttackBehaviour
 * @see WanderBehaviour
 * @see EnemyStatus
 * @see StormveilEnemy
 * @see Club
 */

public class GodrickSoldier extends StormveilEnemy {
    /**
     * GodrickSoldier Constructor.
     * Calls parent class to set inherited data attributes.
     * Sets despawning chance to be 10%.
     */
    public GodrickSoldier() {
        // Set the Enemy and StormveilEnemy data attributes of the Godrick Soldier by calling the parent constructor.
        // Since Heavy Crossbow has become optional, therefore use Club as initial weapon
        super("Godrick Soldier", 'p', 198, 45, 10, 38, 70, new Club());

        // Add the HOSTILE_TO_ENEMY capability meaning it can be attacked by other Enemies with certain conditions.
        this.addCapability(EnemyStatus.HOSTILE_TO_ENEMY);

        // Add Attacking and Wandering behaviours to Godrick Soldier.
        super.getBehaviours().put(0, new AttackBehaviour());
        super.getBehaviours().put(999, new WanderBehaviour());
    }
}
