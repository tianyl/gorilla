package fi.utu.tech.distributed.gorilla.logic;

import java.util.Random;

/**
 * Represents a game turn
 * TODO: make compatible with network play
 */
public final class Turn {
    /**
     * Turn id. The next turn will have an id of this.id+1
     */
    public final int id;

    /**
     * Wind speed and direction (neg = left, pos = right).
     */
    public final double windSpeed;

    /**
     * Timestamp value in the beginning of the turn.
     * Note that the timestamp values are tied to game engine ticks which are in turn
     * loosely connected to actual wall clock ticks, activated by the oomkit framework.
     */
    public final double startTimeStamp;

    /**
     * Time length in timestamp compatible time units
     */
    public final double turnLength;

    private transient Random builder;

    public Turn(Random builder, int id, double startTimeStamp, double turnLength) {
        this.builder = builder;
        windSpeed = (builder.nextInt(100) - 50) / 10.0;
        this.id = id;
        this.startTimeStamp = startTimeStamp;
        this.turnLength = turnLength;
    }

    public Turn next(double timeStamp) {
        return new Turn(builder, id + 1, timeStamp + turnLength, turnLength);
    }
}