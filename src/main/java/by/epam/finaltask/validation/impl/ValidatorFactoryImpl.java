package by.epam.finaltask.validation.impl;

import by.epam.finaltask.validation.NumberValidator;
import by.epam.finaltask.validation.StringClientParameterValidator;
import by.epam.finaltask.validation.ValidatorFactory;

public class ValidatorFactoryImpl implements ValidatorFactory{

    private static volatile ValidatorFactory factoryInstance;
    private static volatile StringClientParameterValidator stringValidatorInstance;
    private static volatile NumberValidator numberValidatorInstance;

    private ValidatorFactoryImpl() {
    }

    public static ValidatorFactory getFactoryInstance() {
        if (factoryInstance == null) {
            synchronized (ValidatorFactoryImpl.class) {
                if (factoryInstance == null) {
                    factoryInstance = new ValidatorFactoryImpl();
                }
            }
        }
        return factoryInstance;
    }

    public StringClientParameterValidator stringParameterValidator() {
        if (stringValidatorInstance == null) {
            synchronized (CommonStringClientParameterValidator.class) {
                if (stringValidatorInstance == null) {
                    stringValidatorInstance = new CommonStringClientParameterValidator();
                }
            }
        }
        return stringValidatorInstance;
    }

    public NumberValidator idValidator() {
        if (numberValidatorInstance == null) {
            synchronized (CommonNumberValidator.class) {
                if (numberValidatorInstance == null) {
                    numberValidatorInstance = new CommonNumberValidator();
                }
            }
        }
        return numberValidatorInstance;
    }
}
