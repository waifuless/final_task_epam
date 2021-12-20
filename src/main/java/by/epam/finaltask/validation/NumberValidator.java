package by.epam.finaltask.validation;

import by.epam.finaltask.exception.ClientErrorException;

public interface NumberValidator {

    void validateNumberIsPositive(long id, long... others) throws ClientErrorException;
}
