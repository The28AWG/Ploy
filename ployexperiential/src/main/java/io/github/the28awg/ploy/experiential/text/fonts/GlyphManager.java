package io.github.the28awg.ploy.experiential.text.fonts;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import io.github.the28awg.ploy.experiential.text.Style;

public class GlyphManager {

    private static final List<Glyph> GLYPHS = new CopyOnWriteArrayList<>();

    public static Glyph glyph(char c, Style style, int size, GlyphFactory factory) {
        for (Glyph glyph : GLYPHS) {
            if (glyph.glyph_char() == c && glyph.style().equals(style) && glyph.size() == size) {
                return glyph;
            }
        }
        if (factory != null) {
            Glyph glyph = factory.glyph(c, style, size);
            if (glyph != null) {
                GLYPHS.add(glyph);
            }
            return glyph;
        }
        return null;
    }

    public static boolean glyph(Glyph glyph) {
        return GLYPHS.add(glyph);
    }
}
