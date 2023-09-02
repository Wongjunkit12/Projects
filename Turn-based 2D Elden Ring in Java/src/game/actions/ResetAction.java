package game.actions;


import edu.monash.fit2099.engine.actions.Action;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.positions.GameMap;
import game.utils.ResetManager;

/**
 * A class that represents a Reseting Action when the Player dies.
 * Created by:
 * @author Bryan Wong
 * Modified by:
 *
 * @version 1.0
 * @see Action
 * @see Actor
 * @see GameMap
 * @see ResetManager
 */

public class ResetAction extends Action {
    /**
     * When executed, call the ResetManager .run() method to reset all Resettables.
     *
     * @param actor The actor performing the resting action.
     * @param map The map the actor is on.
     * @return the result of the ResetAction.
     */
    @Override
    public String execute(Actor actor, GameMap map) {
        ResetManager.getInstance().setGameMap(map);     // Sets the GameMap to be used in Resetting map objects.

        ResetManager.getInstance().runDeadResettables();   // Call the runDeadResettables() method to reset all Resettables,

        return menuDescription(actor);  // Return the action menu string, which is the Player dying.
    }

    /**
     * Describes the Player reseting/dying.
     *
     * @param actor: The actor performing the action.
     * @return a description used for the menu UI.
     */
    @Override
    public String menuDescription(Actor actor) {
        return String.format("%s resets the game.", actor);
    }
}