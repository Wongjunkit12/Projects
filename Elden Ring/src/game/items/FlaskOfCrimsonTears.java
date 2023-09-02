package game.items;

import edu.monash.fit2099.engine.actions.Action;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.items.Item;
import edu.monash.fit2099.engine.positions.GameMap;
import edu.monash.fit2099.engine.positions.Location;
import game.actions.ConsumeAction;
import game.utils.ResetManager;
import game.utils.Resettable;
import game.utils.Status;
import game.utils.Utils;

/**
 * A class FlaskOfCrimsonTears that inherits from abstract class.
 * Implements Consumable as can be consumed to heal 250 health initially.
 * Implements Resettable as it reset its uses back to 2.
 * Created by:
 * @author Bryan Wong
 * Modified by:
 * @author Po Han Tay
 * @version 1.0
 * @see Action
 * @see Actor
 * @see Item
 * @see GameMap
 * @see Location
 * @see ConsumeAction
 * @see ResetManager
 * @see Resettable
 * @see Status
 * @see Utils
 */

public class FlaskOfCrimsonTears extends Item implements Consumable, Resettable {
    /**
     * An integer representing the amount of uses the Flask Of Crimson Tears has.
     */
    private int uses;
    /**
     * An integer representing the maximum amount of uses the Flask Of Crimson Tears has.
     */
    private int maxUses;
    /**
     * An integer representing the amount of Health to heal the Player when consumed.
     */
    private int healthToHeal;
    /**
     * An integer representing the potency of the Flask of Crimson Tears. Can be upgraded using a Sacred Tears.
     */
    private int potency;

    /**
     * FlaskOfCrimsonTears Constructor.
     * Will call the Item parent constructor to set its respective data attributes.\
     * The displayChar will be set to ':' as a Placeholder as the Flask Of Crimson Tears cannot be dropped, so thus have
     * no display on the map.
     * Will set portable to false as the Flask cannot be dropped or picked up.
     * @param uses: The number of uses the Flask Of Crimson Tears has.
     * @throws Exception if failed to set the number of uses.
     */
    public FlaskOfCrimsonTears(int uses) throws Exception {
        super("Flask of Crimson Tears", ':', false);
        setUses(uses);          // Set the number of uses of the FlaskOfCrimsonTears.
        setHealthToHeal(250);   // Set the default amount of Health to heal to 250 initially.
        setPotency(0);          // Set potency of Flask of Crimson Tears to be 0 initially.

        // Set the maximum number of  uses of the FlaskOfCrimsonTears. If return false, means the input is not valid, throw an Exception.
        if (setMaxUses(uses)) {
            setMaxUses(uses);
        }
        else {
            throw new Exception("Invalid Maximum Number of Uses. Number must be more than or equal to 0.");
        }

        // Flask Of Crimson Tears can heal the Player.
        this.addCapability(ItemStatus.HEALTH_POTION);
        // Add the capability CONSUMABLE to signify that this item can be consumed by the Player.
        this.addCapability(ItemStatus.CONSUMABLE);

        // Register this Flask Of Crimson Tears as a RestResettable and add it to the resettables ArrayList,
        // Will reset when Player dies or rests.
        ResetManager.getInstance().registerRestResettable(this);
        ResetManager.getInstance().registerDeadResettable(this);

        // If the allowableActions list is Empty and the Item is consumable, add a ConsumeAction into it.
        if (this.getAllowableActions().isEmpty() && this.hasCapability(ItemStatus.CONSUMABLE)) {
            // Create a new ConsumeAction, representing the Player consuming the Flask Of Crimson Tears, passing the FlaskOfCrimsonTears
            // into the constructor.
            this.addAction(new ConsumeAction(this));
        }
    }

    /**
     * A method to return the uses data attribute.
     * @return the amount of uses the Flask Of Crimson Tears has.
     */
    public int getUses() {
        return uses;
    }

    /**
     * A method to return the maxuUses data attribute.
     * @return the maximum amount of uses the Flask Of Crimson Tears has.
     */
    public int getMaxUses() {
        return maxUses;
    }

