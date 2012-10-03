/**
 * Start of the Engine. Mainly to execute the Program for the First time
 */

package engine;

import gui.GamePanel;

public class Main {
	private static int DEFAULT_FPS = 100;
	private static GamePanel panel;
	
	public static void main(String args[]) {
    	int fps = DEFAULT_FPS;
    	long period = (long) 1000.0 / fps;
        setPanel(new GamePanel(period * 1000000L));
    }
	public static GamePanel getPanel() {
		return panel;
	}
	public static void setPanel(GamePanel panel) {
		Main.panel = panel;
	}
	public static int getMapPosX() {
		return panel.getMapPosX();
	}
	public static int getMapPosY() {
		return panel.getMapPosY();
	}
}
