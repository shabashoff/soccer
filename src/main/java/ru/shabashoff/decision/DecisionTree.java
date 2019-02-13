package ru.shabashoff.decision;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import ru.shabashoff.entity.Player;

import java.io.Serializable;

@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class DecisionTree implements Serializable {
    static final long serialVersionUID = 1L;

    @Getter
    Node head;

    public DecisionTree(Node head) {
        this.head = head;
    }

    public void action(Player player) {
        head.run(player.getSnapshot()).run(player);
    }
}
