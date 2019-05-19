package ru.shabashoff.utils;

import java.math.BigDecimal;
import java.util.*;

import lombok.extern.log4j.Log4j;
import ru.shabashoff.entity.Point;
import ru.shabashoff.entity.Vector;
import ru.shabashoff.entity.server.SeeMessage;

@Log4j
public class GameUtils {
    static final double TO_RADIANS_CONST = Math.PI / 180.0;
    static final Map<Integer, Point> playersPoint = new HashMap<>();

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

    public static Double parseNum(String s) {
        Double num;
        try {
            num = Double.valueOf(s);
        } catch (Exception e) {
            return null;
        }
        return num;
    }

    public static <T extends Number> int compare(Comparable<T> o1, T o2) {
        return (o1).compareTo(o2);
    }

    public static boolean eqWithNull(Object o1, Object o2) {
        if (o1 == null) {
            return o2 == null;
        }

        return o1.equals(o2);
    }

    public static Point getPointByAngleAndLength(double len, double angle) {
        double x = len * Math.cos(angle);
        double y = len * Math.sin(angle);
        return new Point(x, y);
    }

    public static double theAverageAngle(double... nums) {
        double sum = 0.0;
        for (double num : nums) {
            while (num < 0) num += 360.0;

            sum += num;
        }

        return convertAngleToNormalForm(sum / nums.length);
    }

    public static Point getValidPoint(Point... points) {
        for (Point point : points) {
            if (Math.abs(point.getX()) < 59.0 && Math.abs(point.getY()) < 39.0) {
                return point;
            }
        }


        log.warn("Can't find valid point!\n" + Arrays.toString(points));
        return null;
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

    public static double calcLength(Point p1, Point p2) {
        return new Vector(p1, p2).getLength();
    }


    public static boolean isBallCatchable(Point player, Point ball) {
        return calcLength(player, ball) <= 1.0;
    }

    public static boolean isBallCatchable(SeeMessage see) {
        if (see == null || see.getBallPoint() == null) return false;
        return isBallCatchable(see.getPlayerExpectedPoint(), see.getBallPoint());
    }

    public static double convertAngleToNormalForm(double a) {
        while (a > 180.0) a -= 360.0;
        while (a < -180.0) a += 360.0;

        return a;
    }

    public static boolean isNum(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static BigDecimal log2(BigDecimal val) {
        return BigDecimal.valueOf(Math.log(val.doubleValue()) / Math.log(2));
    }

    public static List<String> parseMessages(String message) {
        List<String> list = new ArrayList<>();
        char[] chars = message.toCharArray();
        int start = 0;
        int count = 0;
        for (int i = 0; i < chars.length; i++) {
            if (chars[i] == '(') count++;
            else if (chars[i] == ')') count--;

            if ((chars[i] == ' ') && count == 0) {
                list.add(new String(chars, start, i - start));
                start = i + 1;
            }
        }
        list.add(new String(chars, start, message.length() - start));
        return list;
    }

    public static void setPlayerPoint(int id, Point pp) {
        playersPoint.put(id, pp);
    }

    public static Point getPlayerPoint(int id) {
        return playersPoint.get(id);
    }
}
