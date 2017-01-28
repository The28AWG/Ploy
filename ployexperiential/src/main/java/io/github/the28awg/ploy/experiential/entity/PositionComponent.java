package io.github.the28awg.ploy.experiential.entity;

import io.github.the28awg.ploy.experiential.geom.XY;

/**
 * Created by the28awg on 18.01.17.
 */

public class PositionComponent extends XY implements Component {
    public PositionComponent() {
        super();
    }

    public PositionComponent(float x, float y) {
        super(x, y);
    }

    public PositionComponent(XY xy) {
        super(xy.x(), xy.y());
    }

}
