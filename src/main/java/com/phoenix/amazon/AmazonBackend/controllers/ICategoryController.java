package com.phoenix.amazon.AmazonBackend.controllers;

import com.phoenix.amazon.AmazonBackend.dto.CategoryDto;
import com.phoenix.amazon.AmazonBackend.dto.PageableResponse;
import com.phoenix.amazon.AmazonBackend.exceptions.BadApiRequestExceptions;
import com.phoenix.amazon.AmazonBackend.exceptions.CategoryExceptions;
import com.phoenix.amazon.AmazonBackend.exceptions.CategoryNotFoundExceptions;
import com.phoenix.amazon.AmazonBackend.exceptions.UserExceptions;
import com.phoenix.amazon.AmazonBackend.exceptions.UserNotFoundExceptions;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RequestMapping("/api/category")
public interface ICategoryController {
    @PostMapping(value = "/v1/create",consumes = { "multipart/form-data" })
    ResponseEntity<CategoryDto> createCategory(@RequestPart("categoryDto") @Valid CategoryDto categoryDto,
                                           @RequestPart("coverImage") MultipartFile coverImage) throws UserNotFoundExceptions, UserExceptions, BadApiRequestExceptions, IOException, CategoryNotFoundExceptions, CategoryExceptions;

    @GetMapping("/v1/getAllCategories")
    ResponseEntity<PageableResponse<CategoryDto>> getAllCategories(@RequestParam(value = "pageNumber", defaultValue = "1", required = false) final int pageNumber,
                                                      @RequestParam(value = "pageSize", defaultValue = "5", required = false) final int pageSize,
                                                      @RequestParam(value = "sortBy", defaultValue = "title", required = false) final String sortBy,
                                                      @RequestParam(value = "sortDir", defaultValue = "asc", required = false) final String sortDir) throws UserNotFoundExceptions, UserExceptions, BadApiRequestExceptions, IOException, CategoryNotFoundExceptions, CategoryExceptions;


}
