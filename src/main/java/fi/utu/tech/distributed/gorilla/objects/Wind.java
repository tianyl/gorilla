package fi.utu.tech.distributed.gorilla.objects;

import fi.utu.tech.oomkit.app.Scheduled;

/**
 * Just eye candy.
 * Shows the direction of the wind.
 */
public class Wind implements Scheduled {
    private double now = 0;
    private double target = 0;

    @Override
    public void tick() {
        if (now < target) now += 0.01;
        if (now > target) now -= 0.01;
    }

    public void setTarget(double target) {
        this.target = target;
    }

    public double now() {
        return now;
    }

    public double target() {
        return target;
    }
}