package com.mygdx.game.Screens;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.mygdx.game.GameHelpers.Record;

import java.util.ArrayList;

/**
 * Screen that displays the fastest times
 */
public class RecordScreen extends Screen {
    private final Button backButton;
    private final Record[] records;
    private final int level;

    /**
     * Generates a screen from the Records inserted
     * @param records records gathered from database
     * @param level level that the records are from
     */
    public RecordScreen(Record[] records, int level){
        this.records = records;
        this.level = level;
        backButton = new Button(Button.getXByScreenPercentage(0.8f, screenWidth), 50,
                0.8f * screenWidth, 0.15f * screenHeight, "Back", Color.BLUE);
    }

    /**
     * Displays the records to the screen
     * @param shapeRenderer shapeRenderer to display buttons
     * @param spriteBatch spriteBatch to display text
     */
    public void render(ShapeRenderer shapeRenderer, SpriteBatch spriteBatch){
        renderBackground(spriteBatch);
        String text = "Level " + level + " Records";
        spriteBatch.begin();
        fontHandler.getFont().draw(spriteBatch, text, fontHandler.centerX(text, screenWidth),
                screenHeight - 20);
        spriteBatch.end();
        backButton.render(shapeRenderer, spriteBatch);
        float bottomPadding = 200f;
        float rowHeight = 50f;
        float leftPadding = 175f;
        spriteBatch.begin();
        for(int i = 0; i < records.length; i++){
            float fontY = i * rowHeight + bottomPadding;
            fontHandler.getFont().draw(spriteBatch, records[i].getTimeString(), leftPadding,
                    fontY);
            fontHandler.getFont().draw(spriteBatch, records[i].getName(), leftPadding + 200,
                    fontY);
        }
        spriteBatch.end();
    }

    public Button getBackButton() {
        return backButton;
    }
}
