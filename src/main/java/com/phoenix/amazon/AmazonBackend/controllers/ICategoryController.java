package com.phoenix.amazon.AmazonBackend.controllers;

import com.phoenix.amazon.AmazonBackend.dto.requestDtos.CategoryDto;
import com.phoenix.amazon.AmazonBackend.dto.responseDtos.ApiResponse;
import com.phoenix.amazon.AmazonBackend.dto.responseDtos.PageableResponse;
import com.phoenix.amazon.AmazonBackend.exceptions.BadApiRequestExceptions;
import com.phoenix.amazon.AmazonBackend.exceptions.CategoryExceptions;
import com.phoenix.amazon.AmazonBackend.exceptions.CategoryNotFoundExceptions;
import com.phoenix.amazon.AmazonBackend.exceptions.UserExceptions;
import com.phoenix.amazon.AmazonBackend.exceptions.UserNotFoundExceptions;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RequestMapping("/api/category")
public interface ICategoryController {
    /**
     * @param categoryDto - category request object
     * @param coverImage  - coverImage of category
     * @return CategoryDto
     * @throws BadApiRequestExceptions,IOException,CategoryNotFoundExceptions,CategoryExceptions - list of exceptions being thrown
     **/
    @PostMapping(value = "/v1/create", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    ResponseEntity<CategoryDto> createCategory(@RequestPart("categoryDto") @Valid final CategoryDto categoryDto,
                                               @RequestPart("coverImage") MultipartFile coverImage) throws BadApiRequestExceptions, IOException, CategoryNotFoundExceptions, CategoryExceptions;

    /**
     * @param categoryId - categoryId of category
     * @return ApiResponse
     * @throws CategoryNotFoundExceptions,CategoryExceptions - list of exceptions being thrown
     **/
    @DeleteMapping("/v1/deleteCategory")
    ResponseEntity<ApiResponse> deleteCategory(@RequestParam(value = "categoryId") final String categoryId) throws CategoryNotFoundExceptions, CategoryExceptions;

    /**
     * @param categoryDto - category request object
     * @param coverImage  - cover image of category
     * @param categoryId  - id of category
     * @return CategoryDto
     * @throws BadApiRequestExceptions,IOException,CategoryNotFoundExceptions,CategoryExceptions - list of exceptions being thrown
     **/
    @PutMapping(value = "/v1/update", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    ResponseEntity<CategoryDto> updateCategory(@RequestPart(value = "categoryDto", required = false) @Valid final CategoryDto categoryDto,
                                               @RequestPart(value = "coverImage", required = false) MultipartFile coverImage,
                                               @RequestParam("categoryId") final String categoryId) throws BadApiRequestExceptions, IOException, CategoryNotFoundExceptions, CategoryExceptions;

    /**
     * @param pageNumber - index of current page
     * @param pageSize   - size of page
     * @param sortBy     - sort column
     * @param sortDir    - sort direction
     * @return PageableResponse<CategoryDto>
     * @throws CategoryNotFoundExceptions,CategoryExceptions - list of exceptions being thrown
     **/
    @GetMapping("/v1/getAllCategories")
    ResponseEntity<PageableResponse<CategoryDto>> getAllCategories(@RequestParam(value = "pageNumber", defaultValue = "1", required = false) final int pageNumber,
                                                                   @RequestParam(value = "pageSize", defaultValue = "5", required = false) final int pageSize,
                                                                   @RequestParam(value = "sortBy", defaultValue = "title", required = false) final String sortBy,
                                                                   @RequestParam(value = "sortDir", defaultValue = "asc", required = false) final String sortDir) throws CategoryNotFoundExceptions, CategoryExceptions;

    /**
     * @param categoryId - categoryId of category
     * @return CategoryDto
     * @throws CategoryNotFoundExceptions,CategoryExceptions - list of exceptions being thrown
     **/
    @GetMapping("/v1/getCategory")
    ResponseEntity<CategoryDto> getCategory(@RequestParam(value = "categoryId") final String categoryId) throws CategoryNotFoundExceptions, CategoryExceptions;
}
