package fi.utu.tech.distributed.gorilla.engine.rtree;

import fi.utu.tech.distributed.gorilla.engine.Region;

import java.util.Collection;
import java.util.Set;
import java.util.function.Consumer;

public class Leaf<X extends Region> extends Node<X> {
    public final X object;
    private final Set<X> set;

    public Leaf(X obj) {
        object = obj;
        set = Set.of(object);
        setRegion(obj);
    }

    @Override
    public Collection<X> findIntersections(Region region) {
        if (object.intersectsWith(region)) return set;
        return Set.of();
    }

    @Override
    public void handleIntersections(Region region, Consumer<X> handler) {
        if (object.intersectsWith(region)) handler.accept(object);
    }

    @Override
    public boolean intersectsWith(Region region) {
        return object.intersectsWith(region);
    }

    @Override
    public Collection<X> contents() {
        return set;
    }

    @Override
    public Node<X> add(X obj) {
        return new Parent<>(this, new Leaf<>(obj));
    }

    @Override
    public Node<X> remove(X obj) {
        return null;
    }

    @Override
    public int size() {
        return 1;
    }

    @Override
    public int depth() {
        return 1;
    }
}
