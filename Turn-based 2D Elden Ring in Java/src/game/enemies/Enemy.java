package game.enemies;

import edu.monash.fit2099.engine.actions.Action;
import edu.monash.fit2099.engine.actions.ActionList;
import edu.monash.fit2099.engine.actions.DoNothingAction;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.displays.Display;
import edu.monash.fit2099.engine.positions.Exit;
import edu.monash.fit2099.engine.positions.GameMap;
import edu.monash.fit2099.engine.positions.Location;
import edu.monash.fit2099.engine.weapons.IntrinsicWeapon;
import edu.monash.fit2099.engine.weapons.WeaponItem;
import game.actions.DespawnAction;
import game.behaviours.Behaviour;
import game.behaviours.FollowBehaviour;
import game.items.Runes;
import game.skills.Skills;
import game.utils.*;

import java.util.HashMap;
import java.util.Map;

/**
 * An abstract class that represents the Enemy that can attack the Player. Inherits from Actor.
 * Created by:
 * @author Bryan Wong
 * Modified by:
 * @author Po Han Tay
 * @version 1.0
 * @see Action
 * @see ActionList
 * @see DoNothingAction
 * @see Actor
 * @see Display
 * @see GameMap
 * @see Location
 * @see IntrinsicWeapon
 * @see WeaponItem
 * @see DespawnAction
 * @see Behaviour
 * @see FollowBehaviour
 * @see Runes
 * @see Skills
 * @see Despawnable
 * @see RandomNumberGenerator
 * @see ResetManager
 * @see Resettable
 * @see RuneManager
 * @see Spawnable
 * @see Status
 * @see Utils
 */

public abstract class Enemy extends Actor implements Despawnable, Resettable {
    /**
     * A HashMap of Integer and Behaviours that the Enemy has and can perform.
     */
    private Map<Integer, Behaviour> behaviours;
    /**
     * An IntrinsicWeapon object that represents the Intrinsic Weapon of the Enemy.
     */
    private IntrinsicWeapon intrinsicWeapon;
    /**
     * An integer representing the percentage chance the Enemy can spawm every turn.
     */
    private int spawningChance;
    /**
     * An integer representing the percentage chance the Enemy can despawn every turn.
     */
    private int despawningChance;
    /**
     * A String instance variable representing the type/faction of the Enemy.
     */
    private String type;
    /**
     * A Skills instance representing the performable Skill the Enemy can perform.
     */
    private Skills skills;

    /**
     * Enemy Constructor.
     * Will call setters to set data attributes of Enemy using parameters given.
     * Calls parent class to set inherited data attributes.
     * Default create an empty HashMap for behaviours at creation.
     *
     * @param name: The name of the Enemy
     * @param displayChar: The character that will represent the Enemy in the display
     * @param hitPoints: The Enemy's starting hit points
     * @param spawningChance: The percentage chance the Enemy can spawn every turn.
     * @param despawningChance: The percentage chance the Enemy can despawn every turn.
     * @param type: The type/factin this Enemy belongs to.
     * @param runeMin: The minimum number of Runes given by the Enemy when killed by the Player.
     * @param runeMax: The maximum number of Runes given by the Enemy when killed by the Player.
     * @param weapon: The weapon used by the Enemy to attack.
     */
    public Enemy(String name, char displayChar, int hitPoints, int spawningChance, int despawningChance, String type, int runeMin, int runeMax, WeaponItem weapon) {
        super(name, displayChar, hitPoints);    // Set the Actor data attributes of the Enemy by calling the parent constructor.

        // Set data attributes.
        setSpawningChance(spawningChance);      // Set the spawning chance of Enemy.
        setDespawningChance(despawningChance);  // Set the despawning chance of Enemy.
        setType(type);                          // Set the type of Enemy

        // Create an empty HashMap for behaviours and pass it to the setter.
        Map<Integer, Behaviour> behaviours = new HashMap<>();
        setBehaviours(behaviours);

        // Generate a random amount of runes from runeMin to runeMax inclusive that the Enemy will give the Player when killed
        // and add that to the Enemy inventory.
        this.addItemToInventory(new Runes(RandomNumberGenerator.getRandomInt(runeMin, runeMax)));
        // Add the WeaponItem to the Enemy's inventory.
        this.addWeaponToInventory(weapon);

        // Add the EnemyStatus.HOSTILE_TO_PLAYER capability to signify the Enemy can attack Player.
        this.addCapability(EnemyStatus.HOSTILE_TO_PLAYER);
        // Add capability for Enemies to drop Runes.
        this.addCapability(EnemyStatus.CAN_DROP_RUNES);

        // Register this Enemy as a Resettable and add it to the resettables ArrayList,
        // Will reset when Player dies or rests.
        ResetManager.getInstance().registerRestResettable(this);
        ResetManager.getInstance().registerDeadResettable(this);
    }

    /**
     * A method to return the behaviours data attribute.
     * @return the behaviours HashMaps which shows the number of behaviours that can be performed.
     */
    public Map<Integer, Behaviour> getBehaviours() {
        return behaviours;
    }

    /**
     * A method to return the intrinsicWeapon data attribute.
     * @return the IntrinsicWeapon object used by the Enemy.
     */
    @Override
    public IntrinsicWeapon getIntrinsicWeapon() {
        return intrinsicWeapon;
    }

