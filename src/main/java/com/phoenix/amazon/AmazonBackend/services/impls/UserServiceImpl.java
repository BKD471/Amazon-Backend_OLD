package com.phoenix.amazon.AmazonBackend.services.impls;

import com.phoenix.amazon.AmazonBackend.dto.PageableResponse;
import com.phoenix.amazon.AmazonBackend.dto.UserDto;
import com.phoenix.amazon.AmazonBackend.entity.Users;
import com.phoenix.amazon.AmazonBackend.exceptions.BadApiRequestExceptions;
import com.phoenix.amazon.AmazonBackend.exceptions.UserExceptions;
import com.phoenix.amazon.AmazonBackend.exceptions.UserNotFoundExceptions;
import com.phoenix.amazon.AmazonBackend.helpers.AllConstantHelpers.USER_FIELDS;
import com.phoenix.amazon.AmazonBackend.repository.IUserRepository;
import com.phoenix.amazon.AmazonBackend.services.AbstractUserService;
import com.phoenix.amazon.AmazonBackend.services.IUserService;
import com.phoenix.amazon.AmazonBackend.services.validationservice.IUserValidationService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;

import java.util.UUID;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

import static com.phoenix.amazon.AmazonBackend.helpers.AllConstantHelpers.GENDER;
import static com.phoenix.amazon.AmazonBackend.helpers.AllConstantHelpers.USER_FIELDS.PASSWORD;
import static com.phoenix.amazon.AmazonBackend.helpers.AllConstantHelpers.USER_FIELDS.USER_NAME;
import static com.phoenix.amazon.AmazonBackend.helpers.AllConstantHelpers.USER_FIELDS.FIRST_NAME;
import static com.phoenix.amazon.AmazonBackend.helpers.AllConstantHelpers.USER_FIELDS.LAST_NAME;
import static com.phoenix.amazon.AmazonBackend.helpers.AllConstantHelpers.USER_FIELDS.ABOUT;
import static com.phoenix.amazon.AmazonBackend.helpers.AllConstantHelpers.USER_FIELDS.PRIMARY_EMAIL;
import static com.phoenix.amazon.AmazonBackend.helpers.AllConstantHelpers.USER_FIELDS.SECONDARY_EMAIL;
import static com.phoenix.amazon.AmazonBackend.helpers.AllConstantHelpers.USER_FIELDS.PROFILE_IMAGE;
import static com.phoenix.amazon.AmazonBackend.helpers.AllConstantHelpers.USER_FIELDS.GENDER;
import static com.phoenix.amazon.AmazonBackend.helpers.AllConstantHelpers.USER_VALIDATION.NULL_OBJECT;
import static com.phoenix.amazon.AmazonBackend.helpers.AllConstantHelpers.USER_VALIDATION.CREATE_USER;
import static com.phoenix.amazon.AmazonBackend.helpers.AllConstantHelpers.USER_VALIDATION.UPDATE_USERNAME;
import static com.phoenix.amazon.AmazonBackend.helpers.AllConstantHelpers.USER_VALIDATION.UPDATE_PRIMARY_EMAIL;
import static com.phoenix.amazon.AmazonBackend.helpers.AllConstantHelpers.USER_VALIDATION.UPDATE_SECONDARY_EMAIL;
import static com.phoenix.amazon.AmazonBackend.helpers.AllConstantHelpers.USER_VALIDATION.UPDATE_PASSWORD;
import static com.phoenix.amazon.AmazonBackend.helpers.AllConstantHelpers.USER_VALIDATION.UPDATE_PROFILE_IMAGE;
import static com.phoenix.amazon.AmazonBackend.helpers.AllConstantHelpers.USER_VALIDATION.DELETE_USER_BY_USER_ID_OR_USER_NAME;
import static com.phoenix.amazon.AmazonBackend.helpers.AllConstantHelpers.USER_VALIDATION.GET_ALL_USERS;
import static com.phoenix.amazon.AmazonBackend.helpers.AllConstantHelpers.USER_VALIDATION.SEARCH_USER_BY_EMAIL;
import static com.phoenix.amazon.AmazonBackend.helpers.AllConstantHelpers.USER_VALIDATION.SEARCH_ALL_USERS_BY_USER_NAME;
import static com.phoenix.amazon.AmazonBackend.helpers.AllConstantHelpers.USER_VALIDATION.SEARCH_USER_BY_USER_NAME;
import static com.phoenix.amazon.AmazonBackend.helpers.AllConstantHelpers.USER_VALIDATION.SEARCH_ALL_USERS_BY_FIRST_NAME;
import static com.phoenix.amazon.AmazonBackend.helpers.AllConstantHelpers.USER_VALIDATION.SEARCH_ALL_USERS_BY_LAST_NAME;
import static com.phoenix.amazon.AmazonBackend.helpers.AllConstantHelpers.USER_VALIDATION.SEARCH_ALL_USERS_BY_GENDER;
import static com.phoenix.amazon.AmazonBackend.helpers.MappingHelpers.UserDtoToUsers;
import static com.phoenix.amazon.AmazonBackend.helpers.MappingHelpers.UsersToUsersDto;
import static com.phoenix.amazon.AmazonBackend.helpers.MappingHelpers.getPageableResponse;
import static com.phoenix.amazon.AmazonBackend.helpers.AllConstantHelpers.DestinationDtoType.USER_DTO;


