package game;

import edu.monash.fit2099.engine.displays.Display;
import edu.monash.fit2099.engine.positions.GameMap;
import edu.monash.fit2099.engine.positions.World;
import game.characters.non_playable_characters.FingerReaderEnia;
import game.characters.non_playable_characters.MerchantKale;
import game.characters.playable_characters.Player;
import game.displays.FancyMessage;
import game.enemies.stormveil.GodrickTheGrafted;
import game.environments.Altar;
import game.environments.GoldenFogDoor;
import game.environments.GroundStatus;
import game.environments.SiteOfLostGrace;
import game.items.GoldenRunes;
import game.items.SacredTears;
import game.maps.*;
import game.utils.ResetManager;

/**
 * The main class to start the game.
 * Created by:
 * @author Adrian Kristanto
 * Modified by:
 * @author Bryan Wong
 * @version 1.0
 * @see Display
 * @see GameMap
 * @see World
 * @see FingerReaderEnia
 * @see MerchantKale
 * @see Player
 * @see FancyMessage
 * @see GodrickTheGrafted
 * @see Altar
 * @see GoldenFogDoor
 * @see GroundStatus
 * @see SiteOfLostGrace
 * @see GoldenRunes
 * @see SacredTears
 * @see BossRoom
 * @see Limgrave
 * @see MapDisplay
 * @see MapManager
 * @see RoundtableHold
 * @see StormveilCastle
 * @see ThirdChurchOfMarika
 * @see ResetManager
 */

