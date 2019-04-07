package ru.shabashoff.decision;

import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.shabashoff.Utils.TreeUtils;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.function.Predicate;

@SuppressWarnings("Duplicates")
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class DecisionTree implements Serializable {
    static final long serialVersionUID = 1L;

    static final long SPLIT_COUNT = 10;

    @Getter
    Node head;

    public DecisionTree(Node head) {
        this.head = head;
    }

    public int action(BigDecimal[] arr) {
        return head.run(arr);
    }

    public List<TrainingPair> train(BigDecimal[][] vector, int[] classes) {
        assert vector.length == classes.length;

        int ctnClasses = 0;
        for (int aClass : classes) {
            ctnClasses = Integer.max(ctnClasses, aClass + 1);
        }

        int[] classesCount = new int[ctnClasses];

        for (int aClass : classes) {
            classesCount[aClass]++;
        }
        int countZero = 0;

        for (int i : classesCount) if (i == 0) countZero++;

        int[] clsL = new int[ctnClasses];
        int[] clsR = new int[ctnClasses];

        List<TrainingPair> sorted = new ArrayList<>();

        BigDecimal entL;
        BigDecimal entR;

        BigDecimal sizeClasses = bd(classesCount.length - countZero);

        for (int i = 0; i < vector[0].length; i++) {
            getTrainingPair(vector, classes, ctnClasses,i);
        }

        return null;
    }


    private TrainingPair onlyFirstTrainigPair(BigDecimal[][] vector, int[] classes, int cntClasses, int i) {
        return getTrainingPair(vector, classes, cntClasses, i).get(0);
    }


    public static List<TrainingPair> getTrainingPair(BigDecimal[][] vector, int[] classes, int cntClasses, int i) {
        List<TrainingPair> trainPairs = new ArrayList<>();

        int[] clsL = new int[cntClasses];
        int[] clsR = new int[cntClasses];

        Arrays.fill(clsL, 0);
        Arrays.fill(clsR, 0);

        for (int j = 0; j < vector.length; j++) {
            trainPairs.add(new TrainingPair(vector[j][i], classes[j]));
        }

        for (int j = 0; j < vector.length; j++) {
            clsR[classes[j]]++;
        }

        trainPairs.sort(Comparator.comparing(a -> a.val));

        BigDecimal fullEntropy = calcEntropy(clsR, bd(vector.length));

        for (int j = 0; j < vector.length - 1; j++) {
            TrainingPair tp = trainPairs.get(j);

            clsR[tp.cls]--;
            clsL[tp.cls]++;

            int vl = j + 1;
            int vr = vector.length - vl;

            tp.entropyLeft = calcEntropy(clsL, bd(vl));
            tp.entropyRight = calcEntropy(clsR, bd(vr));

            tp.gain = fullEntropy
                    .subtract(divide(bd(vl), bd(vector.length)).multiply(tp.entropyLeft))
                    .subtract(divide(bd(vr), bd(vector.length)).multiply(tp.entropyRight));

            BigDecimal lc = divide(bd(vl), bd(vector.length));
            BigDecimal rc = divide(bd(vr), bd(vector.length));

            tp.splitInfo = lc.multiply(TreeUtils.log2(lc)).add(rc.multiply(TreeUtils.log2(rc))).negate();

            tp.gainRatio = divide(tp.gain, tp.splitInfo);
        }

        trainPairs.remove(trainPairs.size() - 1);

        trainPairs.sort((o1, o2) -> o2.gainRatio.compareTo(o1.gainRatio));

        return trainPairs;
    }


    private static BigDecimal divide(BigDecimal a, BigDecimal b) {
        return a.divide(b, 5, RoundingMode.CEILING);
    }

    private static BigDecimal bd(int n) {
        return BigDecimal.valueOf(n);
    }


    private static BigDecimal bd(long n) {
        return BigDecimal.valueOf(n);
    }

    public static BigDecimal calcEntropy(int[] clsCount, BigDecimal cls) {
        BigDecimal entropy = BigDecimal.ZERO;
        for (int i1 : clsCount) {
            if (i1 != 0) {
                BigDecimal divide = BigDecimal.valueOf(i1).divide(cls, 5, RoundingMode.CEILING);
                entropy = entropy.add(divide.multiply(TreeUtils.log2(divide)).negate());
            }
        }
        return entropy;
    }

    @ToString
    @RequiredArgsConstructor
    static class TrainingPair {
        final BigDecimal val;
        final int cls;
        BigDecimal entropyLeft;
        BigDecimal entropyRight;
        BigDecimal gain;
        BigDecimal splitInfo;
        BigDecimal gainRatio;
    }

}
