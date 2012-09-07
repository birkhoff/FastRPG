package engine;

public class Tileset {
	
	private int firstgid, tilewidth, tileheight, spacing, margin, imagewidth, imageheight;
	private String name, imagesource, imagetransparency;

	
	public Tileset(int firstgid, String name, int tilewidth, int tileheight, int spacing, int margin, String imgsource, int imgwidth,
					int imgheight, String imgtrans){
		this.firstgid = firstgid;
		this.name = name;
		this.tileheight = tileheight;
		this.tilewidth = tilewidth;
		this.spacing = spacing;
		this.margin = margin;
		this.imagesource = imgsource;
		this.imagewidth = imgwidth;
		this.imageheight = imgheight;
		this.imagetransparency = imgtrans;
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


	public String getImagetransparency() {
		return imagetransparency;
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
