package io.github.the28awg.ploy.experiential.geom;

/**
 * Created by the28awg on 25.12.16.
 */

public class Rectangle {
    private float left;
    private int top;
    private float right;
    private float bottom;

    public Rectangle() {
    }

    public Rectangle(float left, int top, float right, float bottom) {
        this.left = left;
        this.top = top;
        this.right = right;
        this.bottom = bottom;
    }

    public float left() {
        return left;
    }

    public Rectangle left(float left) {
        this.left = left;
        return this;
    }

    public int top() {
        return top;
    }

    public Rectangle top(int top) {
        this.top = top;
        return this;
    }

    public float right() {
        return right;
    }

    public Rectangle right(float right) {
        this.right = right;
        return this;
    }

    public float bottom() {
        return bottom;
    }

    public Rectangle bottom(float bottom) {
        this.bottom = bottom;
        return this;
    }

    public float width() {
        return right - left;
    }

    public float height() {
        return bottom - top;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Rectangle rectangle = (Rectangle) o;

        return Float.compare(rectangle.left, left) == 0 && top == rectangle.top && Float.compare(rectangle.right, right) == 0 && Float.compare(rectangle.bottom, bottom) == 0;

    }

    @Override
    public int hashCode() {
        int result = (left != +0.0f ? Float.floatToIntBits(left) : 0);
        result = 31 * result + top;
        result = 31 * result + (right != +0.0f ? Float.floatToIntBits(right) : 0);
        result = 31 * result + (bottom != +0.0f ? Float.floatToIntBits(bottom) : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Rectangle{" +
                "left=" + left +
                ", top=" + top +
                ", right=" + right +
                ", bottom=" + bottom +
                '}';
    }
}
