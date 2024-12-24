package game.actions;

import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.items.PickUpAction;
import edu.monash.fit2099.engine.positions.GameMap;
import game.items.Runes;
import game.utils.RuneManager;

/**
 * A class that represents a Recovering Runes Action when the Player recovers the dropped Runes after they have died.
 * Created by:
 * @author Bryan Wong
 * Modified by:
 * @author Po Han Tay
 * @version 1.0
 * @see Actor
 * @see PickUpAction
 * @see GameMap
 * @see Runes
 * @see RuneManager
 */

public class RecoverRunesAction extends PickUpAction {
    /**
     * The Runes object that is on the Ground.
     */
    private Runes runesToRecover;

    /**
     * Constructor for RecoverRunesAction.
     * @param runesToRecover: The Runes object that is on the ground.
     */
    public RecoverRunesAction(Runes runesToRecover) {
        super(runesToRecover);      // Passes in the item to "pick up" to be the Runes on the ground.
        setRunesToRecover(runesToRecover);
    }

    /**
     * A method to return the runesToRecover data attribute.
     * @return the Runes object that is on the ground.
     */
    public Runes getRunesToRecover() {
        return runesToRecover;
    }

    /**
     * A method to set the runesToRecover data attribute with a new Runes object.
     * @param runesToRecover: The Runes object that is on the ground.
     */
    public void setRunesToRecover(Runes runesToRecover) {
        this.runesToRecover = runesToRecover;
    }

    /**
     * When executed, use RuneManager to add the runeCount of the Runes object in the Actors inventory based on the runeCount of
     * the Runes on the ground.
     * @param actor The actor performing the recovering Runes action.
     * @param map The map the actor is on.
     * @return the result of the RecoverRunesAction.
     */
    @Override
    public String execute(Actor actor, GameMap map) {
        // Call the gainRunes with the Actor and dropped Runes on the ground to increase the runeCount of the Runes object
        // in the actor inventory with the runeCount of the dropped runes.
        RuneManager.getInstance(actor).gainRunes(getRunesToRecover());

        // Call the parent execute method to remove the Runes from the map.
        super.execute(actor, map);

        // Return the result of the RecoverRunesAction.
        return String.format("%s recovers %d Runes", actor, getRunesToRecover().getRuneCount());
    }

    /**
     * Describes how many Runes is recovered by the Player.
     *
     * @param actor: The actor performing the action.
     * @return a description used for the menu UI.
     */
    @Override
    public String menuDescription(Actor actor) {
        return String.format("%s recovers Runes", actor);
    }
}
