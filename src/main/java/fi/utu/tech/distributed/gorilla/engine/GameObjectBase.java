package fi.utu.tech.distributed.gorilla.engine;

import fi.utu.tech.oomkit.canvas.Point2D;

public abstract class GameObjectBase implements GameObject {
    protected int id;
    protected final Point2D position;
    protected final Point2D velocity;
    protected final Point2D acceleration;
    protected final Point2D form;
    protected double mass;
    protected boolean movable;

    public GameObjectBase(int id, Point2D position, Point2D velocity, Point2D acceleration, Point2D form, double mass, boolean movable) {
        this.id = id;
        this.acceleration = acceleration;
        this.velocity = velocity;
        this.position = position;
        this.form = form;
        this.mass = mass;
        this.movable = movable;
    }

    public GameObjectBase(GameObject other) {
        this(other.getId(), other.getPosition().copy(), other.getVelocity().copy(), other.getAcceleration().copy(), other.getForm().copy(), other.getMass(), other.isMovable());
    }


    @Override
    public int getId() {
        return id;
    }

    @Override
    public Point2D getPosition() {
        return position;
    }

    @Override
    public Point2D getVelocity() {
        return velocity;
    }

    @Override
    public Point2D getAcceleration() {
        return acceleration;
    }

    @Override
    public Point2D getForm() {
        return form;
    }

    @Override
    public double getMass() {
        return mass;
    }

    @Override
    public boolean isMovable() {
        return movable;
    }

    @Override
    public void setId(int id) {
        this.id = id;
    }

    @Override
    public void setPosition(Point2D pos) {
        position.set(pos);
    }

    @Override
    public void setVelocity(Point2D pos) {
        velocity.set(pos);
    }

    @Override
    public void setAcceleration(Point2D pos) {
        acceleration.set(pos);
    }

    @Override
    public void setForm(Point2D form) {
        form.set(form);
    }

    @Override
    public void setMovable(boolean movable) {
        this.movable = movable;
    }

    @Override
    public void setMass(double mass) {
        this.mass = mass;
    }
}