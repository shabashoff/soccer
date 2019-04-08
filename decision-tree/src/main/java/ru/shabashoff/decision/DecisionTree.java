package ru.shabashoff.decision;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;
import java.math.BigDecimal;

@SuppressWarnings("Duplicates")
@ToString
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class DecisionTree implements Serializable {
    static final long serialVersionUID = 1L;


    @Getter
    Node head;

    public DecisionTree(Node head) {
        this.head = head;
    }

    public int action(BigDecimal[] arr) {
        return head.run(arr);
    }
}
