package com.mygdx.game.GameHelpers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.*;
import com.mygdx.game.Entities.*;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Map;

/**
 * Loads the Levels created in the level(number).png files
 */
public class LevelLoader {
    private enum EntityType {
        SHOOTING_ENEMY,
        TRACKING_ENEMY,
        PLAYER,
        BOUNDARY,
        BACKGROUND,
        GOAL,
        CONDITIONAL_BOUNDARY,
        NONE
    }
    //Holds the specified color for each entity and what entity that color represents
    private final Hashtable<String, EntityType> colorEntityMap = new Hashtable<>();
    private Texture boundaryTexture, goalTexture, playerTexture, shootingEnemyTexture,
            trackingEnemyTexture, projectileTexture, backgroundTexture, healthHeartTexture;
    //array of levels to be run when selected
    private final LevelRunner[] levels = new LevelRunner[4];

    /**
     * loads levels and textures
     */
    public void load(){
        createColorEntityMap();
        loadTextures();
        loadLevels();
    }

    /**
     * loads all the textures used in the game
     */
    private void loadTextures(){
        boundaryTexture = FileLoader.loadTexture("Boundary.png");
        goalTexture = FileLoader.loadTexture("Goal.png");
        playerTexture = FileLoader.loadTexture("Player.png");
        shootingEnemyTexture = FileLoader.loadTexture("ShootingEnemy.png");
        trackingEnemyTexture = FileLoader.loadTexture("TrackingEnemy.png");
        backgroundTexture = FileLoader.loadTexture("BackgroundTile.png");
        projectileTexture = FileLoader.loadTexture("EnemyProjectile.png");
        healthHeartTexture = FileLoader.loadTexture("HealthHeart.png");
    }

    /**
     * loads all the levels and adds them to the levels array
     */
    private void loadLevels(){
        for(int i = 0; i < 4; i++){
            levels[i] = loadLevel(i + 1);
        }
    }

    /**
     * loads the level specified by the parameter
     * @param level level to load
     * @return loaded LevelRunner
     */
    public LevelRunner loadLevel(int level){
        return buildLevelRunner(loadPixmap(level), level);
    }

    /**
     * loads the pixels from the level.png file specified by the parameter
     * @param level level to load the image for
     * @return Pixmap with each of the individual pixels loaded
     */
    private Pixmap loadPixmap(int level){
        Texture levelTexture = new Texture(Gdx.files.internal("Level" + level + ".png"));
        if(!levelTexture.getTextureData().isPrepared())
            levelTexture.getTextureData().prepare();
        return levelTexture.getTextureData().consumePixmap();
    }

    /**
     * creates the HealthDisplays and sets their position on screen
     * @return HealthDisplay[] in their proper positions
     */
    private HealthDisplay[] createHealthDisplays(){
        float height = Gdx.graphics.getHeight();
        HealthDisplay[] healthDisplays = new HealthDisplay[8];
        for(int i = 0; i < healthDisplays.length; i++){
            healthDisplays[i] = new HealthDisplay(i * 20 + 10, height - 20, healthHeartTexture);
        }
        return healthDisplays;
    }

    /**
     * Creates a map with the colors and their corresponding EntityType in the image
     */
    private void createColorEntityMap(){
        colorEntityMap.put("#ff6804", EntityType.BOUNDARY);
        colorEntityMap.put("#0035ff", EntityType.PLAYER);
        colorEntityMap.put("#ff0000", EntityType.TRACKING_ENEMY);
        colorEntityMap.put("#9a0b96", EntityType.SHOOTING_ENEMY);
        colorEntityMap.put("#ffffff", EntityType.BACKGROUND);
        colorEntityMap.put("#0e8300", EntityType.GOAL);
        colorEntityMap.put("#ff6f6f", EntityType.CONDITIONAL_BOUNDARY);
    }

    /**
     * Gets a specified pixel from the pixmap
     * @param x x position of the pixel
     * @param y y position of the pixel
     * @param pixmap pixmap to take the pixel from
     * @return color string of the pixel formatted to the colorEntityMap Strings
     */
    private String getPixel(int x, int y, Pixmap pixmap){
        if(x > pixmap.getWidth() || x < 0 || y > pixmap.getHeight() || y < 0)
            throw new IndexOutOfBoundsException("getPixel index out of bounds");
        Color color = new Color(pixmap.getPixel(x, y));
        String colorString = color.toString();
        //Substring removes the alpha parts of the hex that are not needed and the pound sign is added
        //to match the format of the HashTable
        return "#" + colorString.substring(0, colorString.length() - 2);
    }

