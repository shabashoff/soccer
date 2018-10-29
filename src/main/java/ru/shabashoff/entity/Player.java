package ru.shabashoff.entity;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.log4j.Log4j;
import ru.shabashoff.entity.server.ErrorMessage;
import ru.shabashoff.entity.server.HearMessage;
import ru.shabashoff.entity.server.SeeMessage;
import ru.shabashoff.entity.server.SenseBody;

import java.text.MessageFormat;

@Log4j
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Player extends RCSSServerClient {
    final int id;
    final String teamName;

    double expectedX = 0.0;
    double expectedY = 0.0;

    double speedX = 0.0;
    double speedY = 0.0;

    volatile SenseBody sense;
    final Object[] senseMonitor = new Object[0];

    volatile SeeMessage see;
    final Object[] seeMonitor = new Object[0];

    public Player(int id, String teamName) {
        super(teamName);
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

    public void circleRun() {
        dash(50);
        turn(10);
    }

    @Override
    protected void onErrorMessage(ErrorMessage message) {
        log.error(message);
    }

    @Override
    protected void onHearMessage(HearMessage message) {
        log.debug(message);
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
        }
        circleRun();
    }
}
