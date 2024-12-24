package game.actions;

import edu.monash.fit2099.engine.actions.Action;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.positions.GameMap;
import edu.monash.fit2099.engine.weapons.Weapon;
import game.utils.RandomNumberGenerator;
import game.utils.Status;

/**
 * An Action to attack another Actor.
 * Created by:
 * @author Adrian Kristanto
 * Modified by:
 * @author Bryan Wong, Po Han Tay
 * @version 1.0
 * @see Action
 * @see Actor
 * @see GameMap
 * @see Weapon
 * @see RandomNumberGenerator
 */

public class AttackAction extends Action {
	/**
	 * The Actor that is to be attacked
	 */
	private Actor target;
	/**
	 * The direction of incoming attack.
	 */
	private String direction;
	/**
	 * Weapon used for the attack
	 */
	private Weapon weapon;

	/**
	 * Constructor with Intrinsic Weapon used as default.
	 *
	 * @param target: The Actor to attack
	 * @param direction: The direction where the attack should be performed (only used for display purposes)
	 */
	public AttackAction(Actor target, String direction) {
		setTarget(target);        	// Set the target of incoming attack.
		setDirection(direction);    // Set the direction of incoming attack.
	}

	/**
	 * Overloaded AttackAction Constructor for weapon wielders.
	 *
	 * @param target: The Actor to attack
	 * @param direction: The direction where the attack should be performed (only used for display purposes)
	 * @param weapon: The weapon used by the attacker to attack.
	 */
	public AttackAction(Actor target, String direction, Weapon weapon) {
		setTarget(target);			// Set the target of incoming attack.
		setDirection(direction);	// Set the direction of incoming attack.
		setWeapon(weapon);       	// Set the weapon used in incoming attack.
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
	 * A method to return the weapon data attribute.
	 * @return the weapon used to attack. Will remain as null if none exists.
	 */
	public Weapon getWeapon() {
		return weapon;
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
	 * A method to set the weapon data attribute.
	 * @param weapon: The weapon used to attack. Will remain as null if none exists.
	 */
	public void setWeapon(Weapon weapon) {
		this.weapon = weapon;
	}

	/**
	 * When executed, the chance to hit of the weapon that the Actor used is computed to determine whether
	 * the actor will hit the target.
	 * If so, deal damage to the target and determine whether the target is killed.
	 *
	 * @param actor The actor performing the attack action.
	 * @param map The map the actor is on.
	 * @return the result of the attack, e.g. whether the target is killed, etc.
	 */
	@Override
	public String execute(Actor actor, GameMap map) {
		// If no weapon is provided, use IntrinsicWeapon by default.
		if (weapon == null) {
			weapon = actor.getIntrinsicWeapon();
		}

		String result = "";        // The result of the attack. Whether it misses o hits and whether it kills.

		// Generate a random number from 1 to 100 inclusive and compare it with chance to hit.
		// If the generated random number is not less than or equal to the chanceToHit (misses), then attack does no damage.
		if (!(RandomNumberGenerator.getRandomInt(1, 100) <= weapon.chanceToHit())) {
			// Return the missed attack result.
			result += String.format("%s tried to %s %s but misses", actor, weapon.verb(), target);
		}

		// Else attack hits so subtract target health.
		else {
			int damage = weapon.damage();    // Get weapon damage, either weapon in inventory or IntrinsicWeapon.
			// Successful hit between Actor and target.
			result += String.format("%s %s %s for %d damage", actor, weapon.verb(), target, damage);
			target.hurt(damage);    // Reduce target health based on weapon's damage.

			// If target has died (health < 0), then create a new DeathAction with the target and execute it, appending
			// the DeathAction result to the result String.
			if (!target.isConscious() && !getTarget().hasCapability(Status.DEAD)) {
				// Execute the DeathAction and get the Death message,
				result += new DeathAction(actor).execute(getTarget(), map);
			}
		}
		return result;
	}

	/**
	 * Describes which target the actor is attacking with which Weapon.
	 *
	 * @param actor: The actor performing the action.
	 * @return a description used for the menu UI.
	 */
	@Override
	public String menuDescription(Actor actor) {
		return String.format("%s attacks %s at %s with %s", actor, target, direction, (weapon != null ? weapon : "Intrinsic Weapon"));
	}
}
