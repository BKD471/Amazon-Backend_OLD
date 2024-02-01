package com.phoenix.amazon.AmazonBackend.dto.fieldvalidator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.apache.commons.lang3.StringUtils;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.phoenix.amazon.AmazonBackend.dto.fieldvalidator.RegexMatchersHelpers.PATTERN_FOR_EMAIL;

public class ValidateNullOrEmail implements ConstraintValidator<NullOrEmail,String> {
    /**
     * @param constraintAnnotation - constraintAnnotation
     */
    @Override
    public void initialize(NullOrEmail constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    /**
     * @param s - passed string for validation
     * @param constraintValidatorContext - annotation context
     * @return - boolean
     */
    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        // Secondary email is empty, allow it since its optional
        if(StringUtils.isBlank(s)) return true;

        Pattern patternForEmail=Pattern.compile(PATTERN_FOR_EMAIL);
        Matcher matcher=patternForEmail.matcher(s);
        return matcher.matches();
    }
}
