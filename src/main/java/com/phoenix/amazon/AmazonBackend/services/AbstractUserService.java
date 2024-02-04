package com.phoenix.amazon.AmazonBackend.services;

import com.phoenix.amazon.AmazonBackend.entity.PassWordSet;
import com.phoenix.amazon.AmazonBackend.entity.Users;
import com.phoenix.amazon.AmazonBackend.exceptions.BadApiRequestExceptions;
import com.phoenix.amazon.AmazonBackend.exceptions.UserExceptions;
import com.phoenix.amazon.AmazonBackend.exceptions.UserNotFoundExceptions;
import com.phoenix.amazon.AmazonBackend.repository.IUserRepository;
import com.phoenix.amazon.AmazonBackend.services.validationservice.IUserValidationService;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static com.phoenix.amazon.AmazonBackend.helpers.AllConstantHelpers.USER_FIELDS;
import static com.phoenix.amazon.AmazonBackend.helpers.AllConstantHelpers.USER_FIELD_VALIDATION.VALIDATE_USER_NAME_OR_EMAIL;
import static com.phoenix.amazon.AmazonBackend.helpers.AllConstantHelpers.USER_FIELD_VALIDATION.VALIDATE_USER_ID_OR_USER_NAME;
import static com.phoenix.amazon.AmazonBackend.helpers.AllConstantHelpers.USER_VALIDATION.GET_USER_INFO_BY_EMAIL_USER_NAME;
import static com.phoenix.amazon.AmazonBackend.helpers.AllConstantHelpers.USER_VALIDATION.GET_USER_INFO_BY_USERID_USER_NAME;

public abstract class AbstractUserService {
    private final IUserRepository userRepository;
    private final IUserValidationService userValidationService;

    protected AbstractUserService(final IUserRepository userRepository, final IUserValidationService userValidationService) {
        this.userRepository = userRepository;
        this.userValidationService = userValidationService;
    }

    /**
     * Depending on type of request
     * invoke dao impl of load user
     * <p>
     * LU1 - to load by userId, userName
     * LU2 - to load by userName, email
     */
    protected enum UserLoadType {LU1, LU2}

    /**
     * @param userId     - id of User
     * @param userName   - userName of user
     * @param email      - email of user
     * @param methodName - origin of requesting method
     * @param loadType   - type by which we load user
     * @return Users
     **/
    private Users loadUserByUserNameOrEmailOrUserId(final String userId, final String userName, final String email,
                                                    final String methodName, UserLoadType loadType) throws UserNotFoundExceptions, UserExceptions, BadApiRequestExceptions, IOException {
        Optional<Users> users = Optional.empty();
        switch (loadType) {
            case LU1 -> {
                users = userRepository.findByUserIdOrUserName(userId, userName);
                userValidationService.validateUser(Optional.empty(), users, methodName, GET_USER_INFO_BY_USERID_USER_NAME);
            }
            case LU2 -> {
                users = userRepository.findByPrimaryEmailOrUserName(email, userName);
                userValidationService.validateUser(Optional.empty(), users, methodName, GET_USER_INFO_BY_EMAIL_USER_NAME);
            }
        }
        return users.get();
    }


    /**
     * @param email      - email of user
     * @param userName   - userName of user
     * @param methodName - origin of requesting method
     * @return Users
     **/
    protected Users loadUserByEmailOrUserName(final String email, final String userName, final String methodName) throws UserNotFoundExceptions, UserExceptions, BadApiRequestExceptions, IOException {
        userValidationService.validateFields(null, userName, email, methodName, VALIDATE_USER_NAME_OR_EMAIL);
        return loadUserByUserNameOrEmailOrUserId(null, userName, email, methodName, UserLoadType.LU2);
    }

