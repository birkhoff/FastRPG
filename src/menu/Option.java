/**
 * Objekte zum Auswaehlen in den Menues. Spaeter auch mit Auswahl eines
 * Bildchens, das stattdessen erscheint
 */

package menu;

public class Option {
	private String text;
	
	public Option(String text) {
		this.text = text;
	}
	public String getText() {
		return this.text;
	}
}
