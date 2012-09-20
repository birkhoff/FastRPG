/**
 * Library for all objects, which may be relevant (e.g. collisions are important for heroes and enemies)
 */

package engine;

import Interfaces.IGameObject;

public class Lib {

	public boolean isSolid(int x, int y, IGameObject obj){
		if(obj.isSolid(x, y)){
			return true;
		}
		return false;
	}
	
}
