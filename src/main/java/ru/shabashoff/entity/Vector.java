package ru.shabashoff.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;


@FieldDefaults(level = AccessLevel.PRIVATE)
public class Vector {
    @Getter
    Point p1, p2;

    Double angle;

    Double length;

    public Vector(Point p1, Point p2) {
        this.p1 = p1;
        this.p2 = p2;
    }

    public void addVector(Vector v) {
        p2 = new Point(v.getP2());
    }

    public void minusPoint(Vector v) {
        p1 = new Point(v.getP2());
    }

    static public Vector add2Vectors(Vector v1, Vector v2) {
        return new Vector(new Point(v1.getP1()), new Point(v2.getP2()));
    }

    public Double getAngle() {
        if (angle == null) {
            Point p = p2.minusPoint(p1);
            angle = Math.atan(p.getY() / p.getX()) * 180.0 / Math.PI;

            if (p.getX() < 0.0) angle += 180.0;

        }

        if (angle > 180.0) return angle - 360.0;
        return angle;
    }

    public void rotate(double angle) {
        Point point = p2.minusPoint(p1);

        point.rotate(angle);

        double length = getLength();

        point.setLength(length);

        p2 = p1.addPoint(point);
    }

    public void setLength(double length) {
        Point point = p2.minusPoint(p1);

        point.setLength(length);

        p2 = p1.addPoint(point);
    }

    public Double getLength() {
        if (length == null) {
            Point p = p2.minusPoint(p1);
            length = Math.sqrt(p.getX() * p.getX() + p.getY() * p.getY());
        }

        return length;
    }


}
