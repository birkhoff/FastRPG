/**
 * The Hero class. Having attributes, position and the image file
 */
package chars;

import interfaces.IGameChars;

import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;

import javax.imageio.ImageIO;

import engine.AssetCreator;
import engine.Main;

import weapons.SimpleSword;


public class Hero implements IGameChars {
	private float position[];		// 0 = x position, 1 = y position
	private BufferedImage image;
	BufferedImage[] hero;
	private int hp = 1000;
	private String name = "Insane";
	private int i = 0;
	private int k = 0;

	private float stepsize = 3f;
	
	// Properties of Width
	private int width;
	private int height;
	private int rows;
	private int cols;
	private boolean slash = false;
	private int numberOfSlashSprites;
	private int oneSlash;
	private int maxSlash;
	private boolean standing;
	private int look;
	private float stamina = 1f;
	
	//Loads a SimpleSword
	public SimpleSword sword;
	
	public Hero(float x, float y) {
		try {
			image = ImageIO.read(new File("images/sprites/HeroComplete2.png"));
			// The above line throws an checked IOException which must be caught.
			setWidth(113);
			setHeight(150);
			setRows(35);
			setCols(1);
			this.setNumberOfSlashSprites(10);		// number of slash pictures which will be shown after one 'slash-button' press
			oneSlash = 10;							// number of pictures of one slash
			maxSlash = 20;							// number of maximal slash pictures

			hero = new BufferedImage[getRows() * getCols()];
			sword = new SimpleSword();

			for (int i = 0; i < getRows(); i++) {
			    for (int j = 0; j < getCols(); j++) {
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
	
	public void slash(){
		setSlash(true);
		this.setStepsize(getStepsize()/2);
		i=0;
		k=0;
	}
	
	public void addStrike() {
		int i = getNumberOfSlashSprites()+oneSlash;
		if(i>=maxSlash) i = maxSlash;
		this.setNumberOfSlashSprites(i);
	}
	public void resetStrike(){
		this.setNumberOfSlashSprites(oneSlash);
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
	public void updateStrike(int spriteCount) {
		sword.setAlpha(sword.getAlpha()+10);
		sword.setImage(sword.rotateImage((int)sword.getAlpha()));
		sword.setX(this.getPositionX()-46+(float)(sword.getAlpha()/4));
		sword.setY(this.getPositionY()-23+(float)(sword.getAlpha()/4));
	}
	public Image getImage() {
		// GET THE SLASHING SPRITES
		if(isSlash()){		
			if(k>1){
				i=i+1;
				k = 0;
			}
			i= i%getNumberOfSlashSprites();
			k+=1;
			
			//Ending Slash Sequence
			if( i >= getNumberOfSlashSprites()-1){
				this.setSlash(false);
				this.setStepsize(getStepsize()*2);
				this.resetStrike();
				LinkedList<Mob> Mobs = AssetCreator.getMobs();
				for (int i = 0; i < Mobs.size(); i++) {
					Mob mob = Mobs.get(i);
					mob.setHit(false);
				}
				
				k = 0;
				i = 0;
				//this.sword.setImageBack();
			}
			return toImage(hero[i+15]);
		}
		
		// GET STANDING SPRITE
		if(isStanding()){
			k=0;
			i=0;
			return toImage(hero[1]);
		}
		//Get Front Moving Sprites
		if(this.getLook()==4 || this.getLook()==5 || this.getLook()==6){
			if(k>3){
				i=i+1;
				k = 0;
			}
			i= i%7;
			k+=1;
			return toImage(hero[i+1]);
		}
		
		//Get Back Moving Sprites
		if(this.getLook()==7 || this.getLook()==0 || this.getLook()==1){
			if(k>3){
				i=i+1;
				k = 0;
			}
			i= i%7;
			k+=1;
			return toImage(hero[i+8]);
		}
		return toImage(hero[0]);
	}
	
	public void setLook(int i){
		switch(i){
			case 0: this.look = 0;	 /*look.N; 	*/	break;
			case 1: this.look = 1;	/*look.NE;	*/	break;
			case 2: this.look = 2;	/*look.E;	*/	break;
			case 3: this.look = 3;	/*look.SE;	*/	break;
			case 4: this.look = 4;	/*look.S;	*/	break;
			case 5: this.look = 5;	/*look.SW;	*/	break;
			case 6: this.look = 6;	/*look.W;	*/	break;
			case 7: this.look = 7;	/*look.NW;	*/	break;
		}
	}
	
	public int getLook(){
		return this.look;
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
	public void setSlash(boolean slash) {
		this.slash = slash;
	}
	public boolean isSlash() {
		return slash;
	}
	public void setNumberOfSlashSprites(int numberOfSlashSprites) {
		this.numberOfSlashSprites = numberOfSlashSprites;
	}
	public int getNumberOfSlashSprites() {
		return numberOfSlashSprites;
	}
	public void setStanding(boolean standing) {
		this.standing = standing;
	}
	public boolean isStanding() {
		return standing;
	}
	public void setSword(SimpleSword sword) {
		this.sword = sword;
	}
	public SimpleSword getSword() {
		return sword;
	}
	
	public float getStamina(){
		return stamina;
	}
	
	public void regenerateStamina(){
		if (stamina < 1f)
			stamina += 0.0015f;
	}
	
	public void loseStamina(float loss){
		stamina -= loss;
	}
}
