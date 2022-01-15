package com.mygdx.game.GameHelpers;

/**
 * Holds the runtime configs set up in the options screen. Updated along with the database table
 */
public class RuntimeConfigs {
    private static int musicVolume = 100, sfxVolume = 100;
    private static String name = "AAA";

    public static int getMusicVolume() {
        return musicVolume;
    }

    public static int getSfxVolume() {
        return sfxVolume;
    }

    public static String getName() {
        return name;
    }

    public static void setMusicVolume(int musicVolume) {
        RuntimeConfigs.musicVolume = musicVolume;
    }

    public static void setSfxVolume(int sfxVolume) {
        RuntimeConfigs.sfxVolume = sfxVolume;
    }

    public static void setName(String name) {
        RuntimeConfigs.name = name;
    }
}
