package game.weapons;

import edu.monash.fit2099.engine.actions.Action;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.weapons.WeaponItem;
import game.actions.AreaAttackAction;
import game.skills.Skills;
import game.skills.SlamAttack;
import game.utils.Status;

/**
 * A class GiantCrayfishPincer that inherits from WeaponItem and is utilised by the GiantCrayfish.
 * Deals 527 damage and has a hit rate of 100%.
 * Has special SlamAttack skill.
 * Created by:
 * @author Po Han Tay
 * Modified by:
 * @author Bryan Wong
 * @version 1.0
 * @see Action
 * @see Actor
 * @see WeaponItem
 * @see AreaAttackAction
 * @see Skills
 * @see SlamAttack
 * @see Status
 */

public class GiantCrayfishPincer extends WeaponItem {
    /**
     * A Skills instance representing the performable Skill the GiantCrayfish can perform.
     */
    private Skills skills = null;

    /**
     * GiantCrayfishPincer Constructor.
     * Will call the WeaponItem parent constructor to set its respective data attributes.
     *
     * @param name: Name of the item.
     * @param displayChar: Character to use for display when item is on the ground.
     * @param damage: Amount of damage this weapon does.
     * @param verb: Verb to use for this weapon, e.g. "hits", "zaps".
     * @param hitRate: The probability/chance to hit the target.
     */
    public GiantCrayfishPincer(String name, char displayChar, int damage, String verb, int hitRate) {
        // Call the WeaponItem parent constructor to set its inherited data attributes.
        super(name, displayChar, damage, verb, hitRate);

        // Set the GiantCrayfishPincer skill to a SlamAttack object, so that it performs a slam attack.
        this.setSkills(new SlamAttack("slams ground", 527, 100, "slams"));

        // Made GiantCrayfishPincer to be not droppable or pickable.
        this.togglePortability();

        // Add a CAN_AREA_ATTACK capability to this weapon to signify it can perform an AreaAttackAction.
        this.addCapability(WeaponStatus.CAN_AREA_ATTACK);
    }

    /**
     * A method to return the skills data attribute.
     * @return the Skills instance representing the performable Skill the Actor can perform.
     */
    public Skills getSkills() {
        return skills;
    }

    /**
     * A method to set the skills data attribute.
     * @param skills: The performable Skill the Actor can perform.
     */
    public void setSkills(Skills skills) {
        this.skills = skills;
    }

    /**
     * Check if GiantCrayfishPincer can perform a Skill.
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
