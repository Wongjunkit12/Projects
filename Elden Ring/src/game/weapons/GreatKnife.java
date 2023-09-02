package game.weapons;

import edu.monash.fit2099.engine.actions.Action;
import edu.monash.fit2099.engine.actors.Actor;
import game.actions.QuickStepAction;
import game.skills.Quickstep;
import game.utils.Status;

/**
 * A class GreatKnife that inherits from TradeableWeapon and is wielded by the Player. It can be bought and sold by
 * the trader.
 * Deals 75 damage and has a hit rate of 70%.
 * Created by:
 * @author Po Han Tay
 * Modified by:
 *
 * @version 1.0
 * @see Action
 * @see Actor
 * @see QuickStepAction
 * @see Quickstep
 * @see Status
 */

public class GreatKnife extends TradeableWeapon {
    /**
     * GreatKnife Constructor.
     * Will call the TradeableWeapon parent constructor to set its respective data attributes.
     */
    public GreatKnife() {
        // Call the TradeableWeapon parent constructor to set its inherited data attributes.
        super("GreatKnife", '/', 75, "stabs", 70, 3500, 350);

        // Set the GreatKnife skill to a Quickstep object, so that it performs its Skill attack.
        this.setSkills(new Quickstep("performs Quickstep", 75, 70, "quicksteps"));

        // Adds a CAN_EVADE capability to indicate this weapon can evade/move after executing a skill/attack.
        this.addCapability(WeaponStatus.CAN_EVADE);
    }

    /**
     * Check if GreatKnife can perform a Skill.
     * @param target target actor
     * @return an QuickStepAction that attacks all adjacent Actors. Null if no available Skill.
     */
    @Override
    public Action getSkill(Actor target, String direction) {
        // If Weapon can perform an QuickStep Attack, return a QuickStepAction.
        if (this.hasCapability(WeaponStatus.CAN_EVADE) && !target.hasCapability(Status.DEAD)) {
            return new QuickStepAction(target, direction, getSkills());
        }
        return null;
    }
}
