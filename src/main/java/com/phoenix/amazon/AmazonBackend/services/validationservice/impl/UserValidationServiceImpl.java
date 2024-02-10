package com.phoenix.amazon.AmazonBackend.services.validationservice.impl;

import com.phoenix.amazon.AmazonBackend.entity.PassWordSet;
import com.phoenix.amazon.AmazonBackend.entity.Users;
import com.phoenix.amazon.AmazonBackend.exceptions.BadApiRequestExceptions;
import com.phoenix.amazon.AmazonBackend.exceptions.UserExceptions;
import com.phoenix.amazon.AmazonBackend.exceptions.UserNotFoundExceptions;
import com.phoenix.amazon.AmazonBackend.exceptions.builder.ExceptionBuilder;
import com.phoenix.amazon.AmazonBackend.repository.IUserRepository;
import com.phoenix.amazon.AmazonBackend.services.validationservice.IUserValidationService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Properties;
import java.util.Set;
import java.util.HashSet;
import java.util.Optional;
import java.util.Collection;
import java.util.Objects;
import java.util.function.Predicate;

import com.phoenix.amazon.AmazonBackend.helpers.TriPredicate;

import static com.phoenix.amazon.AmazonBackend.helpers.AllConstantHelpers.EXCEPTION_CODES.BAD_API_EXEC;
import static com.phoenix.amazon.AmazonBackend.helpers.AllConstantHelpers.EXCEPTION_CODES.USER_NOT_FOUND_EXEC;
import static com.phoenix.amazon.AmazonBackend.helpers.AllConstantHelpers.EXCEPTION_CODES.USER_EXEC;
import static com.phoenix.amazon.AmazonBackend.helpers.AllConstantHelpers.USER_VALIDATION;
import static com.phoenix.amazon.AmazonBackend.helpers.AllConstantHelpers.USER_FIELD_VALIDATION;

@Service
public class UserValidationServiceImpl implements IUserValidationService {
    private final String imagePath;
    private final IUserRepository userRepository;
    private final Properties properties;
    Logger logger= LoggerFactory.getLogger(UserValidationServiceImpl.class);

    UserValidationServiceImpl(IUserRepository userRepository,
                              @Value("${path.services.user.image.properties}") final String PATH_TO_PROPS) {
        this.userRepository = userRepository;

        properties=new Properties();
        try {
            properties.load(new FileInputStream(PATH_TO_PROPS));
        }catch (IOException e){
            logger.error("Error in reading the props in {} UserValidationService", e.getMessage());
        }
        this.imagePath=properties.getProperty("user.profile.images.path");
    }

    private void checkEmails(final Set<Users> usersSet, final String new_email, final String methodName, final String checkFor) throws UserExceptions {
        // Check for previous existing
        Predicate<Users> checkEmailExist = null;
        if (checkFor.equalsIgnoreCase("primary"))
            checkEmailExist = (Users user) -> new_email.equalsIgnoreCase(user.getPrimaryEmail());
        else checkEmailExist = (Users user) -> new_email.equalsIgnoreCase(user.getSecondaryEmail());

        boolean isEmailPresent = usersSet.stream().anyMatch(checkEmailExist);
        if (isEmailPresent) throw (UserExceptions) ExceptionBuilder.builder()
                .className(UserExceptions.class)
                .description(String.format("There's an account with Email %s", new_email))
                .methodName(methodName).build(USER_EXEC);

        // for primary check for existing in secondary,
        // for secondary check for existing in primary
        if (checkFor.equalsIgnoreCase("primary"))
            checkEmailExist = (Users user) -> new_email.equalsIgnoreCase(user.getSecondaryEmail());
        if (checkFor.equalsIgnoreCase("secondary"))
            checkEmailExist = (Users user) -> new_email.equalsIgnoreCase(user.getPrimaryEmail());

        isEmailPresent = usersSet.stream().anyMatch(checkEmailExist);
        if (isEmailPresent) throw (UserExceptions) ExceptionBuilder.builder()
                .className(UserExceptions.class)
                .description(String.format("There's an account with Email %s", new_email))
                .methodName(methodName).build(USER_EXEC);
    }

    private void checkUserName(final Set<Users> usersSet, final String new_user_name, final String methodName) throws UserExceptions {
        Predicate<Users> checkUserNameExist = (Users user) -> user.getUserName().equalsIgnoreCase(new_user_name);
        boolean isUserNamePresent = usersSet.stream().anyMatch(checkUserNameExist);

        if (isUserNamePresent) throw (UserExceptions) ExceptionBuilder.builder()
                .className(UserExceptions.class)
                .description(String.format("There's an account with UserName %s", new_user_name))
                .methodName(methodName).build(USER_EXEC);
    }

