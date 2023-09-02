package game.items;

import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.items.DropAction;
import edu.monash.fit2099.engine.items.DropItemAction;
import edu.monash.fit2099.engine.items.Item;
import edu.monash.fit2099.engine.items.PickUpAction;
import edu.monash.fit2099.engine.positions.GameMap;
import edu.monash.fit2099.engine.positions.Location;
import game.actions.RecoverRunesAction;
import game.utils.Resettable;
import game.utils.Status;

/**
 * A class Runes that inherits from Item abstract class.
 * Created by:
 * @author Bryan Wong
 * Modified by:
 *
 * @version 1.0
 * @see Actor
 * @see DropAction
 * @see DropItemAction
 * @see Item
 * @see PickUpAction
 * @see GameMap
 * @see Location
 * @see RecoverRunesAction
 * @see Resettable
 * @see Status
 */

public class Runes extends Item implements Resettable {
    /**
     * An integer representing the amount of Runes the Actor has in its inventory.
     */
    private int runeCount;
    /**
     * The number of times the Player died.
     */
    private int numberOfDeaths;
    /**
     * The current Location of the Runes.
     */
    private Location currentLocation;

    /**
     * Runes Constructor.
     * Will call the Item parent constructor to set its respective data attributes.
     * @param runeCount: The number of Runes the Actor has in its inventory.
     */
    public Runes(int runeCount) {
        // Call the parent constructor to set the Item attributes then set the runeCount.
        super("Runes", '$', true);
        setRuneCount(runeCount);
        setNumberOfDeaths(0);   // Default set Number of Deaths to 0.

        // Runes is the currency of this world.
        this.addCapability(ItemStatus.CURRENCY);
    }

    /**
     * A method to return the runeCount data attribute.
     * @return the number of runes the Actor has.
     */
    public int getRuneCount() {
        return runeCount;
    }

    /**
     * A method to return the numberOfDeaths data attribute.
     * @return the number of times the Player died.
     */
    public int getNumberOfDeaths() {
        return numberOfDeaths;
    }

    /**
     * A method to return the currentLocation data attribute.
     * @return the current Location of the Runes.
     */
    public Location getCurrentLocation() {
        return currentLocation;
    }

    /**
     * A method to set the runeCount data attribute.
     * @param runeCount: An Integer representing the number of runes the Actor has.
     */
    public void setRuneCount(int runeCount) {
        this.runeCount = runeCount;
    }

    /**
     * A method to set the numberOfDeaths data attribute.
     * @param numberOfDeaths: The number of times the Player died.
     */
    public void setNumberOfDeaths(int numberOfDeaths) {
        this.numberOfDeaths = numberOfDeaths;
    }

    /**
     * A method to set the currentLocation data attribute.
     * @param currentLocation: The current Location of the Runes.
     */
    public void setCurrentLocation(Location currentLocation) {
        this.currentLocation = currentLocation;
    }

    /**
     * Inform the Rune carried by the actor of the passage of time.
     * Updates the currentLocation of the Runes each turn.
     * This method is called once per turn, if the Item is being carried.
     * @param currentLocation: The Location of the actor carrying this Item.
     * @param actor: The actor carrying this Item.
     */
    @Override
    public void tick(Location currentLocation, Actor actor) {
        // Set the Runes current location to the currentLocation of the actor carrying it.
        setCurrentLocation(currentLocation);
    }

    /**
     * Inform the Rune on the ground of the passage of time.
     * Updates the current Location of the Runes.
     * @param currentLocation: The location of the ground on which we lie.
     */
    @Override
    public void tick(Location currentLocation) {
        // Set the Runes current location to the currentLocation of the actor carrying it.
        setCurrentLocation(currentLocation);
    }

    /**
     * Create and return an action to drop this Rune when Actor dies.
     * If this Rune is not portable or Actor is alive, returns null.
     * @return a new DropItemAction if this Rune is droppable and Actor is dead, null otherwise.
     */
    @Override
    public DropAction getDropAction(Actor actor) {
        // If the Actor is dead and he can gain Runes and the Rune is portable, drop the Rune.
        if (actor.hasCapability(Status.CAN_GAIN_RUNES) && actor.hasCapability(Status.DEAD) && this.portable) {
            return new DropItemAction(this);
        }
        // Else do not drop the Rune.
        return null;
    }

    /**
     * Create and return an action to pick this Rune up.
     * @return RecoverRunesAction if the Actor picking it up is one with the CAN_GAIN_RUNES capability, null otherwise.
     */
    @Override
    public PickUpAction getPickUpAction(Actor actor) {
        if (currentLocation.getActor().hasCapability(Status.CAN_GAIN_RUNES)) {
            return new RecoverRunesAction(this);
        }
        // Else return null
        return null;
    }

    /**
     * A method which will reset the Runes only if the Player dies again without picking up the Rune.
     * @param map: The Game Map.
     * @param location: THe location of the Last Site of Lost Grace the Player rested at.
     */
    @Override
    public void reset(GameMap map, Location location) {
        // Increment number of deaths of Player by 1.
        this.setNumberOfDeaths(getNumberOfDeaths() + 1);

        // If the Player has died twice without picking up the Runes, remove it from the map.
        if (getNumberOfDeaths() > 1) {
            getCurrentLocation().removeItem(this);  // Remove the Item from the map.
        }
    }
}