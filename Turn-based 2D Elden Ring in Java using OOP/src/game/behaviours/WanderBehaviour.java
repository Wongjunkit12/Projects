package game.behaviours;

import java.util.ArrayList;

import edu.monash.fit2099.engine.actions.Action;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.positions.Exit;
import edu.monash.fit2099.engine.positions.GameMap;
import edu.monash.fit2099.engine.positions.Location;
import game.utils.RandomNumberGenerator;

/**
 * Class WanderBehaviour that implements Behaviour interface. Will allow the Actor to wander around aimlessly.
 * Created by:
 * @author Riordan D. Alfredo
 * Modified by:
 * @author Bryan Wong
 * @version 1.0
 * @see Action
 * @see Actor
 * @see Exit
 * @see GameMap
 * @see Location
 * @see RandomNumberGenerator
 */

public class WanderBehaviour implements Behaviour {
	/**
	 * Returns a MoveAction to wander to a random location, if possible.
	 * If no movement is possible, returns null.
	 *
	 * @param actor: The Actor enacting the behaviour
	 * @param map: The map that Actor is currently on
	 * @return a MoveAction, or null if no MoveAction is possible
	 */
	@Override
	public Action getAction(Actor actor, GameMap map) {
		// Create an ArrayList filled with Actions to perform certain actions
		ArrayList<Action> actions = new ArrayList<>();

		// Loop through all possible exits from the Actor's current location and add MoveActions to there in the ArrayList.
		for (Exit exit : map.locationOf(actor).getExits()) {
            Location destination = exit.getDestination();    // Get the destination of the said exit.

			// If there are no actors at destination and if the Actor can enter the destination,
			// add a MoveAction to there to the ArrayList.
            if (!destination.containsAnActor() && destination.canActorEnter(actor)) {
				actions.add(destination.getMoveAction(actor, "around", exit.getHotKey()));
            }
        }

		// If at least one possible location to move to, randomly choose one.
		if (!actions.isEmpty()) {
			return actions.get(RandomNumberGenerator.getRandomInt(actions.size() - 1));
		}
		// Else if no possible locations to move to, return null.
		else {
			return null;
		}

	}
}
