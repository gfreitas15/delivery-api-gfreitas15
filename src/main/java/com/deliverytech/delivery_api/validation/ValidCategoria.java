package com.deliverytech.delivery_api.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Constraint(validatedBy = com.deliverytech.delivery_api.validation.CategoriaValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidCategoria {
    String message() default "Categoria inválida. Categorias permitidas: Pizza, Hambúrguer, Japonesa, Chinesa, Brasileira, Italiana, Mexicana, Árabe, Doces, Bebidas";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}

