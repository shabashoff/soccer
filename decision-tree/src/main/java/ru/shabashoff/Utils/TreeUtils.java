package ru.shabashoff.Utils;

import lombok.extern.log4j.Log4j;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Log4j
public class TreeUtils {

    public static boolean isZero(BigDecimal bd) {
        return bd.compareTo(bd(0.0000001)) < 1;
    }

    public static BigDecimal log2(BigDecimal val) {
        return BigDecimal.valueOf(Math.log(val.doubleValue()) / Math.log(2));
    }

    public static BigDecimal bd(int n) {
        return BigDecimal.valueOf(n);
    }

    public static BigDecimal bd(double n) {
        return BigDecimal.valueOf(n);
    }

    public static BigDecimal bd(long n) {
        return BigDecimal.valueOf(n);
    }

    public static BigDecimal divide(BigDecimal a, BigDecimal b) {
        return a.divide(b, 5, RoundingMode.CEILING);
    }
}
