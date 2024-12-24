package game.actions;

import edu.monash.fit2099.engine.actions.Action;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.positions.Exit;
import edu.monash.fit2099.engine.positions.GameMap;
import edu.monash.fit2099.engine.positions.Location;
import game.skills.Skills;

import java.util.ArrayList;

/**
 * A class that represents an Area Attack which attacks all Actors surrounding the Actor attacking.
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
 */

public class AreaAttackAction extends Action {
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
     * AreaAttackAction Constructor for area attacks.
     *
     * @param target: The Actor to attack
     * @param direction: The direction where the attack should be performed (only used for display purposes)
     * @param skills: The Skill to execute.
     */
    public AreaAttackAction(Actor target, String direction, Skills skills) {
        setTarget(target);			// Set the target of incoming area attack.
        setDirection(direction);	// Set the direction of incoming area attack.
        setSkills(skills);			// Set the skills used in incoming area attack.
    }

    /**
     * A method to return the target data attribute.
     * @return the target the AreaAttackAction is being performed on.
     */
    public Actor getTarget() {
        return target;
    }

    /**
     * A method to return the target data attribute.
     * @return the direction the AreaAttackAction is performed from.
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
     * @param target: The target the AreaAttackAction is being performed on.
     */
    public void setTarget(Actor target) {
        this.target = target;
    }

    /**
     * A method to set the direction data attribute.
     * @param direction: The direction the AreaAttackAction is performed from.
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
     * Performs multiple SpecialAttackAction on all surrounding Actors.
     * Will create a list of Actions containing multiple SpecialAttackActions with the target being adjacent Actors.
     * Loops through said lists and execute each SpecialAttackAction.
     *
     * @param actor The actor performing the Area Attack Action.
     * @param map The map the Actor is on.
     * @return the result of the attack, e.g. whether the target is killed, etc.
     */
    @Override
    public String execute(Actor actor, GameMap map) {
        String result = "";     // The result menu description to display.

        // Create an ArrayList filled with SpecialAttackActions to perform multiple SpecialAttackActions.
        ArrayList<Action> specialAttacks = new ArrayList<>();

        Actor target;   // The target of the Attack.

        // Loop through all possible exits from the Actor's current location and check if an actor exists at the destination.
        for (Exit exit : map.locationOf(actor).getExits()) {
            Location destination = exit.getDestination();    // Get the destination of the said exit.

            // If the destination contains an actor (adjacent actor), set it as the target and create an SpecialAttackAction for it.
            if (destination.containsAnActor()) {
                target = destination.getActor();        // Set the said Actor as the target of the SpecialAttackAction.

                // Create an SpecialAttackAction with the target being the target and use the Weapon's Skills attributes to attack.
                specialAttacks.add(new SpecialAttackAction(target, exit.getName(), getSkills()));
            }
        }

        // Loop through all the specialAttacks and execute each one.
        for (int i = 0; i < specialAttacks.size(); i++) {
            // Get the SpecialAttackAction for the list.
            Action specialAttack = specialAttacks.get(i);

            // If i is the first SpecialAttackAction, execute it and add the string without a line separator.
            // Else, add a line separator for the result string.
            result += (i == 0) ? specialAttack.execute(actor, map) : System.lineSeparator() + specialAttack.execute(actor, map);
        }
        return result;  // Return the menu description.
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
