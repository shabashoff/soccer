package ru.shabashoff.decision;

import ru.shabashoff.entity.Player;

@FunctionalInterface
public interface Node {
    void run(Player player);
}
