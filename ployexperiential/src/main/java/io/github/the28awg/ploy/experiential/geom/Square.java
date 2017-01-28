package io.github.the28awg.ploy.experiential.geom;

/**
 * Created by the28awg on 25.12.16.
 */

public class Square {

    private XY a;
    private XY b;
    private XY c;
    private XY d;

    public Square() {
        this.a = new XY();
        this.b = new XY();
        this.c = new XY();
        this.d = new XY();
    }

    public Square(XY a, XY b, XY c, XY d) {
        this.a = a;
        this.b = b;
        this.c = c;
        this.d = d;
    }

    public Square(float ax, float ay, float bx, float by, float cx, float cy, float dx, float dy) {
        this.a = new XY(ax, ay);
        this.b = new XY(bx, by);
        this.c = new XY(cx, cy);
        this.d = new XY(dx, dy);
    }

    public XY a() {
        return a;
    }

    public Square a(XY a) {
        this.a = a;
        return this;
    }

    public Square a(float ax, float ay) {
        this.a = new XY(ax, ay);
        return this;
    }

    public XY b() {
        return b;
    }

    public Square b(XY b) {
        this.b = b;
        return this;
    }

    public Square b(float bx, float by) {
        this.b = new XY(bx, by);
        return this;
    }

    public XY c() {
        return c;
    }

    public Square c(XY c) {
        this.c = c;
        return this;
    }

    public Square c(float cx, float cy) {
        this.c = new XY(cx, cy);
        return this;
    }

    public XY d() {
        return d;
    }

    public Square d(XY d) {
        this.d = d;
        return this;
    }

    public Square d(float dx, float dy) {
        this.d = new XY(dx, dy);
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Square square = (Square) o;

        if (a != null ? !a.equals(square.a) : square.a != null) return false;
        if (b != null ? !b.equals(square.b) : square.b != null) return false;
        if (c != null ? !c.equals(square.c) : square.c != null) return false;
        return d != null ? d.equals(square.d) : square.d == null;

    }

    @Override
    public int hashCode() {
        int result = a != null ? a.hashCode() : 0;
        result = 31 * result + (b != null ? b.hashCode() : 0);
        result = 31 * result + (c != null ? c.hashCode() : 0);
        result = 31 * result + (d != null ? d.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Square{" +
                "a=" + a +
                ", b=" + b +
                ", c=" + c +
                ", d=" + d +
                '}';
    }
}
