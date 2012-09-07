package gui;

import engine.*;

public class Main {

	public static void main(String[] args){
		Map island = new Map("maps/map1.tmx");
		System.out.println(island.getOrientation());
	}
}
