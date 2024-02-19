package com.phoenix.amazon.AmazonBackend.services.validationservice.impl;

import com.phoenix.amazon.AmazonBackend.entity.Category;
import com.phoenix.amazon.AmazonBackend.exceptions.CategoryExceptions;
import com.phoenix.amazon.AmazonBackend.exceptions.CategoryNotFoundExceptions;
import com.phoenix.amazon.AmazonBackend.exceptions.builder.ExceptionBuilder;
import com.phoenix.amazon.AmazonBackend.helpers.AllConstantHelpers;
import com.phoenix.amazon.AmazonBackend.repository.ICategoryRepository;
import com.phoenix.amazon.AmazonBackend.services.validationservice.ICategoryValidationService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

import static com.phoenix.amazon.AmazonBackend.helpers.AllConstantHelpers.EXCEPTION_CODES.CATEGORY_EXEC;
import static com.phoenix.amazon.AmazonBackend.helpers.AllConstantHelpers.EXCEPTION_CODES.CATEGORY_NOT_FOUND_EXEC;
import static com.phoenix.amazon.AmazonBackend.helpers.AllConstantHelpers.CATEGORY_VALIDATION;

@Service("CategoryValidationServicePrimary")
public class CategoryValidationServiceImpl implements ICategoryValidationService {
    private final ICategoryRepository categoryRepository;
    private final int CATEGORY_DESCRIPTION_THRESHOLD = 10000;

    CategoryValidationServiceImpl(ICategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    /**
     * @param categoryOptional
     * @param methodName
     * @param categoryValidation
     */
    @Override
    public void validateCategory(Optional<Category> categoryOptional, String methodName,
                                 CATEGORY_VALIDATION categoryValidation) throws CategoryNotFoundExceptions, CategoryExceptions {

        List<Category> categoryList = categoryRepository.findAll();
        switch (categoryValidation) {
            case CREATE_CATEGORY -> {
                //check for duplicate title
                Category fetchedCategory = categoryOptional.get();
                Predicate<Category> findDuplicateTitle = (Category category) -> fetchedCategory.getTitle().equals(category.getTitle());
                boolean isDuplicateTitlePresent = categoryList.stream().anyMatch(findDuplicateTitle);
                if (isDuplicateTitlePresent) throw (CategoryExceptions) ExceptionBuilder.builder()
                        .className(CategoryExceptions.class)
                        .description(String.format("There exists a category with title %s", fetchedCategory.getTitle()))
                        .methodName(methodName)
                        .build(CATEGORY_EXEC);

                //check for description greater than 10000
                if (fetchedCategory.getDescription().length() >= CATEGORY_DESCRIPTION_THRESHOLD)
                    throw (CategoryExceptions) ExceptionBuilder.builder()
                            .className(CategoryExceptions.class)
                            .description(String.format("The category description must be within %s ",CATEGORY_DESCRIPTION_THRESHOLD))
                            .methodName(methodName)
                            .build(CATEGORY_EXEC);
            }
            case NOT_FOUND_CATEGORY -> {
                if (categoryOptional.isEmpty())
                    throw (CategoryNotFoundExceptions) ExceptionBuilder.builder().className(CategoryNotFoundExceptions.class)
                            .description("NO Categories found exception")
                            .methodName(methodName).build(CATEGORY_NOT_FOUND_EXEC);
            }
        }

    }
}
