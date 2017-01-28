package io.github.the28awg.ploy.experiential.events;

import io.github.the28awg.ploy.experiential.bus.Event;
import io.github.the28awg.ploy.experiential.geom.Dimension;

@Event
public class WindowDimensionChange {
    private final Dimension dimension;

    public WindowDimensionChange(Dimension dimension) {
        this.dimension = dimension;
    }

    public Dimension dimension() {
        return dimension;
    }
}
