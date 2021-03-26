package com.food.register.validation;

import javax.validation.ConstraintValidatorContext;

public interface RequestValidation {

    default boolean isValid(ConstraintValidatorContext constraintValidatorContext) {
        return true;
    }
}
