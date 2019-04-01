package ru.shabashoff.decision;

import lombok.extern.log4j.Log4j;
import org.junit.Assert;
import org.junit.Test;

import java.io.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Log4j
public class TestDecisionTree {

    @Test
    public void testWrite() throws IOException {
        Action rl = new Action(ActionType.CATCH);
        Decision decision = new Decision(rl, rl, 1, IfStatement.MORE, BigDecimal.valueOf(1.1));
        DecisionTree dt = new DecisionTree(decision);

        ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("tt.dt"));

        out.writeObject(dt);
        out.close();
    }

    @Test
    public void testRead() throws IOException, ClassNotFoundException {
        Action rl = new Action(ActionType.CATCH);
        Decision decision = new Decision(rl, rl, 1, IfStatement.MORE, BigDecimal.valueOf(1.1));
        DecisionTree dtCheck = new DecisionTree(decision);

        ObjectInputStream in = new ObjectInputStream(new FileInputStream("tt.dt"));

        DecisionTree dt = (DecisionTree) in.readObject();
        in.close();


        Assert.assertEquals(((Decision) dtCheck.getHead()).getIfStmt(), ((Decision) dt.getHead()).getIfStmt());
        Assert.assertEquals(((Decision) dtCheck.getHead()).getIndexOfParameter(), ((Decision) dt.getHead()).getIndexOfParameter());
        Assert.assertEquals(((Decision) dtCheck.getHead()).getLeft(), ((Decision) dt.getHead()).getLeft());
        Assert.assertEquals(((Decision) dtCheck.getHead()).getRight(), ((Decision) dt.getHead()).getRight());
    }

    @Test
    public void javaTest() throws IOException {
        Decision ifCanCatch = new Decision(at(ActionType.CATCH), at(ActionType.DASH), 5, IfStatement.LESS_EQ, BigDecimal.valueOf(2));

        Decision ifSmallAngle = new Decision(ifCanCatch, at(ActionType.GO_TO_BALL), 6, IfStatement.LESS_EQ, BigDecimal.valueOf(45));

        Decision ifSeeBall = new Decision(ifSmallAngle, at(ActionType.ROTATE_RIGHT), 3, IfStatement.NON_EQ, null);//dont see ball

        Decision ifBallCatched = new Decision(at(ActionType.KICK_IN_GATE), ifSeeBall, 7, IfStatement.MORE, BigDecimal.valueOf(0));

        DecisionTree dt = new DecisionTree(ifBallCatched);

        ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("tt.dt"));

        out.writeObject(dt);
        out.close();
    }

    @Test
    public void testEntropyCalc() {

        ArrayList<ActionType> at = new ArrayList<ActionType>();
        List<List<BigDecimal>> vector = new ArrayList<List<BigDecimal>>();

        for (int i = 0; i < 9; i++) {
            at.add(ActionType.KICK_IN_GATE);
        }

        for (int i = 0; i < 5; i++) {
            at.add(ActionType.GO_TO_BALL);
        }

        for (int i = 0; i < 14; i++) {
            vector.add(null);
        }

        DecisionTree dt = new DecisionTree(null);
        BigDecimal entropy = dt.calcEntropy(a -> true, vector, at);

        Assert.assertEquals(0.94, entropy.doubleValue(), 0.01);
    }


    private Action at(ActionType a) {
        return new Action(a);
    }
}
