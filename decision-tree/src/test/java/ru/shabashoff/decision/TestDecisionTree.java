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
        Action rl = new Action(1);
        Decision decision = new Decision(rl, rl, 1, IfStatement.MORE, BigDecimal.valueOf(1.1));
        DecisionTree dt = new DecisionTree(decision);

        ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("tt.dt"));

        out.writeObject(dt);
        out.close();
    }

    @Test
    public void testRead() throws IOException, ClassNotFoundException {
        Action rl = new Action(1);
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
    public void testTrain() {
        BigDecimal[][] vector = new BigDecimal[14][2];
        Integer[] classes = new Integer[14];

        classes[0] = 0;//No
        classes[1] = 0;//No
        classes[2] = 1;//Yes
        classes[3] = 1;//Yes
        classes[4] = 1;//Yes
        classes[5] = 0;//No
        classes[6] = 1;//Yes
        classes[7] = 0;//No
        classes[8] = 1;//Yes
        classes[9] = 1;//Yes
        classes[10] = 1;//Yes
        classes[11] = 1;//Yes
        classes[12] = 1;//Yes
        classes[13] = 0;//No

        vector[0][0] = BigDecimal.valueOf(85);
        vector[1][0] = BigDecimal.valueOf(90);
        vector[2][0] = BigDecimal.valueOf(78);
        vector[3][0] = BigDecimal.valueOf(96);
        vector[4][0] = BigDecimal.valueOf(80);
        vector[5][0] = BigDecimal.valueOf(70);
        vector[6][0] = BigDecimal.valueOf(65);
        vector[7][0] = BigDecimal.valueOf(95);
        vector[8][0] = BigDecimal.valueOf(70);
        vector[9][0] = BigDecimal.valueOf(80);
        vector[10][0] = BigDecimal.valueOf(70);
        vector[11][0] = BigDecimal.valueOf(90);
        vector[12][0] = BigDecimal.valueOf(75);
        vector[13][0] = BigDecimal.valueOf(80);

        vector[0][1] = BigDecimal.valueOf(85);
        vector[1][1] = BigDecimal.valueOf(80);
        vector[2][1] = BigDecimal.valueOf(83);
        vector[3][1] = BigDecimal.valueOf(70);
        vector[4][1] = BigDecimal.valueOf(68);
        vector[5][1] = BigDecimal.valueOf(65);
        vector[6][1] = BigDecimal.valueOf(64);
        vector[7][1] = BigDecimal.valueOf(72);
        vector[8][1] = BigDecimal.valueOf(69);
        vector[9][1] = BigDecimal.valueOf(75);
        vector[10][1] = BigDecimal.valueOf(75);
        vector[11][1] = BigDecimal.valueOf(72);
        vector[12][1] = BigDecimal.valueOf(81);
        vector[13][1] = BigDecimal.valueOf(71);

        DecisionTree<Integer> dt = (new C45<Integer>()).trainModel(vector, classes);

        int right = 0;

        for (int i = 0; i < vector.length; i++) {
            if (dt.action(vector[i]).equals(classes[i])) right++;
        }


        System.out.println(dt);
        System.out.println("Accuracy=" + (double) right / (double) vector.length);
    }
}
