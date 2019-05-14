package ru.shabashoff.decision;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.ToString;
import lombok.extern.log4j.Log4j;
import ru.shabashoff.utils.TreeUtils;

import static ru.shabashoff.utils.TreeUtils.bd;
import static ru.shabashoff.utils.TreeUtils.divide;

@Log4j
@SuppressWarnings("Duplicates")
public class C45<T> {
    private static BigDecimal DEFAULT_NULL = BigDecimal.ZERO;
    private static final int COUNT_SEP = 20;

    private final Map<Integer, T> classes = new HashMap<>();


    @SneakyThrows
    public DecisionTree<T> trainModel(BigDecimal[][] vector, T[] ccc) {
        int[] ints = new int[vector[0].length];

        Arrays.fill(ints, COUNT_SEP);

        return trainModel(vector, ccc, ints);
    }


    @SneakyThrows
    public DecisionTree<T> trainModel(BigDecimal[][] vector, T[] ccc, final int[] ints) {

        for (BigDecimal[] bd : vector) {
            for (int i = 0; i < bd.length; i++) {
                if (bd[i] == null) bd[i] = DEFAULT_NULL;
            }
        }

        int[] clss = createClasses(ccc);

        if (vector.length != clss.length) {
            throw new IllegalArgumentException("Sizes is illegal");
        }

        int ctnClasses = 0;

        InputVector[] inputVectors = new InputVector[vector.length];

        for (int i = 0; i < clss.length; i++) {
            int cls = clss[i];
            inputVectors[i] = new InputVector(vector[i], cls);

            ctnClasses = Integer.max(ctnClasses, cls + 1);
        }

        return new DecisionTree<>(trainModel(inputVectors, ctnClasses, Arrays.stream(ints).sum(), ints));
    }

    @SneakyThrows
    private Node<T> trainModel(InputVector[] vector, int ctnClasses, int remCls, int[] remaining) {

        if (remCls == 0) {
            int[] clss = new int[ctnClasses];
            for (InputVector iv : vector) {
                clss[iv.cls]++;
            }

            int mx = 0;

            for (int j = 0; j < clss.length; j++) {
                if (clss[j] > clss[mx]) mx = j;
            }

            return new Action<>(classes.get(mx));
        }

        final Map<Integer, TrainingPair> map = Collections.synchronizedMap(new HashMap<>());

        for (int i = 0; i < vector[0].vector.length; i++) {
            if (remaining[i] > 0) {
                TrainingPair mtp = getMaxGainTrainingPair(vector, ctnClasses, i);

                if (mtp != null) map.put(i, mtp);
            }
        }


        if (map.isEmpty()) return new Action<>(classes.get(getMaxClass(vector, ctnClasses)));

        BigDecimal max = BigDecimal.ONE.negate();
        Integer curPos = 0;

        for (Map.Entry<Integer, TrainingPair> pp : map.entrySet()) {
            if (max.compareTo(pp.getValue().gain) < 0) {
                max = pp.getValue().gain;
                curPos = pp.getKey();
            }
        }

        final Integer finalCurPos = curPos;
        Arrays.sort(vector, Comparator.comparing(v -> v.vector[finalCurPos]));

        int i = Arrays.binarySearch(vector, map.get(finalCurPos).val, ((o1, o2) -> {
            if (o1 instanceof InputVector) return ((InputVector) o1).vector[finalCurPos].compareTo((BigDecimal) o2);

            return ((BigDecimal) o1).compareTo(((InputVector) o2).vector[finalCurPos]);
        }));

        InputVector[] left = Arrays.copyOfRange(vector, 0, i + 1);
        InputVector[] right = Arrays.copyOfRange(vector, i + 1, vector.length);

        remaining[finalCurPos]--;

        return new Decision<>(
                trainModel(left, ctnClasses, remCls - 1, Arrays.copyOf(remaining, remaining.length)),
                trainModel(right, ctnClasses, remCls - 1, Arrays.copyOf(remaining, remaining.length)),
                finalCurPos,
                IfStatement.LESS_EQ,
                map.get(finalCurPos).val
        );
    }

    private int getMaxClass(InputVector[] vector, int ctnClasses) {
        int[] clss = new int[ctnClasses];
        for (InputVector iv : vector) {
            clss[iv.cls]++;
        }

        int mx = 0;

        for (int j = 0; j < clss.length; j++) {
            if (clss[j] > clss[mx]) mx = j;
        }

        return mx;

    }

    private TrainingPair getMaxGainTrainingPair(InputVector[] vector, int cntClasses, int i) {//TODO:Error occur bcz size is small!!!
        List<TrainingPair> tp = getTrainingPair(vector, cntClasses, i);

        if (tp == null || tp.isEmpty()) return null;

        return Collections.max(tp, Comparator.comparing(o -> o.gain));
    }

    private List<TrainingPair> getTrainingPair(InputVector[] vector, int cntClasses, int i) {

        if (vector.length <= 1) return null;

        List<TrainingPair> trainPairs = new ArrayList<>();

        int[] clsL = new int[cntClasses];
        int[] clsR = new int[cntClasses];

        Arrays.fill(clsL, 0);
        Arrays.fill(clsR, 0);

        for (int j = 0; j < vector.length; j++) {
            trainPairs.add(new TrainingPair(vector[j].vector[i], vector[j].cls));
        }

        for (int j = 0; j < vector.length; j++) {
            clsR[vector[j].cls]++;
        }

        trainPairs.sort(Comparator.comparing(a -> a.val));

        BigDecimal fullEntropy = calcEntropy(clsR, bd(vector.length));
        TrainingPair tp;

        for (int j = 0; j < vector.length - 1; j++) {
            while (j < vector.length - 2 && trainPairs.get(j).val.equals(trainPairs.get(j + 1).val)) {
                tp = trainPairs.get(j);
                clsR[tp.cls]--;
                clsL[tp.cls]++;

                j++;
            }

            tp = trainPairs.get(j);

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

        return trainPairs.stream().filter(o -> o.gainRatio != null).collect(Collectors.toList());
    }


    private BigDecimal calcEntropy(int[] clsCount, BigDecimal cls) {
        BigDecimal entropy = BigDecimal.ZERO;
        for (int i1 : clsCount) {
            if (i1 != 0) {
                BigDecimal divide = BigDecimal.valueOf(i1).divide(cls, 5, RoundingMode.CEILING);
                entropy = entropy.add(divide.multiply(TreeUtils.log2(divide)).negate());
            }
        }
        return entropy;
    }

    private int[] createClasses(T[] classes) {
        int[] clss = new int[classes.length];
        Map<T, Integer> cc = new HashMap<>();
        int cur = 0;

        for (int i = 0; i < classes.length; i++) {
            T t = classes[i];
            if (cc.containsKey(t)) {
                clss[i] = cc.get(t);
            } else {
                this.classes.put(cur, t);
                cc.put(t, cur);
                clss[i] = cur;
                cur++;
            }
        }
        return clss;
    }


    @Data
    @RequiredArgsConstructor
    static class InputVector {
        private final BigDecimal[] vector;
        private final int cls;
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
