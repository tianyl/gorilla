package fi.utu.tech.distributed.gorilla.logic;

import fi.utu.tech.distributed.gorilla.engine.Engine;
import fi.utu.tech.distributed.gorilla.engine.ProxyGameObject;
import fi.utu.tech.distributed.gorilla.engine.SimpleEngine;
import fi.utu.tech.distributed.gorilla.objects.*;
import fi.utu.tech.distributed.gorilla.views.BuildingView;
import fi.utu.tech.oomkit.app.Scheduled;
import fi.utu.tech.oomkit.canvas.Point2D;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * The game world class contains methods for creating a game world.
 * <p>
 * The class contains state that doesn't need to be shared on remote systems,
 * the initial state can be fully reconstructed from 'configuration' and 'players'.
 * </p>
 */
public class GameWorld implements Scheduled {
    private final GameConfiguration configuration;
    private final ArrayList<Cloud> clouds = new ArrayList<>();
    private final List<Banana> bananas = new ArrayList<>();
    private final Wind wind = new Wind();

    public final Engine engine;
    public final long initialStateSeed;

    /**
     * The initial state can be fully reconstructed from 'configuration' and 'players'.
     *
     * @param configuration
     * @param players
     */
    public GameWorld(GameConfiguration configuration, List<Player> players) {
        this.configuration = configuration;
        Random builder = new Random(configuration.seed);
        initialStateSeed = builder.nextLong();
        engine = new SimpleEngine(configuration.gameWorldHeight, configuration.maxObjects, configuration.timeStep);
        init(builder, players);
    }

    /**
     * 1) Moves the cloud (just aesthetic, does not affect game outcome)
     * 2) Adjusts the banana trajectories based on wind (deterministic, affects game outcome)
     */
    @Override
    public void tick() {
        wind.tick();
        for (Cloud c : clouds) c.tick();
        for (Banana b : bananas) b.tick();
    }

    /**
     * Changes the wind speed when a new turn begins. Deterministic, affects game outcome.
     */
    public void newTurn(Turn currentTurn) {
        wind.setTarget(currentTurn.windSpeed);
    }

    protected void addGorilla(Point2D position, Player player) {
        Gorilla gorilla = new Gorilla(engine, position, player);
        engine.bindObject(gorilla, true);
    }

    protected void addBanana(Point2D initParams, Point2D initPosition) {
        Banana banana = new Banana(engine, initParams, initPosition, configuration.safetyZone, wind, configuration.windFactor, bananas::remove);
        bananas.add(banana);
        engine.bindObject(banana, true);
    }

    protected void addClouds(Random builder, double width, double maxHeight, int cloudCount) {
        for (int i = 0; i < cloudCount; i++) {
            Point2D position = new Point2D(builder.nextDouble() * width, builder.nextDouble() * maxHeight);
            Cloud cloud = new Cloud(engine, position, wind, builder.nextDouble() * 2 + 0.5, width, (i + 1) * -2);
            clouds.add(cloud);
            engine.bindObject(cloud, true);
        }
    }

    protected void addSun(double x, double y, int z) {
        Sun sun = new Sun(engine, new Point2D(x, y), z);
        engine.bindObject(sun, false);
    }

    protected void addFloor(double sceneHeight, double x1, double x2) {
        ProxyGameObject floor = new SceneBorder(engine,
                new Point2D(x1, sceneHeight + 1),
                new Point2D(x2, 10));
        engine.bindObject(floor, false);
    }

    protected void determinePlayerPositions(List<Player> players, List<Point2D> playerPositions, Random builder) {
        Collections.shuffle(playerPositions, builder);
        for (Player player : players) {
            Point2D position = playerPositions.remove(0);
            addGorilla(position, player);
        }
    }

    protected void init(Random builder, List<Player> players) {
        final double sceneHeight = configuration.gameWorldHeight;
        final List<Point2D> playerPositions = new ArrayList<>();
        double currentX = 0;

        engine.init();
        clouds.clear();

        // landscape construction, also populates 'playerPositions'
        {
            Point2D tmp = new Point2D();
            Point2D tmp2 = new Point2D();
            double nextDistance = -1;
            double distance = 0;

            while (playerPositions.size() < players.size()) {
                BuildingView bv = BuildingView.createRandom(builder.nextLong(), 140, 500, 0.0, tmp, tmp2);
                Point2D tl = new Point2D(currentX, sceneHeight - bv.height);
                Building building = new Building(engine, tl, bv);
                engine.bindObject(building, true);

                distance += bv.width + 1;

                if (nextDistance == -1) {
                    nextDistance = configuration.minGorillaDistance + builder.nextDouble() * (configuration.maxGorillaDistance - configuration.minGorillaDistance);
                }

                if (distance > nextDistance && bv.width > 95) {
                    distance = 0;
                    nextDistance = -1;
                    playerPositions.add(tl.copy().add(bv.width / 2.0, 0));
                }

                currentX += bv.width + 1;
            }
        }

        addFloor(sceneHeight, -10, currentX + 20);

        determinePlayerPositions(players, playerPositions, builder);

        // construct clouds & sun
        {
            int cloudCount = (int) (currentX / 300);

            if (configuration.enableClouds)
                addClouds(builder, currentX, configuration.gameWorldHeight / 8.0, cloudCount);

            if (configuration.enableSun)
                addSun(currentX / 2, configuration.gameWorldHeight / 16, -(cloudCount / 2) * 2 - 1);
        }
    }
}
