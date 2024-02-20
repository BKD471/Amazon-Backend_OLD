package com.phoenix.amazon.AmazonBackend.services.validationservice;

import com.phoenix.amazon.AmazonBackend.entity.Users;
import com.phoenix.amazon.AmazonBackend.exceptions.BadApiRequestExceptions;
import com.phoenix.amazon.AmazonBackend.exceptions.UserExceptions;


import java.io.IOException;
import java.util.Collection;
import java.util.Optional;

import com.phoenix.amazon.AmazonBackend.exceptions.UserNotFoundExceptions;
import com.phoenix.amazon.AmazonBackend.helpers.AllConstantHelpers.USER_VALIDATION;
import static com.phoenix.amazon.AmazonBackend.helpers.AllConstantHelpers.USER_FIELD_VALIDATION;

public interface IUserValidationService {
    /**
     * @param newUsersOptional - new user object
     * @param oldUsersOptional - old user object
     * @param methodName       - origin method
     * @param userValidation   - user validation type
     * @throws UserExceptions,BadApiRequestExceptions,UserNotFoundExceptions,IOException - list of exceptions being thrown
     */
    void validateUser(final Optional<Users> newUsersOptional, final Optional<Users> oldUsersOptional, final String methodName, final USER_VALIDATION userValidation) throws UserExceptions, BadApiRequestExceptions, UserNotFoundExceptions, IOException;

    /**
     * @param usersList      - collection of users
     * @param methodName     - origin method
     * @param userValidation - user validation type
     * @throws UserNotFoundExceptions - list of exceptions being thrown
     */
    void validateUserList(final Collection<Users> usersList, final String methodName, final USER_VALIDATION userValidation) throws UserNotFoundExceptions;


    /**
     * @param userId              - userId of user
     * @param userName            - userName of user
     * @param primaryEmail        - primary Email of user
     * @param methodName          - origin method
     * @param userFieldValidation - user validation field
     * @throws BadApiRequestExceptions - list of exceptions being thrown
     **/
    void validatePZeroUserFields(final String userId, final String userName, final String primaryEmail, final String methodName, final USER_FIELD_VALIDATION userFieldValidation) throws BadApiRequestExceptions;
}
