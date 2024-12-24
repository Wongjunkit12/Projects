package game.items;

import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.items.Item;
import edu.monash.fit2099.engine.positions.Exit;
import edu.monash.fit2099.engine.positions.Location;
import game.actions.SellingAction;
import game.utils.RuneManager;
import game.utils.Status;
import game.weapons.Sellable;

/**
 * A class RemembranceOfTheGrafted that inherits from Item abstract class.
 * Implements Sellable as they can be sold to the Traders.
 * Created by:
 * @author Po Han Tay
 * Modified by:
 * @author Bryan Wong
 * @version 1.0
 * @see Actor
 * @see Item
 * @see Exit
 * @see Location
 * @see SellingAction
 * @see RuneManager
 * @see Status
 * @see Sellable
 */

public class RemembranceOfTheGrafted extends Item implements Sellable {
    /**
     * An Integer instance variable representing the selling price of the RemembranceOfTheGrafted to the Trader.
     */
    private int sellingPrice;

    /**
     * RemembranceOfTheGrafted Constructor.
     * Will call the Item parent constructor to set its respective data attributes.
     */
    public RemembranceOfTheGrafted() {
        // Call the parent constructor to set the Item attributes then set the runeCount.
        super("Remembrance Of The Grafted", 'O', true);

        // Set the selling price of Remembrance Of The Grafted to 20000 Runes
        setSellingPrice(20000);

        // Remembrance Of The Grafted can be sold.
        this.addCapability(ItemStatus.TRADEABLE);

        // Remembrance Of The Grafted can be used to exchange for weapons with Enia.
        this.addCapability(ItemStatus.EXCHANGEABLE_WITH_ENIA);
    }

    /**
     * A method to return the sellingPrice data attribute.
     * @return the selling price of the Item.
     */
    @Override
    public int getSellingPrice() {
        return sellingPrice;
    }

    /**
     * A method to set the sellingPrice data attribute.
     * @param sellingPrice: The selling price of the Item to the Trader
     */
    @Override
    public void setSellingPrice(int sellingPrice) {
        this.sellingPrice = sellingPrice;
    }

    /**
     * Inform a carried RemembranceOfTheGrafted of the passage of time.
     * This method is called once per turn, if the RemembranceOfTheGrafted is being carried.
     * Creates a SellingAction if adjacent to an Actor with the capability Status.TRADEABLE.
     * @param currentLocation The location of the actor carrying this RemembranceOfTheGrafted.
     * @param actor The actor carrying this RemembranceOfTheGrafted.
     */
    @Override
    public void tick(Location currentLocation, Actor actor) {
        // If the Actor carrying the RemembranceOfTheGrafted can trade, check the exits of the Actor.
        if (actor.hasCapability(Status.TRADEABLE)) {
            // hasMerchant flag to determine if a Trader is adjacent.
            boolean hasMerchant = false;

            // Loop through all possible exits from the Actor's current location and find a Merchant to sell this RemembranceOfTheGrafted
            // to
            for (Exit exit : currentLocation.getExits()) {
                Location destination = exit.getDestination();    // Get the destination of the said exit.

                // If there is an actor at the destination, it has the capability Status.TRADEABLE and the RemembranceOfTheGrafted
                // allowableActions list is empty, add a SellingAction to the RemembranceOfTheGrafted allowableActions list.
                if (destination.containsAnActor() && destination.getActor().hasCapability(Status.TRADEABLE) &&
                        this.getAllowableActions().isEmpty()) {
                    // Create a new SellingAction for this weapon.
                    this.addAction(new SellingAction(this));
                    hasMerchant = true;     // Signify that a merchant is present on an adjacent tile.
                }
                // Else if there's no adjacent merchant and the allowableACtions list is not empty, remove the SellingAction.
                else if (!hasMerchant && !this.getAllowableActions().isEmpty()) {
                    /// Remove the SellingAction from the allowableActions List once TradeableWeapon is sold.
                    this.removeAction(this.getAllowableActions().get(0));
                }
            }
        }
    }

    /**
     * A method which will allow the Actor to sell this Item.
     * @param actor: The Actor enacting the SellingAction.
     */
    @Override
    public String sells(Actor actor) {
        // Get the RuneManager instance to add the Runes based on Item sellingPrice.
        RuneManager runeManager = RuneManager.getInstance(actor);

        // Add the runeCount based on the Item selling price.
        runeManager.addRunes(getSellingPrice());

        // Remove the sold Sellable from the Actor's inventory as sellable has been sold.
        actor.removeItemFromInventory(this);
        // Remove the SellingAction from the allowableActions List once RemembranceOfTheGrafted is sold.
        if (!this.getAllowableActions().isEmpty()) {
            this.removeAction(this.getAllowableActions().get(0));
        }

        // Return the result of this SellingAction.
        return String.format("%s sells %s for %d Runes", actor, this, getSellingPrice());
    }
}
