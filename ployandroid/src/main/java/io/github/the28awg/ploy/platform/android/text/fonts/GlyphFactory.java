package io.github.the28awg.ploy.platform.android.text.fonts;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.text.TextPaint;
import android.util.TypedValue;

import io.github.the28awg.ploy.experiential.geom.Rectangle;
import io.github.the28awg.ploy.experiential.text.Style;
import io.github.the28awg.ploy.experiential.text.fonts.Glyph;
import io.github.the28awg.ploy.platform.android.PloyActivity;
import io.github.the28awg.ploy.platform.android.graphics.Texture;

/**
 * Created by the28awg on 26.01.17.
 */

public class GlyphFactory extends io.github.the28awg.ploy.experiential.text.fonts.GlyphFactory {

    private Typeface typeface;
    private TextPaint paint = new TextPaint(Paint.ANTI_ALIAS_FLAG);

    @Override
    protected void init(String name) {
        this.name = name;
        typeface = Typeface.createFromAsset(PloyActivity.context().getAssets(), ("fonts/" + name));
        paint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
    }

    @Override
    public Glyph glyph(char c, Style style, int size) {
        paint.setTextSize(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, size, PloyActivity.context().getResources().getDisplayMetrics()));
        paint.setTypeface(typeface);
        if (style.style() > 0) {
            int typefaceStyle = typeface != null ? typeface.getStyle() : 0;
            int need = style.style() & ~typefaceStyle;
            paint.setFakeBoldText((need & Typeface.BOLD) != 0);
            paint.setTextSkewX((need & Typeface.ITALIC) != 0 ? -0.25f : 0);
        }
        if (style.underline()) {
            paint.setUnderlineText(true);
        }
        Rect rect = new Rect();
        paint.getTextBounds(new char[]{c}, 0, 1, rect);
        System.out.println(rect.toString());
        Rectangle rectangle = new Rectangle(rect.left, rect.top, rect.right, rect.bottom);
//        Dimension dimension = new Dimension(rect.width(), rect.height());
        System.out.println(rectangle);
        paint.setColor(style.foreground());
        System.out.println("char: \"" + c + "\"");
        if (c == ' ') {

        }
        try {
            Bitmap bitmap = Bitmap.createBitmap((int) rectangle.width(), (int) rectangle.height(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            paint.setColor(style.foreground());
            canvas.drawText(new char[]{c}, 0, 1, -rect.left, -rect.top, paint);
            return new Glyph(rectangle, style, c, new Texture(bitmap), size);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException(e);
        }
    }
}
