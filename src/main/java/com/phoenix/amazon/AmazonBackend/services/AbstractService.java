package com.phoenix.amazon.AmazonBackend.services;

import com.phoenix.amazon.AmazonBackend.entity.Users;
import com.phoenix.amazon.AmazonBackend.repository.IUserRepository;
import com.phoenix.amazon.AmazonBackend.services.validationservice.IUserValidationService;

import java.util.Optional;
import java.util.Set;

import static com.phoenix.amazon.AmazonBackend.helpers.AllConstantHelpers.USER_FIELDS;
import static com.phoenix.amazon.AmazonBackend.helpers.AllConstantHelpers.USER_FIELD_VALIDATION.VALIDATE_USER_NAME_OR_EMAIL;
import static com.phoenix.amazon.AmazonBackend.helpers.AllConstantHelpers.USER_FIELD_VALIDATION.VALIDATE_USER_ID_OR_USER_NAME;
import static com.phoenix.amazon.AmazonBackend.helpers.AllConstantHelpers.USER_VALIDATION.GET_ALL_USER_BY_SIMILAR_USER_NAME;
import static com.phoenix.amazon.AmazonBackend.helpers.AllConstantHelpers.USER_VALIDATION.GET_USER_INFO_BY_USERID_USER_NAME;
import static com.phoenix.amazon.AmazonBackend.helpers.AllConstantHelpers.USER_VALIDATION.GET_USER_INFO_BY_EMAIL_USER_NAME;


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
                                                    final String methodName, UserLoadType loadType) {
        Optional<Users> users = Optional.empty();
        switch (loadType) {
            case LU1 -> {
                users= userRepository.findByUserIdOrUserName(userId, userName);
                userValidationService.validateUser(users, methodName, GET_USER_INFO_BY_USERID_USER_NAME);
            }
            case LU2 -> {
                users = userRepository.findByEmailOrUserName(email, userName);
                userValidationService.validateUser(users, methodName, GET_USER_INFO_BY_EMAIL_USER_NAME);
            }
        }
        return users.get();
    }


    /**
     * @param userName   - userName of user
     * @param email      - email of user
     * @param methodName - origin of requesting method
     * @return Users
     **/
    protected Users loadUserByEmailOrUserName(final String userName, final String email, final String methodName) {
        userValidationService.validateFields(null, userName, email, methodName, VALIDATE_USER_NAME_OR_EMAIL);
        return loadUserByUserNameOrEmailOrUserId(null, userName, email, methodName, UserLoadType.LU2);
    }

    /**
     * @param userName   - userName of user
     * @param methodName - origin of requesting method
     * @return Users
     **/
    protected Users loadUserByUserIdOrUserName(final String userId, final String userName, final String methodName) {
        userValidationService.validateFields(userId, userName, null, methodName, VALIDATE_USER_ID_OR_USER_NAME);
        return loadUserByUserNameOrEmailOrUserId(userId, userName, null, methodName, UserLoadType.LU1);
    }

    /**
     * @param userNameLike - keyword to search for all similar user name
     * @param methodName   - origin of requesting method
     * @return Set<Users> - set of all found users
     **/
    protected Set<Users> loadAllUserByUserNameMatched(final String userNameLike, final String methodName) {
        Set<Users> allUsersWithNearlyUserName = userRepository.findAllByUserNameContaining(userNameLike).get();
        userValidationService.validateUserList(allUsersWithNearlyUserName, methodName, GET_ALL_USER_BY_SIMILAR_USER_NAME);
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
                        .email(oldUser.getEmail())
                        .gender(oldUser.getGender())
                        .password(oldUser.getPassword())
                        .about(oldUser.getAbout())
                        .imageName(oldUser.getProfileImage())
                        .lastSeen(oldUser.getLastSeen())
                        .build();
            }
            case LAST_NAME -> {
                return new Users.builder()
                        .lastName(newUser.getLastName())
                        .firstName(oldUser.getFirstName())
                        .userId(oldUser.getUserId())
                        .userName(oldUser.getUserName())
                        .email(oldUser.getEmail())
                        .gender(oldUser.getGender())
                        .password(oldUser.getPassword())
                        .about(oldUser.getAbout())
                        .imageName(oldUser.getProfileImage())
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
                        .email(oldUser.getEmail())
                        .gender(oldUser.getGender())
                        .password(oldUser.getPassword())
                        .imageName(oldUser.getProfileImage())
                        .lastSeen(oldUser.getLastSeen())
                        .build();

            }
            case EMAIL -> {
                return new Users.builder()
                        .lastName(newUser.getLastName())
                        .firstName(oldUser.getFirstName())
                        .userId(oldUser.getUserId())
                        .userName(oldUser.getUserName())
                        .email(oldUser.getEmail())
                        .gender(oldUser.getGender())
                        .password(oldUser.getPassword())
                        .about(oldUser.getAbout())
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
                        .email(oldUser.getEmail())
                        .password(oldUser.getPassword())
                        .about(oldUser.getAbout())
                        .lastSeen(oldUser.getLastSeen())
                        .build();
            }
        }
        return oldUser;
    }
}
