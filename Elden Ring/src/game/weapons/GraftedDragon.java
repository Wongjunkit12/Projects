package game.weapons;

import edu.monash.fit2099.engine.actions.Action;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.items.Item;
import game.actions.AreaAttackAction;
import game.items.ItemStatus;
import game.items.RemembranceOfTheGrafted;
import game.skills.SlamAttack;
import game.utils.Status;
import game.utils.Utils;

/**
 * A class GraftedDragon that inherits from TradeableWeapon. It can be exchanged with the Remembrance of the Grafted by
 * Finger Reader Enia.
 * Deals 89 damage and has a hit rate of 90%.
 * Created by:
 * @author Po Han Tay
 * Modified by:
 * @author Bryan Wong
 * @version 1.0
 * @see Action
 * @see Actor
 * @see Item
 * @see AreaAttackAction
 * @see ItemStatus
 * @see RemembranceOfTheGrafted
 * @see SlamAttack
 * @see Status
 * @see Utils
 */

public class GraftedDragon extends TradeableWeapon {
    /**
     * GraftedDragon Constructor.
     * Will call the TradeableWeapon parent constructor to set its respective data attributes.
     */
    public GraftedDragon() {
        // Call the TradeableWeapon parent constructor to set its inherited data attributes.
        super("Grafted Dragon", 'N', 89, "strikes", 90, 0, 200);

        // Set the GraftedDragon skill to a SlamAttack object, so that it performs a slam attack.
        this.setSkills(new SlamAttack("slams ground using Grafted Dragon", 89, 90, "slams"));

        // Add a CAN_AREA_ATTACK capability to this weapon to signify it can perform an AreaAttackAction.
        this.addCapability(WeaponStatus.CAN_AREA_ATTACK);
    }

    /**
     * Check if GraftedDragon can perform a skill.
     * @param target target actor
     * @return an AreaAttackAction that attacks all adjacent Actors. Null if no available Skill.
     */
    @Override
    public Action getSkill(Actor target, String direction) {
        // If Weapon can perform an Area Attack, return an AreaAttackAction.
        if (this.hasCapability(WeaponStatus.CAN_AREA_ATTACK) && !target.hasCapability(Status.DEAD)) {
            return new AreaAttackAction(target, direction, getSkills());
        }
        return null;
    }

    /**
     * A method to return the Menu Description of the BuyingAction.
     * @return the Grafted Dragon being exchanged for a Remembrance of the Grafted.
     */
    @Override
    public String getMenuDescription() {
        return String.format("exchanges a Remembrance of the Grafted for %s", this);
    }

    /**
     * Override the buys method to allow the Player to exchange for a Grafted Dragon with a Remembrance Of The Grafted.
     * @param actor: The Actor enacting the BuyingAction.
     */
    @Override
    public String buys(Actor actor) {
        String result = "";             // The result of this action.
        Item itemToExchange = null;     // The Item to be exchanged for this Weapon. Default to null.

        // Loops through the Actor's inventory to find the RemembranceOfTheGrafted object.
        for (Item item : actor.getItemInventory()) {
            // If the RemembranceOfTheGrafted object is found, set the itemToExchange to be the RemembranceOfGradted.
            if (item.hasCapability(ItemStatus.EXCHANGEABLE_WITH_ENIA)) {
                itemToExchange = item;
            }
        }

        // Allow the Player to exchange the RemembranceOfTheGrafted for an GraftedDragon if he has one.
        if (itemToExchange != null) {
            // Remove the RemembranceOfTheGrafted from the Player's inventory.
            actor.removeItemFromInventory(itemToExchange);
            // Add the GraftedDragon exchanged to the Player inventory.
            actor.addWeaponToInventory(this);

            result += String.format("%s exchanges a %s for %s", actor, itemToExchange, this);
        }
        // Else if Player does not have a RemembranceOfTheGrafted, do not allow the exchange.
        else {
            result += String.format("%s does not have a Remembrance of the Grafted to exchange for %s", actor, this);
        }
        return result;
    }
}
