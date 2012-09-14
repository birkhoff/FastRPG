/**
 * The Hero class. Having attributes, position and the image file
 */
package chars;

import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

enum Look {
	N, NE, E, SE, S, SW, W, NW
}

public class Hero {
	private float position[];		// 0 = x position, 1 = y position
	private BufferedImage image;
	BufferedImage[] hero;
	private int hp;
	private String name = "Insane";
	private Look look = Look.S;
	private int i = 0;
	private int k = 0;
	private float stepsize = 1f;
	
	public Hero() {
		try {
			image = ImageIO.read(new File("images/sprites/CompleteMoveSmall.png"));
			// The above line throws an checked IOException which must be caught.

			final int width = 375;
			final int height = 500;
			final int rows = 14;
			final int cols = 1;
			hero = new BufferedImage[rows * cols];

			for (int i = 0; i < rows; i++)
			{
			    for (int j = 0; j < cols; j++)
			    {
			        hero[(i * cols) + j] = image.getSubimage(
			            j * width,
			            i * height,
			            width,
			            height
			        );
			    }
			}
		} catch (IOException e) {				
			System.out.println("Bild von "+this.name+" konnte nicht geladen werden.");
		}
		position = new float[2];
		position[0] = 100;
		position[1] = 100;
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
	public Image getImage() {
		if(k>3){
			i=i+1;
			k = 0;
		}
		i= i%6;
		k+=1;
		return toImage(hero[i+1]);
	}
	public void setImage(Image image) {
		this.image = (BufferedImage) image;
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
}
