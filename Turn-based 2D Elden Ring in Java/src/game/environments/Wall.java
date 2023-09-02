package game.environments;

import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.positions.Ground;

/**
 * A class that represents the Wall.
 * Created by:
 * @author Riordan D. Alfredo
 * Modified by:
 * @author Bryan Wong
 * @version 1.0
 * @see Actor
 * @see Ground
 */

public class Wall extends Ground {
	/**
	 * Wall Constructor.
	 * Calls the parent Ground constructor to set its inherited data attributes.
	 */
	public Wall() {
		super('#');
	}

	/**
	 * A method to determine if the inputted Actor can enter into it. All Actors are unable to enter into the Wall.
	 * @param actor: The Actor trying to enter the Wall.
	 * @return false always as nobody can enter the Wall.
	 */
	@Override
	public boolean canActorEnter(Actor actor) {
		return false;
	}

	/**
	 * A method to determine if the Wall can block thrown objects.
	 * @return true always as Wall can block all thrown objects always.
	 */
	@Override
	public boolean blocksThrownObjects() {
		return true;
	}
}
