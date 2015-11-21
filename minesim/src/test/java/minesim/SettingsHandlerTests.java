package minesim;

import minesim.util.Settings;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

import static org.junit.Assert.assertEquals;

/**
 * Created by Sean on 25/10/2015.
 */
public class SettingsHandlerTests {

    String file = "../minesim/src/test/bad_settings.txt";

    private SettingsHandler settingsHandler = new SettingsHandler();

    @Test
    public void testValid() throws Exception {
        saveNiceFile();
        SettingsHandler.loadSettings(file);
    }

    @Test
    public void testInvalid() throws Exception {
        saveBadFile();
        SettingsHandler.loadSettings(file);
    }

    private void saveNiceFile() throws IOException {
        String properties = "MUTEMUSIC = true\n" +
                "LOOPMUSIC = true\n" +
                "MUTESFX = true\n" +
                "MUSICVALUE = 0.0\n" +
                "SFXVOLUME = 0.0\n" +
                "RESOLUTION = 800x600\n" +
                "DEBUG = true";
        ArrayList<String> settings = new ArrayList<>();
        Collections.addAll(settings, properties.split("\n"));
        SettingsHandler.saveSettings(settings, file);
    }

    private void saveBadFile() throws IOException {
        String properties = "terrible\n" +
                "not equal to things ===\n" +
                "===bad";
        ArrayList<String> settings = new ArrayList<>();
        Collections.addAll(settings, properties.split("\n"));
        SettingsHandler.saveSettings(settings, file);
    }

    @Test
    public void testGet() throws Exception {
        saveNiceFile();
        SettingsHandler.loadSettings(file);
        assertEquals("800x600", SettingsHandler.getLoadedSettings().get(Settings.RESOLUTION));
    }

    @Test
    public void testLoad() throws Exception {
        SettingsHandler.loadSettings();
    }

    @Test
    public void testBadFilePath() throws Exception {
        SettingsHandler.loadSettings("dumbpath");
    }
}
