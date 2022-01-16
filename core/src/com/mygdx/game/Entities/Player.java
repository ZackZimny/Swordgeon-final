package com.mygdx.game.Entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.GameHelpers.Action;
import com.mygdx.game.GameHelpers.Boxes.Box;
import com.mygdx.game.GameHelpers.Boxes.HitBox;
import com.mygdx.game.GameHelpers.Boxes.HitBoxTag;
import com.mygdx.game.GameHelpers.Direction;
import com.mygdx.game.GameHelpers.GameSounds;
import com.mygdx.game.GameHelpers.Timer;

import java.util.ArrayList;

public class Player extends Entity {
    //enum of the cardinal directions the player can move and face
    private Animation<TextureRegion> downWalk, downSwing, upWalk, upSwing, rightWalk, rightSwing;
    //direction that the player is facing; begins facing up
    private Direction direction = Direction.UP;
    //holds and handles the lengths of certain states of the Player's swing
    private final Action swing;
    //handles the length of time that the player is knocked back when hit by an enemy
    private final Timer knockBackTimer = new Timer(2f);
    //length of time when the player cannot be hit by an enemy after he has been hit
    private final Timer invincibilityTimer = new Timer(3f);
    //current animation playing
    private Animation<TextureRegion> currentAnimation;
    //amount of time that the current animation has been playing; kept to determine the current frame of the animation
    private float stateTime = 0;
    //boolean that turns true when the player moves with the press of a key like (WASD or arrow keys)
    private boolean moving = false;
    //amount of time that each frame of an animation stays on screen
    private static final float FRAME_DURATION = 0.1f;
    //turns true when the player releases the space bar key; kept to ensure that the Player cannot repeatedly swing
    //while holding down the space bar and must release it before swinging again
    private boolean spaceReleased = true;
    //amount of time when the sword hitBox is active
    public static final float SWING_LENGTH = FRAME_DURATION;
    //repeatedly changes to its opposite when the invincibility timer is active to achieve the "flashing" effect while
    //the player is invincible
    private boolean flashInvincibility = false;
    //decrements every time the player gets hit. The player loses when it reaches 0.
    private int health;

    /**
     * Creates a Player object with the given texture and the x and y positions representing the bottom left corner
     * of the Player's HurtBox
     * @param x x position of the bottom-left corner of the Player's hurtBox
     * @param y y position of the bottom-left corner of the Player's hurtBox
     * @param texture pre-loaded SpriteSheet with all the Player's animations
     */
    public Player(float x, float y, Texture texture) {
        super(x, y, 20, 20);
        health = 8;
        swing = new Action(FRAME_DURATION * 2, FRAME_DURATION, FRAME_DURATION);
        createAnimations(texture);
    }

    /**
     * Creates animation by splitting the SpriteSheet Texture by its rows. Each row represents a different animation.
     * @param texture SpriteSheet texture to be split
     */
    private void createAnimations(Texture texture) {
        int FRAME_COLS = 4;
        int FRAME_ROWS = 6;
        //splits the SpriteSheet by its number of rows each with 4 columns for the 4 frames of animation in each
        //animation
        ArrayList<Animation<TextureRegion>> animations =
                createAnimationsArrayList(FRAME_ROWS, FRAME_COLS, texture, FRAME_DURATION);
        downWalk = animations.get(0);
        downSwing = animations.get(1);
        upWalk = animations.get(2);
        upSwing = animations.get(3);
        rightWalk = animations.get(4);
        rightSwing = animations.get(5);
    }

    /**
     * Displays the Player character on screen
     * @param spriteBatch SpriteBatch that displays all the Entities
     */
    public void render(SpriteBatch spriteBatch) {
        handleAnimation();
        TextureRegion currentFrame = currentAnimation.getKeyFrame(stateTime, true);
        //changing the width to a negative value flips the sprite
        int width = direction == Direction.LEFT ? -64 : 64;
        //when the sprite is flipped the x has to be negative to center the sprite correctly
        float spriteX = hurtBox.getX() + (direction == Direction.LEFT ? 44 : -22);
        //stops displaying/starts re-displaying the Player every frame while the invincibility timer is active
        if(invincibilityTimer.isActive()){
            flashInvincibility = !flashInvincibility;
        } else {
            flashInvincibility = false;
        }
        if(!flashInvincibility) {
            spriteBatch.begin();
            spriteBatch.draw(currentFrame, spriteX,
                    hurtBox.getY() - 22,
                    width, 64);
            spriteBatch.end();
        }
    }

