package fi.utu.tech.distributed.gorilla.engine.rtree;

import fi.utu.tech.distributed.gorilla.engine.Region;

import java.util.Collection;
import java.util.function.Consumer;

public final class Root<X extends Region> extends Node<X> {
    public volatile Node<X> tree = nullNode();

    public void clear() {
        tree = nullNode();
    }

    public Node<X> remove(X obj) {
        return tree = tree.remove(obj);
    }

    @Override
    public Collection<X> findIntersections(Region region) {
        return tree.findIntersections(region);
    }

    @Override
    public void handleIntersections(Region region, Consumer<X> handler) {
        tree.handleIntersections(region, handler);
    }

    @Override
    public boolean intersectsWith(Region region) {
        return tree.intersectsWith(region);
    }

    @Override
    public Node<X> add(X obj) {
        return tree = tree.add(obj);
    }

    @Override
    public Collection<X> contents() {
        return tree.contents();
    }

    @Override
    public int size() {
        return tree.size();
    }

    @Override
    public int depth() {
        return tree.depth();
    }

    @Override
    public String toString() {
        return tree.toString();
    }
}