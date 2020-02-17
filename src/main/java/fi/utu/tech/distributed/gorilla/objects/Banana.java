package fi.utu.tech.distributed.gorilla.objects;

import fi.utu.tech.distributed.gorilla.AssetManager;
import fi.utu.tech.distributed.gorilla.engine.Engine;
import fi.utu.tech.distributed.gorilla.engine.ProxyGameObject;
import fi.utu.tech.oomkit.app.Scheduled;
import fi.utu.tech.oomkit.canvas.Canvas;
import fi.utu.tech.oomkit.canvas.Point2D;
import javafx.scene.image.Image;

public class Banana extends ImageGameObject implements Scheduled {
    private final Image lethalImage = AssetManager.getInstance().getImage("/banana2.png");
    private final Wind wind;
    private final int safetyZone;
    private final HitHandler<Banana> bananaHandler;
    private final double initTimestamp;
    private final double windFactor;
    private boolean active = true;

    public Banana(Engine engine, Point2D velocity, Point2D position, int safetyZone, Wind wind, double windFactor, HitHandler<Banana> bananaHandler) {
        super(engine, velocity, position, 1, "/banana.png");
        this.initTimestamp = engine.currentTimeStamp();
        this.safetyZone = safetyZone;
        this.bananaHandler = bananaHandler;
        this.wind = wind;
        this.windFactor = windFactor;
        zOrder = 3;
    }

    // only become lethal after 'safetyZone' time steps
    public boolean lethal() {
        return active && engine.currentTimeStamp() > initTimestamp + safetyZone * engine.timeStep();
    }

    public void deactivate() {
        active = false;
    }

    protected boolean collisionTest(ProxyGameObject other) {
        // destroy if hit with a gorilla
        if (other instanceof Gorilla && lethal()) {
            return true;
        }

        // destroy if two bananas hit
        if (other instanceof Banana && other != this && lethal())
            return true;

        return !active || (other instanceof SceneBorder || other instanceof Building);
    }

    // remove banana && activate hit handled if deactivated / hits a building
    @Override
    protected boolean collideWith(ProxyGameObject other) {
        if (collisionTest(other)) {
            bananaHandler.activate(this);
            return true;
        }
        return false;
    }

    private final Point2D tmp = new Point2D();

    @Override
    public void tick() {
        tmp.set(getVelocity());
        tmp.add(wind.target() / windFactor, 0);
        setVelocity(tmp);
    }

    @Override
    protected void drawTo(Canvas canvas, Point2D position) {
        canvas.drawImage(position, lethal() ? lethalImage : img);
    }
}
