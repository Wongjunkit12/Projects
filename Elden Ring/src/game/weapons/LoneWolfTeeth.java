package game.weapons;

import edu.monash.fit2099.engine.actions.Action;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.weapons.WeaponItem;

/**
 * A class LoneWolfTeeth that inherits from WeaponItem and is utilised by the LoneWolf.
 * Deals 97 damage and has a hit rate of 95%.
 * Created by:
 * @author Po Han Tay
 * Modified by:
 * @author Bryan Wong
 * @version 1.0
 * @see Action
 * @see Actor
 * @see WeaponItem
 */

public class LoneWolfTeeth extends WeaponItem {
    /**
     * LoneWolfTeeth Constructor.
     * Will call the WeaponItem parent constructor to set its respective data attributes.
     *
     * @param name: Name of the item.
     * @param displayChar: Character to use for display when item is on the ground.
     * @param damage: Amount of damage this weapon does.
     * @param verb: Verb to use for this weapon, e.g. "hits", "zaps".
     * @param hitRate: The probability/chance to hit the target.
     */
    public LoneWolfTeeth(String name, char displayChar, int damage, String verb, int hitRate) {
        // Call the WeaponItem parent constructor to set its inherited data attributes.
        super(name, displayChar, damage, verb, hitRate);

        // Made LoneWolfTeeth to be not droppable or pickable.
        this.togglePortability();
    }

    /**
     * Check if LoneWolfTeeth can perform a Skill.
     * @param target target actor
     * @return null as no Skill can be performed.
     */
    @Override
    public Action getSkill(Actor target, String direction) {
        return null;
    }
}
