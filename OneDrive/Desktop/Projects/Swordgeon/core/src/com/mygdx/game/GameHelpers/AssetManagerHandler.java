package com.mygdx.game.GameHelpers;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.GdxRuntimeException;

/**
 * Loads the textures, music, and sound for the game before it begins
 */
public class AssetManagerHandler {
    public static final AssetManager assetManager = new AssetManager();

    /**
     * Loads all textures, music, and sound located in the assets folder
     */
    public static void load(){
        try {
            assetManager.load("BackgroundTile.png", Texture.class);
            assetManager.load("BlipSelect.wav", Sound.class);
            assetManager.load("Boundary.png", Texture.class);
            assetManager.load("ControlsScreen.png", Texture.class);
            assetManager.load("EnemyProjectile.png", Texture.class);
            assetManager.load("Goal.png", Texture.class);
            assetManager.load("HealthHeart.png", Texture.class);
            assetManager.load("Hit.wav", Sound.class);
            assetManager.load("Level1.png", Texture.class);
            assetManager.load("Level2.png", Texture.class);
            assetManager.load("Level3.png", Texture.class);
            assetManager.load("Level4.png", Texture.class);
            assetManager.load("MainLoop.wav", Music.class);
            assetManager.load("MenuScreen.png", Texture.class);
            assetManager.load("Player.png", Texture.class);
            assetManager.load("ProjectileCreated.wav", Sound.class);
            assetManager.load("ShootingEnemy.png", Texture.class);
            assetManager.load("TrackingEnemy.png", Texture.class);
        } catch (GdxRuntimeException e){
            CrashLogHandler.logSevere("There was an issue loading an asset. ", e.getMessage());
        }
    }
}
