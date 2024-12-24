package game.characters.playable_characters.classes;

import game.weapons.GreatKnife;

/**
 * A class representing the Bandit Class, giving the player 414 staring HP and a GreatKnife as their starting weapon.
 * Inherits from Classes abstract class.
 *
 * Created by:
 * @author Bryan Wong
 * Modified by:
 * @author Po Han Tay
 * @version 1.0
 * @see GreatKnife
 */

public class Bandit extends Classes {
    /**
     * Bandit Constructor.
     * Set their respective inherited data attributes.
     */
    public Bandit() {
        super("Bandit", 414, new GreatKnife());
    }
}
