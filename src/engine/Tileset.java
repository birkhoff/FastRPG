package engine;

public class Tileset {
	
	private int firstgid, tilewidth, tileheight, spacing, margin;
	private String name;
	
	public Tileset(int firstgid, String name, int tilewidth, int tileheight, int spacing, int margin){
		this.firstgid = firstgid;
		this.name = name;
		this.tileheight = tileheight;
		this.tilewidth = tilewidth;
		this.spacing = spacing;
		this.margin = margin;
	}
	

	public int getFirstGid() {
		return firstgid;
	}

	public int getTileWidth() {
		return tilewidth;
	}

	public int getTileHeight() {
		return tileheight;
	}

	public int getSpacing() {
		return spacing;
	}

	public int getMargin() {
		return margin;
	}

	public String getName() {
		return name;
	}

}
