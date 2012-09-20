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
	private float stepsize = 30f;
	
	// Properties of Width
	private int width;
	private int height;
	private int rows;
	private int cols;
	
	public Hero(float x, float y) {
		try {
			image = ImageIO.read(new File("images/sprites/HeroSlash.png"));
			// The above line throws an checked IOException which must be caught.
			setWidth(113);
			setHeight(150);
			setRows(20);
			setCols(1);

			hero = new BufferedImage[getRows() * getCols()];

			for (int i = 0; i < getRows(); i++)
			{
			    for (int j = 0; j < getCols(); j++)
			    {
			        hero[(i * getCols()) + j] = image.getSubimage(
			            j * getWidth(),
			            i * getHeight(),
			            getWidth(),
			            getHeight()
			        );
			    }
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
		if(k>1){
			i=i+1;
			k = 0;
		}
		i= i%20;
		k+=1;
		return toImage(hero[i]);
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
	public int getWidth() {
		return width;
	}
	public void setWidth(int width) {
		this.width = width;
	}
	public int getHeight() {
		return height;
	}
	public void setHeight(int height) {
		this.height = height;
	}
	public int getRows() {
		return rows;
	}
	public void setRows(int rows) {
		this.rows = rows;
	}
	public int getCols() {
		return cols;
	}
	public void setCols(int cols) {
		this.cols = cols;
	}
}
