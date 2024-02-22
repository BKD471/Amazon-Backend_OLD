package com.phoenix.amazon.AmazonBackend.services.validationservice;

import com.phoenix.amazon.AmazonBackend.entity.Category;
import com.phoenix.amazon.AmazonBackend.exceptions.CategoryExceptions;
import com.phoenix.amazon.AmazonBackend.exceptions.CategoryNotFoundExceptions;
import java.util.Optional;

import static com.phoenix.amazon.AmazonBackend.helpers.AllConstantHelpers.CATEGORY_VALIDATION;

public interface ICategoryValidationService {
    /**
     * @param categoryOptional   - category object
     * @param methodName         - origin of request
     * @param categoryValidation - category validation field
     * @throws CategoryNotFoundExceptions,CategoryExceptions - list of exceptions being thrown
     ***/
    void validateCategory(final Optional<Category> categoryOptional, final String methodName, final CATEGORY_VALIDATION categoryValidation) throws CategoryNotFoundExceptions, CategoryExceptions;
}
