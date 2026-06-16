package com.gabriel.fluxbank.modules.user.validation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Documented
@Constraint(validatedBy = PasswordValidator.class)
@Target({
        ElementType.FIELD,
        ElementType.PARAMETER,
        ElementType.RECORD_COMPONENT,
        ElementType.ANNOTATION_TYPE
})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidPassword {

    String message() default "Password must contain at least one uppercase letter, one number and one special character, without spaces";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}