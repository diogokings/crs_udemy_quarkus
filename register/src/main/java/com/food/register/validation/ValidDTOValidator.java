package com.food.register.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ValidDTOValidator implements ConstraintValidator<ValidDTO, RequestValidation> {

    @Override
    public void initialize(ValidDTO validDTO) {

    }

    @Override
    public boolean isValid(RequestValidation requestValidation, ConstraintValidatorContext context) {
        return requestValidation.isValid(context);
    }

}
