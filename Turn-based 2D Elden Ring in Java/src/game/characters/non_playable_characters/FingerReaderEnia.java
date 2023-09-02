package game.characters.non_playable_characters;

import game.weapons.AxeOfGodrick;
import game.weapons.GraftedDragon;

/**
 * A class that represents the Finger Reader Enia Trader. Inherits from Trader.
 * Created by:
 * @author Bryan Wong
 * Modified by:
 *
 * @version 1.0
 * @see AxeOfGodrick
 * @see GraftedDragon
 */

public class FingerReaderEnia extends Trader {
    /**
     * FingerReaderEnia Constructor.
     * Call Trader parent constructor to set the name, displayChar and hitpoints.
     * Sets the buyable HashMap inventory for the Finger Reader Enia..
     */
    public FingerReaderEnia() {
        // Call parent constructor to set inherited data attributes.
        super("Finger Reader Enia", 'E', Integer.MAX_VALUE);

        // Although Finger Reader Enia cannot sell Weapons, put the Axe of Godrick and Grafted Dragon into its inventory as they
        // can be "exchanged" with a Remembrance of the Grafted, and thus be "bought".
        this.getBuyables().put(0, new AxeOfGodrick());
        this.getBuyables().put(1, new GraftedDragon());
    }
}
