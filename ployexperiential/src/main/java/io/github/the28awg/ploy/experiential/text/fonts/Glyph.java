package io.github.the28awg.ploy.experiential.text.fonts;

import io.github.the28awg.ploy.experiential.geom.Rectangle;
import io.github.the28awg.ploy.experiential.graphics.Texture;
import io.github.the28awg.ploy.experiential.text.Style;

/**
 * Created by the28awg on 25.01.17.
 */

public class Glyph {

    private Rectangle rectangle;
    private Style style;
    private char glyph_char;
    private Texture texture;
    private int size;

    public Glyph(Rectangle rectangle, Style style, char glyph_char, Texture texture, int size) {
        this.rectangle = rectangle;
        this.style = style;
        this.glyph_char = glyph_char;
        this.texture = texture;
        this.size = size;
    }

    public Rectangle rectangle() {
        return rectangle;
    }

    public Style style() {
        return style;
    }

    public char glyph_char() {
        return glyph_char;
    }

    public Texture texture() {
        return texture;
    }

    public int size() {
        return size;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Glyph glyph = (Glyph) o;
        return glyph_char == glyph.glyph_char && size == glyph.size && rectangle.equals(glyph.rectangle) && style.equals(glyph.style) && texture.equals(glyph.texture);

    }

    @Override
    public int hashCode() {
        int result = rectangle.hashCode();
        result = 31 * result + style.hashCode();
        result = 31 * result + (int) glyph_char;
        result = 31 * result + texture.hashCode();
        result = 31 * result + size;
        return result;
    }

    @Override
    public String toString() {
        return "Glyph{" +
                "rectangle=" + rectangle +
                ", style=" + style +
                ", glyph_char=" + glyph_char +
                ", texture=" + texture +
                ", size=" + size +
                '}';
    }
}
