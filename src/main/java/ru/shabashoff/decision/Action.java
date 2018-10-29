package ru.shabashoff.decision;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import ru.shabashoff.entity.Player;

@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class Action implements Node {
    ActionRunable runable;

    public Action(ActionRunable runable) {
        this.runable = runable;
    }

    @Override
    public void run(Player player) {
        runable.run(player); //TODO: add multithreading.
    }
}
