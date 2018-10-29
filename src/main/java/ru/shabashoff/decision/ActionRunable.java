package ru.shabashoff.decision;

import ru.shabashoff.entity.Player;

@FunctionalInterface
public interface ActionRunable {
    void run(Player player);
}
