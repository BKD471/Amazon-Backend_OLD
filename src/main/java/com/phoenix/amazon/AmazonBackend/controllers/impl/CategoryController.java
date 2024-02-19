package com.phoenix.amazon.AmazonBackend.controllers.impl;

import com.phoenix.amazon.AmazonBackend.controllers.ICategoryController;
import com.phoenix.amazon.AmazonBackend.dto.CategoryDto;
import com.phoenix.amazon.AmazonBackend.dto.PageableResponse;
import com.phoenix.amazon.AmazonBackend.exceptions.BadApiRequestExceptions;
import com.phoenix.amazon.AmazonBackend.exceptions.CategoryExceptions;
import com.phoenix.amazon.AmazonBackend.exceptions.CategoryNotFoundExceptions;
import com.phoenix.amazon.AmazonBackend.exceptions.UserExceptions;
import com.phoenix.amazon.AmazonBackend.exceptions.UserNotFoundExceptions;
import com.phoenix.amazon.AmazonBackend.services.ICategoryService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
public class CategoryController implements ICategoryController {
    private final ICategoryService categoryService;

    CategoryController(ICategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @Override
    public ResponseEntity<CategoryDto> createCategory(final CategoryDto categoryDto, final MultipartFile coverImage) throws BadApiRequestExceptions, IOException, CategoryNotFoundExceptions, CategoryExceptions {
        CategoryDto categoryDto1 = categoryService.createCategoryService(categoryDto, coverImage);
        return new ResponseEntity<>(categoryDto1, HttpStatus.ACCEPTED);
    }

    /**
     * @param pageNumber
     * @param pageSize
     * @param sortBy
     * @param sortDir
     * @return
     * @throws UserNotFoundExceptions
     * @throws UserExceptions
     * @throws BadApiRequestExceptions
     * @throws IOException
     */
    @Override
    public ResponseEntity<PageableResponse<CategoryDto>> getAllCategories(int pageNumber, int pageSize, String sortBy, String sortDir) throws UserNotFoundExceptions, UserExceptions, BadApiRequestExceptions, IOException, CategoryNotFoundExceptions, CategoryExceptions {
        PageableResponse<CategoryDto> categoryDtoSet = categoryService.getAllCategoryService(pageNumber, pageSize, sortBy, sortDir);
        return new ResponseEntity<>(categoryDtoSet, HttpStatus.OK);
    }
}
