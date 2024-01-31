package com.phoenix.amazon.AmazonBackend.dto.fieldvalidator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.lang.annotation.RetentionPolicy;
@Documented
@Constraint(validatedBy = ValidatePassword.class)
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidPassword {
    //default error message
    String message() default "Your password must have 1 uppercase," +
            "1 lowercase,1 special symbol,1 number and of at least 15 characters long";


    /**
     * @return the groups the constraint belongs to
     */
    Class<?>[] groups() default {};

    /**
     * @return the payload associated to the constraint
     */
    Class<? extends Payload>[] payload() default {};
}
