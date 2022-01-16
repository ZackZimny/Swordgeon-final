package com.mygdx.game.GameHelpers;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;

public class GameSounds {
    private static Sound blipSelect, hit, projectileCreated;
    private static Music music;
    private static float sfxVolume, musicVolume;

    /**
     * loads all the sounds for the game and their volume from RuntimeConfigs
     */
    public static void load(){
        music = FileLoader.loadMusic("MainLoop.wav");
        assert music != null;
        music.setLooping(true);
        blipSelect = FileLoader.loadSound("BlipSelect.wav");
        hit = FileLoader.loadSound("Hit.wav");
        projectileCreated = FileLoader.loadSound("ProjectileCreated.wav");
        updateSFXVolume(RuntimeConfigs.getSfxVolume());
        updateMusicVolume(RuntimeConfigs.getMusicVolume());
    }

    /**
     * Plays the BlipSelect sound from GameSounds
     */
    public static void playBlipSelect(){
        blipSelect.play(sfxVolume);
    }

    /**
     * Plays the Hit sound from GameSounds
     */
    public static void playHit(){
        hit.play(sfxVolume);
    }

    /**
     * Plays the ProjectileCreated sound from GameSounds
     */
    public static void playProjectileCreated(){
        projectileCreated.play(sfxVolume);
    }

    /**
     * Plays the background music for the game with the volume specified from the database
     */
    public static void playMusic(){
        music.setVolume(musicVolume);
        music.play();
    }

    /**
     * sets the music volume to the percentage given
     * @param percentage percentage / 100 for the volume to be
     */
    public static void updateMusicVolume(int percentage){
        float musicBaseVolume = 0.8f;
        musicVolume = musicBaseVolume * percentage / 100f;
    }

    /**
     * sets the sfx volume to the percentage given
     * @param percentage percentage / 100 for the volume to be
     */
    public static void updateSFXVolume(int percentage){
        sfxVolume = percentage / 100f;
    }
}
