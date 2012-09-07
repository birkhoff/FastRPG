package engine;

import org.w3c.dom.Document;
import org.w3c.dom.*;

// Has the ability to read out *TMX-Files and build a Map out of them
public class Map {
	
	private Document MapXML;
	
	public Map(String MapTMX /* path to *TMX */ ){
		// create a new Mapinstance with the corresponding XMl-Document and name
		XMLReader reader = new XMLReader();
		MapXML = reader.getDocument(MapTMX);
	}
	
	//*************** Map-Attribute-Getter *******************//
	public String getOrientation(){
		return MapXML.getElementsByTagName("map").item(0).getAttributes().getNamedItem("orientation").getNodeValue();
	}
	
	public int getWidth(){ //width in pixels
		int x = Integer.parseInt(MapXML.getElementsByTagName("map").item(0).getAttributes().getNamedItem("tilewidth").getNodeValue());
		int tx = Integer.parseInt(MapXML.getElementsByTagName("map").item(0).getAttributes().getNamedItem("width").getNodeValue());
		return x*tx;
	}
	
	public int getWidthInTiles(){
		return Integer.parseInt(MapXML.getElementsByTagName("map").item(0).getAttributes().getNamedItem("width").getNodeValue());
	}
	
	public int getHeightInTiles(){
		return Integer.parseInt(MapXML.getElementsByTagName("map").item(0).getAttributes().getNamedItem("height").getNodeValue());
	}
	
	public int getHeight(){ //height in tiles
		int y = Integer.parseInt(MapXML.getElementsByTagName("map").item(0).getAttributes().getNamedItem("tileheight").getNodeValue());
		int ty = Integer.parseInt(MapXML.getElementsByTagName("map").item(0).getAttributes().getNamedItem("height").getNodeValue());
		return y*ty;
	}
	
	public int getTileWidth(){
		return Integer.parseInt(MapXML.getElementsByTagName("map").item(0).getAttributes().getNamedItem("tilewidth").getNodeValue());
	}
	
	public int getTileHeight(){
		return Integer.parseInt(MapXML.getElementsByTagName("map").item(0).getAttributes().getNamedItem("tileheight").getNodeValue());
	}
	
	public int tilesetAmount(){  //retuns the amount of tilesets this map uses
		return MapXML.getElementsByTagName("tileset").getLength();
	}
	
	public Tileset getTileset(int index){ //returns Tileset #index
		NamedNodeMap att = MapXML.getElementsByTagName("tileset").item(index).getAttributes();
		int fgid = Integer.parseInt(att.getNamedItem("firstgid").getNodeValue());
		String name = att.getNamedItem("name").getNodeValue();
		int tilewidth = Integer.parseInt(att.getNamedItem("tilewidth").getNodeValue());
		int tileheight = Integer.parseInt(att.getNamedItem("tileheight").getNodeValue());
		int spacing = Integer.parseInt(att.getNamedItem("spacing").getNodeValue());
		int margin = Integer.parseInt(att.getNamedItem("margin").getNodeValue());
		return new Tileset(fgid, name, tilewidth, tileheight, spacing, margin);
	}
	
	//*************** End of Getter **************************//

}
