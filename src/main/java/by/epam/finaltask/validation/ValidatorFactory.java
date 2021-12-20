package by.epam.finaltask.validation;

public class ValidatorFactory {

    private static volatile ValidatorFactory factoryInstance;
    private static volatile StringClientParameterValidator stringValidatorInstance;
    private static volatile NumberValidator numberValidatorInstance;

    private ValidatorFactory() {
    }

    public static ValidatorFactory getFactoryInstance() {
        if (factoryInstance == null) {
            synchronized (ValidatorFactory.class) {
                if (factoryInstance == null) {
                    factoryInstance = new ValidatorFactory();
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
