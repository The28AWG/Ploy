package io.github.the28awg.ploy.experiential.events;

import io.github.the28awg.ploy.experiential.bus.Event;
import io.github.the28awg.ploy.experiential.geom.XY;

/**
 * Created by the28awg on 18.01.17.
 */

@Event
public class WindowOffsetChange {
    private final XY xy;

    public WindowOffsetChange(XY xy) {
        this.xy = xy;
    }

    public XY offset() {
        return xy;
    }
}
