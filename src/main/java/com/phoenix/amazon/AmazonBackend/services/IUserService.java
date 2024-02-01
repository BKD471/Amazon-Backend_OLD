package com.phoenix.amazon.AmazonBackend.services;

import com.phoenix.amazon.AmazonBackend.dto.UserDto;
import com.phoenix.amazon.AmazonBackend.exceptions.BadApiRequestExceptions;
import com.phoenix.amazon.AmazonBackend.exceptions.UserExceptions;
import com.phoenix.amazon.AmazonBackend.exceptions.UserNotFoundExceptions;

import java.util.Set;

import static com.phoenix.amazon.AmazonBackend.helpers.AllConstantHelpers.USER_FIELDS;

public interface IUserService {
    /**
     * @param user - user object
     * @return UserDto
     **/
    UserDto createUser(final UserDto user) throws UserExceptions, UserNotFoundExceptions, BadApiRequestExceptions;

    /**
     * @param user     - user object
     * @param userId   - id of user
     * @param userName - username of user
     * @return UserDto
     **/
    UserDto updateUserByUserIdOrUserName(final UserDto user, final String userId, final String userName) throws UserNotFoundExceptions, UserExceptions, BadApiRequestExceptions;

    /**
     * @param userId   - id of user
     * @param userName - username of user
     **/
    void deleteUserByUserIdOrUserName(final String userId, final String userName) throws UserExceptions, UserNotFoundExceptions, BadApiRequestExceptions;

    /**
     * @return Set<userDto>
     **/
    Set<UserDto> getALlUsers(final int pageNumber, final int pageSize) throws UserNotFoundExceptions;

    /**
     * @param email    - email of user
     * @param userName - username of user
     * @return UserDto
     **/
    UserDto getUserInformationByEmailOrUserName(final String email, final String userName) throws UserExceptions, UserNotFoundExceptions, BadApiRequestExceptions;

    /**
     * @param field      - field of user entity
     * @param value      - value to query the field
     * @param pageNumber - index value of current page
     * @param pageSize   - size of current page
     * @return Set<UserDto>
     **/
    Set<UserDto> searchUserByFieldAndValue(final USER_FIELDS field, final String value, final int pageNumber, final int pageSize) throws UserNotFoundExceptions;

    /**
     * @param userNameWord - username of user
     * @param pageNumber   - index value of page
     * @param pageSize     - size of page
     * @return Set<UserDto>
     */
    Set<UserDto> searchAllUsersByUserName(final String userNameWord, final int pageNumber, final int pageSize) throws UserNotFoundExceptions;
}
