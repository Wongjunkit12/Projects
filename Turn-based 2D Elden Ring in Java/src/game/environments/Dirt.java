package game.environments;

import edu.monash.fit2099.engine.positions.Ground;

/**
 * A class that represents bare dirt.
 * Created by:
 * @author Riordan D. Alfredo
 * Modified by:
 *
 * @see Ground
 */

public class Dirt extends Ground {
	/**
	 * Dirt Constructor.
	 */
	public Dirt() {
		super('.');
		// Add CAN_GENERATE_GOLDEN_RUNNES status to show that it can spawn Golden Runes.
		this.addCapability(GroundStatus.CAN_GENERATE_GOLDEN_RUNES);
	}
}
