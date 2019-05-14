package ru.shabashoff.entity.server;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import lombok.extern.log4j.Log4j;
import ru.shabashoff.entity.Point;
import ru.shabashoff.entity.Vector;
import ru.shabashoff.utils.GameUtils;

@Log4j
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SeeMessage extends ServerMessage {

    final static double X_RIGHT_LINE = 54;
    final static double X_LEFT_LINE = -54;

    final static double Y_BOTTOM_LINE = 34;
    final static double Y_TOP_LINE = -34;

    final static double OUTER_FLAGS_PADDING = 5;

    final static double Y_BOTTOM_FLAGS = Y_BOTTOM_LINE + 5;
    final static double Y_TOP_FLAGS = Y_TOP_LINE - 5;

    final static double X_RIGHT_FLAGS = X_RIGHT_LINE + 5;
    final static double X_LEFT_FLAGS = X_LEFT_LINE - 5;

    final static Map<String, Point> defObjects = new HashMap<>();

    static {
        defObjects.put("fc", new Point(0, 0));

        defObjects.put("fb0", new Point(0, Y_BOTTOM_FLAGS));
        defObjects.put("ft0", new Point(0, Y_TOP_FLAGS));

        defObjects.put("fcb", new Point(0, Y_BOTTOM_LINE));
        defObjects.put("fct", new Point(0, Y_TOP_LINE));

        defObjects.put("flb", new Point(X_LEFT_LINE, Y_BOTTOM_LINE));
        defObjects.put("flt", new Point(X_LEFT_LINE, Y_TOP_LINE));
        defObjects.put("frt", new Point(X_RIGHT_LINE, Y_TOP_LINE));
        defObjects.put("frb", new Point(X_RIGHT_LINE, Y_BOTTOM_LINE));


        for (int i = 10; i <= 50; i += 10) {
            defObjects.put("fbl" + i, new Point(-i, Y_BOTTOM_FLAGS));
            defObjects.put("fbr" + i, new Point(i, Y_BOTTOM_FLAGS));

            defObjects.put("ftl" + i, new Point(-i, Y_TOP_FLAGS));
            defObjects.put("ftr" + i, new Point(i, Y_TOP_FLAGS));
        }

        for (int i = 10; i <= 30; i += 10) {
            defObjects.put("flb" + i, new Point(X_LEFT_FLAGS, i));
            defObjects.put("flt" + i, new Point(X_LEFT_FLAGS, -i));

            defObjects.put("frb" + i, new Point(X_RIGHT_FLAGS, i));
            defObjects.put("frt" + i, new Point(X_RIGHT_FLAGS, -i));
        }
    }

    final List<GameObject> gameObjects;

    long time;

    Point playerExpectedPoint;

    Point ballPoint;

    Double playerExpectedAngle = null;


    public SeeMessage() {
        super(MessageType.SEE_MESSAGE);
        this.gameObjects = new ArrayList<>();
    }

    @Override
    public void fillFields(List<String> params) {
        time = Long.parseLong(params.get(0));
        for (int i = 1; i < params.size(); i++) {
            gameObjects.add(parseGameObject(params.get(i)));
        }
    }

    public Map<String, Point> getDefObjects() {
        return defObjects;
    }

    public Point getBallPoint() {
        if (ballPoint != null) return ballPoint;

        if (playerExpectedPoint == null) return null;

        for (GameObject gameObject : gameObjects) {
            if ("b".equals(gameObject.getName())) {
                ballPoint = playerExpectedPoint.addPoint(GameUtils.getPointByAngleAndLength(gameObject.getNumOnPosition(0),
                        GameUtils.convertToRadians(gameObject.getNumOnPosition(1) + getPlayerExpectedAngle())));

                return ballPoint;
            }
        }

        return null;
    }

    public void findPlayerPointAngle() {
        List<GameObject> mainObjects = new ArrayList<>();

        gameObjects.forEach(g -> {
            if (defObjects.containsKey(g.getName())) mainObjects.add(g);
        });

        if (mainObjects.size() < 3) {
            log.warn("Can't find player point. Because they don't see enough points!");
            return;
        }

        GameObject prev = mainObjects.get(0);

        ArrayList<Point> fPoints = new ArrayList<>();


        for (int i = 1; i < mainObjects.size(); i++) {
            GameObject gameObject = mainObjects.get(i);

            Point[] expPoints = calcExpectedPoint(prev, gameObject);

            Point validPoint = GameUtils.getValidPoint(expPoints);
            if (validPoint != null) {
                fPoints.add(validPoint);
            }

            prev = mainObjects.get(i);
        }

        Point sum = new Point();

        for (Point point : fPoints) {
            sum.add(point);
        }

        sum.divide(fPoints.size());

        playerExpectedPoint = sum;

        prev = mainObjects.get(0);

        double sumAngle = 0.0;

        for (int i = 1; i < mainObjects.size(); i++) {
            sumAngle += calcExpectedAngle(prev, mainObjects.get(i));

            prev = mainObjects.get(i);
        }

        playerExpectedAngle = sumAngle / (mainObjects.size() - 1);
    }

    public double calcExpectedAngle(GameObject g1, GameObject g2) {
        Point point = defObjects.get(g1.getName());
        double angle = g1.getNumOnPosition(1);

        Point prevPoint = defObjects.get(g2.getName());
        double prevAngle = g2.getNumOnPosition(1);

        return calcExpectedAngle(point, angle, prevPoint, prevAngle, playerExpectedPoint);
    }

    public double calcExpectedAngle(Point p1, double a1, Point p2, double a2, Point playerPoint) {
        return GameUtils.theAverageAngle(GameUtils.calcVecAngle(playerPoint, p1) - a1, GameUtils.calcVecAngle(playerPoint, p2) - a2);
    }

    public Point[] calcExpectedPoint(GameObject g1, GameObject g2) {
        Point point = defObjects.get(g1.getName());

        double angle = GameUtils.convertToRadians(g1.getNumOnPosition(1));
        double len = g1.getNumOnPosition(0);

        Point prevPoint = defObjects.get(g2.getName());

        double prevAngle = GameUtils.convertToRadians(g2.getNumOnPosition(1));
        double prevLen = g2.getNumOnPosition(0);

        return calcExpectedPoint(point, len, angle, prevPoint, prevLen, prevAngle);
    }

    public Point[] calcExpectedPoint(Point p1, double l1, double a1, Point p2, double l2, double a2) {
        double angle;

        Point[] points = new Point[2];

        if (a2 > a1) {
            Point p = p2;
            double l = l2;
            double a = a2;

            p2 = p1;
            l2 = l1;
            a2 = a1;

            p1 = p;
            l1 = l;
            a1 = a;
        }

        if ((a1 > 0.0 && a2 < 0.0) || (a1 < 0.0 && a2 > 0.0)) angle = Math.abs(a1) + Math.abs(a2);
        else angle = Math.abs(a1 - a2);

        double c = Math.sqrt(l1 * l1 + l2 * l2 - 2 * l1 * l2 * Math.cos(angle));

        double phi = Math.acos((l1 * l1 + c * c - l2 * l2) / (2 * l1 * c));
        for (int i = 0; i < 2; i++) {
            Vector vector = new Vector(p1, p2);
            vector.rotate(i == 0 ? -phi : phi);
            vector.setLength(l1);

            Point ans1 = vector.getP2();

            phi = Math.acos((l2 * l2 + c * c - l1 * l1) / (2 * l2 * c));

            vector = new Vector(p2, p1);
            vector.rotate(i == 0 ? phi : -phi);
            vector.setLength(l2);
            points[i] = GameUtils.theAverage(ans1, vector.getP2());
        }

        return points;
    }


    private GameObject parseGameObject(String str) {
        GameObject gameObject = new GameObject();
        int l = 0;
        int start = 0;
        char[] charArray = str.toCharArray();
        for (int i = 0; i < charArray.length; i++) {
            char c = charArray[i];
            if (c == '(') {
                start = i + 1;
                l++;
            }

            if (start != i && (c == ')' || c == ' ')) {
                if (l == 2) {
                    while (charArray[start] != ')') {
                        if (charArray[start] != ' ') gameObject.addName(charArray[start]);
                        start++;
                    }
                    i = start;
                    l--;
                }
                else {
                    gameObject.addNum(Float.valueOf(str.substring(start, i)));
                }
                start = i + 1;
            }

            if (c == ')') l--;
        }
        return gameObject;
    }
}