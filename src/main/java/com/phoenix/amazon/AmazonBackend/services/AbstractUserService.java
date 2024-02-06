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
import static com.phoenix.amazon.AmazonBackend.helpers.AllConstantHelpers.USER_FIELD_VALIDATION.VALIDATE_USER_ID_OR_USER_NAME_OR_PRIMARY_EMAIL;
import static com.phoenix.amazon.AmazonBackend.helpers.AllConstantHelpers.USER_VALIDATION.GET_USER_INFO_BY_USERID_USER_NAME_PRIMARY_EMAIL;

public abstract class AbstractUserService {
    private final IUserRepository userRepository;
    private final IUserValidationService userValidationService;

    protected AbstractUserService(final IUserRepository userRepository, final IUserValidationService userValidationService) {
        this.userRepository = userRepository;
        this.userValidationService = userValidationService;
    }


    /**
     * @param userId       - user id of user
     * @param userName     - userName of user
     * @param primaryEmail - primary email of user
     * @param methodName   - origin of requesting method
     * @return Users
     * @throws UserNotFoundExceptions,UserExceptions,BadApiRequestExceptions,IOException - list of exceptions being thrown
     **/
    protected Users loadUserByUserIdOrUserNameOrPrimaryEmail(final String userId, final String userName,final String primaryEmail, final String methodName) throws UserNotFoundExceptions, UserExceptions, BadApiRequestExceptions, IOException {
        userValidationService.validatePZeroUserFields(userId, userName, primaryEmail, methodName, VALIDATE_USER_ID_OR_USER_NAME_OR_PRIMARY_EMAIL);
        Optional<Users> users = userRepository.findByUserIdOrUserNameOrPrimaryEmail(userId, userName,primaryEmail);
        userValidationService.validateUser(Optional.empty(), users, methodName, GET_USER_INFO_BY_USERID_USER_NAME_PRIMARY_EMAIL);
        return users.get();
    }

    /**
     * @param sortBy - column to sort
     * */
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
