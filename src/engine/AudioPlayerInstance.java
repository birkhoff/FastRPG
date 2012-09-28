package engine;

import java.io.File;
import java.net.URL;
import javax.sound.sampled.*;

public class AudioPlayerInstance {
	
	Clip clip;
	
	public AudioPlayerInstance() throws Exception{
        clip = AudioSystem.getClip();
	}
	
	public void playSound(String urlString) throws Exception {
		File file = new File(urlString);
        // getAudioInputStream() also accepts a File or InputStream
        AudioInputStream ais = AudioSystem.
            getAudioInputStream( file );
        clip.open(ais);
        clip.loop(1);
    }
	
	public void loopSound(String urlString) throws Exception {
		File file = new File(urlString);
        // getAudioInputStream() also accepts a File or InputStream
        AudioInputStream ais = AudioSystem.
            getAudioInputStream( file );
        clip.open(ais);
        clip.loop(Clip.LOOP_CONTINUOUSLY);
    }
	
	public void stopClip(){
		clip.stop();
	}


}
