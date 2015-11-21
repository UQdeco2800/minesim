package minesim;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import minesim.events.tracker.DayNightTransistionTracker;
import minesim.events.tracker.MineEventHandler;
import minesim.events.tracker.Watcher;
import minesim.util.Settings;
import org.apache.log4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InvalidClassException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * A singleton Music object that plays music for the minesim game. 
 * It uses the javaFX MediaPlayer to play music. 
 * Builds upon it by adding playlist functionality, fading in and out between tracks, ensuring that there is only
 * one music track playing at any one time, being able to modify the volume, mute status from anywhere in minesim.
 */

public class Music {
	
	//A logger to log errors and issues
    private static final Logger LOG = Logger.getLogger(Music.class);
    //The initial intance should be null, as is the case with singletons
    private static Music instance = null;
    //Current media player object
    private MediaPlayer player;
    //An arraylist of media objects
    private ArrayList<MediaPlayer> playlist;
    //double between 0.0 and 1.0 describing volume
    private double volume;
    //Current index of the track being played
    private int trackIndex = 0;
    //A flag that checks if a day has passed
    private boolean dayPassed = false;
    //A flag that checks if a night has passed
    private boolean nightPassed = false;
    //A boolean that stores the status of whether music should be muted
    private boolean musicMute = false;
    //A boolean that stores the status of whether the current playlist should loop
    private boolean musicLoop = true;

    //A boolean that stores the status of the Day
    private boolean day = true;



    /**
     * The constructor method for music, takes an ArrayList and creates the instance of the class
     * using the default play list
     * @param playlistStrings is a list of strings that music initializes with, accessing those sound files
     * and turning them into a play list to be played.
     */
    public Music(List<String> playlistStrings) {
    	persistentSettings();
        playlist = new ArrayList<MediaPlayer>();
        for (int i = 0; i < playlistStrings.size(); i++) {
            try {
                URL url = Music.class.getResource("../Music/" + playlistStrings.get(i));
                Media media = new Media(url.toString());
                MediaPlayer listPlayer = new MediaPlayer(media);
                playlist.add(listPlayer);
            } catch (RuntimeException re) {
                LOG.error("There was an error creating the mediaplayer, perhaps incorrect filename or it is not" +
                        " stored in the music folder under resources", re);
            }
        }
    }

    public Music(ArrayList<MediaPlayer> playListMedia){
        playlist = playListMedia;
    }

    /**
     * Music initializes with the titlescreen music on repeat. 
     * If this initialization has already been called then it just returns the current instance.
     * This method forces music into being a singleton!
     */
    public static Music getInstance() {
        if (instance == null) {
            LOG.debug("making new instance..");
        	ArrayList<String> trackList = new ArrayList<String>();
        	trackList.add("titlescreen.mp3");
            instance = new Music(trackList);
            
            try { // register to the Day/Night Transistion Tracker
                MineEventHandler.getInstance().registerFor(DayNightTransistionTracker.class, new Watcher() {
                    @Override
                    public void handle(Object arg) {
                        if (DayNightTransistionTracker.states.DAYTONIGHT == arg) {
                            Music.getInstance().playNightMusic();
                        } else {
                            Music.getInstance().playDayMusic();
                        }

                    }
                });
            } catch (InvalidClassException e) {
            	LOG.error("There was an error handling the day/night transtion", e);
            }
        }
        
        return instance;
    }

    //set the instance
    public static void setInstance(Music music){
        instance = music;
    }

    /**
     * Plays the music player on repeat, unless music loop is disabled.
     */
    public void play() {
        player = playlist.get(trackIndex);
        player.setVolume(volume);
        player.play();
        player.setMute(musicMute);
        player.setOnEndOfMedia(new Runnable() {
            @Override
            public void run() {
                player.stop();
                trackIndex++;
                if (trackIndex >= playlist.size() && musicLoop) {
                    trackIndex = 0;
                } else if (trackIndex >= playlist.size() && !musicLoop) {
                    return;
                }
                player = playlist.get(trackIndex);
                play();
            }
        });
    }
    /**
     * Takes a file name and adds it to the current playlist object 
     * for the music.
     * @param file the filename which is to be added to the playlist, file location not needed
     * if the file has been included in the resources folder under Music.
     */
    public void addTrack(String file){
    	URL url = Music.class.getResource("../Music/" + file);
        Media media = new Media(url.toString());
        MediaPlayer listPlayer = new MediaPlayer(media);
        playlist.add(listPlayer);
    }

