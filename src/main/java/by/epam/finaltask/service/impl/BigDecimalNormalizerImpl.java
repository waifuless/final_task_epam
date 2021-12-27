package by.epam.finaltask.service.impl;

import by.epam.finaltask.service.BigDecimalNormalizer;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class BigDecimalNormalizerImpl implements BigDecimalNormalizer {

    private final static int DEFAULT_SCALE = 2;
    private final static RoundingMode DEFAULT_ROUNDING_MODE = RoundingMode.HALF_UP;

    private static volatile BigDecimalNormalizer instance;

    public static BigDecimalNormalizer getInstance() {
        if (instance == null) {
            synchronized (BigDecimalNormalizerImpl.class) {
                if (instance == null) {
                    instance = new BigDecimalNormalizerImpl();
                }
            }
        }
        return instance;
    }

    @Override
    public BigDecimal normalize(BigDecimal number) {
        return number.setScale(DEFAULT_SCALE, DEFAULT_ROUNDING_MODE);
    }
}
