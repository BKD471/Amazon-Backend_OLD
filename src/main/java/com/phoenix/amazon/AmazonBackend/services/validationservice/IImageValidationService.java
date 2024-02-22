package com.phoenix.amazon.AmazonBackend.services.validationservice;

import com.phoenix.amazon.AmazonBackend.entity.Users;
import com.phoenix.amazon.AmazonBackend.exceptions.BadApiRequestExceptions;
import com.phoenix.amazon.AmazonBackend.exceptions.UserExceptions;

import java.io.IOException;

import static com.phoenix.amazon.AmazonBackend.helpers.AllConstantHelpers.IMAGE_VALIDATION;

public interface IImageValidationService {
    /**
     * @param newUser         - new user object
     * @param oldUser         - old user object
     * @param methodName      - origin of request
     * @param imageValidation - image validation request type
     * @throws IOException,UserExceptions,BadApiRequestExceptions - list of exceptions being thrown
     ***/
    void validateUserImage(final Users newUser, final Users oldUser, final String methodName, final IMAGE_VALIDATION imageValidation) throws IOException, UserExceptions, BadApiRequestExceptions;

    /**
     * @param imageName       - name iof image
     * @param methodName      - origin of request
     * @param imageValidation - image validation request type
     * @throws IOException,BadApiRequestExceptions - list of exceptions being thrown
     ***/
    void validateCategoryImage(String imageName, final String methodName, IMAGE_VALIDATION imageValidation) throws IOException, BadApiRequestExceptions;
}
