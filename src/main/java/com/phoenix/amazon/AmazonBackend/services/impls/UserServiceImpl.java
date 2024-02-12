package com.phoenix.amazon.AmazonBackend.services.impls;

import com.phoenix.amazon.AmazonBackend.dto.PageableResponse;
import com.phoenix.amazon.AmazonBackend.dto.PasswordUpdateDto;
import com.phoenix.amazon.AmazonBackend.dto.UpdateUserDto;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;

import java.util.Properties;
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
import static com.phoenix.amazon.AmazonBackend.helpers.AllConstantHelpers.USER_FIELDS.GENDER;
import static com.phoenix.amazon.AmazonBackend.helpers.AllConstantHelpers.USER_VALIDATION.NULL_OBJECT;
import static com.phoenix.amazon.AmazonBackend.helpers.AllConstantHelpers.USER_VALIDATION.CREATE_USER;
import static com.phoenix.amazon.AmazonBackend.helpers.AllConstantHelpers.USER_VALIDATION.UPDATE_USERNAME;
import static com.phoenix.amazon.AmazonBackend.helpers.AllConstantHelpers.USER_VALIDATION.UPDATE_PRIMARY_EMAIL;
import static com.phoenix.amazon.AmazonBackend.helpers.AllConstantHelpers.USER_VALIDATION.UPDATE_SECONDARY_EMAIL;
import static com.phoenix.amazon.AmazonBackend.helpers.AllConstantHelpers.USER_VALIDATION.UPDATE_PASSWORD;
import static com.phoenix.amazon.AmazonBackend.helpers.AllConstantHelpers.USER_VALIDATION.DELETE_USER_BY_USER_ID_OR_USER_NAME;
import static com.phoenix.amazon.AmazonBackend.helpers.AllConstantHelpers.USER_VALIDATION.GET_ALL_USERS;
import static com.phoenix.amazon.AmazonBackend.helpers.AllConstantHelpers.USER_VALIDATION.SEARCH_USER_BY_EMAIL;
import static com.phoenix.amazon.AmazonBackend.helpers.AllConstantHelpers.USER_VALIDATION.SEARCH_ALL_USERS_BY_USER_NAME;
import static com.phoenix.amazon.AmazonBackend.helpers.AllConstantHelpers.USER_VALIDATION.SEARCH_USER_BY_USER_NAME;
import static com.phoenix.amazon.AmazonBackend.helpers.AllConstantHelpers.USER_VALIDATION.SEARCH_ALL_USERS_BY_FIRST_NAME;
import static com.phoenix.amazon.AmazonBackend.helpers.AllConstantHelpers.USER_VALIDATION.SEARCH_ALL_USERS_BY_LAST_NAME;
import static com.phoenix.amazon.AmazonBackend.helpers.AllConstantHelpers.USER_VALIDATION.SEARCH_ALL_USERS_BY_GENDER;
import static com.phoenix.amazon.AmazonBackend.helpers.AllConstantHelpers.USER_VALIDATION.VALIDATE_PASSWORD;
import static com.phoenix.amazon.AmazonBackend.helpers.MappingHelpers.UserDtoToUsers;
import static com.phoenix.amazon.AmazonBackend.helpers.MappingHelpers.UserUpdateDtoToUsers;
import static com.phoenix.amazon.AmazonBackend.helpers.MappingHelpers.UsersToUsersDto;
import static com.phoenix.amazon.AmazonBackend.helpers.PagingHelpers.getPageableResponse;
import static com.phoenix.amazon.AmazonBackend.helpers.AllConstantHelpers.DestinationDtoType.USER_DTO;


@Service("UserServicePrimary")
public class UserServiceImpl extends AbstractUserService implements IUserService {
    private final IUserRepository userRepository;
    private final IUserValidationService userValidationService;
    private final String imagePath;
    Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    public UserServiceImpl(IUserRepository userRepository,
                           IUserValidationService userValidationService,
                           @Value("${path.services.user.image.properties}") final String PATH_TO_IMAGE_PROPS) {
        super(userRepository, userValidationService);
        this.userRepository = userRepository;
        this.userValidationService = userValidationService;

        final Properties properties = new Properties();
        try {
            properties.load(new FileInputStream(PATH_TO_IMAGE_PROPS));
        } catch (IOException e) {
            logger.error("Error in reading the props in {} UserServiceImpl", e.getMessage());
        }
        this.imagePath = properties.getProperty("user.profile.images.path");
    }

