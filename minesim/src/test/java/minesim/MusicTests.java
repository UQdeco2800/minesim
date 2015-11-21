package minesim;


import minesim.entities.Animation;

import org.junit.Before;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

import javafx.scene.media.MediaPlayer;
import static org.junit.Assert.*;

import org.apache.log4j.Appender;
import org.apache.log4j.Layout;
import org.apache.log4j.Logger;
import org.apache.log4j.SimpleLayout;
import org.apache.log4j.WriterAppender;
import org.junit.Test;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.net.URL;
import java.util.ArrayList;

import static org.junit.Assert.*;
import static org.mockito.Matchers.*;

@PrepareForTest({MediaPlayer.class})
@RunWith(PowerMockRunner.class)


public class MusicTests {

    private ArrayList<String> musicList = new ArrayList<String>();
    private ArrayList<MediaPlayer> mockmusicList = new ArrayList<MediaPlayer>();

    MediaPlayer p1;
    MediaPlayer p2;
    MediaPlayer p3;

    Music music = new Music(mockmusicList);
    double volume = music.getVolume();
    double setVolume = 0.3;

    boolean musicMute = music.getMusicMute();
    boolean setMute = true;


    //private Music instance = null;

    private static final Logger LOGGER = Logger.getLogger(Music.class);

    @Before
    public void setup() {
        musicList.add("beach.mp3");
        musicList.add("collapse.mp3");

        p1 = PowerMockito.mock(MediaPlayer.class);
        p2 = PowerMockito.mock(MediaPlayer.class);
        p3 = PowerMockito.mock(MediaPlayer.class);
        mockmusicList.add(p1);
        mockmusicList.add(p2);
        mockmusicList.add(p3);

    }

    //Test getInstance -----------------------------------------------------------------------
    @Test @Ignore
    public void testGetInstance(){
        Music musicInstance = Music.getInstance();

        assertEquals(musicInstance.getClass(), Music.class);

    }

    //Test changeMusic -----------------------------------------------------------------------
    //Tests the music played in change music
    @Test @Ignore
    public void testChangeMusic(){

        ArrayList<String> startList = new ArrayList<String>();
        ArrayList<String> finishList = new ArrayList<String>();

        startList.add("beach.mp3");
        startList.add("collapse.mp3");

        finishList.add("sad apples.mp3");
        finishList.add("gloomy.mp3");

        Music music = new Music(startList);

        music.changeMusic(finishList);

        //creates list of media players that should be the same as the actual media player
        ArrayList<MediaPlayer> finishedPlaylist = new ArrayList<MediaPlayer>();
        for (int i = 0; i < finishList.size(); i++) {
            try {
                URL url = Music.class.getResource("../Music/" + finishList.get(i));
                Media media = new Media(url.toString());
                MediaPlayer listPlayer = new MediaPlayer(media);
                finishedPlaylist.add(listPlayer);
                //volume = 1.0;
            } catch (RuntimeException re) {

            }
        }

        assertEquals(finishedPlaylist, music.getPlaylist());

    }

    //music.play() tests -------------------------------------------------

    /**
     * tests that .play() is called in play()
     */
    @Test(expected = RuntimeException.class)
    public void playTest() {
        Music music = new Music(mockmusicList);
        Mockito.doThrow(new RuntimeException()).when(p1).play();
        music.play();

    }

