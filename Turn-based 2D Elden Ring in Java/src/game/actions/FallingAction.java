package game.actions;

import edu.monash.fit2099.engine.actions.Action;
import edu.monash.fit2099.engine.actions.ActionList;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.items.Item;
import edu.monash.fit2099.engine.positions.GameMap;
import edu.monash.fit2099.engine.positions.Ground;
import edu.monash.fit2099.engine.positions.Location;
import edu.monash.fit2099.engine.weapons.WeaponItem;
import game.displays.FancyMessage;
import game.environments.GroundStatus;
import game.items.ItemStatus;
import game.utils.RuneManager;
import game.utils.Status;

/**
 * An Action executed if an Actor fall and died.
 * Created by:
 * @author Bryan Wong
 * Modified by:
 *
 * @version 1.0
 * @see Action
 * @see ActionList
 * @see Actor
 * @see Item
 * @see GameMap
 * @see Ground
 * @see Location
 * @see WeaponItem
 * @see FancyMessage
 * @see GroundStatus
 * @see RuneManager
 * @see Status
 */

public class FallingAction extends Action {
    /**
     * The Ground the Actor has fallen off from.
     */
    private Ground ground;

    /**
     * FallingAction Constructor.
     * @param ground: The Ground that the Actor falls of from.
     */
    public FallingAction(Ground ground) {
        setGround(ground);
    }

    /**
     * A method to return the ground data attribute.
     * @return the Ground that the Actor falls of from.
     */
    public Ground getGround() {
        return ground;
    }

    /**
     * A method to set the ground data attribute.
     * @param ground: The Ground that the Actor falls of from.
     */
    public void setGround(Ground ground) {
        this.ground = ground;
    }

    /**
     * Is called when the target falls off a Cliff or a high ground.
     * All the Items and Weapons carried by target with the portable boolean will be dropped to the location in the game map
     * where the target was.
     *
     * @param actor: The Actor performing the action.
     * @param map: The map the Actor is on.
     * @return result of the action to be displayed on the UI.
     */
    @Override
    public String execute(Actor actor, GameMap map) {
        String result = "";     // The death message of the actor.

        ActionList dropActions = new ActionList();  // List of DropActions.

        actor.addCapability(Status.DEAD);  // Add the capability DEAD to signify actor is dead.

        // If the Ground the Actor is standing on is FALLABLE, kill the Actor.
        if (map.locationOf(actor).getGround().hasCapability(GroundStatus.FALLABLE)) {
            // If the actor is the Actor that CAN_GAIN_RUNES, pass its currentLocation and numberOfDeaths to the Rune so that it
            // will drop the Rune and reset if the Actor dies again.
            if (actor.hasCapability(Status.CAN_GAIN_RUNES)) {
                // Get the current location of the Player.
                Location currentLocation = map.locationOf(actor);
                // Get the past location of the Player.
                Location pastLocation = RuneManager.getInstance(actor).getPastLocation();

                // If the Player moved before they died, move them back to previous location so that Runes will be dropped there.
                if (!((currentLocation.x() == pastLocation.x()) && (currentLocation.y() == pastLocation.y())) &&
                        !(pastLocation.containsAnActor())) {
                    // Move the Player to its previous Location so that it drops the Runes at its previous location.
                    map.moveActor(actor, pastLocation);
                }
            }

            // Drop every single item in the victim's inventory.
            for (Item item : actor.getItemInventory()) {
                // If the Item is a Rune (has a currency) or if the actor is not the Player, drop all Items.
                if (item.hasCapability(ItemStatus.CURRENCY) || !actor.hasCapability(Status.PLAYER)) {
                    dropActions.add(item.getDropAction(actor));
                }
            }
            // Drop every single weapon in the victim's inventory when dead if they are not the Player.
            for (WeaponItem weapon : actor.getWeaponInventory()) {
                // If the actor is not the Player, drop all Weapons as Player cannot drop Weapons when dead.
                if (!actor.hasCapability(Status.PLAYER)) {
                    dropActions.add(weapon.getDropAction(actor));
                }
            }
            // Execute all DropActions stored in the dropActions ActionList and create the dropped items/weapons character in map.
            for (Action drop : dropActions) {
                drop.execute(actor, map);
            }

            // The Actor falls off from the Fallable ground.
            result += menuDescription(actor);

            // Else if target death causes the Game to Reset, reset the Game when it dies.
            if (actor.hasCapability(Status.CAN_RESET_GAME)) {
                new ResetAction().execute(actor, map);      // Execute a ResetAction.

                // YOU DIED.
                result += System.lineSeparator() + FancyMessage.YOU_DIED;
            }
        }
        return result;
    }

    /**
     * A method that displays in the menu the death message of an Actor.
     * @param actor: The Actor that has fallen off the Cliff.
     * @return the String death message of the Actor that has fallen off and from where.
     */
    @Override
    public String menuDescription(Actor actor) {
        return String.format("%s falls of the %s", actor, getGround());
    }
}
