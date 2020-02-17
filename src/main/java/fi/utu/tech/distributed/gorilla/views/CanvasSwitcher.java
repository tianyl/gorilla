package fi.utu.tech.distributed.gorilla.views;

import fi.utu.tech.oomkit.canvas.Canvas;

import java.util.Map;

public class CanvasSwitcher<C> extends ProxyCanvas {
    private final Map<C, ProxyCanvas> mappings;
    private ProxyCanvas active;

    public CanvasSwitcher(Canvas backend, Map<C, ProxyCanvas> mappings) {
        super(backend);
        this.mappings = mappings;
    }

    public void switchView(C option) {
        active = option == null ? null : mappings.getOrDefault(option, null);
    }

    @Override
    public void updateContent() {
        if (active != null) active.updateContent();
    }

    @Override
    public void drawBackgroundContent() {
        if (active != null) active.drawBackgroundContent();
    }

    @Override
    public void drawForegroundContent() {
        if (active != null) active.drawForegroundContent();
    }
}