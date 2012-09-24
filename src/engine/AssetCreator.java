/**
 * Creates objects, mobs, npcs, ...
 * Objects MUST be located in a new objectgroup on 
 */

package engine;

import java.util.LinkedList;
import java.util.List;

import chars.Mob;
import chars.NPC;


public class AssetCreator {
	private static LinkedList<Mob> Mobs = new LinkedList<Mob>();
	private static LinkedList<NPC> NPCs = new LinkedList<NPC>();
	
	public static void getEnemiesFromMap(Map island) {
		if (island.getObjectGroup("mobs") != null) {
			Objectgroup group = island.getObjectGroup("mobs");
			Entity[] entity = group.getObjetcs();
			for (int i = 0; i < entity.length; i++) {
				System.out.println("Entities: "+entity.length);
				createEnemy(entity[i].getName(), entity[i].getX(), entity[i].getY());
			}
		}
	}
	
	public static void createAssets(Map island) {
		getEnemiesFromMap(island);
		getNPCSFromMap(island);
	}
	
	public static void getNPCSFromMap(Map island) {
		if (island.getObjectGroup("npcs") != null) {
			Objectgroup group = island.getObjectGroup("npcs");
			Entity[] entity = group.getObjetcs();
			for (int i = 0; i < entity.length; i++) {
				System.out.println("Entities: "+entity.length);
				createNPC(entity[i].getName(), entity[i].getX(), entity[i].getY());
			}
		}
	}
	public static void createEnemy(String name, float x, float y) {
		Mobs.add(new Mob(name, x, y));
	}
	public static void createNPC(String name, float x, float y) {
		NPCs.add(new NPC(name, x, y));
	}
	public static void remove(Object obj) {
		Mobs.remove(obj);
	}
	public static LinkedList<Mob> getMobs() {
		return Mobs;
	}
}
