package ru.shabashoff.decision;

import java.io.File;
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
public class DecisionTree<T> implements Serializable {
    static final long serialVersionUID = 1L;


    @Getter
    Node<T> head;

    public DecisionTree(Node<T> head) {
        this.head = head;
    }

    public T action(BigDecimal[] arr) {
        return head.run(arr);
    }

    @SneakyThrows
    public void saveToFile(String path) {
        ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(path));
        out.writeObject(this);
        out.close();
    }

    @SneakyThrows
    public static <T> DecisionTree<T> loadFromFile(String path) {
        System.out.println(new File(path).getCanonicalPath());
        ObjectInputStream in = new ObjectInputStream(new FileInputStream(path));
        DecisionTree<T> dt = (DecisionTree<T>) in.readObject();
        in.close();

        return dt;
    }
}