    /**
     * Takes an ArrayList of file names and fades out the music currently playing, 
     * and fades in the music specified in the ArrayList.
     * @param trackList An ArrayList of strings that contain all the music files that 
     * are to be added to the new playlist
     */
    public void changeMusic(List<String> trackList) {
    	 Thread changemusic = new Thread(new Runnable() {
             @Override
             public void run() {
             	double fadeVolume = volume;
             	LOG.debug("Fade volume start: " + fadeVolume);
                 for (int i = 0; i < (volume*1000); i++) {
                     player.setVolume(fadeVolume);
                     LOG.debug("Fade volume: " + fadeVolume);
                     fadeVolume -= (volume/(volume*1000));
                     try {
                         Thread.sleep(10);
                     } catch (InterruptedException e) {
                         LOG.error("Thread.sleep for fadeOut was interupted", e);
                     }
                 }
                 Music.getInstance().stop();
                 playlist.clear();
                 trackIndex = 0;
                 for(int i = 0; i < trackList.size(); i++){
                     LOG.debug("addling: " + trackList.get(i));
                	 addTrack(trackList.get(i));
                 }
                 double fadeInVolume = 0.0;
                 player.setVolume(fadeInVolume);
                 Music.getInstance().play();
                 for (int i = 0; i < (volume*1000); i++) {
                     player.setVolume(fadeInVolume);
                     fadeInVolume += (volume/(volume*1000));
                     try {
                         Thread.sleep(10);
                     } catch (InterruptedException e) {
                         LOG.error("Thread.sleep for fadeIn was interupted", e);
                     }
                 }
                 player.setVolume(volume);
             }
         });
         changemusic.start();
    }
    
    /**
     * Plays the music for the day. A simple boolean keeps track of what track played
     * last, although if there were more than two tracks there would have to be 
     * day/night indexes that kept track, in this case a boolean is fine.
     */
    public void playDayMusic() {
    	ArrayList<String> dayList = new ArrayList<String>();
    	if(dayPassed){
    		dayList.add("beach.mp3");
    		dayList.add("collapse.mp3");
    		dayPassed = false;
    	}
    	else{
    		dayList.add("collapse.mp3");
    		dayList.add("beach.mp3");
    		dayPassed = true;
    	}
    	changeMusic(dayList);
    }

    /**
     * Plays the music for the night. A simple boolean keeps track of what track played
     * last, although if there were more than two tracks there would have to be 
     * day/night indexes that kept track, in this case a boolean is fine.
     */
    public void playNightMusic(){
    	ArrayList<String> nightList = new ArrayList<String>();
    	if(nightPassed){
    		nightList.add("sad apples.mp3");
    		nightList.add("gloomy.mp3");
    		nightPassed = false;
    	}
    	else{
    		nightList.add("gloomy.mp3");
    		nightList.add("sad apples.mp3");
    		nightPassed = true;
    	}
    	changeMusic(nightList);
    }

    /**
     * Sets the music loop to be equal to input value
     */
    public void setLoop(boolean value) {
        musicLoop = value;
    }


    /**
     * Fades the music out and mutes it. Just a note as to why these functions aren't use in 
     * "Change Music", is because this fadeout needs to be faster to prevent a bug, whereas I 
     * want the fade out to be a bit slower between day/night. 
     * Secondly, these fadeouts happen independently of one and other, but in changemusic they have to wait
     * for the other to finish. 
     */
    public void fadeOut() {
    	Thread fadeOut = new Thread(new Runnable() {
    		@Override 
    		public void run() {
    			double fadeVolume = volume;
    			for (int i = 0; i < (volume*1000); i++) {
    				player.setVolume(fadeVolume);
    				fadeVolume -= (volume/(volume*1000));
    				try {
    					Thread.sleep(5);
    				} catch (InterruptedException e) {
    					LOG.error("Thread.sleep for fadeOut was interupted", e);
    				}
    			}
    			musicMute = true; 
    			player.setMute(true);
    		}
    	});
    	fadeOut.start();	
    }
    

