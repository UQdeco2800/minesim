package minesim;

/**
 * Created by lena on 20/10/2015.
 */

import javafx.scene.media.AudioClip;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;

import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.junit.Assert.*;
import static org.mockito.Matchers.*;

@PrepareForTest({AudioClip.class})
@RunWith(PowerMockRunner.class)


public class SoundTests {

    AudioClip sound1;
    Sound sound = new Sound(sound1);
    double volume = Sound.getVolume();
    double setVolume = 0.6;
    double volumeForever = Sound.getVolume();
    double setVolumeForever = 0.5;

    private static final Logger LOGGER = Logger.getLogger(Music.class);

    @Before
    public void setUp(){
        sound1 = PowerMockito.mock(AudioClip.class);
    }

    //Test SoundClip Get and Set ----------------------------------------------------------------
    @Test
    public void testSoundClipGetSet(){
        Sound sound = new Sound(sound1);
        assertEquals(sound.getSoundClip(), sound1);
    }

    //music.play() tests -------------------------------------------------
    @Test(expected = Exception.class)
    public void playTest() {
        Sound sound = new Sound(sound1);
        Mockito.doThrow(new Exception()).when(sound1).play();
        sound.play();

    }

    @Test
    public void testSetVolumeInPlay() throws Exception {
        Sound sound = new Sound(sound1);

        Sound.setMute(false);

        Mockito.doAnswer(new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {

                Object[] args = invocation.getArguments();
                setVolume = (double) args[0];
                return setVolume;
            }
        }).when(sound1).setVolume(anyFloat());

        assertNotEquals(volume, setVolume);
        sound.play();
        assertEquals(volume, setVolume, 0.1);
    }

    //music.playForever() tests -------------------------------------------------
    @Test(expected = Exception.class)
    public void testPlayForever() {
        Sound sound = new Sound(sound1);
        Mockito.doThrow(new Exception()).when(sound1).play();
        sound.playForever();
    }

    @Test
    public void testSetVolumeInPlayForever() throws Exception {
        Sound sound = new Sound(sound1);

        Sound.setMute(false);

        Mockito.doAnswer(new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {

                Object[] args = invocation.getArguments();
                setVolumeForever = (double) args[0];
                return setVolumeForever;
            }
        }).when(sound1).setVolume(anyFloat());

        assertNotEquals(volumeForever, setVolumeForever);
        sound.playForever();
        assertEquals(volumeForever, setVolumeForever, 0.1);
    }


    //music.stop() tests -------------------------------------------------
    @Test(expected = Exception.class)
    public void stopTest() {
        Sound sound = new Sound(sound1);
        Mockito.doThrow(new Exception()).when(sound1).stop();
        sound.stop();

    }

    //test stop() ------------------------------------------------------------------------------

    /**
     * Tests stop() function
     */
    @Test
    public void testingStop(){
        Sound sound = PowerMockito.mock(Sound.class);

        PowerMockito.doCallRealMethod().when(sound).setSoundClip(sound1);
        PowerMockito.doCallRealMethod().when(sound).stop();
        sound.setSoundClip(sound1);
        sound.stop();


        Mockito.verify(sound1).stop();
    }


    //Volume setters and getters -------------------------------------------------------------------------
    @Test
    public void testGetSetVolume(){
        Sound sound = new Sound(sound1);
        Sound.setVolume(0.4);
        assertEquals(Sound.getVolume(), 0.4, 0.1);
    }

    //Mute setters and getters ---------------------------------------------------------------------------
    @Test
    public void testGetSetMute(){
        Sound sound = new Sound(sound1);
        Sound.setMute(true);
        assertEquals(sound.getMute(), true);
    }
}
