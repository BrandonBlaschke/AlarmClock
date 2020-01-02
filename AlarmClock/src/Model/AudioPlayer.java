package Model;

import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

/**
 * @author Brandon Blaschke
 * Class to control and play Audio.
 */
public class AudioPlayer {
	
	/**
	 * Clip of audio to play from
	 */
	Clip clip; 
	
	/**
	 * Audio input stream.
	 */
	AudioInputStream audioInputStream;
	
	/**
	 * Status of the audio
	 */
	AudioStatus audioStatus;
	
	/**
	 * Create a AudioPlayer for a single piece of audio.
	 * @param filePath Path to file.
	 * @throws UnsupportedAudioFileException
	 * @throws IOException
	 * @throws LineUnavailableException
	 */
	public AudioPlayer(String filePath) throws UnsupportedAudioFileException, IOException, LineUnavailableException
	{
		audioInputStream = AudioSystem.getAudioInputStream(new File(filePath).getAbsoluteFile());
		clip = AudioSystem.getClip();
		clip.open(audioInputStream);
		clip.loop(Clip.LOOP_CONTINUOUSLY);
		clip.stop();
		audioStatus = AudioStatus.STOPPED;
	}
	
	/**
	 * Play the audio.
	 */
	public void play() {
		if (audioStatus != AudioStatus.PLAYING)
			clip.loop(Clip.LOOP_CONTINUOUSLY);
			audioStatus = AudioStatus.PLAYING;
	}
	
	/**
	 * Pause the Audio.
	 */
	public void pause() {
		if (audioStatus == AudioStatus.PLAYING && audioStatus != AudioStatus.STOPPED)
			clip.stop();
			audioStatus = AudioStatus.PAUSED;
	}
	
	/**
	 * Stop audio and close stream.
	 */
	public void stop() {
		if (audioStatus != AudioStatus.STOPPED) {
			clip.stop();
			clip.close();	
			audioStatus = AudioStatus.STOPPED;
		}
	}
}

/**
 * Enum for the current status of the audio.
 */
enum AudioStatus {
	PLAYING, STOPPED, PAUSED;
}
