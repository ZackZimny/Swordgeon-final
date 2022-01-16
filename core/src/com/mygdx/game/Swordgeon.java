package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.game.GameHelpers.AssetManagerHandler;
import com.mygdx.game.GameHelpers.CrashLogHandler;
import com.mygdx.game.GameHelpers.EventLogHandler;
import com.mygdx.game.Screens.ScreenHandler;

public class Swordgeon extends ApplicationAdapter {
	Texture loading;
	SpriteBatch spriteBatch;
	ScreenHandler screenHandler;
	//Runs before the game is created.
	//Try catch blocks added for crash handling
	@Override
	public void create () {
		try {
			CrashLogHandler.start();
			EventLogHandler.start();
			AssetManagerHandler.load();
			loading = new Texture(Gdx.files.internal("Loading.png"));
			spriteBatch = new SpriteBatch();
		} catch (Exception e){
			CrashLogHandler.logSevere("There was an error during the loading process. ", e.getMessage());
		}
	}

	//Updates every frame
	@Override
	public void render () {
		//displays loading screen while assetManager loads the assets
		try {
			if (AssetManagerHandler.assetManager.update()) {
				if (screenHandler == null)
					screenHandler = new ScreenHandler();
				screenHandler.render();
			} else {
				spriteBatch.begin();
				spriteBatch.draw(loading, 0, 0);
				spriteBatch.end();
			}
		} catch (Exception e){
			CrashLogHandler.logSevere("There was an error running the game. ", e.getMessage());
		}
	}
}
