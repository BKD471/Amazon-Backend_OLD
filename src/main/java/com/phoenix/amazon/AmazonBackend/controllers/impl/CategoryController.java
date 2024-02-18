package com.phoenix.amazon.AmazonBackend.controllers.impl;

import com.phoenix.amazon.AmazonBackend.controllers.ICategoryController;
import com.phoenix.amazon.AmazonBackend.dto.CategoryDto;
import com.phoenix.amazon.AmazonBackend.dto.PageableResponse;
import com.phoenix.amazon.AmazonBackend.exceptions.BadApiRequestExceptions;
import com.phoenix.amazon.AmazonBackend.exceptions.UserExceptions;
import com.phoenix.amazon.AmazonBackend.exceptions.UserNotFoundExceptions;
import com.phoenix.amazon.AmazonBackend.services.ICategoryService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
public class CategoryController implements ICategoryController {
    private final ICategoryService categoryService;

    CategoryController(ICategoryService categoryService){
        this.categoryService=categoryService;
    }

    @Override
    public ResponseEntity<CategoryDto> createCategory(final CategoryDto categoryDto,final MultipartFile coverImage) throws BadApiRequestExceptions, IOException {
        CategoryDto categoryDto1=categoryService.createCategoryService(categoryDto,coverImage);
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
    public ResponseEntity<PageableResponse<CategoryDto>> getAllCategories(int pageNumber, int pageSize, String sortBy, String sortDir) throws UserNotFoundExceptions, UserExceptions, BadApiRequestExceptions, IOException {
        PageableResponse<CategoryDto> categoryDtoSet=categoryService.getAllCategoryService(pageNumber,pageSize,sortBy,sortDir);
        return new ResponseEntity<>(categoryDtoSet,HttpStatus.OK);
    }
}
