package io.github.the28awg.ploy.experiential.geom;

public class Dimension {
    private float width;
    private float height;

    public Dimension() {
        width = -1;
        height = -1;
    }

    public Dimension(float width, float height) {
        if (width < 0 || height < 0) {
            throw new RuntimeException("width(" + width + ") < 0 || height(" + height + ") < 0");
        }
        this.width = width;
        this.height = height;
    }

    public float width() {
        return width;
    }

    public Dimension width(float width) {
        if (width < 0) {
            throw new RuntimeException("width(" + width + ") < 0");
        }
        this.width = width;
        return this;
    }

    public float height() {
        return height;
    }

    public Dimension height(float height) {
        if (height < 0) {
            throw new RuntimeException("height(" + height + ") < 0");
        }
        this.height = height;
        return this;
    }

    @Override
    public String toString() {
        return "Dimension{" +
                "width=" + width +
                ", height=" + height +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Dimension dimension = (Dimension) o;

        return Float.compare(dimension.width, width) == 0 && Float.compare(dimension.height, height) == 0;

    }

    @Override
    public int hashCode() {
        int result = (width != +0.0f ? Float.floatToIntBits(width) : 0);
        result = 31 * result + (height != +0.0f ? Float.floatToIntBits(height) : 0);
        return result;
    }
}
