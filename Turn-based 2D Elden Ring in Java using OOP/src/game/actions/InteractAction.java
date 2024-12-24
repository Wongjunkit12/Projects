package game.actions;

import edu.monash.fit2099.engine.actions.Action;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.positions.GameMap;
import game.utils.Interactable;

/**
 * A class that represents an Interact Action when the Player activates a Site of Lost Grace.
 * Created by:
 * @author Bryan Wong
 * Modified by:
 * @author Po Han Tay
 * @version 1.0
 * @see Action
 * @see Actor
 * @see GameMap
 * @see Interactable
 */

public class InteractAction extends Action {
    /**
     * An Interactable that can be interacted with.
     */
    private final Interactable INTERACTABLE;

    /**
     * Constructor for InteractAction.
     * @param interactable: The Interactable that can be interacted with.
     */
    public InteractAction(Interactable interactable) {
        this.INTERACTABLE = interactable;
    }

    /**
     * A method to return the INTERACTABLE data attribute.
     * @return the Interactable that can be interacted with.
     */
    public Interactable getINTERACTABLE() {
        return INTERACTABLE;
    }

    /**
     * When executed, activate the Site of Lost Grace
     * @param actor The actor performing the consuming action.
     * @param map The map the actor is on.
     * @return the result of the ConsumeAction.
     */
    @Override
    public String execute(Actor actor, GameMap map) {
        String result = "";

        // Call the interact method in the Interactable to allow the Actor to interact with it.
        result += getINTERACTABLE().interact(map, actor);
        // Return the result of the InteractAction.
        return result;
    }

    /**
     * Describes what Interactable was interacted with by the Actor.
     * @param actor: The actor performing the action.
     * @return a description used for the menu UI.
     */
    @Override
    public String menuDescription(Actor actor) {
        return String.format("%s %s", actor, getINTERACTABLE().getInteractDescription());
    }
}
