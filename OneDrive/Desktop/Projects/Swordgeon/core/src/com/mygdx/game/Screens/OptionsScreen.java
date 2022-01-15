package com.mygdx.game.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.mygdx.game.GameHelpers.DatabaseManager;
import com.mygdx.game.GameHelpers.GameSounds;
import com.mygdx.game.GameHelpers.RuntimeConfigs;

/**
 * OptionsScreen that edits runtime configurations
 */
public class OptionsScreen extends Screen {
    private final Button applyButton, backButton;
    private final Slider sfxSlider, musicSlider;
    private String name;
    public OptionsScreen(){
        float buttonWidth = 0.4f;
        float buttonX = Button.getXByScreenPercentage(buttonWidth * 2, screenWidth);
        backButton = new Button(buttonX, 50,
                screenWidth * buttonWidth, 50, "Back", Color.RED);
        applyButton = new Button(buttonX + buttonWidth * screenWidth, 50, screenWidth * buttonWidth,
                50, "Apply", Color.BLUE);
        sfxSlider = new Slider(50, 300, "Sound effects volume", fontHandler.getFont());
        musicSlider = new Slider(50, 200, "Music volume", fontHandler.getFont());
        DatabaseManager.loadRuntimeConfigData();
        name = RuntimeConfigs.getName();
        sfxSlider.setPercentage(RuntimeConfigs.getSfxVolume());
        musicSlider.setPercentage(RuntimeConfigs.getMusicVolume());
    }

    /**
     * gathers the input from the key board
     */
    private void getInput(){
        //Libgdx key bindings are 29 - 54
        //ASCII starts at 65
        for(int i = 29; i <= 54; i++) {
            if (Gdx.input.isKeyJustPressed(i)) {
                name += (char) (65 + (i - 29));
                break;
            }
        }
        if(Gdx.input.isKeyJustPressed(Input.Keys.BACKSPACE) && name.length() != 0){
            name = name.substring(0, name.length() - 1);
        }
    }

    /**
     * renders the OptionsScreen to the screen and updates the database when the apply button is clicked
     * @param shapeRenderer renders button boxes and sliders
     * @param spriteBatch sprite batch renders text
     */
    public void render(ShapeRenderer shapeRenderer, SpriteBatch spriteBatch){
        getInput();
        renderBackground(spriteBatch);
        sfxSlider.render(shapeRenderer, spriteBatch);
        musicSlider.render(shapeRenderer, spriteBatch);
        applyButton.render(shapeRenderer, spriteBatch);
        backButton.render(shapeRenderer, spriteBatch);
        if(applyButton.isClicked()){
            GameSounds.updateMusicVolume(musicSlider.getPercentage());
            GameSounds.updateSFXVolume(sfxSlider.getPercentage());
            RuntimeConfigs.setMusicVolume(musicSlider.getPercentage());
            RuntimeConfigs.setSfxVolume(sfxSlider.getPercentage());
            RuntimeConfigs.setName(name);
            DatabaseManager.updateRuntimeConfigs();
        }
        spriteBatch.begin();
        fontHandler.getFont().draw(spriteBatch, "Options", fontHandler.centerX("Options", screenWidth),
                screenHeight - 50);
        fontHandler.getFont().draw(spriteBatch, "Name: " + name, 50, screenHeight - 100);
        spriteBatch.end();
    }

    public Button getBackButton() {
        return backButton;
    }
}
