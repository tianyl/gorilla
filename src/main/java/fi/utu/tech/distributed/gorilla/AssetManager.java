package fi.utu.tech.distributed.gorilla;

import fi.utu.tech.oomkit.util.ResourceLoader;
import javafx.scene.image.Image;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class AssetManager {
    private static AssetManager instance;
    private static final Object lock = new Object();

    public static AssetManager getInstance() {
        synchronized (lock) {
            return instance = (instance == null ? new AssetManager() : instance);
        }
    }

    private final Map<String, Image> cache = new HashMap<>();

    protected Image loadImage(String imgFile) {
        try {
            return ResourceLoader.loadImage(ResourceLoader.findResource(imgFile));
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
            return null;
        }
    }

    public Image getImage(String name) {
        cache.putIfAbsent(name, loadImage(name));
        return cache.get(name);
    }
}