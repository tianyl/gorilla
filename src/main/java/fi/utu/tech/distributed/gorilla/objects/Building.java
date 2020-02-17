package fi.utu.tech.distributed.gorilla.objects;

import fi.utu.tech.distributed.gorilla.engine.Engine;
import fi.utu.tech.distributed.gorilla.engine.ProxyGameObject;
import fi.utu.tech.distributed.gorilla.views.BuildingView;
import fi.utu.tech.distributed.gorilla.views.objects.ObjectView;
import fi.utu.tech.oomkit.canvas.Canvas;
import fi.utu.tech.oomkit.canvas.Point2D;

public class Building extends ProxyGameObject implements ObjectView {
    private final BuildingView view;
    final transient private Point2D tmp = new Point2D();

    public Building(Engine engine, Point2D pos, BuildingView view) {
        super(engine, pos, new Point2D(), new Point2D(view.width, view.height), 1);
        this.view = view;
        movable = false;
    }

    @Override
    public void draw(Canvas canvas, Point2D trans) {
        tmp.set(getPosition()).sub(trans);
        view.draw(canvas, tmp);
    }

    @Override
    public int zOrder() {
        return 2;
    }

    @Override
    protected boolean collideWith(ProxyGameObject other) {
        movable = other instanceof SceneBorder;
        return false;
    }
}
