package fi.utu.tech.distributed.gorilla.logic;

import fi.utu.tech.oomkit.canvas.Point2D;

import java.util.concurrent.LinkedBlockingQueue;

/**
 * TODO: may need modifications for network play
 * depending on your implementation, probably not
 */
public class Player {
    /**
     * Player's name.
     */
    public final String name;

    /**
     * Is this a local player. Might affect e.g. the rendered graphics color.
     */
    public final boolean local;

    /**
     * Determines the pixel offset of the banana launch position.
     */
    private final Point2D launchPos = new Point2D();

    /**
     * Thread safe queue of potential future moves.
     */
    public final LinkedBlockingQueue<Move> moves;

    /* These are public since a set of getters/setters wouldn't provide any safety. */
    public double angle = Double.NaN;
    public double velocity = Double.NaN;
    public boolean alive = true;

    public Player(String name, LinkedBlockingQueue<Move> moves, boolean local) {
        this.name = name;
        this.moves = moves;
        this.local = local;
    }

    public void setLaunchPosition(Point2D s) {
        launchPos.set(s);
    }

    public Point2D getLaunchPosition() {
        return launchPos;
    }

    /**
     * Processes the potentially partial moves from 'moves'. A move is partial if
     * a velocity/angle is Double.NaN.
     */
    public void readMoves() {
        while (moves.peek() != null) {
            Move suggestion = moves.poll();
            if (suggestion instanceof MoveThrowBanana) {
                MoveThrowBanana mtb = (MoveThrowBanana) suggestion;
                if (!Double.isNaN(mtb.angle)) angle = mtb.angle;
                if (!Double.isNaN(mtb.velocity)) velocity = mtb.velocity;
            }
        }
    }

    public boolean readyToMove() {
        return !Double.isNaN(velocity) && !Double.isNaN(angle);
    }

    /**
     * Called by the game main thread. Must not block!
     * @return A Move or null if haven't decided yet.
     */
    public Move playTurn() {
        readMoves();
        if (!Double.isNaN(angle) && !Double.isNaN(velocity)) {
            // for debugging
            if (false)
                System.out.println(name + " heittää, " + angle + "° @ " + velocity);

            Move move = new MoveThrowBanana(angle, velocity);
            angle = velocity = Double.NaN;
            return move;
        }
        return null;
    }
}