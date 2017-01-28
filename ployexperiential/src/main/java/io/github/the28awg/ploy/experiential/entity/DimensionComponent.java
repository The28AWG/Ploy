package io.github.the28awg.ploy.experiential.entity;

import io.github.the28awg.ploy.experiential.geom.Dimension;

/**
 * Created by the28awg on 18.01.17.
 */

public class DimensionComponent extends Dimension implements Component {

    public DimensionComponent() {
    }

    public DimensionComponent(float width, float height) {
        super(width, height);
    }
}
