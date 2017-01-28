package io.github.the28awg.ploy.experiential;

import io.github.the28awg.ploy.experiential.bus.Bus;
import io.github.the28awg.ploy.experiential.events.WindowOffsetChange;
import io.github.the28awg.ploy.experiential.events.WindowScaleChange;
import io.github.the28awg.ploy.experiential.geom.Dimension;
import io.github.the28awg.ploy.experiential.geom.XY;

/**
 * Created by the28awg on 24.12.16.
 */

public class Platform {
    public static final boolean DEBUG = true;
    public static final int UNKNOWN = 0;
    public static final int ANDROID = 1;
    private static final Bus BUS = new Bus();
    private static int current = UNKNOWN;
    private static Dimension dimension = new Dimension();
    private static int fps = 0;
    private static float scale = 1.00f;
    private static XY offset = new XY();

    public static Bus bus() {
        return BUS;
    }

    public static void dimension(Dimension dimension) {
        Platform.dimension = dimension;
    }

    public static Dimension dimension() {
        return dimension;
    }

    public static int fps() {
        return fps;
    }

    public static void fps(int fps) {
        Platform.fps = fps;
    }

    public static float scale() {
        return scale;
    }

    public static void scale(float scale) {
        Platform.scale = scale;
        bus().post(new WindowScaleChange(scale));
    }

    public static void offset(float x, float y) {
        offset(new XY(x, y));
    }

    public static void offsetX(float x) {
        offset(offset().x(x));
    }

    public static void offsetY(float y) {
        offset(offset().y(y));
    }

    public static void offset(XY xy) {
        offset.xy(xy);
        bus().post(new WindowOffsetChange(offset));
    }

    public static XY offset() {
        return offset;
    }

    public static int current() {
        if (current == UNKNOWN) {
            String name = System.getProperty("java.vm.name");
            if (name != null && name.equals("Dalvik")) {
                current = ANDROID;
            }
        }
        return current;
    }
}
