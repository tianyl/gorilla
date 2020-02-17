package fi.utu.tech.distributed.gorilla.objects;

import fi.utu.tech.distributed.gorilla.AssetManager;
import fi.utu.tech.distributed.gorilla.engine.Engine;
import fi.utu.tech.distributed.gorilla.engine.ProxyGameObject;
import fi.utu.tech.distributed.gorilla.logic.Player;
import fi.utu.tech.oomkit.canvas.Canvas;
import fi.utu.tech.oomkit.canvas.Point2D;
import fi.utu.tech.oomkit.colors.CoreColor;
import javafx.scene.image.Image;

public class Gorilla extends ImageGameObject  {
    private final Player player;
    private final Image deadImage = AssetManager.getInstance().getImage("/tombstone.png");
    final transient private Point2D tmp = new Point2D();
    final transient private Point2D tmp2 = new Point2D();
    final transient private Point2D launchPos = new Point2D();

    public Gorilla(Engine engine, Point2D position, Player player) {
        super(engine, new Point2D(), position, 1, player.local ? "/monkey3.png" : "/monkey.png");
        this.player = player;
        place(position);
        player.setLaunchPosition(getLaunchPosition());
        zOrder = 1;
    }

    public void place(Point2D position) {
        this.position.set(position);
        this.position.sub(img.getWidth() / 2, img.getHeight());
    }

    @Override
    protected boolean collideWith(ProxyGameObject other) {
        if (other instanceof SceneBorder || other instanceof Building) {
            movable = false;
        }
        // kill the gorilla if hit by a lethal banana
        if (other instanceof Banana) {
            boolean wasAlive = player.alive;

            Banana b = (Banana)other;
            if (b.lethal()) {
                player.alive = false;
                b.deactivate();
            }

            // remove the tombstone if hit
            return !wasAlive;
        }

        return false;
    }

    private Point2D getLaunchPosition() {
        launchPos.set(getPosition());
        launchPos.add(img.getWidth() / 2, img.getHeight() / 3);
        Point2D bananaForm = new Banana(engine, null, null, 0, null, 0, null).getForm();
        launchPos.sub(bananaForm.x/2, bananaForm.y/2);
        return launchPos;
    }

    @Override
    public void draw(Canvas canvas, Point2D trans) {
        if (player.angle != -1 && player.alive) {
            tmp.set(launchPos).sub(trans);
            tmp2.dir(-player.angle, 60);
            tmp2.add(tmp);
            canvas.drawLine(tmp, tmp2, CoreColor.White, 2);
        }
        tmp.set(getPosition()).sub(trans);
        if (player.alive) {
            canvas.drawImage(tmp, img);
        } else {
            canvas.drawImage(tmp, deadImage);
        }
    }
}
