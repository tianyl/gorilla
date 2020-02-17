package fi.utu.tech.distributed.gorilla.views.layers;

import fi.utu.tech.distributed.gorilla.views.ProxyCanvas;
import fi.utu.tech.oomkit.canvas.Canvas;
import fi.utu.tech.oomkit.canvas.Point2D;
import fi.utu.tech.oomkit.colors.CoreColor;

public class TextView extends ProxyCanvas {
    private final double fontSize;
    public String[] rows;
    private double originalWidth;

    public TextView(Canvas backend, String[] rows, double fontSize) {
        super(backend);
        this.rows = rows;
        this.fontSize = fontSize;
        originalWidth = getWidth();
    }

    protected char charAt(int x, int y) {
        if (y < rows.length) {
            String rivi = rows[y];
            if (x < rivi.length())
                return rivi.charAt(x);
        }
        return ' ';
    }

    protected double fontSize() {
        return fontSize * getWidth() / originalWidth;
    }

    protected int rowCount() {
        return (int) (getHeight() / fontSize()) - 1;
    }

    protected int colCount() {
        return (int) (getWidth() / fontSize()) - 1;
    }

    protected Point2D place(Point2D p) {
        double f = (int) fontSize();
        return p.set(f + f / 2.0 + p.x * f, 2.0 * f + f / 4.0 + p.y * f);
    }

    protected void drawCharacter(Point2D p, char l) {
        Point2D paikka = place(p);
        backend.drawCharacter(paikka, CoreColor.White, l, (int) fontSize(), true, false);
    }

    @Override
    public void drawBackgroundContent() {
    }

    protected Point2D tmp = new Point2D();

    @Override
    public void drawForegroundContent() {
        for (int y = 0; y < rowCount(); y++)
            for (int x = 0; x < colCount(); x++)
                drawCharacter(tmp.set(x, y), charAt(x, y));
    }
}