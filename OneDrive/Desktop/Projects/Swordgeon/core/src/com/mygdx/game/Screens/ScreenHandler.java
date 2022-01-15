package com.mygdx.game.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.mygdx.game.Entities.Entity;
import com.mygdx.game.GameHelpers.*;

public class ScreenHandler {
    public enum ScreenType {
        MENU,
        TUTORIAL,
        TUTORIAL_CONTROLS,
        TUTORIAL_BOUNDARY,
        TUTORIAL_SETTING,
        LEVEL_SELECT,
        GAME,
        PLAYER_WON,
        PLAYER_DIED,
        RECORD_SCREEN,
        OPTIONS
    }
    private final SpriteBatch spriteBatch = new SpriteBatch();
    private final ShapeRenderer shapeRenderer = new ShapeRenderer();
    private ScreenType currScreen = ScreenType.MENU;
    private int currLevel = -1;
    private final MenuScreen menuScreen = new MenuScreen();
    private final TutorialIntroScreen tutorialIntroScreen;
    private final TutorialOverlayScreen controlsScreen = new TutorialOverlayScreen("ControlsScreen.png");
    private final TutorialOverlayScreen boundaryScreen = new TutorialOverlayScreen("BoundaryTutorial.png");
    private final TutorialOverlayScreen settingTutorialScreen = new TutorialOverlayScreen("SettingsTutorial.png");
    private final LevelSelectScreen levelSelectScreen = new LevelSelectScreen();
    private final MenuBrowserScreen playerWonScreen = new MenuBrowserScreen("You Survived! ", "Next Level",
            Color.CYAN);
    private final OptionsScreen optionsScreen;
    private final MenuBrowserScreen playerDiedScreen = new MenuBrowserScreen("You died...", "Retry", Color.RED);
    private final LevelLoader levelLoader = new LevelLoader();
    private RecordScreen recordScreen;
    private LevelRunner levelRunner;

    /**
     * Deals with screen-to-screen communication
     */
    public ScreenHandler(){
        DatabaseManager.createDatabase();
        DatabaseManager.createTables();
        DatabaseManager.loadRuntimeConfigData();
        System.out.println(RuntimeConfigs.getMusicVolume());
        GameSounds.load();
        optionsScreen = new OptionsScreen();
        levelLoader.load();
        TextureRegion playerTexture = Entity.createAnimationsArrayList(6, 4,
                levelLoader.getPlayerTexture(),
                0.1f).get(1).getKeyFrame(0f);
        TextureRegion trackingEnemyTexture = Entity.createAnimationsArrayList(1, 2,
                levelLoader.getTrackingEnemyTexture(), 0.1f).get(0).getKeyFrame(0f);
        TextureRegion shootingEnemyTexture = Entity.createAnimationsArrayList(1, 2,
                levelLoader.getShootingEnemyTexture(), 0.1f).get(0).getKeyFrame(0);
        tutorialIntroScreen = new TutorialIntroScreen(playerTexture, shootingEnemyTexture, trackingEnemyTexture);
    }

