package chars;

import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import javax.imageio.ImageIO;

public class NPC {
	private float position[];		// 0 = x position, 1 = y position
	private BufferedImage image;
	private int hp;
	private String name = "";
	private String conversation = "Lass uns Wildschweine jagen gehen";

	private float stepsize = 3f;
	
	public NPC(String name, float x, float y) {
		this.name = name;
		try {
			setImage(ImageIO.read(new File("images/sprites/npcs/"+name+".png")));
			BufferedReader in = new BufferedReader(new FileReader("chars/"+name+"/conversation.txt"));
			conversation = in.readLine();
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
	
	public String getText(int number){
		return conversation;
	}
	
	protected void setText(String text){
		this.conversation = text;
	}
}
