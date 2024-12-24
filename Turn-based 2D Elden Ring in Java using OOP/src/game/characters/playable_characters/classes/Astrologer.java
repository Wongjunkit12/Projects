package game.characters.playable_characters.classes;

import game.weapons.GreatKnife;

/**
 * A class representing the Astrologer Class, giving the player 396 staring HP and a GreatKnife as their starting weapon.
 * Inherits from Classes abstract class.
 *
 * Created by:
 * @author Wah Tan
 * Modified by:
 * @author Bryan Wong
 * @version 1.0
 * @see GreatKnife
 */

public class  Astrologer extends Classes {
    /**
     * Astrologer Constructor.
     * Set their respective inherited data attributes. Since Astrologer's Staff is optional therefore put the initial weapon as GreatKnife
     */
    public Astrologer() {
        super("Astrologer", 396, new GreatKnife());
    }
}