    /**
     * A method to return the healthToHeal data attribute.
     * @return the amount of Health to heal the Player when consumed.
     */
    public int getHealthToHeal() {
        return healthToHeal;
    }

    /**
     * A method to return the potency data attribute.
     * @return the potency of the Flask of Crimson Tears.
     */
    public int getPotency() {
        return potency;
    }

    /**
     * A method to set the uses data attribute.
     * @param uses: The amount of uses the Flask Of Crimson Tears has.
     */
    public void setUses(int uses) {
        this.uses = uses;
    }

    /**
     * A method to set the maxUses data attribute.
     * @param maxUses: The maximum amount of uses the Flask Of Crimson Tears has.
     * @return true if the input is valid and the attribute is set, false if the input is invalid.
     */
    public boolean setMaxUses(int maxUses) {
        boolean isValid = false;    // Boolean variable if the input is valid or not. False by default.

        // If the uses is a positive integer, its valid so set it.
        if (Utils.verifyRange(maxUses, 0)) {
            this.maxUses = maxUses;
            isValid = true;
        }
        // If valid input, return True. Else return False.
        return isValid;
    }

    /**
     * A method to set the healthToHeal data attribute.
     * @param healthToHeal: The amount of Health to heal the Player when consumed.
     */
    public void setHealthToHeal(int healthToHeal) {
        this.healthToHeal = healthToHeal;
    }

    /**
     * A method to set the potency data attribute.
     * @param potency: The potency of the Flask of Crimson Tears.
     */
    public void setPotency(int potency) {
        this.potency = potency;
    }

    /**
     * A method which will allow the Actor to consume the Flask of Crimson Tears and heal getHealthToHeal health points.
     * @param actor: The Actor that will consume the Flask of Crimson Tears.
     * @return the result String of the Flask of Crimson Tears being consumed for getHealthToHeal.
     */
    @Override
    public String consume(Actor actor) {
        // If the Flask of Crimson Tears still have uses left, allow the Actor to consume it and heal themselves.
        if (uses > 0) {
            actor.heal(getHealthToHeal());      // Increases the Actor's health by the health to heal variable.
            setUses(getUses() - 1);             // Decrement uses by one as Actor has use it once.

            // Remove the ConsumeAction from the allowableActions list.
            this.removeAction(this.getAllowableActions().get(0));
            // Return the result of the Action.
            return String.format("%s consumes the %s and heals %d health", actor, this, getHealthToHeal());
        }
        return String.format("%s cannot consume %s", actor, this);
    }

    /**
     * Inform a carried FlaskOfCrimsonTears of the passage of time.
     * This method is called once per turn, if the FlaskOfCrimsonTears is being carried.
     * Creates a ConsumeAction and add that to the list of Actions for the FlaskOfCrimsonTears.
     * @param currentLocation The location of the actor carrying this Item.
     * @param actor The actor carrying this Item.
     */
    @Override
    public void tick(Location currentLocation, Actor actor) {
        // If the allowableActions list is Empty and if there are enough uses for it and the Item is consumable, add a ConsumeAction into it.
        if (this.getAllowableActions().isEmpty() && this.getUses() > 0 && !actor.hasCapability(Status.DEAD) && this.hasCapability(ItemStatus.CONSUMABLE)) {
            // Create a new ConsumeAction, representing the Player consuming the Flask Of Crimson Tears, passing the FlaskOfCrimsonTears
            // into the constructor.
            this.addAction(new ConsumeAction(this));
        }
    }

    /**
     * A method which will reset the Flask Of Crimson Tears uses back to maximum whenever the Player rests or dies.
     * @param map: The Game Map.
     * @param location: THe location of the Last Site of Lost Grace the Player rested at.
     */
    @Override
    public void reset(GameMap map, Location location) {
        setUses(getMaxUses());      // Set the uses back to the maxUses.

        // If the number of uses is 0, remove the ConsumeAction for the Flask of Crimson Tears.
        if (getUses() == 0) {
            this.removeAction(this.getAllowableActions().get(0));
        }
    }
}
