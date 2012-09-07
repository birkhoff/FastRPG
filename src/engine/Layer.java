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
