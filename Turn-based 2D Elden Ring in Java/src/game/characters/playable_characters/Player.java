package game.characters.playable_characters;

import edu.monash.fit2099.engine.actions.Action;
import edu.monash.fit2099.engine.actions.ActionList;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.displays.Display;
import edu.monash.fit2099.engine.positions.GameMap;
import edu.monash.fit2099.engine.displays.Menu;
import edu.monash.fit2099.engine.positions.Location;
import game.actions.FallingAction;
import game.characters.playable_characters.classes.*;
import game.enemies.EnemyStatus;
import game.environments.GroundStatus;
import game.items.FlaskOfCrimsonTears;
import game.items.Runes;
import game.utils.*;

import java.util.Scanner;

/**
 * Class representing the Player. It implements the Resettable interface.
 * It carries around a club to attack a hostile creature in the Lands Between.
 * Created by:
 * @author Adrian Kristanto
 * Modified by:
 * @author Bryan Wong, Po Han Tay
 * @version 1.0
 * @see Action
 * @see ActionList
 * @see Actor
 * @see Display
 * @see GameMap
 * @see Menu
 * @see Location
 * @see FallingAction
 * @see Bandit
 * @see Classes
 * @see Samurai
 * @see Wretch
 * @see EnemyStatus
 * @see GroundStatus
 * @see FlaskOfCrimsonTears
 * @see Runes
 * @see ResetManager
 * @see Resettable
 * @see Status
 * @see Utils
 */

public class Player extends Actor implements Resettable {
	/**
	 * Constant MENU object.
	 */
	private final Menu MENU = new Menu();
	/**
	 * The Player Class they have chosen.
	 */
	private Classes playerClass;
	/**
	 * The past Location of the Player.
	 */
	private Location pastLocation;

	/**
	 * Player Constructor.
	 * Call Actor parent constructor to set the name, displayChar and hitpoints.
	 * Add the HOSTILE_TO_ENEMY status to allow this actor to enter the Floor.
	 *
	 * @param name: Name to call the player in the UI
	 * @param displayChar: Character to represent the player in the UI
	 * @param hitPoints: Player's starting number of hitpoints
	 * @throws Exception when failed to set data attributes of Player.
	 */
	public Player(String name, char displayChar, int hitPoints) throws Exception {
		// Call parent constructor to set inherited data attributes.
		super(name, displayChar, hitPoints);
		// Add the HOSTILE_TO_ENEMY capability meaning all Enemies can attack it.
		this.addCapability(EnemyStatus.HOSTILE_TO_ENEMY);
		// Add the NOT_HOSTILE_TO_ALLY capability meaning Player cannot attack it.
		this.addCapability(EnemyStatus.NOT_HOSTILE_TO_ALLY);
		// Add the Player capability meaning this character is a Player.
		// Adds other capabilities like being able to enter the Floor and can rest on the Site of Lost Grace.
		this.addCapability(Status.PLAYER);
		this.addCapability(Status.CAN_ENTER_FLOOR);			// Player can enter Floor.
		this.addCapability(Status.CAN_ENTER_CLIFF);			// Player can enter Cliff.
		this.addCapability(Status.CAN_REST);				// Player can rest.
		this.addCapability(Status.CAN_RESET_GAME);    		// Player's death can Reset game.
		this.addCapability(Status.CAN_FAST_TRAVEL);      	// Player can enter travel to different maps.
		this.addCapability(Status.CAN_SUMMON);				// Player can summon guests from another Realm.

		// Followable means it can be followed by Enemies.
		this.addCapability(Status.FOLLOWABLE);
		// Tradeable means it can trade with the Trader.
		this.addCapability(Status.TRADEABLE);
		// CAN_GAIN_RUNES means the Player can gain Runes when slaughtering its foes.
		this.addCapability(Status.CAN_GAIN_RUNES);

		// Create a new Runes object and set the runeCount to 0 as Player does not have any Runes at the game start yet.
		this.addItemToInventory(new Runes(0));
		// Create a new FlaskOfCrimsonTears object and set its uses to 2 as Player starts with 2 uses.
		this.addItemToInventory(new FlaskOfCrimsonTears(2));

		// Choose the Player Class.
		this.chooseClass();

		// Register this Enemy as a Resettable and add it to the resettables ArrayList,
		// Will reset when Player dies or rests.
		ResetManager.getInstance().registerRestResettable(this);
		ResetManager.getInstance().registerDeadResettable(this);

		// Add this the Player Runes to the deadResettables ArrayList in ResetManager as Runes can only be reset when Player dies.
		ResetManager.getInstance().registerDeadResettable(Utils.getRunes(this));
	}

	/**
	 * A method to return the playerClass data attribute.
	 * @return the Classes object representing which Class the Player is.
	 */
	public Classes getPlayerClass() {
		return playerClass;
	}

	/**
	 * A method to return the pastLocation data attribute.
	 * @return the past Location of the Player.
	 */
	public Location getPastLocation() {
		return pastLocation;
	}

	/**
	 * A method to set the playerClass data attribute.
	 * @param playerClass: The Classes object representing which Class the Player is.
	 */
	public void setPlayerClass(Classes playerClass) {
		this.playerClass = playerClass;
	}

	/**
	 * A method to set the pastLocation data attribute.
	 * @param pastLocation: The past Location of the Player.
	 */
	public void setPastLocation(Location pastLocation) {
		this.pastLocation = pastLocation;
	}

