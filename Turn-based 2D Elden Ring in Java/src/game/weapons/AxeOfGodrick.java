package game.weapons;

import edu.monash.fit2099.engine.actions.Action;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.items.Item;
import game.actions.AreaAttackAction;
import game.items.ItemStatus;
import game.items.RemembranceOfTheGrafted;
import game.skills.SpinAttack;
import game.utils.Status;
import game.utils.Utils;

/**
 * A class AxeOfGodrick that inherits from TradeableWeapon. It can be exchanged with the Remembrance of the Grafted by
 * Finger Reader Enia.
 * Deals 142 damage and has a hit rate of 84%.
 * Created by:
 * @author Po Han Tay
 * Modified by:
 * @author Bryan Wong
 * @version 1.0
 * @see Action
 * @see Actor
 * @see Item
 * @see AreaAttackAction
 * @see RemembranceOfTheGrafted
 * @see ItemStatus
 * @see SpinAttack
 * @see Status
 * @see Utils
 */

public class AxeOfGodrick extends TradeableWeapon {
    /**
     * AxeOfGodrick Constructor.
     * Will call the TradeableWeapon parent constructor to set its respective data attributes.
     */
    public AxeOfGodrick() {
        // Call the TradeableWeapon parent constructor to set its inherited data attributes.
        super("Axe Of Godrick", 'T', 142, "chops", 84, 0, 100);

        // Set the AxeOfGodrick skill to a SpinAttack object, so that it performs a spin attack.
        this.setSkills(new SpinAttack("spins Axe Of Godrick", 142, 84, "spins"));

        // Add a CAN_AREA_ATTACK capability to this weapon to signify it can perform an AreaAttackAction.
        this.addCapability(WeaponStatus.CAN_AREA_ATTACK);
    }

    /**
     * Check if AxeOfGodrick can perform a skill.
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
     * @return the Axe of Godrick being exchanged for a Remembrance of the Grafted.
     */
    @Override
    public String getMenuDescription() {
        return String.format("exchanges a Remembrance of the Grafted for %s", this);
    }

    /**
     * Override the buys method to allow the Player to exchange for an Axe Of Godrick with a Remembrance Of The Grafted.
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

        // Allow the Player to exchange the RemembranceOfTheGrafted for an AxeOfGodrick if he has one.
        if (itemToExchange != null) {
            // Remove the RemembranceOfTheGrafted from the Player's inventory.
            actor.removeItemFromInventory(itemToExchange);
            // Add the AxeOfGodrick exchanged to the Player inventory.
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
