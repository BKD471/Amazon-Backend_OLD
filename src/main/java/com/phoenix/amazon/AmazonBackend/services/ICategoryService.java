package com.phoenix.amazon.AmazonBackend.services;

import com.phoenix.amazon.AmazonBackend.dto.CategoryDto;
import com.phoenix.amazon.AmazonBackend.dto.PageableResponse;
import com.phoenix.amazon.AmazonBackend.exceptions.BadApiRequestExceptions;

import java.io.IOException;

public interface ICategoryService {
    CategoryDto createCategoryService(final CategoryDto categoryDto) throws BadApiRequestExceptions, IOException;
    CategoryDto updateCategoryServiceByCategoryId(final CategoryDto categoryDto,final String categoryId);
    void deleteCategoryServiceByCategoryId(final String categoryId);
    PageableResponse<CategoryDto> getAllCategoryService();
    CategoryDto getCategoryServiceByCategoryId(final String categoryId);
}
