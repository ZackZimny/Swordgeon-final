package com.mygdx.game.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.mygdx.game.GameHelpers.Boxes.Box;

public class Slider {
    private int percentage = 50;
    private final float width = 100;
    private final float x;
    private final float y;
    private final String text;
    private final BitmapFont font;
    private final Box hitBox;
    private float circleX;

    /**
     * displays a 100 pixel width slider at the specified position
     * @param x x position of the bottom left corner
     * @param y y position of the bottom left corner
     * @param text text to display by the slider
     * @param font font to display text
     */
    public Slider(float x, float y, String text, BitmapFont font){
        this.x = x;
        this.y = y;
        this.text = text;
        this.font = font;
        circleX = percentage + x;
        hitBox = new Box(x, y - 20f, width, 40f);
    }

    /**
     * Displays the slider to the screen
     * @param shapeRenderer shapeRenderer to render the slider
     * @param spriteBatch SpriteBatch to render the text
     */
    public void render(ShapeRenderer shapeRenderer, SpriteBatch spriteBatch){
        handleInput();
        float rectHeight = 4;
        spriteBatch.begin();
        font.draw(spriteBatch, text, x + width + 20, y + 10);
        spriteBatch.end();
        shapeRenderer.setColor(Color.PURPLE);
        shapeRenderer.begin();
        shapeRenderer.set(ShapeRenderer.ShapeType.Line);
        shapeRenderer.rect(hitBox.getX(), hitBox.getY(), hitBox.getWidth(), hitBox.getHeight());
        shapeRenderer.set(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.rect(x, y, width, rectHeight);
        float radius = 10;
        shapeRenderer.circle(circleX, y, radius);
        shapeRenderer.end();
    }

    /**
     * @return if the mouse is within the circle
     */
    private boolean circleClicked(){
        return hitBox.intersects(Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY()) &&
                Gdx.input.isButtonJustPressed(Input.Buttons.LEFT);
    }

    /**
     * keeps the specified value within a certain range
     * @param value value to keep in the range
     * @param min lowest possible number the value can be
     * @param max highest number the value can be
     * @return value within the clamp range
     */
    private float clamp(float value, float min, float max){
        if(value < min)
            return min;
        else if(value > max)
            return max;
        return value;
    }

    /**
     * changes the percentage if the slider is clicked
     */
    public void handleInput(){
        if(circleClicked()){
            circleX = clamp(Gdx.input.getX(), x, x + width);
            percentage =  Math.round(circleX - x);
        }
    }

    public int getPercentage() {
        return percentage;
    }

    public void setPercentage(int percentage) {
        this.percentage = percentage;
        circleX = percentage + x;
    }
}
