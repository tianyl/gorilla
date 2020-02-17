package fi.utu.tech.distributed.gorilla.engine;

import fi.utu.tech.oomkit.canvas.Point2D;

public interface GameObject {
    int getId();

    Point2D getAcceleration() ;

    Point2D getVelocity();

    Point2D getPosition();

    Point2D getForm();

    double getMass();

    boolean isMovable();

    void setId(int id);

    void setAcceleration(Point2D a) ;

    void setVelocity(Point2D v);

    void setPosition(Point2D p);

    void setForm(Point2D f);

    void setMass(double m);

    void setMovable(boolean movable);

    default void setTo(GameObject other) {
        getPosition().set(other.getPosition());
        getVelocity().set(other.getVelocity());
        getAcceleration().set(other.getAcceleration());
        getForm().set(other.getForm());
        setMass(other.getMass());
        setMovable(other.isMovable());
    }
}
