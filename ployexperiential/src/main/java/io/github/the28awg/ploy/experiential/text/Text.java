package io.github.the28awg.ploy.experiential.text;

import android.graphics.Color;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.github.the28awg.ploy.experiential.geom.Dimension;
import io.github.the28awg.ploy.experiential.graphics.Graphics;
import io.github.the28awg.ploy.experiential.text.fonts.Glyph;
import io.github.the28awg.ploy.experiential.text.fonts.GlyphFactory;
import io.github.the28awg.ploy.experiential.text.fonts.GlyphManager;

/**
 * Created by the28awg on 26.01.17.
 */

public class Text {

    private static final Pattern WORD = Pattern.compile("[a-zA-Zа-яА-Я]+");
    private static final String LINES = "\r\n|\r|\n";

    public static void text(float x, float y, Graphics graphics, GlyphFactory factory, String text) {
        float offsetX = 0;
        float offsetY = 0;
        for (String line : text.split("\n")) {
            float tmp_h = 0;
            Matcher word = WORD.matcher(line);
            while (word.find()) {
                Dimension dimension = draw_chars(x + offsetX, y + offsetY, graphics, factory, word.group(0).toCharArray());
                offsetX += dimension.width() + 10;
                tmp_h = Math.max(tmp_h, dimension.height());
            }
            offsetY -= tmp_h;
            offsetX = 0;
        }
    }

    private static Dimension draw_chars(float x, float y, Graphics graphics, GlyphFactory factory, char[] line_chars) {
        float width = 0;
        float height = 0;
        for (char line_char : line_chars) {
            Glyph glyph = GlyphManager.glyph(line_char, new Style().foreground(Color.BLUE).italic(true), 30, factory);
            if (glyph != null) {
                height = Math.max(height, glyph.rectangle().height());
                graphics.push(x + width, y - glyph.rectangle().bottom(), glyph.texture());
                width += glyph.rectangle().width() + 2;
            }
        }
        return new Dimension(width, height);
    }

    public static void text2(String text) {
        System.out.println("words");
        Matcher matcher = WORD.matcher(text);
        while (matcher.find()) {
            System.out.println("Full match: " + matcher.group(0));
        }
        System.out.println("lines");
        String[] lines = text.split(LINES);

    }
}
