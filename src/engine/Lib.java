/**
 * Library for all objects, which may be relevant (e.g. collisions are important for heroes and enemies)
 */

package engine;

import chars.*;

public class Lib {
	/**
	 * 
	 * @return
	 */
	public static float[] getCenterHero(Hero hero) {
		float[] center = new float[2];
		center[0] = hero.getWidth() / 2;
		center[1] = hero.getHeight() / 2;
		return center;
	}
}
