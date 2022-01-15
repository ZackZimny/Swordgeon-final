package com.mygdx.game.Screens;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class TutorialScreen extends Screen {
    private final Button backButton;
    private final Button nextButton;

    /**
     * Parent class that automatically adds back and next buttons
     */
    public TutorialScreen(){
        float buttonWidth = (screenWidth - 40) / 2;
        backButton = new Button(20, 30f,
                buttonWidth, 0.1f * screenHeight, "Back", Color.BLUE);
        nextButton = new Button(20 + buttonWidth, 30f,
                buttonWidth, 0.1f * screenHeight, "Next", Color.PURPLE);
    }

    /**
     * Displays back and next buttons to the screen
     * @param shapeRenderer shapeRenderer to display the buttons with
     * @param spriteBatch spriteBatch to load the font on the button
     */
    protected void renderButtons(ShapeRenderer shapeRenderer, SpriteBatch spriteBatch){
        backButton.render(shapeRenderer, spriteBatch);
        nextButton.render(shapeRenderer, spriteBatch);
    }

    public Button getBackButton() {
        return backButton;
    }

    public Button getNextButton() {
        return nextButton;
    }
}
