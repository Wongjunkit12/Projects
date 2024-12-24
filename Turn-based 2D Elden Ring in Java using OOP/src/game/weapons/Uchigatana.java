package game.weapons;

import edu.monash.fit2099.engine.actions.Action;
import edu.monash.fit2099.engine.actors.Actor;
import game.actions.AreaAttackAction;
import game.actions.SpecialAttackAction;
import game.skills.Unsheathe;
import game.utils.Status;

/**
 * A class Uchigatana that inherits from TradeableWeapon and is wielded by the Player. It can be bought and sold by
 * the trader.
 * Deals 115 damage and has a hit rate of 80%.
 * Created by:
 * @author Po Han Tay
 * Modified by:
 *
 * @version 1.0
 * @see Action
 * @see Actor
 * @see SpecialAttackAction
 * @see Unsheathe
 */

public class Uchigatana extends TradeableWeapon {
    /**
     * Uchigatana Constructor.
     * Will call the TradeableWeapon parent constructor to set its respective data attributes.
     */
    public Uchigatana() {
        // Call the TradeableWeapon parent constructor to set its inherited data attributes.
        super("Uchigatana", ')', 115, "slashes", 80, 5000, 500);

        // Set the Uchigatana skill to a Unsheathe object, so that it performs its Skill attack.
        this.setSkills(new Unsheathe("unsheathes Uchigatana", 230, 60, "unsheathes"));
    }

    /**
     * Check if Uchigatana can perform a Skill.
     * @param target target actor
     * @return an SpecialAttackAction that attacks the targeted Actor.
     */
    @Override
    public Action getSkill(Actor target, String direction) {
        // If the wielder is not
        if (!target.hasCapability(Status.DEAD)) {
            return new SpecialAttackAction(target, direction, getSkills());
        }
        return null;
    }
}
