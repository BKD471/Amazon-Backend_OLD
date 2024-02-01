package com.phoenix.amazon.AmazonBackend.dto.fieldvalidator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.apache.commons.lang3.StringUtils;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.phoenix.amazon.AmazonBackend.dto.fieldvalidator.RegexMatchersHelpers.PATTERN_FOR_USERNAME;

public class ValidateUserName implements ConstraintValidator<ValidUserName,String> {
    /**
     * @param constraintAnnotation 
     */
    @Override
    public void initialize(ValidUserName constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    /**
     * @param s - passed string for validation
     * @param constraintValidatorContext - annotation context
     * @return - boolean
     */
    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        if(StringUtils.isBlank(s)) return false;

        Pattern pattern=Pattern.compile(PATTERN_FOR_USERNAME);
        Matcher matcher=pattern.matcher(s);
        return matcher.matches();
    }
}
