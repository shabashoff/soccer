package ru.shabashoff.server;

import lombok.AccessLevel;
import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;
import lombok.extern.log4j.Log4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Log4j
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class Team {
    List<Player> players = new ArrayList<>();

    public Team(int countPlayers, String teamName) {
        for (int i = 0; i < countPlayers; i++) {
            players.add(new Player(i, teamName));
        }
    }

    @SneakyThrows
    public void simpleStrategy() {
        Random rn = new Random();

        while (true) {
            for (Player player : players) {
                player.move(rn.nextInt(100), rn.nextInt(100));
            }
            Thread.sleep(10_000);
        }
    }
}
