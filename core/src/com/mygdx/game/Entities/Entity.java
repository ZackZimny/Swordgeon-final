package com.mygdx.game.Entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.GameHelpers.Boxes.Box;
import com.mygdx.game.GameHelpers.Boxes.DynamicBox;
import com.mygdx.game.GameHelpers.Boxes.HitBox;

import java.util.ArrayList;

/**
 * Has a hurtBox, which is an instance of the DynamicBox class and an ArrayList of hitBoxes, which represent the collision
 * of how the entity interacts with its environment. The hit boxes are instances of the HitBox class.
 * @see HitBox
 * @see DynamicBox
 */
public class Entity {
    //the Entity's position on the screen. Can be acted upon with physics
    protected DynamicBox hurtBox;
    //a list of the entities hit boxes, which are the extra collision boxes that help the entity interact with its environment
    protected ArrayList<HitBox> hitBoxes;
    protected Vector2 movement = new Vector2(0, 0);

    /**
     * Creates an Entity using an x, y, width, and height to represent the hurtBox
     * @param x x position of the bottom-left corner of the hurtBox
     * @param y y position of the bottom-left corner of the hurtBox
     * @param width width of the hurtBox in pixels
     * @param height height of the hurtBox in pixels
     */
    public Entity (float x, float y, float width, float height){
        hurtBox = new DynamicBox(x - width / 2, y - height / 2, width, height);
        hitBoxes = new ArrayList<>();
    }

    /**
     * Creates a 2 dimensional arrayList with each index representing a certain cell in the Texture
     * @param frameRows number of rows of sprites to split in the spriteSheet; number of Animations that will be returned
     * @param frameCols number of columns of sprites to split in the spriteSheet; length of each Animation in number of frames
     * @param spriteSheet spriteSheet to split into the Animation ArrayList
     * @param frameDuration length that each frame of the Animation displays on the screen
     * @return an Animation ArrayList with equal length Animations.
     */
    public static ArrayList<Animation<TextureRegion>> createAnimationsArrayList(int frameRows, int frameCols,
                                                                                Texture spriteSheet, float frameDuration){
        TextureRegion[][] tmp = TextureRegion.split(spriteSheet,
                spriteSheet.getWidth() / frameCols,
                spriteSheet.getHeight() / frameRows);
        ArrayList<Animation<TextureRegion>> animations = new ArrayList<>();
        for(TextureRegion[] t : tmp){
            animations.add(new Animation<>(frameDuration, t));
        }
        return animations;
    }
    /**
     * Displays the Entity's hurt boxes and hit boxes on screen (for DEBUG mode only)
     * @param sr ShapeRenderer that renders the shapes that are drawn in the function
     */
    public void showBoxes(ShapeRenderer sr) {
        //initializes the shapeRenderer
        sr.setAutoShapeType(true);
        sr.begin();
        sr.set(ShapeRenderer.ShapeType.Line);
        //makes the hurtBoxes blue
        sr.setColor(Color.BLUE);
        sr.rect(hurtBox.getX(), hurtBox.getY(), hurtBox.getWidth(), hurtBox.getHeight());
        //makes the hurtBoxes red
        sr.setColor(Color.RED);
        for(Box i : hitBoxes){
            sr.rect(i.getX(), i.getY(), i.getWidth(), i.getHeight());
        }
        sr.end();
    }

    //Method created as a placeholder for the children to override
    public void render(SpriteBatch spriteBatch){
        //do nothing
    }

    /**
     * Looks at all the Boundaries and returns if the entity "runs into" any of them
     * @param boundaries all the Boundaries in the level
     * @return if the Entity runs into any boundaries in the level
     */
    public boolean hitBoundary(ArrayList<Boundary> boundaries){
        for(Boundary b : boundaries){
            if(b.hitBoundary(this)){
                return true;
            }
        }
        return false;
    }

    /**
     * removes the specified HitBox from the Entity's HitBox ArrayList
     * @param hb HitBox to be removed
     */
    public void destroyHitBox(HitBox hb){
        hitBoxes.remove(hb);
    }

    public ArrayList<HitBox> getHitBoxes() {
        return hitBoxes;
    }

    public DynamicBox getHurtBox() {
        return hurtBox;
    }

    public Vector2 getMovement() {
        return movement;
    }
}
