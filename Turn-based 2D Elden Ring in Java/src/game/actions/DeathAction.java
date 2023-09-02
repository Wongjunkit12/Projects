package game.actions;

import edu.monash.fit2099.engine.actions.Action;
import edu.monash.fit2099.engine.actions.ActionList;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.items.Item;
import edu.monash.fit2099.engine.positions.GameMap;
import edu.monash.fit2099.engine.positions.Location;
import edu.monash.fit2099.engine.weapons.WeaponItem;
import game.displays.FancyMessage;
import game.enemies.EnemyStatus;
import game.enemies.PileOfBones;
import game.environments.SiteOfLostGrace;
import game.items.ItemStatus;
import game.utils.RuneManager;
import game.utils.Status;

/**
 * An Action executed if an Actor is killed.
 * Created by:
 * @author Adrian Kristanto
 * Modified by:
 * @author Bryan Wong, Po Han Tay
 * @version 1.0
 * @see Action
 * @see ActionList
 * @see Actor
 * @see Item
 * @see GameMap
 * @see Location
 * @see WeaponItem
 * @see FancyMessage
 * @see EnemyStatus
 * @see PileOfBones
 * @see SiteOfLostGrace
 * @see ItemStatus
 * @see RuneManager
 * @see Status
 */

public class DeathAction extends Action {
    /**
     * The Actor that has performed the attack on the now dead victim.
     */
    private Actor attacker;

    /**
     * DeathAction Constructor.
     * @param actor: The attacking Actor the AttackAction is performed by.
     */
    public DeathAction(Actor actor) {
        setAttacker(actor);     // Set the attacker.
    }

    /**
     * A method to return the attacker data attribute.
     * @return the attacking Actor the AttackAction is performed by.
     */
    public Actor getAttacker() {
        return attacker;
    }

    /**
     * A method to set the attacker data attribute.
     * @param attacker: The attacking Actor the AttackAction is performed by.
     */
    public void setAttacker(Actor attacker) {
        this.attacker = attacker;
    }

    /**
     * When the target is killed, the Items and Weapons carried by target with the portable boolean
     * will be dropped to the location in the game map where the target was.
     *
     * @param target: The Actor performing the action.
     * @param map: The map the Actor is on.
     * @return result of the action to be displayed on the UI.
     */
    @Override
    public String execute(Actor target, GameMap map) {
        String result = "";     // The death message of the target.

        ActionList dropActions = new ActionList();  // List of DropActions.

        target.addCapability(Status.DEAD);  // Add the capability DEAD to signify the target is dead.

        // If the attacker is an Actor who can gain runes amd the target is one who can drop Runes
        // and the target is not a RESURRECTABLE Enemy, give them the Runes dropped by the dead Enemy.
        if (((getAttacker()).hasCapability(Status.CAN_GAIN_RUNES)) && (target.hasCapability(EnemyStatus.CAN_DROP_RUNES))
                && !(target.hasCapability(EnemyStatus.RESURRECTABLE))) {
            // Call the gainRunes method in the RuneManager to add the runeCount of the attacker who CAN_GAIN_RUNES.
            RuneManager.getInstance(getAttacker()).gainRunes(getAttacker(), target);
        }

        // If the target is the Actor that CAN_GAIN_RUNES, pass its currentLocation and numberOfDeaths to the Rune so that it
        // will drop the Rune and reset if the Actor dies again.
        if (target.hasCapability(Status.CAN_GAIN_RUNES)) {
            // Get the current location of the Player.
            Location currentLocation = map.locationOf(target);
            // Get the past location of the Player.
            Location pastLocation = RuneManager.getInstance(target).getPastLocation();

            // If the Player moved before they died, move them back to previous location so that Runes will be dropped there.
            if (!((currentLocation.x() == pastLocation.x()) && (currentLocation.y() == pastLocation.y())) &&
                    !(pastLocation.containsAnActor())) {
                // Move the Player to its previous Location so that it drops the Runes at its previous location.
                map.moveActor(target, pastLocation);
            }
        }

        // Drop every single item in the victim's inventory.
        for (Item item : target.getItemInventory()) {
            // If the Item is a Rune (has a currency) or if the target is not the Player, drop all Items.
            if (item.hasCapability(ItemStatus.CURRENCY) || !target.hasCapability(Status.PLAYER)) {
                dropActions.add(item.getDropAction(target));
            }
        }
        // Drop every single weapon in the victim's inventory when dead if they are not the Player.
        for (WeaponItem weapon : target.getWeaponInventory()) {
            // If the target is not the Player, drop all Weapons as Player cannot drop Weapons when dead.
            if (!target.hasCapability(Status.PLAYER)) {
                dropActions.add(weapon.getDropAction(target));
            }
        }
        // Execute all DropActions stored in the dropActions ActionList and create the dropped items/weapons character in map.
        for (Action drop : dropActions) {
            drop.execute(target, map);
        }

        // Return death message of dead victim.
        result += System.lineSeparator() + menuDescription(target);

        // If the target has the EnemyStatus of RESURRECTABLE, turn them into a PileOfBones object
        if (target.hasCapability(EnemyStatus.RESURRECTABLE)) {
            Location targetLocation = map.locationOf(target);   // Get the target location.
            map.removeActor(target);    // Remove the RESURRECTABLE Enemy to replace them with a PileOfBones.

            target.increaseMaxHp(0);    // Reset the RESURRECTABLE Enemy to have maximum HP.

            // Create a new PileOfBones and add it to the map at targetLocation.
            PileOfBones pileOfBones = new PileOfBones(target, 3);
            map.addActor(pileOfBones, targetLocation);
        }

        // Else if target death causes the Game to Reset, reset the Game when it dies.
        else if (target.hasCapability(Status.CAN_RESET_GAME)) {
            new ResetAction().execute(target, map);     // Execute a ResetAction.

            // YOU DIED.
            result += System.lineSeparator() + FancyMessage.YOU_DIED;
        }

        // Else if target is a DEMIGOD, remove the Actor and drop a Site of Lost Grace on his dead corpse.
        else if (target.hasCapability(EnemyStatus.DEMIGOD)) {
            // Add in a "Godrick the Grafted" Site of Lost Grace at the location where the Demigod died.
            map.locationOf(target).setGround(new SiteOfLostGrace("Godrick the Grafted"));

            // Remove now dead Actor :(.
            map.removeActor(target);

            // DEMIGOD SLAIN.
            result += System.lineSeparator() + FancyMessage.DEMIGOD_FELLED;
        }

        // Else just remove the actor from the map.
        else {
            // Remove now dead Actor :(.
            map.removeActor(target);
        }
        return result;
    }

    /**
     * A method that displays in the menu the death message of an Actor.
     * @param actor: The Actor that has died.
     * @return the String death message of the Actor that has been killed and by whom.
     */
    @Override
    public String menuDescription(Actor actor) {
        return String.format("%s is killed by %s", actor, getAttacker());
    }
}
