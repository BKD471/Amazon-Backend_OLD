package com.phoenix.amazon.AmazonBackend.dto.fieldvalidator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Constraint(validatedBy = ValidateUserName.class)
public @interface ValidUserName {
    //default error message
    String message() default "UserName Can only contain letters & numbers[0-9] & underscore of size from 5 to 15";

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
