package gui;

import engine.*;

public class Main {

	public static void main(String[] args){
		Map island = new Map("maps/map1.tmx");
		System.out.println("Orientation:" + island.getOrientation());
		System.out.println(island.getWidth());
		System.out.println(island.getHeight());
		System.out.println(island.getWidthInTiles());
		System.out.println(island.getHeightInTiles());
		System.out.println(island.getTileWidth());
		System.out.println(island.getTileHeight());
		
		System.out.println(island.getTileset(0).getImagesource());
		System.out.println(island.getTileset(0).getName());
		System.out.println(island.getTileset(1).getSpacing());
		
		System.out.println(island.getLayerName(1));
		System.out.println(island.getLayerTiles(1));
		
		System.out.println(island.getObjectgroup(0).getName());
		System.out.println(island.getObjectgroup(0).getObjetcs()[0].getName());
		System.out.println(island.getObjectgroup(0).getObjetcs()[0].getType());
		System.out.println(island.getObjectgroup(0).getObjetcs()[0].getPropertie(0).getName());
		System.out.println(island.getObjectgroup(0).getObjetcs()[0].getPropertie(0).getValue());
		
		
		System.out.println(island.getTileset(0).hasProperty(12));
		System.out.println(island.getTileset(0).hasProperty(1));
		System.out.println(island.getTileset(0).getProperty(12)[0].getName());
		
		
	}
}
