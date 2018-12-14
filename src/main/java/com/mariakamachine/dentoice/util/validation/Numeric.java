package com.mariakamachine.dentoice.util.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.ReportAsSingleViolation;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({FIELD, ANNOTATION_TYPE, PARAMETER})
@Retention(RUNTIME)
@ReportAsSingleViolation
@Constraint(validatedBy = NumericValidator.class)
public @interface Numeric {

    String message() default "Value is not numeric";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
