package ru.shabashoff.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;
import lombok.extern.log4j.Log4j;
import ru.shabashoff.decision.DecisionTree;
import ru.shabashoff.parser.MsgParser;

@Log4j
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Team implements Serializable {
    @Getter
    final List<Player> players = new ArrayList<>();
    final Coach coach;
    static int teamCounts = 1;

    public Team() {
        this("team-" + teamCounts++);
    }


    private Team(String teamName) {

        Player gk = new Player(1, teamName, PlayerPosition.GOALKEEPER, getDt());
        gk.move(-41, 0);
        addPlayer(gk);

        for (int i = 0; i < 4; i++) {
            addPlayer(new Player(i + 2, teamName, PlayerPosition.BACK, getDt()));
        }

        for (int i = 0; i < 4; i++) {
            addPlayer(new Player(i + 6, teamName, PlayerPosition.HALFBACK, getDt()));
        }

        for (int i = 0; i < 2; i++) {
            addPlayer(new Player(i + 10, teamName, PlayerPosition.FORWARD, getDt()));
        }

        coach = new Coach(teamName);

        coach.initPlayer(players.get(0));
    }

    @SneakyThrows
    protected DecisionTree<String> getDt() {
        //return new DecisionTree(at(ActionType.ROTATE_RIGHT));
        return DecisionTree.loadFromFile("soccer-player/testSample.dt");
    }

    private void addPlayer(Player player) {
        players.add(player);
    }

}
