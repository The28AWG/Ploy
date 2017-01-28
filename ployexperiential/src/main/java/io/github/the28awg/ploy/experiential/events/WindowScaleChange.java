package io.github.the28awg.ploy.experiential.events;

import io.github.the28awg.ploy.experiential.bus.Event;

/**
 * Created by the28awg on 18.01.17.
 */

@Event
public class WindowScaleChange {

    private final float scale;

    public WindowScaleChange(float scale) {
        this.scale = scale;
    }

    public float scale() {
        return scale;
    }
}