    /**
     * @param userName   - userName of user
     * @param methodName - origin of requesting method
     * @return Users
     **/
    protected Users loadUserByUserIdOrUserName(final String userId, final String userName, final String methodName) throws UserNotFoundExceptions, UserExceptions, BadApiRequestExceptions, IOException {
        userValidationService.validateFields(userId, userName, null, methodName, VALIDATE_USER_ID_OR_USER_NAME);
        return loadUserByUserNameOrEmailOrUserId(userId, userName, null, methodName, UserLoadType.LU1);
    }

    protected StringBuffer getUserDbField(USER_FIELDS sortBy){
        StringBuffer sortByColumn=new StringBuffer();
        switch (sortBy){
            case USER_NAME -> sortByColumn.append("user_name");
            case FIRST_NAME -> sortByColumn.append("first_name");
            case LAST_NAME -> sortByColumn.append("last_name");
            case PRIMARY_EMAIL -> sortByColumn.append("user_primary_email");
            case SECONDARY_EMAIL -> sortByColumn.append("user_secondary_email");
            case PASSWORD -> sortByColumn.append("user_password");
            case GENDER -> sortByColumn.append("gender");
            case PROFILE_IMAGE -> sortByColumn.append("user_image_name");
            case LAST_SEEN -> sortByColumn.append("last_seen");
            case ABOUT -> sortByColumn.append("about");
        }
        return sortByColumn;
    }

    /**
     * no setter in entity class to stop partial initialization
     * so we need constructUser
     **/

