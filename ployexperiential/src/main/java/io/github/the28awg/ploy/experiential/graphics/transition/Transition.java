package io.github.the28awg.ploy.experiential.graphics.transition;

import io.github.the28awg.ploy.experiential.graphics.GraphicLayer;
import io.github.the28awg.ploy.experiential.graphics.GraphicLayerGroup;

public interface Transition extends GraphicLayer {

    void preRender(GraphicLayerGroup group);

    void postRender(GraphicLayerGroup group);

    boolean isComplete();

    void init(GraphicLayer first_layer, GraphicLayer second_layer);
}
