package game.actions;

import edu.monash.fit2099.engine.actions.Action;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.positions.GameMap;
import game.weapons.Buyable;

/**
 * A class that represents a Buying Action. Buys the Buyable from the Trader for a fixed price.
 * Created by:
 * @author Po Han Tay
 * Modified by:
 * @author Bryan Wong
 * @version 1.0
 * @see Action
 * @see Actor
 * @see GameMap
 * @see Buyable
 */

public class BuyingAction extends Action {
    /**
     * The Buyable the Player wants to buy.
     */
    private Buyable buyable;

    /**
     * Constructor for BuyingAction.
     * Will take in one Buyable to buy from the Trader.
     *
     * @param buyable: The Buyable to be bought.
     */
    public BuyingAction(Buyable buyable) {
        // Call setters to set the respective data attributes.
        setBuyable(buyable);
    }

    /**
     * A method to return the buyable data attribute.
     * @return the Buyable to be bought.
     */
    public Buyable getBuyable() {
        return buyable;
    }

    /**
     * A method to set the buyable data attribute.
     * @param buyable: The Buyable to be bought.
     */
    public void setBuyable(Buyable buyable) {
        this.buyable = buyable;
    }

    /**
     * When executed, buy the buyable from the Trader and subtract the runeCount of the Player Runes based on the
     * Buyable buying price.
     * @param actor The actor performing the buying action.
     * @param map The map the actor is on.
     * @return the result of the buying action.
     */
    @Override
    public String execute(Actor actor, GameMap map) {
        String result = "";

        // Buy the Buyable bu executing the .buys() method.
        result += getBuyable().buys(actor);

        return result;      // Return the result of the BuyingAction.
    }

    /**
     * Describes what happens during the BuyingAction.
     *
     * @param actor: The actor performing the action.
     * @return a description used for the menu UI.
     */
    @Override
    public String menuDescription(Actor actor) {
        return String.format("%s %s", actor, getBuyable().getMenuDescription());
    }
}
