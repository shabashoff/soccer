package ru.shabashoff.server;

import lombok.AccessLevel;
import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;
import lombok.extern.log4j.Log4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

@Log4j
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Team {
    final List<Player> players = new ArrayList<>();
    static int teamCounts = 1;


    public Team() {
        this(11, "team-" + teamCounts++);
    }


    public Team(int countPlayers, String teamName) {
        for (int i = 0; i < countPlayers; i++) {
            players.add(new Player(i, teamName));
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
                    log.info("Enter angle: ");
                    int power;
                    power = sc.nextInt();
                    for (Player player : players) {
                        player.dash(power);
                    }
                    break;
            }
        }
    }
}
