package ru.shabashoff.decision;

import lombok.AccessLevel;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.FieldDefaults;

import java.io.IOException;
import java.io.ObjectStreamException;
import java.io.Serializable;
import java.math.BigDecimal;

@Data
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Decision implements Node, Serializable {

    static final long serialVersionUID = 1L;

    DecisionRunnable decisionRunnableNew = this::check;
    Node left, right;

    int indexOfParameter;
    IfStatement ifStmt;
    BigDecimal val;

    public Decision(Node left, Node right, int indexOfParameter, IfStatement ifStmt, BigDecimal val) {
        this.left = left;
        this.right = right;
        this.indexOfParameter = indexOfParameter;
        this.ifStmt = ifStmt;
        this.val = val;
    }

    private boolean check(BigDecimal[] nums) {
        return ifStmt.getFunc().apply(nums[indexOfParameter], val);
    }

    @Override
    public int run(BigDecimal[] vector) {
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
        left = (Action) in.readObject();
        right = (Action) in.readObject();
    }


    private void readObjectNoData() throws ObjectStreamException {}
}


