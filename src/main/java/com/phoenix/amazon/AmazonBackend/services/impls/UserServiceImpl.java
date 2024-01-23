package com.phoenix.amazon.AmazonBackend.services.impls;

import com.phoenix.amazon.AmazonBackend.dto.UserDto;
import com.phoenix.amazon.AmazonBackend.entity.Users;
import com.phoenix.amazon.AmazonBackend.exceptions.BadApiRequestExceptions;
import com.phoenix.amazon.AmazonBackend.exceptions.UserExceptions;
import com.phoenix.amazon.AmazonBackend.exceptions.UserNotFoundExceptions;
import com.phoenix.amazon.AmazonBackend.helpers.AllConstantHelpers;
import com.phoenix.amazon.AmazonBackend.helpers.MappingHelpers;
import com.phoenix.amazon.AmazonBackend.repository.IUserRepository;
import com.phoenix.amazon.AmazonBackend.services.AbstractService;
import com.phoenix.amazon.AmazonBackend.services.IUserService;
import com.phoenix.amazon.AmazonBackend.services.validationservice.IUserValidationService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.BiPredicate;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static com.phoenix.amazon.AmazonBackend.helpers.AllConstantHelpers.USER_FIELDS;
import static com.phoenix.amazon.AmazonBackend.helpers.AllConstantHelpers.GENDER;

import static com.phoenix.amazon.AmazonBackend.helpers.AllConstantHelpers.USER_VALIDATION.*;
import static com.phoenix.amazon.AmazonBackend.helpers.MappingHelpers.UserDtoToUsers;
import static com.phoenix.amazon.AmazonBackend.helpers.MappingHelpers.UsersToUsersDto;

@Service("UserServiceMain")
public class UserServiceImpl extends AbstractService implements IUserService {
    private final IUserRepository userRepository;
    private final IUserValidationService userValidationService;

    public UserServiceImpl(IUserRepository userRepository, IUserValidationService userValidationService) {
        super(userRepository, userValidationService);
        this.userRepository = userRepository;
        this.userValidationService = userValidationService;
    }

    private UserDto initializeUserId(final UserDto userDto) {
        final String methodName="initializeUserId";
        if(Objects.isNull(userDto)) userValidationService.validateUser(Optional.empty(),
                "initializeUserId in UserService",NULL_OBJECT);

        final String userIdUUID = UUID.randomUUID().toString();
        return new UserDto.builder()
                .userId(userIdUUID)
                .userName(userDto.userName())
                .firstName(userDto.firstName())
                .lastName(userDto.lastName())
                .email(userDto.email())
                .gender(userDto.gender())
                .profileImage(userDto.profileImage())
                .password(userDto.password())
                .about(userDto.about())
                .build();
    }

    /**
     * @param userDto - User Object
     * @return UserDTo
     */
    @Override
    public UserDto createUser(final UserDto userDto) throws UserExceptions, UserNotFoundExceptions, BadApiRequestExceptions {
        final String methodName = "createUser(UserDto) in UserServiceImpl";

        UserDto userDtoWithId = initializeUserId(userDto);
        Users user = UserDtoToUsers(userDtoWithId);
        userValidationService.validateUser(Optional.of(user), methodName, CREATE_USER);
        Users savedUser = userRepository.save(user);
        return UsersToUsersDto(savedUser);
    }

    /**
     * @param user     - Incoming User Object from client
     * @param userId   - User Id
     * @param userName - userName of user
     * @return UserDto
     */
    @Override
    public UserDto updateUserByUserIdOrUserName(final UserDto user, final String userId, final String userName) {
        final String methodName = "updateUserByUserIdOrUserName(UserDto,String) in UserServiceImpl";

        Users userDetails = UserDtoToUsers(user);
        Users fetchedUser = loadUserByUserIdOrUserName(userId, userName, methodName);

        Predicate<String> isNotBlankField = StringUtils::isNotBlank;
        BiPredicate<String, String> checkFieldEquality = String::equalsIgnoreCase;

        Predicate<GENDER> isNotBlankFieldEnum = Objects::nonNull;
        BiPredicate<GENDER, GENDER> checkEqualEnumValues = Objects::equals;
        if (isNotBlankField.test(userDetails.getFirstName()) &&
                !checkFieldEquality.test(userDetails.getFirstName(), fetchedUser.getFirstName())) {
            fetchedUser = constructUser(fetchedUser, userDetails, USER_FIELDS.FIRST_NAME);
        }
        if (isNotBlankField.test(userDetails.getLastName()) &&
                !checkFieldEquality.test(userDetails.getLastName(), fetchedUser.getLastName())) {
            fetchedUser = constructUser(fetchedUser, userDetails, USER_FIELDS.LAST_NAME);
        }
        if (isNotBlankField.test(userDetails.getAbout()) &&
                !checkFieldEquality.test(userDetails.getAbout(), fetchedUser.getAbout())) {
            fetchedUser = constructUser(fetchedUser, userDetails, USER_FIELDS.ABOUT);
        }
        if (isNotBlankField.test(userDetails.getEmail()) &&
                !checkFieldEquality.test(userDetails.getEmail(), fetchedUser.getEmail())) {
            userValidationService.validateUser(Optional.of(userDetails), methodName, UPDATE_USER_BY_USER_ID_OR_USER_NAME);
            fetchedUser = constructUser(fetchedUser, userDetails, USER_FIELDS.EMAIL);
        }
        if (isNotBlankFieldEnum.test(userDetails.getGender()) &&
                !checkEqualEnumValues.test(userDetails.getGender(), fetchedUser.getGender())) {
            fetchedUser = constructUser(fetchedUser, userDetails, USER_FIELDS.GENDER);
        }
        Users savedUser = userRepository.save(fetchedUser);
        return UsersToUsersDto(savedUser);
    }

