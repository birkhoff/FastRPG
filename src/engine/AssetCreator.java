/**
 * Creates objects, mobs, npcs, ...
 */

package engine;

import java.util.LinkedList;
import java.util.List;

import chars.Mob;


public class AssetCreator {
	private static List<Mob> Mobs = new LinkedList<Mob>();
	
	public static void getEnemiesFromMap(Map island) {
//		Objectgroup group = island.getObjectgroup("mobs");
//		Entity[] entity = group.getObjetcs();
	}
	
	public static void createEnemy(String name, float x, float y) {
		Mobs.add(new Mob(name, x, y));
	}
	public static void remove(Object obj) {
		Mobs.remove(obj);
	}
	public static List<Mob> getMobs() {
		return Mobs;
	}
	public static void setMobs(List<Mob> mobs) {
		Mobs = mobs;
	}
}
