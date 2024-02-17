package com.phoenix.amazon.AmazonBackend.services.impls;

import com.phoenix.amazon.AmazonBackend.dto.CategoryDto;
import com.phoenix.amazon.AmazonBackend.dto.PageableResponse;
import com.phoenix.amazon.AmazonBackend.entity.Category;
import com.phoenix.amazon.AmazonBackend.exceptions.BadApiRequestExceptions;
import com.phoenix.amazon.AmazonBackend.helpers.MappingHelpers;
import com.phoenix.amazon.AmazonBackend.repository.ICategoryRepository;
import com.phoenix.amazon.AmazonBackend.repository.IUserRepository;
import com.phoenix.amazon.AmazonBackend.services.AbstractService;
import com.phoenix.amazon.AmazonBackend.services.ICategoryService;
import com.phoenix.amazon.AmazonBackend.services.IImageService;
import com.phoenix.amazon.AmazonBackend.services.validationservice.IUserValidationService;
import org.hibernate.mapping.MappingHelper;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.UUID;

import static com.phoenix.amazon.AmazonBackend.helpers.MappingHelpers.CategoryDtoToCategory;
import static com.phoenix.amazon.AmazonBackend.helpers.MappingHelpers.categoryToCategoryDto;

@Service("CategoryServicePrimary")
public class CategoryServiceImpl extends AbstractService implements ICategoryService {
    private final ICategoryRepository categoryRepository;
    private final IImageService imageService;

    CategoryServiceImpl(final IUserRepository userRepository,
                        final IUserValidationService userValidationService,
                        final ICategoryRepository categoryRepository,final IImageService imageService){
        super(userRepository,userValidationService);
        this.categoryRepository=categoryRepository;
        this.imageService=imageService;
    }
    /**
     * @param categoryDto 
     * @return
     */
    @Override
    public CategoryDto createCategoryService(CategoryDto categoryDto) throws BadApiRequestExceptions, IOException {
        Category category= CategoryDtoToCategory(categoryDto);
        final String categoryId= UUID.randomUUID().toString();

        final String coverImageName=imageService.uploadCoverImageByCategoryId(categoryDto.coverImage(),categoryId);
        Category processedCategory=new Category.builder()
                .categoryId(categoryId).description(category.getDescription())
                .coverImage(coverImageName)
                .title(categoryDto.title()).build();

        Category savedCategory= categoryRepository.save(processedCategory);
        return categoryToCategoryDto(savedCategory);
    }

    /**
     * @param categoryDto 
     * @param categoryId
     * @return
     */
    @Override
    public CategoryDto updateCategoryServiceByCategoryId(CategoryDto categoryDto, String categoryId) {
        return null;
    }

    /**
     * @param categoryId 
     */
    @Override
    public void deleteCategoryServiceByCategoryId(String categoryId) {

    }

    /**
     * @return 
     */
    @Override
    public PageableResponse<CategoryDto> getAllCategoryService() {
        return null;
    }

    /**
     * @param categoryId 
     * @return
     */
    @Override
    public CategoryDto getCategoryServiceByCategoryId(String categoryId) {
        return null;
    }
}
