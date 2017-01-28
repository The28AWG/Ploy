package io.github.the28awg.ploy.experiential.io;

import java.io.IOException;
import java.io.InputStream;

import io.github.the28awg.ploy.experiential.Platform;

/**
 * Created by the28awg on 24.12.16.
 */

public abstract class IO {

    private static final Object lock = new Object();
    private static IO INSTANCE;

    public static IO get() {
        synchronized (lock) {
            if (INSTANCE == null && Platform.current() == Platform.ANDROID) {
                try {
                    Class clazz = Class.forName("io.github.the28awg.ploy.platform.android.io.IO");
                    INSTANCE = IO.class.cast(clazz.newInstance());
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException(e);
                } catch (InstantiationException e) {
                    throw new RuntimeException(e);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return INSTANCE;
    }

    public abstract InputStream stream(String file) throws IOException;

    public abstract String string(String file) throws IOException;
}