    /**
     * @param oldUser - old user object
     * @param newUser - new user object
     * @param fields  - field of user entity
     * @return Users
     **/
    protected Users constructUser(final Users oldUser, final Users newUser, final USER_FIELDS fields) {
        switch (fields) {
            case USER_NAME -> {
                return new Users.builder()
                        .userName(newUser.getUserName())
                        .firstName(oldUser.getFirstName())
                        .userId(oldUser.getUserId())
                        .lastName(oldUser.getLastName())
                        .primaryEmail(oldUser.getPrimaryEmail())
                        .secondaryEmail(oldUser.getSecondaryEmail())
                        .gender(oldUser.getGender())
                        .password(oldUser.getPassword())
                        .about(oldUser.getAbout())
                        .profileImage(oldUser.getProfileImage())
                        .lastSeen(oldUser.getLastSeen())
                        .build();
            }
            case FIRST_NAME -> {
                return new Users.builder()
                        .firstName(newUser.getFirstName())
                        .userId(oldUser.getUserId())
                        .userName(oldUser.getUserName())
                        .lastName(oldUser.getLastName())
                        .primaryEmail(oldUser.getPrimaryEmail())
                        .secondaryEmail(oldUser.getSecondaryEmail())
                        .gender(oldUser.getGender())
                        .password(oldUser.getPassword())
                        .about(oldUser.getAbout())
                        .profileImage(oldUser.getProfileImage())
                        .lastSeen(oldUser.getLastSeen())
                        .build();
            }
            case LAST_NAME -> {
                return new Users.builder()
                        .lastName(newUser.getLastName())
                        .firstName(oldUser.getFirstName())
                        .userId(oldUser.getUserId())
                        .userName(oldUser.getUserName())
                        .primaryEmail(oldUser.getPrimaryEmail())
                        .secondaryEmail(oldUser.getSecondaryEmail())
                        .gender(oldUser.getGender())
                        .password(oldUser.getPassword())
                        .about(oldUser.getAbout())
                        .profileImage(oldUser.getProfileImage())
                        .lastSeen(oldUser.getLastSeen())
                        .build();

            }
            case ABOUT -> {
                return new Users.builder()
                        .about(newUser.getAbout())
                        .lastName(oldUser.getLastName())
                        .firstName(oldUser.getFirstName())
                        .userId(oldUser.getUserId())
                        .userName(oldUser.getUserName())
                        .primaryEmail(oldUser.getPrimaryEmail())
                        .secondaryEmail(oldUser.getSecondaryEmail())
                        .gender(oldUser.getGender())
                        .password(oldUser.getPassword())
                        .profileImage(oldUser.getProfileImage())
                        .lastSeen(oldUser.getLastSeen())
                        .build();

            }
            case PRIMARY_EMAIL -> {
                return new Users.builder()
                        .primaryEmail(newUser.getPrimaryEmail())
                        .secondaryEmail(oldUser.getSecondaryEmail())
                        .lastName(newUser.getLastName())
                        .firstName(oldUser.getFirstName())
                        .userId(oldUser.getUserId())
                        .userName(oldUser.getUserName())
                        .gender(oldUser.getGender())
                        .password(oldUser.getPassword())
                        .about(oldUser.getAbout())
                        .profileImage(oldUser.getProfileImage())
                        .lastSeen(oldUser.getLastSeen())
                        .build();
            }
            case SECONDARY_EMAIL -> {
                return new Users.builder()
                        .secondaryEmail(newUser.getSecondaryEmail())
                        .primaryEmail(oldUser.getPrimaryEmail())
                        .lastName(newUser.getLastName())
                        .firstName(oldUser.getFirstName())
                        .userId(oldUser.getUserId())
                        .userName(oldUser.getUserName())
                        .gender(oldUser.getGender())
                        .password(oldUser.getPassword())
                        .about(oldUser.getAbout())
                        .profileImage(oldUser.getProfileImage())
                        .lastSeen(oldUser.getLastSeen())
                        .build();
            }
            case GENDER -> {
                return new Users.builder()
                        .gender(newUser.getGender())
                        .lastName(oldUser.getLastName())
                        .firstName(oldUser.getFirstName())
                        .userId(oldUser.getUserId())
                        .userName(oldUser.getUserName())
                        .primaryEmail(oldUser.getPrimaryEmail())
                        .secondaryEmail(oldUser.getSecondaryEmail())
                        .password(oldUser.getPassword())
                        .profileImage(oldUser.getProfileImage())
                        .about(oldUser.getAbout())
                        .lastSeen(oldUser.getLastSeen())
                        .build();
            }
            case PASSWORD -> {
                Set<PassWordSet> oldPassWordSet = oldUser.getPrevious_password_set();
                if (CollectionUtils.isEmpty(oldPassWordSet)) oldPassWordSet = new HashSet<>();
                PassWordSet newPassWordSet = new PassWordSet.builder()
                        .password_id(UUID.randomUUID().toString())
                        .passwords(newUser.getPassword())
                        .users(oldUser).build();
                oldPassWordSet.add(newPassWordSet);
                return new Users.builder()
                        .password(newUser.getPassword())
                        .userId(oldUser.getUserId())
                        .userName(oldUser.getUserName())
                        .firstName(oldUser.getFirstName())
                        .lastName(oldUser.getLastName())
                        .primaryEmail(oldUser.getPrimaryEmail())
                        .secondaryEmail(oldUser.getSecondaryEmail())
                        .gender(oldUser.getGender())
                        .about(oldUser.getAbout())
                        .profileImage(oldUser.getProfileImage())
                        .previous_password_set(oldPassWordSet)
                        .lastSeen(oldUser.getLastSeen())
                        .build();
            }
            case PROFILE_IMAGE -> {
                return new Users.builder()
                        .profileImage(newUser.getProfileImage())
                        .gender(oldUser.getGender())
                        .lastName(oldUser.getLastName())
                        .firstName(oldUser.getFirstName())
                        .userId(oldUser.getUserId())
                        .userName(oldUser.getUserName())
                        .primaryEmail(oldUser.getPrimaryEmail())
                        .secondaryEmail(oldUser.getSecondaryEmail())
                        .password(oldUser.getPassword())
                        .about(oldUser.getAbout())
                        .lastSeen(oldUser.getLastSeen())
                        .build();
            }

        }
        return oldUser;
    }
}
