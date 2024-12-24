package game.utils;

import edu.monash.fit2099.engine.actions.ActionList;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.items.Item;
import edu.monash.fit2099.engine.weapons.WeaponItem;
import game.actions.AttackAction;
import game.items.FlaskOfCrimsonTears;
import game.items.ItemStatus;
import game.items.RemembranceOfTheGrafted;
import game.items.Runes;

/**
 * This class contains generic helper functions used in the program.
 * Created by:
 * @author Bryan Wong
 * Modified by:
 * @author Po Han Tay
 * @version 1.0
 * @see ActionList
 * @see Actor
 * @see Item
 * @see WeaponItem
 * @see AttackAction
 * @see FlaskOfCrimsonTears
 * @see ItemStatus
 * @see RemembranceOfTheGrafted
 * @see Runes
 * */

public class Utils {
    /**
     * Method to check if the length of the input string is more than low but no maximum limit.
     * @param stringInput: The input string to check the length of.
     * @param low: The minimum inclusive value the stringInput must have
     * @return true if the length of the stringInput is more than the low given. False otherwise.
     */
    public static boolean verifyLength(String stringInput, int low) {
        int stringLength = stringInput.length();    // Get the length of the string.

        // Return true if the length of the string is more than low.
        return (stringLength >= low);
    }

    /**
     * Method to check if the length of the input string is between the low and high inputs.
     * @param stringInput: The input string to check the length of.
     * @param low: The minimum inclusive value the stringInput must have
     * @param high: The maximum inclusive value the stringInput must have
     * @return true if the length of the stringInput is within the low and high given. False otherwise.
     */
    public static boolean verifyLength(String stringInput, int low, int high) {
        int stringLength = stringInput.length();    // Get the length of the string.

        // Return true if the length of the string is between low and high inclusive.
        return (stringLength >= low && stringLength <= high);
    }

    /**
     * Method to check if the range of the input integer is more than or equal to low. No maximum high enforced.
     * @param integerInput: The input integer to check the range of.
     * @param low: The minimum inclusive value the integerInput must have
     * @return true if the range of the integerInput is more than or equal to low. False otherwise.
     */
    public static boolean verifyRange(int integerInput, int low) {
        // Return true if the integer is more than or equal to low. False if it's not.
        return (integerInput >= low);
    }

    /**
     * Method to check if the range of the input integer is between the low and high inputs.
     * @param integerInput: The input integer to check the range of.
     * @param low: The minimum inclusive value the integerInput must have
     * @param high: The maximum inclusive value the integerInput must have
     * @return true if the range of the integerInput is within the low and high given. False otherwise.
     */
    public static boolean verifyRange(int integerInput, int low, int high) {
        // Return true if the range of the integer is between low and high inclusive.
        return (integerInput >= low && integerInput <= high);
    }

    /**
     * Determines the performable Attack Actions by the Actor by checking whether the Actor has a performable skill.
     * A normal AttackAction if no Skill and a SpecialAttackAction if it does have a Skill.
     * If the Actor can perform an AreaAttack, it can perform an AreaAttackAction skill instead.
     * Will either uses WeaponItem or IntrinsicWeapon based on whether attacking actor has a Weapon or not.
     *
     * @param target: The target of the attack.
     * @param otherActor: The Actor that might be performing attack
     * @param direction: String representing the direction of the other Actor
     * @return an ActionList of performable attack actions, eg: AttackAction, AreaAttackAction, QuickStepAction or SpecialAttackAction.
     */
    public static ActionList getAttackTypes(Actor target, Actor otherActor, String direction) {
        ActionList actions = new ActionList();      // The list of actions to be performed with a Weapon.

        // If the Actor has a Weapon in their inventory.
        if (!otherActor.getWeaponInventory().isEmpty()) {
            // Loop through all Weapons in the attacking Actor inventory and create AttackActions for all of them,
            // along with whatever SpecialAttackActions skills if applicable.
            for (WeaponItem weapon : otherActor.getWeaponInventory()) {
                // Create a normal AttackAction using the WeaponItem and add it to the ActionList.
                actions.add(new AttackAction(target, direction, weapon));

                // If the Weapon does have a skill, get the Skill Action for it.
                if (weapon.getSkill(target, direction) != null) {
                    // Get the SpecialAttackAction/AreaAttackAction skill that the Weapon can perform and add it to the ActionList.
                    actions.add(weapon.getSkill(target, direction));
                }
            }
        }
        // Else if Actor has no Weapon and is not the Player, use IntrinsicWeapon instead.
        else if (!otherActor.hasCapability(Status.PLAYER)) {
            // Create a normal AttackAction using the IntrinsicWeapon and add it to the ActionList.
            actions.add(new AttackAction(target, direction));
        }
        return actions;     // Return the ActionList filled with different Attack types performable.
    }

    /**
     * Static method to obtain the Runes object from the Actor's inventory.
     * @param actor: The Actor carrying Runes.
     * @return the Runes Item from the Actor inventory.
     */
    public static Runes getRunes(Actor actor) {
        // Loops through the Actor's inventory to find the Runes object.
        for (Item item : actor.getItemInventory()) {
            // If the Runes object is found, return it.
            if (item.hasCapability(ItemStatus.CURRENCY)) {
                return (Runes) item;
            }
        }
        return null;    // Return null if no Runes found.
    }

    /**
     * Static method to obtain the FlaskOfCrimsonTears object from the Actor's inventory.
     * @param actor: The Actor carrying FlaskOfCrimsonTears.
     * @return the FlaskOfCrimsonTears Item from the Actor inventory.
     */
    public static FlaskOfCrimsonTears getFlask(Actor actor) {
        // Loops through the Actor's inventory to find the FlaskOfCrimsonTears object.
        for (Item item : actor.getItemInventory()) {
            // If the FlaskOfCrimsonTears object is found, return it.
            if (item.hasCapability(ItemStatus.HEALTH_POTION)) {
                return (FlaskOfCrimsonTears) item;
            }
        }
        return null;    // Return null if no FlaskOfCrimsonTears found.
    }
}
