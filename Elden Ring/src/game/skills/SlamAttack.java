package game.skills;

/**
 * A class that represents a SpecialAttackAction that slams, attack hits anything in their surroundings
 * including other enemies of the same type within the attack area.
 * @author Wah Yang Tan
 * Modified by:
 * @author Po Han Tay, Bryan Wong
 * @version 1.0
 */

public class SlamAttack extends Skills {
    /**
     * SlamAttack Constructor.
     * Calls the parent constructor to set the inherited attributes.
     * @param name: The name of the Skill.
     * @param damage: The damage dealt to the target Actor.
     * @param accuracy: The accuracy chance of the Skill attack.
     * @param verb: The verb action when using the Skill attack.
     */
    public SlamAttack(String name, int damage, int accuracy, String verb) {
        super(name, damage, accuracy, verb);
    }
}
