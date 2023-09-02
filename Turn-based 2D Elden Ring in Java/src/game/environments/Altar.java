package game.environments;

import edu.monash.fit2099.engine.actions.ActionList;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.items.Item;
import edu.monash.fit2099.engine.positions.Ground;
import edu.monash.fit2099.engine.positions.Location;
import game.actions.BuyingAction;
import game.utils.RuneManager;
import game.utils.Status;
import game.weapons.Buyable;

import java.lang.reflect.Constructor;

/**
 * A class that inherits from Ground. The Player can make offering using Runes in exchange for Sacred Tears.
 * Implements Buyable as they can be used to "buy" Sacred Tears.
 * Created by:
 * @author Bryan Wong
 * Modified by:
 *
 * @version 1.0
 * @see ActionList
 * @see Actor
 * @see Item
 * @see Ground
 * @see Location
 * @see BuyingAction
 * @see RuneManager
 * @see Status
 * @see Buyable
 */

public class Altar extends Ground implements Buyable {
    /**
     * An integer representing the amount of Runes that needs to be offered in exchange for Sacred Tears.
     */
    private int offeringPrice;
    /**
     * An integer representing the amount of Runes to increment the offeringPrice once an offering is performed.
     */
    private int offeringIncrement;
    /**
     * The amount of offerings made on this Altar.
     */
    private int offeringCount;
    /**
     * The limit of offerings allowed on this Altar.
     */
    private int offeringLimit;
    /**
     * The Item the Player will be blessed with once sufficient offerings are given.
     */
    private Item divineBlessing;

    /**
     * Altar Constructor.
     * Calls parent class to set inherited data attributes.
     * @param offeringPrice: The amount of Runes that needs to be offered in exchange for Sacred Tears.
     * @param offeringIncrement: The amount of Runes to increment the offeringPrice once an offering is performed.
     * @param offeringLimit: The limit of offerings allowed on this Altar.
     * @param divineBlessing: The Item the Player will be blessed with once sufficient offerings are given.
     */
    public Altar(int offeringPrice, int offeringIncrement, int offeringLimit, Item divineBlessing) {
        super('Q');                       // Set the display character as 'Q'.
        setBuyingPrice(offeringPrice);              // Set the offeringPrice based on input given.
        setOfferingIncrement(offeringIncrement);    // Set the offeringIncrement based on input given.
        setOfferingLimit(offeringLimit);            // Set the offeringLimit based on input given.
        setDivineBlessing(divineBlessing);          // Set the divineBlessing based on input given.
        setOfferingCount(0);                        // Set the amount of offering made to this Altar as 0 initially.
    }

    /**
     * A method to return the offeringPrice data attribute.
     * @return the amount of Runes that needs to be offered in exchange for Sacred Tears.
     */
    @Override
    public int getBuyingPrice() {
        return offeringPrice;
    }

    /**
     * A method to return the offeringIncrement data attribute.
     * @return the amount of Runes to increment the offeringPrice once an offering is performed.
     */
    public int getOfferingIncrement() {
        return offeringIncrement;
    }

    /**
     * A method to return the offeringCount data attribute.
     * @return the amount of offerings made on this Altar.
     */
    public int getOfferingCount() {
        return offeringCount;
    }

    /**
     * A method to return the offeringLimit data attribute.
     * @return the limit of offerings allowed on this Altar.
     */
    public int getOfferingLimit() {
        return offeringLimit;
    }

    /**
     * A method to return the divineBlessing data attribute.
     * @return the Item the Player will be blessed with once sufficient offerings are given.
     */
    public Item getDivineBlessing() {
        return divineBlessing;
    }

    /**
     * A method to return the Menu Description of the BuyingAction.
     * @return how many Runes needed to be offered in exchange for Sacred Tears.
     */
    @Override
    public String getMenuDescription() {
        return String.format("offers %d Runes", getBuyingPrice());
    }

