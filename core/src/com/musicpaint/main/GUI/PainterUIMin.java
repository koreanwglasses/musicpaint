package com.musicpaint.main.GUI;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.musicpaint.main.Painter;
import com.musicpaint.main.SignalColor.PastelColorScheme;
import com.musicpaint.main.SignalColor.SignalColorScheme;
import com.musicpaint.main.SignalColor.VibrantColorScheme;

/**
 * Created by fchoi on 2/23/2016.
 */
public class PainterUIMin extends Screen2 implements InputProcessor {
    MusicPaintHelp helpScreen;

    Painter painter;
    PainterTouchUI touch;
    SignalColorScheme[] colorSchemes = new SignalColorScheme[] {new PastelColorScheme(), new VibrantColorScheme()}; // 0th is default

    boolean noSlowModeFlag;
    boolean forceVoronoi;

    Stage stage;
    Viewport viewport;

    Table screenTable;

    SolidDrawable background;
    SolidDrawable shadowBackground;
    Table tabsTable;
    Table openTab;

    MinUIImageButton btnPause;
    MinUIImageButton soundControlTab;
    MinUIImageButton resetTab;
    MinUIImageButton optionsTab;

    Table resetTable;
    Label lblMode;
    TextButton btnMode;
    Label lblReset;
    TextButton btnReset;

    Label lblFps;

    Table soundControlTable;
    Label lblThreshold;
    Slider hsbThreshold;
    Label lblMax;
    Slider hsbMax;
    Label lblAmplitude;
    ProgressBar barAmplitude;

    Table optionsTable;
    Label lblVoronoi;
    CheckBox chkVoronoi;
    Label lblFreeze;
    CheckBox chkFreeze;
    TextButton btnHelp;

    Table infoTable;
    Label lblInfo;
    CheckBox chkInfo;
    boolean showInfo;

    public boolean freezeUiColor;

    public PainterUIMin(Game game) {
        super(game);

        painter = new Painter();

        touch = new PainterTouchUI(painter);
    }

