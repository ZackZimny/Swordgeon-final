package com.mygdx.game.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class LevelSelectScreen extends Screen {
    Button[] buttons = new Button[4];
    Button[] recordButtons = new Button[4];
    Button backButton;

    /**
     * Creates a level select screen with a back button and a record and level select button for every level
     */
    public LevelSelectScreen(){
        float screenWidth = Gdx.graphics.getWidth();
        float screenHeight = Gdx.graphics.getHeight();
        float buttonX = Button.getXByScreenPercentage(0.8f, screenWidth);
        float buttonHeight = screenHeight * 0.1f;
        for(int i = 0; i < buttons.length; i++){
            //creates alternating colors
            Color color = i % 2 == 0 ? Color.RED : Color.BLUE;
            Color opposite = i % 2 == 1 ? Color.RED : Color.BLUE;
            float buttonY = 75f * -(i + 1) + 450f;
            //creates a button with 60% of the screen width
            buttons[i] = new Button(buttonX, buttonY, screenWidth * 0.6f, buttonHeight,
                    "Level " + (i + 1), color);
            //creates a button with 20% of the screen width next to the level select button
            recordButtons[i] = new Button(buttonX + screenWidth * 0.6f, buttonY, screenWidth * 0.2f,
                    buttonHeight, "records", opposite);
        }
        backButton = new Button(buttonX, 75f * -5 + 450f, screenWidth * 0.8f, screenHeight * 0.1f, "Back",
                Color.PURPLE);
    }

    /**
     * Displays the LevelSelectScreen to the screen
     * @param shapeRenderer shapeRenderer to render buttons
     * @param spriteBatch spriteBatch to render the background and text
     */
    public void render(ShapeRenderer shapeRenderer, SpriteBatch spriteBatch){
        renderBackground(spriteBatch);
        for (Button b : buttons){
            b.render(shapeRenderer, spriteBatch);
        }
        for (Button b : recordButtons){
            b.render(shapeRenderer, spriteBatch);
        }
        backButton.render(shapeRenderer, spriteBatch);
        //displays title
        spriteBatch.begin();
        fontHandler.getFont().draw(spriteBatch, "Level Selection",
                fontHandler.centerX("Level Selection", screenWidth), screenHeight - 20);
        spriteBatch.end();
    }

    public Button[] getButtons() {
        return buttons;
    }

    public Button[] getRecordButtons() {
        return recordButtons;
    }

    public Button getBackButton() {
        return backButton;
    }
}
