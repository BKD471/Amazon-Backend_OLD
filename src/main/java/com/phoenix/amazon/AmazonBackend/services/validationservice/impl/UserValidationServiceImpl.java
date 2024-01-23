package com.phoenix.amazon.AmazonBackend.services.validationservice.impl;

import com.phoenix.amazon.AmazonBackend.entity.Users;
import com.phoenix.amazon.AmazonBackend.exceptions.BadApiRequestExceptions;
import com.phoenix.amazon.AmazonBackend.exceptions.UserExceptions;
import com.phoenix.amazon.AmazonBackend.exceptions.UserNotFoundExceptions;
import com.phoenix.amazon.AmazonBackend.exceptions.builder.ExceptionBuilder;
import com.phoenix.amazon.AmazonBackend.repository.IUserRepository;
import com.phoenix.amazon.AmazonBackend.services.validationservice.IUserValidationService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

import static com.phoenix.amazon.AmazonBackend.helpers.AllConstantHelpers.EXCEPTION_CODES.BAD_API_EXEC;
import static com.phoenix.amazon.AmazonBackend.helpers.AllConstantHelpers.EXCEPTION_CODES.USER_NOT_FOUND_EXEC;
import static com.phoenix.amazon.AmazonBackend.helpers.AllConstantHelpers.EXCEPTION_CODES.USER_EXEC;
import static com.phoenix.amazon.AmazonBackend.helpers.AllConstantHelpers.USER_VALIDATION;
import static com.phoenix.amazon.AmazonBackend.helpers.AllConstantHelpers.USER_FIELD_VALIDATION;

@Service
public class UserValidationServiceImpl implements IUserValidationService {
    private final IUserRepository userRepository;