    /**
     * @param userId   - User Id
     * @param userName - userName of user
     */
    @Override
    public void deleteUserByUserIdOrUserName(final String userId, final String userName) {
        final String methodName = "deleteUserByUserIdOrUserName(string) in UserServiceImpl";

        Users fetchedUser = loadUserByUserIdOrUserName(userId, userName, methodName);
        userValidationService.validateUser(Optional.of(fetchedUser), methodName, DELETE_USER_BY_USER_ID_OR_USER_NAME);
        userRepository.deleteByUserIdOrUserName(userId, userName);
    }

    /**
     * @return Set<UserDto> - List Of all Users
     */
    @Override
    public Set<UserDto> getALlUsers() {
        final String methodName = "getALlUsers() in UserServiceImpl";

        Set<Users> usersSet = new HashSet<>(userRepository.findAll());
        userValidationService.validateUserList(usersSet, methodName, GET_ALL_USERS);
        return usersSet.stream().map(MappingHelpers::UsersToUsersDto).collect(Collectors.toSet());
    }

    /**
     * @param email    - email of User
     * @param userName - userName of user
     * @return UserDTo
     */
    @Override
    public UserDto getUserInformationByEmailOrUserName(final String email, final String userName) throws UserExceptions {
        final String methodName = "getUserInformationByEmailOrUserName(String) in UserServiceImpl";

        Users users = loadUserByEmailOrUserName(email, userName, methodName);
        userValidationService.validateUser(Optional.of(users), methodName, GET_USER_INFO_BY_EMAIL_USER_NAME);
        return UsersToUsersDto(users);
    }

    /**
     * @param field - field of User Entity
     * @param value - value of field
     * @return List<UserDTo>
     */
    @Override
    public Set<UserDto> searchUserByFieldAndValue(final USER_FIELDS field, final String value) {
        final String methodName = "searchUserByFieldAndValue(field,String) in UserServiceImpl";
        Set<Users> users = null;
        switch (field) {
            case EMAIL -> {
                users = userRepository.searchUserByEmail(value).get();
                userValidationService.validateUserList(users, methodName, SEARCH_USER_BY_EMAIL);
            }
            case USER_NAME -> {
                users = userRepository.searchUserByUserName(value).get();
                userValidationService.validateUserList(users, methodName, SEARCH_USER_BY_USER_NAME);
            }
            case GENDER -> {
                users = userRepository.searchUserByGender(value).get();
                userValidationService.validateUserList(users, methodName, SEARCH_ALL_USERS_BY_GENDER);
            }
            case FIRST_NAME -> {
                users = userRepository.searchUserByFirstName(value).get();
                userValidationService.validateUserList(users, methodName, SEARCH_ALL_USERS_BY_FIRST_NAME);
            }
            case LAST_NAME -> {
                users = userRepository.searchUserByLastName(value).get();
                userValidationService.validateUserList(users, methodName, SEARCH_ALL_USERS_BY_LAST_NAME);
            }
        }
        return users.stream().map(MappingHelpers::UsersToUsersDto).collect(Collectors.toSet());
    }

    /**
     * @param userNameWord - Keyword to get multiple users with almost same name initials
     * @return Set<UserDto>
     */
    @Override
    public Set<UserDto> searchAllUsersByUserName(final String userNameWord) {
        final String methodName = "searchAllUsersByUserName(string) in UsersServiceImpl";

        Set<Users> usersSet = loadAllUserByUserNameMatched(userNameWord, methodName);
        userValidationService.validateUserList(usersSet, methodName, SEARCH_ALL_USERS_BY_USER_NAME);
        return usersSet.stream().map(MappingHelpers::UsersToUsersDto).collect(Collectors.toSet());
    }
}