package ru.shabashoff.Utils;

import lombok.extern.log4j.Log4j;

import java.math.BigDecimal;

@Log4j
public class TreeUtils {
    public static BigDecimal log2(BigDecimal val) {
        return BigDecimal.valueOf(Math.log(val.doubleValue()) / Math.log(2));
    }
}
