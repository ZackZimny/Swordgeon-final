package com.mygdx.game.Entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.GameHelpers.Boxes.Box;
import com.mygdx.game.GameHelpers.Boxes.HitBox;
import com.mygdx.game.GameHelpers.GameSounds;
import com.mygdx.game.GameHelpers.Timer;

import java.util.ArrayList;

/**
 * Child of the Enemy class. Is able to shoot tracking projectiles towards the player that he can deflect with his sword
 */
public class ShootingEnemy extends Enemy {
    //animation that plays while the ShootingEnemy charges his projectiles
    Animation<TextureRegion> shootAnimation;
    //Texture that displays for the projectiles created
    Texture projectileTexture;
    //charging time between each shot
    private final float SHOOT_TIME = 3f;
    //time that the current animation has been running; used to access the current frame to display
    private float stateTime = 0f;
    //a new tracking bullet is created when this timer goes off
    Timer bulletTimer = new Timer(SHOOT_TIME);
    public ShootingEnemy(float x, float y, float playerSwingLength, Texture texture, Texture projectileTexture) {
        super(x, y, 22, 22, playerSwingLength);
        createAnimations(texture);
        this.projectileTexture = projectileTexture;
        movesOnHit = false;
    }

    private void createAnimations(Texture texture){
        int FRAME_ROWS = 1;
        int FRAME_COLS = 2;
        ArrayList<Animation<TextureRegion>> animations =
                createAnimationsArrayList(FRAME_ROWS, FRAME_COLS, texture, SHOOT_TIME);
        shootAnimation = animations.get(0);
    }

    private void trackBullets(Player player){
        final float speed = 1.5f;
        for(HitBox h : hitBoxes){
            Vector2 direction = player.getHurtBox().getCenter().sub(h.getCenter()).nor();
            h.changePos(direction.scl(speed));
        }
    }

    private void destroyBullets(Player player, ArrayList<Boundary> boundaries){
        ArrayList<Box> impactBoxes = new ArrayList<Box>(player.getHitBoxes());
        for(Boundary boundary : boundaries){
            impactBoxes.add(boundary.getHurtBox());
        }
        destroyBulletOnImpact(impactBoxes);
    }

    private void destroyBulletOnImpact(ArrayList<Box> boxes){
        ArrayList<HitBox> bulletCopy = new ArrayList<>();
        for(HitBox bullet : hitBoxes){
            boolean hit = false;
            for(Box box : boxes){
                if(bullet.intersects(box)){
                    hit = true;
                    break;
                }
            }
            if(!hit)
                bulletCopy.add(bullet);
        }
        hitBoxes = bulletCopy;
    }

    public void handlePhysics(float deltaTime, Player player, ArrayList<Boundary> boundaries){
        bulletTimer.start();
        stateTime += deltaTime;
        bulletTimer.play(deltaTime);
        handleHit(player, deltaTime);
        if(!bulletTimer.isActive()){
            GameSounds.playProjectileCreated();
            hitBoxes.add(new HitBox(hurtBox.getX(), hurtBox.getY(), 5, 5));
        }
        trackBullets(player);
        destroyBullets(player, boundaries);
        stateTime += deltaTime;
    }

    public void render(SpriteBatch spriteBatch) {
        TextureRegion currentFrame = shootAnimation.getKeyFrame(stateTime, true);
        spriteBatch.begin();
        spriteBatch.draw(currentFrame, hurtBox.getX() - 4, hurtBox.getY() - 4, 32, 32);
        for(HitBox hitBox : hitBoxes){
            spriteBatch.draw(projectileTexture, hitBox.getX() - 8, hitBox.getY() - 8, 20, 20);
        }
        spriteBatch.end();
    }

}
