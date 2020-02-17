package fi.utu.tech.distributed.gorilla.views;

import fi.utu.tech.distributed.gorilla.logic.GameMode;
import fi.utu.tech.distributed.gorilla.logic.GameState;

import java.util.Map;

public class Views {
    private final MainCanvas mainCanvas;
    private final IntroCanvas introCanvas;
    private final GameCanvas gameCanvas;
    private final MenuCanvas menuCanvas;
    private final CanvasSwitcher<GameMode> switcher;

    public Views(MainCanvas main, boolean lowendMachine, boolean synkistely, double tickDuration, long seed) {
        this.mainCanvas = main;
        introCanvas = new IntroCanvas(mainCanvas, lowendMachine, synkistely, seed);
        gameCanvas = new GameCanvas(tickDuration, mainCanvas, null, lowendMachine, seed);
        menuCanvas = new MenuCanvas(mainCanvas, lowendMachine, seed, "Gorillasota 2029", new String[]{});

        switcher = new CanvasSwitcher<>(mainCanvas, Map.of(
                GameMode.Intro, introCanvas,
                GameMode.Game, gameCanvas,
                GameMode.Menu, menuCanvas
        ));
        setMode(GameMode.Intro);
    }

    public void setMode(GameMode mode) {
        switch (mode) {
            case Intro:
                introCanvas.init();
                break;
        }
        switcher.switchView(mode);
    }

    public void setSelectedMenuItem(int selectedMenuItem) {
        menuCanvas.setSelected(selectedMenuItem);
    }

    public void setMenu(String title, String[] items) {
        menuCanvas.setMenu(title, items);
    }

    public void setMenuInfo(String[] items) {
        menuCanvas.setInfo(items);
    }

    public void setGameState(GameState gameState) {
        gameCanvas.setGameState(gameState);
    }

    public boolean introDone() {
        return introCanvas.done();
    }

    public void redraw() {
        switcher.redraw();
    }

    public void setVelocity(int v) {
        gameCanvas.setVelocity(v);
    }

    public void addVelocity(int v) {
        gameCanvas.addVelocity(v);
    }

    public void focusOnMe() {
        gameCanvas.focusOnMe();
    }
}