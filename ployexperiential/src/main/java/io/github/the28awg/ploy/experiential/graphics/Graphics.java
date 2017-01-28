package io.github.the28awg.ploy.experiential.graphics;

import io.github.the28awg.ploy.experiential.Platform;
import io.github.the28awg.ploy.experiential.bus.Subscribe;
import io.github.the28awg.ploy.experiential.events.WindowDimensionChange;
import io.github.the28awg.ploy.experiential.events.WindowOffsetChange;
import io.github.the28awg.ploy.experiential.events.WindowScaleChange;
import io.github.the28awg.ploy.experiential.geom.Dimension;
import io.github.the28awg.ploy.experiential.geom.XY;
import io.github.the28awg.ploy.experiential.io.Disposable;

/**
 * Created by the28awg on 24.12.16.
 */

public abstract class Graphics implements Disposable {


    protected String tag;


    protected abstract void init(Builder builder);

    public abstract void enable();

    public abstract void disable();

    public void dispose() {
        Platform.bus().disable(this);
    }

    @Subscribe
    private void window_dimension_change(WindowDimensionChange window_dimension_change) {
        resize(window_dimension_change.dimension());
    }

    protected void resize(Dimension dimension) {

    }

    @Subscribe
    private void window_scale_change(WindowScaleChange window_scale_change) {
        scale(window_scale_change.scale());
    }

    @Subscribe
    private void window_offset_change(WindowOffsetChange window_offset_change) {
        offset(window_offset_change.offset());
    }

    protected void scale(float scale) {
    }

    protected void offset(XY xy) {
    }

    public abstract void push(XY xy, Tile tile);

    public abstract void push(float x, float y, Tile tile);

    public abstract void push(XY xy, Texture texture);

    public abstract void push(float x, float y, Texture texture);

    public abstract void push(XY xy, Dimension dimension, Tile tile);

    public abstract void push(float x, float y, float width, float height, Tile tile);

    public abstract void push(XY xy, Dimension dimension, Texture texture);

    public abstract void push(float x, float y, float width, float height, Texture texture);

    public String tag() {
        return tag;
    }

    public static class Builder {
        private String tag;

        public Builder(String tag) {
            this.tag = tag;
        }

        public String tag() {
            return tag;
        }

        public Graphics build() {
            if (tag == null || tag.isEmpty()) {
                throw new RuntimeException("tag == null || tag.isEmpty()");
            }
            if (Platform.current() == Platform.ANDROID) {
                try {
                    Class clazz = Class.forName("io.github.the28awg.ploy.platform.android.graphics.Graphics");
                    Graphics graphics = Graphics.class.cast(clazz.newInstance());
                    Platform.bus().enable(graphics);
                    graphics.init(this);
                    return graphics;
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException(e);
                } catch (InstantiationException e) {
                    throw new RuntimeException(e);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }
            throw new RuntimeException("Unknown platform!");
        }
    }
}
