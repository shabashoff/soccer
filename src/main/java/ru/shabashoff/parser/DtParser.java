package ru.shabashoff.parser;

import lombok.AccessLevel;
import lombok.Setter;
import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;
import ru.shabashoff.Utils.GameUtils;
import ru.shabashoff.decision.DecisionTree;
import ru.shabashoff.decision.Node;
import ru.shabashoff.entity.Player;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiPredicate;
import java.util.function.Supplier;

@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class DtParser {

    BufferedReader in;

    Map<String, Supplier> getters = new HashMap<>();

    static Map<String, BiPredicate> predicateMap = new HashMap<>();


    static {
        predicateMap.put("==", GameUtils::eqWithNull);
        predicateMap.put(">=", (e, u) -> GameUtils.compare((Comparable<Number>) e, (Number) u) >= 0);
        predicateMap.put("<=", (e, u) -> GameUtils.compare((Comparable<Number>) e, (Number) u) <= 0);
        predicateMap.put(">", (e, u) -> GameUtils.compare((Comparable<Number>) e, (Number) u) > 0);
        predicateMap.put("<", (e, u) -> GameUtils.compare((Comparable<Number>) e, (Number) u) < 0);

    }


    @SneakyThrows
    public DtParser(File file, Player player) {
        this.in = new BufferedReader(new FileReader(file));

        getters.put("getExpectedPoint", player::getExpectedPoint);
        getters.put("getExpectedAngle", player::getExpectedAngle);
        getters.put("getBallPoint", player::getBallPoint);

    }


    public DecisionTree parse() {
        return new DecisionTree(parsing(in));
    }

    @SneakyThrows
    public Node parsing(BufferedReader in) {
        String[] st = in.readLine().split(" ");
        if (st[0].equals("IF")) {
            if (getters.containsKey(st[1])){

            }else{

            }
        }

        return null;
    }

    static class Val<T> {
        T num;
        Supplier<T> supplier;

        public Val(T num) {
            this.num = num;
        }

        public Val(Supplier<T> supplier) {
            this.supplier = supplier;
        }

        T get() {
            if (num != null) return num;

            return supplier.get();
        }
    }

    @Setter
    static class Condition<T, U> {
        BiPredicate<T, U> predicate;
        Val<T> left;
        Val<U> right;

        public Condition(BiPredicate<T, U> predicate) {
            this.predicate = predicate;
        }
    }

}
