package ru.shabashoff.decision;

import ru.shabashoff.entity.Player;

import java.util.function.Consumer;

public enum ActionType {
    CATCH(Player::catchAction),
    DASH(Player::dashAction),
    KICK_IN_GATE(Player::kickInGateAction),
    ROTATE_RIGHT(Player::rotateRightAction),
    GO_TO_BALL(Player::goToBallAction);

    private final Consumer<Player> action;

    ActionType(Consumer<Player> action) {
        this.action = action;
    }


    public void run(Player p) {
        action.accept(p);
    }
}
