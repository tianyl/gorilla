package fi.utu.tech.distributed.gorilla.objects;

import fi.utu.tech.distributed.gorilla.AssetManager;
import fi.utu.tech.distributed.gorilla.engine.Engine;
import fi.utu.tech.distributed.gorilla.engine.ProxyGameObject;
import fi.utu.tech.oomkit.canvas.Canvas;
import fi.utu.tech.oomkit.canvas.Point2D;
import javafx.scene.image.Image;

/**
 * Just eye candy.
 * Doesn't do anything special other than temporarily changes the face when hit.
 */
public class Sun extends ImageGameObject {
    private Image img2 = AssetManager.getInstance().getImage("/sun2.png");
    private double faceTimeout = -1;

    public Sun(Engine engine, Point2D position, int z) {
        super(engine, new Point2D(0, 0), position, 0, "/sun.png");
        this.zOrder = z;
    }

    @Override
    protected boolean collideWith(ProxyGameObject other) {
        if (other instanceof Banana) {
            faceTimeout = engine.currentTimeStamp() + 15;
        }
        return false;
    }

    @Override
    protected void drawTo(Canvas canvas, Point2D position) {
        canvas.drawImage(position, faceTimeout >= engine.currentTimeStamp() ? img2 : img);
    }
}
