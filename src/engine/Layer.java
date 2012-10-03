/**
 * This class represents a Layer f the Map with a name and a unformatted tile-Stirng
 */

package engine;

public class Layer {
	private String name;
	private String tiles;
	
	public Layer(String name, String tiles) {
		this.name = name;
		this.tiles = tiles;
	}
	
	public String getName(){
		return name;
	}
	
	public String getTiles(){
		return tiles;
	}
}
