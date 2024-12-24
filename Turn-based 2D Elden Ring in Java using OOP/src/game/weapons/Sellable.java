package game.weapons;

import edu.monash.fit2099.engine.actors.Actor;

/**
 * A Sellable interface.
 * Created by:
 * @author Bryan Wong
 * Modified by:
 * @author Po Han Tay
 * @version 1.0
 * @see Actor
 */

public interface Sellable {
    /**
     * A method which will allow the Actor to sell the Weapon.
     * @param actor: The Actor enacting the SellingAction.
     * @return the result of the SellingAction.
     */
    String sells(Actor actor);

    /**
     * A method to return the sellingPrice data attribute.
     * @return the selling price of the weapon.
     */
    int getSellingPrice();

    /**
     * A method to set the sellingPrice data attribute.
     * @param sellingPrice: An Integer representing the selling price of the Weapon to the Trader
     */
    void setSellingPrice(int sellingPrice);
}
