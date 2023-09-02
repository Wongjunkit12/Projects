package game.actions;

import edu.monash.fit2099.engine.actions.Action;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.positions.GameMap;
import game.items.Consumable;

/**
 * A class that represents a Consume Action when the Player consumes a Consumable Item.
 * Created by:
 * @author Bryan Wong
 * Modified by:
 *
 * @version 1.0
 * @see Action
 * @see Actor
 * @see GameMap
 * @see Consumable
 */

public class ConsumeAction extends Action {
    /**
     * A Consumable that will be consumed by the Player when this Action is executed.
     */
    private final Consumable CONSUMABLE;

    /**
     * Constructor for ConsumeAction.
     * @param consumable: The Consumable that the Player consumes when this action is executed.
     */
    public ConsumeAction(Consumable consumable) {
        this.CONSUMABLE = consumable;
    }

    /**
     * A method to return the CONSUMABLE data attribute.
     * @return the Consumable object.
     */
    public Consumable getCONSUMABLE() {
        return CONSUMABLE;
    }

    /**
     * When executed, call the Consumable .consume() method to simulate the Player consuming the Consumable.
     * @param actor The actor performing the consuming action.
     * @param map The map the actor is on.
     * @return the result of the ConsumeAction.
     */
    @Override
    public String execute(Actor actor, GameMap map) {
        String result = "";     // The result return String.

        // Calls the .consume() method the Consumable has and pass the actor performing the Action into it.
        result += getCONSUMABLE().consume(actor);

        return result;          // Return the action menu string of what was consumed and by who.
    }

    /**
     * Describes what item is consumed by the Player.
     *
     * @param actor: The actor performing the action.
     * @return a description used for the menu UI.
     */
    @Override
    public String menuDescription(Actor actor) {
        return String.format("%s consumes the %s", actor, CONSUMABLE.toString());
    }
}
