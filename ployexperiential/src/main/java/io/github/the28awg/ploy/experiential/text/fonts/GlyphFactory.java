package io.github.the28awg.ploy.experiential.text.fonts;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import io.github.the28awg.ploy.experiential.Platform;
import io.github.the28awg.ploy.experiential.text.Style;

/**
 * Created by the28awg on 25.01.17.
 */

public abstract class GlyphFactory {

    private static final List<GlyphFactory> GLYPH_FACTORIES = new CopyOnWriteArrayList<>();
    protected String name;

    public static GlyphFactory factory(String name) {
        for (GlyphFactory factory : GLYPH_FACTORIES) {
            if (factory.name().equals(name)) {
                return factory;
            }
        }
        if (Platform.current() == Platform.ANDROID) {
            try {
                System.out.println("create font");
                Class clazz = Class.forName("io.github.the28awg.ploy.platform.android.text.fonts.GlyphFactory");
                GlyphFactory factory = GlyphFactory.class.cast(clazz.newInstance());
                factory.init(name);
                GLYPH_FACTORIES.add(factory);
                System.out.println("done");
                return factory;
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            } catch (InstantiationException e) {
                throw new RuntimeException(e);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
        throw new RuntimeException("error");
    }

    public String name() {
        return name;
    }

    protected abstract void init(String name);

    public abstract Glyph glyph(char c, Style style, int size);
}
