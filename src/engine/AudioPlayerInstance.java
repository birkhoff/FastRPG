/**
 * Create a new Instance of this Class and then simply use playSound or loopSound to play a Sound
 * stopSound() stops the AudioPlayerInstance completely
 * 
 */

package engine;

import java.io.File;
import javax.sound.sampled.*;

public class AudioPlayerInstance {
	
	Clip clip;
	
	public AudioPlayerInstance() throws Exception{
        clip = AudioSystem.getClip();
	}
	
	public void playSound(String urlString) throws Exception {
		File file = new File(urlString);
        // getAudioInputStream() also accepts a File or InputStream
        AudioInputStream ais = AudioSystem.getAudioInputStream( file );
        clip.open(ais);
        clip.loop(0);
    }
	
	public void loopSound(String urlString) throws Exception {
		File file = new File(urlString);
        // getAudioInputStream() also accepts a File or InputStream
        AudioInputStream ais = AudioSystem.getAudioInputStream( file );
        clip.open(ais);
        clip.loop(Clip.LOOP_CONTINUOUSLY);
    }
	
	public void stopClip(){
		clip.stop();
	}


}