    /**
     * A method to return the spawningChance data attribute.
     * @return the percentage chance an Enemy can spawm every turn.
     */
    public int getSpawningChance() {
        return spawningChance;
    }

    /**
     * A method to return the despawningChance data attribute.
     * @return the percentage chance an Enemy can despawn every turn.
     */
    public int getDespawningChance() {
        return despawningChance;
    }

    /**
     * A method to return the type data attribute.
     * @return the type of Enemy.
     */
    public String getType() {
        return type;
    }

    /**
     * A method to return the skills data attribute.
     * @return the Skills instance representing the performable Skill the Enemy can perform.
     */
    public Skills getSkills() {
        return skills;
    }

    /**
     * A method to set the behaviours data attribute.
     * @param behaviours: The behaviours HashMap that contains all the behaviours performable by the Enemy.
     */
    public void setBehaviours(Map<Integer, Behaviour> behaviours) {
        this.behaviours = behaviours;
    }

    /**
     * A method to set the intrinsicWeapon data attribute.
     * @param intrinsicWeapon: The IntrinsicWeapon object used by the Enemy.
     */
    public void setIntrinsicWeapon(IntrinsicWeapon intrinsicWeapon) {
        this.intrinsicWeapon = intrinsicWeapon;
    }

    /**
     * A method to set the spawningChance data attribute.
     * @param spawningChance: The percentage chance an Enemy can spawm every turn.
     */
    public void setSpawningChance(int spawningChance) {
        this.spawningChance = spawningChance;
    }

    /**
     * A method to set the despawningChance data attribute.
     * @param despawningChance: The percentage chance an Enemy can despawn every turn.
     */
    public void setDespawningChance(int despawningChance) {
        this.despawningChance = despawningChance;
    }

    /**
     * A method to set the type data attribute.
     * @param type: A string representing the type/faction the Enemey belongs to.
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * A method to set the skills data attribute.
     * @param skills: The performable Skill the Enemy can perform.
     */
    public void setSkills(Skills skills) {
        this.skills = skills;
    }

    /**
     * At each turn, select a valid action the Enemy can perform by looping through the behaviours HashMap.
     * If there are no performable actions, do nothing.
     *
     * @param actions: Collection of possible Actions for this Enemy.
     * @param lastAction: The Action this Enemy took last turn. Can do interesting things in conjunction with Action.getNextAction().
     * @param map: The map containing the Enemy.
     * @param display: The I/O object to which messages may be written.
     * @return the valid action that can be performed in that iteration or null if no valid action is found.
     */
    @Override
    public Action playTurn(ActionList actions, Action lastAction, GameMap map, Display display) {
        // If the Enemy has despawned, return new DespawnAction to despawn the Enemy.
        if (despawn()) {
            return new DespawnAction();
        }

        // Loop through all possible exits from the Actor's current location and check if an actor exists at the destination.
        for (Exit exit : map.locationOf(this).getExits()) {
            Location destination = exit.getDestination();    // Get the destination of the said exit.

            // If the destination contains an actor (adjacent actor) and it's a FOLLOWABLE Actor,
            // set it as the target and get the allowable actions for it.
            if (this.hasCapability(EnemyStatus.HOSTILE_TO_PLAYER) &&
                    (destination.containsAnActor() && destination.getActor().hasCapability(Status.FOLLOWABLE))) {
                // Create a FollowBehaviour and set the FOLLOWABLE Actor as the target and add it in the behaviours list.
                // Add FollowBehaviour to Enemy.
                this.getBehaviours().put(99, new FollowBehaviour(destination.getActor()));

                // Add this Enemy the FOLLOWING status to indicate it's following the target.
                this.addCapability(EnemyStatus.FOLLOWING);
            }
        }

        // Loop through the behaviour HashMap to see allowable behaviours the Enemy can perform.
        for (Behaviour behaviour : behaviours.values()) {
            // Get the action the Enemy can perform based on their behaviour.
            Action action = behaviour.getAction(this, map);

            // If there exist a performable action, return that.
            if (action != null) {
                return action;
            }
        }
        // Else if no such performable action exists or Enemy has despawned, do nothing.
        return new DoNothingAction();
    }

    /**
     * A method to despawn the Enemy.
     * @return true if Enemy despawns and false otherwise.
     */
    @Override
    public boolean despawn() {
        boolean hasDespawned = false;   // Boolean variable alluding if the Enemy has despawned or not.

        // Generate a random number from 1 to 100 inclusive and compare it with despawningChance.
        // If the generated random number is below or equal to the despawning chance, despawn the enemy.
        if (!this.hasCapability(EnemyStatus.FOLLOWING) && RandomNumberGenerator.getRandomInt(1, 100) <= getDespawningChance()) {
            hasDespawned = true;        // Enemy has despawned from the game.
        }
        return hasDespawned;            // Return true if enemy has despawned and false if it hasn't.
    }

    /**
     * A method to remove the Enemy from the Game Map once Player rests or dies.
     * @param map: The Game Map.
     * @param location: THe location of the Last Site of Lost Grace the Player rested at.
     */
    @Override
    public void reset(GameMap map, Location location) {
        map.removeActor(this);  // Remove the Enemy from the map.
    }
}
