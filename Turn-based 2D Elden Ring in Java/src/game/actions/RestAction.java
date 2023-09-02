package game.actions;

import edu.monash.fit2099.engine.actions.Action;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.positions.GameMap;
import game.environments.SiteOfLostGrace;
import game.utils.ResetManager;

/**
 * A class that represents a Resting Action when the Player rests on the Site of Lost Grace.
 * Created by:
 * @author Bryan Wong
 * Modified by:
 *
 * @version 1.0
 * @see Action
 * @see Actor
 * @see GameMap
 * @see SiteOfLostGrace
 * @see ResetManager
 */

public class RestAction extends Action {
    /**
     * The SiteOfLostGrace object the Player is resting at.
     */
    private SiteOfLostGrace siteOfLostGrace;

    /**
     * Constructor for RestAction.
     * Saves the Site Of Lost Grace method.
     * @param siteOfLostGrace: The SiteOfLostGrace object which is being rested on.
     */
    public RestAction(SiteOfLostGrace siteOfLostGrace) {
        setSiteOfLostGrace(siteOfLostGrace);    // Save the Site of Lost Grace the Player is resting at.
    }

    /**
     * A method to return the siteOfLostGrace data attribute.
     * @return the SiteOfLostGrace object the Player is resting at.
     */
    public SiteOfLostGrace getSiteOfLostGrace() {
        return siteOfLostGrace;
    }

    /**
     * A method to set the siteOfLostGrace data attribute.
     * @param siteOfLostGrace: The SiteOfLostGrace object the Player is resting at.
     */
    public void setSiteOfLostGrace(SiteOfLostGrace siteOfLostGrace) {
        this.siteOfLostGrace = siteOfLostGrace;
    }

    /**
     * When executed, call the ResetManager .run() method to reset all Resettables.
     *
     * @param actor The actor performing the resting action.
     * @param map The map the actor is on.
     * @return the result of the RestAction.
     */
    @Override
    public String execute(Actor actor, GameMap map) {
        ResetManager.getInstance().setGameMap(map);     // Sets the GameMap to be used in Resetting map objects.

        ResetManager.getInstance().runRestResettables();   // Call the runRestResettables() method to reset all Resettables,

        // Saves the Site of Lost Grace rested as the last once.
        ResetManager.getInstance().setLastLostGrace(getSiteOfLostGrace());

        return menuDescription(actor);  // Return the action menu string, which is the Player resting.
    }

    /**
     * Describes the Player resting on the Site of Lost Grace.
     *
     * @param actor: The actor performing the action.
     * @return a description used for the menu UI.
     */
    @Override
    public String menuDescription(Actor actor) {
        return String.format("%s rests on %s", actor, getSiteOfLostGrace());
    }
}
