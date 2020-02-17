package fi.utu.tech.distributed.gorilla.engine.rtree;

import fi.utu.tech.distributed.gorilla.engine.Region;

import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.function.Consumer;

public class MultiLeaf<X extends Region> extends Node<X> {
    private Collection<X> objects = new LinkedList<>();

    public MultiLeaf(X obj) {
        objects.add(obj);
        setRegion(obj);
    }

    @Override
    public Collection<X> findIntersections(Region region) {
        Collection<X> ret = new HashSet<>();
        if (region.intersectsWith(this))
            for (X obj : objects)
                if (region.intersectsWith(obj)) ret.add(obj);

        return ret;
    }

    @Override
    public void handleIntersections(Region region, Consumer<X> handler) {
        if (region.intersectsWith(this))
            for (X obj : objects)
                if (region.intersectsWith(obj)) handler.accept(obj);
    }

    @Override
    public boolean intersectsWith(Region region) {
        if (region.intersectsWith(this))
            for (X obj : objects)
                if (region.intersectsWith(obj))
                    return true;

        return false;
    }

    @Override
    public Collection<X> contents() {
        return objects;
    }

    @Override
    public Node<X> add(X obj) {
        if (!contains(obj)) {
            return new Parent<>(this, new MultiLeaf<>(obj));
        }
        objects.add(obj);
        union(obj);

        return this;
    }

    @Override
    public Node<X> remove(X obj) {
        objects.remove(obj);

        if (objects.isEmpty()) return nullNode();
        unionN(objects);
        return this;
    }

    @Override
    public int size() {
        return objects.size();
    }

    @Override
    public int depth() {
        return 1;
    }
}