package game.maps;

import edu.monash.fit2099.engine.positions.FancyGroundFactory;
import edu.monash.fit2099.engine.positions.World;
import game.characters.playable_characters.Player;
import game.environments.*;

/**
 * A class representing the Limgrave Map.
 * Created by:
 * @author Bryan Wong
 * Modified by:
 *
 * @version 1.0
 * @see FancyGroundFactory
 * @see World
 * @see Player
 * @see Dirt
 * @see Floor
 * @see GustOfWind
 * @see Graveyard
 * @see PuddleOfWater
 * @see SummonSign
 * @see Wall
 */

public class Limgrave extends Maps {
    /**
     * The x-coordinate of the spawning location of the Player for this Map.
     */
    private int xSpawnLocation = 38;
    /**
     * The y-coordinate of the spawning location of the Player for this Map.
     */
    private int ySpawnLocation = 11;

    /**
     * LimgraveMap constructor. Will create a GameMap instance for Limgrave GameMap.
     * Will create a new GameMap instance and add it to the world.
     * @param world: The Elden Ring World class instance.
     * @param player: The Player being added to the GameMap.
     */
    public Limgrave(World world, Player player) {
        // Call the parent constructor to create and set the GameMap for this Map.
        // Pass in the Limgrave FancyGroundFactory and MapDisplay to create the GameMap.
        // Passes in the fixed spawning coordinates of the Player.
        super(new FancyGroundFactory(new Cliff(), new Dirt(), new Floor(), new Graveyard(), new GustOfWind(), new PuddleOfWater(),
                        new SummonSign(), new Wall()),
                MapDisplay.LIMGRAVE, world);

        // Add the Player at it's spawning location for Limgrave.
        world.addPlayer(player, getGameMap().at(this.getxSpawnLocation(), this.getySpawnLocation()));
    }

    /**
     * A method to return the xSpawnLocation data attribute.
     * @return the x-coordinate of the spawning location of the Player for this Map.
     */
    public int getxSpawnLocation() {
        return xSpawnLocation;
    }

    /**
     * A method to return the ySpawnLocation data attribute.
     * @return the y-coordinate of the spawning location of the Player for this Map.
     */
    public int getySpawnLocation() {
        return ySpawnLocation;
    }
}
