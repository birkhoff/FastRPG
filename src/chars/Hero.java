/**
 * The Hero class. Having attributes, position and the image file
 */
package chars;

import java.awt.Image;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Hero {
	private int position[];		// 0 = x position, 1 = y position
	private Image image;
	private int hp;
	private String name = "Insane";
	
	public Hero() {
		try {
			image = ImageIO.read(new File("images/hero.png"));
		} catch (IOException e) {				
			System.out.println("Bild von "+this.name+" konnte nicht geladen werden.");
		}
		position = new int[2];
		position[0] = 100;
		position[1] = 100;
	}
	
/******************* Getter und Setter ******************/
	public int getPositionX() {
		return position[0];
	}
	public int getPositionY() {
		return position[1];
	}
	public int[] getPosition() {
		return position;
	}
	public void setPosition(int[] position) {
		this.position = position;
	}
	public void setPositionX(int position) {
		this.position[0] = position;
	}
	public void setPositionY(int position) {
		this.position[1] = position;
	}

	public Image getImage() {
		return image;
	}

	public void setImage(Image image) {
		this.image = image;
	}

	public int getHp() {
		return hp;
	}

	public void setHp(int hp) {
		this.hp = hp;
	}

	
}
