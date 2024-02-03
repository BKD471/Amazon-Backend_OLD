package com.phoenix.amazon.AmazonBackend.dto.fieldvalidator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.apache.commons.lang3.StringUtils;
import java.util.Set;


public class ValidateGender implements ConstraintValidator<ValidGender, String> {
    /**
     * @param constraintAnnotation - constraint annotation
     */
    @Override
    public void initialize(ValidGender constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    /**
     * @param gender                     - passed gender for validation
     * @param constraintValidatorContext - constraint context
     * @return - boolean
     */
    @Override
    public boolean isValid(final String gender, ConstraintValidatorContext constraintValidatorContext) {
        if (StringUtils.isBlank(gender)) return false;

        final Set<String> validGenderSet = Set.of("MALE", "FEMALE", "NON_BINARY", "LGBTQ");
        return validGenderSet.contains(gender);
    }
}
