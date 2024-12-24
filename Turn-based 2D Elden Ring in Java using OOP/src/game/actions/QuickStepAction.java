package game.actions;

import edu.monash.fit2099.engine.actions.Action;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.positions.Exit;
import edu.monash.fit2099.engine.positions.GameMap;
import edu.monash.fit2099.engine.positions.Location;
import game.skills.Skills;
import game.utils.RandomNumberGenerator;

import java.util.ArrayList;

/**
 * A class that represents a Quick Step Action.
 * Created by:
 * @author Bryan Wong
 * Modified by:
 * @author Po Han Tay
 * @version 1.0
 * @see Action
 * @see Actor
 * @see Exit
 * @see GameMap
 * @see Location
 * @see Skills
 * @see RandomNumberGenerator
 */

public class QuickStepAction extends Action {
    /**
     * The Actor that is to be attacked.
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
     * QuickStepAction Constructor for Quick Step Attack.
     *
     * @param target: The Actor to attack
     * @param direction: The direction where the attack should be performed (only used for display purposes)
     * @param skills: The skill to execute.
     */
    public QuickStepAction(Actor target, String direction, Skills skills) {
        setTarget(target);          // Set the target of the Quick Step Action.
        setDirection(direction);    // Set the direction of incoming Quick Step attack.
        setSkills(skills);          // Set the skills used in incoming Quick Step attack.
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
     * Will perform an QuickStepAction on the target based on the Skill of the Actor.
     * When executed, call SpecialAttackAction to execute the Skill attack, then randomly pick a Location to move to.
     *
     * @param actor The Actor performing the Special Attack Action.
     * @param map The map the Actor is on.
     * @return the result of the attack, e.g. whether the target is killed, etc.
     */
    @Override
    public String execute(Actor actor, GameMap map) {
        String result = "";

        // Call the SpecialAttackAction to execute the Special Skill Attack.
        SpecialAttackAction specialAttackAction = new SpecialAttackAction(getTarget(), getDirection(), getSkills());
        result += specialAttackAction.execute(actor, map);  // Add the result of the SpecialAttackAction to the results string.

        // Find all possible locations for the Player to move to and randomly moves the Player to one of them.
        // Create an ArrayList filled with MoveActions to perform certain MoveActions after executing the Skill.
        ArrayList<Action> moveActions = new ArrayList<>();

        // Loop through all possible exits from the Actor's current location and add MoveActions to there in the ArrayList.
        for (Exit exit : map.locationOf(actor).getExits()) {
            Location destination = exit.getDestination();    // Get the destination of the said exit.

            // If there are no actors at destination and if the Actor can enter the destination,
            // add a MoveAction to there to the ArrayList.
            if (!destination.containsAnActor() && destination.canActorEnter(actor)) {
                moveActions.add(destination.getMoveAction(actor, exit.getName(), exit.getHotKey()));
            }
        }

        // If at least one possible location to move to, randomly choose one.
        if (!moveActions.isEmpty()) {
            // Execute that MoveAction to move to that Location.
            Action moveAction = moveActions.get(RandomNumberGenerator.getRandomInt(moveActions.size() - 1));
            result += System.lineSeparator() + moveAction.execute(actor, map);
        }
        // Else if no possible locations to move to, don't move and print out message.
        else {
            result += System.lineSeparator() + String.format("%s couldn't evade as no possible locations to move to.", actor);
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