    /**
     * Handles all the physics on the Player object
     * @param deltaTime time between the last frame
     * @param enemies ArrayList of all the enemies on this current level
     * @param boundaries boundaries that the player has to handle
     */
    public void handlePhysics(float deltaTime, ArrayList<Enemy> enemies, ArrayList<Boundary> boundaries){
        stateTime += deltaTime;
        //knocks the player back when hit by an enemy
        for(Enemy enemy : enemies){
            handleEnemyKnockBack(enemy, deltaTime);
        }
        //handles the input given by the player
        Vector2 velocity = handleInput(deltaTime);
        //moves the player and deals with collision with boundaries
        move(velocity, boundaries, deltaTime);
        //updates the forces on the hurtBox every frame
        hurtBox.handleForces(deltaTime);
    }

    /**
     * Handles user inputted commands to allow the character to move
     * @param deltaTime Time between frames
     */
    private Vector2 handleInput(float deltaTime) {
        //force that is added to the player while keys are pressed
        Vector2 velocity = new Vector2(0, 0);
        //Does the actions assigned to each button press
        moving = false;
        Input in = Gdx.input;
        if (in.isKeyPressed(Input.Keys.A) || in.isKeyPressed(Input.Keys.LEFT)){
            velocity.x = -1;
            direction = Direction.LEFT;
            moving = true;
        }
        else if (in.isKeyPressed(Input.Keys.D) || in.isKeyPressed(Input.Keys.RIGHT)){
            velocity.x = 1;
            direction = Direction.RIGHT;
            moving = true;
        }
        else if(in.isKeyPressed(Input.Keys.S) || in.isKeyPressed(Input.Keys.DOWN)){
            velocity.y = -1;
            direction = Direction.DOWN;
            moving = true;
        }
        else if(in.isKeyPressed(Input.Keys.W) || in.isKeyPressed(Input.Keys.UP)){
            velocity.y = 1;
            direction = Direction.UP;
            moving = true;
        }

        //begins the swing once the space key is pressed and prevents it from starting again when the space bar has
        //not been released
        if(in.isKeyPressed(Input.Keys.SPACE) && !knockBackTimer.isActive() && !moving && spaceReleased){
            swing.start(deltaTime);
            spaceReleased = false;
        } else if(!in.isKeyPressed(Input.Keys.SPACE)){
            spaceReleased = true;
        }

        //changes the swing's state based on time
        handleSwing(deltaTime);
        return velocity;
    }

    /**
     * sets the current animation based on the player's state and resets the stateTime if he is not moving or swinging
     * his sword
     */
    private void handleAnimation(){
        if(!moving && swing.getState() == Action.State.NEUTRAL)
            stateTime = 0;
        switch (direction){
            case DOWN:
                if(swing.getState() != Action.State.NEUTRAL)
                    currentAnimation = downSwing;
                else
                    currentAnimation = downWalk;
                break;
            case RIGHT:
            case LEFT:
                if(swing.getState() != Action.State.NEUTRAL)
                    currentAnimation = rightSwing;
                else
                    currentAnimation = rightWalk;
                break;
            case UP:
                if(swing.getState() != Action.State.NEUTRAL)
                    currentAnimation = upSwing;
                else
                    currentAnimation = upWalk;
                break;
        }
    }

