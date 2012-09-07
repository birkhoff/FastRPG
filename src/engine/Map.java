package engine;

import org.w3c.dom.Document;
import org.w3c.dom.*;

// Has the ability to read out *TMX-Files and build a Map out of them
public class Map {
	
	private Document MapXML;
	private Tileset[] tilesets;
	private Layer[] layers;
	
	public Map(String MapTMX /* path to *TMX */ ){
		// create a new Mapinstance with the corresponding XMl-Document and name
		XMLReader reader = new XMLReader();
		MapXML = reader.getDocument(MapTMX);
		
		//create Tileset-Array
		tilesets = new Tileset[MapXML.getElementsByTagName("tileset").getLength()];
		for(int i=0; i < tilesets.length; i++){
			NamedNodeMap att = MapXML.getElementsByTagName("tileset").item(i).getAttributes();
			int fgid = Integer.parseInt(att.getNamedItem("firstgid").getNodeValue());
			String name = att.getNamedItem("name").getNodeValue();
			int tilewidth = Integer.parseInt(att.getNamedItem("tilewidth").getNodeValue());
			int tileheight = Integer.parseInt(att.getNamedItem("tileheight").getNodeValue());
			int spacing = 0;
			
			//TODO NullpointerException
			try {
				spacing = Integer.parseInt(att.getNamedItem("spacing").getNodeValue());
			} catch (Exception e) { /**/ }
			int margin = 0;
			try {
				margin = Integer.parseInt(att.getNamedItem("margin").getNodeValue());
			} catch (Exception e) { /**/ }
			Node node = MapXML.getElementsByTagName("image").item(i);
			String source = node.getAttributes().getNamedItem("source").getNodeValue();
			int imgwidth = Integer.parseInt(node.getAttributes().getNamedItem("width").getNodeValue());
			int imgheight = Integer.parseInt(node.getAttributes().getNamedItem("height").getNodeValue());	
			tilesets[i] = new Tileset(fgid, name, tilewidth, tileheight, spacing, margin, source, imgwidth, imgheight);
		}
		
		// create Layers Array
		layers = new Layer[MapXML.getElementsByTagName("layer").getLength()];
		for(int i=0; i < layers.length; i++){
			layers[i] = new Layer(
					MapXML.getElementsByTagName("layer").item(i).getAttributes().getNamedItem("name").getNodeValue(),
					MapXML.getElementsByTagName("layer").item(i).getTextContent()
			);
		}
		
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
		return tilesets.length;
	}
	
	public Tileset getTileset(int index){ //returns Tileset #index
		// Doesnt support <tile> inside of <tileset> at the moment (needed for Properties) 
		return tilesets[index];
	}
	
	public int getLayerAmount(){ //returns Amount of layers
		return layers.length;
	}
	
	public String getLayerName(int index){
		return layers[index].getName();
	}
	
	public String getLayerTiles(int index){
		return layers[index].getTiles();
	}
	
	//*************** End of Getter **************************//

}
