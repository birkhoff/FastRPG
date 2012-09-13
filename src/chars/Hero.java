/**
 * The Hero class. Having attributes, position and the image file
 */
package chars;

import java.awt.Image;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

enum Look {
	N, NE, E, SE, S, SW, W, NW
}

public class Hero {
	private float position[];		// 0 = x position, 1 = y position
	private Image image;
	private int hp;
	private String name = "Insane";
	private Look look = Look.S;
	
	public Hero() {
		try {
			image = ImageIO.read(new File("images/hero.png"));
		} catch (IOException e) {				
			System.out.println("Bild von "+this.name+" konnte nicht geladen werden.");
		}
		position = new float[2];
		position[0] = 100;
		position[1] = 100;
	}
	
/******************* Getter und Setter ******************/
	public float getPositionX() {
		return position[0];
	}
	public float getPositionY() {
		return position[1];
	}
	public float[] getPosition() {
		return position;
	}
	public void setPosition(float[] position) {
		this.position = position;
	}
	public void setPositionX(float position) {
		this.position[0] = position;
	}
	public void setPositionY(float position) {
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
	public Look getLook() {
		return look;
	}
	public void setLook(Look look) {
		this.look = look;
	}
}
