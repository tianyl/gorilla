package fi.utu.tech.distributed.gorilla.views;

import fi.utu.tech.oomkit.canvas.Canvas;
import fi.utu.tech.oomkit.canvas.Point2D;

public class ProxyCanvas extends Canvas {
    protected final Canvas backend;
    protected final Point2D dimensions = new Point2D();

    public ProxyCanvas(Canvas backend) {
        super(false);
        this.backend = backend;
        if (backend.resizable) {
            backend.peer().widthProperty().addListener(o -> resized());
            backend.peer().heightProperty().addListener(o -> resized());
        }
        resized();
    }

    @Override
    protected void resized() {
        dimensions.set(getWidth(), getHeight());
    }

    @Override
    public javafx.scene.canvas.Canvas peer() {
        return backend.peer();
    }
}