    public void show() {
        helpScreen = new MusicPaintHelp(game, this);
        if(false && Assets.prefs.getBoolean("first_run", true)) {
            game.setScreen(helpScreen);

            Assets.prefs.putBoolean("first_run", false);
            Assets.prefs.flush();
        }

        painter.show();
        painter.setColorScheme(colorSchemes[0]);
        forceVoronoi = false;

        stage = new Stage();

        freezeUiColor = false;

        InputMultiplexer inputMulti = new InputMultiplexer();
        inputMulti.addProcessor(this);
        inputMulti.addProcessor(stage);
        inputMulti.addProcessor(new GestureDetector(touch));

        Gdx.input.setInputProcessor(inputMulti);

        if (Assets.guiSizeClass == Assets.SIZE_COMPACT)
            viewport = new ExtendViewport(450, 450);
        else
            viewport = new ScreenViewport();
        stage.setViewport(viewport);

        screenTable = new Table();
        screenTable.setFillParent(true);
        stage.addActor(screenTable);

        background = new SolidDrawable();
        background.color = Color.LIGHT_GRAY.cpy(); // Starting UI Color
        shadowBackground = new SolidDrawable();
        shadowBackground.color = background.color.cpy();

        tabsTable = new Table();
        tabsTable.setBackground(background.getInstance());
        openTab = new Table();
        openTab.setBackground(shadowBackground.getInstance());

        resetTab = new MinUIImageButton(Assets.resetTextureDrawable);
        resetTab.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                UI_openCloseTab(TAB_RESET);
            }
        });

        soundControlTab = new MinUIImageButton(Assets.controlTextureDrawable);
        soundControlTab.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                UI_openCloseTab(TAB_CONTROL);
            }
        });

        optionsTab = new MinUIImageButton(Assets.optionsTextureDrawable);
        optionsTab.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                UI_openCloseTab(TAB_OPTIONS);
            }
        });

        resetTable = new Table();

        lblMode = new Label("Color Scheme:", Assets.transparentSkin);
        btnMode = new TextButton(painter.getColorScheme().getName(), Assets.transparentSkin);
        btnMode.getLabel().setWrap(false);
        btnMode.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if(showInfo)
                    lblInfo.setText("This is the current color scheme. Tap to change, then tap \"Reset.\"");
                else
                    UI_nextMode();
            }
        });

        lblReset = new Label("", Assets.transparentSkin);
        btnReset = new TextButton("Reset", Assets.transparentSkin);
        btnReset.getLabel().setWrap(false);
        btnReset.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if(showInfo)
                    lblInfo.setText("Tap to clear the canvas.");
                else {
                    painter.reset();
                    if (nextModeFlag)
                        changeMode();
                }
            }
        });

        btnPause = new MinUIImageButton(Assets.playTextureDrawable);
        btnPause.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                UI_pause();
            }
        });

        lblFps = new Label("0", Assets.transparentSkin);

        soundControlTable = new Table();

        lblAmplitude = new Label("Amplitude", Assets.transparentSkin);
        // TODO: Make a min / max / amplitude bar
        // TODO: Clean up this code!
        Skin skin = new Skin();
        Pixmap pixmap = new Pixmap(1, 20, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.WHITE);
        pixmap.fill();
        skin.add("Amplitude_Progress_Bar", new Texture(pixmap));
        ProgressBar.ProgressBarStyle barStyle = new ProgressBar.ProgressBarStyle(skin.newDrawable("Amplitude_Progress_Bar", new Color(0,0,0,.1f)), skin.newDrawable("Amplitude_Progress_Bar", new Color(0,0,0,.6f)));
        barStyle.knobBefore = barStyle.knob;
        barAmplitude = new ProgressBar(0, 1, .001f, false, barStyle);
        barAmplitude.setAnimateDuration(.3f);
        barAmplitude.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if(showInfo)
                    lblInfo.setText("Amplitude of sound input");
            }
        });

        lblThreshold = new Label("Threshold", Assets.transparentSkin);
        hsbThreshold = new Slider(0, 1, .001f, false, Assets.transparentSkin);
        hsbThreshold.setValue((float) painter.signalColor.getThreshold());
        hsbThreshold.addListener(new ClickListener() {
            public void touchDragged(InputEvent event, float x, float y,
                                     int pointer) {
                if(showInfo)
                    lblInfo.setText("Drag to adjust the threshold. When the amplitude falls below the threshold, nothing is drawn.");

                UI_updateSignalColor();
            }

            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                if(showInfo)
                    lblInfo.setText("Drag to adjust the threshold. When the amplitude falls below the threshold, nothing is drawn.");

                UI_updateSignalColor();
            }
        });

        lblMax = new Label("Input Level", Assets.transparentSkin);
        hsbMax = new Slider(0, 1, .001f, false, Assets.transparentSkin);
        hsbMax.setValue((float) painter.signalColor.getLevel());
        hsbMax.addListener(new ClickListener() {
            public void touchDragged(InputEvent event, float x, float y,
                                     int pointer) {
                if(showInfo)
                    lblInfo.setText("Drag to adjust level. Lower levels result in more opaque colors.");

                UI_updateSignalColor();
            }

            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                if(showInfo)
                    lblInfo.setText("Drag to adjust level. Lower levels result in more opaque colors.");

                UI_updateSignalColor();
            }
        });

        optionsTable = new Table();

        lblVoronoi = new Label("Force Voronoi", Assets.transparentSkin);
        chkVoronoi = new CheckBox("", Assets.transparentSkin);
        chkVoronoi.setChecked(forceVoronoi);
        chkVoronoi.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if(showInfo)
                    lblInfo.setText("Forces voronoi rendering when device switched to pixel mode.");

                forceVoronoi = chkVoronoi.isChecked();
            }
        });

        lblFreeze = new Label("Freeze UI Color", Assets.transparentSkin);
        chkFreeze = new CheckBox("", Assets.transparentSkin);
        chkFreeze.setChecked(freezeUiColor);
        chkFreeze.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if(showInfo)
                    lblInfo.setText("Freeze the background color of the interface.");

                freezeUiColor = chkFreeze.isChecked();
            }
        });

        btnHelp = new TextButton("Help", Assets.transparentSkin);
        btnHelp.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // game.setScreen(helpScreen);

                chkInfo.setChecked(!chkInfo.isChecked());
                showInfo = chkInfo.isChecked();
                infoTable.setVisible(showInfo);
                lblInfo.setText("Tap a button to see what it does. Tap \"i\" when done.");
            }
        });

        infoTable = new Table();
        infoTable.setBackground(shadowBackground.getInstance());
        infoTable.setVisible(false);

        lblInfo = new Label("", Assets.transparentSkin);
        lblInfo.setWrap(true);
        lblInfo.setWidth(380);
        lblInfo.setAlignment(Align.center);

        CheckBox.CheckBoxStyle chkInfoStyle = new CheckBox.CheckBoxStyle(Assets.infoTextureDrawable, Assets.infoCheckedTextureDrawable, Assets.labelFont, Color.WHITE);
        chkInfo = new CheckBox("", chkInfoStyle);
        chkInfo.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                showInfo = chkInfo.isChecked();
                infoTable.setVisible(showInfo);
                lblInfo.setText("Tap a button to see what it does. Tap \"i\" when done.");
            }
        });

        infoTable.add(lblInfo).pad(10).width(380);

        if(Assets.guiSizeClass == Assets.SIZE_COMPACT) {
            resetTable.add(lblMode);
            resetTable.row();
            resetTable.add(btnMode).height(55).width(200).pad(5);
            resetTable.row();
            resetTable.add(btnReset).colspan(2).height(55).width(200).pad(5);
            resetTable.row();
            resetTable.add(lblReset);

            soundControlTable.add(lblThreshold);
            soundControlTable.add(hsbThreshold).height(30).width(250).pad(5);
            soundControlTable.row();
            soundControlTable.add(lblMax);
            soundControlTable.add(hsbMax).height(30).width(250).pad(5);
            soundControlTable.row();
            soundControlTable.add(lblAmplitude);
            soundControlTable.add(barAmplitude).height(30).width(250).pad(5);

            optionsTable.add(lblVoronoi);
            optionsTable.add(chkVoronoi).width(20).height(55).pad(5);
            optionsTable.row();
            optionsTable.add(lblFreeze);
            optionsTable.add(chkFreeze).width(20).height(55).pad(5);
            optionsTable.row();
            optionsTable.add(btnHelp).width(200).height(55).pad(5).colspan(2);

            screenTable.add(infoTable).expand().width(400).bottom().padBottom(10);
            screenTable.row();
            screenTable.add(openTab).fillX().bottom();
            screenTable.row();
            screenTable.add(tabsTable).fillX().bottom();
            tabsTable.add(btnPause).width(112.5f).height(55).center();
            tabsTable.add(resetTab).width(112.5f).height(55).center();
            tabsTable.add(soundControlTab).width(112.5f).height(55).center();
            tabsTable.add(optionsTab).width(112.5f).height(55).center();
        } else {
            resetTable.add(lblMode);
            resetTable.row();
            resetTable.add(btnMode).height(40).width(150).pad(5);
            resetTable.row();
            resetTable.add(btnReset).height(40).width(150).pad(5);
            resetTable.row();
            resetTable.add(lblReset);

            soundControlTable.add(lblThreshold);
            soundControlTable.add(hsbThreshold).height(30).width(300).pad(5);
            soundControlTable.row();
            soundControlTable.add(lblMax);
            soundControlTable.add(hsbMax).height(30).width(300).pad(5);
            soundControlTable.row();
            soundControlTable.add(lblAmplitude);
            soundControlTable.add(barAmplitude).height(30).width(300).pad(5);

            optionsTable.add(lblVoronoi);
            optionsTable.add(chkVoronoi).width(20).height(40).pad(5);
            optionsTable.row();
            optionsTable.add(lblFreeze);
            optionsTable.add(chkFreeze).width(20).height(40).pad(5);
            optionsTable.row();
            optionsTable.add(btnHelp).width(150).height(40).pad(5).colspan(2);

            screenTable.add(infoTable).expand().width(400).bottom().padBottom(10);
            screenTable.row();
            screenTable.add(openTab).width(450).bottom();
            screenTable.row();
            screenTable.add(tabsTable).width(450).bottom();
            tabsTable.add(btnPause).width(112.5f).height(40).center();
            tabsTable.add(resetTab).width(112.5f).height(40).center();
            tabsTable.add(soundControlTab).width(112.5f).height(40).center();
            tabsTable.add(optionsTab).width(112.5f).height(40).center();
        }

        noSlowModeFlag = true;
    }

    @Override
    public void base_render(float delta) {
        if (delta > 1 / 5f && !noSlowModeFlag) painter.setSlowMode(true);
        if(forceVoronoi) painter.setSlowMode(false);
        noSlowModeFlag = false;

        painter.render(delta);

        barAmplitude.setValue((float) painter.getCurrentAmplitude());
        lblFps.setText(String.valueOf((int) (1 / delta)));

        if(!painter.isPaused() && painter.getCurrentColor().a > .7 && !freezeUiColor) {
            background.color.lerp(painter.getCurrentColor(), delta).a = 1;
            shadowBackground.color = background.color.cpy().lerp(Color.BLACK, 0.1f);
        }

        stage.act(delta);
        stage.draw();
    }

    public void resize(int width, int height) {
        painter.resize(width, height);
        stage.getViewport().update(width, height, true);

        noSlowModeFlag = true;
    }

    @Override
    public void pause() {
        painter.pause();

        if (Assets.guiSizeClass == Assets.SIZE_COMPACT) {
            painter.stop();
            btnPause.setImage(Assets.playTextureDrawable);
        }

        noSlowModeFlag = true;
    }

    @Override
    public void resume() {
        painter.pause();
    }

    @Override
    public void hide() {
        painter.hide();
    }

    @Override
    public void dispose() {
        painter.dispose();
    }

    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        switch (keycode) {
            case Input.Keys.R:
                painter.reset();
                break;
            case Input.Keys.SPACE:
                UI_pause();
                break;
            default:
                return false;
        }return true;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        Vector2 stageCoords = stage.screenToStageCoordinates(new Vector2(screenX, screenY));
        Rectangle openTabBounds = new Rectangle(openTab.getX(), openTab.getY(), openTab.getWidth(), openTab.getHeight());
        Rectangle tabsTableBounds = new Rectangle(tabsTable.getX(), tabsTable.getY(), tabsTable.getWidth(), tabsTable.getHeight());
        if(!openTabBounds.contains(stageCoords) && !tabsTableBounds.contains(stageCoords))
            UI_openCloseTab(0);
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        painter.setScale(painter.getScale() * (float) Math.pow(1.25, amount));
        return true;
    }

    public void UI_pause() {
        if (painter.isPaused()) {
            painter.start();
            btnPause.setImage(Assets.pauseTextureDrawable);
        } else {;
            painter.stop();
            btnPause.setImage(Assets.playTextureDrawable);
            noSlowModeFlag = true;
        }
    }

    private void UI_updateSignalColor() {
        if (hsbThreshold.getValue() >= hsbMax.getValue())
            hsbMax.setValue(hsbThreshold.getValue() + hsbMax.getStepSize());

        painter.signalColor.setThreshold(hsbThreshold.getValue());
        painter.signalColor.setLevel(hsbMax.getValue());
    }

    private int currentModeIndex = 0;
    private int nextModeIndex = 0;
    private boolean nextModeFlag = false;
    void UI_nextMode() {
        nextModeIndex = (nextModeIndex + 1) % colorSchemes.length;
        btnMode.setText(colorSchemes[nextModeIndex].getName());

        if(nextModeIndex == currentModeIndex) {
            lblReset.setText("");
            nextModeFlag = false;
        }
        else {
            lblReset.setText("Reset to apply changes");
            nextModeFlag = true;
        }
    }

    private void changeMode() {
        currentModeIndex = nextModeIndex;
        painter.setColorScheme(colorSchemes[currentModeIndex]);
        lblReset.setText("");
        nextModeFlag = false;
    }

    static final int TAB_NONE = 0;
    static final int TAB_CONTROL = 2;
    static final int TAB_RESET = 1;
    static final int TAB_OPTIONS = 3;

    int currentTab = 0;
    void UI_openCloseTab(int tab) {
        openTab.clearChildren();
        if(tab == currentTab)
            currentTab = 0;
        else {
            switch (tab) {
                case TAB_RESET:
                    if(Assets.guiSizeClass == Assets.SIZE_COMPACT)
                        openTab.add(resetTable).expand().fill().height(250).padLeft(40);
                    else
                        openTab.add(resetTable).expand().fill().height(180).padLeft(40);
                    break;
                case TAB_CONTROL:
                    if(Assets.guiSizeClass == Assets.SIZE_COMPACT)
                        openTab.add(soundControlTable).expand().fill().height(180).padLeft(40);
                    else
                        openTab.add(soundControlTable).expand().fill().height(160).padLeft(40);
                    break;
                case TAB_OPTIONS:
                    if(Assets.guiSizeClass == Assets.SIZE_COMPACT)
                        openTab.add(optionsTable).expand().fill().height(240).padLeft(40);
                    else
                        openTab.add(optionsTable).expand().fill().height(180).padLeft(40);
                    break;
                case TAB_NONE:
                    chkInfo.setChecked(false);
                    infoTable.setVisible(false);
                    showInfo = false;
                    break;
            }

            if(tab != 0)
                openTab.add(chkInfo).expandY().bottom().width(40).height(40);

            currentTab = tab;
        }

    }
}
