package com.phoenix.amazon.AmazonBackend.services;

import com.phoenix.amazon.AmazonBackend.dto.ApiResponse;
import com.phoenix.amazon.AmazonBackend.dto.CategoryDto;
import com.phoenix.amazon.AmazonBackend.dto.PageableResponse;
import com.phoenix.amazon.AmazonBackend.exceptions.BadApiRequestExceptions;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ICategoryService {
    CategoryDto createCategoryService(final CategoryDto categoryDto,final  MultipartFile coverImage) throws BadApiRequestExceptions, IOException;
    CategoryDto updateCategoryServiceByCategoryId(final CategoryDto categoryDto,final  MultipartFile coverImage,final String categoryId);
    ApiResponse deleteCategoryServiceByCategoryId(final String categoryId);
    PageableResponse<CategoryDto> getAllCategoryService(final int pageNumber, final int pageSize, final String sortBy, final String sortDir);
    CategoryDto getCategoryServiceByCategoryId(final String categoryId);
}
