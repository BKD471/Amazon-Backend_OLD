package com.phoenix.amazon.AmazonBackend.services.impls;

import com.phoenix.amazon.AmazonBackend.dto.UserDto;
import com.phoenix.amazon.AmazonBackend.entity.Users;
import com.phoenix.amazon.AmazonBackend.repository.IUserRepository;
import com.phoenix.amazon.AmazonBackend.services.IUserService;
import com.phoenix.amazon.AmazonBackend.services.validationservice.IUserValidationService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.phoenix.amazon.AmazonBackend.helpers.AllConstantHelpers.USER_FIELDS;
import static com.phoenix.amazon.AmazonBackend.helpers.MappingHelpers.UserDtoToUsers;
import static com.phoenix.amazon.AmazonBackend.helpers.MappingHelpers.UsersToUsersDto;
import static com.phoenix.amazon.AmazonBackend.helpers.AllConstantHelpers.USER_VALIDATION.GET_USER_INFO_BY_EMAIL_USER_NAME;

@Service("UserServiceMain")
public class UserServiceImpl implements IUserService {
    private final IUserRepository userRepository;
    private final IUserValidationService userValidationService;

    public UserServiceImpl(IUserRepository userRepository,IUserValidationService userValidationService) {
        this.userRepository = userRepository;
        this.userValidationService=userValidationService;
    }

    private UserDto initializeUserId(final UserDto userDto){
        final String userIdUUID=UUID.randomUUID().toString();
        return new UserDto.builder()
                .userId(userIdUUID)
                .userName(userDto.userName())
                .firstName(userDto.firstName())
                .lastName(userDto.lastName())
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
     * @param userIdOrUserName
     * @return
     */
    @Override
    public UserDto updateUserByUserIdOrUserName(final UserDto user,final String userIdOrUserName) {
        return null;
    }

    /**
     * @param userIdOrUserName
     */
    @Override
    public void deleteUserByUserIdOrUserName(final String userIdOrUserName) {

    }

    /**
     * @return
     */
    @Override
    public List<UserDto> getALlUsers() {
        return null;
    }

    /**
     * @param emailOrUserName
     * @return
     */
    @Override
    public UserDto getUserInformationByEmailOrUserName(final String emailOrUserName) {
        Optional<Users> users=userRepository.findByEmailOrUserName(emailOrUserName);
        userValidationService.validateUser(users,GET_USER_INFO_BY_EMAIL_USER_NAME);
        return UsersToUsersDto(users.get());
    }

    /**
     * @param field
     * @param value
     * @return
     */
    @Override
    public List<UserDto> searchUserByFieldAndValue(final USER_FIELDS field,final String value) {
        return null;
    }

    /**
     * @param userNameWord
     * @return
     */
    @Override
    public List<UserDto> searchAllUsersByUserName(final String userNameWord) {
        return null;
    }
}
