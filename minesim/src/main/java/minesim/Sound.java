package minesim;

import javafx.scene.media.AudioClip;
import minesim.util.Settings;
import org.apache.log4j.Logger;
import java.net.URL;
import java.util.HashMap;


/**
 * A sound object that when creates using a filename can be played or stopped. Useful for small sound clips.
 * Utilizes the JavaFX object AudioClip, but builds upon it. The main advantages being an easier ADT to use 
 * for other developers, and the ability to globally modify the Volume, and Mute status.
 */
public class Sound {

    //Mute is static and applied to all sounds
    private static boolean mute;
    //volume is static and applied to all sounds
    private static double vol;
    //Actual player object
    private AudioClip soundClip;
    //A logger to log errors and issues
    private Logger LOG = Logger.getLogger(Sound.class);

    /**
     * Constructor for sound object, takes the filename and creates an AudioClip object
     * @param fileName The filename of the sound file that will be played
     */
    public Sound(String fileName) {
    	persistentSettings();
        try {
            URL url = Music.class.getResource("../SoundClips/" + fileName);
            soundClip = new AudioClip(url.toString());
        } catch (RuntimeException e) {
            LOG.error("There was an error creating sound, check file URL", e);
        }
    }

    //Make separate AudioClip for testing
    public Sound(AudioClip audioSound){
        soundClip = audioSound;
    }

    //Set AudioClip
    public void setSoundClip(AudioClip audioSetSound){
        soundClip = audioSetSound;
    }

    //Get the Sound Clip
    public AudioClip getSoundClip(){
        return soundClip;
    }

    /**
     * Sets the volume of all sound objects
     * @param The volume that soundFX should be set to
     */
    public static void setVolume(double volume) {
    		vol = volume; 
    }

    /**
     * Gets the volume of all sound objects (It's one volume, all sound objects have that volume)
     * @return vol The current volume of the sound class
     */
    public static double getVolume(){
    	return vol;
    }
    
    /**
     * Getter method for mute
     * @return mute the current status of the sound mute 
     */
    public boolean getMute() {
        return mute;
    }

    /**
     * Sets the mute status of all sound objects
     * @param value the mute boolean that mute is to be set to
     */
    public static void setMute(boolean value) {
        mute = value;
    }

    /**
     * Sets the number of times that sound should cycle
     * @param cycle sets the number of times that the sound should play
     */
    public void setCycle(int cycle) {
    	try {
    		soundClip.setCycleCount(cycle);
    	} catch (RuntimeException e) {
    		LOG.error("Could not set Cycle",e);
    	}
        
    }

    /**
     * Plays the sound indefinitely
     */
    public void playForever() {
        try {
            if (mute) {
                return;
            }
            soundClip.setVolume(vol);
            soundClip.setCycleCount(AudioClip.INDEFINITE);
            soundClip.play();
        } catch (RuntimeException e) {
            LOG.error("Could not play the sound forever", e);
        }

    }

    /**
     * stops the sound
     */
    public void stop() {
    	try {
    		soundClip.stop();
    	} catch (RuntimeException e) {
    		LOG.error("The sound clicp could not stop",e);
    	}
    }

    /**
     * Plays the sound once.
     */
    public void play() {
        if (mute) {
            System.out.println("tried to play but was muted");
            return;
        }
        try {
        	soundClip.setVolume(vol);
            soundClip.play();
        } catch (RuntimeException e) {
        	LOG.error("could not play the sound clip",e);
        } 
    }
    /**
     * Loads in the sounds settings. Then sets the sound's variables to their desired values
     */
    private void persistentSettings() {
        HashMap<String, String> settings = minesim.SettingsHandler.loadSettings();
        mute = Boolean.parseBoolean(settings.get(Settings.MUTE_SFX));
        vol = Double.parseDouble(settings.get(Settings.SFX_VOLUME));
    }
}
