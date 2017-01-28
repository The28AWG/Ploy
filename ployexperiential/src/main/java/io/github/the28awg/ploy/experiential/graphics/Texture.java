package io.github.the28awg.ploy.experiential.graphics;

import io.github.the28awg.ploy.experiential.Platform;
import io.github.the28awg.ploy.experiential.io.Disposable;

/**
 * Created by the28awg on 24.12.16.
 */

public abstract class Texture implements Disposable {

    public static final int NONE = 0;
    protected int texture = NONE;
    protected int width;
    protected int height;
    protected Filter filter;
    protected Wrap wrap;

    public int texture() {
        return texture;
    }

    public int width() {
        return width;
    }

    public int height() {
        return height;
    }

    public Filter filter() {
        return filter;
    }

    public Wrap wrap() {
        return wrap;
    }

    protected abstract void init(Builder builder);

    public abstract void enable();

    public abstract void disable();

    @Override
    public String toString() {
        return "[id = " + texture + ", width = " + width + ", height = " + height + ", filter = " + filter + ", wrap = " + wrap + "]";
    }

    public enum Filter {
        NEAREST, LINEAR
    }

    public enum Wrap {
        CLAMP_TO_EDGE, REPEAT
    }

    public static class Builder {

        private String file;
        private Filter filter;
        private Wrap wrap;
        private int width;
        private int height;

        public Builder() {
        }

        public Builder(String file) {
            this.file = file;
        }

        public Builder(int width, int height) {
            this.width = width;
            this.height = height;
        }

        public String file() {
            return file;
        }

        public Builder file(String file) {
            this.file = file;
            return this;
        }

        public Filter filter() {
            return filter;
        }

        public Builder filter(Filter filter) {
            this.filter = filter;
            return this;
        }

        public Wrap wrap() {
            return wrap;
        }

        public Builder wrap(Wrap wrap) {
            this.wrap = wrap;
            return this;
        }

        public int width() {
            return width;
        }

        public Builder width(int width) {
            this.width = width;
            return this;
        }

        public int height() {
            return height;
        }

        public Builder height(int height) {
            this.height = height;
            return this;
        }

        public Texture build() {
            if (file == null && width == NONE && height == NONE) {
                throw new RuntimeException("file == null");
            }
            if (filter == null) {
                filter = Filter.NEAREST;
            }
            if (wrap == null) {
                wrap = Wrap.REPEAT;
            }
            if (Platform.current() == Platform.ANDROID) {
                try {
                    Class clazz = Class.forName("io.github.the28awg.ploy.platform.android.graphics.Texture");
                    Texture texture = Texture.class.cast(clazz.newInstance());
                    texture.init(this);
                    return texture;
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
