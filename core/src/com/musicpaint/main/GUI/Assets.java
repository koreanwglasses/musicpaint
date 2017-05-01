package com.musicpaint.main.GUI;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.TextureLoader;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

/**
 * Created by fchoi on 2/18/2016.
 */
public class Assets {
    public static AssetManager manager;
    public static Preferences prefs;

    static final int SIZE_COMPACT = 0;
    static final int SIZE_FULL = 1;

    public static int guiSizeClass;

    public static Skin transparentSkin;

    public static TextureRegionDrawable pauseTextureDrawable;
    public static TextureRegionDrawable playTextureDrawable;
    public static TextureRegionDrawable resetTextureDrawable;
    public static TextureRegionDrawable controlTextureDrawable;
    public static TextureRegionDrawable optionsTextureDrawable;
    public static TextureRegionDrawable infoTextureDrawable;
    public static TextureRegionDrawable infoCheckedTextureDrawable;

    public static BitmapFont labelFont;

    public static void load() {
        prefs = Gdx.app.getPreferences("music_paint_prefs"); // Load prefs

        switch (Gdx.app.getType()) {
            case Android:
                if(getScreenSizeInches() > 7)
                    guiSizeClass = SIZE_FULL;
                else
                    guiSizeClass = SIZE_COMPACT;
                break;
            case Applet:
                guiSizeClass = SIZE_FULL;
                break;
            case Desktop:
                guiSizeClass = SIZE_FULL;
                break;
            case HeadlessDesktop:
                break;
            case WebGL:
                guiSizeClass = SIZE_FULL;
                break;
            case iOS:
                guiSizeClass = SIZE_COMPACT;
                break;
            default:
                guiSizeClass = SIZE_FULL;
                break;
        }

//        guiSizeClass = SIZE_COMPACT;

        manager = new AssetManager();
        manager.load("GUI/Transparent Skin/uiskin.atlas", TextureAtlas.class);

        TextureLoader.TextureParameter textureParameter = new TextureLoader.TextureParameter();
        textureParameter.minFilter = Texture.TextureFilter.MipMapLinearLinear;
        textureParameter.genMipMaps = true;
        manager.load("GUI/pause.png", Texture.class, textureParameter);
        manager.load("GUI/play.png", Texture.class, textureParameter);
        manager.load("GUI/reset.png", Texture.class, textureParameter);
        manager.load("GUI/control.png", Texture.class, textureParameter);
        manager.load("GUI/options.png", Texture.class, textureParameter);
        manager.load("GUI/info.png", Texture.class, textureParameter);
        manager.load("GUI/infochecked.png", Texture.class, textureParameter);
    }

    public static void initialize() {

        FreeTypeFontGenerator.FreeTypeFontParameter params = new FreeTypeFontGenerator.FreeTypeFontParameter();
        params.genMipMaps = true;
        params.kerning = true;
        params.magFilter = Texture.TextureFilter.Linear;
        params.minFilter = Texture.TextureFilter.MipMapLinearLinear;

        FreeTypeFontGenerator regfont = new FreeTypeFontGenerator(Gdx.files.internal("GUI/fonts/Ubuntu-RI.ttf"));
        if(guiSizeClass == SIZE_COMPACT) {
            params.size = 28;
            params.size *= 5;
        } else {
            params.size = 20;
        }
        BitmapFont defaultFont = regfont.generateFont(params);
        regfont.dispose();
        if(guiSizeClass == SIZE_COMPACT)
            defaultFont.getData().setScale(.2f);

        FreeTypeFontGenerator lblfont = new FreeTypeFontGenerator(Gdx.files.internal("GUI/fonts/Ubuntu-LI.ttf"));
        if(guiSizeClass == SIZE_COMPACT) {
            params.size = 20;
            params.size *= 5;
        } else {
            params.size = 15;
        }

        labelFont = lblfont.generateFont(params);
        lblfont.dispose();
        if(guiSizeClass == SIZE_COMPACT)
            labelFont.getData().setScale(.2f);

        transparentSkin = new Skin();
        transparentSkin.addRegions(manager.get("GUI/Transparent Skin/uiskin.atlas", TextureAtlas.class));
        transparentSkin.add("default-font", defaultFont);
        transparentSkin.add("italic-font", labelFont);
        transparentSkin.load(Gdx.files.internal("GUI/Transparent Skin/uiskin.json"));

        pauseTextureDrawable = new TextureRegionDrawable(new TextureRegion(manager.get("GUI/pause.png", Texture.class)));
        playTextureDrawable = new TextureRegionDrawable(new TextureRegion(manager.get("GUI/play.png", Texture.class)));
        resetTextureDrawable = new TextureRegionDrawable(new TextureRegion(manager.get("GUI/reset.png", Texture.class)));
        controlTextureDrawable = new TextureRegionDrawable(new TextureRegion(manager.get("GUI/control.png", Texture.class)));
        optionsTextureDrawable = new TextureRegionDrawable(new TextureRegion(manager.get("GUI/options.png", Texture.class)));
        infoTextureDrawable = new TextureRegionDrawable(new TextureRegion(manager.get("GUI/info.png", Texture.class)));
        infoCheckedTextureDrawable = new TextureRegionDrawable(new TextureRegion(manager.get("GUI/infochecked.png", Texture.class)));
    }

    public static double getScreenSizeInches () {
        //According to LibGDX documentation; getDensity() returns a scalar value for 160dpi.
        float dpi = 160 * Gdx.graphics.getDensity();
        float widthInches = Gdx.graphics.getWidth() / dpi;
        float heightInches = Gdx.graphics.getHeight() / dpi;

        //Use the pythagorean theorem to get the diagonal screen size
        return Math.sqrt(Math.pow(widthInches, 2) + Math.pow(heightInches, 2));
    }
}
