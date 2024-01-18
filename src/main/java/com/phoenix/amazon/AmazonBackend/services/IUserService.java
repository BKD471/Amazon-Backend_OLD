package com.phoenix.amazon.AmazonBackend.services;

import com.phoenix.amazon.AmazonBackend.dto.UserDto;
import static  com.phoenix.amazon.AmazonBackend.helpers.AllConstantHelpers.USER_FIELDS;

import java.util.List;

public interface IUserService {
    UserDto createUser(final UserDto user);
    UserDto updateUserByUserIdOrUserName(final UserDto user,final String userIdOrUserName);
    void deleteUserByUserIdOrUserName(final String userIdOrUserName);
    List<UserDto> getALlUsers();
    UserDto getUserInformationByEmailOrUserName(final String emailOrUserName);
    List<UserDto> searchUserByFieldAndValue(final USER_FIELDS field,final String value);
    List<UserDto> searchAllUsersByUserName(final String userNameWord);
}
