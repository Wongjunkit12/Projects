package game.environments;

import edu.monash.fit2099.engine.actions.ActionList;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.positions.GameMap;
import edu.monash.fit2099.engine.positions.Ground;
import edu.monash.fit2099.engine.positions.Location;
import game.actions.TravelMapAction;
import game.utils.Status;
import game.utils.Travelable;

/**
 * GoldenFogDoor which inherits from Ground and implements Travelable as it can be used "travel" to other GameMaps.
 * Created by:
 * @author Wah Tan
 * Modified by:
 * @author Bryan Wong
 * @version 1.0
 * @see ActionList
 * @see Actor
 * @see GameMap
 * @see Ground
 * @see Location
 * @see TravelMapAction
 * @see Status
 * @see Travelable
 */

public class GoldenFogDoor extends Ground implements Travelable {
    /**
     * The current Location of the Golden Fog Door.
     */
    private Location currentLocation = null;
    /**
     * The name of the GameMap this Golden Fog Door goes to.
     */
    private String mapName;
    /**
     * The GameMap instance the Player can travel to.
     */
    private GameMap gameMapDestination;
    /**
     * The Location this Golden Fog Door is connected to.
     */
    private Location goldenDoorDestination;

    /**
     * GoldenFogDoor Constructor.
     * Calls parent class to set inherited data attributes.
     * Adds the CAN_TRAVEL capability to signify this Golden Fog Door allows the Player to travel to another map.
     * @param mapName: The name of the GameMap this Golden Fog Door goes to.
     * @param gameMapDestination: The GameMap instance the Player can travel to.
     */
    public GoldenFogDoor(String mapName, GameMap gameMapDestination) {
        // Call parent constructor to set display character and their respective destination name and GameMap instance.
        super('D');
        setMapName(mapName);
        setGameMapDestination(gameMapDestination);

        // Adds CAN_TRAVEL capability to signify this Golden Fog Door can travel to another map.
        this.addCapability(GroundStatus.CAN_TRAVEL);
    }

    /**
     * A method to return the currentLocation data attribute.
     * @return the current Location of the Golden Fog Door.
     */
    public Location getCurrentLocation() {
        return currentLocation;
    }

    /**
     * A method to return the mapName data attribute.
     * @return the name of the GameMap this Golden Fog Door goes to.
     */
    public String getMapName() {
        return mapName;
    }

    /**
     * A method to return the gameMapDestination data attribute.
     * @return the GameMap instance the Player can travel to.
     */
    public GameMap getGameMapDestination() {
        return gameMapDestination;
    }

    /**
     * A method to return the goldenDoorDestination data attribute.
     * @return the Location this Golden Fog Door is connected to.
     */
    public Location getGoldenDoorDestination() {
        return goldenDoorDestination;
    }

    /**
     * A method to return the destination of the Golden Fog Door.
     * @return the mapName of the Golden Fog Door.
     */
    @Override
    public String getDestination() {
        return getMapName();
    }

    /**
     * A method to set the currentLocation data attribute.
     * @param currentLocation: The current Location of the Golden Fog Door.
     */
    public void setCurrentLocation(Location currentLocation) {
        this.currentLocation = currentLocation;
    }

    /**
     * A method to set the mapName data attribute.
     * @param mapName: The name of the GameMap this Golden Fog Door goes to.
     */
    public void setMapName(String mapName) {
        this.mapName = mapName;
    }

    /**
     * A method to set the gameMapDestination data attribute.
     * @param gameMapDestination: The GameMap instance the Player can travel to.
     */
    public void setGameMapDestination(GameMap gameMapDestination) {
        this.gameMapDestination = gameMapDestination;
    }

    /**
     * A method to set the goldenDoorDestination data attribute.
     * @param goldenDoorDestination: The Location this Golden Fog Door is connected to.
     */
    public void setGoldenDoorDestination(Location goldenDoorDestination) {
        this.goldenDoorDestination = goldenDoorDestination;
    }

    /**
     * A method to determine if the inputted Actor can enter into it. Only Player can enter and use the Golden Fog Door.
     * @param actor: The Actor trying to enter the Golden Fog Door.
     * @return true if Actor can enter and false otherwise.
     */
    @Override
    public boolean canActorEnter(Actor actor) {
        return actor.hasCapability(Status.CAN_FAST_TRAVEL);
    }

    /**
     * Will contain the lists of Actions allowable for Player to perform on the Golden Fog Door.
     *
     * @param actor: The Actor that is on the Golden Fog Door.
     * @param location: The Location of the Golden Fog Door.
     * @param direction: String representing the direction of the Actor.
     * @return the list of allowable actions that can be performed by the Actor on the Golden Fog Door.
     */
    @Override
    public ActionList allowableActions(Actor actor, Location location, String direction) {
        // Create an ActionList containing a list of allowable actions to be performed by the Player on the Golden Fog Door.
        ActionList actions = new ActionList();

        // If the other Actor is able to travel and is currently on the Golden Fog Door, create a TravelMapAction.
        if (actor.hasCapability(Status.CAN_FAST_TRAVEL)) {
            // Create a new TravelMapAction and pass the currentLocation of the Door to TravelMapAction.
            actions.add(new TravelMapAction(this));
        }
        return actions;
    }

    /**
     * If the Location of the GoldenFogDoor has not been set yet, set it.
     * @param location The location of the GoldenFogDoor.
     */
    @Override
    public void tick(Location location) {
        // If the currentLocation of the GoldenFogDoor has not been set yet, set it.
        if (getCurrentLocation() == null) {
            setCurrentLocation(location);   // Set the current location to GoldenFogDoor.
        }
    }

    /**
     * A method that will allow the Player to travel to another GameMap using the Golden Fog Door.
     * @param map: The GameMap that the Player is travelling from.
     * @param actor: The Actor travelling through GameMaps using the Golden Fog Door.
     * @return the result of the travelling.
     */
    @Override
    public String travel(GameMap map, Actor actor) {
        // Move the Actor to their destination Location.
        this.getGameMapDestination().moveActor(actor, this.getGoldenDoorDestination());
        // Return the result of this travel action.
        return String.format("%s travels to %s", actor, this.getMapName());
    }
}
