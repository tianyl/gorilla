package fi.utu.tech.distributed.gorilla.logic;

/**
 * TODO: make compatible with network play
 */
public final class MoveThrowBanana extends Move {
    /**
     * Angle: -45° <= angle <= 225°. Double.NaN = not set
     */
    public final double angle;

    /**
     * Velocity: 0 <= velocity <= 150. Double.NaN = not set
     */
    public final double velocity;

    public MoveThrowBanana(double angle, double velocity) throws IllegalArgumentException {
        this.angle = angle;
        this.velocity = velocity;

        if (!Double.isNaN(angle) && !(angle >= -45 && angle <= 225))
            throw new IllegalArgumentException("Virheellinen kulman arvo, sallittu väli -45 .. 225 astetta.");
        if (!Double.isNaN(velocity) && !(velocity >= 0 && velocity <= 150))
            throw new IllegalArgumentException("Virheellinen nopeuden arvo, sallittu väli 0 .. 150 voimayksikköä.");

    }
}
