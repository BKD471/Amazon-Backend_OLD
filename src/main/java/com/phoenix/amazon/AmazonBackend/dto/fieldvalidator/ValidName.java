package com.phoenix.amazon.AmazonBackend.dto.fieldvalidator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Target(ElementType.FIELD)
@Constraint(validatedBy = ValidateName.class)
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidName {
    //default error message
    String message() default "Name Must not be blank/whitespace or must not have any numbers";

    //

    /**
     * @return the groups the constraint belongs to
     */
    Class<?>[] groups() default {};

    /**
     * @return the payload associated to the constraint
     */
    Class<? extends Payload>[] payload() default {};
}
