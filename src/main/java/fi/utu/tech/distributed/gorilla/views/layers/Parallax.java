package fi.utu.tech.distributed.gorilla.views.layers;

import fi.utu.tech.distributed.gorilla.engine.SimpleEngine;
import fi.utu.tech.distributed.gorilla.logic.Move;
import fi.utu.tech.distributed.gorilla.logic.Player;
import fi.utu.tech.distributed.gorilla.objects.Gorilla;
import fi.utu.tech.distributed.gorilla.views.BuildingView;
import fi.utu.tech.distributed.gorilla.views.ProxyCanvas;
import fi.utu.tech.oomkit.canvas.Canvas;
import fi.utu.tech.oomkit.canvas.Point2D;

import java.util.Deque;
import java.util.LinkedList;
import java.util.Random;

public class Parallax extends ProxyCanvas {
    private final Deque<BuildingView> buildings;
    private final double shade;
    private final boolean containsGorillas;
    private final Random generator;
    private final Point2D tmp = new Point2D();
    private final Point2D tmp2 = new Point2D(0, 0);
    private double x = 0;

    public Parallax(Canvas backend, double shade, boolean containsGorillas, long seed) {
        super(backend);
        buildings = new LinkedList<>();
        this.shade = shade;
        this.containsGorillas = containsGorillas;
        generator = new Random(seed);

        // initial buildings
        resized();
    }

    protected void resized() {
        super.resized();
        x = 0;
        if (buildings == null) return;
        buildings.clear();
        addBuildings(getWidth());
    }

    private void addBuildings(double w) {
        while (w > 0) {
            BuildingView bv = newBuilding();
            w -= bv.width;
            buildings.addLast(bv);
        }
    }

    private BuildingView newBuilding() {
        return BuildingView.createRandom(generator.nextLong(), 140, 500, shade, tmp, tmp2);
    }

    public void update(double delta) {
        // scroll to right
        if (x < 0 && x + buildings.getFirst().width < 0) {
            buildings.removeFirst();
            x = 0;
        }

        // scroll to left
        if (delta < 0 && x >= 0) {
            buildings.removeLast();
            buildings.addFirst(newBuilding());
            x = -buildings.getFirst().width;
        }
        if (x > 0 && buildings.getLast().width - x < 0) {
            x = 0;
        }

        double w = getWidth() + 150;
        for (BuildingView bv : buildings) w -= bv.width;
        addBuildings(w);

        x -= delta;
    }

    private Gorilla gorillaGraphics = new Gorilla(new SimpleEngine(0, 1, 1), new Point2D(), new Player("foo", null, false)) {
        public Move playTurn() {
            return null;
        }
    };

    @Override
    public void drawBackgroundContent() {
        tmp.set(x, 0);
        tmp2.set(0, 0);
        for (BuildingView building : buildings) {
            tmp.set(tmp.x, getHeight() - building.height);
            building.draw(this, tmp);
            tmp.add(building.width / 2.0, 0.0);
            if (containsGorillas && building.width > 100) {
                gorillaGraphics.place(tmp);
                gorillaGraphics.draw(this, tmp2);
            }
            tmp.add(building.width / 2.0, 0.0);
        }
    }
}