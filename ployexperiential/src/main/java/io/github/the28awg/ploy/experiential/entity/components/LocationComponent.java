package io.github.the28awg.ploy.experiential.entity.components;

import io.github.the28awg.ploy.experiential.entity.Component;
import io.github.the28awg.ploy.experiential.geom.XY;

/**
 * Created by the28awg on 20.01.17.
 */

public class LocationComponent extends XY implements Component {

    public LocationComponent() {
    }

    public LocationComponent(float x, float y) {
        super(x, y);
    }

    public LocationComponent(XY xy) {
        super(xy);
    }
}
