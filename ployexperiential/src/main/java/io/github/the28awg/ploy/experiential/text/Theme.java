package io.github.the28awg.ploy.experiential.text;

import java.util.HashMap;
import java.util.Map;

public class Theme {

    private Style plain;
    private Map<String, Style> styles;
    private int foreground;
    private int background;

    private int gutter_foreground;
    private int gutter_background;

    private String font;
    private int font_size;

    public Theme(Style plain, Map<String, Style> styles, int foreground, int background, int gutter_foreground, int gutter_background, String font, int font_size) {
        this.plain = plain;
        this.styles = styles;
        this.foreground = foreground;
        this.background = background;
        this.gutter_foreground = gutter_foreground;
        this.gutter_background = gutter_background;
        this.font = font;
        this.font_size = font_size;
    }

    public Theme(Style plain, int foreground, int background, int gutter_foreground, int gutter_background, String font, int font_size) {
        this.plain = plain;
        this.foreground = foreground;
        this.background = background;
        this.gutter_foreground = gutter_foreground;
        this.gutter_background = gutter_background;
        this.font = font;
        this.font_size = font_size;
        this.styles = new HashMap<>();
    }

    public Style plain() {
        return plain;
    }

    public Theme plain(Style plain) {
        this.plain = plain;
        return this;
    }

    public Map<String, Style> styles() {
        return styles;
    }

    public Theme styles(Map<String, Style> styles) {
        this.styles = styles;
        return this;
    }

    public Theme style(String styleKey, Style style) {
        this.styles.put(styleKey, style);
        return this;
    }

    public Style style(String key) {
        Style returnStyle = this.styles.get(key);
        return returnStyle != null ? returnStyle : plain;
    }

    public int foreground() {
        return foreground;
    }

    public Theme foreground(int foreground) {
        this.foreground = foreground;
        return this;
    }

    public int background() {
        return background;
    }

    public Theme background(int background) {
        this.background = background;
        return this;
    }

    public int gutter_foreground() {
        return gutter_foreground;
    }

    public Theme gutter_foreground(int gutter_foreground) {
        this.gutter_foreground = gutter_foreground;
        return this;
    }

    public int gutter_background() {
        return gutter_background;
    }

    public Theme gutter_background(int gutter_background) {
        this.gutter_background = gutter_background;
        return this;
    }

    public String font() {
        return font;
    }

    public Theme font(String font) {
        this.font = font;
        return this;
    }

    public int font_size() {
        return font_size;
    }

    public Theme font_size(int font_size) {
        this.font_size = font_size;
        return this;
    }
}

