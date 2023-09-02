package game.behaviours;

import edu.monash.fit2099.engine.actions.Action;
import edu.monash.fit2099.engine.actions.ActionList;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.positions.Exit;
import edu.monash.fit2099.engine.positions.GameMap;
import edu.monash.fit2099.engine.positions.Location;
import game.utils.RandomNumberGenerator;

import java.util.ArrayList;

/**
 * A behaviour that determines what type of Attack Action should be performed and on who by the Actor.
 * Created by:
 * @author Riordan D. Alfredo
 * Modified by:
 * @author Bryan Wong, Po Han Tay
 * @version 1.0
 * @see Action
 * @see ActionList
 * @see Actor
 * @see Exit
 * @see GameMap
 * @see Location
 * @see RandomNumberGenerator
 */

public class AttackBehaviour implements Behaviour {
    /**
     * Get list of allowable Attack Actions types for every adjacent Actor that can be attacked. Randomly chooses one action
     * to return.
     * If no attack is possible, return null.
     * @param actor: The Actor enacting the behaviour
     * @param map: The map that Actor is currently on
     * @return an AttackAction or a SpecialAttackAction, or null if none is possible
     */
    @Override
    public Action getAction(Actor actor, GameMap map) {
        // Create an ArrayList filled with Actions to perform certain actions
        ArrayList<Action> actions = new ArrayList<>();
        // Set the ActionList of allowable Actions that can be performed on the target by this attacking Actor.
        ActionList allowableActions;

        Actor target;   // The target of the Attack.

        // Loop through all possible exits from the Actor's current location and check if an actor exists at the destination.
        for (Exit exit : map.locationOf(actor).getExits()) {
            Location destination = exit.getDestination();    // Get the destination of the said exit.

            // If the destination contains an actor (adjacent actor), set it as the target and get the allowable actions for it.
            if (destination.containsAnActor()) {
                target = destination.getActor();        // Set the said Actor as the target of the Attack.

                // Get all the performable actions that can be done from the attacking Actor to the target Actor.
                allowableActions = target.allowableActions(actor, exit.getName(), map);

                // If a performable attack can be done, add it to the actions ArrayList.
                // Randomly chooses SpecialAttackAction/AreaAttackAction or AttackAction with a 50% chance.
                if (allowableActions.size() > 0) {
                    // Randomly choose either normal AttackAction or SpecialAttackAction/AreaAttackAction.
                    // Enemy will only have a maximum of 2 performable actions - AttackAction or SpecialAttackAction/AreaAttackAction.
                    // As maximum AttackAction and SpecialAttackAction only, randomly choosing either is 50% chance.
                    int randomChoice = RandomNumberGenerator.getRandomInt(allowableActions.size() - 1);
                    // Add the randomly chosen attack into the actions ArrayList.
                    actions.add(allowableActions.get(randomChoice));
                }
            }
        }
        // If at least one possible Actor to attack, randomly choose one one to attack and return the Action.
        if (!actions.isEmpty()) {
            return actions.get(RandomNumberGenerator.getRandomInt(actions.size() - 1));
        }

        return null;    // Else return null if no attack possible.
    }
}
