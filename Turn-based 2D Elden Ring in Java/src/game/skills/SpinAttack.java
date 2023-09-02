package game.skills;

/**
 * A class that represents a SpecialAttackAction that spins and attacks all Enemies around the attacking Actor,
 * including other enemies of the same type.
 * Inherits from the Skills abstract class.
 * Created by:
 * @author Bryan Wong
 * Modified by:
 * @author Po Han Tay
 * @version 1.0
 */

public class SpinAttack extends Skills {
    /**
     * SpinAttack Constructor.
     * Calls the parent constructor to set the inherited attributes.
     * @param name: The name of the Skill.
     * @param damage: The damage dealt to the target Actor.
     * @param accuracy: The accuracy chance of the Skill attack.
     * @param verb: The verb action when using the Skill attack.
     */
    public SpinAttack(String name, int damage, int accuracy, String verb) {
        super(name, damage, accuracy, verb);
    }
}
