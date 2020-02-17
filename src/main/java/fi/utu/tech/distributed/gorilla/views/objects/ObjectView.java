package fi.utu.tech.distributed.gorilla.views.objects;

import fi.utu.tech.oomkit.canvas.Canvas;
import fi.utu.tech.oomkit.canvas.Point2D;

public interface ObjectView extends Comparable<ObjectView> {
    int zOrder();

    default int compareTo(ObjectView o) {
        return zOrder() - o.zOrder();
    }

    void draw(Canvas canvas, Point2D viewTransform);
}