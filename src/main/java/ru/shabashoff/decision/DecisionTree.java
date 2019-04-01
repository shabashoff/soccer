package ru.shabashoff.decision;

import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.shabashoff.Utils.GameUtils;
import ru.shabashoff.entity.Player;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.function.Predicate;

@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class DecisionTree implements Serializable {
    static final long serialVersionUID = 1L;

    static final long SPLIT_COUNT = 10;

    @Getter
    Node head;

    public DecisionTree(Node head) {
        this.head = head;
    }

    public void action(Player player) {
        head.run(player.getSnapshot()).run(player);
    }


    public void train(List<List<BigDecimal>> vector, List<ActionType> classes) {
        BigDecimal entropy = calcEntropy(a -> true, vector, classes);


    }

    public void train(BigDecimal[][] vector, ActionType[] classes) {
        assert vector.length == classes.length;

        int[] clsL = new int[classes.length];
        int[] clsR = new int[classes.length];
        PriorityQueue<TrainingPair> sorted = new PriorityQueue<>(Comparator.comparing(a -> a.val));

        for (int i = 0; i < vector[0].length; i++) {
            sorted.clear();

            Arrays.fill(clsL, 0);
            Arrays.fill(clsR, 0);

            for (int j = 0; j < vector.length; j++) {
                sorted.add(new TrainingPair(vector[j][i], j));
                clsL[j]++;
            }

            for (TrainingPair trainingPair : sorted) {

            }


        }


    }

    public BigDecimal calcEntropy(Predicate<List<BigDecimal>> pred, List<List<BigDecimal>> vector, List<ActionType> classes) {
        Map<ActionType, Integer> map = new HashMap<>();

        for (int i = 0; i < classes.size(); i++) {
            ActionType a = classes.get(i);
            if (pred.test(vector.get(i))) {
                if (map.containsKey(a)) {
                    map.put(a, map.get(a) + 1);
                } else {
                    map.put(a, 1);
                }
            }
        }

        BigDecimal sum = BigDecimal.ZERO;
        BigDecimal len = BigDecimal.valueOf(classes.size());

        for (Map.Entry<ActionType, Integer> e : map.entrySet()) {
            BigDecimal divide = BigDecimal.valueOf(e.getValue()).divide(len, 5, RoundingMode.CEILING);

            sum = sum.add(divide.negate().multiply(GameUtils.log2(divide)));
        }

        return sum;
    }

    @RequiredArgsConstructor
    static class TrainingPair {
        final BigDecimal val;
        final int pos;
        BigDecimal gain;
    }

}
