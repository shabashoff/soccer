package ru.shabashoff;

import org.junit.Test;
import ru.shabashoff.decision.DecisionTree;
import ru.shabashoff.entity.Player;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class LogToTrain {
    TrainPlayer tp = new TrainPlayer(1, "1", null);

    @Test
    public void test() throws FileNotFoundException {
        int side = 0;
        FileInputStream fi = new FileInputStream("../ts.rcl");


        Scanner sc = new Scanner(fi);


        System.out.println(sc.nextLine());
    }


    static class TrainPlayer extends Player {

        public TrainPlayer(int id, String teamName, DecisionTree tree) {
            super(id, teamName, tree);
        }
    }
}
