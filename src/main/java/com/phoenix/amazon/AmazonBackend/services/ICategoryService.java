package com.phoenix.amazon.AmazonBackend.services;

import com.phoenix.amazon.AmazonBackend.dto.requestDtos.CategoryDto;
import com.phoenix.amazon.AmazonBackend.dto.responseDtos.ApiResponse;
import com.phoenix.amazon.AmazonBackend.dto.responseDtos.PageableResponse;
import com.phoenix.amazon.AmazonBackend.exceptions.BadApiRequestExceptions;
import com.phoenix.amazon.AmazonBackend.exceptions.CategoryExceptions;
import com.phoenix.amazon.AmazonBackend.exceptions.CategoryNotFoundExceptions;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ICategoryService {
    /**
     * @param categoryDto - category object
     * @param coverImage  - category cover image
     * @return CategoryDto
     * @throws BadApiRequestExceptions,IOException,CategoryNotFoundExceptions,CategoryExceptions - list of exceptions being thrown
     ***/
    CategoryDto createCategoryService(final CategoryDto categoryDto, final MultipartFile coverImage) throws BadApiRequestExceptions, IOException, CategoryNotFoundExceptions, CategoryExceptions;

    /**
     * @param categoryDto - category object
     * @param coverImage  - category cover image
     * @param categoryId  - categoryId of category
     * @return CategoryDto
     * @throws BadApiRequestExceptions,IOException,CategoryNotFoundExceptions,CategoryExceptions - list of exceptions being thrown
     ***/
    CategoryDto updateCategoryServiceByCategoryId(final CategoryDto categoryDto, final MultipartFile coverImage, final String categoryId) throws BadApiRequestExceptions, CategoryNotFoundExceptions, CategoryExceptions, IOException;

    /**
     * @param categoryId - categoryId of category
     * @return ApiResponse
     * @throws CategoryNotFoundExceptions,CategoryExceptions - list of exceptions being thrown
     ***/
    ApiResponse deleteCategoryServiceByCategoryId(final String categoryId) throws CategoryNotFoundExceptions, CategoryExceptions;

    /**
     * @param pageNumber - index of page
     * @param pageSize   - size of page
     * @param sortBy     - sort by column
     * @param sortDir    - sorting direction
     * @return PageableResponse<CategoryDto>
     * @throws CategoryNotFoundExceptions,CategoryExceptions - list of exceptions being thrown
     ***/
    PageableResponse<CategoryDto> getAllCategoryService(final int pageNumber, final int pageSize, final String sortBy, final String sortDir) throws CategoryNotFoundExceptions, CategoryExceptions;

    /**
     * @param categoryId - categoryId of category
     * @return CategoryDto
     * @throws CategoryNotFoundExceptions,CategoryExceptions - list of exceptions being thrown
     ***/
    CategoryDto getCategoryServiceByCategoryId(final String categoryId) throws CategoryNotFoundExceptions, CategoryExceptions;
}
