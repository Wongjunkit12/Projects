package game.characters.non_playable_characters;

import game.weapons.*;

/**
 * A class that represents the Merchant Kale Trader. Inherits from Trader.
 * Created by:
 * @author Bryan Wong
 * Modified by:
 *
 * @version 1.0
 * @see Club
 * @see GreatKnife
 * @see Scimitar
 * @see Uchigatana
 */

public class MerchantKale extends Trader {
    /**
     * MerchantKale Constructor.
     * Call Trader parent constructor to set the name, displayChar and hitpoints.
     * Sets the buyable HashMap inventory for Merchant Kale.
     */
    public MerchantKale() {
        // Call parent constructor to set inherited data attributes.
        super("Merchant Kale", 'K', Integer.MAX_VALUE);

        // Add all weapons that Merchant Kale sells to its inventory.
        this.getBuyables().put(0, new Uchigatana());
        this.getBuyables().put(1, new GreatKnife());
        this.getBuyables().put(2, new Club());
        this.getBuyables().put(3, new Scimitar());
    }
}
