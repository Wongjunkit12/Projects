package game.characters.playable_characters.classes;

import edu.monash.fit2099.engine.weapons.WeaponItem;

/**
 * An abstract class representing all the Classes the Player can choose, giving unique staring HP, weapon and skills.
 * Created by:
 * @author Bryan Wong
 * Modified by:
 * @author Po Han Tay
 * @version 1.0
 * @see WeaponItem
 */

public abstract class Classes {
    /**
     * A String instance variable representing the name of the Class.
     */
    private String name;
    /**
     * An integer representing the maximum HP of the class.
     */
    private int startingHP;
    /**
     * The WeaponItem the Class will start with.
     */
    private WeaponItem startingWeapon;

    /**
     * Classes Constructor
     * Set the name, startingHP and the startingWeapon of the Class.
     * @param name: The name of the Class
     * @param startingHP: Staring hitpoints of the Class.
     * @param startingWeapon: The starting Weapon the Class has.
     */
    public Classes(String name, int startingHP, WeaponItem startingWeapon) {
        // Set the respective data attributes based on parameters of constructor.
        setName(name);
        setStartingHP(startingHP);
        setStartingWeapon(startingWeapon);
    }

    /**
     * A method to return the name data attribute.
     * @return the name of the Class.
     */
    public String getName() {
        return name;
    }

    /**
     * A method to return the startingHP data attribute.
     * @return the startingHP of the Class.
     */
    public int getStartingHP() {
        return startingHP;
    }

    /**
     * A method to return the startingWeapon data attribute.
     * @return the WeaponItem that the Class will start with.
     */
    public WeaponItem getStartingWeapon() {
        return startingWeapon;
    }

    /**
     * A method to set the name data attribute.
     * @param name: The name of the Class.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * A method to set the startingHP data attribute.
     * @param startingHP: The startingHP of the Class.
     */
    public void setStartingHP(int startingHP) {
        this.startingHP = startingHP;
    }

    /**
     * A method to set the startingWeapon data attribute.
     * @param startingWeapon: The WeaponItem that the Class will start with.
     */
    public void setStartingWeapon(WeaponItem startingWeapon) {
        this.startingWeapon = startingWeapon;
    }
}
