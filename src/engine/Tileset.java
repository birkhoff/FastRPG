package engine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Tileset {
	
	private int firstgid, tilewidth, tileheight, spacing, margin, imagewidth, imageheight;
	private String name, imagesource;
	private List<Integer> propertyTiles; 
	private HashMap<Integer, Property[]> propertyMap;

	
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
		propertyTiles = new ArrayList<Integer>();
		propertyMap = new HashMap<Integer, Property[]>();
	}

	public void addProperty(int tileid, Property[] property){
		propertyTiles.add(new Integer(tileid));
		propertyMap.put(new Integer(tileid), property);
	}
	
	public Property[] getProperty(int tileid){
		return propertyMap.get(new Integer(tileid));
	}
	
	public boolean hasProperty(int tileid){
		if (propertyTiles.contains(new Integer(tileid))){
			return true;
		}
		return false;
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
