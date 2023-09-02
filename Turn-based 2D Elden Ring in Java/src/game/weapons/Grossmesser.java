package game.weapons;

import edu.monash.fit2099.engine.actions.Action;
import edu.monash.fit2099.engine.actors.Actor;
import game.actions.AreaAttackAction;
import game.skills.SpinAttack;
import game.utils.Status;

/**
 * A class Grossmesser that inherits from TradeableWeapon and is wielded by the Heavy Skeletal Swordsman.
 * Deals 115 damage and has a hit rate of 85%.
 * Has special SpinAttack skill.
 * Created by:
 * @author Bryan Wong
 * Modified by:
 * @author Po Han Tay
 * @version 1.0
 * @see Action
 * @see Actor
 * @see AreaAttackAction
 * @see SpinAttack
 * @see Status
 */

public class Grossmesser extends TradeableWeapon {
    /**
     * Grossmesser Constructor.
     * Will call the TradeableWeapon parent constructor to set its respective data attributes.
     */
    public Grossmesser() {
        // Call the TradeableWeapon parent constructor to set its inherited data attributes.
        super("Grossmesser", '?', 115, "attacks", 85, 0, 100);

        // Set the Grossmesser skill to a SpinAttack object, so that it performs a spin attack.
        this.setSkills(new SpinAttack("spins Grossmesser", 115, 85, "spins"));

        // Add a CAN_AREA_ATTACK capability to this weapon to signify it can perform an AreaAttackAction.
        this.addCapability(WeaponStatus.CAN_AREA_ATTACK);
    }

    /**
     * Check if Grossmesser can perform a skill.
     * @param target target actor
     * @return an AreaAttackAction that attacks all adjacent Actors. Null if no available Skill.
     */
    @Override
    public Action getSkill(Actor target, String direction) {
        // If Weapon can perform an Area Attack, return an AreaAttackAction.
        if (this.hasCapability(WeaponStatus.CAN_AREA_ATTACK) && !target.hasCapability(Status.DEAD)) {
            return new AreaAttackAction(target, direction, getSkills());
        }
        return null;
    }
}
