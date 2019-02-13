package ru.shabashoff.entity;

import lombok.AccessLevel;
import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;
import lombok.extern.log4j.Log4j;
import ru.shabashoff.decision.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

@Log4j
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Team implements Serializable {
    final List<Player> players = new ArrayList<>();
    static int teamCounts = 1;


    public Team() {
        this(1, "team-" + teamCounts++);
    }


    public Team(int countPlayers, String teamName) {
        for (int i = 0; i < countPlayers; i++) {
            Player player = new Player(i, teamName, createSimpleTree());
            players.add(player);
            player.move(-25, 0);
        }
    }

    @SneakyThrows
    public void simpleStrategy() {
        Scanner sc = new Scanner(System.in);

        while (true) {
            log.info("Enter command move or turn or dash: ");
            String next = sc.next();
            switch (next) {
                case "move":
                    log.info("Enter x,y: ");
                    int x, y;
                    x = sc.nextInt();
                    y = sc.nextInt();
                    for (Player player : players) {
                        player.move(x, y);
                    }
                    break;
                case "turn":
                    log.info("Enter angle: ");
                    int a;
                    a = sc.nextInt();
                    for (Player player : players) {
                        player.turn(a);
                    }
                    break;
                case "dash":
                    log.info("Enter power: ");
                    int power;
                    power = sc.nextInt();
                    for (Player player : players) {
                        player.dash(power);
                    }
                    break;
            }
            Thread.sleep(500);
        }
    }

    @SneakyThrows
    private DecisionTree createSimpleTree() {

        /*final Point[] mainPoints = new Point[]{new Point(-40, -20), new Point(40, -20), new Point(40, 20), new Point(-40, 20)};

        final double minLen = 10.0;

        class Wrapper implements Serializable {
            private int n = 1;

            public int getN() {
                return n;
            }

            public void add() {
                n = (n + 1) % mainPoints.length;
            }
        }

        final AtomicBoolean catched = new AtomicBoolean(false);

        final AtomicInteger catchMoveCount = new AtomicInteger(0);

        final Wrapper curElem = new Wrapper();

        Node ballActions = p -> {
            if (catchMoveCount.getAndIncrement() < 2) {
                p.rotateToGoal();

            }
            else {
                p.kick(100, 0);
                catched.set(false);
                catchMoveCount.set(0);
            }
        };

        Node findingBall = p -> p.turn(15);

        Node catchBall = p -> {
            catched.set(true);
            p.catchBall(0);
        };

        Node goToBall = r -> {
            SeeMessage see = r.getSee();
            if (see != null) {
                Point ballPoint = see.getBallPoint();
                if (ballPoint != null) {
                    r.goTo(ballPoint.getX(), ballPoint.getY());
                }
            }
            else {
                log.info("Expected point: " + r.getExpectedPoint());
                r.goTo(mainPoints[curElem.getN()].getX(), mainPoints[curElem.getN()].getY());
                if (new Vector(r.getExpectedPoint(), mainPoints[curElem.getN()]).getLength() < minLen) curElem.add();
            }
        };

        Decision decisionCatchGo = new Decision(catchBall, goToBall, p -> GameUtils.isBallCatchable(p.getSee()));

        Decision findOrGo = new Decision(decisionCatchGo, findingBall, p -> p.getSee() != null && p.getSee().getBallPoint() != null);

        Decision kickOrOther = new Decision(ballActions, findOrGo, p -> catched.get());

        DecisionTree tree = new DecisionTree(kickOrOther);
        FileOutputStream fStream = new FileOutputStream("./tree.dt");
        ObjectOutputStream oos = new ObjectOutputStream(fStream);
        oos.writeObject(tree);
        oos.flush();
        oos.close();
        fStream.flush();
        fStream.close();

        FileInputStream inputStream = new FileInputStream("./tree.dt");
        ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);

        */

        Decision ifCanCatch = new Decision(at(ActionType.CATCH), at(ActionType.DASH), 5, IfStatement.LESS_EQ, BigDecimal.valueOf(1.5));

        Decision ifSmallAngle = new Decision(ifCanCatch, at(ActionType.GO_TO_BALL), 6, IfStatement.LESS_EQ, BigDecimal.valueOf(45));

        Decision ifSeeBall = new Decision(ifSmallAngle, at(ActionType.ROTATE_RIGHT), 3, IfStatement.NON_EQ, null);//dont see ball

        Decision ifBallCatched = new Decision(at(ActionType.KICK_IN_GATE), ifSeeBall, 7, IfStatement.MORE, BigDecimal.valueOf(0));


        return new DecisionTree(ifBallCatched);
    }

    private Action at(ActionType a) {
        return new Action(a);
    }

}
