package fi.utu.tech.distributed.gorilla.logic;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * TODO: You may want to compare the constructors
 * The other may be more suitable for multiplayer
 */
public final class GameConfiguration implements Serializable {
    // random seed for constructing the game world
    // should result in identical game worlds on different Java systems
    // affects world creation + gameplay
    public final long seed;

    // world height, affects world creation
    public final double gameWorldHeight;

    // min distance between gorillas, affects world creation
    public final int minGorillaDistance = 600;

    // max distance between gorillas, affects world creation
    public final int maxGorillaDistance = 1100;

    // player names (the number of players can also be deducted from this)
    public final List<String> playerNames;

    // time step for physics simulation (affects gameplay)
    public final double timeStep = 0.15;

    // turn length (see GameCanvas.drawForegroundContent) (affects gameplay)
    public final double turnLength = 30;

    // how many simultaneous objects should the physics engine support (probably ok)
    public final int maxObjects = 10000;

    // how many time step units to wait until the banana becomes lethal (affects gameplay)
    public final int safetyZone = 40;

    // how strongly does the wind affect the banana velocity (affects gameplay)
    public final double windFactor = 40;

    // turn on/off the sun (purely eye candy)
    public final boolean enableSun = true;

    // turn on/off the clouds (purely eye candy)
    public final boolean enableClouds = true;

    // Tämän nimi tulisi muuttaa kuvaavammaksi
    public final Map<Long, String> playerIdNames;

    /**
     * Constructor for game configuration, stores only player names (ids are probably not needed on local play)
     * @param seed Seed value for world generation and other deterministic processes. Affects gameplay.
     * @param gameWorldHeight Height of the game world, affects gameplay.
     * @param playerNames List of player names to play the game. Player objects will be created using this information
     */
    public GameConfiguration(long seed, double gameWorldHeight, List<String> playerNames) {
        this.seed = seed;
        this.gameWorldHeight = gameWorldHeight;
        this.playerNames = playerNames;
        this.playerIdNames = null;
    }

    /**
     * Alternative constructor for game configuration that stores player id and name together
     * Useful for binding peers by their ids but not used in local play
     * @param seed Seed value for world generation and other deterministic processes. Affects gameplay.
     * @param gameWorldHeight Height of the game world, affects gameplay.
     * @param playerIdNames Map of player ids to names. Names will be used to create player objects, id's can be useful for binding peers to player objects.
     */
    public GameConfiguration(long seed, double gameWorldHeight, Map<Long, String> playerIdNames) {
        this.seed = seed;
        this.gameWorldHeight = gameWorldHeight;
        this.playerIdNames = playerIdNames;
        this.playerNames = null;

    }
}