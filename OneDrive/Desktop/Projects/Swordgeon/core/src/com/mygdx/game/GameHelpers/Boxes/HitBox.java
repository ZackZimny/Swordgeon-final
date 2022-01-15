package com.mygdx.game.GameHelpers.Boxes;

import com.badlogic.gdx.math.Vector2;

public class HitBox extends Box {
    private HitBoxTag tag = HitBoxTag.DEFAULT;

    /**
     * Creates a HitBox with a default tag
     * @param x x-position of the bottom left corner of the box
     * @param y y-position of the bottom left corner of the box
     * @param width width of the box
     * @param height height of the box
     */
    public HitBox(float x, float y, float width, float height) {
        super(x, y, width, height);
    }

    /**
     * Creates a HitBox with a default tag
     * @param position Vector2 that represents the bottom left corner of the box
     * @param width width of the box
     * @param height height of the box
     */
    public HitBox(Vector2 position, float width, float height) {
        super(position, width, height);
    }

    /**
     * Creates a HitBox with the given tag
     * @param position Vector2 that represents the bottom left corner of the box
     * @param width width of the box
     * @param height height of the box
     * @param tag represents which entity this HitBox came from
     */
    public HitBox(Vector2 position, float width, float height, HitBoxTag tag) {
        super(position, width, height);
        this.tag = tag;
    }

    public HitBoxTag getTag() {
        return tag;
    }

}
