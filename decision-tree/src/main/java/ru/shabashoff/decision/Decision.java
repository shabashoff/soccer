package ru.shabashoff.decision;

import java.io.IOException;
import java.io.ObjectStreamException;
import java.io.Serializable;
import java.math.BigDecimal;
import lombok.AccessLevel;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.FieldDefaults;

@Data
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Decision<T> implements Node<T>, Serializable {

    static final long serialVersionUID = 1L;

    DecisionRunnable decisionRunnableNew = this::check;
    Node<T> left, right;

    int indexOfParameter;
    IfStatement ifStmt;
    BigDecimal val;

    public Decision(Node<T> left, Node<T> right, int indexOfParameter, IfStatement ifStmt, BigDecimal val) {
        this.left = left;
        this.right = right;
        this.indexOfParameter = indexOfParameter;
        this.ifStmt = ifStmt;
        this.val = val;
    }

    private boolean check(BigDecimal[] nums) {
        if (nums[indexOfParameter] == null) return true;

        return ifStmt.getFunc().apply(nums[indexOfParameter], val);
    }

    @Override
    public T run(BigDecimal[] vector) {
        if (decisionRunnableNew.run(vector)) return left.run(vector);
        else return right.run(vector);
    }

    private void writeObject(java.io.ObjectOutputStream out) throws IOException {
        out.writeObject(val);
        out.writeObject(ifStmt);
        out.writeObject(indexOfParameter);
        out.writeObject(left);
        out.writeObject(right);
    }

    private void readObject(java.io.ObjectInputStream in) throws IOException, ClassNotFoundException {
        val = (BigDecimal) in.readObject();
        ifStmt = (IfStatement) in.readObject();
        indexOfParameter = (int) in.readObject();
        left = (Node<T>) in.readObject();
        right = (Node<T>) in.readObject();
        decisionRunnableNew = this::check;
    }


    private void readObjectNoData() throws ObjectStreamException {}
}


