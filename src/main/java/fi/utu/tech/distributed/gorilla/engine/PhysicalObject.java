package fi.utu.tech.distributed.gorilla.engine;

import fi.utu.tech.oomkit.canvas.Point2D;

class PhysicalObject extends GameObjectBase implements Region {
    private final Point2D bottomRight = new Point2D();
    boolean collision = false;

    @Override
    public void setPosition(Point2D pos) {
        position.set(pos);
        bottomRight.set(pos).add(form);
    }

    @Override
    public Point2D topLeft() {
        return position;
    }

    @Override
    public Point2D bottomRight() {
        return bottomRight;
    }

    public PhysicalObject(GameObject gameObject, boolean movable) {
        super(gameObject);
        setPosition(getPosition());
        setMovable(gameObject.isMovable() && movable);
    }

    @Override
    public void setTo(GameObject gobj) {
        setPosition(gobj.getPosition());
        setVelocity(gobj.getVelocity());
        setAcceleration(gobj.getAcceleration());
        setForm(gobj.getForm());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PhysicalObject)) return false;
        PhysicalObject that = (PhysicalObject) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return id;
    }

    public String toString() {
        return topLeft().toPoint() + " - " + bottomRight().toPoint() + " " + id;
    }
}
