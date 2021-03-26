package com.food.register.validation;

import java.io.Serializable;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.validation.ConstraintViolation;

import org.eclipse.microprofile.openapi.annotations.media.Schema;

public class ConstraintViolationImpl implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "Path do atributo, ex: dataInicio, pessoa.endereco.numero", required = false)
    private final String attribute;

    @Schema(description = "Mensagem descritiva do erro, possivelmente associada ao path", required = true)
    private final String message;

    private ConstraintViolationImpl(ConstraintViolation<?> violation) {
        this.message = violation.getMessage();
        this.attribute = Stream.of(violation.getPropertyPath().toString().split("\\.")).skip(2)
                .collect(Collectors.joining());
    }

    public ConstraintViolationImpl(String attribute, String message) {
        this.attribute = attribute;
        this.message = message;
    }

    public static ConstraintViolationImpl of(ConstraintViolation<?> violation) {
        return new ConstraintViolationImpl(violation);
    }

    public static ConstraintViolationImpl of(String violation) {
        return new ConstraintViolationImpl(null, violation);
    }

    public String getAttribute() {
        return attribute;
    }

    public String getMessage() {
        return message;
    }

}
