package ru.shabashoff.entity;

import lombok.extern.log4j.Log4j;
import org.junit.Assert;
import org.junit.Test;
import ru.shabashoff.Utils.GameUtils;
import ru.shabashoff.entity.server.SeeMessage;
import ru.shabashoff.parser.MsgParser;

@Log4j
public class SeeMessageTest {
    String
            TEST_SEE_MESSAGE_1 =
            "(see 60 ((f l b) 43.8 11) ((f p l b) 22.6 6 -0 0) ((f b l 20) 36.2 -36) ((f b l 30) 37.3 -21) ((f b l 40) 40.4 -7) ((f b l 50) 46.1 4) ((f l b 10) 36.6 45) ((f l b 20) 39.6 31) ((f l b 30) 45.2 19) ((l b) 37.3 56))";
    String
            TEST_SEE_MESSAGE_2 =
            "(see 3096 ((f l b) 42.1 -4) ((f g l b) 32.1 36) ((f p l b) 20.5 -4 -0 -0) ((f b l 30) 33.8 -37) ((f b l 40) 37.7 -22) ((f b l 50) 43.8 -11) ((f l b 10) 37 32) ((f l b 20) 39.3 17) ((f l b 30) 43.8 5) ((l l) 40 -53))";

    private void testExpectedPoint(Point p1, double l1, double a1, Point p2, double l2, double a2, Point expPoint, double angle, int p) {
        SeeMessage see = new SeeMessage();
        Point point = see.calcExpectedPoint(p1, l1, GameUtils.convertToRadians(a1), p2, l2, GameUtils.convertToRadians(a2))[p];

        log.info("point: " + point + " expected point: " + expPoint);

        Assert.assertEquals(expPoint.getX(), point.getX(), 0.00001);
        Assert.assertEquals(expPoint.getY(), point.getY(), 0.00001);

        Assert.assertEquals(angle, see.calcExpectedAngle(p1, a1, p2, a2, expPoint), 0.00001);

    }

    @Test
    public void testGetExpectedPoint1() {
        Point p1 = new Point(15, 15);
        Point p2 = new Point(15, -15);

        Point expPoint0 = new Point(0.0, 0.0);
        Point expPoint1 = new Point(30.0, 0.0);

        testExpectedPoint(p1, 21.2132, 45.0, p2, 21.2132, -45.0, expPoint0, 0.0, 0);
        testExpectedPoint(p2, 21.2132, -45.0, p1, 21.2132, 45.0, expPoint0, 0.0, 0);

        testExpectedPoint(p1, 21.2132, 45.0, p2, 21.2132, -45.0, expPoint1, 180.0, 1);
        testExpectedPoint(p2, 21.2132, -45.0, p1, 21.2132, 45.0, expPoint1, 180.0, 1);
    }

    @Test
    public void testGetExpectedPoint2() {
        Point p1 = new Point(10.5, 12.330127);
        Point p2 = new Point(12.330127, 5.5);

        Point expPoint = new Point(8.0, 8.0);

        testExpectedPoint(p1, 5.0, 60.0, p2, 5.0, -30.0, expPoint, 0.0, 0);
        testExpectedPoint(p2, 5.0, -30.0, p1, 5.0, 60.0, expPoint, 0.0, 0);
    }

    @Test
    public void testGetExpectedPoint3() {
        Point p1 = new Point(2.0, 2.0);
        Point p2 = new Point(1.0, 2.0);

        Point expPoint0 = new Point(1.0, 1.0);

        testExpectedPoint(p1, 1.41421356, 45.0, p2, 1.0, 90.0, expPoint0, 0.0, 0);
        testExpectedPoint(p2, 1.0, 90.0, p1, 1.41421356, 45.0, expPoint0, 0.0, 0);
    }

    @Test
    public void testGetExpectedPoint4() {
        Point p1 = new Point(2.0, 1.0);
        Point p2 = new Point(1.0, 2.0);

        Point expPoint0 = new Point(1.0, 1.0);
        Point expPoint1 = new Point(2.0, 2.0);

        testExpectedPoint(p1, 1.0, -45.0, p2, 1.0, 45.0, expPoint0, 45.0, 0);
        testExpectedPoint(p2, 1.0, 45.0, p1, 1.0, -45.0, expPoint0, 45.0, 0);

        testExpectedPoint(p1, 1.0, -45.0, p2, 1.0, 45.0, expPoint1, -135.0, 1);
        testExpectedPoint(p2, 1.0, 45.0, p1, 1.0, -45.0, expPoint1, -135.0, 1);
    }

    @Test
    public void testCalculatePointAndAngle1() {
        MsgParser parser = new MsgParser();
        SeeMessage see = (SeeMessage) parser.parseMessage(TEST_SEE_MESSAGE_1);

        see.findPlayerPointAngle();

        log.info("Player expected point: " + see.getPlayerExpectedPoint());
        log.info("Player expected angle: " + see.getPlayerExpectedAngle());

        Assert.assertEquals(-22.3, see.getPlayerExpectedPoint().getX(), 0.1);
        Assert.assertEquals(2.6, see.getPlayerExpectedPoint().getY(), 0.1);

        Assert.assertEquals(127.5, see.getPlayerExpectedAngle(), 0.1);
    }

    @Test
    public void testCalculatePointAndAngle2() {
        MsgParser parser = new MsgParser();
        SeeMessage see = (SeeMessage) parser.parseMessage(TEST_SEE_MESSAGE_2);

        see.findPlayerPointAngle();

        log.info("Player expected point: " + see.getPlayerExpectedPoint());
        log.info("Player expected angle: " + see.getPlayerExpectedAngle());

        Assert.assertEquals(-21.6, see.getPlayerExpectedPoint().getX(), 0.1);
        Assert.assertEquals(6.1, see.getPlayerExpectedPoint().getY(), 0.1);

        Assert.assertEquals(138.6, see.getPlayerExpectedAngle(), 0.1);
    }
}
