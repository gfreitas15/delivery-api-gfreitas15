package com.deliverytech.delivery_api.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class CEPValidator implements ConstraintValidator<ValidCEP, String> {
    
    private static final String CEP_PATTERN = "^\\d{5}-?\\d{3}$";
    
    @Override
    public void initialize(ValidCEP constraintAnnotation) {
    }
    
    @Override
    public boolean isValid(String cep, ConstraintValidatorContext context) {
        if (cep == null || cep.isBlank()) {
            return true; // CEP é opcional, então null ou vazio é válido
        }
        return cep.matches(CEP_PATTERN);
    }
}

