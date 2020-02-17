package fi.utu.tech.distributed.gorilla.views;

import fi.utu.tech.distributed.gorilla.engine.ProxyGameObject;
import fi.utu.tech.distributed.gorilla.engine.Rect;
import fi.utu.tech.distributed.gorilla.logic.GameState;
import fi.utu.tech.distributed.gorilla.logic.Player;
import fi.utu.tech.distributed.gorilla.views.layers.Parallax;
import fi.utu.tech.distributed.gorilla.views.objects.ObjectView;
import fi.utu.tech.oomkit.app.Scheduled;
import fi.utu.tech.oomkit.canvas.Canvas;
import fi.utu.tech.oomkit.canvas.Point2D;
import fi.utu.tech.oomkit.colors.CoreColor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.function.Consumer;

public class GameCanvas extends ProxyCanvas implements Scheduled {
    private final boolean lowendMachine;
    private final Parallax layer2;
    private final Parallax layer3;
    private final Point2D topLeft = new Point2D(0, 0);
    private final double gameTickDuration;

    private GameState gameState;
    private final Rect view = new Rect(new Point2D(), new Point2D());
    private int viewVelocity = 0;

    public GameCanvas(double tickDuration, Canvas main, GameState gameState, boolean lowendMachine, long seed) {
        super(main);
        this.lowendMachine = lowendMachine;
        this.gameTickDuration = tickDuration;
        Random generator = new Random(seed);
        layer2 = new Parallax(main, 0.6, false, generator.nextLong());
        layer3 = new Parallax(main, 0.8, false, generator.nextLong());
        setGameState(gameState);
    }

    public void setVelocity(int v) {
        viewVelocity = v;
    }

    public void addVelocity(int v) {
        viewVelocity += v;
    }

    public void focusOnMe() {
        if (gameState == null) return;
        viewVelocity = 0;
        double mx = (view.topLeft.x + view.bottomRight.x) / 2;
        double gx = gameState.getLocalPlayer().getLaunchPosition().x;
        double delta = gx-mx;
        view.topLeft.add(delta,0);
        view.bottomRight.add(delta,0);
    }

    @Override
    protected void resized() {
        super.resized();
        setGameState(gameState);
    }

    public void setGameState(GameState gameState) {
        if (gameState == null) return;

        this.gameState = gameState;
        updateContent();
        double sceneHeight = gameState.configuration.gameWorldHeight;
        view.topLeft.set(0, sceneHeight - getHeight());
        view.bottomRight.set(getWidth(), sceneHeight);
        viewVelocity = 0;
    }

    @Override
    public void updateContent() {
        if (viewVelocity != 0) {
            view.topLeft.add(viewVelocity, 0);
            view.bottomRight.add(viewVelocity, 0);
        }
        layer2.update(viewVelocity / 2.0);
        layer3.update(viewVelocity / 4.0);
    }

    private final Point2D tmp = new Point2D();

    private String renderTime(double seconds) {
        return (int) (seconds) + " millisekuntia";
    }

    private String renderGameStatus() {
        int aliveCount = 0;
        for (Player p : gameState.getPlayers())
            if (p.alive) aliveCount++;

        return aliveCount + " / " + gameState.getPlayers().size() + " gorillaa elossa.";
    }

    private String renderWindStatus() {
        return "Tuuli: " + gameState.getWindSpeed() + (gameState.getWindSpeed() > 0 ? " yks. oikealle" : " yks. vasemmalle");
    }

    @Override
    public void drawBackgroundContent() {
        drawRectangle(topLeft, dimensions, CoreColor.Blue, true);
    }

    private class ObjectListHandler implements Consumer<ProxyGameObject> {
        ArrayList<ObjectView> list = new ArrayList<>();

        @Override
        public void accept(ProxyGameObject g) {
            if (g instanceof ObjectView) list.add((ObjectView) g);
        }

        void draw() {
            Collections.sort(objectListHandler.list);

            for (ObjectView obj : objectListHandler.list) obj.draw(GameCanvas.this, view.topLeft);
            objectListHandler.list.clear();
        }
    }

    private ObjectListHandler objectListHandler = new ObjectListHandler();

    public void drawForegroundContent() {
        if (!lowendMachine) {
            layer3.redraw();
            layer2.redraw();
        }
        if (gameState != null) {
            gameState.forObjectsInRegion(view, objectListHandler);

            objectListHandler.draw();

            tmp.set(10, 30);
            backend.drawText(tmp, CoreColor.Yellow, "Vuoroa jäljellä: " + renderTime(gameTickDuration * (gameState.turnTimeLeft() / gameState.configuration.timeStep)), 16, true, false);
            backend.drawText(tmp.add(0, 20), CoreColor.Yellow, renderGameStatus(), 16, true, false);
            backend.drawText(tmp.add(0, 40), CoreColor.Yellow, renderWindStatus(), 16, true, false);
        }
    }
}

