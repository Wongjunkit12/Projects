package game.environments;

import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.positions.Ground;
import game.utils.Status;

/**
 * A class that represents the Floor inside a building.
 * Created by:
 * @author Riordan D. Alfredo
 * Modified by:
 * @author Bryan Wong
 * @version 1.0
 * @see Actor
 * @see Ground
 * @see Status
 */

public class Floor extends Ground {
	/**
	 * Floor Constructor.
	 * Calls the parent SpawnableGround constructor to set its inherited data attributes. Will spawn 1 trader at game start.
	 */
	public Floor() {
		// Create Floor object and spawn 1 Trader in it.
		super('_');
		// Add CAN_GENERATE_GOLDEN_RUNNES status to show that it can spawn Golden Runes.
		this.addCapability(GroundStatus.CAN_GENERATE_GOLDEN_RUNES);
	}

	/**
	 * A method to determine if the inputted Actor can enter into it. All Enemies are unable to enter into the Floor.
	 * @param actor: The Actor trying to enter the Floor.
	 * @return true if Actor can enter and false otherwise.
	 */
	@Override
	public boolean canActorEnter(Actor actor) {
		// Only Player can enter the floor.
		return actor.hasCapability(Status.CAN_ENTER_FLOOR);
	}
}