    /**
     * A method to set the offeringPrice data attribute.
     * @param offeringPrice: The amount of Runes that needs to be offered in exchange for Sacred Tears.
     */
    @Override
    public void setBuyingPrice(int offeringPrice) {
        this.offeringPrice = offeringPrice;
    }

    /**
     * A method to set the offeringIncrement data attribute.
     * @param offeringIncrement: The amount of Runes to increment the offeringPrice once an offering is performed.
     */
    public void setOfferingIncrement(int offeringIncrement) {
        this.offeringIncrement = offeringIncrement;
    }

    /**
     * A method to set the offeringCount data attribute.
     * @param offeringCount: The amount of offerings made on this Altar.
     */
    public void setOfferingCount(int offeringCount) {
        this.offeringCount = offeringCount;
    }

    /**
     * A method to set the offeringLimit data attribute.
     * @param offeringLimit: The limit of offerings allowed on this Altar.
     */
    public void setOfferingLimit(int offeringLimit) {
        this.offeringLimit = offeringLimit;
    }

    /**
     * A method to set the divineBlessing data attribute.
     * @param divineBlessing: The Item the Player will be blessed with once sufficient offerings are given.
     */
    public void setDivineBlessing(Item divineBlessing) {
        this.divineBlessing = divineBlessing;
    }

    /**
     * Will contain the lists of Actions allowable for Player to perform on the Altar.
     *
     * @param actor: The Actor that is on the Altar.
     * @param location: The Location of the Altar.
     * @param direction: String representing the direction of the Actor.
     * @return the list of allowable actions that can be performed by the Actor on the Altar.
     */
    @Override
    public ActionList allowableActions(Actor actor, Location location, String direction) {
        // Create an ActionList containing a list of allowable actions to be performed by the Player on the Altar.
        ActionList actions = new ActionList();

        // If the Actor performing the action has the capability CAN_GAIN_RUNES, create a BuyingAction for them to allow them to
        // make an offering.
        if (actor.hasCapability(Status.CAN_GAIN_RUNES)) {
            // Add in a BuyingAction to the actions list and pass in this Altar as a Buyable instance.
            actions.add(new BuyingAction(this));
        }
        return actions;
    }

    @Override
    public String buys(Actor actor) {
        String result = "";

        // Try block.
        try {
            // Get the constructor for the divineBlessing Item.
            Class<?> cls = getDivineBlessing().getClass();
            Constructor<?> constructor;
            constructor = cls.getConstructor();

            // Get the RuneManager instance to subtract the Runes based on offeringPrice.
            RuneManager runeManager = RuneManager.getInstance(actor);

            // If the amount of offerings made is equal to or have surpassed the limit allowed, refuse the offering.
            if (getOfferingCount() >= getOfferingLimit()) {
                result += String.format("The Gods refuse your offering");
            }
            // Else if the Player has enough Runes to offer to the Altar, subtract the runeCount from the runes.
            else if (runeManager.getRunes().getRuneCount() >= getBuyingPrice()) {
                // Subtract the runeCount based on the upgrade price.
                runeManager.subtractRunes(getBuyingPrice());

                // Create a new instance of the divineBlessing to bless the Player with. Upcast it to Item.
                Item blessedItem = (Item) constructor.newInstance();
                // Add the Item to the Player's inventory.
                actor.addItemToInventory(blessedItem);

                result += String.format("%s offers %d Runes and is blessed with %s", actor, getBuyingPrice(), blessedItem);

                // Increase the offering required by the offeringIncrement each time the offering is made.
                this.setBuyingPrice(getBuyingPrice() + getOfferingIncrement());
                // Increment the offeringCount by one.
                this.setOfferingCount(getOfferingCount() + 1);
            }
            // Else if not enough Runes to offer, do not be blessed.
            else {
                result += String.format("The Gods are displeased with your stringent offering");
            }
        // Catch exceptions
        } catch (Exception e) {
            e.printStackTrace();
        }
        // Return the results.
        return result;
    }
}
