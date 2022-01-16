package com.mygdx.game.Screens;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

/**
 * Screen with the ability to go back to the main menu and level select with a button that has a specified action
 */
public class MenuBrowserScreen extends Screen {
    private final Button actionButton, backToLevelSelectButton, backToTitleScreenButton;
    private final BitmapFont font;
    private String title;

    /**
     * @param title title of the screen
     * @param buttonText text within the action button
     * @param color color of the title
     */
    public MenuBrowserScreen(String title, String buttonText, Color color){
        this.title = title;
        float buttonX = Button.getXByScreenPercentage(0.8f, screenWidth);
        float buttonWidth = 0.8f * screenWidth;
        float buttonHeight = screenHeight * 0.15f;
        actionButton = new Button(buttonX, 300, buttonWidth, buttonHeight, buttonText, Color.BLUE);
        backToLevelSelectButton = new Button(buttonX, 200, buttonWidth,
                buttonHeight, "Back to Level Select", Color.RED);
        backToTitleScreenButton = new Button(buttonX, 100, buttonWidth, buttonHeight, "Back to Title Screen",
                Color.BLUE);

        FreeTypeFontGenerator.FreeTypeFontParameter param = fontHandler.getParameter();
        param.color = color;
        font = fontHandler.getFontGenerator().generateFont(param);
    }

    /**
     * displays the MenuBrowserScreen to the screen
     * @param shapeRenderer shapeRender to display buttons
     * @param spriteBatch spriteBatch to display background and text
     */
    protected void render(ShapeRenderer shapeRenderer, SpriteBatch spriteBatch){
        renderBackground(spriteBatch);
        actionButton.render(shapeRenderer, spriteBatch);
        backToLevelSelectButton.render(shapeRenderer, spriteBatch);
        backToTitleScreenButton.render(shapeRenderer, spriteBatch);
        spriteBatch.begin();
        font.draw(spriteBatch, title, fontHandler.centerX(title, screenWidth), screenHeight - 20);
        spriteBatch.end();
    }

    public Button getActionButton() {
        return actionButton;
    }

    public Button getBackToLevelSelectButton() {
        return backToLevelSelectButton;
    }

    public Button getBackToTitleScreenButton() {
        return backToTitleScreenButton;
    }
}
