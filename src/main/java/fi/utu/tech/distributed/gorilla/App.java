package fi.utu.tech.distributed.gorilla;

import fi.utu.tech.distributed.gorilla.logic.GameMode;
import fi.utu.tech.distributed.gorilla.logic.GorillaLogic;
import fi.utu.tech.oomkit.app.OOMApp;
import fi.utu.tech.oomkit.controls.Button;
import fi.utu.tech.oomkit.controls.NodeList;
import fi.utu.tech.oomkit.windows.Window;
import javafx.scene.control.Label;
import javafx.stage.Stage;

/**
 * Few things worth noting here:
 *   - 'appLogic' will be initialized just before App is constructed (called by the main() thread)
 *   - 'appLogic' has uninitialized fields pointing to GUI elements at this point
 *
 *   - at this point the main() thread will block, waiting for the JavaFX thread to finish
 *   - thus, let's focus on the newly creating JavaFX thread, which becomes our foreground thread
 *   - 'App' will first call appLogic.getCanvas and appLogic.configuration when constructing the GUI (called by the JavaFX thread)
 *   - 'App' will later call appLogic.initialize when the GUI is ready (called by the JavaFX thread)
 *
 *   - at this point the 'App' has become active
 *   - when the 'App' is active, it will periodically call appLogic.tick() (called by the JavaFX thread)
 *   - when the 'App' is active and a key has been pressed, it will call appLogic.handleKey() (called by the JavaFX thread)
 *   - when the 'App' is active and a button (see below) has been pressed, it will call appLogic.XXX (see below) (called by the JavaFX thread)
 *
 *   A simplified representation of the game loop looks like:
 *   while(true) {
 *       appLogic.tick();
 *       if (system.keys_pressed) appLogic.handleKey();
 *       if (system.gui_buttons_pressed) appLogic.XXX();
 *       wait(a_short_duration);
 *   }
 *
 *   The actual rendering to screen occurs in a JavaFX background thread. However, all delays in our
 *   foreground JavaFX thread may cause stuttering or dropped frames (that is, something will be
 *   rendered, but our rendering commands will miss the deadline and something partial will be drawn
 *   instead).
 */
public class App extends OOMApp {
    final static GorillaLogic appLogic = new GorillaLogic();

    @Override
    protected Window generateMainWindow(Stage stage, String appName, double width, double height) {
        return new SimpleMainWindow(stage, appName, width < 1024 ? width : Math.min(width * 0.91, 1920), height < 768 ? height : Math.min(height * 0.91, 1080)) {
            @Override
            public NodeList bottomBarContent() {
                return new NodeList(
                        new Label("Some examples:"),
                        new Button("Intro", e -> appLogic.setMode(GameMode.Intro)),
                        new Button("Menu", e -> appLogic.setMode(GameMode.Menu)),
                        new Button("Game", e -> appLogic.setMode(GameMode.Game)),
                        new Button("<<", e -> appLogic.views.addVelocity(-5)),
                        new Button("=", e -> appLogic.views.setVelocity(0)),
                        new Button("0", e -> appLogic.views.focusOnMe()),
                        new Button(">>", e -> appLogic.views.addVelocity(5))
                ).cat(basicButtons());
            }
        };
    }

    /**
     * Override if you need to execute stuff using the main() thread before the GUI launches.
     */
    @Override
    public void init() {
        report("javafx.runtime.version: " + System.getProperties().get("javafx.runtime.version"));
        report("java version: " + System.getProperty("java.version"));
    }

    public App() {
        super(appLogic);
    }
}
