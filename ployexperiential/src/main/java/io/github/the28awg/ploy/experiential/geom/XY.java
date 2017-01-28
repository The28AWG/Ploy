package io.github.the28awg.ploy.experiential.geom;

import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class XY {
    private float x;
    private float y;
    private ReadWriteLock lock;

    public XY() {
        this.x = 0.0f;
        this.y = 0.0f;
        lock = new ReentrantReadWriteLock();
    }

    public XY(float x, float y) {
        this.x = x;
        this.y = y;
        lock = new ReentrantReadWriteLock();
    }

    public XY(XY xy) {
        this.x = xy.x();
        this.y = xy.y();
        lock = new ReentrantReadWriteLock();
    }

    public static double distance(XY p1, XY p2) {
        return Math.sqrt((p1.x() - p2.x()) * (p1.x() - p2.x()) + (p1.y() - p2.y()) * (p1.y() - p2.y()));
    }

    public static XY interpolationByDistance(XY p1, XY p2, double d) {
        float len = (float) distance(p1, p2);
        float ratio = (float) (d / len);
        float x = (float) (ratio * p2.x() + (1.0 - ratio) * p1.x());
        float y = (float) (ratio * p2.y() + (1.0 - ratio) * p1.y());
        return new XY(x, y);
    }

    public static XY div(XY xy, float div) {
        return new XY(xy.x() / div, xy.y() / div);
    }


    public static boolean contains(XY pos, Dimension d, XY p) {
        return contains(pos, new XY(pos.x() + d.width(), pos.y() + d.height()), p);
    }

    public static boolean contains(XY pos1, XY pos2, XY pos3) {
        return (pos1.x() < pos3.x() && pos1.y() < pos3.y() && pos2.x() > pos3.x() && pos2.y() > pos3.y());
    }

    public float x() {
        lock.writeLock().lock();
        try {
            return x;
        } finally {
            lock.writeLock().unlock();
        }
    }

    public XY x(float x) {
        lock.readLock().lock();
        try {
            this.x = x;
        } finally {
            lock.readLock().unlock();
        }
        return this;
    }

    public float y() {
        lock.writeLock().lock();
        try {
            return y;
        } finally {
            lock.writeLock().unlock();
        }
    }

    public XY y(float y) {
        lock.readLock().lock();
        try {
            this.y = y;
        } finally {
            lock.readLock().unlock();
        }
        return this;
    }

    public XY xy(XY xy) {
        return xy(xy.x(), xy.y());
    }

    public XY xy(float x, float y) {
        return x(x).y(y);
    }

    @Override
    public String toString() {
        return "XY{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;

        XY xy = (XY) o;

        if (Float.compare(xy.x, x) != 0) return false;
        return Float.compare(xy.y, y) == 0;

    }

    @Override
    public int hashCode() {
        int result = (x != +0.0f ? Float.floatToIntBits(x) : 0);
        result = 31 * result + (y != +0.0f ? Float.floatToIntBits(y) : 0);
        return result;
    }
}
