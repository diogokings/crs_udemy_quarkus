package com.food.register.dto;

import javax.validation.ConstraintValidatorContext;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.food.register.Restaurant;
import com.food.register.validation.RequestValidation;
import com.food.register.validation.ValidDTO;

@ValidDTO
public class RequestCreateRestaurantDTO implements RequestValidation {

    @NotNull
    @NotEmpty
    public String owner;

    @NotNull
    @Size(min = 3, max = 50)
    public String name;

    @NotNull
    @Pattern(regexp = "[0-9]{2}\\.[0-9]{3}\\.[0-9]{3}\\/[0-9]{4}\\-[0-9]{2}")
    public String document;

    public GeoLocationDTO geoLocation;

    @Override
    public boolean isValid(ConstraintValidatorContext constraintValidatorContext) {
        constraintValidatorContext.disableDefaultConstraintViolation();
        if (Restaurant.find("document", document).count() > 0) {
            constraintValidatorContext.buildConstraintViolationWithTemplate("CNPJ já cadastrado")
                    .addPropertyNode("document")
                    .addConstraintViolation();
            return false;
        }
        return true;
    }
}