	/**
	 * A method to allow the user to choose their preferred starting Class for the Player.
	 */
	public void chooseClass() {
		int selectionChoice = 0;    // Classes menu selection. Default set to 0.
		do {
			Scanner sel = new Scanner(System.in);		// Create new Scanner object
			Display display = new Display();			// Create new Display object

			display.println("\n");
			display.println("Select your Class:");
			display.println("1: Bandit");
			display.println("2: Samurai");
			display.println("3: Wretch");
			display.println("4: Astrologer");

			// Try to get the user input for their menu selection.
			try {
				selectionChoice = Integer.parseInt(sel.nextLine());     // Get the user choice.

				// Switch case which will set their respective Classes based on user selection.
				switch (selectionChoice) {
					// Case 1 to set Player's Class to Bandit.
					case 1:
						setClassAttributes(new Bandit());
						break;
					// Case 2 to set Player's Class to Samurai.
					case 2:
						setClassAttributes(new Samurai());
						break;
					// Case 3 to set Player's Class to Wretch.
					case 3:
						setClassAttributes(new Wretch());
						break;
					// Case 3 to set Player's Class to
					case 4:
						setClassAttributes(new Astrologer());
				}
			}
			// Else if input is not a number, display error message.
			catch (NumberFormatException e) {
				display.println("Invalid input. Enter an integer value.");
			}
		} while (!((selectionChoice > 0) && (selectionChoice < 5)));
	}

	/**
	 * A method to set the Player's Class, Maximum HP and starting Weapon based on their selected Class.
	 * @param chosenClass: The Player Class that the user has selected.
	 */
	public void setClassAttributes(Classes chosenClass) {
		// Set the Player Class instance variable to chosen Class.
		setPlayerClass(chosenClass);
		// Set the Player's Starting HP to the chosen Class Starting HP.
		this.resetMaxHp(getPlayerClass().getStartingHP());
		// Add the Starting Weapon to the Player's inventory.
		this.addWeaponToInventory(getPlayerClass().getStartingWeapon());
	}

	/**
	 * At each turn, select a valid action the Player can perform by looping through ActionList.
	 * If there are no performable actions, do nothing.
	 *
	 * @param actions: Collection of possible Actions for this Player.
	 * @param lastAction: The Action this Player took last turn. Can do interesting things in conjunction with Action.getNextAction().
	 * @param map: The map containing the Player.
	 * @param display: The I/O object to which messages may be written.
	 * @return the valid action that can be performed in that iteration or null if no valid action is found.
	 */
	@Override
	public Action playTurn(ActionList actions, Action lastAction, GameMap map, Display display) {
		// If the Player is standing on a fallable ground, kill them.
		if (map.locationOf(this).getGround().hasCapability(GroundStatus.FALLABLE)) {
			// Create a FallingAction and execute it to make the Player fall off the cliff.
			return new FallingAction(map.locationOf(this).getGround());
		}

		// Add in new Runes to the Player's inventory after it has dropped them when it died.
		if (Utils.getRunes(this) == null) {
			this.addItemToInventory(new Runes(0));

			// Add this the Player Runes to the deadResettables ArrayList in ResetManager as Runes can only be reset when Player dies.
			ResetManager.getInstance().registerDeadResettable(Utils.getRunes(this));
		}

		// Set the past Location of the Player and set it in the RuneManager.
		setPastLocation(map.locationOf(this));
		RuneManager.getInstance(this).setPastLocation(getPastLocation());

		// Display the Player's current HP and current number of Runes.
		display.println(String.format("%s HP: %s | Runes: %d | Flask of Crimson Tears Uses: %d",
				this, this.printHp(), Utils.getRunes(this).getRuneCount(),
				Utils.getFlask(this).getUses()));

		// Handle multi-turn Actions
		if (lastAction.getNextAction() != null)
			return lastAction.getNextAction();

		// return/print the console menu
		return MENU.showMenu(this, actions, display);
	}

	/**
	 * The Actor can attack the Player if they have the capability EnemyStatus.HOSTILE_TO_PLAYER.
	 *
	 * @param otherActor: The Actor that might be performing attack
	 * @param direction: String representing the direction of the other Actor
	 * @param map: current GameMap
	 * @return the list of allowable actions that can be performed by the other Actor to the Player.
	 */
	@Override
	public ActionList allowableActions(Actor otherActor, String direction, GameMap map) {
		// Create an ActionList containing a list of allowable actions to be performed on the Player.
		ActionList actions = new ActionList();

		// If the attacking Actor is hostile to the Player, attack.
		if (otherActor.hasCapability(EnemyStatus.HOSTILE_TO_PLAYER)) {
			// Call the getAttackTypes utility method which will get the ActionList of all performable Attack types on this Player.
			actions = Utils.getAttackTypes(this, otherActor, direction);
		}
		return actions;
	}

	/**
	 * A method which will reset the Player hitpoints back to maximum whenever the Player rests or dies.
	 * @param map: The Game Map.
	 * @param location: THe location of the Last Site of Lost Grace the Player rested at.
	 */
	@Override
	public void reset(GameMap map, Location location) {
		// Resets the current HP back to the maximum HP.
		this.resetMaxHp(this.getMaxHp());

		// If Player is dead, create new Rune object and add it to their inventory
		if (this.hasCapability(Status.DEAD)) {
			// If the location does not contain an Actor, move him there.
			if (!location.containsAnActor()) {
				// Move the Player to the last Site of Lost Grace the Player rested after Game resets.
				map.moveActor(this, location);
			}

			// Update Player's capability to remove dead status when resets
			this.removeCapability(Status.DEAD);
		}
	}
}
