package game.skills;

/**
 * A class that represents a unique Skill "Unsheathe" performed when using the Uchigatana.
 * @author Po Han Tay
 * Modified by:
 * @author Bryan Wong
 * @version 1.0
 */

public class Unsheathe extends Skills {
    /**
     * Unsheathe Constructor.
     * Calls the parent constructor to set the inherited attributes.
     * @param name: The name of the Skill.
     * @param damage: The damage dealt to the target Actor.
     * @param accuracy: The accuracy chance of the Skill attack.
     * @param verb: The verb action when using the Skill attack.
     */
    public Unsheathe(String name, int damage, int accuracy, String verb) {
        super(name, damage, accuracy, verb);
    }
}
