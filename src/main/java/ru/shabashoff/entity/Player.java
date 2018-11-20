package ru.shabashoff.entity;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.log4j.Log4j;
import ru.shabashoff.Utils.GameUtils;
import ru.shabashoff.decision.DecisionTree;
import ru.shabashoff.entity.server.ErrorMessage;
import ru.shabashoff.entity.server.HearMessage;
import ru.shabashoff.entity.server.SeeMessage;
import ru.shabashoff.entity.server.SenseBody;

import java.text.MessageFormat;

@Log4j
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Player extends RCSSServerClient {
    final int id;
    final String teamName;

    double expectedX = 0.0;
    double expectedY = 0.0;

    double speedX = 0.0;
    double speedY = 0.0;

    volatile SenseBody sense;
    final Object[] senseMonitor = new Object[0];

    volatile SeeMessage see;
    final Object[] seeMonitor = new Object[0];

    final DecisionTree tree;

    public Player(int id, String teamName, DecisionTree tree) {
        super(teamName);
        this.tree = tree;
        this.id = id;
        this.teamName = teamName;
    }

    public void move(int x, int y) {
        sendMessage(MessageFormat.format("(move {0} {1})", x, y));
    }

    public void dash(int power) {
        sendMessage(MessageFormat.format("(dash {0})", power));
    }

    public void turn(int angle) {
        sendMessage(MessageFormat.format("(turn {0})", angle));
    }

    public void action() {
        tree.action(this);
    }

    public Point getExpectedPoint() {
        synchronized (seeMonitor) {
            return see.getPlayerExpectedPoint();
        }
    }

    @Override
    protected void onErrorMessage(ErrorMessage message) {
        log.error(message);
    }

    @Override
    protected void onHearMessage(HearMessage message) {
        log.debug(message);
    }

    @Override
    protected void onSenseBodyMessage(SenseBody message) {
        synchronized (senseMonitor) {
            sense = message;
        }
    }

    @Override
    protected void onSeeMessage(SeeMessage message) {
        synchronized (seeMonitor) {
            see = message;
            synchronized (senseMonitor) {
                see.findPlayerPointAngle();
            }
        }
        action();
    }

    public void goTo(double x, double y) {
        double minAngle = 20;

        Vector vector = new Vector(getExpectedPoint(), new Point(x, y));

        double angle = vector.getAngle();

        double playerAngle;

        synchronized (senseMonitor) {
            playerAngle = sense.getSpeedAngle();
        }

        log.info("Angle to point " + angle);
        log.info("Player angle   " + playerAngle);

        angle = GameUtils.diffAngles(playerAngle, angle);

        log.info("Angle to go   " + angle);


        if (minAngle > Math.abs(angle)) {
            dash(50);
        }
        else {
            if (angle > 0) {
                turn(-5);
            }
            else {
                turn(5);
            }
        }

    }
}
