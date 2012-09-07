package engine;

public class Tileset {
	
	private int firstgid, tilewidth, tileheight, spacing, margin, imagewidth, imageheight;
	private String name, imagesource;

	
	public Tileset(int firstgid, String name, int tilewidth, int tileheight, int spacing, int margin, String imgsource, int imgwidth,
					int imgheight){
		this.firstgid = firstgid;
		this.name = name;
		this.tileheight = tileheight;
		this.tilewidth = tilewidth;
		this.spacing = spacing;
		this.margin = margin;
		this.imagesource = imgsource;
		this.imagewidth = imgwidth;
		this.imageheight = imgheight;
	}

	public int getImagewidth() {
		return imagewidth;
	}


	public int getImageheight() {
		return imageheight;
	}


	public String getImagesource() {
		return imagesource;
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
