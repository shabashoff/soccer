package ru.shabashoff.decision;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.IOException;
import java.io.ObjectStreamException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
public class Action implements Node, Serializable {
    static final long serialVersionUID = 1L;

    int type;

    public int run(BigDecimal[] player) {
        return type;
    }

    private void writeObject(java.io.ObjectOutputStream out) throws IOException {
        out.writeObject(type);
    }

    private void readObject(java.io.ObjectInputStream in) throws IOException, ClassNotFoundException {
        type = (int) in.readObject();
    }


    private void readObjectNoData() throws ObjectStreamException {
    }
}
