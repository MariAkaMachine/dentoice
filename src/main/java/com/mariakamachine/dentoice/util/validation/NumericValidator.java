package com.mariakamachine.dentoice.util.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import static org.apache.commons.lang3.math.NumberUtils.isDigits;

public class NumericValidator implements ConstraintValidator<Numeric, String> {

    @Override
    public void initialize(Numeric constraintAnnotation) {
        //no-op
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return isDigits(value);
    }

}
