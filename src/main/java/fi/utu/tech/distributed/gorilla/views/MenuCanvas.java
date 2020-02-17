package fi.utu.tech.distributed.gorilla.views;

import fi.utu.tech.distributed.gorilla.engine.SimpleEngine;
import fi.utu.tech.distributed.gorilla.logic.Move;
import fi.utu.tech.distributed.gorilla.logic.Player;
import fi.utu.tech.distributed.gorilla.objects.Gorilla;
import fi.utu.tech.distributed.gorilla.views.layers.Parallax;
import fi.utu.tech.distributed.gorilla.views.layers.ScrollingTextView;
import fi.utu.tech.distributed.gorilla.views.layers.TextView;
import fi.utu.tech.oomkit.canvas.Canvas;
import fi.utu.tech.oomkit.canvas.Point2D;
import fi.utu.tech.oomkit.colors.CoreColor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class MenuCanvas extends ProxyCanvas {
    private final Parallax layer3;
    private final Point2D topLeft = new Point2D(0, 0);
    private final Point2D tmp = new Point2D(0, 0);
    private final Point2D gorillaSize = new Point2D(0, 0);
    private boolean lowendMachine;
    private List<String> menu;
    private List<String> info;
    private ScrollingTextView menuText;
    private TextView infoText;
    private int selectedItem = 0;
    private double hScale = 0.8;

    private final Gorilla menuGorilla = new Gorilla(new SimpleEngine(1, 2, 1), new Point2D(), new Player("foo", null, false)) {
        public Move playTurn() {
            return null;
        }

        public void draw(Canvas canvas, Point2D pos) {
            canvas.drawImage(pos, gorillaSize, img);
        }
    };

    public MenuCanvas(Canvas backend, boolean lowendMachine, long seed, String title, String[] menuItems) {
        super(backend);
        this.lowendMachine = lowendMachine;

        setMenu(title, menuItems);
        layer3 = new Parallax(backend, 0.8, false, new Random(seed).nextLong());
        resized();
    }

    @Override
    protected void resized() {
        super.resized();
        if (menuGorilla != null) {
            double s = getHeight() / 11;
            gorillaSize.set(s * menuGorilla.getForm().x / menuGorilla.getForm().y, s);
        }
    }

    public void setMenu(String title, String[] menuItems) {
        menu = new ArrayList<>();
        info = new ArrayList<>();
        menu.add(title);
        menu.add("");
        menu.addAll(Arrays.asList(menuItems));

        menuText = new ScrollingTextView(backend, menu.toArray(new String[]{}), 16) {
            protected double fontSize() {
                return Math.min(Math.max(gorillaSize.y / 2.2, 12), 64);
            }

            @Override
            protected Point2D place(Point2D p) {
                return p.set(24 + gorillaSize.x * 1.5 + p.x * fontSize() * hScale, 40 + fontSize() / 2 + p.y * gorillaSize.y * 1.1);
            }
        };

        setInfo(new String[]{});
    }

    public void setInfo(String[] infoItems) {
        info.clear();
        info.addAll(Arrays.asList(infoItems));

        infoText = new TextView(backend, info.toArray(new String[]{}), 16) {
            protected double fontSize() {
                return Math.min(Math.max(gorillaSize.y / 2.5, 12), 64);
            }

            @Override
            protected Point2D place(Point2D p) {
                return p.set(24 + p.x * fontSize() * hScale, 40 + menuText.rows.length * gorillaSize.y * 1.1 + fontSize() + p.y * fontSize());
            }
        };
    }

    @Override
    public void updateContent() {
        layer3.update(0.5);
        menuText.tick();
    }

    @Override
    public void drawBackgroundContent() {
        drawRectangle(topLeft, dimensions, CoreColor.Blue, true);
        if (!lowendMachine) {
            layer3.redraw();
        }
    }

    public void setSelected(int selectedItem) {
        this.selectedItem = selectedItem;
    }

    @Override
    public void drawForegroundContent() {
        menuText.drawForegroundContent();
        if (menuText.done()) infoText.drawForegroundContent();
        tmp.set(24, (1 + selectedItem) * (gorillaSize.y * 1.1) + Math.min(Math.max(gorillaSize.y / 2.2, 12), 64) + 40);
        menuGorilla.draw(this, tmp);
    }
}
