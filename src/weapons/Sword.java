package weapons;

import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;

import javax.imageio.ImageIO;

import chars.Mob;

import engine.AssetCreator;

public abstract class Sword {

	private float x;
	private float y;
	private double alpha;
	private float damage;
	private boolean isSlashing;
	private float length;
	private BufferedImage image;
	private BufferedImage originalImage;
	
	public Sword(float damagePar , float lengthPar, String imagePath){
		this.alpha = -40;
		this.damage = damagePar;
		this.length = lengthPar;
		try {
			setImage(ImageIO.read(new File(imagePath)));
			originalImage= image;
			// The above line throws an checked IOException which must be caught.
			
		} catch (IOException e) {				
			System.out.println("Bild von "+imagePath+" konnte nicht geladen werden.");
		}
	}

	private void checkHit() {
		int epsilon = 10;	// Epsilon fuer Radius um Schwert
		LinkedList<Mob> Mobs = AssetCreator.getMobs();
    	for (int i = 0; i < Mobs.size(); i++) {
			Mob mob = Mobs.get(i);
			if (x == mob.getPositionX() && y == mob.getPositionY()) {
				mob.setHp((int)(mob.getHp()-damage));
				if (mob.getHp() < 0 ) {
					Mobs.remove(mob);
				}
			}
		}
	}
	
	public void setX(float x) {
		this.x = x;
	}
	public float getX() {
		return x;
	}
	public void setY(float y) {
		this.y = y;
	}
	public float getY() {
		return y;
	}
	public void setAlpha(double alpha) {
		this.alpha = alpha;
	}
	public double getAlpha() {
		return alpha;
	}
	public void setDamage(float damage) {
		this.damage = damage;
	}
	public float getDamage() {
		return damage;
	}
	public void setSlashing(boolean isSlashing) {
		this.isSlashing = isSlashing;
	}
	public boolean isSlashing() {
		return isSlashing;
	}
	public void setLength(float length) {
		this.length = length;
	}
	public float getLength() {
		return length;
	}
	public void setImage(BufferedImage image) {
		this.image = image;
	}
	public BufferedImage getImage() {		
		return image;
	}
		
	public BufferedImage rotateImage( double degrees) {
		checkHit();
		AffineTransform transform = new AffineTransform();
	    transform.rotate(Math.toRadians(-degrees), originalImage.getWidth()/2, originalImage.getHeight()*0.75);
	    AffineTransformOp op = new AffineTransformOp(transform, AffineTransformOp.TYPE_BILINEAR);
	    BufferedImage returnIm = op.filter(originalImage, null);
        return returnIm;
    }
}
