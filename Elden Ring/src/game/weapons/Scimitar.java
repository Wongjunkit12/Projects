package game.weapons;

import edu.monash.fit2099.engine.actions.Action;
import edu.monash.fit2099.engine.actors.Actor;
import game.actions.AreaAttackAction;
import game.skills.SpinAttack;
import game.utils.Status;

/**
 * A class Scimitar that inherits from TradeableWeapon and is wielded by the Player. It can be bought and sold by
 * the trader.
 * Deals 118 damage and has a hit rate of 88%.
 * Has special SpinAttack skill.
 * Created by:
 * @author Po Han Tay
 * Modified by:
 * @author Bryan Wong
 * @version 1.0
 * @see Action
 * @see Actor
 * @see AreaAttackAction
 * @see SpinAttack
 * @see Status
 */

public class Scimitar extends TradeableWeapon {
    /**
     * Scimitar Constructor.
     * Will call the TradeableWeapon parent constructor to set its respective data attributes.
     */
    public Scimitar() {
        // Call the TradeableWeapon parent constructor to set its inherited data attributes.
        super("Scimitar", 's', 118, "cuts", 88, 600, 100);

        // Set the Grossmesser skill to a SpinAttack object, so that it performs a spin attack.
        this.setSkills(new SpinAttack("spins Scimitar", 118, 88, "spins"));

        // Add a CAN_AREA_ATTACK capability to this weapon to signify it can perform an AreaAttackAction.
        this.addCapability(WeaponStatus.CAN_AREA_ATTACK);
    }

    /**
     * Check if Scimitar can perform a Skill.
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
