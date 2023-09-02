package game.utils;

import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.positions.GameMap;

/**
 * A Travelable interface that enforces a .travel() method for things that allow Player to travel to different Game Maps.
 * Created by:
 * @author Bryan Wong
 * Modified by:
 *
 * @version 1.0
 * @see Actor
 * @see GameMap
 */

public interface Travelable {
    /**
     * A method that will allow the Player to travel to another GameMap using a Travelable object.
     * @param map: The GameMap that the Player is travelling from.
     * @param actor: The Actor travelling through GameMaps using the travelable.
     * @return the result of the travelling.
     */
    String travel(GameMap map, Actor actor);

    /**
     * A method to return the destination of the Travelable.
     * @return the name of the destination.
     */
    String getDestination();
}
