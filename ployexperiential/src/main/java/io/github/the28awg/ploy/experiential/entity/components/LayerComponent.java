package io.github.the28awg.ploy.experiential.entity.components;

import io.github.the28awg.ploy.experiential.entity.Component;

/**
 * Created by the28awg on 19.01.17.
 */

public class LayerComponent implements Component {
    private LayerLevelStrategy layerLevelStrategy;

    public LayerComponent(LayerLevelStrategy layerLevelStrategy) {
        this.layerLevelStrategy = layerLevelStrategy;
    }

    public LayerComponent() {
        this.layerLevelStrategy = LayerLevelStrategy.FORGROUND;
    }

    public LayerLevelStrategy layerLevelStrategy() {
        return layerLevelStrategy;
    }

    public LayerComponent layerLevelStrategy(LayerLevelStrategy layerLevelStrategy) {
        this.layerLevelStrategy = layerLevelStrategy;
        return this;
    }

    public enum LayerLevelStrategy {
        BACKGROUND, FORGROUND, UI
    }
}
