package com.phoenix.amazon.AmazonBackend.services.validationservice;

import com.phoenix.amazon.AmazonBackend.entity.Users;
import com.phoenix.amazon.AmazonBackend.exceptions.BadApiRequestExceptions;
import com.phoenix.amazon.AmazonBackend.exceptions.UserExceptions;

import java.io.IOException;

import static com.phoenix.amazon.AmazonBackend.helpers.AllConstantHelpers.IMAGE_VALIDATION;

public interface IImageValidationService {
    void validateUserImage(final Users newUser, final Users oldUser,final String methodName, final IMAGE_VALIDATION imageValidation) throws IOException, UserExceptions, BadApiRequestExceptions;
    void validateCategoryImage(String imageName, final String methodName, IMAGE_VALIDATION imageValidation) throws IOException, BadApiRequestExceptions;
}
