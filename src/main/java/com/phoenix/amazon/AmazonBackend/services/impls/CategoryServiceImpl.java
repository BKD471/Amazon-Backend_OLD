package com.phoenix.amazon.AmazonBackend.services.impls;

import com.phoenix.amazon.AmazonBackend.dto.ApiResponse;
import com.phoenix.amazon.AmazonBackend.dto.CategoryDto;
import com.phoenix.amazon.AmazonBackend.dto.PageableResponse;
import com.phoenix.amazon.AmazonBackend.entity.Category;
import com.phoenix.amazon.AmazonBackend.entity.Users;
import com.phoenix.amazon.AmazonBackend.exceptions.BadApiRequestExceptions;
import com.phoenix.amazon.AmazonBackend.repository.ICategoryRepository;
import com.phoenix.amazon.AmazonBackend.repository.IUserRepository;
import com.phoenix.amazon.AmazonBackend.services.AbstractService;
import com.phoenix.amazon.AmazonBackend.services.ICategoryService;
import com.phoenix.amazon.AmazonBackend.services.IImageService;
import com.phoenix.amazon.AmazonBackend.services.validationservice.ICategoryValidationService;
import com.phoenix.amazon.AmazonBackend.services.validationservice.IUserValidationService;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

import static com.phoenix.amazon.AmazonBackend.helpers.AllConstantHelpers.DestinationDtoType.CATEGORY_DTO;
import static com.phoenix.amazon.AmazonBackend.helpers.AllConstantHelpers.DestinationDtoType.USER_DTO;
import static com.phoenix.amazon.AmazonBackend.helpers.AllConstantHelpers.USER_VALIDATION.GET_ALL_USERS;
import static com.phoenix.amazon.AmazonBackend.helpers.MappingHelpers.CategoryDtoToCategory;
import static com.phoenix.amazon.AmazonBackend.helpers.MappingHelpers.categoryToCategoryDto;
import static com.phoenix.amazon.AmazonBackend.helpers.PagingHelpers.getPageableResponse;

@Service("CategoryServicePrimary")
public class CategoryServiceImpl extends AbstractService implements ICategoryService {
    private final ICategoryRepository categoryRepository;
    private final ICategoryValidationService categoryValidationService;
    private final IImageService imageService;

    CategoryServiceImpl(final IUserRepository userRepository,
                        final IUserValidationService userValidationService,
                        final ICategoryValidationService categoryValidationService,
                        final ICategoryRepository categoryRepository,
                        final IImageService imageService) {
        super(userRepository, userValidationService);
        this.categoryValidationService = categoryValidationService;
        this.categoryRepository = categoryRepository;
        this.imageService = imageService;
    }

    private Pageable getPageableObject(final int pageNumber, final int pageSize, final Sort sort) {
        return PageRequest.of(pageNumber - 1, pageSize, sort);
    }


    /**
     * @param categoryDto
     * @return
     */
    @Override
    public CategoryDto createCategoryService(final CategoryDto categoryDto, final MultipartFile coverImage) throws BadApiRequestExceptions, IOException {
        // check for duplicate title
        Category category = CategoryDtoToCategory(categoryDto);
        final String categoryId = UUID.randomUUID().toString();

        final String coverImageName = imageService.uploadCoverImageByCategoryId(coverImage);
        Category processedCategory = new Category.builder()
                .categoryId(categoryId)
                .title(categoryDto.title())
                .description(category.getDescription())
                .coverImage(coverImageName)
                .build();

        Category savedCategory = categoryRepository.save(processedCategory);
        return categoryToCategoryDto(savedCategory);
    }

    /**
     * @param categoryDto
     * @param categoryId
     * @return
     */
    @Override
    public CategoryDto updateCategoryServiceByCategoryId(final CategoryDto categoryDto,final MultipartFile coverImage,final String categoryId) {
        return null;
    }

    /**
     * @param categoryId
     */
    @Override
    public ApiResponse deleteCategoryServiceByCategoryId(final String categoryId) {
        //check for existing categoryId
        categoryRepository.deleteById(categoryId);
        return new ApiResponse.builder()
                .message(String.format("Deleted category with categoryId: %s",categoryId))
                .success(true)
                .status(HttpStatus.ACCEPTED).build();
    }

    /**
     * @return
     */
    @Override
    public PageableResponse<CategoryDto> getAllCategoryService(final int pageNumber, final int pageSize, final String sortBy, final String sortDir) {
        final String methodName = "getAllCategoryService() in CategoryServiceImpl";
        final Sort sort = sortDir.equals("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        final Pageable pageableObject = getPageableObject(pageNumber, pageSize, sort);
        Page<Category> userPage = categoryRepository.findAll(pageableObject);

        return getPageableResponse(userPage, CATEGORY_DTO);
    }

    /**
     * @param categoryId
     * @return
     */
    @Override
    public CategoryDto getCategoryServiceByCategoryId(final String categoryId) {
        return null;
    }
}
