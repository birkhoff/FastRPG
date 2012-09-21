package chars;

import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import interfaces.IGameChars;

enum LookMob {
	N, NE, E, SE, S, SW, W, NW
}

public class Mob implements IGameChars {
	private float position[];		// 0 = x position, 1 = y position
	private BufferedImage image;
	BufferedImage[] hero;
	private int hp;
	private String name = "Mobby";
	private Look look = Look.S;

	private float stepsize = 3f;
	
	public Mob(String name, float x, float y) {
		this.name = name;
		try {
			if (name.equals("Gumba"))
				setImage(ImageIO.read(new File("images/sprites/"+name+".png")));
		} catch (IOException e) {				
			System.out.println("Bild von "+this.name+" konnte nicht geladen werden.");
		}
		position = new float[2];
		position[0] = x;
		position[1] = y;
	}
	
	public Image toImage(BufferedImage bufferedImage) {
	    return Toolkit.getDefaultToolkit().createImage(bufferedImage.getSource());
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
	public float getStepsize() {
		return stepsize;
	}
	public void setStepsize(float stepsize) {
		this.stepsize = stepsize;
	}

	public BufferedImage getImage() {
		return image;
	}

	public void setImage(BufferedImage image) {
		this.image = image;
	}

	@Override
	public int getWidth() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getHeight() {
		// TODO Auto-generated method stub
		return 0;
	}
}
