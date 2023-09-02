package game.weapons;

import edu.monash.fit2099.engine.actions.Action;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.weapons.WeaponItem;

/**
 * A class DogTeeth that inherits from WeaponItem and is utilised by the Dog.
 * Deals 101 damage and has a hit rate of 93%.
 * Created by:
 * @author Po Han Tay
 * Modified by:
 *
 * @version 1.0
 * @see Action
 * @see Actor
 * @see WeaponItem
 */

public class DogTeeth extends WeaponItem {
    /**
     * DogTeeth Constructor.
     * Will call the WeaponItem parent constructor to set its respective data attributes.
     *
     * @param name: Name of the item.
     * @param displayChar: Character to use for display when item is on the ground.
     * @param damage: Amount of damage this weapon does.
     * @param verb: Verb to use for this weapon, e.g. "hits", "zaps".
     * @param hitRate: The probability/chance to hit the target.
     */
    public DogTeeth(String name, char displayChar, int damage, String verb, int hitRate) {
        // Call the WeaponItem parent constructor to set its inherited data attributes.
        super(name, displayChar, damage, verb, hitRate);

        // Made DogTeeth to be not droppable or pickable.
        this.togglePortability();
    }

    /**
     * Check if DogTeeth can perform a Skill.
     * @param target target actor
     * @return null as no Skill can be performed.
     */
    @Override
    public Action getSkill(Actor target, String direction) {
        return null;
    }
}
