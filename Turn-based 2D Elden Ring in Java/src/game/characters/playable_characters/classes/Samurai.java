package game.characters.playable_characters.classes;

import game.weapons.Uchigatana;

/**
 * A class representing the Samurai Class, giving the player 455 staring HP and a Uchigatana as their starting weapon.
 * Inherits from Classes abstract class.
 *
 * Created by:
 * @author Bryan Wong
 * Modified by:
 * @author Po Han Tay
 * @version 1.0
 * @see Uchigatana
 */

public class Samurai extends Classes {
    /**
     * Samurai Constructor.
     * Set their respective inherited data attributes.
     */
    public Samurai() {
        super("Samurai", 455, new Uchigatana());
    }
}
