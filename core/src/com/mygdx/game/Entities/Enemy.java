package com.mygdx.game.Entities;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.GameHelpers.Boxes.HitBox;
import com.mygdx.game.GameHelpers.Boxes.HitBoxTag;
import com.mygdx.game.GameHelpers.GameSounds;
import com.mygdx.game.GameHelpers.Timer;

import java.util.ArrayList;

/**
 * Child of the Entity object. Attacks the character when spawned and drains his health.
 * Can be extended to add extra functionality. Has functionality for getting hit by the player and whether it is dead
 * @see Entity
 */
public class Enemy extends Entity {
    //provides invincibility after being hit so that when the enemy is hit, the forces aren't added multiple times
    protected Timer hitInvincibility;
    //pixels moved per second
    protected float speed = 100f;
    //hits until this Enemy is destroyed
    protected int hitsToDeath = 3;
    //hits this enemy has currently taken
    protected int hits = 0;
    //takes knock back when it is hit when this is true
    protected boolean movesOnHit = true;

    /**
     * Creates an Enemy. PlayerSwingLength is needed to calculate the amount of invincibility the enemy needs
     * to have after being hit
     * @param x x-position in the bottom left corner of the hurtBox
     * @param y y-position in the bottom left corner of the hurtBox
     * @param width width of the hurtBox
     * @param height height of the hurtBox
     * @param playerSwingLength length of time when the player swing is active
     */
    public Enemy(float x, float y, float width, float height, float playerSwingLength) {
        super(x, y, width, height);
        //the invincibility must last as long as the active frames as the swing so that
        //the forces of the swing aren't added multiple times
        hitInvincibility = new Timer(playerSwingLength);
    }

    /**
     * Deals enemy knock back when hit by the player and applies it to the DynamicBox hurtBox
     * @param deltaTime time between the last frame
     * @param player user-controlled player character
     */
    public void handlePhysics(float deltaTime, Player player, ArrayList<Boundary> boundaries){
        handleHit(player, deltaTime);
        //dissipates every frame
        hurtBox.handleForces(deltaTime);
    }
    /**
     * Handles the knock back of being hit by the player
     * @param player user controlled character object
     * @param deltaTime amount of time that has passed from the last frame
     */
    public void handleHit(Player player, float deltaTime) {
        //determines how far the enemy falls back if it is hit
        float strength = 5f;
        for (HitBox hb : player.getHitBoxes()) {
            //starts the knock back of the Enemy object if it is within the swing hit box of the player
            if (!hitInvincibility.isActive() && hb.intersects(hurtBox) && hb.getTag() == HitBoxTag.PLAYER_SWORD) {
                GameSounds.playHit();
                //knocks back the enemy in the opposite direction the enemy is facing
                hurtBox.addForce(player.getDirectionVector().scl(strength));
                //begins the hit invincibility so that the knock back is not calculated multiple times when the enemy overlaps with the swing hit box
                hitInvincibility.start();
                hits++;
            }
        }
        //starts the timer of the invincibility if it has started
        hitInvincibility.play(deltaTime);
        //applies friction to the forces if they exist
        if(movesOnHit)
            hurtBox.handleForces(deltaTime);
    }

    //overwritten in children with more functionality; only used as a placeholder
    public void render(SpriteBatch spriteBatch){
        //do nothing
    }
    public Vector2 getMovement() {
        return movement;
    }

    public boolean isDead() {
        return hits >= hitsToDeath;
    }
}
