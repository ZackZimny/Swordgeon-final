package com.mygdx.game.Entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.GameHelpers.Boxes.DynamicBox;

/**
 * Entity that other entities cannot pass through
 */
public class Boundary extends Entity {
    private final Texture texture;

    /**
     * Creates a Boundary Entity that no other Entity can pass through
     * @param x x-position in the bottom left corner
     * @param y y-position in the bottom left corner
     * @param texture pre-loaded texture that this boundary will render with
     */
    public Boundary(float x, float y, Texture texture){
        super(x, y, 32, 32);
        this.texture = texture;
    }

    /**
     * Checks to see if the entity passed in as a parameter will run into this boundary
     * @param entity entity to be checked
     * @return boolean that is true if the boundary and entity are overlapping
     */
    public boolean hitBoundary(Entity entity) {
        //create a copy of the enemy hurtBox so that any calculations we do on it will not apply to the
        //entity's hurtBox
        DynamicBox entityHurtBox = entity.getHurtBox().copy();
        //gathers all the movement of the Entity being checked
        Vector2 entityCompositeVector = entityHurtBox.getForce().add(entity.getMovement());
        //Scaling the composite vector by two creates a "phantom" move of what would happen on the next frame.
        //This must be done instead of checking the position on the current frame so that the entity does not
        //get stuck in the boundary.
        entityHurtBox.changePos(entityCompositeVector.scl(2f));
        return entityHurtBox.intersects(hurtBox);
    }

    /**
     * Renders the Boundary at its position using the current SpriteBatch.
     * @param spriteBatch-current SpriteBatch
     */
    public void render(SpriteBatch spriteBatch) {
        spriteBatch.begin();
        spriteBatch.draw(texture, hurtBox.getX(), hurtBox.getY(), 32, 32);
        spriteBatch.end();
    }

}