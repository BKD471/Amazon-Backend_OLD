package com.phoenix.amazon.AmazonBackend.dto.fieldvalidator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.BeanWrapperImpl;

import java.util.Objects;

public class ValidateFieldsValueMatch implements ConstraintValidator<FieldsValueMatch,Object> {
    private String field;
    private String fieldMatch;

    /**
     * @param constraintAnnotation
     */
    @Override
    public void initialize(FieldsValueMatch constraintAnnotation) {
        this.field= constraintAnnotation.field();
        this.fieldMatch=constraintAnnotation.fieldMatch();
    }

    /**
     * @param o
     * @param constraintValidatorContext
     * @return
     */
    @Override
    public boolean isValid(Object o, ConstraintValidatorContext constraintValidatorContext) {
        Object fieldValue=new BeanWrapperImpl(o).getPropertyValue(field);
        Object fieldMatchedValue=new BeanWrapperImpl(o).getPropertyValue(fieldMatch);
        return (!Objects.isNull(fieldValue)) ? fieldValue.equals(fieldMatchedValue):fieldMatchedValue==null;
    }
}
