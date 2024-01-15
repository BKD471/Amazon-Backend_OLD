package com.phoenix.amazon.AmazonBackend.services.impls;

import com.phoenix.amazon.AmazonBackend.dto.UserDto;
import com.phoenix.amazon.AmazonBackend.entity.Users;
import com.phoenix.amazon.AmazonBackend.helpers.UserFields;
import com.phoenix.amazon.AmazonBackend.repository.IUserRepository;
import com.phoenix.amazon.AmazonBackend.services.IUserService;

import java.util.List;

public class UserServiceImpl implements IUserService {
    private final IUserRepository userRepository;

    public UserServiceImpl(IUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * @param user
     * @return
     */
    @Override
    public UserDto createUser(UserDto user) {
        return null;
    }

    /**
     * @param user
     * @param userId
     * @return
     */
    @Override
    public UserDto updateUserByUserId(UserDto user, String userId) {
        return null;
    }

    /**
     * @param userId
     */
    @Override
    public void deleteUserByUserId(String userId) {

    }

    /**
     * @return
     */
    @Override
    public List<UserDto> getALlUsers() {
        return null;
    }

    /**
     * @param userIdOrEmail
     * @return
     */
    @Override
    public UserDto getUserInformationByUserIdOrEmail(String userIdOrEmail) {
        return null;
    }

    /**
     * @param field
     * @param value
     * @return
     */
    @Override
    public List<UserDto> searchUserByFieldAndValue(UserFields field, String value) {
        return null;
    }
}
