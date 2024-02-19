package com.phoenix.amazon.AmazonBackend.services.impls;

import com.phoenix.amazon.AmazonBackend.dto.ApiResponse;
import com.phoenix.amazon.AmazonBackend.dto.CategoryDto;
import com.phoenix.amazon.AmazonBackend.dto.PageableResponse;
import com.phoenix.amazon.AmazonBackend.entity.Category;
import com.phoenix.amazon.AmazonBackend.exceptions.BadApiRequestExceptions;
import com.phoenix.amazon.AmazonBackend.exceptions.CategoryExceptions;
import com.phoenix.amazon.AmazonBackend.exceptions.CategoryNotFoundExceptions;
import com.phoenix.amazon.AmazonBackend.helpers.AllConstantHelpers;
import com.phoenix.amazon.AmazonBackend.helpers.MappingHelpers;
import com.phoenix.amazon.AmazonBackend.repository.ICategoryRepository;
import com.phoenix.amazon.AmazonBackend.repository.IUserRepository;
import com.phoenix.amazon.AmazonBackend.services.AbstractService;
import com.phoenix.amazon.AmazonBackend.services.ICategoryService;
import com.phoenix.amazon.AmazonBackend.services.IImageService;
import com.phoenix.amazon.AmazonBackend.services.validationservice.ICategoryValidationService;
import com.phoenix.amazon.AmazonBackend.services.validationservice.IUserValidationService;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import static com.phoenix.amazon.AmazonBackend.helpers.AllConstantHelpers.CATEGORY_VALIDATION.CREATE_CATEGORY;
import static com.phoenix.amazon.AmazonBackend.helpers.AllConstantHelpers.CATEGORY_VALIDATION.NOT_FOUND_CATEGORY;
import static com.phoenix.amazon.AmazonBackend.helpers.AllConstantHelpers.CATEGORY_VALIDATION.UPDATE_DESCRIPTION;
import static com.phoenix.amazon.AmazonBackend.helpers.AllConstantHelpers.CATEGORY_VALIDATION.UPDATE_TITLE;
import static com.phoenix.amazon.AmazonBackend.helpers.AllConstantHelpers.DestinationDtoType.CATEGORY_DTO;
import static com.phoenix.amazon.AmazonBackend.helpers.MappingHelpers.CategoryDtoToCategory;
import static com.phoenix.amazon.AmazonBackend.helpers.MappingHelpers.categoryToCategoryDto;
import static com.phoenix.amazon.AmazonBackend.helpers.PagingHelpers.getPageableResponse;
import static com.phoenix.amazon.AmazonBackend.helpers.AllConstantHelpers.CATEGORY_FIELDS.TITLE;
import static com.phoenix.amazon.AmazonBackend.helpers.AllConstantHelpers.CATEGORY_FIELDS.DESCRIPTION;
import static com.phoenix.amazon.AmazonBackend.helpers.AllConstantHelpers.CATEGORY_FIELDS.COVER_IMAGE;

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
    public CategoryDto createCategoryService(final CategoryDto categoryDto, final MultipartFile coverImage) throws BadApiRequestExceptions, IOException, CategoryNotFoundExceptions, CategoryExceptions {
        final String methodName="createCategoryService(CategoryDto,MultipartFile)";
        validateNullField(categoryDto,"Category object passed is null",methodName);
        Category category = CategoryDtoToCategory(categoryDto);
        categoryValidationService.validateCategory(Optional.of(category),
                "createCategoryService()", CREATE_CATEGORY);
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
    public CategoryDto updateCategoryServiceByCategoryId(final CategoryDto categoryDto,final MultipartFile coverImage,final String categoryId) throws BadApiRequestExceptions, CategoryNotFoundExceptions, CategoryExceptions, IOException {
        final String methodName="updateCategoryServiceByCategoryId(CategoryDto)";
        validateNullField(categoryDto,"CategoryDto is null",methodName);

        Optional<Category> category=categoryRepository.findById(categoryId);
        categoryValidationService.validateCategory(category,"updateCategoryServiceByCategoryId",NOT_FOUND_CATEGORY);
        Category fetchedCategory=category.get();

        Category updatedCategory=fetchedCategory;
        if(!StringUtils.isBlank(categoryDto.title())
                && categoryDto.title().equals(fetchedCategory.getTitle())){
             updatedCategory=constructCategory(fetchedCategory,CategoryDtoToCategory(categoryDto),TITLE);
             categoryValidationService.validateCategory(Optional.of(updatedCategory),methodName,UPDATE_TITLE);
        }
        if(!StringUtils.isBlank(categoryDto.description())
                && categoryDto.description().equals(fetchedCategory.getDescription())){
               updatedCategory=constructCategory(fetchedCategory,CategoryDtoToCategory(categoryDto), DESCRIPTION);
               categoryValidationService.validateCategory(Optional.of(updatedCategory),methodName,UPDATE_DESCRIPTION);
        }
        if(Objects.isNull(coverImage)){
              final String imageName=imageService.uploadCoverImageByCategoryId(coverImage);
              Category processedCategory=new Category.builder().coverImage(imageName).build();
              updatedCategory=constructCategory(fetchedCategory,processedCategory,COVER_IMAGE);
        }
        return categoryToCategoryDto(updatedCategory);
    }

    /**
     * @param categoryId
     */
    @Override
    public ApiResponse deleteCategoryServiceByCategoryId(final String categoryId) throws CategoryNotFoundExceptions, CategoryExceptions {
        //check for existing categoryId
        Optional<Category> category=categoryRepository.findById(categoryId);
        categoryValidationService.validateCategory(category,
                "deleteCategoryServiceByCategoryId",NOT_FOUND_CATEGORY);
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
    public PageableResponse<CategoryDto> getAllCategoryService(final int pageNumber, final int pageSize, final String sortBy, final String sortDir) throws CategoryNotFoundExceptions, CategoryExceptions {
        final String methodName = "getAllCategoryService() in CategoryServiceImpl";
        final Sort sort = sortDir.equals("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        final Pageable pageableObject = getPageableObject(pageNumber, pageSize, sort);
        Page<Category> userPage = categoryRepository.findAll(pageableObject);
        if(userPage.getContent().isEmpty()) categoryValidationService.validateCategory(Optional.empty()
                ,methodName,NOT_FOUND_CATEGORY);
        return getPageableResponse(userPage, CATEGORY_DTO);
    }

    /**
     * @param categoryId
     * @return
     */
    @Override
    public CategoryDto getCategoryServiceByCategoryId(final String categoryId) throws CategoryNotFoundExceptions, CategoryExceptions {
        Optional<Category> category=categoryRepository.findById(categoryId);
        categoryValidationService.validateCategory(category,
                "getCategoryServiceByCategoryId",NOT_FOUND_CATEGORY);
        return categoryToCategoryDto(category.get());
    }
}
