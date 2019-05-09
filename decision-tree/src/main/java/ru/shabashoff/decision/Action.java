package ru.shabashoff.decision;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

import java.io.IOException;
import java.io.ObjectStreamException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@Data
@ToString
@AllArgsConstructor
public class Action<T> implements Node<T>, Serializable {
    static final long serialVersionUID = 2L;

    T command;

    public T run(BigDecimal[] player) {
        return command;
    }

    private void writeObject(java.io.ObjectOutputStream out) throws IOException {
        out.writeObject(command);
    }

    private void readObject(java.io.ObjectInputStream in) throws IOException, ClassNotFoundException {
        command = (T) in.readObject();
    }


    private void readObjectNoData() throws ObjectStreamException {
    }
}
