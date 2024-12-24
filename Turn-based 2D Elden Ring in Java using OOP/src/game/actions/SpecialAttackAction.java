package game.actions;

import edu.monash.fit2099.engine.actions.Action;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.positions.GameMap;
import game.skills.Skills;
import game.utils.RandomNumberGenerator;
import game.utils.Status;


/**
 * A class that represents a Special Attack Action.
 * Created by:
 * @author Bryan Wong
 * Modified by:
 * @author Po Han Tay
 * @version 1.0
 * @see Action
 * @see Actor
 * @see GameMap
 * @see Skills
 * @see RandomNumberGenerator
 */

public class SpecialAttackAction extends Action {
    /**
     * The Actor that is to be attacked
     */
    private Actor target;
    /**
     * The direction of incoming attack.
     */
    private String direction;
    /**
     * Skills used for the attack
     */
    private Skills skills;

    /**
     * SpecialAttackAction Constructor for skill attacks.
     *
     * @param target: The Actor to attack
     * @param direction: The direction where the attack should be performed (only used for display purposes)
     * @param skills: The skill to execute.
     */
    public SpecialAttackAction(Actor target, String direction, Skills skills) {
        setTarget(target);              // Set the target of incoming special attack.
        setDirection(direction);        // Set the direction of incoming special attack.
        setSkills(skills);              // Set the skills used in incoming special attack.
    }

    /**
     * A method to return the target data attribute.
     * @return the target the AttackAction is being performed on.
     */
    public Actor getTarget() {
        return target;
    }

    /**
     * A method to return the target data attribute.
     * @return the direction the AttackAction is performed from.
     */
    public String getDirection() {
        return direction;
    }

    /**
     * A method to return the skills data attribute.
     * @return the Skills instance representing the Skills used for the attack.
     */
    public Skills getSkills() {
        return skills;
    }

    /**
     * A method to set the target data attribute.
     * @param target: The target the AttackAction is being performed on.
     */
    public void setTarget(Actor target) {
        this.target = target;
    }

    /**
     * A method to set the direction data attribute.
     * @param direction: The direction the AttackAction is performed from.
     */
    public void setDirection(String direction) {
        this.direction = direction;
    }

    /**
     * A method to set the skills data attribute.
     * @param skills: The Skills used for the attack.
     */
    public void setSkills(Skills skills) {
        this.skills = skills;
    }

    /**
     * Will perform an SpecialAttackAction on the target based on the Skill of the Actor.
     * When executed, the accuracy of the Skill of the Actor is computed to determine whether
     * the Actor will hit the target. If so, deal damage to the target and determine whether the target is killed.
     *
     * @param actor The Actor performing the Special Attack Action.
     * @param map The map the Actor is on.
     * @return the result of the attack, e.g. whether the target is killed, etc.
     */
    @Override
    public String execute(Actor actor, GameMap map) {
        String result = "";     // The result of the attack. Whether it misses o hits and whether it kills.

        // Generate a random number from 1 to 100 inclusive and compare it with skills' accuracy.
        // If the generated random number is not less than or equal to the chanceToHit (misses), then attack does no damage.
        if (!(RandomNumberGenerator.getRandomInt(1, 100) <= getSkills().getAccuracy())) {
            // Return the missed attack result.
            result += String.format("%s %s but misses %s", actor, getSkills().getName(), getTarget());
        }

        // Else attack hits so subtract target health.
        else {
            int damage = getSkills().getDamage();    // Get weapon damage, either weapon in inventory or IntrinsicWeapon.
            // Successful hit between Actor and target.
            result += String.format("%s %s and hits %s for %d damage", actor, getSkills().getName(), getTarget(), damage);
            getTarget().hurt(damage);    // Reduce target health based on weapon's damage.

            // If target has died (health < 0), then create a new DeathAction with the target and execute it, appending
            // the DeathAction result to the result String.
            if (!getTarget().isConscious() && !getTarget().hasCapability(Status.DEAD)) {
                // Execute the DeathAction and get the Death message,
                result += new DeathAction(actor).execute(getTarget(), map);
            }
        }
        return result;
    }

    /**
     * Describes which target the actor is attacking with which Skill.
     *
     * @param actor: The actor performing the action.
     * @return a description used for the menu UI.
     */
    @Override
    public String menuDescription(Actor actor) {
        return String.format("%s %s on %s at %s", actor, getSkills().getName(), getTarget(), getDirection());
    }
}
