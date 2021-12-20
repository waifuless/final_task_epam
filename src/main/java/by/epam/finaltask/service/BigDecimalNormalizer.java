package by.epam.finaltask.service;

import java.math.BigDecimal;

public interface BigDecimalNormalizer {

    static BigDecimalNormalizer getInstance() {
        return CommonBigDecimalNormalizer.getInstance();
    }

    BigDecimal normalize(BigDecimal number);
}
