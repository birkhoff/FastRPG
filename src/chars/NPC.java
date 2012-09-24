package chars;

import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class NPC {
	private float position[];		// 0 = x position, 1 = y position
	private BufferedImage image;
	private int hp;
	private String name = "";

	private float stepsize = 3f;
	
	public NPC(String name, float x, float y) {
		this.name = name;
		try {
			if (name.equals("Grumpy Old Dad")) {
				System.out.println("Male einen NPC");
				setImage(ImageIO.read(new File("images/sprites/npcs/"+name+".png")));
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
	public void setImage(BufferedImage image) {
		this.image = image;
	}
	
	public BufferedImage getImage(){
		return image;
	}
	
	public float getPositionX(){
		return position[0];
	}
	
	public float getPositionY(){
		return position[1];
	}
}
