/**
 * Hier alle moeglichen Arten von Menues wie Hauptmenue, Pausemenue, ... erstellen
 */

package menu;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class MainMenu implements KeyListener {
	private Graphics2D g;
	private int selection;
	private int mWidth;
	private int mHeight;
	
	public MainMenu(Graphics2D g, int selection, int mWidth, int mHeight) {
		this.g = g;
		this.selection = selection;
		this.mWidth = mWidth;
		this.mHeight = mHeight;
		displayMainMenu();
	}
	private void displayMainMenu() {
		int heightPos = 1; 
		int start = 100;
		g.setColor(Color.black);
		g.drawString("Hauptmenue", mWidth/2-30, 100);
		Option option[] = new Option[5];
		option[0] = new Option("Spiel starten");
		option[1] = new Option("Spielstand laden");
		option[2] = new Option("Einstellungen");
		option[3] = new Option("Die ultimativen Entwickler");
		option[4] = new Option("Spiel beenden");
		for (int i = 0; i < option.length; i++) {
			if (selection == i) {
				g.setColor(Color.red);
				g.drawString(option[i].getText(), mWidth/2-30, start+30*heightPos);
				heightPos++;
			} else {
				g.setColor(Color.black);
				g.drawString(option[i].getText(), mWidth/2-30, start+30*heightPos);
				heightPos++;
			}
		}
	}
	public void keyTyped(KeyEvent e) {
		int key = e.getKeyCode();
		if (key == KeyEvent.VK_DOWN) gui.GamePanel.setPositionInMainMenu(selection++);	
		System.out.println(selection);
	}
	public void keyReleased(KeyEvent e) {}
	public void keyPressed(KeyEvent e) {}
}