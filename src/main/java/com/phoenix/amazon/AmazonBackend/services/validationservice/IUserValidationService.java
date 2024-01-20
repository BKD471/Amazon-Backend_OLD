package com.phoenix.amazon.AmazonBackend.services.validationservice;

import com.phoenix.amazon.AmazonBackend.entity.Users;
import com.phoenix.amazon.AmazonBackend.exceptions.UserExceptions;


import java.util.Set;
import com.phoenix.amazon.AmazonBackend.helpers.AllConstantHelpers.USER_VALIDATION;
import static com.phoenix.amazon.AmazonBackend.helpers.AllConstantHelpers.USER_FIELD_VALIDATION;

public interface IUserValidationService {
    /**
     * @param users - user object
     * @param methodName - origin method
     * @param userValidation - user validation type
     */
    void validateUser(final Users users, final String methodName, final USER_VALIDATION userValidation) throws UserExceptions;

    /**
     * @param usersList - set of users
     * @param methodName - origin method
     * @param userValidation - user validation type
     */
    void validateUserList(final Set<Users> usersList, final String methodName, final USER_VALIDATION userValidation);

    /**
     * @param userId - id of user
     * @param userName - username of user
     * @param email - email of user
     * @param methodName - origin method
     * @param userFieldValidation - user validation field
     */
    void validateFields(final String userId, final String userName, final String email, final String methodName, final USER_FIELD_VALIDATION userFieldValidation);
}
