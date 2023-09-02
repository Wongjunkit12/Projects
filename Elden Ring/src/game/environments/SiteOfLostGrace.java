package game.environments;

import edu.monash.fit2099.engine.actions.ActionList;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.positions.GameMap;
import edu.monash.fit2099.engine.positions.Ground;
import edu.monash.fit2099.engine.positions.Location;
import game.actions.InteractAction;
import game.actions.BuyingAction;
import game.actions.RestAction;
import game.actions.TravelMapAction;
import game.displays.FancyMessage;
import game.maps.MapManager;
import game.utils.*;
import game.weapons.Buyable;

/**
 * A class that inherits from Ground. The Player can rest here to reset the Game.
 * Implements Interactable as they can be interacted (activated) by the Player,
 * Implements Travelable as they can be used to Fast Travel to other Site of Lost Grace.
 * Implements Buyable as they can be used to upgrade maximum health.
 * Created by:
 * @author Bryan Wong
 * Modified by:
 *
 * @version 1.0
 * @see ActionList
 * @see Actor
 * @see GameMap
 * @see Ground
 * @see Location
 * @see InteractAction
 * @see BuyingAction
 * @see RestAction
 * @see TravelMapAction
 * @see FancyMessage
 * @see MapManager
 * @see ResetManager
 * @see RuneManager
 * @see Status
 * @see Interactable
 * @see Travelable
 * @see Buyable
 */

public class SiteOfLostGrace extends Ground implements Interactable, Travelable, Buyable {
    /**
     * The name of the Site of Lost Grace.
     */
    private String name;
    /**
     * The current Location of the Site Of Lost Grace.
     */
    private Location currentLocation = null;

    /**
     * SiteOfLostGrace Constructor.
     * Will call setters to set data attributes of SiteOfLostGrace using parameters given.
     * Calls parent class to set inherited data attributes.
     * @param name: The name of the Site of Lost Grace.
     */
    public SiteOfLostGrace(String name) {
        super('U');     // Set the display character of the Ground.
        setName(name);          // Set the Name of the Site of Lost Grace.
    }

    /**
     * A method to return the name data attribute.
     * @return the name of the Site of Lost Grace.
     */
    public String getName() {
        return name;
    }

    /**
     * A method to return the currentLocation data attribute.
     * @return the current Location of the Site of Lost Grace.
     */
    public Location getCurrentLocation() {
        return currentLocation;
    }

    /**
     * A method to return the destination of the Site of Lost Grace.
     * @return the name of the Site of Lost Grace.
     */
    @Override
    public String getDestination() {
        return getName();
    }

    /**
     * A method to return the upgradePrice data attribute.
     * Gets it from the ResetManager.
     * @return the price of upgrading the Player's max HP.
     */
    @Override
    public int getBuyingPrice() {
        // Return the ResetManager upgradePrice.
        return ResetManager.getInstance().getUpgradePrice();
    }

    /**
     * A method to return the description of the InteractAction.
     * @return the Player activating the Site of Lost Grace.
     */
    @Override
    public String getInteractDescription() {
        return String.format("activates %s", this);
    }

    /**
     * A method to return the Menu Description of the BuyingAction.
     * @return how much can be spent to upgrade the maximum HP.
     */
    @Override
    public String getMenuDescription() {
        return String.format("upgrades maximum HP for %d Runes", this.getBuyingPrice());
    }

    /**
     * A method to set the name data attribute.
     * @param name: The name of the Site of Lost Grace.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * A method to set the currentLocation data attribute.
     * @param currentLocation: The current Location of the Site of Lost Grace.
     */
    public void setCurrentLocation(Location currentLocation) {
        this.currentLocation = currentLocation;
    }

    /**
     * A method to set the upgradePrice data attribute.
     * Sets it in the ResetManager.
     * @param buyingPrice: The price of upgrading the Player's max HP.
     */
    @Override
    public void setBuyingPrice(int buyingPrice) {
        // Increment te ResetManager upgradePrice with the buyingPrice inputted.
        ResetManager.getInstance().setUpgradePrice(getBuyingPrice() + buyingPrice);
    }

