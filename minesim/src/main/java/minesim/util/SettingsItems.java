package minesim.util;

/**
 * Created by Sean on 25/10/2015.
 */
public class SettingsItems implements Settings {
    public static Class getType(String key) {
        switch (key) {
            case DEBUG:
                return boolean.class;
            case LOOP_MUSIC:
                return boolean.class;
            case MUSIC_VALUE:
                return Double.class;
            case MUTE_MUSIC:
                return boolean.class;
            case MUTE_SFX:
                return boolean.class;
        }
        return boolean.class;
    }
}
