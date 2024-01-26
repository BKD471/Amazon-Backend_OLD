package com.phoenix.amazon.AmazonBackend.services;

import com.phoenix.amazon.AmazonBackend.entity.Users;
import com.phoenix.amazon.AmazonBackend.exceptions.BadApiRequestExceptions;
import com.phoenix.amazon.AmazonBackend.exceptions.UserExceptions;
import com.phoenix.amazon.AmazonBackend.exceptions.UserNotFoundExceptions;
import com.phoenix.amazon.AmazonBackend.repository.IUserRepository;
import com.phoenix.amazon.AmazonBackend.services.validationservice.IUserValidationService;

import java.util.Optional;
import java.util.Set;

import static com.phoenix.amazon.AmazonBackend.helpers.AllConstantHelpers.USER_FIELDS;
import static com.phoenix.amazon.AmazonBackend.helpers.AllConstantHelpers.USER_FIELD_VALIDATION.VALIDATE_USER_NAME_OR_EMAIL;
import static com.phoenix.amazon.AmazonBackend.helpers.AllConstantHelpers.USER_FIELD_VALIDATION.VALIDATE_USER_ID_OR_USER_NAME;
import static com.phoenix.amazon.AmazonBackend.helpers.AllConstantHelpers.USER_VALIDATION.GET_USER_INFO_BY_EMAIL_USER_NAME;
import static com.phoenix.amazon.AmazonBackend.helpers.AllConstantHelpers.USER_VALIDATION.GET_USER_INFO_BY_USERID_USER_NAME;
import static com.phoenix.amazon.AmazonBackend.helpers.AllConstantHelpers.USER_VALIDATION.SEARCH_ALL_USERS_BY_USER_NAME;

public abstract class AbstractService {
    private final IUserRepository userRepository;
    private final IUserValidationService userValidationService;

    protected AbstractService(IUserRepository userRepository, IUserValidationService userValidationService) {
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
                                                    final String methodName, UserLoadType loadType) throws UserNotFoundExceptions, UserExceptions, BadApiRequestExceptions {
        Optional<Users> users = Optional.empty();
        switch (loadType) {
            case LU1 -> {
                users = userRepository.findByUserIdOrUserName(userId, userName);
                userValidationService.validateUser(users, methodName, GET_USER_INFO_BY_USERID_USER_NAME);
            }
            case LU2 -> {
                users = userRepository.findByPrimaryEmailAndUserName(email, userName);
                userValidationService.validateUser(users, methodName, GET_USER_INFO_BY_EMAIL_USER_NAME);
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
    protected Users loadUserByEmailOrUserName(final String email, final String userName, final String methodName) throws UserNotFoundExceptions, UserExceptions, BadApiRequestExceptions {
        userValidationService.validateFields(null, userName, email, methodName, VALIDATE_USER_NAME_OR_EMAIL);
        return loadUserByUserNameOrEmailOrUserId(null, userName, email, methodName, UserLoadType.LU2);
    }

    /**
     * @param userName   - userName of user
     * @param methodName - origin of requesting method
     * @return Users
     **/
    protected Users loadUserByUserIdOrUserName(final String userId, final String userName, final String methodName) throws UserNotFoundExceptions, UserExceptions, BadApiRequestExceptions {
        userValidationService.validateFields(userId, userName, null, methodName, VALIDATE_USER_ID_OR_USER_NAME);
        return loadUserByUserNameOrEmailOrUserId(userId, userName, null, methodName, UserLoadType.LU1);
    }

    /**
     * @param userNameLike - keyword to search for all similar user name
     * @param methodName   - origin of requesting method
     * @return Set<Users> - set of all found users
     **/
    protected Set<Users> loadAllUserByUserNameMatched(final String userNameLike, final String methodName) throws UserNotFoundExceptions {
        Set<Users> allUsersWithNearlyUserName = userRepository.findAllByUserNameContaining(userNameLike).get();
        userValidationService.validateUserList(allUsersWithNearlyUserName, methodName, SEARCH_ALL_USERS_BY_USER_NAME);
        return allUsersWithNearlyUserName;
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
    protected Users constructUser(Users oldUser, Users newUser, USER_FIELDS fields) {
        switch (fields) {
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
                Set<String> passWordSet=oldUser.getPrevious_password_set();
                passWordSet.add(newUser.getPassword());
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
                        .previous_password_set(passWordSet)
                        .lastSeen(oldUser.getLastSeen())
                        .build();
            }

        }
        return oldUser;
    }
}
