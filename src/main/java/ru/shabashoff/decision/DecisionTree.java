package ru.shabashoff.decision;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import ru.shabashoff.entity.Player;

@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class DecisionTree {
    Node head;

    public DecisionTree(Node head) {
        this.head = head;
    }

    public void action(Player player){
        head.run(player);
    }
}
