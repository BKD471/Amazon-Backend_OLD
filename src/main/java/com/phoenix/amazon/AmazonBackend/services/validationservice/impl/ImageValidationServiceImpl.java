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
    Logger logger = LoggerFactory.getLogger(ImageValidationServiceImpl.class);

    ImageValidationServiceImpl(@Value("${path.services.image.properties}") final String PATH_TO_IMAGE_PROPS) {
        final Properties properties = new Properties();
        try {
            properties.load(new FileInputStream(PATH_TO_IMAGE_PROPS));
        } catch (IOException e) {
            logger.error("Error in reading the props in {} UserValidationService", e.getMessage());
        }
        this.userImagePath = properties.getProperty("user.profile.images.path");
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
            case UPDATE_PROFILE_IMAGE -> {
                final String pathToImage = userImagePath + File.separator + newUser.getProfileImage();
                File file = new File(pathToImage);
                double fileSizeInKb = (double) (file.length() / 1024);

                if (fileSizeInKb > 100.0d) {
                    Files.delete(Paths.get(pathToImage));
                    throw (BadApiRequestExceptions) ExceptionBuilder.builder().className(BadApiRequestExceptions.class)
                            .description("File should not be greater than 100kb").methodName(methodName)
                            .build(BAD_API_EXEC);
                }
            }
        }
    }
}
