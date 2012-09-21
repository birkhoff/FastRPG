/**
 * Creates objects, mobs, npcs, ...
 */

package engine;

import java.util.LinkedList;
import java.util.List;

import chars.Mob;


public class AssetCreator {
	private static LinkedList<Mob> Mobs = new LinkedList<Mob>();
	
	public static void getEnemiesFromMap(Map island) {
		Objectgroup group = island.getObjectGroup("mobs");
		Entity[] entity = group.getObjetcs();
		for (int i = 0; i < entity.length; i++) {
			System.out.println("Entities: "+entity.length);
			createEnemy(entity[i].getName(), entity[i].getX(), entity[i].getY());
		}
	}
	
	public static void createEnemy(String name, float x, float y) {
		Mobs.add(new Mob(name, x, y));
	}
	public static void remove(Object obj) {
		Mobs.remove(obj);
	}
	public static LinkedList<Mob> getMobs() {
		return Mobs;
	}
}
