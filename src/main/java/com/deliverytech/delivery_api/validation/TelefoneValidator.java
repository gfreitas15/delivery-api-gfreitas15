package com.deliverytech.delivery_api.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class TelefoneValidator implements ConstraintValidator<ValidTelefone, String> {
    
    @Override
    public void initialize(ValidTelefone constraintAnnotation) {
    }
    
    @Override
    public boolean isValid(String telefone, ConstraintValidatorContext context) {
        if (telefone == null || telefone.isBlank()) {
            return true; // Telefone pode ser opcional dependendo do contexto
        }
        
        // Remove caracteres não numéricos para validação
        String apenasNumeros = telefone.replaceAll("[^0-9]", "");
        
        // Deve ter 10 ou 11 dígitos
        return apenasNumeros.length() == 10 || apenasNumeros.length() == 11;
    }
}

