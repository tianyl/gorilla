package fi.utu.tech.distributed.gorilla.objects;

import fi.utu.tech.distributed.gorilla.engine.Engine;
import fi.utu.tech.oomkit.app.Scheduled;
import fi.utu.tech.oomkit.canvas.Point2D;

/**
 * The cloud is just visualizing the wind direction and speed.
 */
public class Cloud extends ImageGameObject implements Scheduled {
    private final Wind wind;
    private final double speedFactor;
    private final double worldWidth;

    public Cloud(Engine engine, Point2D position, Wind wind, double speedFactor, double worldWidth, int zOrder) {
        super(engine, new Point2D(0, 0), position, 0, speedFactor >= 1.5 ? "/cloud1.png" : "/cloud2.png");
        this.wind = wind;
        this.speedFactor = speedFactor;
        this.worldWidth = worldWidth;
        this.zOrder = zOrder;
    }

    private final Point2D tmp = new Point2D();

    @Override
    public void tick() {
        tmp.set(wind.now() * speedFactor, 0);
        setVelocity(tmp);

        if (position.x < -img.getWidth()) {
            setPosition(position.add(worldWidth, 0));
            engine.updateObject(this);
        }

        if (position.x > worldWidth) {
            setPosition(position.sub(worldWidth, 0));
            engine.updateObject(this);
        }
    }
}