package chars;

import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import engine.AssetCreator;
import interfaces.IGameChars;


public class Mob implements IGameChars {
	private float position[];		// 0 = x position, 1 = y position
	private BufferedImage image;
	private int hp;
	private String name = "";

	private float stepsize = 3f;
	
	public Mob(String name, float x, float y) {
		this.name = name;
		try {
			if (name.equals("gumba")) {
				System.out.println("Male einen Gumba");
				setImage(ImageIO.read(new File("images/sprites/mobs/"+name+".png")));
			}
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
	
	public void walk() {
		position[0] += (float) (Math.random()*stepsize);
		position[1] += (float) (Math.random()*stepsize);
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
		if (hp <= 0) {
			AssetCreator.remove(this);
		}
		this.hp = hp;
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
