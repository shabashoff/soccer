package ru.shabashoff.decision;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@FunctionalInterface
public interface DecisionRunable extends Serializable {
    boolean run(BigDecimal[] nums);
}
