package io.github.the28awg.ploy.experiential.text;

import java.util.HashMap;
import java.util.Map;

import io.github.the28awg.ploy.experiential.syntax.Syntax;

/**
 * Created by the28awg on 25.01.17.
 */

public class ThemeManager {

    private static final Map<String, Theme> THEME_CACHE = new HashMap<>();

    static {
        Theme default_theme = new Theme(new Style(), Style.BLACK, Style.TRANSPARENT, Style.BLACK, Style.TRANSPARENT, "DroidSansMono.ttf", 24);
        Style style;
        style = new Style();
        style.foreground(0xFF008800);
        default_theme.style(Syntax.PR_STRING, style);
        style = new Style();
        style.foreground(0xFF111111);
        default_theme.style(Syntax.PR_KEYWORD, style);
        style = new Style();
        style.foreground(0xFF880000);
        default_theme.style(Syntax.PR_COMMENT, style);
        style = new Style();
        style.foreground(0xFF660066);
        default_theme.style(Syntax.PR_TYPE, style);
        style = new Style();
        style.foreground(0xFF006666);
        default_theme.style(Syntax.PR_LITERAL, style);
        style = new Style();
        style.foreground(0xFF666600);
        default_theme.style(Syntax.PR_PUNCTUATION, style);
        style = new Style();
        style.foreground(0xFF660066);
        default_theme.style(Syntax.PR_ANNOTATION, style);
        THEME_CACHE.put("default", default_theme);
    }

    public static Theme get(String name) {
        return THEME_CACHE.get(name);
    }

    public static void register(String name, Theme theme) {
        THEME_CACHE.put(name, theme);
    }
}
