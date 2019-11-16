package top.starrysea.rina.util.number;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;

public class NumberUtil {

    public static BigInteger add(Integer... numbers) {
        return Arrays.asList(numbers).stream().map(BigInteger::valueOf).reduce(BigInteger::add).orElse(BigInteger.ZERO);
    }

    public static BigInteger add(BigInteger... numbers) {
        return Arrays.asList(numbers).stream().reduce(BigInteger::add).orElse(BigInteger.ZERO);
    }

    public static BigDecimal add(Double... numbers) {
        return Arrays.asList(numbers).stream().map(BigDecimal::valueOf).reduce(BigDecimal::add).orElse(BigDecimal.ZERO);
    }

    public static BigDecimal add(BigDecimal... numbers) {
        return Arrays.asList(numbers).stream().reduce(BigDecimal::add).orElse(BigDecimal.ZERO);
    }
}
