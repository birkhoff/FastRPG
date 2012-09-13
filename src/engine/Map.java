package engine;

import java.io.File;
import java.io.IOException;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.DOMOutputter;
import org.jdom2.output.XMLOutputter;

// Has the ability to read out *TMX-Files and build a Map out of them
public class Map {
	
	private Document MapXML;
	private Tileset[] tilesets;
	private Layer[] layers;
	private Objectgroup[] objectgroups;
	
	public Map(String MapTMX /* path to *TMX */ ){
		
		SAXBuilder parser = new SAXBuilder();
        try {
            Document doc = parser.build(MapTMX);
        } catch (JDOMException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
        		ex.printStackTrace();
        }
        
		
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
		
		//create Objectgroup Array
		objectgroups = new Objectgroup[MapXML.getElementsByTagName("objectgroup").getLength()];
		for(int i=0; i < objectgroups.length; i++){
			Node node = MapXML.getElementsByTagName("objectgroup").item(i);
			node.getAttributes().getNamedItem("name").getNodeValue();
			NodeList entitys = node.
			Entity[] ents = new Entity[entitys.getLength()];
			for(int j=0; j < entitys.getLength(); j++){
				String name = entitys.item(j).getAttributes().getNamedItem("name").getNodeValue();
				int x = Integer.parseInt(entitys.item(j).getAttributes().getNamedItem("x").getNodeValue());
				int y = Integer.parseInt(entitys.item(j).getAttributes().getNamedItem("y").getNodeValue());
				Entity tempEnt = new Entity(name, x, y);
				tempEnt.setHeight(Integer.parseInt(entitys.item(j).getAttributes().getNamedItem("height").getNodeValue()));
				tempEnt.setWidth(Integer.parseInt(entitys.item(j).getAttributes().getNamedItem("width").getNodeValue()));
				tempEnt.setType(entitys.item(j).getAttributes().getNamedItem("type").getNodeValue());
				if(entitys.item(j).hasChildNodes()){
					NodeList properties = entitys.item(j).getChildNodes();
					Property[] tempProps = new Property[properties.item(0).getChildNodes().getLength()];
					for(int k=0; k< properties.item(0).getChildNodes().getLength(); k++){
						tempProps[k] = new Property(
								properties.item(0).getChildNodes().item(k).getAttributes().getNamedItem("name").getNodeValue(),
								properties.item(0).getChildNodes().item(k).getAttributes().getNamedItem("value").getNodeValue()
						);
					}
					tempEnt.newProperties(tempProps);
				}
				ents[j] = tempEnt;
			}
			objectgroups[i] = new Objectgroup(ents, node.getAttributes().getNamedItem("name").getNodeValue());
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
	
	public int getObjectGroupCount(){
		return objectgroups.length;
	}
	
	public Objectgroup getObjectgroup(int i){
		return objectgroups[i];
	}
	
	//*************** End of Getter **************************//

}