    /**
     * @param oldUsersOptional - old user object
     * @param newUsersOptional - new user object
     * @param userValidation   - user validation field
     */
    @Override
    public void validateUser(final Optional<Users> newUsersOptional, final Optional<Users> oldUsersOptional, String methodName, USER_VALIDATION userValidation) throws UserExceptions, BadApiRequestExceptions, UserNotFoundExceptions, IOException {
        // Get all users
        final Set<Users> userDtoList = new HashSet<>(userRepository.findAll());
        Users newUser = null;
        Users oldUser = null;
        if (newUsersOptional.isPresent()) newUser = newUsersOptional.get();
        if (oldUsersOptional.isPresent()) oldUser = oldUsersOptional.get();

        switch (userValidation) {
            case NULL_OBJECT -> {
                if (newUsersOptional.isEmpty()) throw (BadApiRequestExceptions) ExceptionBuilder.builder()
                        .className(BadApiRequestExceptions.class)
                        .description("Null Users prohibited")
                        .methodName(methodName).build(BAD_API_EXEC);
            }
            case CREATE_USER -> {
                // Null user check is already taken care

                // Existing primary & secondary email
                checkEmails(userDtoList, newUser.getPrimaryEmail(), methodName, "primary");
                if (!StringUtils.isBlank(newUser.getSecondaryEmail())) {
                    if (newUser.getPrimaryEmail().equalsIgnoreCase(newUser.getSecondaryEmail())) {
                        throw (UserExceptions) ExceptionBuilder.builder()
                                .className(UserExceptions.class)
                                .description("Primary & Secondary email must not be same")
                                .methodName(methodName).build(USER_EXEC);
                    }
                    checkEmails(userDtoList, newUser.getSecondaryEmail(), methodName, "secondary");
                }

                // Existing userName
                checkUserName(userDtoList, newUser.getUserName(), methodName);

                //this case is rare and hypothetical, it happens when UUID will generate same userId twice
                final String userId = newUser.getUserId();
                Predicate<Users> checkUserIdExist = (Users user) -> user.getUserName().equalsIgnoreCase(userId);
                boolean isUserIdPresent = userDtoList.stream().anyMatch(checkUserIdExist);

                if (isUserIdPresent) throw (UserExceptions) ExceptionBuilder.builder()
                        .className(UserExceptions.class)
                        .description("System Error In generating user")
                        .methodName(methodName).build(BAD_API_EXEC);
            }
            case GET_USER_INFO_BY_USERID_USER_NAME_PRIMARY_EMAIL -> {
                if (oldUsersOptional.isEmpty()) throw (UserNotFoundExceptions) ExceptionBuilder.builder()
                        .className(UserNotFoundExceptions.class)
                        .description("No User with this userid or UserName or primary email")
                        .methodName(methodName).build(USER_NOT_FOUND_EXEC);
            }
            case UPDATE_USERNAME -> {
                // Null user check is already taken care during loading the user
                checkUserName(userDtoList, newUser.getUserName(), methodName);
            }
            case UPDATE_PRIMARY_EMAIL -> {
                // Null user check is already taken care during loading the user
                checkEmails(userDtoList, newUser.getPrimaryEmail(), methodName, "primary");
            }
            case UPDATE_SECONDARY_EMAIL -> {
                // Null user check is already taken care during loading the user
                checkEmails(userDtoList, newUser.getSecondaryEmail(), methodName, "secondary");
            }
            case UPDATE_PASSWORD -> {
                // null check for old user is already taken care while loading the user
                Set<PassWordSet> passwordList = oldUser.getPrevious_password_set();
                final String NEW_PASSWORD = newUser.getPassword();

                Predicate<PassWordSet> isPassWordMatched = (PassWordSet password) -> password.getPasswords().equals(NEW_PASSWORD);
                boolean isOldPassword = passwordList.stream().anyMatch(isPassWordMatched);

                if (isOldPassword) throw (UserExceptions) ExceptionBuilder.builder()
                        .className(UserExceptions.class)
                        .description("You already had this password before")
                        .methodName(methodName).build(USER_EXEC);
            }
            case UPDATE_PROFILE_IMAGE -> {
                final String pathToImage = imagePath + File.separator + newUser.getProfileImage();
                File file = new File(pathToImage);
                double fileSizeInKb = (double) (file.length() / 1024);

                if (fileSizeInKb > 100.0d) {
                    Files.delete(Paths.get(pathToImage));
                    throw (BadApiRequestExceptions) ExceptionBuilder.builder().className(BadApiRequestExceptions.class)
                            .description("File should not be greater than 100kb").methodName(methodName)
                            .build(BAD_API_EXEC);
                }
            }
            case GET_PROFILE_IMAGE ->{
                 if(StringUtils.isEmpty(oldUser.getProfileImage()))
                     throw (UserExceptions) ExceptionBuilder.builder().className(UserExceptions.class)
                         .description("You dont have any profile image yet").methodName(methodName)
                         .build(USER_EXEC);
            }
            case DELETE_USER_BY_USER_ID_OR_USER_NAME -> {
                if (oldUsersOptional.isEmpty()) throw (UserNotFoundExceptions) ExceptionBuilder.builder()
                        .className(UserExceptions.class)
                        .description("No User Found")
                        .methodName(methodName).build(USER_NOT_FOUND_EXEC);
            }
            case VALIDATE_PASSWORD -> {
                //null check is already done for old & new password using annotation
                if (!oldUser.getPassword().equals(newUser.getPassword()))
                    throw (UserExceptions) ExceptionBuilder.builder()
                            .className(UserExceptions.class)
                            .description("Old Password didn't matched")
                            .methodName(methodName).build(USER_EXEC);
            }
        }
    }

