package ru.shabashoff.entity;

import lombok.AccessLevel;
import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;
import lombok.extern.log4j.Log4j;
import ru.shabashoff.decision.Action;
import ru.shabashoff.decision.DecisionTree;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

@Log4j
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Team {
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

    private DecisionTree createSimpleTree() {

        final Point[] mainPoints = new Point[]{new Point(-40, -20), new Point(40, -20), new Point(40, 20), new Point(-40, 20)};

        final double minLen = 1.0;

        class Wrapper {
            private int n = 1;

            public int getN() {
                return n;
            }

            public void add() {
                n = (n + 1) % mainPoints.length;
            }
        }

        final Wrapper curElem = new Wrapper();

        return new DecisionTree(new Action(r -> {
            log.info("Expected point: " + r.getExpectedPoint());
            r.goTo(mainPoints[curElem.getN()].getX(), mainPoints[curElem.getN()].getY());

            if (new Vector(r.getExpectedPoint(), mainPoints[curElem.getN()]).getLength() < minLen) curElem.add();

        }));
    }
}
