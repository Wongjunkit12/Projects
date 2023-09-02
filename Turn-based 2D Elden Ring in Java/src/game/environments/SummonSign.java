package game.environments;

import edu.monash.fit2099.engine.actions.ActionList;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.positions.Exit;
import edu.monash.fit2099.engine.positions.GameMap;
import edu.monash.fit2099.engine.positions.Location;
import game.actions.InteractAction;
import game.characters.playable_characters.classes.*;
import game.enemies.*;
import game.utils.Interactable;
import game.utils.RandomNumberGenerator;
import game.utils.Status;

import java.util.ArrayList;

/**
 * A class that inherits from SpawnableGround, in which it spawns the Allies/Invaders. Implements Interactable as they can
 * be interacted with.
 * Created by:
 * @author Po Han Tay
 * Modified by:
 * @author Bryan Wong
 * @version 1.0
 * @see ActionList
 * @see Actor
 * @see Exit
 * @see GameMap
 * @see Location
 * @see InteractAction
 * @see Classes
 * @see Astrologer
 * @see Bandit
 * @see Samurai
 * @see Wretch
 * @see Ally
 * @see Enemy
 * @see Invader
 * @see EastSpawningFactory
 * @see SpawningFactory
 * @see WestSpawningFactory
 * @see Interactable
 * @see RandomNumberGenerator
 * @see Status
 */

public class SummonSign extends SpawnableGround implements Interactable {
    /**
     * The Array List of available classes for the summonables to choose.
     */
    private ArrayList<Classes> availableClasses;
    /**
     * The Guest summoned from another Realm.
     */
    private Enemy summonedEnemy;
    /**
     * The current Location of the Summon Sign.
     */
    private Location currentLocation = null;

    /**
     * SummonSign Constructor.
     * Calls parent class to set inherited data attributes.
     * Sets the display character to '=', name to "SummonSign".
     * Sets the spawning limit to the maximum integer value.
     */
    public SummonSign() {
        // Call the parent constructor class with the specific data attributes the SummonSign has.
        super('=', "SummonSign", Integer.MAX_VALUE);

        // Create an ArrayList of available Classes the Guest from the other Realms can have, and also what Guests can be
        // summoned.
        availableClasses = new ArrayList<>();

        // Add in all the possible Classes the Guest can be.
        this.addAvailableClasses(new Bandit());
        this.addAvailableClasses(new Samurai());
        this.addAvailableClasses(new Wretch());
        this.addAvailableClasses(new Astrologer());
    }

    /**
     * A method to return the availableClasses data attribute.
     * @return the Array List of available Classes for the summonables to choose.
     */
    public ArrayList<Classes> getAvailableClasses() {
        return availableClasses;
    }

    /**
     * A method to return the summonedEnemy data attribute.
     * @return the Guest summoned from another Realm.
     */
    public Enemy getSummonedEnemy() {
        return summonedEnemy;
    }

    /**
     * A method to return the currentLocation data attribute.
     * @return the current Location of the Summon Sign.
     */
    public Location getCurrentLocation() {
        return currentLocation;
    }

    /**
     * A method to add the availableClasses to the availableClasses ArrayList.
     * @param availableClasses: The available Classes the Guest can have.
     */
    public void addAvailableClasses(Classes availableClasses) {
        this.getAvailableClasses().add(availableClasses);
    }

    /**
     * A method to set the summonedEnemy data attribute.
     * @param summonedEnemy: The current Location of the Summon Sign.
     */
    public void setSummonedEnemy(Enemy summonedEnemy) {
        this.summonedEnemy = summonedEnemy;
    }

    /**
     * A method to set the currentLocation data attribute.
     * @param currentLocation: The Guest summoned from another Realm.
     */
    public void setCurrentLocation(Location currentLocation) {
        this.currentLocation = currentLocation;
    }

    /**
     * A method to return the description of the InteractAction.
     * Player summons an Ally/Invader.
     * @return the description of what happen during the InteractAction.
     */
    @Override
    public String getInteractDescription() {
        return "summons a Guest from another Realm.";
    }

    /**
     * A method to determine if the inputted Actor can enter into it. All Enemies are unable to enter into the Summon Sign.
     * @param actor: The Actor trying to enter the Summon Sign.
     * @return true if Actor can enter and false otherwise.
     */
    @Override
    public boolean canActorEnter(Actor actor) {
        // Only Player can enter the Summon Sign.
        return actor.hasCapability(Status.CAN_SUMMON);
    }

