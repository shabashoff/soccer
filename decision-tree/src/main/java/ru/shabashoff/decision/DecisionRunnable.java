package ru.shabashoff.decision;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@FunctionalInterface
public interface DecisionRunnable extends Serializable {
    boolean run(BigDecimal[] nums);
}
