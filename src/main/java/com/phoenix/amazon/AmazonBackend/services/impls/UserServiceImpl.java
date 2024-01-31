package com.phoenix.amazon.AmazonBackend.services.impls;

import com.phoenix.amazon.AmazonBackend.dto.UserDto;
import com.phoenix.amazon.AmazonBackend.entity.Users;
import com.phoenix.amazon.AmazonBackend.exceptions.BadApiRequestExceptions;
import com.phoenix.amazon.AmazonBackend.exceptions.UserExceptions;
import com.phoenix.amazon.AmazonBackend.exceptions.UserNotFoundExceptions;
import com.phoenix.amazon.AmazonBackend.helpers.MappingHelpers;
import com.phoenix.amazon.AmazonBackend.repository.IUserRepository;
import com.phoenix.amazon.AmazonBackend.services.AbstractService;
import com.phoenix.amazon.AmazonBackend.services.IUserService;
import com.phoenix.amazon.AmazonBackend.services.validationservice.IUserValidationService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.HashSet;
import java.util.UUID;
import java.util.function.BiPredicate;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static com.phoenix.amazon.AmazonBackend.helpers.AllConstantHelpers.USER_FIELDS;

import static com.phoenix.amazon.AmazonBackend.helpers.AllConstantHelpers.GENDER;

