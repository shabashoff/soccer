package ru.shabashoff.decision;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.math.BigDecimal;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.ToString;
import lombok.experimental.FieldDefaults;

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

    @SneakyThrows
    public void saveToFile(String path) {
        ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(path));
        out.writeObject(this);
        out.close();
    }

    @SneakyThrows
    public static DecisionTree loadFromFile(String path) {
        ObjectInputStream in = new ObjectInputStream(new FileInputStream(path));
        DecisionTree dt = (DecisionTree) in.readObject();
        in.close();

        return dt;
    }
}
