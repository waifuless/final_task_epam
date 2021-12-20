package by.epam.finaltask.validation;

import by.epam.finaltask.exception.ClientErrorException;

public interface StringClientParameterValidator {

    void validateNotEmpty(String str, String... others) throws ClientErrorException;
}