    /**
     * finds the entity type from the color given
     * @param color color to check against the colorEntityMap
     * @return corresponding EntityType
     */
    private EntityType findEntityType(String color){
        for (Map.Entry<String, EntityType> mapElement : colorEntityMap.entrySet()) {
            String key = mapElement.getKey();
            if(color.equals(key)){
                return colorEntityMap.get(key);
            }
        }
        return EntityType.NONE;
    }

    /**
     * Creates a LevelRunner to run the game with
     * @param pixmap pixmap of the level to load
     * @param level level to load
     * @return Runnable version of the loaded level
     * @see LevelRunner
     */
    public LevelRunner buildLevelRunner(Pixmap pixmap, int level){
        Player player = null;
        ArrayList<Boundary> boundaries = new ArrayList<>();
        ArrayList<Enemy> enemies = new ArrayList<>();
        ArrayList<Boundary> conditionalBoundaries = new ArrayList<>();
        ArrayList<Entity> entities = new ArrayList<>();
        ArrayList<BackgroundTile> backgroundTiles = new ArrayList<>();
        Goal goal = null;
        //gets a pixel from the pixmap and if the pixel corresponds to a certain color on the pixmap, it will be loaded
        //into the LevelRunner at the position the pixel was at and added to any extra ArrayList needed.
        for(int y = 0; y < pixmap.getHeight(); y++){
            for(int x = 0; x < pixmap.getWidth(); x++){
                EntityType entityType = findEntityType(getPixel(x, y, pixmap));
                int GRID_SIZE = 32;
                switch (entityType){
                    case NONE:
                        break;
                    case PLAYER:
                        player = new Player(x * GRID_SIZE, y * GRID_SIZE, playerTexture);
                        break;
                    case BOUNDARY:
                        boundaries.add(new Boundary(x * GRID_SIZE, y * GRID_SIZE, boundaryTexture));
                        break;
                    case TRACKING_ENEMY:
                        enemies.add(new TrackingEnemy(x * GRID_SIZE, y * GRID_SIZE, Player.SWING_LENGTH,
                                trackingEnemyTexture));
                        break;
                    case SHOOTING_ENEMY:
                        enemies.add(new ShootingEnemy(x * GRID_SIZE, y * GRID_SIZE, Player.SWING_LENGTH,
                                shootingEnemyTexture, projectileTexture));
                        break;
                    case GOAL:
                        goal = new Goal(x * GRID_SIZE, y * GRID_SIZE, goalTexture);
                        break;
                    case CONDITIONAL_BOUNDARY:
                        Boundary conditionalBoundary = new Boundary(x * GRID_SIZE, y * GRID_SIZE,
                                boundaryTexture);
                        boundaries.add(conditionalBoundary);
                        conditionalBoundaries.add(conditionalBoundary);
                }
                if(entityType != EntityType.NONE && entityType != EntityType.BOUNDARY)
                    backgroundTiles.add(new BackgroundTile(x * GRID_SIZE - 16, y * GRID_SIZE - 16,
                            backgroundTexture));
            }
        }
        try {
            entities.add(player);
        } catch (NullPointerException e){
            CrashLogHandler.logSevere("Player not found in level.", e.getMessage());
        }
        try {
            entities.add(goal);
        } catch(NullPointerException e){
            CrashLogHandler.logSevere("Goal not found in level", e.getMessage());
        }
        entities.add(goal);
        entities.addAll(enemies);
        entities.addAll(boundaries);
        return new LevelRunner(enemies, boundaries, entities, conditionalBoundaries, player,
                backgroundTiles, goal, createHealthDisplays(), level);
    }

    public LevelRunner[] getLevels() {
        return levels;
    }

    public Texture getPlayerTexture() {
        return playerTexture;
    }

    public Texture getShootingEnemyTexture() {
        return shootingEnemyTexture;
    }

    public Texture getTrackingEnemyTexture() {
        return trackingEnemyTexture;
    }
}
