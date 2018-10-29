package ru.shabashoff.decision;

import ru.shabashoff.entity.Player;

@FunctionalInterface
public interface DecisionRunable {
    boolean run(Player player);
}
