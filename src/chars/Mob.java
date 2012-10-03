/**
 *  The generic Mob. Can be extended into more specific Mobs with more function.
 */

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
	private BufferedImage[] mob;
	private int hp = 500;
	private String name = "";
	private int walkingCounter = 0;
	private int i = 0;
	private int k = 10;
	private float stepsize = 1f;
	private int look;
	private int dirX;
	private int dirY;
	private int width;
	private int height;
	private int rows;
	private int cols;
	private int damage = 50;
	private int hitRange = 20;
	private boolean directionForAttack = false;
	private boolean hit;
	private boolean heroInRange;
	
	public Mob(String name, float x, float y) {
		try {
			this.name = name;
			image = ImageIO.read(new File("images/sprites/mobs/skelett.png"));
			setWidth(107);
			setHeight(100);
			setRows(10);
			setCols(1);
			mob = new BufferedImage[getRows() * getCols()];
			for (int i = 0; i < getRows(); i++) {
			    for (int j = 0; j < getCols(); j++) {
			        mob[(i * getCols()) + j] = image.getSubimage(
			            j * getWidth(),
			            i * getHeight(),
			            getWidth(),
			            getHeight()
			        );
			    }
			}
			setHitRange(getWidth()/2);
			setLook((int) (Math.random()*4));
			position = new float[2];
			position[0] = x;
			position[1] = y;
		} catch (IOException e) {				
			System.out.println("Bild von "+this.name+" konnte nicht geladen werden.");
		}
	}
	
	/**
	 * Check if the hero is in the range of radius. If true KILL HIM! (EX-TER-MINATE :D)
	 */
	
	public void checkRange(Hero hero, int MapPosX, int MapPosY) {
		int radius = 300;
		int[] center = new int[2];
		center[0] = (int)this.getPositionX()+MapPosX+this.getWidth()/2;
		center[1] = (int)this.getPositionY()+MapPosY+this.getWidth()/2;
		if (hero.getPositionX()+hero.getWidth()/2 < center[0]+radius && hero.getPositionX()+hero.getWidth()/2 > center[0]-radius &&
				hero.getPositionY()-hero.getHeight()/2 < center[1]+radius && hero.getPositionY()-hero.getHeight()/2 > center[1]-radius) {
			setHeroInRange(true);
		} else setHeroInRange(false);
	}
	
	/******************* Getter and Setter ******************/
	
	
	private int getCols() {
		return cols;
	}
	private int getRows() {	
		return rows;
	}
	private void setCols(int i) {
		this.cols = i;
	}
	private void setRows(int i) {
		this.rows = i;
	}
	private void setHeight(int i) {
		this.height = i;
	}
	private void setWidth(int i) {
		this.width = i;
	}
	
	public void setLook(int i){
		switch(i){
			case 0: this.dirX = 0; this.dirY = -1;	 /*look.N; 	*/	break;
			case 1: this.dirX = 1; this.dirY = 0;	/*look.E;	*/	break;
			case 2: this.dirX = 0; this.dirY = 1;	/*look.S;	*/	break;
			case 3: this.dirX = -1; this.dirY = 0;	/*look.W;	*/	break;
		}
	}
	public void setLook() {
		setLook((int) (Math.random()*4));
	}
	public int getWalkingCounter() {
		return this.walkingCounter;
	}
	public void setWalkingCounter(int temp) {
		this.walkingCounter = temp;
	}
	public int getDirX() {
		return this.dirX;
	}
	public int getDirY() {
		return this.dirY;
	}
	public int getLook(){
		return this.look;
	}
	
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

	public Image getImage() {
		if(k>1){
			i=i+1;
			k = 0;
		}
		i= i%10;
		k+=1;
		return toImage(mob[i]);		
	}
	public Image toImage(BufferedImage bufferedImage) {
	    return Toolkit.getDefaultToolkit().createImage(bufferedImage.getSource());
	}
	public void setImage(BufferedImage image) {
		this.image = image;
	}

	public int getWidth() {
		return width;
	}
	public int getHeight() {
		return height;
	}
	public boolean isHit() {
		return hit;
	}
	public void setHit(boolean hit) {
		this.hit = hit;
	}
	public boolean isHeroInRange() {
		return heroInRange;
	}
	public void setHeroInRange(boolean heroInRange) {
		this.heroInRange = heroInRange;
	}
	public boolean isDirectionForAttack() {
		return directionForAttack;
	}
	public void setDirectionForAttack(boolean directionForAttack) {
		this.directionForAttack = directionForAttack;
	}
	public int getHitRange() {
		return hitRange;
	}
	public void setHitRange(int hitRange) {
		this.hitRange = hitRange;
	}
	public int getDamage() {
		return damage;
	}
	public void setDamage(int damage) {
		this.damage = damage;
	}
}
