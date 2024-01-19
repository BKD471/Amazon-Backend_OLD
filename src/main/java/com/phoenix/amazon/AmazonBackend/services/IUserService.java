package com.phoenix.amazon.AmazonBackend.services;

import com.phoenix.amazon.AmazonBackend.dto.UserDto;
import com.phoenix.amazon.AmazonBackend.exceptions.UserExceptions;

import static  com.phoenix.amazon.AmazonBackend.helpers.AllConstantHelpers.USER_FIELDS;

import java.util.List;

public interface IUserService {
    UserDto createUser(final UserDto user);
    UserDto updateUserByUserIdOrUserName(final UserDto user,final String userIdOrUserName);
    void deleteUserByUserIdOrUserName(final String userIdOrUserName);
    List<UserDto> getALlUsers();
    UserDto getUserInformationByEmailOrUserName(final String emailOrUserName) throws UserExceptions;
    List<UserDto> searchUserByFieldAndValue(final USER_FIELDS field,final String value);
    List<UserDto> searchAllUsersByUserName(final String userNameWord);
}
