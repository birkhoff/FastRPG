/**
 * Library for all objects, which may be relevant (e.g. collisions are important for heroes and enemies)
 */

package engine;
import Interfaces.IGameObject;
import chars.*;

public class Lib {

	public boolean isSolid(int x, int y, IGameObject obj){
		if(obj.isSolid(x, y)){
			return true;
		}
		return false;
	}
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
