package game.utils;

import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.positions.GameMap;

/**
 * A Interactable interface that enforces a .interact() method for things that allow Player to interact with other Interactables.
 * Created by:
 * @author Bryan Wong
 * Modified by:
 *
 * @version 1.0
 * @see Actor
 * @see GameMap
 */

public interface Interactable {
    /**
     * A method that will allow the Player to interact with this Interactable.
     * @param map: The GameMap the interacting is being done on.
     * @param actor: The Actor interacting with the Interactable.
     * @return the result of the interaction.
     */
    String interact(GameMap map, Actor actor);

    /**
     * A method to return the description of the InteractAction.
     * @return the description of what happen during the InteractAction.
     */
    String getInteractDescription();
}
