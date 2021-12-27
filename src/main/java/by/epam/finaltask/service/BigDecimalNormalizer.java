package by.epam.finaltask.service;

import by.epam.finaltask.service.impl.BigDecimalNormalizerImpl;

import java.math.BigDecimal;

public interface BigDecimalNormalizer {

    static BigDecimalNormalizer getInstance() {
        return BigDecimalNormalizerImpl.getInstance();
    }

    BigDecimal normalize(BigDecimal number);
}