    /**
     * tests that setVolume() in play is called
     * @throws Exception
     */
    @Test
    public void testSetVolumeInPlay() throws Exception {
        Music music = new Music(mockmusicList); //set up music with mock etc.

        Mockito.doAnswer(new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {

                Object[] args = invocation.getArguments();
                setVolume = (double) args[0]; //this line gets the float value put into p1.setVolume(volume) and makes our setVolume variable equal to that.
                return setVolume;
            }
        }).when(p1).setVolume(anyFloat());
        //test the value of volume and setVolume are initially different.
        assertNotEquals(volume, setVolume); //not sure of the 'assert' method to do this
        music.play(); //call the play function that will call player.setVolume(volume);
        //now test that they are equal after we start playing. this means the play successfully set the volume of the player.
        assertEquals(volume, setVolume, 0.0);
    }

    /**
     * Tests that setMute is called in play()
     * @throws Exception
     */
    @Test
    public void testSetMuteInPlay() throws Exception {
        Music music = new Music(mockmusicList);

        Mockito.doAnswer(new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {

                Object[] args = invocation.getArguments();
                setMute = (boolean) args[0];
                return setMute;
            }
        }).when(p1).setMute(anyBoolean());
        assertNotEquals(musicMute, setMute);
        music.play();
        assertEquals(musicMute, setMute);
    }


    // music.playDayMusic() tests ---------------------------------------------------------------

    //ArrayList for testing playDayMusic below
    ArrayList<String> grabbedDayList = new ArrayList<String>();

    /**
     * Testing playDayMusic()
     */
    @Test
    public void testPlayDayMusic() {
        Music music = PowerMockito.mock(Music.class);

        PowerMockito.doCallRealMethod().when(music).playDayMusic();
        PowerMockito.doCallRealMethod().when(music).setDayPassed(anyBoolean());
        PowerMockito.doCallRealMethod().when(music).getDayPassed();
        PowerMockito.doCallRealMethod().when(music).isDay();

        ArrayList<String> dayListTrue = new ArrayList<String>();
        ArrayList<String> dayListFalse = new ArrayList<String>();

        dayListTrue.add("beach.mp3");
        dayListTrue.add("collapse.mp3");

        dayListFalse.add("collapse.mp3");
        dayListFalse.add("beach.mp3");


        music.setDayPassed(true);

        Mockito.doAnswer(new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                Object[] args = invocation.getArguments();
                grabbedDayList = (ArrayList<String>) args[0];
                return grabbedDayList;
            }
        }).when(music).changeMusic(anyObject());
        assertNotEquals(grabbedDayList, dayListTrue);
        music.playDayMusic();
        assertEquals(grabbedDayList, dayListTrue);
        assertFalse(music.getDayPassed());
        music.setDayPassed(false);
        music.playDayMusic();
        assertEquals(grabbedDayList, dayListFalse);
        assertTrue(music.getDayPassed());
        assertFalse(music.isDay());
    }

    // music.playNightMusic() tests ---------------------------------------------------------------

    //ArrayList for testing playNightMusic below
    ArrayList<String> grabbedNightList = new ArrayList<String>();

    /**
     * Testing playNightMusic
     */
    @Test
    public void testPlayNightMusic() {
        Music music = PowerMockito.mock(Music.class);

        PowerMockito.doCallRealMethod().when(music).playNightMusic();
        PowerMockito.doCallRealMethod().when(music).setNightPassed(anyBoolean());
        PowerMockito.doCallRealMethod().when(music).isNightPassed();
        PowerMockito.doCallRealMethod().when(music).isDay();

        ArrayList<String> nightListTrue = new ArrayList<String>();
        ArrayList<String> nightListFalse = new ArrayList<String>();

        nightListTrue.add("sad apples.mp3");
        nightListTrue.add("gloomy.mp3");

        nightListFalse.add("gloomy.mp3");
        nightListFalse.add("sad apples.mp3");


        music.setNightPassed(true);

        Mockito.doAnswer(new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                Object[] args = invocation.getArguments();
                grabbedNightList = (ArrayList<String>) args[0];
                return grabbedNightList;
            }
        }).when(music).changeMusic(anyObject());
        assertNotEquals(grabbedNightList, nightListTrue);
        music.playNightMusic();
        assertEquals(grabbedNightList, nightListTrue);
        assertFalse(music.getDayPassed());
        music.setNightPassed(false);
        music.playNightMusic();
        assertEquals(grabbedNightList, nightListFalse);
        assertTrue(music.isNightPassed());
        assertFalse(music.isDay());
    }


    //Fade out tests ---------------------------------------------------------

    /**
     * Testing fadeOut()
     */
    @Test
    public void fadeOutTest() {
        Music music = new Music(mockmusicList);
        ArrayList<Double> grabbedVolumeList = new ArrayList<Double>();

        music.setPlayer(p1);
        double volumeSet = 0.8;
        music.setVolume(volumeSet);

        //Mocks setVolume
        Mockito.doAnswer(new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {

                Object[] args = invocation.getArguments();
                setVolume = (double) args[0]; //this line gets the object value put into p1.setVolume(volume) and makes our setVolume variable equal to that.
                grabbedVolumeList.add(setVolume);
                return setVolume;
            }
        }).when(p1).setVolume(anyDouble());


        //Mocks setMute
        Mockito.doAnswer(new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {

                Object[] args = invocation.getArguments();
                setMute = (boolean) args[0];
                return setMute;
            }
        }).when(p1).setMute(anyBoolean());
        assertNotEquals(music.getMusicMute(), setMute);

        music.fadeOut();
        try {
            Thread.sleep(5500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        assertEquals(grabbedVolumeList.size(), (volumeSet * 1000), 100);
        assertEquals(volumeSet, music.getVolume(), 0.0);
        assertEquals(grabbedVolumeList.get(grabbedVolumeList.size() - 1), 0.0, 0.1);
        assertTrue(music.getMusicMute());
        assertTrue(setMute);
        assertTrue(music.getMusicMute());

    }



    //Fade in tests ---------------------------------------------------------

    /**
     * Tests fadeIn()
     */
    @Test
    public void fadeInTest() {
        Music music = new Music(mockmusicList);
        ArrayList<Double> grabbedVolumeList = new ArrayList<Double>();

        music.setPlayer(p1);
        double volumeSet = 0.9;
        music.setVolume(volumeSet);

        //Mocks setVolume
        Mockito.doAnswer(new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {

                Object[] args = invocation.getArguments();
                setVolume = (double) args[0]; //this line gets the float value put into p1.setVolume(volume) and makes our setVolume variable equal to that.
                grabbedVolumeList.add(setVolume);
                return setVolume;
            }
        }).when(p1).setVolume(anyDouble());

        music.fadeIn();
        try {
            Thread.sleep(5500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        assertEquals(music.getVolume(), volumeSet, 0.1);
        assertEquals(grabbedVolumeList.size(), (volumeSet * 1000), 100);
        assertEquals(volumeSet, music.getVolume(), 0.0);
        assertEquals(grabbedVolumeList.get(grabbedVolumeList.size() - 1), 0.9, 0.1);
        assertFalse(music.getMusicMute());

    }

    //test stop() ------------------------------------------------------------------------------

    /**
     * Tests stop() function
     */
    @Test
    public void testingStop(){
        Music music = PowerMockito.mock(Music.class);

        PowerMockito.doCallRealMethod().when(music).setPlayer(p1);
        PowerMockito.doCallRealMethod().when(music).stop();
        music.setPlayer(p1);
        music.stop();


        Mockito.verify(p1).stop();
    }

    //test tickMusicMute() -------------------------------------------------------------------

    /**
     * Tests tickMusicMute()
     */
    @Test
    public void testTickMusicLoop(){
        boolean getMusicLoop = music.getMusicLoop();
        music.tickMusicLoop();
        boolean postMusicLoop = music.getMusicLoop();
        assertFalse(getMusicLoop == postMusicLoop);

    }


    //test MusicMute() -----------------------------------------------------------------------

    /**
     * Tests musicMute() when mute is true and false
     */
    @Test
    public void testMusicMute(){
        Music music = PowerMockito.mock(Music.class);

        PowerMockito.doCallRealMethod().when(music).setMusicMute(anyBoolean());
        PowerMockito.doCallRealMethod().when(music).getMusicMute();

        music.setMusicMute(true);
        assertEquals(music.getMusicMute(), true);

        try {
            PowerMockito.doCallRealMethod().when(music).musicMute();
            music.musicMute();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Mockito.verify(music).fadeIn();

        music.setMusicMute(false);
        assertEquals(music.getMusicMute(), false);

        try {
            PowerMockito.doCallRealMethod().when(music).musicMute();
            music.musicMute();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Mockito.verify(music).fadeOut();


    }


    // Test Mute Getters and Setters ---------------------------------------------------------------
    @Test
    public void testMuteGetSet(){
        Music music = new Music(mockmusicList);
        music.setMusicMute(true);
        assertEquals(music.getMusicMute(), true);
    }

    //Test Loop Getters and Setters ----------------------------------------------------------------
    @Test
    public void testLoopGetSet(){
        Music music = new Music(mockmusicList);
        music.setLoop(true);
        assertEquals(music.getMusicLoop(), true);
    }

    //Test MediaPlayer List Get and Set ------------------------------------------------------------
    @Test
    public void testMediaListGetSet(){
        Music music = new Music(mockmusicList);
        music.setPlayer(mockmusicList);
        assertEquals(music.getPlaylist(), mockmusicList);
    }

    //Test MediaPlayer Get and Set -----------------------------------------------------------------
    @Test
    public void testMediaPlayerGetSet(){
        Music music = new Music(mockmusicList);
        music.setPlayer(p1);
        assertEquals(music.getMediaPlayer(), p1);
    }

//    //Test Volume Get and Set ---------------------------------------------------------------------
//    @Test
//    public void testVolumeGetSet(){
//        Music music = new Music(mockmusicList);
//        p1.setVolume(0.4);
//        System.out.println(music.getVolume());
//        assertEquals(music.getVolume(), 0.4, 0.1);
//    }

    //Test TrackIndex Getter and Setter -------------------------------------------------------------
    @Test
    public void testTrackIndexGetSet(){
        Music music = new Music(mockmusicList);
        music.setTrackIndex(1);
        assertEquals(music.getTrackIndex(), 1);
    }

    //Test DayPassed Getter and Setter --------------------------------------------------------------
    @Test
    public void testDayPassedGetSet(){
        Music music = new Music(mockmusicList);
        music.setDayPassed(true);
        assertEquals(music.getDayPassed(), true);
    }

    //Test Day Getter and Setter --------------------------------------------------------------
    @Test
    public void testDayGetSet(){
        Music music = new Music(mockmusicList);
        music.setDay(true);
        assertEquals(music.isDay(), true);
    }

    //Test NightPassed Getter and Setter --------------------------------------------------------------
    @Test
    public void testNightPassedGetSet(){
        Music music = new Music(mockmusicList);
        music.setNightPassed(true);
        assertEquals(music.isNightPassed(), true);
    }



}


