package game.utils;

import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.displays.Display;
import edu.monash.fit2099.engine.positions.Location;
import game.items.Runes;

/**
 * A RuneManager class which modifies and manages the Runes.
 * In this case it will be used to add or subtract the runeCount of the Runes object.
 * Created by:
 * @author Bryan Wong
 * Modified by:
 *
 * @version 1.0
 * @see Actor
 * @see Location
 * @see Runes
 */

public class RuneManager {
    /**
     * The Runes object to manage.
     */
    private Runes runes;
    /**
     * The past Location of the Runes.
     */
    private Location pastLocation;

    /**
     * An instance of itself so that only one RuneManager instance is created. Default set to null.
     */
    private static RuneManager runeManager = null;

    /**
     * RuneManager Constructor for actor.
     *
     * @param actor: The Actor which has Runes to manage.
     */
    public RuneManager(Actor actor) {
        updateRunes(actor);
    }

    /**
     * Overloading RuneManager Constructor for Runes object.
     *
     * @param runes: The Actor which has Runes to manage.
     */
    public RuneManager(Runes runes) {
        updateRunes(runes);
    }

    /**
     * A method to return the pastLocation data attribute.
     * @return the past Location of the Runes.
     */
    public Location getPastLocation() {
        return pastLocation;
    }

    /**
     * A method to return the runes data attribute.
     * @return the Runes object to manage.
     */
    public Runes getRunes() {
        return runes;
    }

    /**
     * A method to set the pastLocation data attribute.
     * @param pastLocation: The past Location of the Runes.
     */
    public void setPastLocation(Location pastLocation) {
        this.pastLocation = pastLocation;
    }

    /**
     * A method to update the runes data attribute with a new Runes object.
     * @param actor: The Actor to update the Runes in their inventory.
     */
    public void updateRunes(Actor actor) {
        // If the Actor has Runes in its inventory, update the Runes object.
        if (Utils.getRunes(actor) != null) {
            this.runes = Utils.getRunes(actor);     // Set the Runes object with the Runes in the actor's inventory.
        }
    }

    /**
     * A method to update the runes data attribute with a new Runes object.
     * @param runes: The Runes object to manage and update.
     */
    public void updateRunes(Runes runes) {
        this.runes = runes;
    }

    /**
     * Returns the instance of the runeManager stored as a data attribute used for actors.
     * If one has not been created yet, create a new instance of runeManager.
     * @param actor: The Actor which has Runes to manage.
     * @return the runeManager instance.
     */
    public static RuneManager getInstance(Actor actor) {
        // If an instance of runeManager has not been created yet.
        if (runeManager == null) {
            // Create a new instance of runeManager.
            runeManager = new RuneManager(actor);
        }
        // Else update the Runes object for the runeManager.
        else {
            runeManager.updateRunes(actor);
        }
        // Return the runeManager instance, either newly created or stored currently as a data attribute.
        return runeManager;
    }

    /**
     * Returns the instance of the runeManager stored as a data attribute used for Runes.
     * If one has not been created yet, create a new instance of runeManager.
     * @param runes: The Runes
     * @return the runeManager instance.
     */
    public static RuneManager getInstance(Runes runes) {
        // If an instance of runeManager has not been created yet.
        if (runeManager == null) {
            // Create a new instance of runeManager.
            runeManager = new RuneManager(runes);
        }
        // Else update the Runes object for the runeManager.
        else {
            runeManager.updateRunes(runes);
        }
        // Return the runeManager instance, either newly created or stored currently as a data attribute.
        return runeManager;
    }

    /**
     * A method to increase the Runes runeCount data attribute.
     * @param runeCountToAdd: An Integer representing the number of runes to add to the runeCount of the Runes object.
     */
    public void addRunes(int runeCountToAdd) {
        // Add Runes to current runeCount.
        getRunes().setRuneCount(getRunes().getRuneCount() + runeCountToAdd);
    }

    /**
     * A method to decrease the Runes runeCount data attribute.
     * @param runeCountToMinus: An Integer representing the number of runes to subtract from the runeCount of the Runes object.
     */
    public void subtractRunes(int runeCountToMinus) {
        // Subtract Runes from current runeCount.
        getRunes().setRuneCount(getRunes().getRuneCount() - runeCountToMinus);
    }

    /**
     * Transfer the amount of runes from the ground to recover.
     * @param runes: A Runes object that is on the ground.
     */
    public void gainRunes(Runes runes) {
        // Use the RuneManager addRunes method to increase the amount of Runes the actor get from picking up the Runes.
        this.addRunes(runes.getRuneCount());
    }

    /**
     * Transfer the amount of runes from the target to the attacker.
     * @param attacker: An Actor representing the attacker attacking the target.
     * @param target: An Actor representing the target being attacked by the attacker.
     */
    public void gainRunes(Actor attacker, Actor target) {
        // Get the Rune object from the attacker's inventory.
        Runes targetRunes = Utils.getRunes(target);

        // Use the RuneManager addRunes method to increase the amount of Runes the attacker get from killing the target.
        this.addRunes(targetRunes.getRuneCount());

        // Print out the amount of Runes the attacker received.
        Display display = new Display();
        display.println(String.format("%s gains %d Runes", attacker, targetRunes.getRuneCount()));
    }
}
