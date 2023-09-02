package game.actions;

import edu.monash.fit2099.engine.actions.Action;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.positions.GameMap;
import game.weapons.Sellable;

/**
 * A class that represents a Selling Action. Sells a Weapon to the Trader for a fixed price.
 * Created by:
 * @author Po Han Tay
 * Modified by:
 * @author Bryan Wong
 * @version 1.0
 * @see Action
 * @see Actor
 * @see GameMap
 * @see Sellable
 */

public class SellingAction extends Action {
    /**
     * The Sellable Item the Player wants to sell.
     */
    private Sellable sellable;

    /**
     * Constructor for SellingAction.
     * Will take in one Sellable to sell to the Trader.
     *
     * @param sellable: The Sellable intended to be sold.
     */
    public SellingAction(Sellable sellable) {
        // Call setters to set the respective data attributes.
        setSellable(sellable);
    }

    /**
     * A method to return the sellable data attribute.
     * @return the Sellable intended to be sold.
     */
    public Sellable getSellable() {
        return sellable;
    }

    /**
     * A method to set the sellable data attribute.
     * @param sellable: The Sellable intended to be sold.
     */
    public void setSellable(Sellable sellable) {
        this.sellable = sellable;
    }

    /**
     * When executed, sell the sellable to the Trader and add the runeCount of the Player Runes based on the
     * Weapon selling price.
     * @param actor: The Actor performing the selling action.
     * @param map: The map the actor is on.
     * @return the result of the selling action.
     */
    @Override
    public String execute(Actor actor, GameMap map) {
        String result = "";

        // Sell the Sellable and add Runes.
        result += getSellable().sells(actor);

        return result;      // Return the result of the SellingAction.
    }

    /**
     * Describes what item is sold by the Player to the Trader and for what price.
     *
     * @param actor: The actor performing the action.
     * @return a description used for the menu UI.
     */
    @Override
    public String menuDescription(Actor actor) {
        return String.format("%s sells %s for %d Runes", actor, getSellable(), getSellable().getSellingPrice());
    }
}
