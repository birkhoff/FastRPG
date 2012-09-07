package gui;

import engine.*;

public class Main {

	public static void main(String[] args){
		Map island = new Map("maps/map1.tmx");
		System.out.println(island.getOrientation());
		System.out.println(island.getWidth());
		System.out.println(island.getHeight());
		System.out.println(island.getWidthInTiles());
		System.out.println(island.getHeightInTiles());
		System.out.println(island.getTileWidth());
		System.out.println(island.getTileHeight());
	}
}
