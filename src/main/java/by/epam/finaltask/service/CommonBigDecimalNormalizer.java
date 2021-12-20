package by.epam.finaltask.service;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class CommonBigDecimalNormalizer implements BigDecimalNormalizer {

    private final static int DEFAULT_SCALE = 2;
    private final static RoundingMode DEFAULT_ROUNDING_MODE = RoundingMode.HALF_UP;

    private static volatile BigDecimalNormalizer instance;

    public static BigDecimalNormalizer getInstance() {
        if (instance == null) {
            synchronized (CommonBigDecimalNormalizer.class) {
                if (instance == null) {
                    instance = new CommonBigDecimalNormalizer();
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