    /**
     * Will contain the lists of Actions allowable for Player to perform on the Summon Sign.
     *
     * @param actor: The Actor that is on the Summon Sign.
     * @param location: The Location of the Summon Sign.
     * @param direction: String representing the direction of the Actor.
     * @return the list of allowable actions that can be performed by the Actor on the Summon Sign.
     */
    @Override
    public ActionList allowableActions(Actor actor, Location location, String direction) {
        // Create an ActionList containing a list of allowable actions to be performed by the Player on the Summon Sign.
        ActionList actions = new ActionList();

        // If the Actor can summon, then allow the Summoning action.
        if (actor.hasCapability(Status.CAN_SUMMON)) {
            actions.add(new InteractAction(this));
        }
        return actions;
    }

    /**
     * If the Location of the Summon Sign has not been set yet, set it.
     * @param location The location of the Summon Sign.
     */
    @Override
    public void tick(Location location) {
        // If the currentLocation of the SummonSign has not been set yet, set it.
        if (currentLocation == null) {
            setCurrentLocation(location);   // Set the current location to the Summon Sign.
        }
        // If the SummonSign is on the West side of the map, set the spawningFactor to the WestSpawningFactory instance
        // if they haven't been set yet (spawningFactory is null).
        if ((this.getSpawningFactory() == null) && (location.x() <= (location.map().getXRange().max() / 2))) {
            // Create a new instance of HeavySkeletalSwordsman and get their SpawningFactory instance.
            this.setSpawningFactory(new WestSpawningFactory());
        }
        // Else if the SummonSign is on the East side of the map, set the spawningFactor to the EastSpawningFactory instance
        // if they haven't been set yet (spawningFactory is null).
        else if ((this.getSpawningFactory() == null) && (location.x() > (location.map().getXRange().max() / 2))) {
            // Create a new instance of SkeletalBandit and get their SpawningFactory instance.
            this.setSpawningFactory(new EastSpawningFactory());
        }
    }

    /**
     * A method that will allow the Player to interact with this Interactable.
     * Player will summon a guest from another Realm (50% Ally or Invader).
     * @param map: The GameMap the interacting is being done on.
     * @param actor: The Actor interacting with the Interactable.
     * @return the result of the interaction.
     */
    @Override
    public String interact(GameMap map, Actor actor) {
        // The summoned Enemy.
        Enemy summonedEnemy = null;

        // If there are available Classes to choose,
        if (!getAvailableClasses().isEmpty()) {
            // Randomly picks one Class to set the Guest to.
            int randomChoice1 = RandomNumberGenerator.getRandomInt(getAvailableClasses().size() - 1);
            Classes classChosen = getAvailableClasses().get(randomChoice1);

            // Choose 50% chance to spawn an Ally.
            if (RandomNumberGenerator.getRandomInt(100) <= 50) {
                // Create a new Ally with that random class.
                summonedEnemy = new Ally(classChosen);
            }
            // Else spawn an Invader.
            else {
                // Create a new Invader with that random class.
                summonedEnemy = new Invader(classChosen);
            }
        }

        // If there is a summonable Enemy, summon it.
        if (summonedEnemy != null) {
            // Set the summonedEnemy to be either Ally or Invader.
            setSummonedEnemy(summonedEnemy);
            // ArrayList holding all possible spawning Locations for the Ally/Enemy.
            ArrayList<Location> summoningLocations = new ArrayList<>();

            // Loop through all possible exits from the Actor's current location and store their Locations in the ArrayList.
            for (Exit exit : getCurrentLocation().getExits()) {
                Location destination = exit.getDestination();    // Get the destination of the said exit.

                // If there are no actors at destination and if the Ally/Invader can enter the destination, store
                // the Location into the summoningLocations.
                if (!destination.containsAnActor() && destination.canActorEnter(actor)) {
                    summoningLocations.add(destination);
                }
            }
            // If there is at least one possible adjacent Location to spawn the summonable, call the spawn method.
            if (!summoningLocations.isEmpty()) {
                // Randomly picks a Location to spawn the Ally/Invader to.
                int randomChoice = RandomNumberGenerator.getRandomInt(summoningLocations.size() - 1);
                this.spawn(summoningLocations.get(randomChoice));
            }
        }
        // Return the result of this interaction.
        return String.format("%s summons a %s", actor, summonedEnemy);
    }

    /**
     * A method to spawn the summonedEnemy on the SummonSign.
     * @param location: The location of the Ground.
     */
    @Override
    public void spawn(Location location) {
        // If the limit to spawn the Enemy are more than 0, one can be spawn.
        // Call the createSummonable method inside the spawningFactory to spawn in a summonedEnemy if applicable.
        if (getSpawningLimit() > 0) {
            // Decrement the spawningLimit if a summonedEnemy successfully spawned, don't do so if none spawned.
            this.setSpawningLimit(this.getSpawningFactory().createSummonable(location.map(), location, getSummonedEnemy()) ? (getSpawningLimit() - 1) : getSpawningLimit());
        }
    }
}
