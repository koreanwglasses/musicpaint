package com.musicpaint.main;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.musicpaint.main.GUI.Assets;
import com.musicpaint.main.GUI.LoadScreen;
import com.musicpaint.main.GUI.PainterUIMin;

public class Main extends Game {

	@Override
	public void create () {
		setScreen(new LoadScreen(this));
	}
}
