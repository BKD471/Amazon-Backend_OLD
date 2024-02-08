package com.phoenix.amazon.AmazonBackend.services;

import com.phoenix.amazon.AmazonBackend.dto.PageableResponse;
import com.phoenix.amazon.AmazonBackend.dto.UserDto;
import com.phoenix.amazon.AmazonBackend.exceptions.BadApiRequestExceptions;
import com.phoenix.amazon.AmazonBackend.exceptions.UserExceptions;
import com.phoenix.amazon.AmazonBackend.exceptions.UserNotFoundExceptions;
import com.phoenix.amazon.AmazonBackend.helpers.AllConstantHelpers.USER_FIELDS;

import java.io.IOException;

public interface IUserService {
    /**
     * @param userDto - userDto object
     * @return UserDto - userDto Object
     * @throws UserNotFoundExceptions,UserExceptions,BadApiRequestExceptions,IOException -list of exceptions being thrown
     **/
    UserDto createUserService(final UserDto userDto) throws UserExceptions, UserNotFoundExceptions, BadApiRequestExceptions, IOException;

    /**
     * @param user     - user object
     * @param userId   - id of user
     * @param userName - username of user
     * @return UserDto - userDto Object
     * @throws UserNotFoundExceptions,UserExceptions,BadApiRequestExceptions,IOException -list of exceptions being thrown
     **/
    UserDto updateUserServiceByUserIdOrUserNameOrPrimaryEmail(final UserDto user, final String userId, final String userName, final String primaryEmail) throws UserNotFoundExceptions, UserExceptions, BadApiRequestExceptions, IOException;

    /**
     * @param userId   - id of user
     * @param userName - username of user
     * @throws UserNotFoundExceptions,UserExceptions,BadApiRequestExceptions,IOException -list of exceptions being thrown
     **/
    void deleteUserServiceByUserIdOrUserNameOrPrimaryEmail(final String userId, final String userName, final String primaryEmail) throws UserExceptions, UserNotFoundExceptions, BadApiRequestExceptions, IOException;

    /**
     * @param pageNumber - index value of page
     * @param pageSize   - size of page
     * @param sortBy     - sort column
     * @param sortDir    - direction of sorting
     * @return PageableResponse<userDto> - page of userDto
     * @throws UserNotFoundExceptions - list of exceptions being thrown
     **/
    PageableResponse<UserDto> getAllUsers(final int pageNumber, final int pageSize, final String sortBy, final String sortDir) throws UserNotFoundExceptions;

    /**
     * @param email    - email of user
     * @param userName - username of user
     * @return UserDto - userDto Object
     * @throws UserNotFoundExceptions,UserExceptions,BadApiRequestExceptions,IOException -list of exceptions being thrown
     **/
    UserDto getUserServiceInformationByUserIdOrUserNameOrPrimaryEmail(final String userId, final String userName, final String primaryEmail) throws UserExceptions, UserNotFoundExceptions, BadApiRequestExceptions, IOException;

    /**
     * @param field      - field of user entity
     * @param value      - value to query the field
     * @param pageNumber - index value of page
     * @param pageSize   - size of page
     * @param sortBy     - sort column
     * @param sortDir    - direction of sorting
     * @return PageableResponse<UserDto> - page of userDto
     * @throws UserNotFoundExceptions - list of exceptions being thrown
     **/
    PageableResponse<UserDto> searchUserByFieldAndValue(final USER_FIELDS field, final String value, final int pageNumber, final int pageSize, final USER_FIELDS sortBy, final String sortDir) throws UserNotFoundExceptions;

    /**
     * @param userNameWord - username of user
     * @param pageNumber   - index value of page
     * @param pageSize     - size of page
     * @param sortBy       - sort column
     * @param sortDir      - direction of sorting
     * @return PageableResponse<UserDto> - page of userDto
     * @throws UserNotFoundExceptions - list of exceptions being thrown
     */
    PageableResponse<UserDto> searchAllUsersByUserName(final String userNameWord, final int pageNumber, final int pageSize, final String sortBy, final String sortDir) throws UserNotFoundExceptions;

    /**
     * @return String
     * **/
    String generatePasswords();
}
