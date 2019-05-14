package ru.shabashoff.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import lombok.extern.log4j.Log4j;
import ru.shabashoff.decision.DecisionTree;
import ru.shabashoff.entity.server.ErrorMessage;
import ru.shabashoff.entity.server.HearMessage;
import ru.shabashoff.entity.server.PlayMode;
import ru.shabashoff.entity.server.SeeMessage;
import ru.shabashoff.entity.server.SenseBody;
import ru.shabashoff.utils.GameUtils;

@SuppressWarnings("Duplicates")
@Log4j
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Player extends RCSSServerClient implements Serializable {
    @Getter
    final int id;
    @Getter
    final PlayerPosition playerPosition;
    final String teamName;

    PlayMode playMode = PlayMode.before_kick_off;

    Point point = new Point();
    Point speed = new Point();
    BigDecimal playerAngle;
    BigDecimal playerAngleSpeed;

    BigDecimal lengthToBall;
    BigDecimal angleToBall;

    Point ballPoint = new Point();
    Point ballSpeed = new Point();


    volatile SenseBody sense;
    final Object[] senseMonitor = new Object[0];

    volatile SeeMessage see;
    final Object[] seeMonitor = new Object[0];

    final DecisionTree<String> tree;

    final Point goaliePoint = new Point(54, 0);

    int ballCatchable = 0;


    private static final BigDecimal DEFAULT_NULL = BigDecimal.ZERO;

    public Player(int id, String teamName, PlayerPosition playerPosition, DecisionTree<String> tree) {
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
        if (calcBallPoint() != null && calcExpectedPoint() != null) {
            catchBall(-(int) GameUtils.calcVecAngle(calcExpectedPoint(), calcBallPoint())); //TODO
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
        Point ballPoint = calcBallPoint();
        goTo(ballPoint.getX(), ballPoint.getY());
    }

    private void action() {
        String action = tree.action(getSnapshotV2());
        BigDecimal[] snV2 = getSnapshotV2();

        String[] ss = action.split("_");

        StringBuilder sb = new StringBuilder();
        sb.append('(').append(ss[0]);

        boolean predParam = false;

        for (int i = 1; i < ss.length; i++) {
            if (ss[i].equals("PARAM")) predParam = true;
            else {
                if (predParam) {
                    predParam = false;
                    sb.append(' ').append(snV2[Integer.parseInt(ss[i])]);
                } else {
                    if (!GameUtils.isNum(ss[i])) sb.append('_');
                    else sb.append(' ');

                    sb.append(ss[i]);
                }
            }
        }

        sb.append(')');

        System.out.println("command:" + sb.toString());

        sendMessage(sb.toString());
    }

    public Point calcExpectedPoint() {
        synchronized (seeMonitor) {
            return see.getPlayerExpectedPoint();
        }
    }

    public BigDecimal calcPlayerExpectedAngle() {
        synchronized (seeMonitor) {
            Double pa = see.getPlayerExpectedAngle();
            if (pa != null) {
                try {
                    return BigDecimal.valueOf(pa);
                } catch (Exception e) {
                    log.error(e + "Number:" + pa);
                }
            }
                return DEFAULT_NULL;

        }
    }

    public Double getExpectedAngle() {
        synchronized (seeMonitor) {
            return see.getPlayerExpectedAngle();
        }
    }

    public Point calcBallPoint() {
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
            see.findPlayerPointAngle();

            calcInternalParams(calcPlayerExpectedAngle(), calcExpectedPoint(), calcBallPoint());
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

    public void calcInternalParams(BigDecimal angle, Point pp, Point bp) {
        if (playerAngle != null && angle != null) {
            playerAngleSpeed = angle.subtract(this.playerAngle);
        } else {
            playerAngleSpeed = DEFAULT_NULL;
        }

        this.playerAngle = angle;

        if (pp != null && this.point != null) {
            speed = pp.minusPoint(this.point);
        } else {
            speed = null;
        }

        this.point = pp;

        if (bp != null && this.ballPoint != null) {
            ballSpeed = bp.minusPoint(this.ballPoint);
        } else {
            ballSpeed = null;
        }

        this.ballPoint = bp;

        if (bp != null && pp != null) {
            lengthToBall = BigDecimal.valueOf(GameUtils.getLength(pp, bp));
            angleToBall = BigDecimal.valueOf(GameUtils.calcVecAngle(pp, bp));
        } else {
            lengthToBall = DEFAULT_NULL;
            angleToBall = DEFAULT_NULL;
        }
    }

    public void goTo(double x, double y) {
        double minAngle = 35;

        Vector vector = new Vector(calcExpectedPoint(), new Point(x, y));

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
        List<BigDecimal> sv2 = getSnapshotV2(true);

        BigDecimal[] bd = new BigDecimal[sv2.size()];
        sv2.toArray(bd);

        return bd;
    }

    public List<BigDecimal> getSnapshotV2(boolean t) {
        List<BigDecimal> args = new ArrayList<>();

        args.add(BigDecimal.valueOf(id));
        args.add(BigDecimal.valueOf(playerPosition.internalPos));
        args.add(BigDecimal.valueOf(playMode.ordinal()));

        addPoint(args, point);
        addPoint(args, speed);

        args.add(playerAngle);
        args.add(playerAngleSpeed);

        addPoint(args, ballPoint);
        addPoint(args, ballSpeed);

        args.add(angleToBall);
        args.add(lengthToBall);

        return args;
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
