package ru.shabashoff.decision;

import lombok.Getter;
import ru.shabashoff.entity.Player;

import java.util.function.Consumer;

@Getter
public enum ActionType  {
    CATCH(Player::catchAction, 0),
    DASH(Player::dashAction, 1),
    KICK_IN_GATE(Player::kickInGateAction, 2),
    ROTATE_RIGHT(Player::rotateRightAction, 3),
    GO_TO_BALL(Player::goToBallAction, 4);

    private final Consumer<Player> action;
    private final int n;

    ActionType(Consumer<Player> action, int n) {
        this.action = action;
        this.n = n;
    }

    public void run(Player p) {
        action.accept(p);
    }

    public static ActionType getFromN(int n) {
        return values()[n];
    }
}
