package com.mygdx.game.Entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.GameHelpers.Boxes.Box;
import com.mygdx.game.GameHelpers.Boxes.HitBox;

import java.util.ArrayList;

public class TrackingEnemy extends Enemy {
    private Animation<TextureRegion> slither;
    private float stateTime = 0;
    /**
     * Creates a tracking enemy
     * @param playerSwingLength The amount of time the player's swing is active; needed to determine how long the Enemy's invincibility is after it is hit
     */
    public TrackingEnemy(float x, float y, float playerSwingLength, Texture texture) {
        super(x, y, 32, 16, playerSwingLength);
        createAnimations(texture);
    }

    private void createAnimations(Texture texture){
        int FRAME_COLS = 2;
        int FRAME_ROWS = 1;
        ArrayList<Animation<TextureRegion>> animations =
                createAnimationsArrayList(FRAME_ROWS, FRAME_COLS, texture, 0.25f);
        slither = animations.get(0);
    }
    /**
     * Moves the Enemy towards the Player
     * @param player user controlled Player object
     * @param deltaTime time since the last frame
     */
    public void moveToward(float deltaTime, Player player, ArrayList<Boundary> boundaries){
        //created Vector2 to do Vector operations on
        Vector2 position = new Vector2(hurtBox.getX(), hurtBox.getY());
        Box playerBox = player.getHurtBox();
        Vector2 playerPosition = new Vector2(playerBox.getX(), playerBox.getY());

        //Points the Enemy toward the Player by subtracting the Player's position by the Enemy's position
        //must be calculated before the boundary check to ensure that the "false" move before the collision
        //check works
        movement = playerPosition.sub(position).nor().scl(speed).scl(deltaTime);
        //bounces the player in the opposite direction of the boundary if the enemy hits it
        if(hitBoundary(boundaries)) {
            hurtBox.clearForces();
            movement = new Vector2(0, 0);
        }
        //stops attacking if overlapping the player
        if(player.getInvincibilityTimer().isActive()){
            hitBoxes.clear();
        } else if(!player.getKnockBackTimer().isActive() && hurtBox.intersects(playerBox)){
            hitBoxes.add(new HitBox(position, 32, 16));
        }
        //changes the enemy's position according to its movement
        hurtBox.changePos(movement);
    }

    /**
     * handles all the physics before the Enemy is rendered
     * @param deltaTime time between the last frame
     * @param player user controlled player character
     * @param boundaries all the boundaries in the world
     */
    public void handlePhysics(float deltaTime, Player player, ArrayList<Boundary> boundaries) {
        stateTime += deltaTime;
        moveToward(deltaTime, player, boundaries);
        handleHit(player, deltaTime);
        //bounces back toward the player
        //super.handlePhysics(deltaTime, player, boundaries);
    }

    /**
     * Displays the TrackingEnemy to the screen
     * @param spriteBatch SpriteBatch object used to draw the TrackingEnemy
     */
    public void render(SpriteBatch spriteBatch) {
        TextureRegion currentFrame = slither.getKeyFrame(stateTime, true);
        spriteBatch.begin();
        spriteBatch.draw(currentFrame, hurtBox.getX(), hurtBox.getY() - 8, 32, 32);
        spriteBatch.end();
    }
}
