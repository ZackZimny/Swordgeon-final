package com.mygdx.game.GameHelpers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.GdxRuntimeException;

/**
 * Loads asset files from the AssetManagerHandler
 * @see AssetManagerHandler
 */
public class FileLoader {
    /**
     * Logs an error if an asset cannot be loaded
     * @param path path the asset originated from
     * @param e GdxRuntimeException that occurred while attempting to load the asset
     */
    private static void logError(String path, GdxRuntimeException e){
        CrashLogHandler.logSevere("File " + path + " could not load. ", e.getMessage());
    }

    /**
     * Loads Texture from the AssetManagerHandler
     * @param path path to find the Texture; path begins at the asset folder
     * @return loaded Texture
     */
    public static Texture loadTexture(String path){
        try {
            Texture texture = AssetManagerHandler.assetManager.get(path, Texture.class);
            EventLogHandler.log("File " + path + " was loaded. ");
            return texture;
        } catch (GdxRuntimeException e) {
            logError(path, e);
            //System will close before this statement could ever be returned.
            return null;
        }
    }

    /**
     * Loads Sound from the AssetManagerHandler
     * @param path path to find the Sound; path begins at the asset folder
     * @return loaded Sound
     */
    public static Sound loadSound(String path){
        try {
            Sound sound = AssetManagerHandler.assetManager.get(path, Sound.class);;
            EventLogHandler.log("File " + path + " was loaded. ");
            return sound;
        } catch (GdxRuntimeException e){
            logError(path, e);
            //System will close before this statement could ever be returned.
            return null;
        }
    }

    /**
     * Loads Music from the AssetManagerHandler
     * @param path path to find the Music; path begins at the asset folder
     * @return loaded Music
     */
    public static Music loadMusic(String path){
        try {
            Music music = Gdx.audio.newMusic(Gdx.files.internal(path));
            EventLogHandler.log("File " + path + " was loaded. ");
            return music;
        } catch (GdxRuntimeException e){
            logError(path, e);
            //System will close before this statement could ever be returned.
            return null;
        }
    }
}
