/**
 * Hier alle moeglichen Arten von Menues wie Hauptmenue, Pausemenue, ... erstellen
 */

package menu;

import java.awt.Color;
import java.awt.Graphics2D;

public class Menu {
	
	public static void showMainMenu(Graphics2D g, int selection, int mWidth, int mHeight) {
		int heightPos = 1; 
		int start = 100;
		g.setColor(Color.black);
		g.drawString("Hauptmenue", mWidth/2-30, 100);
		Option option[] = new Option[5];
		option[0] = new Option("Spiel starten");
		option[1] = new Option("Spielstand laden");
		option[2] = new Option("Einstellungen");
		option[3] = new Option("Die ultimativen Entwickler");
		option[4] = new Option("Einstellungen");
		for (int i = 0; i < option.length; i++) {
			if (selection == i) {
				g.setColor(Color.red);
				g.drawString(option[i].getText(), mWidth/2-30, start+30*heightPos);
				System.out.println("Schreibe "+option[i].getText());
				heightPos++;
			} else {
				g.setColor(Color.black);
				g.drawString(option[i].getText(), mWidth/2-30, start+30*heightPos);
				System.out.println("Schreibe "+option[i].getText());
				heightPos++;
			}
		}
		
	}
}