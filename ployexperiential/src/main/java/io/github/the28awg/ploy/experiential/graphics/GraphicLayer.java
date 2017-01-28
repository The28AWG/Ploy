package io.github.the28awg.ploy.experiential.graphics;

public interface GraphicLayer {

    String tag();

    void init(GraphicLayer parent);

    void tick();

    void enter(GraphicLayer parent);

    void leave(GraphicLayer parent);
}