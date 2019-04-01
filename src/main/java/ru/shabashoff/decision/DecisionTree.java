package ru.shabashoff.decision;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import ru.shabashoff.Utils.GameUtils;
import ru.shabashoff.entity.Player;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class DecisionTree implements Serializable {
    static final long serialVersionUID = 1L;

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

    public BigDecimal calcEntropy(Predicate<List<BigDecimal>> pred, List<List<BigDecimal>> vector, List<ActionType> classes) {
        Map<ActionType, Integer> map = new HashMap<>();

        for (int i = 0; i < classes.size(); i++) {
            ActionType a = classes.get(i);
            if (pred.test(vector.get(i))) {
                if (map.containsKey(a)) {
                    map.put(a, map.get(a) + 1);
                }
                else {
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

}
