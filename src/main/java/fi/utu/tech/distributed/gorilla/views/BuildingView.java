package fi.utu.tech.distributed.gorilla.views;

import fi.utu.tech.oomkit.canvas.Canvas;
import fi.utu.tech.oomkit.canvas.Point2D;
import fi.utu.tech.oomkit.colors.Color;
import fi.utu.tech.oomkit.colors.CoreColor;
import javafx.scene.SnapshotParameters;
import javafx.scene.image.WritableImage;

import java.util.List;
import java.util.Random;
import java.util.function.Function;

public class BuildingView {
    public final int width;
    public final int height;
    public final Color color;
    public final boolean[][] windows;
    public final long seed;
    private final double shade;
    private static final List<Color> colorOptions = List.of(CoreColor.Cyan, CoreColor.Gray, CoreColor.Red);
    private WritableImage data;

    public BuildingView(int width, int height, Color color, long seed, double shade, Point2D tmp, Point2D tmp2) {
        this.width = width;
        this.height = height;
        this.color = color;
        this.seed = seed;
        this.windows = windows();
        this.shade = shade;
        draw(tmp, tmp2);
    }

    public static BuildingView createRandom(long seed, int maxWidth, int maxHeight, double shade, Point2D tmp, Point2D tmp2) {
        Random jemma = new Random(seed);

        Function<Integer, Integer> arvo = max -> jemma.nextInt(max * 4 / 5) + max / 5;
        int leveys = arvo.apply(maxWidth);
        int korkeus = arvo.apply(maxHeight);

        return new BuildingView(leveys, korkeus, colorOptions.get(jemma.nextInt(3)).darken(shade), seed, shade, tmp, tmp2);
    }

    public void draw(Canvas canvas, Point2D position) {
        canvas.drawImage(position, data);
    }

    protected boolean[][] windows() {
        Random ikkunaMaatti = new Random(seed);

        double väli = 12;
        int l = (int) (width / väli - 1 / 8.0);
        int k = (int) (height / väli - 1 / 8.0);

        boolean[][] matriisi = new boolean[k][l];

        for (int x = 0; x < l; x++)
            for (int y = 0; y < k; y++)
                matriisi[y][x] = ikkunaMaatti.nextBoolean();

        return matriisi;
    }

    protected void draw(Point2D tmp, Point2D tmp2) {
        Canvas c = Canvas.backBuffer(width, height);

        tmp.set(0,0);
        tmp2.set(width,height);

        c.drawRectangle(tmp, tmp2, color, true);

        double väli = 12;
        double y = väli / 2;
        for (boolean[] rivi : windows) {
            double x = väli / 2;
            for (boolean ikkuna : rivi) {
                tmp.set(x, y);
                tmp2.set(tmp).add(väli / 2, väli / 2);
                c.drawRectangle(
                        tmp,
                        tmp2,
                        ikkuna ? CoreColor.DimGray : CoreColor.Yellow,
                        true);
                x += väli;
            }
            y += väli;
        }

        tmp.set(0,0);
        tmp2.set(width,height);

        c.drawRectangle(tmp, tmp2, CoreColor.Blue.dissolve(1.0-shade/2.0), true);

        SnapshotParameters parameters;
        parameters = new SnapshotParameters();
        data = c.peer().snapshot(parameters, data);
    }
}