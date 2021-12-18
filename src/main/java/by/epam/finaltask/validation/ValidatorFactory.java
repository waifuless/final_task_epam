package by.epam.finaltask.validation;

public class ValidatorFactory {

    private static volatile ValidatorFactory factoryInstance;
    private static volatile StringClientParameterValidator stringValidatorInstance;

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
}
