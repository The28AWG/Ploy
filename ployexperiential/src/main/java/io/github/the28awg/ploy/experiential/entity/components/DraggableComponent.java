package io.github.the28awg.ploy.experiential.entity.components;

import io.github.the28awg.ploy.experiential.entity.Component;

/**
 * Created by the28awg on 18.01.17.
 */

public class DraggableComponent implements Component {

    private boolean accept;
    private boolean dragged;

    public DraggableComponent() {
        accept = true;
    }

    public DraggableComponent(boolean accept) {
        this.accept = accept;
    }

    public boolean accept() {
        return accept;
    }

    public DraggableComponent accept(boolean accept) {
        this.accept = accept;
        return this;
    }

    public boolean dragged() {
        return dragged;
    }

    public DraggableComponent dragged(boolean dragged) {
        this.dragged = dragged;
        return this;
    }
}
