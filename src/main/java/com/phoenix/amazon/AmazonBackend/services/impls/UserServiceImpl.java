package com.phoenix.amazon.AmazonBackend.services.impls;

import com.phoenix.amazon.AmazonBackend.dto.UserDto;
import com.phoenix.amazon.AmazonBackend.entity.Users;
import com.phoenix.amazon.AmazonBackend.exceptions.UserExceptions;
import com.phoenix.amazon.AmazonBackend.helpers.AllConstantHelpers;
import com.phoenix.amazon.AmazonBackend.helpers.MappingHelpers;
import com.phoenix.amazon.AmazonBackend.repository.IUserRepository;
import com.phoenix.amazon.AmazonBackend.services.IUserService;
import com.phoenix.amazon.AmazonBackend.services.validationservice.IUserValidationService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

import static com.phoenix.amazon.AmazonBackend.helpers.AllConstantHelpers.USER_FIELDS;
import static com.phoenix.amazon.AmazonBackend.helpers.AllConstantHelpers.GENDER;

import static com.phoenix.amazon.AmazonBackend.helpers.AllConstantHelpers.USER_VALIDATION.*;
import static com.phoenix.amazon.AmazonBackend.helpers.MappingHelpers.UserDtoToUsers;
import static com.phoenix.amazon.AmazonBackend.helpers.MappingHelpers.UsersToUsersDto;

@Service("UserServiceMain")
public class UserServiceImpl implements IUserService {
    private final IUserRepository userRepository;
    private final IUserValidationService userValidationService;

    public UserServiceImpl(IUserRepository userRepository, IUserValidationService userValidationService) {
        this.userRepository = userRepository;
        this.userValidationService = userValidationService;
    }

    private UserDto initializeUserId(final UserDto userDto) {
        final String userIdUUID = UUID.randomUUID().toString();
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
        final String methodName = "createUser(UserDto) in UserServiceImpl";

        UserDto userDtoWithId = initializeUserId(userDto);
        Users user = UserDtoToUsers(userDtoWithId);
        userValidationService.validateUser(user, methodName, CREATE_USER);
        Users savedUser = userRepository.save(user);
        return UsersToUsersDto(savedUser);
    }

    /**no setter in entity class to stop partial initialization
     *
     * so we need constructUser
     *
     * */
    private Users constructUser(Users oldUser, Users newUser, USER_FIELDS fields) {
        switch (fields) {
            case FIRST_NAME -> {
                return new Users.builder()
                        .firstName(newUser.getFirstName())
                        .userId(oldUser.getUserId())
                        .userName(oldUser.getUserName())
                        .lastName(oldUser.getLastName())
                        .email(oldUser.getEmail())
                        .gender(oldUser.getGender())
                        .password(oldUser.getPassword())
                        .about(oldUser.getAbout())
                        .imageName(oldUser.getImageName())
                        .lastSeen(oldUser.getLastSeen())
                        .build();
            }
            case LAST_NAME -> {
                return new Users.builder()
                        .lastName(newUser.getLastName())
                        .firstName(oldUser.getFirstName())
                        .userId(oldUser.getUserId())
                        .userName(oldUser.getUserName())
                        .email(oldUser.getEmail())
                        .gender(oldUser.getGender())
                        .password(oldUser.getPassword())
                        .about(oldUser.getAbout())
                        .imageName(oldUser.getImageName())
                        .lastSeen(oldUser.getLastSeen())
                        .build();

            }
            case ABOUT -> {
                return new Users.builder()
                        .about(newUser.getAbout())
                        .lastName(oldUser.getLastName())
                        .firstName(oldUser.getFirstName())
                        .userId(oldUser.getUserId())
                        .userName(oldUser.getUserName())
                        .email(oldUser.getEmail())
                        .gender(oldUser.getGender())
                        .password(oldUser.getPassword())
                        .imageName(oldUser.getImageName())
                        .lastSeen(oldUser.getLastSeen())
                        .build();

            }
            case EMAIL -> {
                return new Users.builder()
                        .lastName(newUser.getLastName())
                        .firstName(oldUser.getFirstName())
                        .userId(oldUser.getUserId())
                        .userName(oldUser.getUserName())
                        .email(oldUser.getEmail())
                        .gender(oldUser.getGender())
                        .password(oldUser.getPassword())
                        .about(oldUser.getAbout())
                        .lastSeen(oldUser.getLastSeen())
                        .build();
            }
            case GENDER -> {
                return new Users.builder()
                        .gender(newUser.getGender())
                        .lastName(oldUser.getLastName())
                        .firstName(oldUser.getFirstName())
                        .userId(oldUser.getUserId())
                        .userName(oldUser.getUserName())
                        .email(oldUser.getEmail())
                        .password(oldUser.getPassword())
                        .about(oldUser.getAbout())
                        .lastSeen(oldUser.getLastSeen())
                        .build();
            }
        }
        return oldUser;
    }

