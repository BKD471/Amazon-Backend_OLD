package com.phoenix.amazon.AmazonBackend.services.validationservice;

import com.phoenix.amazon.AmazonBackend.entity.Category;
import com.phoenix.amazon.AmazonBackend.exceptions.CategoryExceptions;
import com.phoenix.amazon.AmazonBackend.exceptions.CategoryNotFoundExceptions;

import java.util.Optional;
import static com.phoenix.amazon.AmazonBackend.helpers.AllConstantHelpers.CATEGORY_VALIDATION;

public interface ICategoryValidationService {
    void validateCategory(final Optional<Category> categoryOptional, final String methodName, final CATEGORY_VALIDATION categoryValidation) throws CategoryNotFoundExceptions, CategoryExceptions;
}
