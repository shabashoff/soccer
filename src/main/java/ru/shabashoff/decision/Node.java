package ru.shabashoff.decision;

import java.math.BigDecimal;
import java.util.List;

@FunctionalInterface
public interface Node {
    ActionType run(List<BigDecimal> vector);
}
