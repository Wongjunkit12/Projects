package game.items;

import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.items.DropAction;
import edu.monash.fit2099.engine.items.Item;
import edu.monash.fit2099.engine.positions.Location;
import game.actions.ConsumeAction;
import game.utils.RandomNumberGenerator;
import game.utils.RuneManager;
import game.utils.Status;

/**
 * A class GoldenRunes that inherits from Item abstract class.
 * Implements Consumanle as they can be consumed to gain 200 - 10000 Runes.
 * Created by:
 * @author Bryan Wong
 * Modified by:
 *
 * @version 1.0
 * @see Actor
 * @see DropAction
 * @see Item
 * @see Location
 * @see ConsumeAction
 * @see RandomNumberGenerator
 * @see RuneManager
 * @see Status
 */

public class GoldenRunes extends Item implements Consumable {
    /**
     * An integer representing the amount of Runes the GoldenRunes have from a range of 200 - 10000 which will be added to
     * the Player's inventory when picked up.
     */
    private int goldenRuneCount;

    /**
     * Runes Constructor.
     * Will call the Item parent constructor to set its respective data attributes.
     * Use the RandomNumberGenerator to generate a Random Integer from 200 to 10000.
     */
    public GoldenRunes() {
        super("Golden Runes", '*', true);
        // Generate a random goldenRuneCount from 200 to 10000.
        setGoldenRuneCount(RandomNumberGenerator.getRandomInt(200,10000));
        // Add the capability CONSUMABLE to signify that this item can be consumed by the Player.
        this.addCapability(ItemStatus.CONSUMABLE);
    }

    /**
     * A method to return the goldenRuneCount data attribute.
     * @return the number of Runes the GoldenRune will give.
     */
    public int getGoldenRuneCount() {
        return goldenRuneCount;
    }

    /**
     * A method to set the goldenRuneCount data attribute.
     * @param goldenRuneCount: The number of Runes the GoldenRune will give.
     */
    public void setGoldenRuneCount(int goldenRuneCount) {
        this.goldenRuneCount = goldenRuneCount;
    }

    /**
     * Inform a carried GoldenRunes of the passage of time.
     * This method is called once per turn, if the GoldenRunes is being carried.
     * Creates a ConsumeAction and add that to the list of Actions for the Golden Runes.
     * @param currentLocation The location of the actor carrying this Item.
     * @param actor The actor carrying this Item.
     */
    @Override
    public void tick(Location currentLocation, Actor actor) {
        // If the allowableActions list is Empty and the Item is consumable, add a ConsumeAction into it.
        if (this.getAllowableActions().isEmpty() && !actor.hasCapability(Status.DEAD) && this.hasCapability(ItemStatus.CONSUMABLE)) {
            // Create a new ConsumeAction, representing the Player consuming the Golden Runes, passing the Golden Runes
            // into the constructor.
            this.addAction(new ConsumeAction(this));
        }
    }

    /**
     * A method which will allow the Actor to consume the Golden Rune and increase Player Rune's runeCount based on the
     * goldenRuneCount.
     * @param actor: The Actor that is consuming the Golden Rune.
     * @return the result String of the Golden Runes being consumed and how many Runes was gained.
     */
    @Override
    public String consume(Actor actor) {
        // Get the RuneManager instance and add the Actor Rune's based on the goldenRuneCount.
        RuneManager.getInstance(actor).addRunes(getGoldenRuneCount());

        // If the allowableActions list is empty.
        if (!this.getAllowableActions().isEmpty()) {
            // Remove the ConsumeAction from the allowableActions once consumed.
            this.removeAction(this.getAllowableActions().get(0));
        }

        // Remove the Golden Runes from the Player's inventory once consumed.
        actor.removeItemFromInventory(this);

        return String.format("%s consumes the %s and gains %d Runes", actor, this, getGoldenRuneCount());
    }
}