    private UserDto initializeUserId(final UserDto userDto) throws UserExceptions, UserNotFoundExceptions, BadApiRequestExceptions, IOException {
        final String methodName = "initializeUserId";
        if (Objects.isNull(userDto)) userValidationService.validateUser(Optional.empty(), Optional.empty(),
                "initializeUserId in UserService", NULL_OBJECT);

        final String userIdUUID = UUID.randomUUID().toString();
        final String secondaryEmail = StringUtils.isBlank(userDto.secondaryEmail()) ? userDto.secondaryEmail() : userDto.secondaryEmail().trim();
        final String about = StringUtils.isBlank(userDto.about()) ? userDto.about() : userDto.about().trim();
        final String password = StringUtils.isBlank(userDto.password()) ? userDto.password() : userDto.password().trim();
        final String userName = StringUtils.isBlank(userDto.userName()) ? userDto.userName() : userDto.userName().trim();
        final String primaryEmail = StringUtils.isBlank(userDto.primaryEmail()) ? userDto.primaryEmail() : userDto.primaryEmail().trim();
        final String gender = StringUtils.isBlank(userDto.gender()) ? userDto.gender() : userDto.gender().trim();
        final String firstName = StringUtils.isBlank(userDto.firstName()) ? userDto.firstName() : userDto.firstName().trim();
        final String lastName = StringUtils.isBlank(userDto.lastName()) ? userDto.lastName() : userDto.lastName().trim();

        return new UserDto.builder()
                .userId(userIdUUID)
                .userName(userName)
                .firstName(firstName)
                .lastName(lastName)
                .primaryEmail(primaryEmail)
                .secondaryEmail(secondaryEmail)
                .gender(gender)
                .password(password)
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
     * @throws UserNotFoundExceptions,UserExceptions,BadApiRequestExceptions,IOException -list of exceptions being thrown
     **/
    @Override
    public UserDto createUserService(final UserDto userDto) throws UserExceptions, UserNotFoundExceptions, BadApiRequestExceptions, IOException {
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
     * @param user         - user object
     * @param userId       - id of user
     * @param userName     - username of user
     * @param primaryEmail - primary Email of user
     * @return UserDto - userDto Object
     * @throws UserNotFoundExceptions,UserExceptions,BadApiRequestExceptions,IOException -list of exceptions being thrown
     **/
    @Override
    public UserDto updateUserServiceByUserIdOrUserNameOrPrimaryEmail(final UpdateUserDto user, final String userId, final String userName, final String primaryEmail) throws UserNotFoundExceptions, UserExceptions, BadApiRequestExceptions, IOException {
        final String methodName = "updateUserByUserIdOrUserName(UserDto,String) in UserServiceImpl";

        Users userDetails = UserUpdateDtoToUsers(user);
        Users fetchedUser = loadUserByUserIdOrUserNameOrPrimaryEmail(userId, userName, primaryEmail, methodName);

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
        if (isNotBlankField.test(userDetails.getAbout()) &&
                !checkFieldEquality.test(userDetails.getAbout(), fetchedUser.getAbout())) {
            fetchedUser = constructUser(fetchedUser, userDetails, ABOUT);
        }

        Users savedUser = userRepository.save(fetchedUser);
        return UsersToUsersDto(savedUser);
    }

    /**
     * @param userId       - id of user
     * @param userName     - username of user
     * @param primaryEmail - primary Email of user
     * @throws UserNotFoundExceptions,UserExceptions,BadApiRequestExceptions,IOException -list of exceptions being thrown
     **/
    @Override
    public void deleteUserServiceByUserIdOrUserNameOrPrimaryEmail(final String userId, final String userName, final String primaryEmail) throws UserExceptions, UserNotFoundExceptions, BadApiRequestExceptions, IOException {
        final String methodName = "deleteUserByUserIdOrUserName(string) in UserServiceImpl";
        Users fetchedUser = loadUserByUserIdOrUserNameOrPrimaryEmail(userId, userName, primaryEmail, methodName);
        userValidationService.validateUser(Optional.empty(), Optional.of(fetchedUser), methodName, DELETE_USER_BY_USER_ID_OR_USER_NAME);

        if (!StringUtils.isBlank(fetchedUser.getProfileImage())) {
            final String pathToProfileIMage = imagePath + File.separator + fetchedUser.getProfileImage();
            Files.deleteIfExists(Paths.get(pathToProfileIMage));
        }

        userRepository.deleteByUserIdOrUserNameOrPrimaryEmail(userId, userName, primaryEmail);
    }

    /**
     * @param pageNumber - index value of page
     * @param pageSize   - size of page
     * @param sortBy     - sort column
     * @param sortDir    - direction of sorting
     * @return PageableResponse<userDto> - page of userDto
     * @throws UserNotFoundExceptions - list of exceptions being thrown
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
     * @param userId       - userid of user
     * @param userName     - username of user
     * @param primaryEmail - primary Email of user
     * @return UserDto     - userDto Object
     * @throws UserNotFoundExceptions,UserExceptions,BadApiRequestExceptions,IOException -list of exceptions being thrown
     **/
    @Override
    public UserDto getUserServiceInformationByUserIdOrUserNameOrPrimaryEmail(final String userId, final String userName, final String primaryEmail) throws UserExceptions, UserNotFoundExceptions, BadApiRequestExceptions, IOException {
        final String methodName = "getUserInformationByEmailOrUserName(String) in UserServiceImpl";
        Users users = loadUserByUserIdOrUserNameOrPrimaryEmail(userId, userName, primaryEmail, methodName);
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
     * @throws UserNotFoundExceptions - list of exceptions being thrown
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
                usersPage = userRepository.searchUserByEmail(value, pageableObject).get();
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
     * @throws UserNotFoundExceptions - list of exceptions being thrown
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

    /**
     * @return String
     **/
    @Override
    public String generatePasswordService() {
        final String lowerCase = "abcdefghijklmnopqrstuvwxyz";
        final String upperCase = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        final String specialCase = "!@#$%^&*()-_+/<>()?=|";
        final String numbers = "0123456789";

        StringBuffer password = new StringBuffer();
        int capacity = 4, randomId = -1;
        for (int i = 0; i < 16; i++) {
            int k = i % capacity;

            switch (k) {
                case 0: {
                    randomId = (int) (Math.random() * lowerCase.length());
                    password.append(lowerCase.charAt(randomId));
                    break;
                }
                case 1: {
                    randomId = (int) (Math.random() * upperCase.length());
                    password.append(upperCase.charAt(randomId));
                    break;
                }
                case 2: {
                    randomId = (int) (Math.random() * specialCase.length());
                    password.append(specialCase.charAt(randomId));
                    break;
                }
                case 3: {
                    randomId = (int) (Math.random() * numbers.length());
                    password.append(numbers.charAt(randomId));
                    break;
                }
            }
        }
        return password.toString();
    }

    /**
     * @param passwordUpdateDto - request object to update password
     * @throws UserNotFoundExceptions,UserExceptions,BadApiRequestExceptions,IOException - list of exceptions to be thrown
     */
    @Override
    public void resetPasswordService(final PasswordUpdateDto passwordUpdateDto) throws UserNotFoundExceptions, UserExceptions, BadApiRequestExceptions, IOException {
        final String methodName = "resetPasswordService(primaryEmail) in UserServiceImpl";

        final String primaryEmail = passwordUpdateDto.primaryEmail();
        Users fetchedUser = loadUserByUserIdOrUserNameOrPrimaryEmail(primaryEmail, primaryEmail, primaryEmail, methodName);

        // check is the old password , the current password of user
        final String oldPassword = passwordUpdateDto.oldPassword();
        Users oldUser = new Users.builder().password(oldPassword).build();
        userValidationService.validateUser(Optional.of(oldUser), Optional.of(fetchedUser), methodName, VALIDATE_PASSWORD);

        //update password & save
        final String newPassword = passwordUpdateDto.newPassword();
        Users newUser = new Users.builder().password(newPassword).build();
        userValidationService.validateUser(Optional.of(newUser), Optional.of(fetchedUser), methodName, UPDATE_PASSWORD);
        fetchedUser = constructUser(fetchedUser, newUser, PASSWORD);
        userRepository.save(fetchedUser);
    }
}