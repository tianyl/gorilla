package fi.utu.tech.distributed.gorilla.engine;

import fi.utu.tech.oomkit.canvas.Point2D;

import java.util.Objects;

public class Rect implements Region {
    public final Point2D topLeft;
    public final Point2D bottomRight;

    public Rect(Point2D topLeft, Point2D bottomRight) {
        this.topLeft = topLeft;
        this.bottomRight = bottomRight;
    }

    public Point2D topLeft() {
        return topLeft;
    }

    public Point2D bottomRight() {
        return bottomRight;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Rect)) return false;
        Rect rect = (Rect) o;
        return topLeft.equals(rect.topLeft) &&
                bottomRight.equals(rect.bottomRight);
    }

    @Override
    public int hashCode() {
        return Objects.hash(topLeft, bottomRight);
    }

    public String toString() {
        return topLeft().toPoint() + " - " + bottomRight().toPoint();
    }
}