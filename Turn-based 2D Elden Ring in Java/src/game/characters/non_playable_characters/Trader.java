package game.characters.non_playable_characters;

import edu.monash.fit2099.engine.actions.Action;
import edu.monash.fit2099.engine.actions.ActionList;
import edu.monash.fit2099.engine.actions.DoNothingAction;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.displays.Display;
import edu.monash.fit2099.engine.positions.GameMap;
import game.actions.BuyingAction;
import game.utils.Status;
import game.weapons.Buyable;

import java.util.HashMap;
import java.util.Map;

/**
 * A class that represents the Trader. Inherits from Actor.
 * Created by:
 * @author Bryan Wong
 * Modified by:
 * @author Po Han Tay
 * @version 1.0
 * @see Action
 * @see ActionList
 * @see DoNothingAction
 * @see Actor
 * @see Display
 * @see GameMap
 * @see BuyingAction
 * @see Status
 * @see Buyable
 */

public abstract class Trader extends Actor {
    /**
     * A HashMap of Integer and Buyables that the Trader has and Player can buy from.
     */
    private Map<Integer, Buyable> buyables;

    /**
     * Trader Constructor.
     * Call Actor parent constructor to set the name, displayChar and hitpoints.
     *
     * @param name: Name to call the player in the UI
     * @param displayChar: Character to represent the player in the UI
     * @param hitPoints: Trader's starting number of hitpoints
     */
    public Trader(String name, char displayChar, int hitPoints) {
        // Call parent constructor to set inherited data attributes.
        super(name, displayChar, hitPoints);

        // Create an empty HashMap for buyables and pass it to the setter.
        Map<Integer, Buyable> buyables = new HashMap<>();
        setBuyables(buyables);
        // Add a TRADEABLE capability to signify this Actor is TRADEABLE.
        this.addCapability(Status.TRADEABLE);
    }

    /**
     * A method to return the buyables data attribute.
     * @return the buyables HashMaps which shows the number of buyables to be bought.
     */
    public Map<Integer, Buyable> getBuyables() {
        return buyables;
    }

    /**
     * A method to set the buyables data attribute.
     * @param buyables: The buyables HashMap that contains all the buyables that can be bought by Player.
     */
    public void setBuyables(Map<Integer, Buyable> buyables) {
        this.buyables = buyables;
    }

    /**
     * At each turn, Trader does literally nothing.
     *
     * @param actions: Collection of possible Actions for this Trader.
     * @param lastAction: The Action this Trader took last turn. Can do interesting things in conjunction with Action.getNextAction().
     * @param map: The map containing the Trader.
     * @param display: The I/O object to which messages may be written.
     * @return the valid action that can be performed in that iteration or null if no valid action is found.
     */
    @Override
    public Action playTurn(ActionList actions, Action lastAction, GameMap map, Display display) {
        return new DoNothingAction();
    }

    /**
     * Will contain the lists of Actions allowable for Player to perform on the Trader. In this case it will be TradingAction.
     *
     * @param otherActor: The Actor that might be performing action,
     * @param direction: String representing the direction of the other Actor.
     * @param map: current GameMap.
     * @return the list of allowable actions that can be performed by the other Actor to this Trader.
     */
    @Override
    public ActionList allowableActions(Actor otherActor, String direction, GameMap map) {
        // Create an ActionList containing a list of allowable actions to be performed on the Trader.
        ActionList actions = new ActionList();

        // If the other actor has capability TRADEABLE, create a TradingAction between the Trader and the Actor.
        if (otherActor.hasCapability(Status.TRADEABLE)) {
            // Loops through Trader Weapon Inventory and create BuyingActions for all of them.
            for (Buyable traderWeapon : buyables.values()) {
                actions.add(new BuyingAction(traderWeapon));
            }
        }
        return actions;
    }
}
