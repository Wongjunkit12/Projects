package game.actions;

import edu.monash.fit2099.engine.actions.Action;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.positions.GameMap;

/**
 * A class that represents a Despawning Action in which a Despawnable will despawn.
 * Created by:
 * @author Bryan Wong
 * Modified by:
 * @author Po Han Tay
 * @version 1.0
 * @see Action
 * @see Actor
 * @see GameMap
 */

public class DespawnAction extends Action {
    /**
     * When executed, remove the Actor from the map as the Despawnable has despawned.
     *
     * @param actor The actor performing the despawning action.
     * @param map The map the actor is on.
     * @return the result of the despawning action. Whether it despawn or not.
     */
    @Override
    public String execute(Actor actor, GameMap map) {
        map.removeActor(actor);     // Remove the Despawnable from the map.

        return String.format("%s despawns", actor);    // Return the despawning result when Actor despawns.
    }

    /**
     * Describes which actor is despawning.
     *
     * @param actor: The actor performing the action.
     * @return a description used for the menu UI.
     */
    @Override
    public String menuDescription(Actor actor) {
        return String.format("%s despawns", actor);
    }
}