    /**
     * moves the player and checks for boundaries if the player is knocked into or runs into them
     * @param velocity direction the player is moving based on player input
     * @param boundaries all boundaries in the world that the player can interact with
     * @param deltaTime time between the last frame
     */
    private void move(Vector2 velocity, ArrayList<Boundary> boundaries, float deltaTime) {
        //determines how quickly the player moves
        float playerSpeed = 250f;
        //moves the player according to which button is pressed so long as he is moving
        if (!velocity.equals(new Vector2(0, 0))){
            movement = velocity.cpy().nor().scl(playerSpeed * deltaTime);
        } else {
            //else statement prevents the normalizing of the Vector (0, 0), which if normalized will cause the
            //player to moved
            movement = new Vector2(0, 0);
        }
        //if the boundary is hit the player will move in the opposite direction
        if(hitBoundary(boundaries)){
            Vector2 composite = hurtBox.getForce();
            hurtBox.clearForces();
            hurtBox.addForce(composite.scl(-1));
            return;
        }
        //prevents moving if knocked back
        if(knockBackTimer.isActive() || swing.getState() != Action.State.NEUTRAL)
            return;
        //changes the hurt box position according to the input
        hurtBox.changePos(movement);
        //moves the hitBoxes along with the Player
        for(Box i : hitBoxes){
            i.changePos(movement);
        }
    }
    /**
     * Updates the hitBoxes according to the swing action's state
     * @param deltaTime amount of time in seconds since the last frame
     */
    private void handleSwing(float deltaTime) {
        //updates the state of the swing action according to how much time has passed
        swing.play(deltaTime);
        switch (swing.getState()) {
            //does nothing if the action is active or the player is not swinging
            case STARTUP:
            case NEUTRAL:
                return;
            //creates a swing hit box if the swing action is active
            case ACTIVE:
                Vector2 hbPosition = hurtBox.getPositionVector();
                float length1 = 30;
                float length2 = 35;
                float buffer = 10;
                if(direction == Direction.RIGHT || direction == Direction.LEFT)
                    //flips the position of the HitBox along with the Player's Sprite
                    hitBoxes.add(new HitBox(getDirectionVector().scl(16).add(hbPosition).sub(buffer, 0), length1,
                            length2, HitBoxTag.PLAYER_SWORD));
                else
                    //16 is roughly the size of the Sprite that includes the Player's body
                    hitBoxes.add(new HitBox(getDirectionVector().scl(16).add(hbPosition).sub(0, buffer), length2,
                            length1, HitBoxTag.PLAYER_SWORD));
                break;
            //deletes the hit boxes if the swing action is not active and prevents the player from starting it again
            case COOL_DOWN:
                hitBoxes.clear();
                break;
        }
        //Ensures that when two buttons are pressed at the same time, only one swing hurt box is created
        if(hitBoxes.size() > 1){
            HitBox first = hitBoxes.get(0);
            hitBoxes = new ArrayList<>();
            hitBoxes.add(first);
        }
    }

    /**
     * When the player intersects an enemy's HitBox, it will get knocked back
     * @param enemy enemy to check
     * @param deltaTime time between the current frame and the last frame in seconds
     */
    private void handleEnemyKnockBack(Enemy enemy, float deltaTime){
        knockBackTimer.play(deltaTime);
        invincibilityTimer.play(deltaTime);
        float enemyKnockBackStrength = 5f;
        for(int i = 0; i < enemy.getHitBoxes().size(); i++){
            HitBox hb = enemy.getHitBoxes().get(i);
            //destroys the hurtBox even if the Player is invincible to prevent recalculation when he is not invincible
            if(hb.intersects(hurtBox)){
                enemy.destroyHitBox(hb);
                if(!invincibilityTimer.isActive()){
                    GameSounds.playHit();
                    health--;
                    Vector2 hbCenter = hb.getCenter();
                    //knocks the player in the opposite direction of the enemy
                    hurtBox.addForce(hurtBox.getCenter().sub(hbCenter).nor().scl(enemyKnockBackStrength));
                    //starts knockBackTimer timer
                    knockBackTimer.start();
                    invincibilityTimer.start();
                }
            }
        }
        //clears the forces on the hitbox if the player is finished being knocked back
        if(!knockBackTimer.isActive())
            hurtBox.clearForces();
    }

    /**
     * Creates a direction vector that is -1 if the Player is pointing left or down in the respective axes, 1 if the
     * Player is facing right or up, and 0 if he isn't facing that axis.
     * @return a representative Vector of the direction the Player is facing
     */
    public Vector2 getDirectionVector(){
        switch(direction) {
            case UP:
                return new Vector2(0, 1);
            case DOWN:
                return new Vector2(0, -1);
            case RIGHT:
                return new Vector2(1, 0);
            case LEFT:
                return new Vector2(-1, 0);
            default:
                return new Vector2(0, 0);
        }
    }

    public Timer getKnockBackTimer() {
        return knockBackTimer;
    }

    public Timer getInvincibilityTimer() {
        return invincibilityTimer;
    }

    public int getHealth() {
        return health;
    }
}