    UserValidationServiceImpl(IUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * @param usersOptional  - user object
     * @param userValidation - user validation field
     */
    @Override
    public void validateUser(Optional<Users> usersOptional, String methodName, USER_VALIDATION userValidation) throws UserExceptions, BadApiRequestExceptions, UserNotFoundExceptions {
        // Get all users
        Set<Users> userDtoList = new HashSet<>(userRepository.findAll());
        Users users = null;
        switch (userValidation) {
            case NULL_OBJECT -> {
                if (usersOptional.isEmpty()) throw (BadApiRequestExceptions) ExceptionBuilder.builder()
                        .className(BadApiRequestExceptions.class)
                        .description("Null Users prohibited")
                        .methodName(methodName).build(BAD_API_EXEC);
            }
            case CREATE_USER -> {
                //Null checks
                users = usersOptional.get();
                if (StringUtils.isBlank(users.getUserName())) throw (BadApiRequestExceptions) ExceptionBuilder.builder()
                        .className(BadApiRequestExceptions.class)
                        .description("Null UserName prohibited")
                        .methodName(methodName).build(BAD_API_EXEC);

                if (StringUtils.isBlank(users.getEmail())) throw (BadApiRequestExceptions) ExceptionBuilder.builder()
                        .className(BadApiRequestExceptions.class)
                        .description("Null email prohibited")
                        .methodName(methodName).build(BAD_API_EXEC);

                if (StringUtils.isBlank(users.getFirstName()))
                    throw (BadApiRequestExceptions) ExceptionBuilder.builder()
                            .className(BadApiRequestExceptions.class)
                            .description("Null firstName prohibited")
                            .methodName(methodName).build(BAD_API_EXEC);

                // Check for existing users

                // Existing email
                final String EMAIL = users.getEmail();
                Predicate<Users> checkEmailExist = (Users user) -> user.getEmail().equalsIgnoreCase(EMAIL);
                boolean isEmailPresent = userDtoList.stream().anyMatch(checkEmailExist);

                if (isEmailPresent) throw (UserExceptions) ExceptionBuilder.builder()
                        .className(UserExceptions.class)
                        .description(String.format("There's an account with Email %s", EMAIL))
                        .methodName(methodName).build(USER_EXEC);

                // Existing userName
                final String USER_NAME = users.getUserName();
                Predicate<Users> checkUserNameExist = (Users user) -> user.getUserName().equalsIgnoreCase(USER_NAME);
                boolean isUserNamePresent = userDtoList.stream().anyMatch(checkUserNameExist);

                if (isUserNamePresent) throw (UserExceptions) ExceptionBuilder.builder()
                        .className(UserExceptions.class)
                        .description(String.format("There's an account with UserName %s", USER_NAME))
                        .methodName(methodName).build(USER_EXEC);

                //this case is rare and hypothetical, it happens when UUID will generate same userId twice
                final String userId = users.getUserId();
                Predicate<Users> checkUserIdExist = (Users user) -> user.getUserName().equalsIgnoreCase(userId);
                boolean isUserIdPresent = userDtoList.stream().anyMatch(checkUserIdExist);

                if (isUserIdPresent) throw (UserExceptions) ExceptionBuilder.builder()
                        .className(UserExceptions.class)
                        .description("System Error In generating user")
                        .methodName(methodName).build(BAD_API_EXEC);
            }
            case GET_USER_INFO_BY_EMAIL_USER_NAME -> {
                if (usersOptional.isEmpty()) throw (UserNotFoundExceptions) ExceptionBuilder.builder()
                        .className(UserExceptions.class)
                        .description("No User with this email or UserName")
                        .methodName(methodName).build(USER_NOT_FOUND_EXEC);
            }
            case GET_USER_INFO_BY_USERID_USER_NAME -> {
                if (usersOptional.isEmpty()) throw (UserNotFoundExceptions) ExceptionBuilder.builder()
                        .className(UserExceptions.class)
                        .description("No User with this UserId or UserName")
                        .methodName(methodName).build(USER_NOT_FOUND_EXEC);
            }
            case UPDATE_USER_BY_USER_ID_OR_USER_NAME -> {
                // Existing email
                if (usersOptional.isEmpty()) throw (UserNotFoundExceptions) ExceptionBuilder.builder()
                        .className(UserExceptions.class)
                        .description("No User")
                        .methodName(methodName).build(USER_NOT_FOUND_EXEC);

                users = usersOptional.get();
                final String EMAIL = users.getEmail();
                Predicate<Users> checkEmailExist = (Users user) -> user.getEmail().equalsIgnoreCase(EMAIL);
                boolean isEmailPresent = userDtoList.stream().anyMatch(checkEmailExist);

                if (isEmailPresent) throw (UserExceptions) ExceptionBuilder.builder()
                        .className(UserExceptions.class)
                        .description(String.format("There's an account with Email %s", EMAIL))
                        .methodName(methodName).build(USER_EXEC);
            }
            case DELETE_USER_BY_USER_ID_OR_USER_NAME -> {
                if (usersOptional.isEmpty()) throw (UserNotFoundExceptions) ExceptionBuilder.builder()
                        .className(UserExceptions.class)
                        .description("No User Found")
                        .methodName(methodName).build(USER_NOT_FOUND_EXEC);
            }
        }
    }

    /**
     * @param userSet        - set of users
     * @param methodName     - origin of request method
     * @param userValidation - user validation field
     */
    @Override
    public void validateUserList(Collection<Users> userSet, String methodName, USER_VALIDATION userValidation) throws UserNotFoundExceptions {
        switch (userValidation) {
            case GET_ALL_USERS -> {
                if (CollectionUtils.isEmpty(userSet)) throw (UserNotFoundExceptions) ExceptionBuilder.builder()
                        .className(UserExceptions.class)
                        .description("Our Database have no Users")
                        .methodName(methodName).build(USER_NOT_FOUND_EXEC);
            }
            case SEARCH_ALL_USERS_BY_USER_NAME -> {
                if (CollectionUtils.isEmpty(userSet)) throw (UserNotFoundExceptions) ExceptionBuilder.builder()
                        .className(UserExceptions.class)
                        .description("Our Database have no Users With this Username")
                        .methodName(methodName).build(USER_NOT_FOUND_EXEC);
            }
            case SEARCH_USER_BY_EMAIL -> {
                if (CollectionUtils.isEmpty(userSet)) throw (UserNotFoundExceptions) ExceptionBuilder.builder()
                        .className(UserExceptions.class)
                        .description("Our Database have no Users With this email")
                        .methodName(methodName).build(USER_NOT_FOUND_EXEC);
            }
            case SEARCH_ALL_USERS_BY_FIRST_NAME -> {
                if (CollectionUtils.isEmpty(userSet)) throw (UserNotFoundExceptions) ExceptionBuilder.builder()
                        .className(UserExceptions.class)
                        .description("Our Database have no Users With this firstName")
                        .methodName(methodName).build(USER_NOT_FOUND_EXEC);
            }
            case SEARCH_ALL_USERS_BY_LAST_NAME -> {
                if (CollectionUtils.isEmpty(userSet)) throw (UserNotFoundExceptions) ExceptionBuilder.builder()
                        .className(UserExceptions.class)
                        .description("Our Database have no Users With this lastName")
                        .methodName(methodName).build(USER_NOT_FOUND_EXEC);
            }
            case SEARCH_USER_BY_USER_NAME -> {
                if (CollectionUtils.isEmpty(userSet)) throw (UserNotFoundExceptions) ExceptionBuilder.builder()
                        .className(UserExceptions.class)
                        .description("Our Database have no Users With this UserName")
                        .methodName(methodName).build(USER_NOT_FOUND_EXEC);
            }
            case SEARCH_ALL_USERS_BY_GENDER -> {
                if (CollectionUtils.isEmpty(userSet)) throw (UserNotFoundExceptions) ExceptionBuilder.builder()
                        .className(UserExceptions.class)
                        .description("Our Database have no Users With this Gender")
                        .methodName(methodName).build(USER_NOT_FOUND_EXEC);
            }
        }
    }

    /**
     * @param userId              - id of user
     * @param userName            - username of user
     * @param email               - email of user
     * @param methodName          - origin method
     * @param userFieldValidation - user validation field
     */
    @Override
    public void validateFields(final String userId, final String userName, final String email, final String methodName,
                               final USER_FIELD_VALIDATION userFieldValidation) throws BadApiRequestExceptions {
        final BiPredicate<String, String> checkBothFieldsNull = (String a, String b) -> Objects.isNull(a) && Objects.isNull(b);
        switch (userFieldValidation) {
            case VALIDATE_USER_ID_OR_USER_NAME -> {
                if (checkBothFieldsNull.test(userId, userName))
                    throw (BadApiRequestExceptions) ExceptionBuilder.builder()
                            .className(BadApiRequestExceptions.class)
                            .description("Please provide non null username or user Id")
                            .methodName(methodName).build(BAD_API_EXEC);
            }
            case VALIDATE_USER_NAME_OR_EMAIL -> {
                if (checkBothFieldsNull.test(userName, email))
                    throw (BadApiRequestExceptions) ExceptionBuilder.builder()
                            .className(BadApiRequestExceptions.class)
                            .description("Please provide non null username or email")
                            .methodName(methodName).build(BAD_API_EXEC);
            }
        }
    }
}