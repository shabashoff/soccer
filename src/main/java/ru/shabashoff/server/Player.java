package ru.shabashoff.server;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.log4j.Log4j;

import java.text.MessageFormat;

@Log4j
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class Player {
    int id;
    String teamName;
    RCSSServerClient rcssPlayer;


    public Player(int id, String teamName) {
        this.id = id;
        rcssPlayer = new RCSSServerClient(teamName);
        this.teamName = teamName;
    }

    public void move(int x, int y) {
        rcssPlayer.sendMessage(MessageFormat.format("(move {0} {1})", x, y));
    }

    public void dash(int power) {
        rcssPlayer.sendMessage(MessageFormat.format("(dash {0})", power));
    }

    public void turn(int angle) {
        rcssPlayer.sendMessage(MessageFormat.format("(turn {0})", angle));
    }
}
