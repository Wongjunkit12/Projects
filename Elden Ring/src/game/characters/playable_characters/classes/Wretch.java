package game.characters.playable_characters.classes;

import game.weapons.Club;

/**
 * A class representing the Wretch Class, giving the player 414 staring HP and a Club as their starting weapon.
 * Inherits from Classes abstract class.
 *
 * Created by:
 * @author Bryan Wong
 * Modified by:
 * @author Po Han Tay
 * @version 1.0
 * @see Club
 */

public class Wretch extends Classes {
    /**
     * Wretch Constructor.
     * Set their respective inherited data attributes.
     */
    public Wretch() {
        super("Wretch", 414, new Club());
    }
}
