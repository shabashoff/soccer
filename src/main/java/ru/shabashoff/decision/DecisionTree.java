package ru.shabashoff.decision;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import ru.shabashoff.entity.Player;

import java.io.Serializable;

@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class DecisionTree implements Serializable {
    Node head;

    public DecisionTree(Node head) {
        this.head = head;
    }

    public void action(Player player){
        head.run(player);
    }
}
