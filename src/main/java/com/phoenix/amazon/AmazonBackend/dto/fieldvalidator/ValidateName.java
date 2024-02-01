package com.phoenix.amazon.AmazonBackend.dto.fieldvalidator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.apache.commons.lang3.StringUtils;

public class ValidateName implements ConstraintValidator<ValidName, String> {
    /**
     * @param constraintAnnotation - constraint annotation
     */
    @Override
    public void initialize(ValidName constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    /**
     * @param s                          - passed string for validation
     * @param constraintValidatorContext - annotation context
     * @return - boolean
     */
    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        // null name & whitespace is prohibited
        if (StringUtils.isBlank(s)) return false;

        // No number in name
        // ascii value of
        // A-Z (65-90)
        // a-z (97-122
        // whitespace - 32
        for (int i = 0; i < s.length(); i++) {
            int character = s.charAt(i);
            if (character == 32) continue;
            if (character < 65 || character > 122) return false;
            if (character > 90 && character < 97) return false;
        }
        return true;
    }
}
