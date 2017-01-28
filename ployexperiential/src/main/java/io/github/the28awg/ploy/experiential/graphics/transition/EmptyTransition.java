package io.github.the28awg.ploy.experiential.graphics.transition;

import io.github.the28awg.ploy.experiential.graphics.GraphicLayer;
import io.github.the28awg.ploy.experiential.graphics.GraphicLayerGroup;

/**
 * Created by the28awg on 26.12.16.
 */

public class EmptyTransition implements Transition {
    @Override
    public void preRender(GraphicLayerGroup group) {

    }

    @Override
    public void postRender(GraphicLayerGroup group) {

    }

    @Override
    public boolean isComplete() {
        return true;
    }

    @Override
    public void init(GraphicLayer first_layer, GraphicLayer second_layer) {

    }

    @Override
    public String tag() {
        return "empty";
    }

    @Override
    public void init(GraphicLayer parent) {

    }

    @Override
    public void tick() {

    }

    @Override
    public void enter(GraphicLayer parent) {

    }

    @Override
    public void leave(GraphicLayer parent) {

    }
}
