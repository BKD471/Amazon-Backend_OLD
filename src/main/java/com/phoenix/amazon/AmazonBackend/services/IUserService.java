package com.phoenix.amazon.AmazonBackend.services;

import com.phoenix.amazon.AmazonBackend.dto.UserDto;
import com.phoenix.amazon.AmazonBackend.exceptions.UserExceptions;
import java.util.Set;

import static com.phoenix.amazon.AmazonBackend.helpers.AllConstantHelpers.USER_FIELDS;

public interface IUserService {
    /**
     * @param user - user object
     * @return UserDto
     **/
    UserDto createUser(final UserDto user);

    /**
     * @param user     - user object
     * @param userId   - id of user
     * @param userName - username of user
     * @return UserDto
     **/
    UserDto updateUserByUserIdOrUserName(final UserDto user, final String userId, final String userName);

    /**
     * @param userId   - id of user
     * @param userName - username of user
     **/
    void deleteUserByUserIdOrUserName(final String userId, final String userName);

    /**
     * @return Set<userDto>
     **/
    Set<UserDto> getALlUsers();

    /**
     * @param email    - email of user
     * @param userName - username of user
     * @return UserDto
     **/
    UserDto getUserInformationByEmailOrUserName(final String email, final String userName) throws UserExceptions;

    /**
     * @param field - field of user entity
     * @param value - value to query the field
     * @return Set<UserDto>
     **/
    Set<UserDto> searchUserByFieldAndValue(final USER_FIELDS field, final String value);

    /**
     * @param userNameWord - username of user
     * @return Set<UserDto>
     */
    Set<UserDto> searchAllUsersByUserName(final String userNameWord);
}
