package com.mygdx.game.GameHelpers.Boxes;

import com.badlogic.gdx.math.Vector2;


/**
 * Child of the Box object. Adds the ability to add forces to the box without rotation.
 * @see Box
 */
public class DynamicBox extends Box {
    private Vector2 force = new Vector2(0, 0);
    public DynamicBox(float x, float y, float width, float height) {
        super(x, y, width, height);
    }

    /**
     * Adds force to the forces ArrayList to act upon this box
     * @param newForce new force acting upon this box
     */
    public void addForce(Vector2 newForce){
        force.add(newForce);
    }

    /**
     * Handles the physics of the force; adds friction
     * @param deltaTime time since the last frame
     */
    public void handleForces(float deltaTime){
        changePos(force);
        force.scl(1 - deltaTime);
    }

    /**
     * Deletes all forces on this object
     */
    public void clearForces(){
        force = new Vector2(0, 0);
    }

    public Vector2 getForce() {
        return force;
    }

    /**
     * Creates a copy of the DynamicBox with its force intact
     * @return DynamicBox copy
     */
    public DynamicBox copy(){
        DynamicBox copy = new DynamicBox(x, y, width, height);
        copy.addForce(force);
        return copy;
    }
}
