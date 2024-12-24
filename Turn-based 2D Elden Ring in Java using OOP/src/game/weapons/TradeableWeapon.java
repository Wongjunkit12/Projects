package game.weapons;

import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.positions.Exit;
import edu.monash.fit2099.engine.positions.Location;
import edu.monash.fit2099.engine.weapons.WeaponItem;
import game.actions.SellingAction;
import game.skills.Skills;
import game.utils.RuneManager;
import game.utils.Status;

/**
 * Am abstract class that represents all Tradeable Weapons in Elden Ring which inherits from WeaponItem.
 * Created by:
 * @author Bryan Wong
 * Modified by:
 * @author Po Han Tay
 * @version 1.0
 * @see Actor
 * @see Exit
 * @see Location
 * @see WeaponItem
 * @see SellingAction
 * @see Skills
 * @see RuneManager
 * @see Status
 */

public abstract class TradeableWeapon extends WeaponItem implements Sellable, Buyable {
    /**
     * An Integer instance variable representing the buying price of the Weapon from the Trader.
     */
    private int buyingPrice;
    /**
     * An Integer instance variable representing the selling price of the Weapon to the Trader.
     */
    private int sellingPrice;
    /**
     * A Skills instance representing the performable Skill the Actor can perform. Null by default if no performable skills.
     */
    private Skills skills = null;

    /**
     * TradeableWeapon Constructor.
     * Will call the WeaponItem parent constructor to set its respective data attributes.
     *
     * @param name: Name of the item
     * @param displayChar: Character to use for display when item is on the ground
     * @param damage: Amount of damage this weapon does
     * @param verb: Verb to use for this weapon, e.g. "hits", "zaps"
     * @param hitRate: The probability/chance to hit the target.
     * @param buyingPrice: The buying price of the Weapon from the Trader.
     * @param sellingPrice: The selling price of the Weapon to the Trader.
     */
    public TradeableWeapon(String name, char displayChar, int damage, String verb, int hitRate, int buyingPrice, int sellingPrice) {
        // Call the WeaponItem parent constructor to set its inherited data attributes.
        super(name, displayChar, damage, verb, hitRate);
        setBuyingPrice(buyingPrice);    // Set the buying price of the weapon from the Trader
        setSellingPrice(sellingPrice);  // Set the selling price of the weapon to the Trader

        // Add a TRADEABLE capability to this weapon to signify it can be bought and sold to the trader.
        this.addCapability(WeaponStatus.TRADEABLE);
    }

    /**
     * A method to return the buyingPrice data attribute.
     * @return the buying price of the weapon.
     */
    @Override
    public int getBuyingPrice() {
        return buyingPrice;
    }

    /**
     * A method to return the Menu Description of the BuyingAction.
     * @return what can be bought and for how much.
     */
    @Override
    public String getMenuDescription() {
        return String.format("purchases %s for %d Runes", this, getBuyingPrice());
    }

    /**
     * A method to return the sellingPrice data attribute.
     * @return the selling price of the weapon.
     */
    @Override
    public int getSellingPrice() {
        return sellingPrice;
    }

    /**
     * A method to return the skills data attribute.
     * @return the Skills instance representing the performable Skill the Actor can perform.
     */
    public Skills getSkills() {
        return skills;
    }

    /**
     * A method to set the buyingPrice data attribute.
     * @param buyingPrice: An Integer representing the buying price of the Weapon from the Trader
     */
    @Override
    public void setBuyingPrice(int buyingPrice) {
        this.buyingPrice = buyingPrice;
    }

    /**
     * A method to set the sellingPrice data attribute.
     * @param sellingPrice: An Integer representing the selling price of the Weapon to the Trader
     */
    @Override
    public void setSellingPrice(int sellingPrice) {
        this.sellingPrice = sellingPrice;
    }

    /**
     * A method to set the skills data attribute.
     * @param skills: The performable Skill the Actor can perform.
     */
    public void setSkills(Skills skills) {
        this.skills = skills;
    }

    /**
     * Inform a carried TradeableWeapon of the passage of time.
     * This method is called once per turn, if the TradeableWeapon is being carried.
     * Creates a SellingAction if adjacent to an Actor with the capability Status.TRADEABLE.
     * @param currentLocation The location of the actor carrying this TradeableWeapon.
     * @param actor The actor carrying this TradeableWeapon.
     */
    @Override
    public void tick(Location currentLocation, Actor actor) {
        // If the Actor carrying the TradeableWeapon can trade, check the exits of the Actor.
        if (actor.hasCapability(Status.TRADEABLE)) {
            // hasMerchant flag to determine if a Trader is adjacent.
            boolean hasMerchant = false;

            // Loop through all possible exits from the Actor's current location and find a Merchant to sell this TradeableWeapon
            // to
            for (Exit exit : currentLocation.getExits()) {
                Location destination = exit.getDestination();    // Get the destination of the said exit.

                // If there is an actor at the destination, it has the capability Status.TRADEABLE and the TradeableWeapon
                // allowableActions list is empty, add a SellingAction to the TradeableWeapon allowableActions list.
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
     * A method which will allow the Actor to sell this Weapon.
     * @param actor: The Actor enacting the SellingAction.
     */
    @Override
    public String sells(Actor actor) {
        // Get the RuneManager instance to add the Runes based on Weapon sellingPrice.
        RuneManager runeManager = RuneManager.getInstance(actor);

        // Add the runeCount based on the Weapon selling price.
        runeManager.addRunes(getSellingPrice());

        // Remove the sold Sellable from the Actor's inventory as sellable has been sold.
        actor.removeWeaponFromInventory(this);
        // Remove the SellingAction from the allowableActions List once TradeableWeapon is sold.
        if (!this.getAllowableActions().isEmpty()) {
            this.removeAction(this.getAllowableActions().get(0));
        }

        // Return the result of this SellingAction.
        return String.format("%s sells %s for %d Runes", actor, this, getSellingPrice());
    }

    /**
     * A method which will allow the Actor to buy this Weapon.
     * @param actor: The Actor enacting the BuyingAction.
     */
    @Override
    public String buys(Actor actor) {
        String result = "";
        // Get the RuneManager instance to subtract the Runes based on weapon buyingPrice.
        RuneManager runeManager = RuneManager.getInstance(actor);

        // If the Player has enough Runes to purchase the Buyable, add it to his inventory and subtract the runeCount from his
        // Runes object.
        if (runeManager.getRunes().getRuneCount() >= getBuyingPrice()) {
            // Add the Buyable purchased to the Player inventory.
            actor.addWeaponToInventory(this);

            // Subtract the runeCount based on the weapon buying price.
            runeManager.subtractRunes(getBuyingPrice());

            result += String.format("%s purchases %s for %d Runes", actor, this, getBuyingPrice());
        }
        // Else if not enough Runes to purchase the Buyable, do not buy the Buyable.
        else {
            result += String.format("Insufficient Runes to purchase %s", this);
        }

        // Return the result of this BuyingAction.
        return result;
    }
}
