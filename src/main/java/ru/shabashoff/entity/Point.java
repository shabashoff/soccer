package ru.shabashoff.entity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Point {
    double x, y;

    public Point(Point p) {
        x = p.getX();
        y = p.getY();
    }

    public Point addPoint(Point p) {
        return new Point(x + p.getX(), y + p.getY());
    }

    public Point minusPoint(Point p) {
        return new Point(x - p.getX(), y - p.getY());
    }

    public void rotate(double angle) {
        double xx = x * Math.cos(angle) - y * Math.sin(angle);
        double yy = x * Math.sin(angle) + y * Math.cos(angle);

        x = xx;
        y = yy;
    }

    public void setLength(double l) {
        double len = Math.sqrt(x * x + y * y);
        x = (x / len) * l;
        y = (y / len) * l;
    }
}
