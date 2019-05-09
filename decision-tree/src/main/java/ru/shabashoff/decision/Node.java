package ru.shabashoff.decision;

import java.math.BigDecimal;
import java.util.List;

@FunctionalInterface
public interface Node<T> {
    T run(BigDecimal[] vector);
}
