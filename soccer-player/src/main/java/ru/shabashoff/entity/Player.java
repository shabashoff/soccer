package ru.shabashoff.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import lombok.extern.log4j.Log4j;
import ru.shabashoff.decision.ActionType;
import ru.shabashoff.decision.DecisionTree;
import ru.shabashoff.entity.server.*;
import ru.shabashoff.utils.GameUtils;

@SuppressWarnings("Duplicates")
@Log4j
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Player extends RCSSServerClient implements Serializable {
    @Getter
    final int id;
    @Getter
    final PlayerPosition playerPosition;
    final String teamName;

    PlayMode playMode;

    double expectedX = 0.0;
    double expectedY = 0.0;

    double speedX = 0.0;
    double speedY = 0.0;

    volatile SenseBody sense;
    final Object[] senseMonitor = new Object[0];

    volatile SeeMessage see;
    final Object[] seeMonitor = new Object[0];

    final DecisionTree tree;

    final Point goaliePoint = new Point(54, 0);

    int ballCatchable = 0;

    private static final BigDecimal DEFAULT_NULL = null;

    public Player(int id, String teamName, PlayerPosition playerPosition, DecisionTree tree) {
        super(teamName);
        this.playerPosition = playerPosition;
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

    public void catchBall(int angle) {
        sendMessage(MessageFormat.format("(catch {0})", angle));
        ballCatchable = 2;
    }

    public void kick(int power, int dicrection) {
        sendMessage(MessageFormat.format("(kick {0} {1})", power, dicrection));
        ballCatchable = 0;
    }

    public void rotateToGoal() {
        synchronized (seeMonitor) {
            turn(-(int) (see.getPlayerExpectedAngle() -
                    GameUtils.calcVecAngle(see.getPlayerExpectedPoint(), goaliePoint)));
        }
    }

    public void catchAction() {
        if (getBallPoint() != null && getExpectedPoint() != null) {
            catchBall(-(int) GameUtils.calcVecAngle(getExpectedPoint(), getBallPoint())); //TODO
        } else {
            catchBall(0);
        }
    }

    public void dashAction() {
        dash(50); //TODO
    }

    public void kickInGateAction() {
        kick(
                50,
                -(int) (see.getPlayerExpectedAngle() - GameUtils.calcVecAngle(see.getPlayerExpectedPoint(), goaliePoint))
        ); //TODO
    }

    public void rotateRightAction() {
        turn(15); //TODO
    }

    public void goToBallAction() {
        Point ballPoint = getBallPoint();
        goTo(ballPoint.getX(), ballPoint.getY());
    }

    private void action() {
        ActionType.getFromN(tree.action(getSnapshot())).run(this);
    }

    public Point getExpectedPoint() {
        synchronized (seeMonitor) {
            return see.getPlayerExpectedPoint();
        }
    }

    public Double getExpectedAngle() {
        synchronized (seeMonitor) {
            return see.getPlayerExpectedAngle();
        }
    }

    public Point getBallPoint() {
        synchronized (seeMonitor) {
            return see.getBallPoint();
        }
    }

    @Override
    protected void onErrorMessage(ErrorMessage message) {
        log.error(message);
    }

    @Override
    protected void onHearMessage(HearMessage message) {
        System.out.println("On hear fckg message:" + message);
        if (message.getName().equals("referee")) {
            System.out.println("MMMM");
            playMode = PlayMode.valueOf(message.getMessage());
            System.out.println(playMode);
        }
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

        if (ballCatchable != 0) {
            ballCatchable--;
        }
    }

    public SenseBody getSense() {
        return sense;
    }

    public SeeMessage getSee() {
        return see;
    }

    public void goTo(double x, double y) {
        double minAngle = 35;

        Vector vector = new Vector(getExpectedPoint(), new Point(x, y));

        double angle = vector.getAngle();

        double playerAngle = see.getPlayerExpectedAngle();

        log.info("Angle to point " + angle);
        log.info("Player angle   " + playerAngle);

        angle = GameUtils.diffAngles(playerAngle, angle);

        log.info("Angle to go   " + angle);


        if (minAngle > Math.abs(angle)) {
            dash(50);
        } else {
            turn((int) angle);

        }

    }

    public BigDecimal[] getSnapshotV2() {
        List<BigDecimal> args = new ArrayList<>();

        args.add(BigDecimal.valueOf(id));

        args.add(BigDecimal.valueOf(playMode.ordinal()));

        Point expectedPoint = getExpectedPoint();

        addPoint(args, expectedPoint);
        args.add(BigDecimal.valueOf(getExpectedAngle()));

        Point ballPoint = getBallPoint();
        addPoint(args, expectedPoint);

        if (ballPoint != null && expectedPoint != null) {
            args.add(BigDecimal.valueOf(GameUtils.getLength(expectedPoint, ballPoint)));
            args.add(BigDecimal.valueOf(GameUtils.calcVecAngle(expectedPoint, ballPoint)));
        } else {
            args.add(DEFAULT_NULL);
            args.add(DEFAULT_NULL);
        }

        args.add(BigDecimal.valueOf(ballCatchable));
        BigDecimal[] bd = new BigDecimal[args.size()];
        args.toArray(bd);


        return bd;
    }


    //TODO: simplify method
    public BigDecimal[] getSnapshot() {
        List<BigDecimal> args = new ArrayList<>();

        Point expectedPoint = getExpectedPoint();

        addPoint(args, expectedPoint);
        args.add(BigDecimal.valueOf(getExpectedAngle()));

        Point ballPoint = getBallPoint();
        addPoint(args, ballPoint);

        if (ballPoint != null && expectedPoint != null) {
            args.add(BigDecimal.valueOf(GameUtils.getLength(expectedPoint, ballPoint)));
            args.add(BigDecimal.valueOf(GameUtils.calcVecAngle(expectedPoint, ballPoint)));
        } else {
            args.add(DEFAULT_NULL);
            args.add(DEFAULT_NULL);
        }

        args.add(BigDecimal.valueOf(ballCatchable));
        BigDecimal[] bd = new BigDecimal[args.size()];
        args.toArray(bd);


        return bd;
    }

    private void addPoint(List<BigDecimal> args, Point pt) {
        if (pt != null) {
            args.add(BigDecimal.valueOf(pt.getX()));
            args.add(BigDecimal.valueOf(pt.getY()));
        } else {
            args.add(DEFAULT_NULL);
            args.add(DEFAULT_NULL);
        }
    }
}
