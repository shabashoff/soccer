package ru.shabashoff.Utils;

import lombok.extern.log4j.Log4j;
import org.junit.Assert;
import org.junit.Test;

@Log4j
public class GameUtilsTest {
    @Test
    public void test() {
        Assert.assertEquals(0.0, GameUtils.diffAngles(45, 45), 0.000001);
        Assert.assertEquals(0.0, GameUtils.diffAngles(160, 160), 0.000001);
        Assert.assertEquals(0.0, GameUtils.diffAngles(-160, -160), 0.000001);
        Assert.assertEquals(0.0, GameUtils.diffAngles(-45, -45), 0.000001);

        Assert.assertEquals(30.0, GameUtils.diffAngles(60, 90), 0.000001);
        Assert.assertEquals(30.0, GameUtils.diffAngles(90, 120), 0.000001);
        Assert.assertEquals(30.0, GameUtils.diffAngles(120, 150), 0.000001);
        Assert.assertEquals(30.0, GameUtils.diffAngles(150, -180), 0.000001);
        Assert.assertEquals(30.0, GameUtils.diffAngles(180, -150), 0.000001);
        Assert.assertEquals(30.0, GameUtils.diffAngles(-180, -150), 0.000001);
        Assert.assertEquals(60.0, GameUtils.diffAngles(-150, -90), 0.000001);

        Assert.assertEquals(180.0, GameUtils.diffAngles(90, -90), 0.000001);
        Assert.assertEquals(-180.0, GameUtils.diffAngles(-90, 90), 0.000001);

        Assert.assertEquals(-180.0, GameUtils.diffAngles(-90, 90), 0.000001);

        Assert.assertEquals(-10, GameUtils.diffAngles(5, -5), 0.000001);
        Assert.assertEquals(10, GameUtils.diffAngles(-5, 5), 0.000001);

        Assert.assertEquals(-52, GameUtils.diffAngles(13, -39), 0.000001);

    }


    @Test
    public void testCompare() {
        log.info(GameUtils.compare(159, 39));

        IfStatement ifStatement = IfStatement.MORE;
        IfStatement ifStatement1 = IfStatement.parse(">");
        log.info(ifStatement1);
    }
}

enum IfStatement {
    MORE(">"), MORE_EQ(">="), LESS("<"), LESS_EQ("<="), EQ("=="), NON_EQ("!=");

    private final String text;

    IfStatement(final String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return text;
    }

    public static IfStatement parse(String s) {

        for (IfStatement is : IfStatement.values()) {
            if (s.equals(is.toString())) {
                return is;
            }
        }

        throw new IllegalArgumentException("Can't parse string " + s);
    }
}