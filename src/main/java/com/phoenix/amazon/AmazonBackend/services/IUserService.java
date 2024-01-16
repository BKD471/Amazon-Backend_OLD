package com.phoenix.amazon.AmazonBackend.services;

import com.phoenix.amazon.AmazonBackend.dto.UserDto;
import static  com.phoenix.amazon.AmazonBackend.helpers.AllConstantHelpers.UserFields;

import java.util.List;

public interface IUserService {
    UserDto createUser(final UserDto user);
    UserDto updateUserByUserId(UserDto user,String userId);
    void deleteUserByUserId(String userId);
    List<UserDto> getALlUsers();
    UserDto getUserInformationByUserIdOrEmail(String userIdOrEmail);
    List<UserDto> searchUserByFieldAndValue(UserFields field, String value);
}
