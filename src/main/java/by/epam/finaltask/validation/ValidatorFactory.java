package by.epam.finaltask.validation;

import by.epam.finaltask.validation.impl.CommonNumberValidator;
import by.epam.finaltask.validation.impl.CommonStringClientParameterValidator;
import by.epam.finaltask.validation.impl.ValidatorFactoryImpl;

public interface ValidatorFactory {

    static ValidatorFactory getFactoryInstance() {
        return ValidatorFactoryImpl.getFactoryInstance();
    }

    StringClientParameterValidator stringParameterValidator();

    NumberValidator idValidator();
}