public class Application {
	/**
	 * Main Game Application
	 * Will create the World and GameMap at start.
	 */
	public static void main(String[] args) throws Exception {
		World world = new World(new Display());

		// BEHOLD, ELDEN RING
		for (String line : FancyMessage.ELDEN_RING.split("\n")) {
			new Display().println(line);
			try {
				Thread.sleep(200);
			} catch (Exception exception) {
				exception.printStackTrace();
			}
		}

		// Create a Player class with 300 hitpoints.
		Player player = new Player("Tarnished", '@', 300);

		// Create Limgrave GameMap
		GameMap limgraveMap = new Limgrave(world, player).getGameMap();
		// Generate random amount of Golden Runes in it.
		MapManager.getInstance(limgraveMap).generateGoldenRunes(new GoldenRunes(), 3, 5);
		// Create the First Step Site of Lost Grace and add it manually to Limgrave.
		SiteOfLostGrace limgraveSite = MapManager.getInstance(limgraveMap).createSiteOfLostGrace("The First Step", 38, 11);
		// Activate the First Step by default and add it to the list of Activate Sites.
		limgraveSite.addCapability(GroundStatus.ACTIVATED);
		MapManager.getInstance(limgraveMap).addActivatedSite(limgraveSite);

		// Manually set the last Site of Lost Grace rested to the Limgrave's Site of Lost Grace.
		ResetManager.getInstance().setLastLostGrace(limgraveSite);

		// Create Roundtable Hold GameMap.
		GameMap roundtableHoldMap = new RoundtableHold(world).getGameMap();
		// Create the Table of Lost Grace Site of Lost Grace and add it manually to Roundtable Hold.
		MapManager.getInstance(roundtableHoldMap).createSiteOfLostGrace("Table of Lost Grace", 9, 5);

		// Create Stormveil Castle GameMap.
		GameMap stormveilCastleMap = new StormveilCastle(world).getGameMap();
		// Generate random amount of Golden Runes in it.
		MapManager.getInstance(stormveilCastleMap).generateGoldenRunes(new GoldenRunes(), 3, 5);
		// Create the Stormveil Main Gate Site of Lost Grace and add it manually to Stormveil Castle.
		MapManager.getInstance(stormveilCastleMap).createSiteOfLostGrace("Stormveil Main Gate", 38, 20);

		// Create the BossRoom GameMap.
		GameMap bossRoomMap = new BossRoom(world).getGameMap();

		// Create the Third Church of Marika GameMap.
		GameMap churchOfMarikeMap = new ThirdChurchOfMarika(world).getGameMap();
		// Create the Third Church of Marika Site of Lost Grace and add it manually to the Third Church of Marika.
		MapManager.getInstance(churchOfMarikeMap).createSiteOfLostGrace("Third Church of Marika", 14, 4);

		// Add the Altar to the Third Church of Marika along with the predefined data attributes.
		// 5000 starting offering price, increment by 5000 each time an offering is made.
		// Limit of 12 offerings only and will be blessed with SacredTears when offering is made.
		churchOfMarikeMap.at(29, 4).setGround(new Altar(3000, 3000, 12, new SacredTears()));

		// Create a GoldenFogDoor to set Roundtable Hold destination, then set its Location manually into the Limgrave GameMap.
		// Call the MapManager createGoldenFogDoors method.
		GoldenFogDoor limgraveToRoundtableHold = MapManager.getInstance(roundtableHoldMap).createGoldenFogDoors("Roundtable Hold", limgraveMap, 6, 23);
		// Create a GoldenFogDoor to set Limgrave destination, then set its Location manually into the Roundtable Hold GameMap.
		// Call the MapManager createGoldenFogDoors method.
		GoldenFogDoor roundtableHoldtoLimgrave = MapManager.getInstance(limgraveMap).createGoldenFogDoors("Limgrave", roundtableHoldMap, 9, 10);

		// Set the destination to limgraveToRoundtableHold Golden Fog Door to roundtableHoldtoLimgrave and vice versa.
		MapManager.connectGoldenFogDoors(limgraveToRoundtableHold, roundtableHoldMap.at(9, 10));
		MapManager.connectGoldenFogDoors(roundtableHoldtoLimgrave, limgraveMap.at(6, 23));

		// Create a GoldenFogDoor to set Stormveil Castle destination, then set its Location manually into the Limgrave GameMap.
		// Call the MapManager createGoldenFogDoors method.
		GoldenFogDoor limgraveToStormveilCastle = MapManager.getInstance(stormveilCastleMap).createGoldenFogDoors("Stormveil Castle", limgraveMap, 29, 0);
		// Create a GoldenFogDoor to set Limgrave destination, then set its Location manually into the Stormveil Castle GameMap.
		// Call the MapManager createGoldenFogDoors method.
		GoldenFogDoor stormveilCastleToLimgrave = MapManager.getInstance(limgraveMap).createGoldenFogDoors("Limgrave", stormveilCastleMap, 38, 23);

		// Set the destination to limgraveToStormveilCastle Golden Fog Door to stormveilCastleToLimgrave and vice versa.
		MapManager.connectGoldenFogDoors(limgraveToStormveilCastle, stormveilCastleMap.at(38, 23));
		MapManager.connectGoldenFogDoors(stormveilCastleToLimgrave, limgraveMap.at(29, 0));

		// Create a GoldenFogDoor to set Third Church of Marika destination, then set its Location manually into the Limgrave GameMap.
		// Call the MapManager createGoldenFogDoors method.
		GoldenFogDoor limgraveToChurchOfMarika = MapManager.getInstance(churchOfMarikeMap).createGoldenFogDoors("Third Church of Marika", limgraveMap, 74, 11);
		// Create a GoldenFogDoor to set Limgrave destination, then set its Location manually into the Stormveil Castle GameMap.
		// Call the MapManager createGoldenFogDoors method.
		GoldenFogDoor churchOfMarikaToLimgrave = MapManager.getInstance(limgraveMap).createGoldenFogDoors("Limgrave", churchOfMarikeMap, 1, 4);

		// Set the destination to limgraveToChurchOfMarika Golden Fog Door to churchOfMarikaToLimgrave and vice versa.
		MapManager.connectGoldenFogDoors(limgraveToChurchOfMarika, churchOfMarikeMap.at(1, 4));
		MapManager.connectGoldenFogDoors(churchOfMarikaToLimgrave, limgraveMap.at(74, 11));

		// Create a GoldenFogDoor to set Boss Room destination, then set its Location manually into the Stormveil Castle GameMap.
		// Call the MapManager createGoldenFogDoors method.
		GoldenFogDoor stormveilToBoss = MapManager.getInstance(bossRoomMap).createGoldenFogDoors("Boss Room", stormveilCastleMap, 5, 0);
		// Set the destination of the Golden Fog Door to the Boss Room.
		MapManager.connectGoldenFogDoors(stormveilToBoss, bossRoomMap.at(0, 4));

		// Create a Boss Godrick The Grafted and add it to the Boss Room.
		bossRoomMap.at(24, 4).addActor(new GodrickTheGrafted(bossRoomMap));

		// Create Merchant Kale and set it to spawn in the center building of Limgrave.
		limgraveMap.at(40, 12).addActor(new MerchantKale());
		// Create Finger Reader Emilia and set it to spawn at the back of the Roundtable Hold.
		roundtableHoldMap.at(9, 1).addActor(new FingerReaderEnia());

		// Run the game.
		world.run();
	}
}