    /**
     * renders the screen based on how the user has navigated them
     * see the screen flow document in the documentation folder
     */
    public void render(){
        GameSounds.playMusic();
        shapeRenderer.setAutoShapeType(true);
        switch(currScreen) {
            case MENU:
                menuScreen.render(shapeRenderer, spriteBatch);
                if (menuScreen.getPlayButton().isClicked()) {
                    EventLogHandler.log("Navigated to level select");
                    currScreen = ScreenType.LEVEL_SELECT;
                } else if (menuScreen.getTutorialButton().isClicked()) {
                    EventLogHandler.log("Navigated to tutorial");
                    currScreen = ScreenType.TUTORIAL;
                } else if (menuScreen.getOptionsButton().isClicked()) {
                    EventLogHandler.log("Navigated to options");
                    currScreen = ScreenType.OPTIONS;
                }
                break;
            case OPTIONS:
                optionsScreen.render(shapeRenderer, spriteBatch);
                if (optionsScreen.getBackButton().isClicked()) {
                    EventLogHandler.log("Navigated to menu");
                    currScreen = ScreenType.MENU;
                }
                break;
            case TUTORIAL:
                tutorialIntroScreen.render(shapeRenderer, spriteBatch);
                if (tutorialIntroScreen.getBackButton().isClicked()) {
                    EventLogHandler.log("Navigated to menu");
                    currScreen = ScreenType.MENU;
                }
                if (tutorialIntroScreen.getNextButton().isClicked()) {
                    currScreen = ScreenType.TUTORIAL_CONTROLS;
                    EventLogHandler.log("Navigated to tutorial controls");
                }
                break;
            case TUTORIAL_CONTROLS:
                controlsScreen.render(shapeRenderer, spriteBatch);
                if (controlsScreen.getBackButton().isClicked()) {
                    EventLogHandler.log("Navigated to tutorial");
                    currScreen = ScreenType.TUTORIAL;
                } else if (controlsScreen.getNextButton().isClicked()) {
                    EventLogHandler.log("Navigated to boundary tutorial");
                    currScreen = ScreenType.TUTORIAL_BOUNDARY;
                }
                break;
            case TUTORIAL_BOUNDARY:
                boundaryScreen.render(shapeRenderer, spriteBatch);
                if(boundaryScreen.getBackButton().isClicked()){
                    EventLogHandler.log("Navigated to tutorial controls");
                    currScreen = ScreenType.TUTORIAL_CONTROLS;
                } else if(boundaryScreen.getNextButton().isClicked()) {
                    EventLogHandler.log("Navigated to settings tutorial");
                    currScreen = ScreenType.TUTORIAL_SETTING;
                }
                break;
            case TUTORIAL_SETTING:
                settingTutorialScreen.render(shapeRenderer, spriteBatch);
                if(settingTutorialScreen.getBackButton().isClicked()){
                    EventLogHandler.log("Navigated to boundary tutorial");
                    currScreen = ScreenType.TUTORIAL_BOUNDARY;
                } else if(settingTutorialScreen.getNextButton().isClicked()){
                    EventLogHandler.log("Navigated to menu");
                    currScreen = ScreenType.MENU;
                }
                break;
            case LEVEL_SELECT:
                levelSelectScreen.render(shapeRenderer, spriteBatch);
                if (levelSelectScreen.getBackButton().isClicked()) {
                    EventLogHandler.log("Navigated to menu");
                    currScreen = ScreenType.MENU;
                }
                Button[] levelButtons = levelSelectScreen.getButtons();
                for (int i = 0; i < levelButtons.length; i++) {
                    if (levelButtons[i].isClicked()) {
                        currScreen = ScreenType.GAME;
                        EventLogHandler.log("Navigated to game");
                        currLevel = i + 1;
                        levelRunner = levelLoader.getLevels()[currLevel - 1];
                    }
                }
                Button[] recordButtons = levelSelectScreen.getRecordButtons();
                for (int i = 0; i < recordButtons.length; i++) {
                    if (recordButtons[i].isClicked()) {
                        EventLogHandler.log("Navigated to record screen " + (i + 1));
                        currScreen = ScreenType.RECORD_SCREEN;
                        Record[] data = DatabaseManager.getTopLevelData(i + 1, 5);
                        recordScreen = new RecordScreen(data, i + 1);
                        break;
                    }
                }
                break;
            case RECORD_SCREEN:
                recordScreen.render(shapeRenderer, spriteBatch);
                if (recordScreen.getBackButton().isClicked()) {
                    currScreen = ScreenType.LEVEL_SELECT;
                    EventLogHandler.log("Navigated to level select");
                }
                break;
            case GAME:
                levelRunner.run();
                if (levelRunner.isPlayerDead()) {
                    currScreen = ScreenType.PLAYER_DIED;
                    EventLogHandler.log("Player has entered the lose screen");
                } else if (levelRunner.playerHasWon()) {
                    currScreen = ScreenType.PLAYER_WON;
                    DatabaseManager.insert(new Record(levelRunner.getTime(), RuntimeConfigs.getName()), currLevel);
                    currLevel += 1;
                    int LEVEL_COUNT = 4;
                    EventLogHandler.log("Player has entered the win screen");
                    if (currLevel > LEVEL_COUNT){
                        currLevel = 1;
                    }
                }
                break;
            case PLAYER_WON:
            case PLAYER_DIED:
                Gdx.gl.glClearColor(0f, 0f, 0f,1f);
                Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
                boolean playerWon = currScreen == ScreenType.PLAYER_WON;
                MenuBrowserScreen currBrowser = playerWon ? playerWonScreen : playerDiedScreen;
                currBrowser.render(shapeRenderer, spriteBatch);
                if(currBrowser.getActionButton().isClicked()){
                    currScreen = ScreenType.GAME;
                    levelRunner = levelLoader.loadLevel(currLevel);
                } else if(currBrowser.getBackToLevelSelectButton().isClicked()){
                    currScreen = ScreenType.LEVEL_SELECT;
                } else if(currBrowser.getBackToTitleScreenButton().isClicked()){
                    currScreen = ScreenType.MENU;
                }
                break;
        }
    }
}
