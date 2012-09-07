package engine;

import org.w3c.dom.Document;

// Has the ability to read out *TMX-Files and build a Map out of them
public class Map {
	
	private Document MapXML;
	
	public Map(String MapTMX /* path to *TMX */ ){
		// create a new Mapinstance with the corresponding XMl-Document and name
		XMLReader reader = new XMLReader();
		MapXML = reader.getDocument(MapTMX);
	}
	
	public String getOrientation(){
		return MapXML.getElementsByTagName("map").item(0).getAttributes().getNamedItem("orientation").getNodeValue();
	}

}
