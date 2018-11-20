package ru.shabashoff.Utils;

import ru.shabashoff.entity.Point;
import ru.shabashoff.entity.Vector;

public class GameUtils {
    static final double TO_RADIANS_CONST = Math.PI / 180.0;


    public static double diffAngles(double a1, double a2) {
        if (a1 < 0.0) a1 = 360 + a1;
        if (a2 < 0.0) a2 = 360 + a2;
        double v = a2 - a1;

        if (v > 180.0) v -= 360;
        if (v < -180.0) v += 360;

        return v;
    }

    public static double convertToRadians(double degrees) {
        return degrees * TO_RADIANS_CONST;
    }

    public static double calcVecAngle(Point from, Point to) {
        return new Vector(from, to).getAngle();
    }

    public static double theAverage(double... nums) {
        double sum = 0.0;

        for (double num : nums) {
            sum += num;
        }

        return sum / nums.length;
    }

    public static double theAverageAngle(double... nums) {
        double sum = 0.0;

        for (double num : nums) {
            while (num < 0) num += 360.0;

            sum += num;
        }

        return convertAngleToNormalForm(sum / nums.length);
    }

    public static Point theAverage(Point... points) {
        double xSum = 0.0;
        double ySum = 0.0;

        for (Point point : points) {
            xSum += point.getX();
            ySum += point.getY();
        }

        return new Point(xSum / points.length, ySum / points.length);
    }

    public static double getLength(Point p1, Point p2) {
        return new Vector(p1, p2).getLength();
    }


    public static double convertAngleToNormalForm(double a) {
        while (a > 180.0) a -= 360.0;
        while (a < -180.0) a += 360.0;

        return a;
    }
}