    /**
     * Plays the music and fades it in. Just a note as to why these functions aren't use in 
     * "Change Music", is because this fadeout needs to be faster to prevent a bug, whereas I 
     * want the fade out to be a bit slower between day/night. 
     * Secondly, these fadeouts happen independently of one and other, but in changemusic they have to wait
     * for the other to finish. 
     */
    public void fadeIn() {
        Thread fadeIn = new Thread(new Runnable() {
            @Override
            public void run() {
            	double fadeInVolume = 0;
            	player.setVolume(fadeInVolume);
                player.setMute(false);
                for (int i = 0; i < (volume*1000); i++) {
                    player.setVolume(fadeInVolume);
                    fadeInVolume += (volume/(volume*1000));
                    try {
                        Thread.sleep(5);
                    } catch (InterruptedException e) {
                        LOG.error("Thread.sleep for fadeIn was interupted", e);
                    }
                }
                musicMute = false;
            }
        });
        fadeIn.start();
    }

    /**
     * Mutes or unmutes the music
     */
    public void musicMute() throws InterruptedException {
        if (musicMute) {
            fadeIn();
        } else {
            fadeOut();
        }
        musicMute =  getMusicMute();
    }

    /**
     * Getter method for music loop
     * @return musicLoop the state of the musicLoop boolean
     */
    public boolean getMusicLoop() {
        return musicLoop;
    }

    /**
     * Getter method for music mute
     * @return musicMute the state of the musicMute boolean
     */
    public boolean getMusicMute() {
        return musicMute;
    }

    /**
     * Setters method for the musicMute boolean
     * @param status the boolean that musicMute will be set to
     */
    public void setMusicMute(boolean status) {
    	musicMute = status;
    }

    /**
     * A method that sets the musicLoop boolean to equal it's opposite. 
     * This is called when the music loop checkbox is clicked.
     */
    public void tickMusicLoop() {
        musicLoop = !musicLoop;
    }

    /**
     * Stops the current player.
     */
    public void stop() {
        player.stop();
    }
    
    /**
     * Sets the volume of the current media player object
     * @param vol The volume that the Music should be set to.
     */
    public void setVolume(double vol) {
        volume = vol;
        if (volume >= 1.0) {
            volume = 1.0;
            LOG.error("Volume cannot be greater than 1.0 or less than or equal to 0");
        }
        volume = vol;
        player.setVolume(volume);
    }
    

    /**
     * Loads the persistent settings for the music. I.e if the music is muted,
     * if it needs to loop, and the volume
     */
    private void persistentSettings(){
        HashMap<String, String> settings = minesim.SettingsHandler.loadSettings();
        musicMute = Boolean.parseBoolean(settings.get(Settings.MUTE_MUSIC));
        musicLoop = Boolean.parseBoolean(settings.get(Settings.LOOP_MUSIC));
        volume = Double.parseDouble(settings.get(Settings.MUSIC_VALUE));
    }


    //create a function to securely set the mediaPlayer ArrayList
    public void setPlayer(List<MediaPlayer> mediaList) {
        this.playlist = (ArrayList<MediaPlayer>) mediaList;
    }

    //securely get the Media Playlist
    public List<MediaPlayer> getPlaylist(){
        return this.playlist;
    }

    //securely get the MediaPlayer
    public MediaPlayer getMediaPlayer() {
        return this.player;
    }

    //create a function to securely set the player
    public void setPlayer(MediaPlayer mplayer) {
        this.player = mplayer;
    }

    //securely get volume level
    public double getVolume() {
        return this.volume;
    }

    //securely get the index of the track
    public int getTrackIndex() {
        return this.trackIndex;
    }

    //create a function to securely set the player
    public void setTrackIndex(int setIndex) {
        this.trackIndex = setIndex;
    }

    //set dayPassed value
    public void setDayPassed(boolean setDay){
        dayPassed = setDay;
    }

    //gets dayPassed value
    public boolean getDayPassed(){
        return dayPassed;
    }

    //Sets the Day
    public void setDay(boolean theDay){
        day = theDay;
    }

    //gets the Day
    public boolean isDay(){
        return day;
    }

    //set nightPassed value
    public void setNightPassed(boolean setNight){
        nightPassed = setNight;
    }


    //gets nightPassed value
    public boolean isNightPassed(){
        return nightPassed;
    }

}

