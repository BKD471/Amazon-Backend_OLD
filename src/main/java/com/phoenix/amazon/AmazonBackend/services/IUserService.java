package com.phoenix.amazon.AmazonBackend.services;

import com.phoenix.amazon.AmazonBackend.dto.PageableResponse;
import com.phoenix.amazon.AmazonBackend.dto.UserDto;
import com.phoenix.amazon.AmazonBackend.exceptions.BadApiRequestExceptions;
import com.phoenix.amazon.AmazonBackend.exceptions.UserExceptions;
import com.phoenix.amazon.AmazonBackend.exceptions.UserNotFoundExceptions;
import com.phoenix.amazon.AmazonBackend.helpers.AllConstantHelpers.USER_FIELDS;

public interface IUserService {
    /**
     * @param userDto - userDto object
     * @return UserDto - userDto Object
     **/
    UserDto createUser(final UserDto userDto) throws UserExceptions, UserNotFoundExceptions, BadApiRequestExceptions;

    /**
     * @param user     - user object
     * @param userId   - id of user
     * @param userName - username of user
     * @return UserDto - userDto Object
     **/
    UserDto updateUserByUserIdOrUserName(final UserDto user, final String userId, final String userName) throws UserNotFoundExceptions, UserExceptions, BadApiRequestExceptions;

    /**
     * @param userId   - id of user
     * @param userName - username of user
     **/
    void deleteUserByUserIdOrUserName(final String userId, final String userName) throws UserExceptions, UserNotFoundExceptions, BadApiRequestExceptions;

    /**
     * @param pageNumber                 - index value of page
     * @param pageSize                   - size of page
     * @param sortBy                     - sort column
     * @param sortDir                    - direction of sorting
     * @return PageableResponse<userDto> - set of userDto
     **/
    PageableResponse<UserDto> getAllUsers(final int pageNumber, final int pageSize, final String sortBy, final String sortDir) throws UserNotFoundExceptions;

    /**
     * @param email    - email of user
     * @param userName - username of user
     * @return UserDto - userDto Object
     **/
    UserDto getUserInformationByEmailOrUserName(final String email, final String userName) throws UserExceptions, UserNotFoundExceptions, BadApiRequestExceptions;

    /**
     * @param field                      - field of user entity
     * @param value                      - value to query the field
     * @param pageNumber                 - index value of page
     * @param pageSize                   - size of page
     * @param sortBy                     - sort column
     * @param sortDir                    - direction of sorting
     * @return PageableResponse<UserDto> - set of userDto
     **/
    PageableResponse<UserDto> searchUserByFieldAndValue(final USER_FIELDS field, final String value, final int pageNumber, final int pageSize, final USER_FIELDS sortBy, final String sortDir) throws UserNotFoundExceptions;

    /**
     * @param userNameWord               - username of user
     * @param pageNumber                 - index value of page
     * @param pageSize                   - size of page
     * @param sortBy                     - sort column
     * @param sortDir                    - direction of sorting
     * @return PageableResponse<UserDto> - set of userDto
     */
    PageableResponse<UserDto> searchAllUsersByUserName(final String userNameWord, final int pageNumber, final int pageSize, final String sortBy, final String sortDir) throws UserNotFoundExceptions;
}
