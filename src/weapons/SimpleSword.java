package weapons;

import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class SimpleSword extends Sword {

	public SimpleSword() {
		super(5, 60, "images/swords/simpplesword1.png");
	}
	
	public void setImageBack(){
	
		try {
			setImage(ImageIO.read(new File("images/swords/simpplesword1.png")));
			// The above line throws an checked IOException which must be caught.
			
		} catch (IOException e) {				
			System.out.println("Bild von images/swords/simpplesword1.png konnte nicht geladen werden.");
		}
	}

}
