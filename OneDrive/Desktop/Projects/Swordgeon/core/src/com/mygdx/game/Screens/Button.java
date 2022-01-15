package com.mygdx.game.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.mygdx.game.GameHelpers.Boxes.Box;
import com.mygdx.game.GameHelpers.GameSounds;

public class Button extends Box {
    //used for text centering
    private final GlyphLayout layout = new GlyphLayout();
    private final String text;
    private final Color bgColor;
    //used to create text
    private final FontHandler fontHandler = new FontHandler();

    /**
     * Creates a Button at the specified position
     * @param x x position of the bottom left corner of the button
     * @param y y position of the bottom left corner of the button
     * @param width width of the button's box
     * @param height height of the button's box
     * @param text text to be displayed in the center of the button
     * @param bgColor color of the box behind the button
     */
    public Button(float x, float y, float width, float height, String text, Color bgColor) {
        super(x, y, width, height);
        this.text = text;
        this.bgColor = bgColor;
        layout.setText(fontHandler.getFont(), text);
    }

    /**
     * gets the x position to horizontally center a button if it is created by a percentage of screen width
     * @param buttonWidthPercentage percentage of the screen for the button to take up
     * @param screenWidth width of the entire screen
     * @return x position to center the button
     */
    public static float getXByScreenPercentage(float buttonWidthPercentage, float screenWidth){
        return screenWidth * (1 - buttonWidthPercentage) / 2;
    }

    /**
     * @return if the mouse is within the bounds of the button
     */
    public boolean isHovered(){
        return intersects(Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY());
    }

    /**
     * @return if the mouse is within the bounds of the button and is the mouse is clicked
     */
    public boolean isClicked() { return isHovered() && Gdx.input.isButtonJustPressed(Input.Buttons.LEFT); }

    /**
     * Displays the button to the screen
     * @param shapeRenderer shapeRenderer to draw the button's box
     * @param spriteBatch spriteBatch to draw the text of the button
     */
    public void render(ShapeRenderer shapeRenderer, SpriteBatch spriteBatch){
        if(isClicked())
            GameSounds.playBlipSelect();
        shapeRenderer.setAutoShapeType(true);
        shapeRenderer.begin();
        shapeRenderer.set(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(bgColor);
        shapeRenderer.rect(x, y ,width, height);
        shapeRenderer.end();
        spriteBatch.begin();
        fontHandler.getFont().draw(spriteBatch, text, x + width / 2 - layout.width / 2,
                y + height / 2 + layout.height / 2);
        spriteBatch.end();
    }
}
