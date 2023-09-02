package game.environments;

import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.positions.Ground;
import game.utils.Status;

/**
 * A class that represents bare dirt.
 * Created by:
 * @author Bryan Wong
 * Modified by:
 *
 * @version 1.0
 * @see Actor
 * @see Ground
 * @see Status
 */

public class Cliff extends Ground {
    /**
     * Name of the Ground.
     */
    private String name;

    /**
     * Cliff Constructor.
     * Call the parent constructor and set the display character to '+'.
     */
    public Cliff() {
        super('+');
        setName("Cliff");                               // Set the name of the Ground.
        this.addCapability(GroundStatus.FALLABLE);      // Add FALLABLE capability as the Cliff can kill the Player instantly.
    }

    /**
     * A method to return the name data attribute.
     * @return the name of the Ground.
     */
    public String getName() {
        return name;
    }

    /**
     * A method to set the name data attribute.
     * @param name: The name of the Ground.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * A method to determine if the inputted Actor can enter into it. All Enemies are unable to enter into the Cliff.
     * @param actor: The Actor trying to enter the Cliff.
     * @return true if Actor can enter and false otherwise.
     */
    @Override
    public boolean canActorEnter(Actor actor) {
        // Only Player can enter the Cliff.
        return actor.hasCapability(Status.CAN_ENTER_CLIFF);
    }

    /**
     * toString method for Cliff to return it's name.
     * @return the name of the Ground.
     */
    @Override
    public String toString() {
        return this.getName();
    }
}