import static com.phoenix.amazon.AmazonBackend.helpers.AllConstantHelpers.USER_FIELDS.*;
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

    private UserDto initializeUserId(final UserDto userDto) throws UserExceptions, UserNotFoundExceptions, BadApiRequestExceptions {
        final String methodName = "initializeUserId";
        if (Objects.isNull(userDto)) userValidationService.validateUser(Optional.empty(),Optional.empty(),
                "initializeUserId in UserService", NULL_OBJECT);

        final String userIdUUID = UUID.randomUUID().toString();
        final String secondaryEmail = StringUtils.isBlank(userDto.secondaryEmail()) ? userDto.secondaryEmail() : userDto.secondaryEmail().trim();
        final String about = StringUtils.isBlank(userDto.about()) ? userDto.about() : userDto.about().trim();
        final String profileImage = StringUtils.isBlank(userDto.profileImage()) ? userDto.profileImage() : userDto.profileImage().trim();
        return new UserDto.builder()
                .userId(userIdUUID)
                .userName(userDto.userName().trim())
                .firstName(userDto.firstName().trim())
                .lastName(userDto.lastName().trim())
                .primaryEmail(userDto.primaryEmail().trim())
                .secondaryEmail(secondaryEmail)
                .gender(userDto.gender())
                .profileImage(profileImage)
                .password(userDto.password().trim())
                .about(about)
                .lastSeen(LocalDateTime.now())
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
        userValidationService.validateUser(Optional.of(user), Optional.empty(), methodName, CREATE_USER);

        //adding the password to set of password
        user = constructUser(user, user, PASSWORD);
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
    public UserDto updateUserByUserIdOrUserName(final UserDto user, final String userId, final String userName) throws UserNotFoundExceptions, UserExceptions, BadApiRequestExceptions {
        final String methodName = "updateUserByUserIdOrUserName(UserDto,String) in UserServiceImpl";

        Users userDetails = UserDtoToUsers(user);
        Users fetchedUser = loadUserByUserIdOrUserName(userId, userName, methodName);

        Predicate<String> isNotBlankField = StringUtils::isNotBlank;
        BiPredicate<String, String> checkFieldEquality = String::equalsIgnoreCase;

        Predicate<GENDER> isNotBlankFieldEnum = Objects::nonNull;
        BiPredicate<GENDER, GENDER> checkEqualEnumValues = Objects::equals;
        if (isNotBlankField.test(userDetails.getUserName()) &&
                !checkFieldEquality.test(userDetails.getUserName(), fetchedUser.getUserName())) {
            userValidationService.validateUser(Optional.of(userDetails), Optional.of(fetchedUser), methodName, UPDATE_USERNAME);
            fetchedUser = constructUser(fetchedUser, userDetails, USER_NAME);
        }
        if (isNotBlankField.test(userDetails.getFirstName()) &&
                !checkFieldEquality.test(userDetails.getFirstName(), fetchedUser.getFirstName())) {
            fetchedUser = constructUser(fetchedUser, userDetails, FIRST_NAME);
        }
        if (isNotBlankField.test(userDetails.getLastName()) &&
                !checkFieldEquality.test(userDetails.getLastName(), fetchedUser.getLastName())) {
            fetchedUser = constructUser(fetchedUser, userDetails, LAST_NAME);
        }
        if (isNotBlankField.test(userDetails.getAbout()) &&
                !checkFieldEquality.test(userDetails.getAbout(), fetchedUser.getAbout())) {
            fetchedUser = constructUser(fetchedUser, userDetails, ABOUT);
        }
        if (isNotBlankField.test(userDetails.getPrimaryEmail()) &&
                !checkFieldEquality.test(userDetails.getPrimaryEmail(), fetchedUser.getPrimaryEmail())) {
            userValidationService.validateUser(Optional.of(userDetails), Optional.of(fetchedUser), methodName, UPDATE_PRIMARY_EMAIL);
            fetchedUser = constructUser(fetchedUser, userDetails, PRIMARY_EMAIL);
        }
        if (isNotBlankField.test(userDetails.getSecondaryEmail()) &&
                !checkFieldEquality.test(userDetails.getSecondaryEmail(), fetchedUser.getSecondaryEmail())) {
            userValidationService.validateUser(Optional.of(userDetails), Optional.of(fetchedUser), methodName, UPDATE_SECONDARY_EMAIL);
            fetchedUser = constructUser(fetchedUser, userDetails, SECONDARY_EMAIL);
        }
        if (isNotBlankFieldEnum.test(userDetails.getGender()) &&
                !checkEqualEnumValues.test(userDetails.getGender(), fetchedUser.getGender())) {
            fetchedUser = constructUser(fetchedUser, userDetails, GENDER);
        }
        if (isNotBlankField.test(userDetails.getPassword()) &&
                !checkFieldEquality.test(userDetails.getPassword(), fetchedUser.getPassword())) {
            userValidationService.validateUser(Optional.of(userDetails),Optional.of(fetchedUser), methodName, UPDATE_PASSWORD);
            fetchedUser = constructUser(fetchedUser, userDetails, PASSWORD);
        }
        if (isNotBlankField.test(userDetails.getProfileImage()) &&
                !checkFieldEquality.test(userDetails.getProfileImage(), fetchedUser.getProfileImage())) {
            userValidationService.validateUser(Optional.of(userDetails),Optional.of(fetchedUser), methodName, UPDATE_PROFILE_IMAGE);
            fetchedUser = constructUser(fetchedUser, userDetails, PROFILE_IMAGE);
        }
        Users savedUser = userRepository.save(fetchedUser);
        return UsersToUsersDto(savedUser);
    }

    /**
     * @param userId   - User Id
     * @param userName - userName of user
     */
    @Override
    public void deleteUserByUserIdOrUserName(final String userId, final String userName) throws UserExceptions, UserNotFoundExceptions, BadApiRequestExceptions {
        final String methodName = "deleteUserByUserIdOrUserName(string) in UserServiceImpl";

        Users fetchedUser = loadUserByUserIdOrUserName(userId, userName, methodName);
        userValidationService.validateUser(Optional.empty(),Optional.of(fetchedUser), methodName, DELETE_USER_BY_USER_ID_OR_USER_NAME);
        userRepository.deleteByUserIdOrUserName(userId, userName);
    }

    /**
     * @return Set<UserDto> - List Of all Users
     */
    @Override
    public Set<UserDto> getALlUsers() throws UserNotFoundExceptions {
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
    public UserDto getUserInformationByEmailOrUserName(final String email, final String userName) throws UserExceptions, UserNotFoundExceptions, BadApiRequestExceptions {
        final String methodName = "getUserInformationByEmailOrUserName(String) in UserServiceImpl";

        Users users = loadUserByEmailOrUserName(email, userName, methodName);
        return UsersToUsersDto(users);
    }

    /**
     * @param field - field of User Entity
     * @param value - value of field
     * @return List<UserDTo>
     */
    @Override
    public Set<UserDto> searchUserByFieldAndValue(final USER_FIELDS field, final String value) throws UserNotFoundExceptions {
        final String methodName = "searchUserByFieldAndValue(field,String) in UserServiceImpl";
        Set<Users> users = null;
        switch (field) {
            case PRIMARY_EMAIL -> {
                users = userRepository.searchUserByPrimaryEmail(value).get();
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
    public Set<UserDto> searchAllUsersByUserName(final String userNameWord) throws UserNotFoundExceptions {
        final String methodName = "searchAllUsersByUserName(string) in UsersServiceImpl";

        Set<Users> usersSet = loadAllUserByUserNameMatched(userNameWord, methodName);
        return usersSet.stream().map(MappingHelpers::UsersToUsersDto).collect(Collectors.toSet());
    }
}