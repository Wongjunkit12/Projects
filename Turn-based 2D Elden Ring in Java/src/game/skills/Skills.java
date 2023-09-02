package game.skills;

/**
 * An abstract class that represents all Skills that can be used by an Actor.
 * Created by:
 * @author Bryan Wong
 * Modified by:
 * @author Po Han Tay
 * @version 1.0
 */

public abstract class Skills {
    /**
     * A string representing the Skill's name.
     */
    private String name;
    /**
     * An integer representing the damage dealt to the target Actor.
     */
    private int damage;
    /**
     * An integer representing the accuracy chance of the Skill attack.
     */
    private int accuracy;
    /**
     * A String representing the verb action when using the Skill attack.
     */
    private String verb;

    /**
     * Skills Constructor.
     * Sets the damage, accuracy and verb for the skill.
     * @param name: The name of the Skill.
     * @param damage: The damage dealt to the target Actor.
     * @param accuracy: The accuracy chance of the Skill attack.
     * @param verb: The verb action when using the Skill attack.
     */
    public Skills(String name, int damage, int accuracy, String verb) {
        // Set the data attributes for skills based on input parameters.
        setName(name);
        setDamage(damage);
        setAccuracy(accuracy);
        setVerb(verb);
    }

    /**
     * A method to return the name data attribute.
     * @return the string representing the Skill's name.
     */
    public String getName() {
        return name;
    }

    /**
     * A method to return the damage data attribute.
     * @return the integer representing the damage dealt to the target.
     */
    public int getDamage() {
        return damage;
    }

    /**
     * A method to return the accuracy data attribute.
     * @return the integer representing the Skill's attack accuracy chance.
     */
    public int getAccuracy() {
        return accuracy;
    }

    /**
     * A method to return the verb data attribute.
     * @return the string representing the verb action when using the Skill attack.
     */
    public String getVerb() {
        return verb;
    }

    /**
     * A method to set the name data attribute.
     * @param name: The Skill's name.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * A method to set the damage data attribute.
     * @param damage: The damage dealt to the target Actor.
     */
    public void setDamage(int damage) {
        this.damage = damage;
    }

    /**
     * A method to set the accuracy data attribute.
     * @param accuracy: The accuracy chance of the Skill attack.
     */
    public void setAccuracy(int accuracy) {
        this.accuracy = accuracy;
    }

    /**
     * A method to set the verb data attribute.
     * @param verb: The verb action when using the Skill attack.
     */
    public void setVerb(String verb) {
        this.verb = verb;
    }
}
