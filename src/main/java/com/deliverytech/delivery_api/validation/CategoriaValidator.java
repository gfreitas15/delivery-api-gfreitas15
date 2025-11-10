package com.deliverytech.delivery_api.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.Arrays;
import java.util.List;

public class CategoriaValidator implements ConstraintValidator<ValidCategoria, String> {
    
    private static final List<String> CATEGORIAS_VALIDAS = Arrays.asList(
        "Pizza", "Hambúrguer", "Japonesa", "Chinesa", "Brasileira", 
        "Italiana", "Mexicana", "Árabe", "Doces", "Bebidas"
    );
    
    @Override
    public void initialize(ValidCategoria constraintAnnotation) {
    }
    
    @Override
    public boolean isValid(String categoria, ConstraintValidatorContext context) {
        if (categoria == null || categoria.isBlank()) {
            return true; // Validação de obrigatoriedade é feita por @NotBlank
        }
        return CATEGORIAS_VALIDAS.contains(categoria);
    }
}

