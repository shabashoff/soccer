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
    final Coach coach;
    static int teamCounts = 1;


    public Team() {
        this("team-" + teamCounts++);
    }


    private Team(String teamName) {

        Player gk = new Player(1, teamName, PlayerPosition.GOALKEEPER, getGoalkeeperTree());
        gk.move(-41, 0);
        addPlayer(gk);

        for (int i = 0; i < 4; i++) {
            addPlayer(new Player(i + 2, teamName, PlayerPosition.BACK, getPlayerTree()));
        }

        for (int i = 0; i < 4; i++) {
            addPlayer(new Player(i + 6, teamName, PlayerPosition.HALFBACK, getPlayerTree()));
        }

        for (int i = 0; i < 2; i++) {
            addPlayer(new Player(i + 10, teamName, PlayerPosition.FORWARD, getPlayerTree()));
        }

        coach = new Coach(teamName);

        coach.initPlayer(players.get(0));
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
    private DecisionTree getPlayerTree() {
        Decision ifCanCatch = new Decision(at(ActionType.CATCH), at(ActionType.GO_TO_BALL), 5, IfStatement.LESS_EQ, BigDecimal.valueOf(1.5));

        Decision ifSeeBall = new Decision(ifCanCatch, at(ActionType.ROTATE_RIGHT), 3, IfStatement.NON_EQ, null);//dont see ball

        Decision ifBallCatched = new Decision(at(ActionType.KICK_IN_GATE), ifSeeBall, 7, IfStatement.MORE, BigDecimal.valueOf(0));

        return new DecisionTree(ifBallCatched);
    }

    @SneakyThrows
    private DecisionTree getGoalkeeperTree() {
        return new DecisionTree(at(ActionType.ROTATE_RIGHT));
    }

    private Action at(ActionType a) {
        return new Action(a.getN());
    }

    private void addPlayer(Player player) {
        players.add(player);
    }

}
