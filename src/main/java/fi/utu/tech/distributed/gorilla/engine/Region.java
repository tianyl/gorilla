package fi.utu.tech.distributed.gorilla.engine;

import fi.utu.tech.oomkit.canvas.Point2D;

import java.util.Collection;

public interface Region {
    Point2D topLeft();

    Point2D bottomRight();

    private boolean testIntersection(Region other) {
        if (other.contains(topLeft()) || other.contains(bottomRight()))
            return true;

        if (other.contains(topLeft().x, bottomRight().y)) return true;
        return other.contains(bottomRight().x, topLeft().y);
    }

    default boolean intersectsWith(Region other) {
        return other != null && (testIntersection(other) || other.testIntersection(this));
    }

    default double area() {
        double dx = bottomRight().x - topLeft().x;
        double dy = bottomRight().y - topLeft().y;

        return dx * dy;
    }
/*
    default Point2D center() {
        return new Point2D(bottomRight()).sub(topLeft()).mul(0.5).add(topLeft());
    }

    default double distance(Region other) {
        Point2D c = other.center().sub(center());
        c.x *= c.x;
        c.y *= c.y;
        return c.x + c.y;

    static Region union(Collection<? extends Region> regions) {
        if (regions.isEmpty()) return null;

        Region first = regions.iterator().next();
        double lx, rx, ty, by;
        {
            assert(first != null);
            assert(first.topLeft() != null);
            double x = first.topLeft().x, y = first.topLeft().y;
            lx = rx = x;
            ty = by = y;
        }
        for (Region r : regions) {
            if (lx > r.topLeft().x) lx = r.topLeft().x;
            if (ty > r.topLeft().y) ty = r.topLeft().y;
            if (rx < r.bottomRight().x) rx = r.bottomRight().x;
            if (by < r.bottomRight().y) by = r.bottomRight().y;
        }

        return new Rect(new Point2D(lx, ty), new Point2D(rx, by));
    }
    default Region union(Region other) {
        return union(List.of(this, other));
    }
    */

    default Region unionN(Collection<? extends Region> regions) {
        Region r1 = regions.iterator().next();
        topLeft().set(r1.topLeft());
        bottomRight().set(r1.bottomRight());
        for (Region r : regions) {
            topLeft().min(r.topLeft());
            bottomRight().max(r.bottomRight());
        }
        return this;
    }

    default Region union2(Region r1, Region r2) {
        topLeft().set(r1.topLeft());
        bottomRight().set(r1.bottomRight());
        topLeft().min(r2.topLeft());
        bottomRight().max(r2.bottomRight());
        return this;
    }

    default Region union(Region r) {
        topLeft().min(r.topLeft());
        bottomRight().max(r.bottomRight());
        return this;
    }

    default boolean contains(Point2D point) {
        return point.x >= topLeft().x &&
                point.y >= topLeft().y &&
                point.x <= bottomRight().x &&
                point.y <= bottomRight().y;
    }

    default boolean contains(double x, double y) {
        return x >= topLeft().x &&
                y >= topLeft().y &&
                x <= bottomRight().x &&
                y <= bottomRight().y;
    }

    default boolean contains(Region other) {
        return contains(other.bottomRight()) && contains(other.topLeft());
    }
}