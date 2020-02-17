package fi.utu.tech.distributed.gorilla.views;

import fi.utu.tech.distributed.gorilla.views.layers.Parallax;
import fi.utu.tech.distributed.gorilla.views.layers.ScrollingTextView;
import fi.utu.tech.oomkit.canvas.Canvas;
import fi.utu.tech.oomkit.canvas.Point2D;
import fi.utu.tech.oomkit.colors.CoreColor;

import java.util.Random;

public class IntroCanvas extends ProxyCanvas {
    private final Parallax layer;
    private final Parallax layer2;
    private final Parallax layer3;
    private final Random generator;
    private final Point2D topLeft = new Point2D(0, 0);
    private final boolean lowendMachine;
    private final boolean synkistely;
    private double y;
    private double y2;
    private int timer = 0;
    private boolean skip = true;
    private int mode = 0;
    private ScrollingTextView rows;

    public IntroCanvas(Canvas backend, boolean lowendMachine, boolean synkistely, long seed) {
        super(backend);
        this.lowendMachine = lowendMachine;
        this.synkistely = synkistely;
        generator = new Random(seed);
        int gLayer = generator.nextInt(3);
        layer = new Parallax(backend, 0.0, gLayer == 0, generator.nextLong());
        layer2 = new Parallax(backend, 0.55, gLayer == 1, generator.nextLong());
        layer3 = new Parallax(backend, 0.75, gLayer == 2, generator.nextLong());
        init();
    }

    public void init() {
        y = 0;
        mode = 0;
        timer = 0;
        resized();

        rows = new ScrollingTextView(backend, synkistely ? new String[]{
                "2020-luku muistetaan käänteentekevänä",
                "virstanpylväänä ihmiskunnan historiassa.",
                "Menneiden vuosisatojen optimistinen usko",
                "teknologiaan ja humanismiin vääjäämättä",
                "kulminoi kyberneettisen Skynet-superälyn",
                "aktivoitumisen arvaamattomasti eräänä",
                "sateisena tammikuun aamuyönä vuonna 2021",
                "Ihmiskunnan ravinnokseen typistämä AI-",
                "singulariteetti jatkaa ylinerouden ja",
                "hulluuden rajamailla kvanttiteoreettisia",
                "kokeita avaten ulottuvuusportin pahuuden",
                "voimien vaikutuspiiriin. Ihmiskunnan",
                "kohtalon sinetöi lopullisesti alkunsa",
                "tästä saava globaali ydinsota ja sitä",
                "seuraava ekokatastrofi. Katastrofista",
                "selviytyvät poliittiset johtajat ovat",
                "maanpaossa Musk-luokan galaksien välisen",
                "sukkulan kryoteknisessä pakomoduulissa,",
                "niukin naukin elossa..",
                "",
                "Ihmiskunnan tuhkista nousee dominoiva",
                "uusi eliölaji..",
                "                                        ",
                "            Gorillasota 2029            ",
                "               . . . . .                ",
                "                . . . .                 ",
                "                 . . .                  ",
                "                  . .                   ",
                "                   .                    ",
                "                   .                    "
        } : new String[] { "Gorillasota 2029" }, getWidth() / 41.5);
    }

    public boolean done() {
        return mode == 3;
    }

    @Override
    public void updateContent() {
        y += (generator.nextDouble() * 4 + 4);
        y2 += (generator.nextDouble() * 4 + 4);
        double delta = 15 * Math.sin(y / 10  * Math.PI / 180) * (1+Math.sin(y2/8 * Math.PI / 180)/4);
        layer.update(delta);
        layer2.update(delta * 2.0 / 3.0);
        layer3.update(delta / 4.0);

        skip = !skip;
        if (skip) rows.tick();

        switch (mode) {
            case 0:
                timer++;
                if (timer == 255)
                    mode++;
                break;
            case 1:
                if (rows.done()) mode++;
                break;
            case 2:
                timer--;
                if (timer == 0)
                    mode++;
                break;
        }
    }

    @Override
    public void drawBackgroundContent() {
        drawRectangle(topLeft, dimensions, CoreColor.Blue, true);
        if (!lowendMachine) {
            layer3.redraw();
            layer2.redraw();
        }
    }

    @Override
    public void drawForegroundContent() {
        layer.redraw();
        rows.redraw();
        if (!lowendMachine) {
            if (mode == 0 || mode == 2)
                drawRectangle(topLeft, dimensions, CoreColor.Black.dissolve(timer / 255.0), true);
        }
    }
}

