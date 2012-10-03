/**
 * Creates objects, mobs, npcs, ...
 * Objects MUST be located in a new objectgroup in the TMX-File
 */

package engine;

import java.util.LinkedList;

import chars.Mob;
import chars.NPC;


public class AssetCreator {
	private static LinkedList<Mob> Mobs = new LinkedList<Mob>();
	private static LinkedList<NPC> NPCs = new LinkedList<NPC>();
	
	// Method creates all Assets from the Map <island>
	public static void createAssets(Map island) {
		getEnemiesFromMap(island);
		getNPCSFromMap(island);
	}
	
	public static void removeAll(){
		Mobs.removeAll(Mobs);
		NPCs.removeAll(NPCs);
	}
	
	private static void getEnemiesFromMap(Map island) {
		if (island.getObjectGroup("mobs") != null) {
			Objectgroup group = island.getObjectGroup("mobs");
			Entity[] entity = group.getObjetcs();
			for (int i = 0; i < entity.length; i++) {
				createEnemy(entity[i].getName(), entity[i].getX(), entity[i].getY());
			}
		}
	}	
	
	private static void getNPCSFromMap(Map island) {
		if (island.getObjectGroup("npcs") != null) {
			Objectgroup group = island.getObjectGroup("npcs");
			Entity[] entity = group.getObjetcs();
			for (int i = 0; i < entity.length; i++) {
				createNPC(entity[i].getName(), entity[i].getX(), entity[i].getY());
			}
		}
	}
	private static void createEnemy(String name, float x, float y) {
		Mobs.add(new Mob(name, x, y));
	}
	private static void createNPC(String name, float x, float y) {
		NPCs.add(new NPC(name, x, y));
	}
	public static void remove(Object obj) {
		Mobs.remove(obj);
	}
	public static LinkedList<Mob> getMobs() {
		return Mobs;
	}
	public static LinkedList<NPC> getNPCs() {
		return NPCs;
	}
}
