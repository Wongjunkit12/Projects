package game.behaviours;

import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.actions.Action;
import edu.monash.fit2099.engine.positions.Exit;
import edu.monash.fit2099.engine.positions.GameMap;
import edu.monash.fit2099.engine.positions.Location;
import edu.monash.fit2099.engine.actions.MoveActorAction;

/**
 * A class that figures out a MoveAction that will move the actor one step closer to a target Actor.
 * Created by:
 * @author Riordan D. Alfredo
 * Modified by:
 * @author Bryan Wong, Po Han Tay
 * @version 1.0
 * @see Actor
 * @see Action
 * @see Exit
 * @see GameMap
 * @see Location
 * @see MoveActorAction
 */

public class FollowBehaviour implements Behaviour {
	/**
	 * The Actor target to follow.
	 */
	private final Actor TARGET;

	/**
	 * FollowBehaviour Constructor.
	 * @param TARGET: The TARGET Actor to be followed.
	 */
	public FollowBehaviour(Actor TARGET) {
		this.TARGET = TARGET;
	}

	/**
	 * A method to return the target data attribute.
	 * @return the Actor target to follow.
	 */
	public Actor getTARGET() {
		return TARGET;
	}

	/**
	 * Returns a MoveAction to wander to a location that will move one step closer to stalked Actor.
	 * If no movement is possible, returns null.
	 *
	 * @param actor: The Actor enacting the behaviour
	 * @param map: The map that Actor is currently on
	 * @return a MoveAction, or null if no MoveAction is possible
	 */
	@Override
	public Action getAction(Actor actor, GameMap map) {
		// If no target to follow available, return null.
		if (getTARGET() == null) {
			return null;
		}

		// Current location of stalker Actor and current location of stalked target.
		Location here = map.locationOf(actor);
		Location there = map.locationOf(getTARGET());

		int currentDistance = distance(here, there);        // Calculate the distance between the two locations.

		// For loop to loop through all possible exits and then find the right location to move to get closer to TARGET.
		for (Exit exit : here.getExits()) {
			Location destination = exit.getDestination();    // Get the destination from the exit.

			// If stalker Actor can enter, find the path to the TARGET.
			if (destination.canActorEnter(actor)) {
				int newDistance = distance(destination, there);        // Calculate the new distance between destination and TARGET.
				// If this path is shorter than the current one, create a MoveAction to that destination and return it.
				if (newDistance < currentDistance) {
					return new MoveActorAction(destination, exit.getName());
				}
			}
		}
		return null;    // Else return null if no movement possible.
	}

	/**
	 * Compute the Manhattan distance between two locations.
	 *
	 * @param a the first location
	 * @param b the first location
	 * @return the number of steps between a and b if you only move in the four cardinal directions.
	 */
	private int distance(Location a, Location b) {
		return Math.abs(a.x() - b.x()) + Math.abs(a.y() - b.y());
	}
}