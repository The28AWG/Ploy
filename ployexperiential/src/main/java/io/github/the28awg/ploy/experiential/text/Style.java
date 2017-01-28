package io.github.the28awg.ploy.experiential.text;

public class Style {

    public static final int BLACK = 0xFF000000;
    public static final int TRANSPARENT = 0;
    public static final int NORMAL = 0;
    public static final int BOLD = 1;
    public static final int ITALIC = 2;
    public static final int BOLD_ITALIC = 3;

    private int style;
    private boolean underline;
    private int foreground;
    private int background;

    public Style() {
        this.style = NORMAL;
        this.underline = false;
        this.foreground = BLACK;
        this.background = TRANSPARENT;
    }

    public Style(int style, boolean underline, int foreground, int background) {
        this.style = style;
        this.underline = underline;
        this.foreground = foreground;
        this.background = background;
    }

    public Style(boolean italic, boolean bold, boolean underline, int foreground, int background) {
        this.underline = underline;
        this.foreground = foreground;
        this.background = background;
        italic(italic);
        bold(bold);
    }

    public Style(int style, boolean underline) {
        this.style = style;
        this.underline = underline;
        this.foreground = BLACK;
        this.background = TRANSPARENT;
    }

    public Style(boolean italic, boolean bold, boolean underline) {
        this.underline = underline;
        this.foreground = BLACK;
        this.background = TRANSPARENT;
        italic(italic);
        bold(bold);
    }

    public Style(int style) {
        this.style = style;
        this.underline = false;
        this.foreground = BLACK;
        this.background = TRANSPARENT;
    }

    public Style(boolean italic, boolean bold) {
        this.underline = false;
        this.foreground = BLACK;
        this.background = TRANSPARENT;
        italic(italic);
        bold(bold);
    }

    public int style() {
        return style;
    }

    public Style style(int style) {
        this.style = style;
        return this;
    }

    public boolean underline() {
        return underline;
    }

    public Style underline(boolean underline) {
        this.underline = underline;
        return this;
    }

    public int foreground() {
        return foreground;
    }

    public Style foreground(int foreground) {
        this.foreground = foreground;
        return this;
    }

    public int background() {
        return background;
    }

    public Style background(int background) {
        this.background = background;
        return this;
    }

    public final boolean bold() {
        return (this.style & BOLD) != 0;
    }

    public final boolean italic() {
        return (this.style & ITALIC) != 0;
    }

    public Style italic(boolean italic) {
        if (italic) {
            if (bold()) {
                this.style = BOLD_ITALIC;
            } else {
                this.style = ITALIC;
            }
        } else {
            if (bold()) {
                this.style = BOLD;
            } else {
                this.style = NORMAL;
            }
        }
        return this;
    }

    public Style bold(boolean bold) {
        if (bold) {
            if (italic()) {
                this.style = BOLD_ITALIC;
            } else {
                this.style = BOLD;
            }
        } else {
            if (italic()) {
                this.style = ITALIC;
            } else {
                this.style = NORMAL;
            }
        }
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Style style1 = (Style) o;

        return style == style1.style && underline == style1.underline && foreground == style1.foreground && background == style1.background;

    }

    @Override
    public int hashCode() {
        int result = style;
        result = 31 * result + (underline ? 1 : 0);
        result = 31 * result + foreground;
        result = 31 * result + background;
        return result;
    }

    @Override
    public String toString() {
        return "Style{" +
                "style=" + style +
                ", underline=" + underline +
                ", foreground=" + foreground +
                ", background=" + background +
                '}';
    }
}

