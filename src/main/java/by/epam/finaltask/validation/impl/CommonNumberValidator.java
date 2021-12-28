package by.epam.finaltask.validation.impl;

import by.epam.finaltask.exception.ClientError;
import by.epam.finaltask.exception.ClientErrorException;
import by.epam.finaltask.validation.NumberValidator;

public class CommonNumberValidator implements NumberValidator {

    CommonNumberValidator() {
    }

    @Override
    public void validateNumberIsPositive(long id, long... others) throws ClientErrorException {
        validateOneIdIsPositive(id);
        for (long other : others) {
            validateOneIdIsPositive(other);
        }
    }

    private void validateOneIdIsPositive(long id) throws ClientErrorException {
        if (id < 1) {
            throw new ClientErrorException(ClientError.INVALID_ARGUMENTS);
        }
    }
}
