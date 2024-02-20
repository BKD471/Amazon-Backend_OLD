package com.phoenix.amazon.AmazonBackend.services.validationservice.impl;

import com.phoenix.amazon.AmazonBackend.entity.Users;
import com.phoenix.amazon.AmazonBackend.exceptions.BadApiRequestExceptions;
import com.phoenix.amazon.AmazonBackend.exceptions.UserExceptions;
import com.phoenix.amazon.AmazonBackend.exceptions.builder.ExceptionBuilder;
import com.phoenix.amazon.AmazonBackend.services.validationservice.IImageValidationService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;

import static com.phoenix.amazon.AmazonBackend.helpers.AllConstantHelpers.EXCEPTION_CODES.BAD_API_EXEC;
import static com.phoenix.amazon.AmazonBackend.helpers.AllConstantHelpers.EXCEPTION_CODES.USER_EXEC;
import static com.phoenix.amazon.AmazonBackend.helpers.AllConstantHelpers.IMAGE_VALIDATION;

@Service("ImageValidationServicePrimary")
public class ImageValidationServiceImpl implements IImageValidationService {
    private final String userImagePath;
    private final String categoryImagePath;
    private final double USER_IMAGE_MAX_SIZE_IN_KB = 100.0d;
    private final double CATEGORY_IMAGE_MAX_SIZE_IN_KB = 500.0d;
    private final double BYTE_TO_KB_CONVERSION_FACTOR = 0.0009765625d;
    Logger logger = LoggerFactory.getLogger(ImageValidationServiceImpl.class);

    ImageValidationServiceImpl(@Value("${path.services.image.properties}") final String PATH_TO_IMAGE_PROPS) {
        final Properties properties = new Properties();
        try {
            properties.load(new FileInputStream(PATH_TO_IMAGE_PROPS));
        } catch (IOException e) {
            logger.error("Error in reading the props in {} UserValidationService", e.getMessage());
        }
        this.userImagePath = properties.getProperty("user.profile.images.path");
        this.categoryImagePath = properties.getProperty("category.images.path");
    }

    private void checkImageUploadThreshold(final String imageDir, final String imageName, final double threshold, final String methodName) throws IOException, BadApiRequestExceptions {
        final String pathToUserImage = imageDir + File.separator + imageName;
        File file = new File(pathToUserImage);
        double fileSizeInKb = file.length() * BYTE_TO_KB_CONVERSION_FACTOR;

        if (fileSizeInKb > threshold) {
            Files.deleteIfExists(Paths.get(pathToUserImage));
            throw (BadApiRequestExceptions) ExceptionBuilder.builder().className(BadApiRequestExceptions.class)
                    .description(String.format("File should not be greater than %s kb", threshold))
                    .methodName(methodName)
                    .build(BAD_API_EXEC);
        }
    }

    /**
     * @param newUser
     * @param oldUser
     * @param imageValidation
     */
    @Override
    public void validateUserImage(final Users newUser, final Users oldUser, final String methodName, final IMAGE_VALIDATION imageValidation) throws IOException, UserExceptions, BadApiRequestExceptions {
        switch (imageValidation) {
            case GET_PROFILE_IMAGE -> {
                if (StringUtils.isEmpty(oldUser.getProfileImage()))
                    throw (UserExceptions) ExceptionBuilder.builder().className(UserExceptions.class)
                            .description("You dont have any profile image yet").methodName(methodName)
                            .build(USER_EXEC);

            }
            case UPDATE_PROFILE_IMAGE ->
                    checkImageUploadThreshold(userImagePath, newUser.getProfileImage(), USER_IMAGE_MAX_SIZE_IN_KB, methodName);
        }
    }

    public void validateCategoryImage(String imageName, final String methodName, IMAGE_VALIDATION imageValidation) throws IOException, BadApiRequestExceptions {
        switch (imageValidation) {
            case UPLOAD_CATEGORY_IMAGE ->
                    checkImageUploadThreshold(categoryImagePath, imageName, CATEGORY_IMAGE_MAX_SIZE_IN_KB, methodName);
        }
    }
}
