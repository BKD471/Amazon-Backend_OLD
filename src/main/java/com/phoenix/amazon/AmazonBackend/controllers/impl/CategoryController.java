package com.phoenix.amazon.AmazonBackend.controllers.impl;

import com.phoenix.amazon.AmazonBackend.controllers.ICategoryController;
import com.phoenix.amazon.AmazonBackend.dto.requestDtos.CategoryDto;
import com.phoenix.amazon.AmazonBackend.dto.responseDtos.ApiResponse;
import com.phoenix.amazon.AmazonBackend.dto.responseDtos.PageableResponse;
import com.phoenix.amazon.AmazonBackend.exceptions.BadApiRequestExceptions;
import com.phoenix.amazon.AmazonBackend.exceptions.CategoryExceptions;
import com.phoenix.amazon.AmazonBackend.exceptions.CategoryNotFoundExceptions;
import com.phoenix.amazon.AmazonBackend.services.ICategoryService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController("CategoryControllerPrimary")
public class CategoryController implements ICategoryController {
    private final ICategoryService categoryService;

    CategoryController(ICategoryService categoryService) {
        this.categoryService = categoryService;
    }

    /**
     * @param categoryDto - category request object
     * @param coverImage  - coverImage of category
     * @return CategoryDto
     * @throws BadApiRequestExceptions,IOException,CategoryNotFoundExceptions,CategoryExceptions - list of exceptions being thrown
     **/
    @Override
    public ResponseEntity<CategoryDto> createCategory(final CategoryDto categoryDto, final MultipartFile coverImage) throws BadApiRequestExceptions, IOException, CategoryNotFoundExceptions, CategoryExceptions {
        CategoryDto categoryDto1 = categoryService.createCategoryService(categoryDto, coverImage);
        return new ResponseEntity<>(categoryDto1, HttpStatus.OK);
    }

    /**
     * @param categoryId - categoryId of category
     * @return ApiResponse
     * @throws CategoryNotFoundExceptions,CategoryExceptions - list of exceptions being thrown
     **/
    @Override
    public ResponseEntity<ApiResponse> deleteCategory(String categoryId) throws CategoryNotFoundExceptions, CategoryExceptions {
        ApiResponse deletedCategoryResponse = categoryService.deleteCategoryServiceByCategoryId(categoryId);
        return new ResponseEntity<>(deletedCategoryResponse, HttpStatus.ACCEPTED);
    }

    /**
     * @param categoryDto - category request object
     * @param coverImage  - cover image of category
     * @param categoryId  - id of category
     * @return CategoryDto
     * @throws BadApiRequestExceptions,IOException,CategoryNotFoundExceptions,CategoryExceptions - list of exceptions being thrown
     **/
    @Override
    public ResponseEntity<CategoryDto> updateCategory(CategoryDto categoryDto, MultipartFile coverImage, String categoryId) throws BadApiRequestExceptions, IOException, CategoryNotFoundExceptions, CategoryExceptions {
        CategoryDto updatedCategory = categoryService.updateCategoryServiceByCategoryId(categoryDto, coverImage, categoryId);
        return new ResponseEntity<>(updatedCategory, HttpStatus.ACCEPTED);
    }

    /**
     * @param pageNumber - index of current page
     * @param pageSize   - size of page
     * @param sortBy     - sort column
     * @param sortDir    - sort direction
     * @return PageableResponse<CategoryDto>
     * @throws CategoryNotFoundExceptions,CategoryExceptions - list of exceptions being thrown
     **/
    @Override
    public ResponseEntity<PageableResponse<CategoryDto>> getAllCategories(int pageNumber, int pageSize, String sortBy, String sortDir) throws CategoryNotFoundExceptions, CategoryExceptions {
        PageableResponse<CategoryDto> categoryDtoSet = categoryService.getAllCategoryService(pageNumber, pageSize, sortBy, sortDir);
        return new ResponseEntity<>(categoryDtoSet, HttpStatus.OK);
    }

    /**
     * @param categoryId - categoryId of category
     * @return CategoryDto
     * @throws CategoryNotFoundExceptions,CategoryExceptions - list of exceptions being thrown
     **/
    @Override
    public ResponseEntity<CategoryDto> getCategory(String categoryId) throws CategoryNotFoundExceptions, CategoryExceptions {
        CategoryDto categoryDto = categoryService.getCategoryServiceByCategoryId(categoryId);
        return new ResponseEntity<>(categoryDto, HttpStatus.OK);
    }
}
