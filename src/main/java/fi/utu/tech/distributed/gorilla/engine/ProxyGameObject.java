package fi.utu.tech.distributed.gorilla.engine;

import fi.utu.tech.oomkit.canvas.Canvas;
import fi.utu.tech.oomkit.canvas.Point2D;

public abstract class ProxyGameObject extends GameObjectBase {
    protected final Engine engine;

    public ProxyGameObject(Engine engine, int id, Point2D position, Point2D velocity, Point2D acceleration, Point2D form, double mass, boolean movable) {
        super(id, position, velocity, acceleration, form, mass, movable);
        this.engine = engine;
    }

    public ProxyGameObject(Engine engine, Point2D position, Point2D velocity, Point2D form, double mass) {
        this(engine, -1, position, velocity, new Point2D(), form, mass, true);
    }

    public abstract void draw(Canvas canvas, Point2D trans);

    @Override
    public void setId(int id) {
        this.id = id;
    }

    @Override
    public void setPosition(Point2D pos) {
        setTo(engine.getEngineObject(this));
        super.setPosition(pos);
        engine.updateObject(this);
    }

    @Override
    public void setVelocity(Point2D v) {
        setTo(engine.getEngineObject(this));
        super.setVelocity(v);
        engine.updateObject(this);
    }

    @Override
    public void setAcceleration(Point2D a) {
        setTo(engine.getEngineObject(this));
        super.setAcceleration(a);
        engine.updateObject(this);
    }

    @Override
    public void setForm(Point2D form) {
        setTo(engine.getEngineObject(this));
        super.setForm(form);
        engine.updateObject(this);
    }

    @Override
    public void setMovable(boolean movable) {
        super.setMovable(movable);
        engine.updateObject(this);
    }

    protected boolean collideWith(ProxyGameObject other) { return false; }
}