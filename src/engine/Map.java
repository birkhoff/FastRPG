package engine;

import java.io.File;
import java.io.IOException;
import java.util.List;

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
	
	private Document doc;
	
	public Map(String MapTMX /* path to *TMX */ ){
		
		SAXBuilder parser = new SAXBuilder();
        try {
            this.doc = parser.build(MapTMX);
        } catch (JDOMException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
        		ex.printStackTrace();
        }
         
		
		//create Tileset-Array
		tilesets = new Tileset[doc.getRootElement().getChildren("tileset").size()];
		for(int i=0; i < tilesets.length; i++){
			List<Element> tileset = doc.getRootElement().getChildren("tileset");
			int fgid = Integer.parseInt(tileset.get(i).getAttributeValue("firstgid"));
			String name = tileset.get(i).getAttributeValue("name");
			int tilewidth = Integer.parseInt(tileset.get(i).getAttributeValue("tilewidth"));
			int tileheight = Integer.parseInt(tileset.get(i).getAttributeValue("tileheight"));
			int spacing = 0;
			if (tileset.get(i).getAttributeValue("spacing") != null){
				spacing = Integer.parseInt(tileset.get(i).getAttributeValue("spacing"));
			}
			int margin = 0;
			if (tileset.get(i).getAttributeValue("margin") != null){
				margin = Integer.parseInt(tileset.get(i).getAttributeValue("margin"));
			}
			String source = tileset.get(i).getChild("image").getAttributeValue("source");
			int imgwidth = Integer.parseInt(tileset.get(i).getChild("image").getAttributeValue("width"));
			int imgheight = Integer.parseInt(tileset.get(i).getChild("image").getAttributeValue("height"));	
			tilesets[i] = new Tileset(fgid, name, tilewidth, tileheight, spacing, margin, source, imgwidth, imgheight);
			if(tileset.get(i).getChildren("tile") != null){
				List<Element> tiles = tileset.get(i).getChildren("tile");
				for(int j=0; j < tiles.size(); j++){
					int tileid = Integer.parseInt(tiles.get(j).getAttributeValue("id"));
					List<Element> props = tiles.get(j).getChildren("properties").get(0).getChildren("property");
					Property[] propsArr = new Property[props.size()];
					for(int k=0; k < props.size(); k++){
						propsArr[k] = new Property(props.get(k).getAttributeValue("name"),
												   props.get(k).getAttributeValue("value"));
					}
					tilesets[i].addProperty(tileid, propsArr);
				}
			}
		}
		
		// create Layers Array
		layers = new Layer[doc.getRootElement().getChildren("layer").size()];
		for(int i=0; i < layers.length; i++){
			layers[i] = new Layer(
					doc.getRootElement().getChildren("layer").get(i).getAttributeValue("name"),
					doc.getRootElement().getChildren("layer").get(i).getChildText("data")
			);
		}
		
		//create Objectgroup Array
		objectgroups = new Objectgroup[doc.getRootElement().getChildren("objectgroup").size()];
		for(int i=0; i < objectgroups.length; i++){
			Element obj = doc.getRootElement().getChildren("objectgroup").get(i);
			List<Element> entitys = obj.getChildren("object");
			Entity[] ents = new Entity[entitys.size()];
			for(int j=0; j < ents.length; j++){
				String name = entitys.get(j).getAttributeValue("name");
				int x = Integer.parseInt(entitys.get(j).getAttributeValue("x"));
				int y = Integer.parseInt(entitys.get(j).getAttributeValue("y"));
				Entity tempEnt = new Entity(name, x, y);
				tempEnt.setHeight(Integer.parseInt(entitys.get(j).getAttributeValue("height")));
				tempEnt.setWidth(Integer.parseInt(entitys.get(j).getAttributeValue("width")));
				tempEnt.setType(entitys.get(j).getAttributeValue("type"));
				if(entitys.get(j).getChildren("properties") != null){
					List<Element> properties = entitys.get(j).getChildren("properties");
					Property[] tempProps = new Property[properties.get(0).getChildren("property").size()];
					for(int k=0; k< tempProps.length; k++){
						tempProps[k] = new Property(
								properties.get(0).getChildren("property").get(k).getAttributeValue("name"),
								properties.get(0).getChildren("property").get(k).getAttributeValue("value")
						);
					}
					tempEnt.newProperties(tempProps);
				}
				ents[j] = tempEnt;
			}
			objectgroups[i] = new Objectgroup(ents, obj.getAttributeValue("name"));
		}
		
	}
	
	//*************** Map-Attribute-Getter *******************//
	public String getOrientation(){
		return doc.getRootElement().getAttributeValue("orientation");
	}
	
	public int getWidth(){ //width in pixels
		int x = Integer.parseInt(doc.getRootElement().getAttributeValue("tilewidth"));
		int tx = Integer.parseInt(doc.getRootElement().getAttributeValue("width"));
		return x*tx;
	}
	
	public int getWidthInTiles(){
		return Integer.parseInt(doc.getRootElement().getAttributeValue("width"));
	}
	
	public int getHeightInTiles(){
		return Integer.parseInt(doc.getRootElement().getAttributeValue("height"));
	}
	
	public int getHeight(){ //height in tiles
		int y = Integer.parseInt(doc.getRootElement().getAttributeValue("tileheight"));
		int ty = Integer.parseInt(doc.getRootElement().getAttributeValue("height"));
		return y*ty;
	}
	
	public int getTileWidth(){
		return Integer.parseInt(doc.getRootElement().getAttributeValue("tilewidth"));
	}
	
	public int getTileHeight(){
		return Integer.parseInt(doc.getRootElement().getAttributeValue("tileheight"));
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
	
	public int[][] getFormattedTiles(int index /*Tiles of which layer?!*/){
		int[][] temp = new int[getWidthInTiles()][getHeightInTiles()];
		String foo = getLayerTiles(index).replaceAll("\n", "");
		String[] rows = foo.split(",");
		for(int i=0; i < getHeightInTiles(); i++){
			for(int j=0; j < getWidthInTiles(); j++){
				temp[j][i] = Integer.parseInt(rows[j+i*getWidthInTiles()]);
			}
		}
		return temp;
	}
	
	public int getObjectGroupCount(){
		return objectgroups.length;
	}
	
	public Objectgroup getObjectgroup(int i){
		return objectgroups[i];
	}
	
	//*************** End of Getter **************************//

}
