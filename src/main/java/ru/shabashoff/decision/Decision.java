package ru.shabashoff.decision;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import ru.shabashoff.entity.Player;

import java.io.Serializable;

@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class Decision implements Node , Serializable {
    DecisionRunable runable;

    Node left, right;


    public Decision(Node left, Node right, DecisionRunable runable) {
        this.left = left;
        this.right = right;
        this.runable = runable;
    }

    @Override
    public void run(Player player) {
        if (runable.run(player)) left.run(player);
        else right.run(player);
    }
}
