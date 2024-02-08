package com.phoenix.amazon.AmazonBackend.dto.fieldvalidator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.apache.commons.lang3.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.phoenix.amazon.AmazonBackend.dto.fieldvalidator.RegexMatchersHelpers.PATTERN_FOR_PASSWORD;

public class ValidatePassword implements ConstraintValidator<ValidPassword,String> {

    /**
     * @param constraintAnnotation - constraint annotation
     */
    @Override
    public void initialize(ValidPassword constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    /**
     * @param s - passed string for validation
     * @param constraintValidatorContext - constraint validator context
     * @return - boolean
     */
    /**
     * UserDto is used by both createUser & UpdateUser
     * we want password to be mandatory for createUser
     * but not for updateUser
     *
     * since UserDto is common so will get exception during updateUser
     * so pass true for null password, put null check during user Creation
     *
     * password reset should be handled by separate service
     * **/
    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        if(StringUtils.isBlank(s)) return true;

        Pattern pattern=Pattern.compile(PATTERN_FOR_PASSWORD);
        Matcher matcher=pattern.matcher(s);
        return matcher.matches();
    }
}
