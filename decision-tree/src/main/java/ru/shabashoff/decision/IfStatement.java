package ru.shabashoff.decision;

import lombok.Getter;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.function.BiFunction;

public enum IfStatement {
    MORE(">", (a, b) -> a.compareTo(b) > 0),
    MORE_EQ(">=", (a, b) -> a.compareTo(b) >= 0),
    LESS("<", (a, b) -> a.compareTo(b) < 0),
    LESS_EQ("<=", (a, b) -> a.compareTo(b) <= 0),
    EQ("==", Objects::equals),
    NON_EQ("!=", (a, b) -> !Objects.equals(a, b));

    private final String text;

    @Getter
    private final BiFunction<BigDecimal, BigDecimal, Boolean> func;

    IfStatement(final String text, BiFunction<BigDecimal, BigDecimal, Boolean> f) {
        this.text = text;
        func = f;
    }

    @Override
    public String toString() {
        return text;
    }

    public static IfStatement parse(String s) {

        for (IfStatement is : IfStatement.values()) {
            if (s.equals(is.toString())) {
                return is;
            }
        }

        throw new IllegalArgumentException("Can't parse string " + s);
    }
}
