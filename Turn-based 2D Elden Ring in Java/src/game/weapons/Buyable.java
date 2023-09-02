package game.weapons;

import edu.monash.fit2099.engine.actors.Actor;

/**
 * A Buyable interface.
 * Created by:
 * @author Bryan Wong
 * Modified by:
 * @author Po Han Tay
 * @version 1.0
 * @see Actor
 */

public interface Buyable {
    /**
     * A method which will allow the Actor to buy the Weapon.
     * @param actor: The Actor enacting the BuyingAction.
     * @return the result of the BuyingAction.
     */
    String buys(Actor actor);

    /**
     * A method to return the buyingPrice data attribute.
     * @return the buying price of the Weapon.
     */
    int getBuyingPrice();

    /**
     * A method to return the Menu Description of the BuyingAction.
     * @return the description of what happen during the BuyingAction.
     */
    String getMenuDescription();

    /**
     * A method to set the buyingPrice data attribute.
     * @param buyingPrice: An Integer representing the buying price of the Weapon from the Trader
     */
    void setBuyingPrice(int buyingPrice);
}
