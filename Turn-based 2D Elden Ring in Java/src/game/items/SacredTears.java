package game.items;

import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.items.Item;
import edu.monash.fit2099.engine.items.PickUpAction;
import edu.monash.fit2099.engine.items.PickUpItemAction;
import edu.monash.fit2099.engine.positions.Location;
import game.actions.ConsumeAction;
import game.utils.Status;
import game.utils.Utils;

/**
 * A class SacredTears that inherits from abstract class.
 * Implements Consumable as can be consumed to increase Flask of Crimson Tears potency.
 * Created by:
 * @author Bryan Wong
 * Modified by:
 *
 * @version 1.0
 * @see Actor
 * @see Item
 * @see PickUpAction
 * @see PickUpItemAction
 * @see Location
 * @see ConsumeAction
 * @see Status
 * @see Utils
 */

public class  SacredTears extends Item implements Consumable {
    /**
     * An integer representing the increase in health restored for the Flask of Crimson Tears when potency is increased.
     */
    private int healthToIncrease;
    /**
     * The minimum health to increase for the Sacred Tears.
     */
    private final int MIN_HEALTH = 15;

    /**
     * SacredTear Constructor.
     * Will call the Item parent constructor to set its respective data attributes.
     */
    public SacredTears() {
        // Set the parents attribute, making portable to false as it cannot be dropped.
        super("Sacred Tears", 'v', false);
        // Set the healthToIncrease to 95 initially.
        setHealthToIncrease(95);
        // Add the capability UPGRADE_POTENCY to signify that this item can be used to upgrade the Flask of Crimson Tears potency.
        this.addCapability(ItemStatus.CAN_UPGRADE_POTENCY);
        // Add the capability CONSUMABLE to signify that this item can be consumed by the Player.
        this.addCapability(ItemStatus.CONSUMABLE);
    }

    /**
     * A method to return the healthToIncrease data attribute.
     * @return the increase in health restored for the Flask of Crimson Tears when potency is increased.
     */
    public int getHealthToIncrease() {
        return healthToIncrease;
    }

    /**
     * A method to set the healthToIncrease data attribute.
     * @param healthToIncrease: The increase in health restored for the Flask of Crimson Tears when potency is increased.
     */
    public void setHealthToIncrease(int healthToIncrease) {
        this.healthToIncrease = healthToIncrease;
    }

    /**
     * Create and return an action to pick this Sacred Tears up.
     * Only allows the Player to pick it up.
     *
     * @return a new PickUpItemAction if the actor is the Player, null otherwise.
     */
    @Override
    public PickUpAction getPickUpAction(Actor actor) {
        // If Actor is the Player, create and return a new PickUpItemAction.
        if (actor.hasCapability(Status.PLAYER)) {
            return new PickUpItemAction(this);
        }
        return null;        // Otherwise return null to prevent others from picking it up.
    }

    /**
     * A method which will allow the Actor to consume the Sacred Tears and upgrade the potency of the Flask of Crimson Tears.
     * @param actor: The Actor that will consume the Sacred Tears.
     * @return the result String of the Sacred Tears being consumed.
     */
    @Override
    public String consume(Actor actor) {
        String result = "";

        // Get the Flask of Crimson Tears in the Player's inventory.
        FlaskOfCrimsonTears flask = Utils.getFlask(actor);

        // If the Actor has a Flask of Crimson Tears, allow its potency to be upgraded.
        if (flask != null && this.hasCapability(ItemStatus.CAN_UPGRADE_POTENCY)) {
            // Increase the Flask of Crimson Tears health restored on consumed by 95 for the first time, decreasing
            // the increase in health restored by 10 for each potency.
            // Meaning second usage of Sacred Tears will result in an increase in 85, with the third being 75 and so on.
            int healingToUpgrade = getHealthToIncrease() - (10 * flask.getPotency());

            // If the healing to upgrade is more than the MIN_HEALTH, increment the health to heal with that.
            if (healingToUpgrade > MIN_HEALTH) {
                flask.setHealthToHeal(flask.getHealthToHeal() + healingToUpgrade);
            }
            // Else only increment health to heal with MIN_HEALTH,
            else {
                flask.setHealthToHeal(flask.getHealthToHeal() + MIN_HEALTH);
            }

            // Increment Flask of Crimson Tears potency by one after use.
            flask.setPotency(flask.getPotency() + 1);
            // Return the result of the upgrade.
            result += String.format("%s upgrades %s potency to %d. Will now restore %d health when used", actor, flask, flask.getPotency(), flask.getHealthToHeal());

            // Remove the ConsumeAction from the allowableActions list if the list is not empty.
            if (!this.getAllowableActions().isEmpty()) {
                this.removeAction(this.getAllowableActions().get(0));
            }
            // Remove the Sacred Tears from the Player's inventory once consumed.
            actor.removeItemFromInventory(this);
        }
        return result;
    }

    /**
     * Inform a carried SacredTears of the passage of time.
     * This method is called once per turn, if the SacredTears is being carried.
     * Creates a ConsumeAction and add that to the list of Actions for the SacredTears.
     * SacredTear can be "consumed" to upgrade Flask of Crimson Tears potency.
     * @param currentLocation The location of the actor carrying this Item.
     * @param actor The actor carrying this Item.
     */
    @Override
    public void tick(Location currentLocation, Actor actor) {
        // If the allowableActions list is Empty and if the Actor carrying it is still alive and the Item is consumable,
        // add a ConsumeAction into it.
        if (this.getAllowableActions().isEmpty() && !actor.hasCapability(Status.DEAD) && this.hasCapability(ItemStatus.CONSUMABLE)) {
            // Create a new ConsumeAction, representing the Player using the Sacred Tears, passing the SacredTears
            // into the constructor.
            this.addAction(new ConsumeAction(this));
        }
    }
}