@Service("UserServiceMain")
public class UserServiceImpl extends AbstractUserService implements IUserService {
    private final IUserRepository userRepository;
    private final IUserValidationService userValidationService;

    public UserServiceImpl(IUserRepository userRepository, IUserValidationService userValidationService) {
        super(userRepository, userValidationService);
        this.userRepository = userRepository;
        this.userValidationService = userValidationService;
    }

    private UserDto initializeUserId(final UserDto userDto) throws UserExceptions, UserNotFoundExceptions, BadApiRequestExceptions {
        final String methodName = "initializeUserId";
        if (Objects.isNull(userDto)) userValidationService.validateUser(Optional.empty(), Optional.empty(),
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

    private Pageable getPageableObject(final int pageNumber, final int pageSize, final Sort sort) {
        return PageRequest.of(pageNumber - 1, pageSize, sort);
    }

    /**
     * @param userDto - userDto object
     * @return UserDto - userDto Object
     **/
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
     * @param user     - user object
     * @param userId   - id of user
     * @param userName - username of user
     * @return UserDto - userDto Object
     **/
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
            userValidationService.validateUser(Optional.of(userDetails), Optional.of(fetchedUser), methodName, UPDATE_PASSWORD);
            fetchedUser = constructUser(fetchedUser, userDetails, PASSWORD);
        }
        if (isNotBlankField.test(userDetails.getProfileImage()) &&
                !checkFieldEquality.test(userDetails.getProfileImage(), fetchedUser.getProfileImage())) {
            userValidationService.validateUser(Optional.of(userDetails), Optional.of(fetchedUser), methodName, UPDATE_PROFILE_IMAGE);
            fetchedUser = constructUser(fetchedUser, userDetails, PROFILE_IMAGE);
        }
        Users savedUser = userRepository.save(fetchedUser);
        return UsersToUsersDto(savedUser);
    }

    /**
     * @param userId   - id of user
     * @param userName - username of user
     **/
    @Override
    public void deleteUserByUserIdOrUserName(final String userId, final String userName) throws UserExceptions, UserNotFoundExceptions, BadApiRequestExceptions {
        final String methodName = "deleteUserByUserIdOrUserName(string) in UserServiceImpl";
        Users fetchedUser = loadUserByUserIdOrUserName(userId, userName, methodName);
        userValidationService.validateUser(Optional.empty(), Optional.of(fetchedUser), methodName, DELETE_USER_BY_USER_ID_OR_USER_NAME);
        userRepository.deleteByUserIdOrUserName(userId, userName);
    }