    /**
     * @param userSet        - set of users
     * @param methodName     - origin of request method
     * @param userValidation - user validation field
     */
    @Override
    public void validateUserList(final Collection<Users> userSet, final String methodName, final USER_VALIDATION userValidation) throws UserNotFoundExceptions {
        switch (userValidation) {
            case GET_ALL_USERS -> {
                if (CollectionUtils.isEmpty(userSet)) throw (UserNotFoundExceptions) ExceptionBuilder.builder()
                        .className(UserNotFoundExceptions.class)
                        .description("Our Database have no Users")
                        .methodName(methodName).build(USER_NOT_FOUND_EXEC);
            }
            case SEARCH_ALL_USERS_BY_USER_NAME -> {
                if (CollectionUtils.isEmpty(userSet)) throw (UserNotFoundExceptions) ExceptionBuilder.builder()
                        .className(UserNotFoundExceptions.class)
                        .description("Our Database have no Users With this Username")
                        .methodName(methodName).build(USER_NOT_FOUND_EXEC);
            }
            case SEARCH_USER_BY_EMAIL -> {
                if (CollectionUtils.isEmpty(userSet)) throw (UserNotFoundExceptions) ExceptionBuilder.builder()
                        .className(UserNotFoundExceptions.class)
                        .description("Our Database have no Users With this email")
                        .methodName(methodName).build(USER_NOT_FOUND_EXEC);
            }
            case SEARCH_ALL_USERS_BY_FIRST_NAME -> {
                if (CollectionUtils.isEmpty(userSet)) throw (UserNotFoundExceptions) ExceptionBuilder.builder()
                        .className(UserNotFoundExceptions.class)
                        .description("Our Database have no Users With this firstName")
                        .methodName(methodName).build(USER_NOT_FOUND_EXEC);
            }
            case SEARCH_ALL_USERS_BY_LAST_NAME -> {
                if (CollectionUtils.isEmpty(userSet)) throw (UserNotFoundExceptions) ExceptionBuilder.builder()
                        .className(UserNotFoundExceptions.class)
                        .description("Our Database have no Users With this lastName")
                        .methodName(methodName).build(USER_NOT_FOUND_EXEC);
            }
            case SEARCH_USER_BY_USER_NAME -> {
                if (CollectionUtils.isEmpty(userSet)) throw (UserNotFoundExceptions) ExceptionBuilder.builder()
                        .className(UserNotFoundExceptions.class)
                        .description("Our Database have no Users With this UserName")
                        .methodName(methodName).build(USER_NOT_FOUND_EXEC);
            }
            case SEARCH_ALL_USERS_BY_GENDER -> {
                if (CollectionUtils.isEmpty(userSet)) throw (UserNotFoundExceptions) ExceptionBuilder.builder()
                        .className(UserNotFoundExceptions.class)
                        .description("Our Database have no Users With this Gender")
                        .methodName(methodName).build(USER_NOT_FOUND_EXEC);
            }
        }
    }


    /**
     * @param userId              - id of user
     * @param userName            - username of user
     * @param primaryEmail        - primary email of user
     * @param methodName          - origin method
     * @param userFieldValidation - user validation field
     */
    @Override
    public void validatePZeroUserFields(final String userId, final String userName, final String primaryEmail, final String methodName,
                                        final USER_FIELD_VALIDATION userFieldValidation) throws BadApiRequestExceptions {
        final TriPredicate<String, String, String> checkAllFieldNull = (String uid, String uname, String email) -> Objects.isNull(uid) && Objects.isNull(uname) && Objects.isNull(email);
        switch (userFieldValidation) {
            case VALIDATE_USER_ID_OR_USER_NAME_OR_PRIMARY_EMAIL -> {
                if (checkAllFieldNull.test(userId, userName, primaryEmail))
                    throw (BadApiRequestExceptions) ExceptionBuilder.builder()
                            .className(BadApiRequestExceptions.class)
                            .description("Please provide non null userid or username or primary email")
                            .methodName(methodName).build(BAD_API_EXEC);
            }
        }
    }

    /**
     * @param field              - field to test
     * @param descriptionMessage - description
     * @param methodName         - place of origin
     * @throws BadApiRequestExceptions - list of exceptions being thrown
     **/
    @Override
    public void validateNullField(final String field, final String descriptionMessage, final String methodName) throws BadApiRequestExceptions {
        if (Objects.isNull(field) || StringUtils.isBlank(field))
            throw (BadApiRequestExceptions) ExceptionBuilder.builder()
                    .className(BadApiRequestExceptions.class)
                    .description(descriptionMessage)
                    .methodName(methodName).build(BAD_API_EXEC);
    }
}
