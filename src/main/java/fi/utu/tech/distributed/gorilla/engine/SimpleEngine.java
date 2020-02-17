package fi.utu.tech.distributed.gorilla.engine;

import fi.utu.tech.distributed.gorilla.engine.rtree.Root;
import fi.utu.tech.oomkit.canvas.Point2D;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;

public class SimpleEngine implements Engine {
    public final double height;
    private final int maxObjects;
    private final double timeStep;

    private final ProxyGameObject[] slotToGameObject;
    private final PhysicalObject[] state1;
    private final PhysicalObject[] state2;
    private final int[] mappingSlots;

    private final Root<PhysicalObject> scene = new Root<>();
    private final Point2D gravity = new Point2D(0, 9);

    private boolean flip;
    private int mappingSlotCursor;
    private double timestamp;

    public SimpleEngine(double height, int maxObjects, double timeStep) {
        this.height = height;
        this.maxObjects = maxObjects;
        this.timeStep = timeStep;

        slotToGameObject = new ProxyGameObject[maxObjects];
        state1 = new PhysicalObject[maxObjects];
        state2 = new PhysicalObject[maxObjects];
        mappingSlots = new int[maxObjects];
        init();
    }

    @Override
    public void init() {
        for (int i = 0; i < maxObjects; i++) {
            mappingSlots[i] = i;
            slotToGameObject[i] = null;
            state1[i] = null;
            state2[i] = null;
        }
        scene.clear();
        mappingSlotCursor = 0;
        timestamp = 0;
    }

    public GameObject getEngineObject(ProxyGameObject g) {
        return g.getId() == -1 ? g : getObject(g.getId());
    }

    @Override
    public void updateObject(ProxyGameObject g) {
        if (g.getId() == -1) return;

        PhysicalObject obj1 = getObject(g.getId());
        PhysicalObject obj2 = getOther(g.getId());

        obj1.setTo(g);
        obj2.setTo(g);
    }

    @Override
    public void bindObject(ProxyGameObject g, boolean movable) {
        assert(g.getId() == -1);
        int id = assignId();
        assert (id >= 0 && id < maxObjects);
        g.setId(id);
        state1[id] = new PhysicalObject(g, movable);
        state2[id] = new PhysicalObject(g, movable);
        slotToGameObject[id] = g;
        scene.add(getObject(id));
    }

    @Override
    public void unbind(ProxyGameObject g) {
        final int id = g.getId();
        if (id == -1) return;
        scene.remove(getObject(id));
        freeId(id);
        g.setId(-1);
    }

    // warning: slow
    @Override
    public Collection<ProxyGameObject> objectsInRegion(Region region) {
        Collection<PhysicalObject> objs = scene.findIntersections(region);
        Set<ProxyGameObject> gobjs = new HashSet<>();
        for (PhysicalObject obj : objs) gobjs.add(slotToGameObject[obj.id]);
        return gobjs;
    }

    private class SearchDispatcher implements Consumer<PhysicalObject> {
        public Consumer<ProxyGameObject> handler;

        @Override
        public void accept(PhysicalObject obj) {
            handler.accept(slotToGameObject[obj.id]);
        }
    }

    private final SearchDispatcher searchDispatcher = new SearchDispatcher();

    @Override
    public void handleObjectsInRegion(Region region, Consumer<ProxyGameObject> handler) {
        searchDispatcher.handler = handler;
        scene.handleIntersections(region, searchDispatcher);
    }

    private int assignId() {
        return mappingSlots[mappingSlotCursor++];
    }

    private void freeId(int id) {
        for (int j = 0; j < mappingSlotCursor; j++)
            if (mappingSlots[j] == id) {
                mappingSlotCursor--;
                mappingSlots[j] = mappingSlots[mappingSlotCursor];
                mappingSlots[mappingSlotCursor] = id;
                break;
            }

        state1[id] = null;
        state2[id] = null;
        slotToGameObject[id] = null;
    }

    private PhysicalObject getObject(int id) {
        return flip ? state2[id] : state1[id];
    }

    private PhysicalObject getOther(int id) {
        return flip ? state1[id] : state2[id];
    }

    private final Point2D tmp = new Point2D();

    private class CollisionDispatcher implements Consumer<PhysicalObject> {
        public ProxyGameObject active;

        @Override
        public void accept(PhysicalObject obj) {
            if (active == null) return;

            ProxyGameObject o = slotToGameObject[obj.id];
            if (active != o) {
                if (active.collideWith(o)) unbind(active);
                if (o.collideWith(active)) unbind(o);
            }
        }
    }

    private final CollisionDispatcher collisionDispather = new CollisionDispatcher();

    @Override
    public double currentTimeStamp() { return timestamp; }

    @Override
    public double timeStep() {
        return timeStep;
    }

    @Override
    public void run() {
        timestamp += timeStep;

        flip = !flip;

        for (int slot = 0; slot < mappingSlotCursor; slot++) {
            int i = mappingSlots[slot];
            final PhysicalObject src = getOther(i);
            final PhysicalObject dst = getObject(i);
            final ProxyGameObject gobj = slotToGameObject[i];
            assert (gobj != null);

            dst.mass = src.mass;
            dst.movable = src.movable;

            if (!src.movable) {
                dst.position.set(src.position);
                dst.acceleration.set(0, 0);
                dst.velocity.set(0, 0);
                continue;
            }

            if (dst.position.y > height) {
                unbind(gobj);
                continue;
            }

            tmp.set(src.acceleration);
            if (src.mass>0) tmp.add(gravity);
            tmp.mul(timeStep);
            dst.acceleration.set(src.acceleration);
            dst.velocity.set(tmp).mul(timeStep).add(src.velocity);
            dst.position.set(src.velocity).mul(timeStep).add(src.position);
            dst.setPosition(dst.position);

            dst.collision = scene.intersectsWith(dst);

            if (dst.collision) {
                collisionDispather.active = gobj;
                scene.handleIntersections(dst, collisionDispather);

                if (slotToGameObject[i] == null) continue;

                if (!gobj.movable) {
                    dst.setPosition(src.position);
                    dst.acceleration.set(0, 0);
                    dst.velocity.set(0, 0);
                } else {
                    scene.remove(src);
                    scene.add(dst);
                }
            }

            gobj.getPosition().set(dst.position);
            gobj.getVelocity().set(dst.velocity);
            gobj.getAcceleration().set(dst.acceleration);
        }
    }
}