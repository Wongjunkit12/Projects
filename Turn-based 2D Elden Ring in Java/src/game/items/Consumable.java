package game.items;

import edu.monash.fit2099.engine.actors.Actor;

/**
 * A Consumable interface.
 * Created by:
 * @author Bryan Wong
 * Modified by:
 *
 * @version 1.0
 * @see Actor
 */

public interface Consumable {
    /**
     * A method which will allow the Actor to consume the Item.
     * @param actor: The Actor that will consume the item.
     * @return the result String of what was consumed.
     */
    String consume(Actor actor);
}
