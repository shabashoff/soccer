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
    private DecisionTree<String> getPlayerTree() {
        /*Decision ifCanCatch = new Decision(at(ActionType.CATCH), at(ActionType.GO_TO_BALL), 5, IfStatement.LESS_EQ, BigDecimal.valueOf(1.5));

        Decision ifSeeBall = new Decision(ifCanCatch, at(ActionType.ROTATE_RIGHT), 3, IfStatement.NON_EQ, null);//dont see ball

        Decision ifBallCatched = new Decision(at(ActionType.KICK_IN_GATE), ifSeeBall, 7, IfStatement.MORE, BigDecimal.valueOf(0));

        return new DecisionTree(ifBallCatched);*/

        return DecisionTree.loadFromFile("soccer-player/testSample.dt");
    }

    @SneakyThrows
    private DecisionTree<String> getGoalkeeperTree() {
        //return new DecisionTree(at(ActionType.ROTATE_RIGHT));
        return DecisionTree.loadFromFile("soccer-player/testSample.dt");
    }

    private void addPlayer(Player player) {
        players.add(player);
    }

}
