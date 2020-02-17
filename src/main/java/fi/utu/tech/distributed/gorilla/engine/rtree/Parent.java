package fi.utu.tech.distributed.gorilla.engine.rtree;

import fi.utu.tech.distributed.gorilla.engine.Rect;
import fi.utu.tech.distributed.gorilla.engine.Region;
import fi.utu.tech.oomkit.canvas.Point2D;

import java.util.Collection;
import java.util.HashSet;
import java.util.function.Consumer;

public class Parent<X extends Region> extends Node<X> {
    public Node<X> left, right;

    public Parent(Node<X> left, Node<X> right) {
        this.left = left;
        this.right = right;
        recalculateRegion();
    }

    @Override
    public Collection<X> findIntersections(Region region) {
        Collection<X> ret = new HashSet<>(left.findIntersections(region));
        ret.addAll(right.findIntersections(region));
        return ret;
    }

    @Override
    public void handleIntersections(Region region, Consumer<X> handler) {
        left.handleIntersections(region, handler);
        right.handleIntersections(region, handler);
    }

    @Override
    public Node<X> add(X obj) {
        performAdd(obj);
        rebalance();
        assert(contents().contains(obj));/*
        if(!findIntersections(obj).contains(obj)) {
            System.out.println(obj);
        }*/
        assert(findIntersections(obj).contains(obj));
        return this;
    }

    @Override
    public Node<X> remove(X obj) {
        if (obj.intersectsWith(left)) {
            left = left.remove(obj);
        }
        if (obj.intersectsWith(right)) {
            right = right.remove(obj);
        }
        if (left.size() == 0 && right.size() == 0) return left;
        if (left.size() == 0) return right;
        if (right.size() == 0) return left;
        recalculateRegion();
        return this;
    }

    @Override
    public boolean intersectsWith(Region region) {
        return region.intersectsWith(left) || region.intersectsWith(right);
    }

    private static final Region tmp = new Rect(new Point2D(), new Point2D());

    private Node<X> performAdd(X obj) {
        if (left.contains(obj)) {
            left = left.add(obj);
            return this;
        }
        if (right.contains(obj)) {
            right = right.add(obj);
            return this;
        }

        double size_l = tmp.union2(left, obj).area() + right.area();
        double size_r = tmp.union2(right, obj).area() + left.area();

        if (size_l > size_r) {
            right = right.add(obj);
        } else {
            left = left.add(obj);
        }
        recalculateRegion();
        return this;
    }

    private void recalculateRegion() { union2(left,right); }

    private void rebalance() {
        int diff = left.depth() - right.depth();

        Node<X> ln = left;
        Node<X> r = right;

        if (diff < -1) {
            ln = right;
            r = left;
            diff = -diff;
        }

        if (diff > 1 && ln instanceof Parent) {
            Parent<X> l = (Parent<X>)ln;
            Node<X> ll = l.left, lr = l.right;

            if (lr.area() + tmp.union2(r, ll).area() < ll.area() + tmp.union2(r, lr).area()) {
                left = lr;
                l.right = r;
                right = l;
                l.recalculateRegion();
                //System.out.println("Moved LL -> R");
            } else {
                left = l.left;
                l.left = l.right;
                l.right = r;
                right = l;
                l.recalculateRegion();
                //System.out.println("Moved LR -> R");
            }
            recalculateRegion();
        }
    }

    @Override
    public Collection<X> contents() {
        Collection<X> ret = new HashSet<>(left.contents());
        ret.addAll(right.contents());
        return ret;
    }

    @Override
    public int size() {
        return left.size() + right.size();
    }

    @Override
    public int depth() {
        return 1 + Math.max(left.depth(), right.depth());
    }

    @Override
    public String toString() {
        return super.toString() + " " + left.size() + " + " + right.size();
    }
}
