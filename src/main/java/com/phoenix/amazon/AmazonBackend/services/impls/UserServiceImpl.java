package com.phoenix.amazon.AmazonBackend.services.impls;

import com.phoenix.amazon.AmazonBackend.dto.UserDto;
import com.phoenix.amazon.AmazonBackend.entity.Users;
import com.phoenix.amazon.AmazonBackend.helpers.AllConstantHelpers;
import com.phoenix.amazon.AmazonBackend.repository.IUserRepository;
import com.phoenix.amazon.AmazonBackend.services.IUserService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

import static com.phoenix.amazon.AmazonBackend.helpers.AllConstantHelpers.UserFields;
import static com.phoenix.amazon.AmazonBackend.helpers.MappingHelpers.UserDtoToUsers;
import static com.phoenix.amazon.AmazonBackend.helpers.MappingHelpers.UsersToUsersDto;

@Service("UserServiceMain")
public class UserServiceImpl implements IUserService {
    private final IUserRepository userRepository;

    public UserServiceImpl(IUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    private UserDto initializeUserId(final UserDto userDto){
        final String userIdUUID=UUID.randomUUID().toString();
        return new UserDto.builder()
                .userId(userIdUUID)
                .name(userDto.name())
                .email(userDto.email())
                .gender(userDto.gender())
                .imageName(userDto.imageName())
                .password(userDto.password())
                .about(userDto.about())
                .build();
    }
    /**
     * @param userDto
     * @return
     */
    @Override
    public UserDto createUser(final UserDto userDto) {
        UserDto userDtoWithId=initializeUserId(userDto);
        Users user = UserDtoToUsers(userDtoWithId);

        Users savedUser = userRepository.save(user);
        return UsersToUsersDto(savedUser);
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
