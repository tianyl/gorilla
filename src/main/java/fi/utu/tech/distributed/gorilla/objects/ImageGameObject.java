package fi.utu.tech.distributed.gorilla.objects;

import fi.utu.tech.distributed.gorilla.AssetManager;
import fi.utu.tech.distributed.gorilla.engine.Engine;
import fi.utu.tech.distributed.gorilla.engine.ProxyGameObject;
import fi.utu.tech.distributed.gorilla.views.objects.ObjectView;
import fi.utu.tech.oomkit.canvas.Canvas;
import fi.utu.tech.oomkit.canvas.Point2D;
import javafx.scene.image.Image;

abstract class ImageGameObject extends ProxyGameObject implements ObjectView {
    protected int zOrder= 0;
    protected final Image img;
    final transient private Point2D tmp = new Point2D();

    public ImageGameObject(Engine engine, Point2D position, Point2D velocity, double mass, String imgFile) {
        super(engine, velocity, position, new Point2D(), mass);
        img = AssetManager.getInstance().getImage(imgFile);
        form.set(img.getWidth(), img.getHeight());
    }

    @Override
    public final int zOrder() {
        return zOrder;
    }

    @Override
    public void draw(Canvas canvas, Point2D trans) {
        tmp.set(getPosition()).sub(trans);
        drawTo(canvas, tmp);
    }

    protected void drawTo(Canvas canvas, Point2D position) {
        canvas.drawImage(position, img);
    }
}