    /**
     * Will contain the lists of Actions allowable for Player to perform on the Site of Lost Grace.
     *
     * @param actor: The Actor that is on the Site of Lost Grace.
     * @param location: The Location of the Site of Lost Grace.
     * @param direction: String representing the direction of the Actor.
     * @return the list of allowable actions that can be performed by the Actor on the Site of Lost Grace.
     */
    @Override
    public ActionList allowableActions(Actor actor, Location location, String direction) {
        // Create an ActionList containing a list of allowable actions to be performed by the Player on the Site of Lost Grace.
        ActionList actions = new ActionList();

        // If the Site of Lost Grace has not been activated yet, add an Interact Action.
        if (!this.hasCapability(GroundStatus.ACTIVATED)) {
            actions.add(new InteractAction(this));
        }

        // Else If the other Actor is able to rest and the Site of Lost Grace is activated, create a Rest Action.
        else if (actor.hasCapability(Status.CAN_REST) && this.hasCapability(GroundStatus.ACTIVATED)) {
            // Create a new RestAction, representing the Player resting on the Site of Lost grace.
            // Pass in the Site of Lost Grace being rested here to the Rest Action to save the last rested Site of Lost Grace.
            actions.add(new RestAction(this));
            // Create a new BuyingAction in which the Player can execute it to upgrade their maximum HP by 48 hitpoints.
            // Only can be done once Site of Lost Grace is activated.
            actions.add(new BuyingAction(this));
        }

        // If the other Actor is able to fast travel, create Travel Map Actions to all activated Site of Lost Grace.
        if (actor.hasCapability(Status.CAN_FAST_TRAVEL) && this.hasCapability(GroundStatus.ACTIVATED)) {
            // Loop through all activated sites and create a TravelMapAction to each one except the current Site of Lost Grace.
            for (SiteOfLostGrace activatedSite : MapManager.getInstance(location.map()).getActivatedSites()) {
                // If the name of the Activated Site of Lost Grace is not equal to this one, create a TravelMapAction to it.
                if (!activatedSite.getName().equals(this.getName())) {
                    // Create a TravelMapAction to the activatedSites and add it to the actions list.
                    actions.add(new TravelMapAction(activatedSite));
                }
            }
        }
        return actions;
    }

    /**
     * If the Location of the Site of Lost Grace has not been set yet, set it.
     * @param location The location of the Site of Lost Grace.
     */
    @Override
    public void tick(Location location) {
        // If the currentLocation of the Site of Lost Grace has not been set yet, set it.
        if (currentLocation == null) {
            setCurrentLocation(location);   // Set the current location to the Site of Lost Grace.
        }
    }

    /**
     * A method that will allow the Player to interact with this Site of Lost Grace to activate it.
     * @param map: The GameMap that the Site of Lost Grace is in.
     * @param actor: The Actor interacting with this Site of Lost Grace.
     * @return the result of the interaction.
     */
    @Override
    public String interact(GameMap map, Actor actor) {
        String result = "";

        // Adds the ACTIVATED capability to the Site Of Lost Grace to signify it's activated.
        this.addCapability(GroundStatus.ACTIVATED);

        // Add this activated Site of Lost Grace to the MapManager activatedSites ArrayList.
        MapManager.getInstance(map).addActivatedSite(this);

        // Result string of the discovery of the Site of Lost Grace once activated.
        result += FancyMessage.LOST_GRACE_DISCOVERED;
        result += System.lineSeparator() + String.format("%s activates %s", actor, this);

        return result;
    }

    /**
     * A method that will allow the Player to travel to another GameMap using the activated Site of Lost Grace.
     * @param map: The GameMap that the Player is travelling from.
     * @param actor: The Actor travelling through GameMaps using the Site of Lost Grace.
     * @return the result of the travelling.
     */
    @Override
    public String travel(GameMap map, Actor actor) {
        // Move the Actor to the Site of Lost Grace Location.
        map.moveActor(actor, this.getCurrentLocation());
        // Return the result of this travel action.
        return String.format("%s fast travels to %s", actor, this);
    }

    /**
     * A method which will allow the Actor to upgrade their max HP by purchasing it from the Site of Lost Grace.
     * @param actor: The Actor enacting the BuyingAction.
     */
    @Override
    public String buys(Actor actor) {
        String result = "";
        // Get the RuneManager instance to subtract the Runes based on upgradePrice.
        RuneManager runeManager = RuneManager.getInstance(actor);

        // If the Player has enough Runes to upgrade his HP, subtract the runeCount from the runes.
        if (runeManager.getRunes().getRuneCount() >= getBuyingPrice()) {
            // Subtract the runeCount based on the upgrade price.
            runeManager.subtractRunes(getBuyingPrice());
            // Increase the Actor max's hp by 48.
            actor.increaseMaxHp(48);

            result += String.format("%s upgrades maximum HP for %d Runes", actor, getBuyingPrice());

            // Increase the upgrade price by 100 each time the HP is upgraded.
            this.setBuyingPrice(100);
        }
        // Else if not enough Runes to upgrade their max HP, do not upgrade.
        else {
            result += String.format("Insufficient Runes to upgrade maximum HP");
        }
        return result;
    }

    /**
     * toString method that returns the Site of Lost Grace name.
     * @return the Site of Lost Grace name.
     */
    @Override
    public String toString() {
        return String.format("%s", this.getName());
    }
}
