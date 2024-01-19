package com.phoenix.amazon.AmazonBackend.services.validationservice;

import com.phoenix.amazon.AmazonBackend.entity.Users;
import com.phoenix.amazon.AmazonBackend.exceptions.UserExceptions;

import java.util.List;

import static com.phoenix.amazon.AmazonBackend.helpers.AllConstantHelpers.USER_VALIDATION;

public interface IUserValidationService {
    void validateUser(final Users users,final String methodName,final USER_VALIDATION userValidation) throws UserExceptions;
    void validateUserList(final List<Users> usersList, final String methodName, final  USER_VALIDATION userValidation);
}
