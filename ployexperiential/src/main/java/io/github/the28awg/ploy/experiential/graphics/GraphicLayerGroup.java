package io.github.the28awg.ploy.experiential.graphics;

import java.util.HashMap;
import java.util.Iterator;

import io.github.the28awg.ploy.experiential.graphics.transition.EmptyTransition;
import io.github.the28awg.ploy.experiential.graphics.transition.Transition;

public abstract class GraphicLayerGroup implements GraphicLayer {

    private HashMap<String, Object> layers;
    private GraphicLayer current_graphic_layer;
    private GraphicLayer next_graphic_layer;
    private Transition enterTransition;
    private Transition leaveTransition;

    public GraphicLayerGroup() {
        layers = new HashMap<>();
        current_graphic_layer = new GraphicLayer() {
            @Override
            public String tag() {
                return "null";
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
        };
    }

    public int count() {
        return layers.keySet().size();
    }

    public String current_tag() {
        return current_graphic_layer.tag();
    }

    public GraphicLayer current_graphic_layer() {
        return current_graphic_layer;
    }

    public GraphicLayerGroup add(GraphicLayer... layers) {
        for (GraphicLayer layer : layers) {
            this.layers.put(layer.tag(), layer);
            if (current_graphic_layer.tag().equals("null")) {
                current_graphic_layer = layer;
            }
        }
        return this;
    }

    public GraphicLayer get(String tag) {
        return (GraphicLayer) layers.get(tag);
    }


    public void enter_layer(String tag) {
        enter_layer(tag, new EmptyTransition(), new EmptyTransition());
    }

    public void enter_layer(String tag, Transition leave, Transition enter) {
        if (leave == null) {
            leave = new EmptyTransition();
        }
        if (enter == null) {
            enter = new EmptyTransition();
        }
        leaveTransition = leave;
        enterTransition = enter;

        next_graphic_layer = get(tag);
        if (next_graphic_layer == null) {
            throw new RuntimeException("No graphic layer registered with the tag: " + tag);
        }

        leaveTransition.init(current_graphic_layer, next_graphic_layer);

    }

    public abstract void init_layers();

    @Override
    public final void init(GraphicLayer parent) {
        init_layers();

        Iterator iterator = layers.values().iterator();

        while (iterator.hasNext()) {
            GraphicLayer layer = (GraphicLayer) iterator.next();

            layer.init(this);
        }

        if (current_graphic_layer != null) {
            current_graphic_layer.enter(this);
        }
    }

    @Override
    public String tag() {
        return null;
    }

    @Override
    public final void tick() {

        if (leaveTransition != null) {
            leaveTransition.tick();
            if (leaveTransition.isComplete()) {
                current_graphic_layer.leave(this);
                GraphicLayer prev_graphic_layer = current_graphic_layer;
                current_graphic_layer = next_graphic_layer;
                next_graphic_layer = null;
                leaveTransition = null;
                if (enterTransition == null) {
                    current_graphic_layer.enter(this);
                } else {
                    enterTransition.init(current_graphic_layer, prev_graphic_layer);
                }
            } else {
                return;
            }
        }

        if (enterTransition != null) {
            enterTransition.tick();
            if (enterTransition.isComplete()) {
                current_graphic_layer.enter(this);
                enterTransition = null;
            } else {
                return;
            }
        }
        if (leaveTransition != null) {
            leaveTransition.preRender(this);
        } else if (enterTransition != null) {
            enterTransition.preRender(this);
        }

        preRenderLayer();
        current_graphic_layer.tick();
        postRenderLayer();

        if (leaveTransition != null) {
            leaveTransition.postRender(this);
        } else if (enterTransition != null) {
            enterTransition.postRender(this);
        }
    }

    @Override
    public void enter(GraphicLayer parent) {

    }

    @Override
    public void leave(GraphicLayer parent) {
        current_graphic_layer.leave(this);
    }

    protected abstract void postRenderLayer();

    protected abstract void preRenderLayer();
}
