package game.actions;

import edu.monash.fit2099.engine.actions.Action;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.positions.GameMap;
import game.utils.Travelable;

/**
 * A class that represents a Special Attack Action.
 * Created by:
 * @author Wah Tan
 * Modified by:
 * @author Bryan Wong
 * @version 1.0
 * @see Action
 * @see Actor
 * @see GameMap
 * @see Travelable
 */

public class TravelMapAction extends Action {
    /**
     * The Travelable instance used to travel across GameMaps.
     */
    private Travelable travelable;

    /**
     * TravelMapAction Constructor for Travelable used for travelling. Sets the Travelable enacting the TravelMapAction.
     * @param travelable: The Travelable instance used to travel across GameMaps.
     */
    public TravelMapAction(Travelable travelable) {
        setTravelable(travelable);
    }

    /**
     * A method to return the travelable data attribute.
     * @return the Travelable instance used to travel across GameMaps.
     */
    public Travelable getTravelable() {
        return travelable;
    }

    /**
     * A method to set the travelable data attribute.
     * @param travelable: The Travelable instance used to travel across GameMaps.
     */
    public void setTravelable(Travelable travelable) {
        this.travelable = travelable;
    }

    /**
     * A method to execute the travel method in the Travelable object.
     * @return result of travelling to another Map.
     */
    @Override
    public String execute(Actor actor, GameMap map) {
        String result = "";     // The result of the TravelMapAction.

        // Call the Travelable .travel() method to allow the player to travel to different GameMaps.
        result += getTravelable().travel(map, actor);

        return result;          // Return the destination he travelled to.
    }

    /**
     * The menu string displayed to the Actor to which GameMap they would want to travel to.
     *
     * @param actor: The actor performing the TravelMapAction.
     * @return a description used for the menu UI.
     */
    @Override
    public String menuDescription(Actor actor) {
        // Return the description used for the menu on the Player travelling to the destination name.
        return String.format("%s travels to %s", actor, getTravelable().getDestination());
    }
}
