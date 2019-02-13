package ru.shabashoff.decision;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
public class ReturnArguments {
    ActionType type;
    private final List<BigDecimal> vector;
}