    /**
     * @param user
     * @param userIdOrUserName
     * @return
     */
    @Override
    public UserDto updateUserByUserIdOrUserName(final UserDto user, final String userIdOrUserName) {
        final String methodName = "updateUserByUserIdOrUserName(UserDto,String) in UserServiceImpl";

        Users userDetails = UserDtoToUsers(user);
        Users fetchedUser = userRepository.findByUserIdOrUserName(userIdOrUserName).get();
        userValidationService.validateUser(fetchedUser, methodName, UPDATE_USER_BY_USER_ID_OR_USER_NAME);


        Predicate<String> isBlankField = StringUtils::isBlank;
        BiPredicate<String, String> checkFieldEquality = String::equalsIgnoreCase;

        Predicate<GENDER> isBlankFieldEnum = Objects::isNull;
        BiPredicate<GENDER, GENDER> checkEqualEnumValues = Objects::equals;
        if (isBlankField.test(userDetails.getFirstName()) &&
                checkFieldEquality.test(userDetails.getFirstName(), fetchedUser.getFirstName())) {
            fetchedUser = constructUser(fetchedUser, userDetails, USER_FIELDS.FIRST_NAME);
        }
        if (isBlankField.test(userDetails.getLastName()) &&
                checkFieldEquality.test(userDetails.getLastName(), fetchedUser.getLastName())) {
            fetchedUser = constructUser(fetchedUser, userDetails, USER_FIELDS.LAST_NAME);
        }
        if (isBlankField.test(userDetails.getAbout()) &&
                checkFieldEquality.test(userDetails.getAbout(), fetchedUser.getAbout())) {
            fetchedUser = constructUser(fetchedUser, userDetails, USER_FIELDS.ABOUT);
        }
        if (isBlankField.test(userDetails.getEmail()) &&
                checkFieldEquality.test(userDetails.getEmail(), fetchedUser.getEmail())) {
            fetchedUser = constructUser(fetchedUser, userDetails, USER_FIELDS.EMAIL);
        }
        if (isBlankFieldEnum.test(userDetails.getGender()) &&
                checkEqualEnumValues.test(userDetails.getGender(), fetchedUser.getGender())) {
            fetchedUser = constructUser(fetchedUser, userDetails, USER_FIELDS.GENDER);
        }
        Users savedUser = userRepository.save(fetchedUser);
        return UsersToUsersDto(savedUser);
    }

    /**
     * @param userIdOrUserName
     */
    @Override
    public void deleteUserByUserIdOrUserName(final String userIdOrUserName) {
        final String methodName="deleteUserByUserIdOrUserName(string) in UserServiceImpl";
        Users fetchedUser=userRepository.findByUserIdOrUserName(userIdOrUserName).get();
        userValidationService.validateUser(fetchedUser,methodName,DELETE_USER_BY_USER_ID_OR_USER_NAME);
        userRepository.deleteByUserIdOrUserName(userIdOrUserName);
    }

    /**
     * @return
     */
    @Override
    public List<UserDto> getALlUsers() {
        final String methodName="getALlUsers() in UserServiceImpl";
        List<Users> usersList=userRepository.findAll();
        userValidationService.validateUserList(usersList,methodName,GET_ALL_USERS);
        return usersList.stream().map(MappingHelpers::UsersToUsersDto).toList();
    }

    /**
     * @param emailOrUserName
     * @return
     */
    @Override
    public UserDto getUserInformationByEmailOrUserName(final String emailOrUserName) throws UserExceptions {
        final String methodName = "getUserInformationByEmailOrUserName(String) in UserServiceImpl";

        Users users = userRepository.findByEmailOrUserName(emailOrUserName).get();
        userValidationService.validateUser(users, methodName, GET_USER_INFO_BY_EMAIL_USER_NAME);
        return UsersToUsersDto(users);
    }

    /**
     * @param field
     * @param value
     * @return
     */
    @Override
    public List<UserDto> searchUserByFieldAndValue(final USER_FIELDS field, final String value) {

        return null;
    }

    /**
     * @param userNameWord
     * @return
     */
    @Override
    public List<UserDto> searchAllUsersByUserName(final String userNameWord) {
        final String methodName="searchAllUsersByUserName(string) in UsersServiceImpl";
        List<Users> usersList=userRepository.findAllByUserNameContaining(userNameWord).get();
        userValidationService.validateUserList(usersList,methodName,SEARCH_ALL_USERS_BY_USER_NAME);
        return usersList.stream().map(MappingHelpers::UsersToUsersDto).toList();
    }
}
