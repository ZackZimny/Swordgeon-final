package com.mygdx.game.GameHelpers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.Entities.*;

import java.util.ArrayList;

public class LevelRunner {
    private final SpriteBatch spriteBatch;
    private final SpriteBatch uiSpriteBatch;
    private final OrthographicCamera camera;
    private final ShapeRenderer shapeRenderer;
    private boolean isPlayerDead = false;
    private boolean playerHasWon = false;
    private float time = 0;
    private ArrayList<Enemy> enemies;
    private final ArrayList<Boundary> boundaries;
    private final ArrayList<Entity> entities;
    private final ArrayList<Boundary> conditionalBoundaries;
    private final ArrayList<BackgroundTile> backgroundTiles;
    private final HealthDisplay[] healthDisplays;
    private final Player player;
    private final Goal goal;
    private final int id;

    /**
     * Creates a LevelRunner with the parameters created in the LevelLoader
     * @param enemies ArrayList of the enemies in this level
     * @param boundaries ArrayList of the boundaries in this level
     * @param entities ArrayList of the entities in this level
     * @param conditionalBoundaries ArrayList of the conditional boundaries in this level
     * @param player player of this level
     * @param backgroundTiles ArrayList of the BackgroundTiles of this level
     * @param goal goal of this level
     * @param healthDisplays HealthDisplays that reload every time the level does
     * @param id level id
     */
    public LevelRunner(ArrayList<Enemy> enemies, ArrayList<Boundary> boundaries, ArrayList<Entity> entities,
                       ArrayList<Boundary> conditionalBoundaries, Player player, ArrayList<BackgroundTile>
                               backgroundTiles, Goal goal, HealthDisplay[] healthDisplays, int id){
        this.enemies = enemies;
        this.boundaries = boundaries;
        this.entities = entities;
        this.conditionalBoundaries = conditionalBoundaries;
        this.player = player;
        this.backgroundTiles = backgroundTiles;
        this.goal = goal;
        this.healthDisplays = healthDisplays;
        this.id = id;
        spriteBatch = new SpriteBatch();
        uiSpriteBatch = new SpriteBatch();
        shapeRenderer = new ShapeRenderer();
        camera = new OrthographicCamera();
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    }

    /**
     * gets the boundary conditions from the level specified in the id
     * @return Arraylist of Boolean that determine if the boundary should disappear. Scanner left-to-right and top-to-bottom
     */
    private ArrayList<Boolean> getBoundaryConditions() {
        ArrayList<Boolean> conditions = new ArrayList<>();
        switch (id){
            case 1:
                conditions.add(enemies.size() <= 1);
                conditions.add(enemies.size() <= 1);
                conditions.add(enemies.size() <= 1);
                break;
            case 2:
                for(int i = 0; i < 3; i++){
                    conditions.add(enemies.size() <= 4);
                    conditions.add(enemies.size() <= 3);
                }
                break;
            case 3:
                for(int i = 0; i < 7; i++){
                    conditions.add(enemies.size() <= 2);
                }
                for(int i = 0; i < 5; i++) {
                    conditions.add(enemies.size() <= 6);
                }
                break;
            case 4:
                for(int i = 0; i < 3; i++) {
                    conditions.add(enemies.size() <= 5);
                }
                for(int i = 0; i < 3; i++) {
                    conditions.add(enemies.size() <= 4);
                }
                for(int i = 0; i < 3; i++) {
                    conditions.add(enemies.size() <= 2);
                }
                break;
        }
        return conditions;
    }

    /**
     * draws backgroundTiles to the screen
     */
    private void drawBackground(){
        for(BackgroundTile b : backgroundTiles){
            b.render(spriteBatch);
        }
    }

    /**
     * handles all knock back and physics that occur during the game loop
     * @param deltaTime time between the current frame and the last one was rendered
     */
    private void handlePhysics(float deltaTime){
        player.handlePhysics(deltaTime, enemies, boundaries);
        for(Enemy enemy : enemies){
            enemy.handlePhysics(deltaTime, player, boundaries);
        }
    }

    /**
     * makes boundaries disappear if the number of enemies has been defeated for them to disappear. If they disappear
     * they are removed from the ArrayList of boundaries and entities
     */
    private void updateConditionalBoundaries() {
        int count = 0;
        ArrayList<Boolean> boundaryConditions = getBoundaryConditions();
        System.out.println(boundaryConditions);
        for(Boundary conditionalBoundary : conditionalBoundaries){
            if(boundaryConditions.get(count)){
                boundaries.remove(conditionalBoundary);
                entities.remove(conditionalBoundary);
            }
            count++;
        }
    }

    /**
     * runs all frame-by-frame checks
     */
    private void updateEntityStates(){
        killEnemies();
        goal.setOpen(enemies.size() == 0);
        updateConditionalBoundaries();
        isPlayerDead = player.getHealth() == 0;
        playerHasWon = player.getHurtBox().intersects(goal.getHurtBox()) && goal.isOpen();
    }


    /**
     * displays all Entities and UI components to the screen during the main game loop
     */
    public void render(){
        spriteBatch.setProjectionMatrix(camera.combined);
        shapeRenderer.setProjectionMatrix(camera.combined);
        drawBackground();
        for(Entity e : entities){
            e.render(spriteBatch);
        }
          /*
        used to help debug Hitboxes
        for(Entity e : entities){
            e.showBoxes(shapeRenderer);
        }
           */
    }

    /**
     * removes enemies from their arrayList if they die
     */
    public void killEnemies(){
        ArrayList<Enemy> enemiesNotDead = new ArrayList<>();
        for(Enemy e : enemies){
            if(!e.isDead()){
                enemiesNotDead.add(e);
            } else {
                entities.remove(e);
            }
        }
        enemies = enemiesNotDead;
    }

    /**
     * changes the state of the health displays if the player is hit
     */
    private void updateHealthDisplays(){
        int MAX_PLAYER_HEALTH = 8;
        for(int i = MAX_PLAYER_HEALTH - 1; i >= player.getHealth(); i--){
            healthDisplays[i].setActive(false);
        }
    }

    /**
     * displays the health displays to the screen
     * @param spriteBatch spritebatch to draw the health displays
     */
    private void renderHealthDisplays(SpriteBatch spriteBatch){
        for(HealthDisplay healthDisplay : healthDisplays){
            healthDisplay.render(spriteBatch);
        }
    }

    /**
     * runs the game's main loop and displays it to the screen
     */
    public void run(){
        //clears the screen upon every frame
        Gdx.gl.glClearColor(0f, 0f, 0f,1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        //number of seconds that has passed since the last frame was loaded; ensures that no matter how fast or slow
        //your computer is, it will not affect how quickly objects move in the game.
        float deltaTime = Gdx.graphics.getDeltaTime();
        handlePhysics(deltaTime);
        updateEntityStates();
        //centers the camera position on the player
        Vector2 playerCenter = player.getHurtBox().getCenter();
        camera.position.set(playerCenter.x, playerCenter.y, 0);
        camera.update();
        render();
        camera.position.set(0, 0, 0);
        spriteBatch.setProjectionMatrix(camera.view);
        updateHealthDisplays();
        renderHealthDisplays(uiSpriteBatch);
        time += deltaTime;
    }

    public boolean isPlayerDead() {
        return isPlayerDead;
    }

    public boolean playerHasWon() {
        return playerHasWon;
    }

    public float getTime() {
        return time;
    }
}
