package fi.utu.tech.distributed.gorilla.objects;

import fi.utu.tech.distributed.gorilla.engine.Engine;
import fi.utu.tech.distributed.gorilla.engine.ProxyGameObject;
import fi.utu.tech.oomkit.canvas.Canvas;
import fi.utu.tech.oomkit.canvas.Point2D;

/**
 * Invisible non-moving border. Needed since the buildings would fall otherwise. Try it!
 */
public class SceneBorder extends ProxyGameObject {
    public SceneBorder(Engine engine, Point2D position, Point2D form) {
        super(engine, position, new Point2D(), form, 0);
        movable = false;
    }

    @Override
    public void draw(Canvas canvas, Point2D trans) {
    }
}
