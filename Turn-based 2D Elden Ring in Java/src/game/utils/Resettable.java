package game.utils;

import edu.monash.fit2099.engine.positions.GameMap;
import edu.monash.fit2099.engine.positions.Location;

/**
 * A Resettable interface.
 * Created by:
 * @author Adrian Kristanto
 * Modified by:
 * @author Bryan Wong
 * @version 1.0
 * @see GameMap
 * @see Location
 */

public interface Resettable {
    /**
     * A method which will reset whenever the Player rests or dies.
     * @param map: The Game Map.
     * @param location: The location of the Last Site of Lost Grace the Player rested at.
     */
    void reset(GameMap map, Location location);
}
