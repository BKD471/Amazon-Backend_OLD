package com.phoenix.amazon.AmazonBackend.services.validationservice.impl;

import com.phoenix.amazon.AmazonBackend.entity.Users;
import com.phoenix.amazon.AmazonBackend.exceptions.BadApiRequestExceptions;
import com.phoenix.amazon.AmazonBackend.exceptions.UserExceptions;
import com.phoenix.amazon.AmazonBackend.exceptions.builder.ExceptionBuilder;
import com.phoenix.amazon.AmazonBackend.repository.IUserRepository;
import com.phoenix.amazon.AmazonBackend.services.validationservice.IUserValidationService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;

import static com.phoenix.amazon.AmazonBackend.helpers.AllConstantHelpers.EXCEPTION_CODES.BAD_API_EXEC;
import static com.phoenix.amazon.AmazonBackend.helpers.AllConstantHelpers.EXCEPTION_CODES.USER_EXEC;
import static com.phoenix.amazon.AmazonBackend.helpers.AllConstantHelpers.USER_VALIDATION;

@Service
public class UserValidationServiceImpl implements IUserValidationService {
    private final IUserRepository userRepository;
    UserValidationServiceImpl(IUserRepository userRepository){
        this.userRepository=userRepository;
    }
    /**
     * @param users
     * @param userValidation
     */
    @Override
    public void validateUser(Users users, String methodName, USER_VALIDATION userValidation) throws UserExceptions {
        switch (userValidation) {
            case CREATE_USER -> {
                //Null checks
                if(Objects.isNull(users)) throw (BadApiRequestExceptions) ExceptionBuilder.builder()
                        .className(BadApiRequestExceptions.class)
                        .description("Null Users prohibited")
                        .methodName(methodName).build(BAD_API_EXEC);

                if(StringUtils.isBlank(users.getUserName())) throw (BadApiRequestExceptions) ExceptionBuilder.builder()
                        .className(BadApiRequestExceptions.class)
                        .description("Null UserName prohibited")
                        .methodName(methodName).build(BAD_API_EXEC);

                if(StringUtils.isBlank(users.getEmail())) throw (BadApiRequestExceptions) ExceptionBuilder.builder()
                        .className(BadApiRequestExceptions.class)
                        .description("Null email prohibited")
                        .methodName(methodName).build(BAD_API_EXEC);

                if(StringUtils.isBlank(users.getFirstName())) throw (BadApiRequestExceptions) ExceptionBuilder.builder()
                        .className(BadApiRequestExceptions.class)
                        .description("Null firstName prohibited")
                        .methodName(methodName).build(BAD_API_EXEC);

                if(StringUtils.isBlank(users.getLastName())) throw (BadApiRequestExceptions) ExceptionBuilder.builder()
                        .className(BadApiRequestExceptions.class)
                        .description("Null LastName prohibited")
                        .methodName(methodName).build(BAD_API_EXEC);

                //Check for existing users
                List<Users> userDtoList=userRepository.findAll();

                final String email=users.getEmail();
                Predicate<Users> checkEmailExist=(Users user)-> user.getEmail().equalsIgnoreCase(email);
                boolean isEmailPresent=userDtoList.stream().anyMatch(checkEmailExist);

                if(isEmailPresent) throw (UserExceptions) ExceptionBuilder.builder()
                        .className(UserExceptions.class)
                        .description(String.format("There's an account with Email %s",email))
                        .methodName(methodName).build(USER_EXEC);

                //this case is rare and hypothetical, it happens when UUID will generate same userId twice
                final String userId= users.getUserId();
                Predicate<Users> checkUserIdExist=(Users user)->user.getUserName().equalsIgnoreCase(userId);
                boolean isUserIdPresent=userDtoList.stream().anyMatch(checkUserIdExist);

                if(isUserIdPresent) throw (UserExceptions) ExceptionBuilder.builder()
                        .className(UserExceptions.class)
                        .description("System Error In generating user")
                        .methodName(methodName).build(BAD_API_EXEC);
            }
            case GET_USER_INFO_BY_EMAIL_USER_NAME -> {
                if (Objects.isNull(users)) throw (UserExceptions) ExceptionBuilder.builder()
                        .className(UserExceptions.class)
                        .description("No User with Email")
                        .methodName(methodName).build(USER_EXEC);
            }
            case UPDATE_USER_BY_USER_ID_OR_USER_NAME -> {

            }
            case DELETE_USER_BY_USER_ID_OR_USER_NAME -> {
                if(Objects.isNull(users)) throw (UserExceptions) ExceptionBuilder.builder()
                        .className(UserExceptions.class)
                        .description("No User Found")
                        .methodName(methodName).build(USER_EXEC);
            }
        }
    }

    /**
     * @param usersList
     * @param methodName
     * @param userValidation
     */
    @Override
    public void validateUserList(List<Users> usersList, String methodName, USER_VALIDATION userValidation) {
        switch (userValidation){
            case GET_ALL_USERS -> {
                if(CollectionUtils.isEmpty(usersList)) throw (UserExceptions) ExceptionBuilder.builder()
                        .className(UserExceptions.class)
                        .description("Our Database have no Users")
                        .methodName(methodName).build(USER_EXEC);
            }
            case SEARCH_ALL_USERS_BY_USER_NAME -> {
                if(CollectionUtils.isEmpty(usersList)) throw (UserExceptions) ExceptionBuilder.builder()
                        .className(UserExceptions.class)
                        .description("Our Database have no Users With this Username")
                        .methodName(methodName).build(USER_EXEC);
            }
        }
    }
}
