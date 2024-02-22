package com.phoenix.amazon.AmazonBackend.services.validationservice.impl;

import com.phoenix.amazon.AmazonBackend.entity.Category;
import com.phoenix.amazon.AmazonBackend.exceptions.CategoryExceptions;
import com.phoenix.amazon.AmazonBackend.exceptions.CategoryNotFoundExceptions;
import com.phoenix.amazon.AmazonBackend.exceptions.builder.ExceptionBuilder;
import com.phoenix.amazon.AmazonBackend.repository.ICategoryRepository;
import com.phoenix.amazon.AmazonBackend.services.validationservice.ICategoryValidationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.Properties;
import java.util.function.Predicate;

import static com.phoenix.amazon.AmazonBackend.helpers.AllConstantHelpers.EXCEPTION_CODES.CATEGORY_EXEC;
import static com.phoenix.amazon.AmazonBackend.helpers.AllConstantHelpers.EXCEPTION_CODES.CATEGORY_NOT_FOUND_EXEC;
import static com.phoenix.amazon.AmazonBackend.helpers.AllConstantHelpers.CATEGORY_VALIDATION;

@Service("CategoryValidationServicePrimary")
public class CategoryValidationServiceImpl implements ICategoryValidationService {
    private final ICategoryRepository categoryRepository;
    private final int CATEGORY_DESCRIPTION_THRESHOLD;
    Logger logger = LoggerFactory.getLogger(CategoryValidationServiceImpl.class);

    CategoryValidationServiceImpl(ICategoryRepository categoryRepository,
                                  @Value("${path.services.category.properties}") String PATH_TO_CATEGORY_PROPS) {
        this.categoryRepository = categoryRepository;
        Properties properties = new Properties();
        try {
            properties.load(new FileInputStream(PATH_TO_CATEGORY_PROPS));
        } catch (IOException e) {
            logger.warn("Error while loading properties file");
        }
        this.CATEGORY_DESCRIPTION_THRESHOLD = Integer.parseInt(properties.getProperty("category.description.threshold"));
    }

    private void checkDuplicateTitle(final Category category, final List<Category> categoryList, final String methodName) throws CategoryExceptions {
        Predicate<Category> findDuplicateTitle = (Category cat) -> category.getTitle().equals(cat.getTitle());
        boolean isDuplicateTitlePresent = categoryList.stream().anyMatch(findDuplicateTitle);
        if (isDuplicateTitlePresent) throw (CategoryExceptions) ExceptionBuilder.builder()
                .className(CategoryExceptions.class)
                .description(String.format("There exists a category with title %s", category.getTitle()))
                .methodName(methodName)
                .build(CATEGORY_EXEC);
    }

    private void checkCategoryDescriptionLimit(final Category category, final String methodName) throws CategoryExceptions {
        if (category.getDescription().length() >= CATEGORY_DESCRIPTION_THRESHOLD)
            throw (CategoryExceptions) ExceptionBuilder.builder()
                    .className(CategoryExceptions.class)
                    .description(String.format("The category description must be within %s ", CATEGORY_DESCRIPTION_THRESHOLD))
                    .methodName(methodName)
                    .build(CATEGORY_EXEC);
    }

    /**
     * @param categoryOptional   - category object
     * @param methodName         - origin of request
     * @param categoryValidation - category validation field
     * @throws CategoryNotFoundExceptions,CategoryExceptions - list of exceptions being thrown
     ***/
    @Override
    public void validateCategory(Optional<Category> categoryOptional, String methodName,
                                 CATEGORY_VALIDATION categoryValidation) throws CategoryNotFoundExceptions, CategoryExceptions {

        List<Category> categoryList = categoryRepository.findAll();
        Category fetchedCategory = null;
        switch (categoryValidation) {
            case CREATE_CATEGORY -> {
                //check for duplicate title
                fetchedCategory = categoryOptional.get();
                checkDuplicateTitle(fetchedCategory, categoryList, methodName);

                //check for description greater than 10000
                checkCategoryDescriptionLimit(fetchedCategory, methodName);
            }
            case UPDATE_TITLE -> {
                fetchedCategory = categoryOptional.get();
                //check for duplicate title
                checkDuplicateTitle(fetchedCategory, categoryList, methodName);
            }
            case UPDATE_DESCRIPTION -> {
                fetchedCategory = categoryOptional.get();
                //check for description greater than 10000
                checkCategoryDescriptionLimit(fetchedCategory, methodName);
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
