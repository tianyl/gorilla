package fi.utu.tech.distributed.gorilla.views.layers;

import fi.utu.tech.oomkit.app.Scheduled;
import fi.utu.tech.oomkit.canvas.Canvas;
import fi.utu.tech.oomkit.canvas.Point;
import fi.utu.tech.oomkit.canvas.Point2D;

import java.util.LinkedList;
import java.util.Queue;

public class ScrollingTextView extends TextView implements Scheduled {
    protected final Queue<Letter> hiddenLetters = new LinkedList<>();
    protected final Queue<Letter> visibleLetters = new LinkedList<>();
    private int currentLine;

    class Letter extends Point {
        final char value;

        public Letter(int x, int y, char value) {
            super(x, y);
            this.value = value;
        }
    }

    public boolean done() {
        return hiddenLetters.isEmpty();
    }

    public ScrollingTextView(Canvas backend, String[] rows, double size) {
        super(backend, rows, size);
        init();
    }

    public void init() {
        currentLine = -1;
        hiddenLetters.clear();
        visibleLetters.clear();
        for (int y = 0; y < rows.length; y++)
            for (int x = 0; x < rows[y].length(); x++)
                if (charAt(x, y) != ' ') hiddenLetters.add(new Letter(x, y, charAt(x, y)));
    }

    @Override
    protected Point2D place(Point2D p) {
        return super.place(p.add(0, -Math.max(0, currentLine / 2)));
    }

    @Override
    public void tick() {
        if (!hiddenLetters.isEmpty()) {
            Letter current = hiddenLetters.remove();
            visibleLetters.add(current);
            currentLine = current.y;
        }
    }

    @Override
    public void drawForegroundContent() {
        //drawText(tmp, CoreColor.White, "", 1, true, true);
        for(Letter l: visibleLetters) {
            drawCharacter(tmp.set(l.x, l.y), l.value);
        }
    }
}