package com.prico.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = SearchRequestValidator.class)
public @interface AtLeastOneNotBlank {
    String message() default "At least one property must have data";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}

