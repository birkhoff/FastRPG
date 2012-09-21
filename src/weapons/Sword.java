package weapons;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public abstract class Sword {

	private float x;
	private float y;
	private float alpha;
	private float damage;
	private boolean isSlashing;
	private float length;
	private BufferedImage image;
	
	
	
	
	public Sword(float damagePar , float lengthPar, String imagePath){
		this.damage = damagePar;
		this.length = lengthPar;
		try {
			setImage(ImageIO.read(new File(imagePath)));
			// The above line throws an checked IOException which must be caught.
			
		} catch (IOException e) {				
			System.out.println("Bild von "+imagePath+" konnte nicht geladen werden.");
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

	public void setAlpha(float alpha) {
		this.alpha = alpha;
	}

	public float getAlpha() {
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
	
	
	
	public BufferedImage rotateImage(BufferedImage rotIm, double degrees) {
		
		AffineTransform transform = new AffineTransform();
	    transform.rotate(Math.toRadians(-degrees), rotIm.getWidth()/2, rotIm.getHeight()/2);
	    AffineTransformOp op = new AffineTransformOp(transform, AffineTransformOp.TYPE_BILINEAR);
	    BufferedImage returnIm = op.filter(rotIm, null);
        return returnIm;
    }
	
}
