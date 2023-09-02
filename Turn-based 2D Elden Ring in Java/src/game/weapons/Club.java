package game.weapons;

import edu.monash.fit2099.engine.actions.Action;
import edu.monash.fit2099.engine.actors.Actor;

/**
 * A class Club that inherits from TradeableWeapon.
 * Deals 103 damage and has a hit rate of 80%.
 * Created by:
 * @author Adrian Kristanto
 * Modified by:
 * @author Bryan Wong, Po Han Tay
 * @version 1.0
 * @see Action
 * @see Actor
 */

public class Club extends TradeableWeapon {
    /**
     * Club Constructor.
     * Will call the TradeableWeapon parent constructor to set its respective data attributes.
     */
    public Club() {
        // Call the TradeableWeapon parent constructor to set its inherited data attributes.
        super("Club", '!', 103, "bonks", 80, 600, 100);
    }

    /**
     * Check if Club can perform a Skill.
     * @param target target actor
     * @return null as no Skill can be performed.
     */
    @Override
    public Action getSkill(Actor target, String direction) {
        return null;
    }
}
