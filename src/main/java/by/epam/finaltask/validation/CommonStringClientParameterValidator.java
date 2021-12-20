package by.epam.finaltask.validation;

import by.epam.finaltask.exception.ClientError;
import by.epam.finaltask.exception.ClientErrorException;

public class CommonStringClientParameterValidator implements StringClientParameterValidator {

    CommonStringClientParameterValidator() {
    }

    @Override
    public void validateNotEmpty(String str, String... others) throws ClientErrorException {
        validateStringNotEmpty(str);
        for (String other : others) {
            validateStringNotEmpty(other);
        }
    }

    private void validateStringNotEmpty(String str) throws ClientErrorException {
        if (str == null || str.trim().isEmpty()) {
            throw new ClientErrorException(ClientError.EMPTY_ARGUMENTS);
        }
    }
}