    /**
     * @param pageNumber - index value of page
     * @param pageSize   - size of page
     * @param sortBy     - sort column
     * @param sortDir    - direction of sorting
     * @return PageableResponse<userDto> - page of userDto
     **/
    @Override
    public PageableResponse<UserDto> getAllUsers(final int pageNumber, final int pageSize, final String sortBy, final String sortDir) throws UserNotFoundExceptions {
        final String methodName = "getALlUsers() in UserServiceImpl";
        final Sort sort = sortDir.equals("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        final Pageable pageableObject = getPageableObject(pageNumber, pageSize, sort);
        Page<Users> userPage = userRepository.findAll(pageableObject);
        userValidationService.validateUserList(userPage.getContent(), methodName, GET_ALL_USERS);
        return getPageableResponse(userPage, USER_DTO);
    }

    /**
     * @param email    - email of user
     * @param userName - username of user
     * @return UserDto - userDto Object
     **/
    @Override
    public UserDto getUserInformationByEmailOrUserName(final String email, final String userName) throws UserExceptions, UserNotFoundExceptions, BadApiRequestExceptions {
        final String methodName = "getUserInformationByEmailOrUserName(String) in UserServiceImpl";
        Users users = loadUserByEmailOrUserName(email, userName, methodName);
        return UsersToUsersDto(users);
    }

    /**
     * @param field      - field of user entity
     * @param value      - value to query the field
     * @param pageNumber - index value of page
     * @param pageSize   - size of page
     * @param sortBy     - sort column
     * @param sortDir    - direction of sorting
     * @return PageableResponse<UserDto> - page of userDto
     **/
    @Override
    public PageableResponse<UserDto> searchUserByFieldAndValue(final USER_FIELDS field, final String value, final int pageNumber, final int pageSize, final USER_FIELDS sortBy, final String sortDir) throws UserNotFoundExceptions {
        final String methodName = "searchUserByFieldAndValue(field,String) in UserServiceImpl";

        final StringBuffer sortByColumn = getUserDbField(sortBy);
        final Sort sort = sortDir.equals("desc") ? Sort.by(sortByColumn.toString()).descending() : Sort.by(sortByColumn.toString()).ascending();
        final Pageable pageableObject = getPageableObject(pageNumber, pageSize, sort);
        Page<Users> usersPage = Page.empty();
        switch (field) {
            case PRIMARY_EMAIL -> {
                usersPage = userRepository.searchUserByPrimaryEmail(value, pageableObject).get();
                userValidationService.validateUserList(usersPage.getContent(), methodName, SEARCH_USER_BY_EMAIL);
            }
            case USER_NAME -> {
                usersPage = userRepository.searchUserByUserName(value, pageableObject).get();
                userValidationService.validateUserList(usersPage.getContent(), methodName, SEARCH_USER_BY_USER_NAME);
            }
            case GENDER -> {
                usersPage = userRepository.searchUserByGender(value, pageableObject).get();
                userValidationService.validateUserList(usersPage.getContent(), methodName, SEARCH_ALL_USERS_BY_GENDER);
            }
            case FIRST_NAME -> {
                usersPage = userRepository.searchUserByFirstName(value, pageableObject).get();
                userValidationService.validateUserList(usersPage.getContent(), methodName, SEARCH_ALL_USERS_BY_FIRST_NAME);
            }
            case LAST_NAME -> {
                usersPage = userRepository.searchUserByLastName(value, pageableObject).get();
                userValidationService.validateUserList(usersPage.getContent(), methodName, SEARCH_ALL_USERS_BY_LAST_NAME);
            }
        }
        return getPageableResponse(usersPage, USER_DTO);
    }

    /**
     * @param userNameWord - username of user
     * @param pageNumber   - index value of page
     * @param pageSize     - size of page
     * @param sortBy       - sort column
     * @param sortDir      - direction of sorting
     * @return PageableResponse<UserDto> - page of userDto
     */
    @Override
    public PageableResponse<UserDto> searchAllUsersByUserName(final String userNameWord, final int pageNumber, final int pageSize, final String sortBy, final String sortDir) throws UserNotFoundExceptions {
        final String methodName = "searchAllUsersByUserName(string) in UsersServiceImpl";

        final Sort sort = sortDir.equals("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        final Pageable pageableObject = getPageableObject(pageNumber, pageSize, sort);
        Page<Users> allUsersWithNearlyUserNamePage = userRepository.findAllByUserNameContaining(userNameWord, pageableObject).get();
        userValidationService.validateUserList(allUsersWithNearlyUserNamePage.getContent(), methodName, SEARCH_ALL_USERS_BY_USER_NAME);
        return getPageableResponse(allUsersWithNearlyUserNamePage, USER_DTO);
    }
}