package com.mygdx.game.GameHelpers;

/**
 * holds the startup, active, and cool down times of an action and handles the timer for these actions
 */
public class Action {
    //amount of seconds before the active frames begin
    private final float startup;
    //amount of seconds while the action is active
    private final float active;
    //amount of seconds before the action can begin again
    private final float coolDown;
    //amount of seconds since the action began; resets to zero upon the end of cool down
    private float timer = 0;
    //decides state based upon what phase the action is in; is NEUTRAL if the action has not been started
    public enum State {
        STARTUP,
        ACTIVE,
        COOL_DOWN,
        NEUTRAL
    }
    //holds the current phase of the action; is NEUTRAL if the action has not been started
    private State state = State.NEUTRAL;

    /**
     * Does operations with the number of seconds each part of an action requires
     * @param startup Number of seconds before an action begins
     * @param active Number of seconds that the action lasts
     * @param coolDown Number of seconds before the action can be performed again
     */
    public Action(float startup, float active, float coolDown){
        this.startup = startup;
        this.active = active;
        this.coolDown = coolDown;
    }

    /**
     * Begins the action by changing its state from NEUTRAL to STARTUP
     * @param deltaTime amount of time since the previous frame; used to keep timing
     */
    public void start(float deltaTime){
        //the action can only begin if the action is in a neutral state
        if(state == State.NEUTRAL){
            state = State.STARTUP;
        }
        play(deltaTime);
    }

    /**
     * Updates the timer when the state is not neutral
     * @param deltaTime amount of time since the previous frame; used to keep timing
     */
    public void play(float deltaTime){
        //prevents timer being started when the action has not begun
        if(state == State.NEUTRAL)
            return;
        //decides state upon how long the timer has been on
        if(timer <= startup){
            state = State.STARTUP;
        } else if(timer <= startup + active){
            state = State.ACTIVE;
        } else if(timer <= startup + active + coolDown){
            state = State.COOL_DOWN;
        } else {
            state = State.NEUTRAL;
            timer = 0;
            return;
        }
        //updates timer
        timer += deltaTime;
    }

    /**
     * @return current state of the action; can be STARTUP, ACTIVE, COOL_DOWN, or NEUTRAL
     */
    public State getState() {
        return state;
    }


}
