package com.phoenix.amazon.AmazonBackend.services.impls;

import com.phoenix.amazon.AmazonBackend.dto.UpdateUserDto;
import com.phoenix.amazon.AmazonBackend.dto.UserDto;
import com.phoenix.amazon.AmazonBackend.entity.Users;
import com.phoenix.amazon.AmazonBackend.exceptions.BadApiRequestExceptions;
import com.phoenix.amazon.AmazonBackend.exceptions.UserExceptions;
import com.phoenix.amazon.AmazonBackend.exceptions.UserNotFoundExceptions;
import com.phoenix.amazon.AmazonBackend.helpers.AllConstantHelpers;
import com.phoenix.amazon.AmazonBackend.repository.IUserRepository;
import com.phoenix.amazon.AmazonBackend.services.IUserService;
import com.phoenix.amazon.AmazonBackend.services.validationservice.IUserValidationService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.MockBeans;

import java.io.FileInputStream;
import java.io.IOException;

import java.nio.file.Files;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Optional;
import java.util.Properties;
import java.util.Set;

import static com.phoenix.amazon.AmazonBackend.helpers.AllConstantHelpers.GENDER.NON_BINARY;
import static com.phoenix.amazon.AmazonBackend.helpers.AllConstantHelpers.GENDER.FEMALE;
import static com.phoenix.amazon.AmazonBackend.helpers.AllConstantHelpers.GENDER.MALE;

import static com.phoenix.amazon.AmazonBackend.helpers.AllConstantHelpers.USER_FIELDS.PRIMARY_EMAIL;
import static com.phoenix.amazon.AmazonBackend.helpers.AllConstantHelpers.USER_FIELDS.FIRST_NAME;
import static com.phoenix.amazon.AmazonBackend.helpers.AllConstantHelpers.USER_FIELDS.LAST_NAME;
import static com.phoenix.amazon.AmazonBackend.helpers.AllConstantHelpers.USER_FIELDS.GENDER;
import static com.phoenix.amazon.AmazonBackend.helpers.AllConstantHelpers.USER_FIELDS.USER_NAME;

import static com.phoenix.amazon.AmazonBackend.helpers.MappingHelpers.UserDtoToUsers;
import static com.phoenix.amazon.AmazonBackend.helpers.MappingHelpers.UserToUpdateUserDto;
import static com.phoenix.amazon.AmazonBackend.helpers.MappingHelpers.UserUpdateDtoToUsers;
import static com.phoenix.amazon.AmazonBackend.helpers.MappingHelpers.UsersToUsersDto;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.anySet;


@ExtendWith(MockitoExtension.class)
@SpringBootTest
public class UserServiceTest {
    private final String TEST_PRIMARY_EMAIL = "test@gmail.com";
    private final String TEST_SECONDARY_EMAIL = "tests@gmail.com";
    private final String TEST_USER_NAME = "TEST_USER_NAME";
    private final String TEST_UUID = "58824409-dd6b-4934-9923-ec1daf9693da";
    private final String TEST_FIRST_NAME = "TEST_FIRST_NAME";
    private final String TEST_LAST_NAME = "TEST_LAST_NAME";
    private final AllConstantHelpers.GENDER TEST_GENDER = MALE;
    private final String TEST_PASSWORD = "$2y$10$JUH1QJQnAndkUwMclwdMc.hAbqAZ61Yb/7yCmCeIdNTjAMgC1NhNC";
    private final String TEST_PROFILE_IMAGE = "0ecbe17e-5537-4533-9b9f-b3c2438e58eb.jpg";
    private final String TEST_ABOUT = "Lorem ipsum dolor sit amet consectetur adipisicing elit. Maxime mollitia,\n" +
            "molestiae quas vel sint commodi repudiandae consequuntur voluptatum laborum\n" +
            "numquam blanditiis harum quisquam eius sed odit fugiat iusto fuga praesentium\n" +
            "optio, eaque rerum! Provident similique accusantium nemo autem. Veritatis\n" +
            "obcaecati tenetur iure eius earum ut molestias architecto voluptate aliquam\n" +
            "nihil, eveniet aliquid culpa officia aut! Impedit sit sunt quaerat, odit,\n";
    private final LocalDateTime TEST_LAST_SEEN = LocalDateTime.now();
    private final String ADMIN = "ADMIN";

    @MockBean
    private IUserService userServiceMock;
    @Mock
    private IUserValidationService userValidationServiceMock;
    @Mock
    private IUserRepository userRepositoryMock;

    @Mock
    private static Files filesMock;

    @Value("${path.services.user.image.properties}")
    private String PATH_TO_IMAGE_PROPS;

    @BeforeEach
    public void setUp() {
        userServiceMock = new UserServiceImpl(userRepositoryMock, userValidationServiceMock, PATH_TO_IMAGE_PROPS);
    }

    @Test
    @DisplayName("Test Happy Path -- createUser() With valid fields")
    public void testCreateUserServiceHappyPath() throws UserExceptions, UserNotFoundExceptions, BadApiRequestExceptions, IOException {
        // When
        when(userRepositoryMock.save(any())).thenReturn(constructUser());
        doNothing().when(userValidationServiceMock).validateUser(any(), any(), anyString(), any());
        UserDto userDto = userServiceMock.createUserService(UsersToUsersDto(constructUser()));

        // Then
        assertThat(userDto.userName()).isEqualTo(TEST_USER_NAME);
        assertThat(userDto.primaryEmail()).isEqualTo(TEST_PRIMARY_EMAIL);
        assertThat(userDto.userId()).isEqualTo(TEST_UUID);
    }

    @Test
    @DisplayName("Test Unhappy Path -- createUserService() with Null User")
    public void testCreateUserUnhappyPathNullUser() throws UserNotFoundExceptions, UserExceptions, BadApiRequestExceptions, IOException {
        // When
        doThrow(new BadApiRequestExceptions(BadApiRequestExceptions.class,
                "Null Users prohibited"
                , "testCreateUserUnhappyPathNullUser"))
                .when(userValidationServiceMock).validateUser(any(), any(), anyString(), any());

        // Then
        assertThrows(BadApiRequestExceptions.class, () -> {
            userServiceMock.createUserService(null);
        }, "BadApiRequestExceptions should have been thrown");
    }

    @Test
    @DisplayName("Test Unhappy Path -- createUserService() with null UserName")
    public void testCreateUserServiceUnhappyPathNullUserName() throws UserNotFoundExceptions, UserExceptions, BadApiRequestExceptions, IOException {
        UserDto userRequest = new UserDto.builder()
                .userId(TEST_UUID)
                .userName(null)
                .firstName(TEST_FIRST_NAME)
                .lastName(TEST_LAST_NAME)
                .primaryEmail(TEST_PRIMARY_EMAIL)
                .secondaryEmail(TEST_SECONDARY_EMAIL)
                .about(TEST_ABOUT)
                .gender(TEST_GENDER.toString())
                .password(TEST_PASSWORD)
                .profileImage(TEST_PROFILE_IMAGE)
                .lastSeen(TEST_LAST_SEEN)
                .build();
        // When
        doThrow(new BadApiRequestExceptions(BadApiRequestExceptions.class,
                "Null UserName prohibited"
                , "testCreateUserUnhappyPathNullUserName"))
                .when(userValidationServiceMock).validateUser(any(), any(), anyString(), any());

        // Then
        assertThrows(BadApiRequestExceptions.class, () -> {
            userServiceMock.createUserService(userRequest);
        }, "BadApiRequestExceptions should have been thrown");
    }

    @Test
    @DisplayName("Test Unhappy Path -- createUserService() with null PrimaryEmail")
    public void testCreateUserServiceUnhappyPathNullEmail() throws UserNotFoundExceptions, UserExceptions, BadApiRequestExceptions, IOException {
        UserDto userRequest = new UserDto.builder()
                .userId(TEST_UUID)
                .userName(TEST_USER_NAME)
                .firstName(TEST_FIRST_NAME)
                .lastName(TEST_LAST_NAME)
                .primaryEmail(null)
                .about(TEST_ABOUT)
                .gender(TEST_GENDER.toString())
                .password(TEST_PASSWORD)
                .profileImage(TEST_PROFILE_IMAGE)
                .lastSeen(TEST_LAST_SEEN)
                .build();
        // When
        doThrow(new BadApiRequestExceptions(BadApiRequestExceptions.class,
                "Null Email prohibited"
                , "testCreateUserUnhappyPathNullEmail"))
                .when(userValidationServiceMock).validateUser(any(), any(), anyString(), any());

        // Then
        assertThrows(BadApiRequestExceptions.class, () -> {
            userServiceMock.createUserService(userRequest);
        }, "BadApiRequestExceptions should have been thrown");
    }

    @Test
    @DisplayName("Test Unhappy Path -- createUserService() with null FirstName")
    public void testCreateUserServiceUnhappyPathNullFirstName() throws UserNotFoundExceptions, UserExceptions, BadApiRequestExceptions, IOException {
        UserDto userRequest = new UserDto.builder()
                .userId(TEST_UUID)
                .userName(TEST_USER_NAME)
                .firstName(null)
                .lastName(TEST_LAST_NAME)
                .primaryEmail(TEST_PRIMARY_EMAIL)
                .secondaryEmail(TEST_SECONDARY_EMAIL)
                .about(TEST_ABOUT)
                .gender(TEST_GENDER.toString())
                .password(TEST_PASSWORD)
                .profileImage(TEST_PROFILE_IMAGE)
                .lastSeen(TEST_LAST_SEEN)
                .build();
        // When
        doThrow(new BadApiRequestExceptions(BadApiRequestExceptions.class,
                "Null FirstName prohibited"
                , "testCreateUserUnhappyPathNullFirstName"))
                .when(userValidationServiceMock).validateUser(any(), any(), anyString(), any());

        // Then
        assertThrows(BadApiRequestExceptions.class, () -> {
            userServiceMock.createUserService(userRequest);
        }, "BadApiRequestExceptions should have been thrown");
    }

    @Test
    @DisplayName("Test Unhappy Path -- createUserService() with null LastName")
    public void testCreateUserServiceUnhappyPathNullLastName() throws UserNotFoundExceptions, UserExceptions, BadApiRequestExceptions, IOException {
        UserDto userRequest = new UserDto.builder()
                .userId(TEST_UUID)
                .userName(TEST_USER_NAME)
                .firstName(TEST_FIRST_NAME)
                .lastName(null)
                .primaryEmail(TEST_PRIMARY_EMAIL)
                .secondaryEmail(TEST_SECONDARY_EMAIL)
                .about(TEST_ABOUT)
                .gender(TEST_GENDER.toString())
                .password(TEST_PASSWORD)
                .profileImage(TEST_PROFILE_IMAGE)
                .lastSeen(TEST_LAST_SEEN)
                .build();
        // When
        doThrow(new BadApiRequestExceptions(BadApiRequestExceptions.class,
                "Null LastName prohibited"
                , "testCreateUserUnhappyPathNullFirstName"))
                .when(userValidationServiceMock).validateUser(any(), any(), anyString(), any());

        // Then
        assertThrows(BadApiRequestExceptions.class, () -> {
            userServiceMock.createUserService(userRequest);
        }, "BadApiRequestExceptions should have been thrown");
    }

    @Test
    @DisplayName("Test Unhappy Path -- createUserService() with null Gender")
    public void testCreateUserServiceUnhappyPathNullGender() throws UserNotFoundExceptions, UserExceptions, BadApiRequestExceptions, IOException {
        UserDto userRequest = new UserDto.builder()
                .userId(TEST_UUID)
                .userName(TEST_USER_NAME)
                .firstName(TEST_FIRST_NAME)
                .lastName(TEST_LAST_NAME)
                .primaryEmail(TEST_PRIMARY_EMAIL)
                .secondaryEmail(TEST_SECONDARY_EMAIL)
                .about(TEST_ABOUT)
                .gender(null)
                .password(TEST_PASSWORD)
                .profileImage(TEST_PROFILE_IMAGE)
                .lastSeen(TEST_LAST_SEEN)
                .build();
        // When
        doThrow(new BadApiRequestExceptions(BadApiRequestExceptions.class,
                "Null Gender prohibited"
                , "testCreateUserUnhappyPathNullFirstName"))
                .when(userValidationServiceMock).validateUser(any(), any(), anyString(), any());

        // Then
        assertThrows(BadApiRequestExceptions.class, () -> {
            userServiceMock.createUserService(userRequest);
        }, "BadApiRequestExceptions should have been thrown");
    }


    @Test
    @DisplayName("Test Unhappy Path -- createUserService() with Primary email already existing")
    public void testCreateUserServiceUnhappyPathExistingPrimaryEmail() throws UserNotFoundExceptions, UserExceptions, BadApiRequestExceptions, IOException {
        // Given
        UserDto userRequest = new UserDto.builder()
                .userId(TEST_UUID)
                .userName(TEST_USER_NAME)
                .firstName(TEST_FIRST_NAME)
                .lastName(TEST_LAST_NAME)
                .primaryEmail("EXISTING_PRIMARY_EMAIL")
                .secondaryEmail(TEST_SECONDARY_EMAIL)
                .about(TEST_ABOUT)
                .gender(TEST_GENDER.toString())
                .password(TEST_PASSWORD)
                .profileImage(TEST_PROFILE_IMAGE)
                .lastSeen(TEST_LAST_SEEN)
                .build();

        // When
        doThrow(new UserExceptions(UserExceptions.class, String.format("There's an account with Primary Email %s", userRequest.primaryEmail())
                , "testCreateUserUnhappyPathExistingPrimaryEmail"))
                .when(userValidationServiceMock).validateUser(any(), any(), anyString(), any());

        // Then
        assertThrows(UserExceptions.class, () -> {
            userServiceMock.createUserService(userRequest);
        }, "UserException should have been thrown");
    }

    @Test
    @DisplayName("Test Unhappy Path -- createUserService() with Secondary email already existing")
    public void testCreateUserServiceUnhappyPathExistingSecondaryEmail() throws UserNotFoundExceptions, UserExceptions, BadApiRequestExceptions, IOException {
        // Given
        UserDto userRequest = new UserDto.builder()
                .userId(TEST_UUID)
                .userName(TEST_USER_NAME)
                .firstName(TEST_FIRST_NAME)
                .lastName(TEST_LAST_NAME)
                .primaryEmail(TEST_PRIMARY_EMAIL)
                .secondaryEmail("EXISTING_SECONDARY_EMAIL")
                .about(TEST_ABOUT)
                .gender(TEST_GENDER.toString())
                .password(TEST_PASSWORD)
                .profileImage(TEST_PROFILE_IMAGE)
                .lastSeen(TEST_LAST_SEEN)
                .build();

        // When
        doThrow(new UserExceptions(UserExceptions.class, String.format("There's an account with Secondary Email %s", userRequest.primaryEmail())
                , "testCreateUserUnhappyPathExistingPrimaryEmail"))
                .when(userValidationServiceMock).validateUser(any(), any(), anyString(), any());

        // Then
        assertThrows(UserExceptions.class, () -> {
            userServiceMock.createUserService(userRequest);
        }, "UserException should have been thrown");
    }

    @Test
    @DisplayName("Test Unhappy Path -- createUserService() with existing UserName")
    public void testCreateUserServiceUnhappyPathNullWithExistingUserName() throws UserNotFoundExceptions, UserExceptions, BadApiRequestExceptions, IOException {
        Users users = constructUser();
        UserDto userRequest = new UserDto.builder()
                .userId(users.getUserId())
                .userName("EXISTING_USER_NAME")
                .firstName(users.getFirstName())
                .lastName(users.getLastName())
                .primaryEmail(users.getPrimaryEmail())
                .secondaryEmail(users.getSecondaryEmail())
                .about(users.getAbout())
                .gender(users.getGender().toString())
                .password(users.getPassword())
                .profileImage(users.getProfileImage())
                .lastSeen(users.getLastSeen())
                .build();
        // When
        doThrow(new UserExceptions(UserExceptions.class,
                String.format("There's an account with UserName %s", userRequest.userName())
                , "testCreateUserUnhappyPathNullWithExistingUserName"))
                .when(userValidationServiceMock).validateUser(any(), any(), anyString(), any());

        // Then
        assertThrows(UserExceptions.class, () -> {
            userServiceMock.createUserService(userRequest);
        }, "UserExceptions should have been thrown");
    }

    @Test
    @DisplayName("Test Unhappy Path -- createUserService() with existing UserId")
    public void testCreateUserServiceUnhappyPathWithExistingUserId() throws UserNotFoundExceptions, UserExceptions, BadApiRequestExceptions, IOException {
        Users users = constructUser();
        UserDto userRequest = new UserDto.builder()
                .userId("EXISTING_USER_ID")
                .userName(users.getUserName())
                .firstName(users.getFirstName())
                .lastName(users.getLastName())
                .primaryEmail(users.getPrimaryEmail())
                .secondaryEmail(users.getSecondaryEmail())
                .about(users.getAbout())
                .gender(users.getGender().toString())
                .password(users.getPassword())
                .profileImage(users.getProfileImage())
                .lastSeen(users.getLastSeen())
                .build();
        // When
        doThrow(new UserExceptions(UserExceptions.class,
                String.format("There's an account with UserId %s", userRequest.userId())
                , "testCreateUserUnhappyPathNullWithExistingUserId"))
                .when(userValidationServiceMock).validateUser(any(), any(), anyString(), any());

        // Then
        assertThrows(UserExceptions.class, () -> {
            userServiceMock.createUserService(userRequest);
        }, "UserExceptions should have been thrown");
    }

    @Test
    @DisplayName("Test Happy Path -- updateUserServiceByUserIdOrUserNameOrPrimaryEmail() With all fields (firstName,lastName,Email,Gender,About) updated ")
    public void testUpdateUserServiceByUserIdOrUserNameOrPrimaryEmailAllFields() throws UserNotFoundExceptions, UserExceptions, BadApiRequestExceptions, IOException {
        // Given
        UpdateUserDto incomingUpdateUserDtoRequest = constructIncomingUpdateUserDtoRequest();

        // When
        when(userRepositoryMock.findByUserIdOrUserNameOrPrimaryEmail(TEST_UUID, TEST_USER_NAME, TEST_PRIMARY_EMAIL)).thenReturn(Optional.of(constructUser()));
        when(userRepositoryMock.save(any())).thenReturn(UserUpdateDtoToUsers(constructIncomingUpdateUserDtoRequest()));
        doNothing().when(userValidationServiceMock).validatePZeroUserFields(anyString(), anyString(), anyString(), anyString(), any());
        doNothing().when(userValidationServiceMock).validateUser(any(), any(), anyString(), any());
        UserDto updatedUser = userServiceMock.updateUserServiceByUserIdOrUserNameOrPrimaryEmail(incomingUpdateUserDtoRequest, TEST_UUID, TEST_USER_NAME, TEST_PRIMARY_EMAIL);

        // Then
        assertThat(updatedUser.userName()).isEqualTo(incomingUpdateUserDtoRequest.userName());
        assertThat(updatedUser.firstName()).isEqualTo(incomingUpdateUserDtoRequest.firstName());
        assertThat(updatedUser.lastName()).isEqualTo(incomingUpdateUserDtoRequest.lastName());
        assertThat(updatedUser.primaryEmail()).isEqualTo(incomingUpdateUserDtoRequest.primaryEmail());
        assertThat(updatedUser.secondaryEmail()).isEqualTo(incomingUpdateUserDtoRequest.secondaryEmail());
        assertThat(updatedUser.gender()).isEqualTo(incomingUpdateUserDtoRequest.gender());
        assertThat(updatedUser.about()).isEqualTo(incomingUpdateUserDtoRequest.about());
    }

    @Test
    @DisplayName("Test Happy Path -- updateUserServiceByUserIdOrUserNameOrPrimaryEmail() With only userName updated")
    public void testUpdateUserServiceByUserIdOrUserNameOrPrimaryEmailWithOnlyUserNameChanged() throws UserNotFoundExceptions, UserExceptions, BadApiRequestExceptions, IOException {
        // Given
        final String UPDATED_USER_NAME = "UPDATED_USER_NAME";
        Users users = constructUser();
        UpdateUserDto updatedUpdateUserDto = new UpdateUserDto.builder()
                .userName(UPDATED_USER_NAME)
                .userId(users.getUserId())
                .firstName(users.getFirstName())
                .lastName(users.getLastName())
                .primaryEmail(users.getPrimaryEmail())
                .secondaryEmail(users.getSecondaryEmail())
                .gender(users.getGender().toString())
                .about(users.getAbout())
                .build();

        // When
        when(userRepositoryMock.findByUserIdOrUserNameOrPrimaryEmail(TEST_UUID, TEST_USER_NAME, TEST_PRIMARY_EMAIL)).thenReturn(Optional.of(users));
        when(userRepositoryMock.save(any())).thenReturn(UserUpdateDtoToUsers(updatedUpdateUserDto));
        doNothing().when(userValidationServiceMock).validatePZeroUserFields(anyString(), anyString(), anyString(), anyString(), any());
        doNothing().when(userValidationServiceMock).validateUser(any(), any(), anyString(), any());
        UserDto updatedUser = userServiceMock.updateUserServiceByUserIdOrUserNameOrPrimaryEmail(updatedUpdateUserDto, TEST_UUID, TEST_USER_NAME, TEST_PRIMARY_EMAIL);

        // Then
        assertThat(updatedUser.userName()).isEqualTo(UPDATED_USER_NAME);
        assertThat(updatedUser.firstName()).isEqualTo(users.getFirstName());
        assertThat(updatedUser.lastName()).isEqualTo(users.getLastName());
        assertThat(updatedUser.primaryEmail()).isEqualTo(users.getPrimaryEmail());
        assertThat(updatedUser.secondaryEmail()).isEqualTo(users.getSecondaryEmail());
        assertThat(updatedUser.about()).isEqualTo(users.getAbout());
        assertThat(updatedUser.gender()).isEqualTo(users.getGender().toString());
    }

    @Test
    @DisplayName("Test Happy Path -- updateUserServiceByUserIdOrUserNameOrPrimaryEmail() With only primary email updated")
    public void testUpdateUserServiceByUserIdOrUserNameOrPrimaryEmailWithOnlyPrimaryEmailChanged() throws UserNotFoundExceptions, UserExceptions, BadApiRequestExceptions, IOException {
        // Given
        final String UPDATED_PRIMARY_EMAIL = "UPDATED_PRIMARY_EMAIL";
        Users users = constructUser();
        UpdateUserDto updatedUpdateUserDto = new UpdateUserDto.builder()
                .userId(users.getUserId())
                .userName(users.getUserName())
                .firstName(users.getFirstName())
                .lastName(users.getLastName())
                .primaryEmail(UPDATED_PRIMARY_EMAIL)
                .secondaryEmail(users.getSecondaryEmail())
                .gender(users.getGender().toString())
                .about(users.getAbout())
                .build();

        // When
        when(userRepositoryMock.findByUserIdOrUserNameOrPrimaryEmail(TEST_UUID, TEST_USER_NAME, TEST_PRIMARY_EMAIL)).thenReturn(Optional.of(users));
        when(userRepositoryMock.save(any())).thenReturn(UserUpdateDtoToUsers(updatedUpdateUserDto));
        doNothing().when(userValidationServiceMock).validatePZeroUserFields(anyString(), anyString(), anyString(), anyString(), any());
        doNothing().when(userValidationServiceMock).validateUser(any(), any(), anyString(), any());
        UserDto updatedUser = userServiceMock.updateUserServiceByUserIdOrUserNameOrPrimaryEmail(updatedUpdateUserDto, TEST_UUID, TEST_USER_NAME, TEST_PRIMARY_EMAIL);

        // Then
        assertThat(updatedUser.userName()).isEqualTo(users.getUserName());
        assertThat(updatedUser.firstName()).isEqualTo(users.getFirstName());
        assertThat(updatedUser.lastName()).isEqualTo(users.getLastName());
        assertThat(updatedUser.primaryEmail()).isEqualTo(UPDATED_PRIMARY_EMAIL);
        assertThat(updatedUser.secondaryEmail()).isEqualTo(users.getSecondaryEmail());
        assertThat(updatedUser.about()).isEqualTo(users.getAbout());
        assertThat(updatedUser.gender()).isEqualTo(users.getGender().toString());
    }

    @Test
    @DisplayName("Test Happy Path -- updateUserServiceByUserIdOrUserNameOrPrimaryEmail() With only secondary email updated")
    public void testUpdateUserServiceByUserIdOrUserNameOrPrimaryEmailWithOnlySecondaryEmailChanged() throws UserNotFoundExceptions, UserExceptions, BadApiRequestExceptions, IOException {
        // Given
        final String UPDATED_SECONDARY_EMAIL = "UPDATED_SECONDARY_EMAIL";
        Users users = constructUser();
        UpdateUserDto updatedUpdateUserDto = new UpdateUserDto.builder()
                .userId(users.getUserId())
                .userName(users.getUserName())
                .firstName(users.getFirstName())
                .lastName(users.getLastName())
                .primaryEmail(users.getPrimaryEmail())
                .secondaryEmail(UPDATED_SECONDARY_EMAIL)
                .gender(users.getGender().toString())
                .about(users.getAbout())
                .build();

        // When
        when(userRepositoryMock.findByUserIdOrUserNameOrPrimaryEmail(TEST_UUID, TEST_USER_NAME, TEST_PRIMARY_EMAIL)).thenReturn(Optional.of(users));
        when(userRepositoryMock.save(any())).thenReturn(UserUpdateDtoToUsers(updatedUpdateUserDto));
        doNothing().when(userValidationServiceMock).validatePZeroUserFields(anyString(), anyString(), anyString(), anyString(), any());
        doNothing().when(userValidationServiceMock).validateUser(any(), any(), anyString(), any());
        UserDto updatedUser = userServiceMock.updateUserServiceByUserIdOrUserNameOrPrimaryEmail(updatedUpdateUserDto, TEST_UUID, TEST_USER_NAME, TEST_PRIMARY_EMAIL);

        // Then
        assertThat(updatedUser.userName()).isEqualTo(users.getUserName());
        assertThat(updatedUser.firstName()).isEqualTo(users.getFirstName());
        assertThat(updatedUser.lastName()).isEqualTo(users.getLastName());
        assertThat(updatedUser.primaryEmail()).isEqualTo(users.getPrimaryEmail());
        assertThat(updatedUser.secondaryEmail()).isEqualTo(UPDATED_SECONDARY_EMAIL);
        assertThat(updatedUser.about()).isEqualTo(users.getAbout());
        assertThat(updatedUser.gender()).isEqualTo(users.getGender().toString());
    }

    @Test
    @DisplayName("Test Happy Path -- updateUserServiceByUserIdOrUserNameOrPrimaryEmail() With only firstName updated")
    public void testUpdateUserServiceByUserIdOrUserNameOrPrimaryEmailWithOnlyFirstNameChanged() throws UserNotFoundExceptions, UserExceptions, BadApiRequestExceptions, IOException {
        // Given
        final String UPDATED_FIRST_NAME = "UPDATED_FIRST_NAME";
        Users users = constructUser();
        UpdateUserDto updatedUpdateUserDto = new UpdateUserDto.builder()
                .userId(users.getUserId())
                .userName(users.getUserName())
                .firstName(UPDATED_FIRST_NAME)
                .lastName(users.getLastName())
                .primaryEmail(users.getPrimaryEmail())
                .secondaryEmail(users.getSecondaryEmail())
                .gender(users.getGender().toString())
                .about(users.getAbout())
                .build();

        // When
        when(userRepositoryMock.findByUserIdOrUserNameOrPrimaryEmail(TEST_UUID, TEST_USER_NAME, TEST_PRIMARY_EMAIL)).thenReturn(Optional.of(users));
        when(userRepositoryMock.save(any())).thenReturn(UserUpdateDtoToUsers(updatedUpdateUserDto));
        doNothing().when(userValidationServiceMock).validatePZeroUserFields(anyString(), anyString(), anyString(), anyString(), any());
        doNothing().when(userValidationServiceMock).validateUser(any(), any(), anyString(), any());
        UserDto updatedUser = userServiceMock.updateUserServiceByUserIdOrUserNameOrPrimaryEmail(updatedUpdateUserDto, TEST_UUID, TEST_USER_NAME, TEST_PRIMARY_EMAIL);

        // Then
        assertThat(updatedUser.userName()).isEqualTo(users.getUserName());
        assertThat(updatedUser.firstName()).isEqualTo(UPDATED_FIRST_NAME);
        assertThat(updatedUser.lastName()).isEqualTo(users.getLastName());
        assertThat(updatedUser.primaryEmail()).isEqualTo(users.getPrimaryEmail());
        assertThat(updatedUser.secondaryEmail()).isEqualTo(users.getSecondaryEmail());
        assertThat(updatedUser.about()).isEqualTo(users.getAbout());
        assertThat(updatedUser.gender()).isEqualTo(users.getGender().toString());
    }

    @Test
    @DisplayName("Test Happy Path -- updateUserServiceByUserIdOrUserNameOrPrimaryEmail() With only lastName updated")
    public void testUpdateUserServiceByUserIdOrUserNameOrPrimaryEmailWithOnlyLastNameChanged() throws BadApiRequestExceptions, UserNotFoundExceptions, UserExceptions, IOException {
        // Given
        final String UPDATED_LAST_NAME = "UPDATED_LAST_NAME";
        Users users = constructUser();
        UpdateUserDto updatedUpdateUserDto = new UpdateUserDto.builder()
                .userId(users.getUserId())
                .userName(users.getUserName())
                .firstName(users.getFirstName())
                .lastName(UPDATED_LAST_NAME)
                .primaryEmail(users.getPrimaryEmail())
                .secondaryEmail(users.getSecondaryEmail())
                .gender(users.getGender().toString())
                .about(users.getAbout())
                .build();

        // When
        when(userRepositoryMock.findByUserIdOrUserNameOrPrimaryEmail(TEST_UUID, TEST_USER_NAME, TEST_PRIMARY_EMAIL)).thenReturn(Optional.of(users));
        when(userRepositoryMock.save(any())).thenReturn(UserUpdateDtoToUsers(updatedUpdateUserDto));
        doNothing().when(userValidationServiceMock).validatePZeroUserFields(anyString(), anyString(), anyString(), anyString(), any());
        doNothing().when(userValidationServiceMock).validateUser(any(), any(), anyString(), any());
        UserDto updatedUser = userServiceMock.updateUserServiceByUserIdOrUserNameOrPrimaryEmail(updatedUpdateUserDto, TEST_UUID, TEST_USER_NAME, TEST_PRIMARY_EMAIL);

        // Then
        assertThat(updatedUser.userName()).isEqualTo(users.getUserName());
        assertThat(updatedUser.firstName()).isEqualTo(users.getFirstName());
        assertThat(updatedUser.lastName()).isEqualTo(UPDATED_LAST_NAME);
        assertThat(updatedUser.primaryEmail()).isEqualTo(users.getPrimaryEmail());
        assertThat(updatedUser.secondaryEmail()).isEqualTo(users.getSecondaryEmail());
        assertThat(updatedUser.about()).isEqualTo(users.getAbout());
        assertThat(updatedUser.gender()).isEqualTo(users.getGender().toString());
    }


    @Test
    @DisplayName("Test Happy Path -- updateUserServiceByUserIdOrUserNameOrPrimaryEmail() With only gender updated")
    public void testUpdateUserServiceByUserIdOrUserNameOrPrimaryEmailWithOnlyGenderChanged() throws BadApiRequestExceptions, UserNotFoundExceptions, UserExceptions, IOException {
        // Given
        final String UPDATED_GENDER = "FEMALE";
        Users users = constructUser();
        UpdateUserDto updatedUpdateUserDto = new UpdateUserDto.builder()
                .userId(users.getUserId())
                .userName(users.getUserName())
                .firstName(users.getFirstName())
                .lastName(users.getLastName())
                .primaryEmail(users.getPrimaryEmail())
                .secondaryEmail(users.getSecondaryEmail())
                .gender(UPDATED_GENDER)
                .about(users.getAbout())
                .build();

        // When
        when(userRepositoryMock.findByUserIdOrUserNameOrPrimaryEmail(TEST_UUID, TEST_USER_NAME, TEST_PRIMARY_EMAIL)).thenReturn(Optional.of(users));
        when(userRepositoryMock.save(any())).thenReturn(UserUpdateDtoToUsers(updatedUpdateUserDto));
        doNothing().when(userValidationServiceMock).validatePZeroUserFields(anyString(), anyString(), anyString(), anyString(), any());
        doNothing().when(userValidationServiceMock).validateUser(any(), any(), anyString(), any());
        UserDto updatedUser = userServiceMock.updateUserServiceByUserIdOrUserNameOrPrimaryEmail(updatedUpdateUserDto, TEST_UUID, TEST_USER_NAME, TEST_PRIMARY_EMAIL);

        // Then
        assertThat(updatedUser.userName()).isEqualTo(users.getUserName());
        assertThat(updatedUser.firstName()).isEqualTo(users.getFirstName());
        assertThat(updatedUser.lastName()).isEqualTo(users.getLastName());
        assertThat(updatedUser.primaryEmail()).isEqualTo(users.getPrimaryEmail());
        assertThat(updatedUser.secondaryEmail()).isEqualTo(users.getSecondaryEmail());
        assertThat(updatedUser.about()).isEqualTo(users.getAbout());
        assertThat(updatedUser.gender()).isEqualTo(UPDATED_GENDER);
    }


    @Test
    @DisplayName("Test Happy Path -- updateUserServiceByUserIdOrUserNameOrPrimaryEmail() With only about updated")
    public void testUpdateUserServiceByUserIdOrUserNameOrPrimaryEmailWithOnlyAboutChanged() throws UserNotFoundExceptions, UserExceptions, BadApiRequestExceptions, IOException {
        // Given
        final String UPDATED_ABOUT = "UPDATED_ABOUT";
        Users users = constructUser();
        UpdateUserDto updatedUpdateUserDto = new UpdateUserDto.builder()
                .userId(users.getUserId())
                .userName(users.getUserName())
                .firstName(users.getFirstName())
                .lastName(users.getLastName())
                .primaryEmail(users.getPrimaryEmail())
                .secondaryEmail(users.getSecondaryEmail())
                .gender(users.getGender().toString())
                .about(UPDATED_ABOUT)
                .build();

        // When
        when(userRepositoryMock.findByUserIdOrUserNameOrPrimaryEmail(TEST_UUID, TEST_USER_NAME, TEST_PRIMARY_EMAIL)).thenReturn(Optional.of(users));
        when(userRepositoryMock.save(any())).thenReturn(UserUpdateDtoToUsers(updatedUpdateUserDto));
        doNothing().when(userValidationServiceMock).validatePZeroUserFields(anyString(), anyString(), anyString(), anyString(), any());
        doNothing().when(userValidationServiceMock).validateUser(any(), any(), anyString(), any());
        UserDto updatedUser = userServiceMock.updateUserServiceByUserIdOrUserNameOrPrimaryEmail(updatedUpdateUserDto, TEST_UUID, TEST_USER_NAME, TEST_PRIMARY_EMAIL);

        // Then
        assertThat(updatedUser.userName()).isEqualTo(users.getUserName());
        assertThat(updatedUser.firstName()).isEqualTo(users.getFirstName());
        assertThat(updatedUser.lastName()).isEqualTo(users.getLastName());
        assertThat(updatedUser.primaryEmail()).isEqualTo(users.getPrimaryEmail());
        assertThat(updatedUser.secondaryEmail()).isEqualTo(users.getSecondaryEmail());
        assertThat(updatedUser.about()).isEqualTo(UPDATED_ABOUT);
        assertThat(updatedUser.gender()).isEqualTo(users.getGender().toString());
    }


    @Test
    @DisplayName("Test Unhappy Path -- updateUserByUserIdOrUserName() With both UserId & UserName Null")
    public void testUpdateUserByUserIdOrUserNameUnhappyPathWithBothUserIdAndUserNameAndPrimaryEmailIsNull() throws BadApiRequestExceptions {
        // Given
        Users users = constructUser();

        // When
        doThrow(new BadApiRequestExceptions(BadApiRequestExceptions.class, "Please provide non null username or user Id", "testUpdateUserByUserIdOrUserNameUnhappyPathWithBothUserIdAndUserNameNull"))
                .when(userValidationServiceMock).validatePZeroUserFields(any(), any(), any(), anyString(), any());

        // Then
        assertThrows(BadApiRequestExceptions.class, () -> {
            userServiceMock.updateUserServiceByUserIdOrUserNameOrPrimaryEmail(UserToUpdateUserDto(users), null, null, null);
        }, "BadApiRequestException should have been thrown");
    }


    @Test
    @DisplayName("Test Unhappy Path -- updateUserServiceByUserIdOrUserNameOrPrimaryEmail() With UserId & UserName & Primary Email invalid or not present in db")
    public void testUpdateUserServiceByUserIdOrUserNameOrPrimaryEmailUnhappyPathForUserNotFound() throws BadApiRequestExceptions, UserNotFoundExceptions, UserExceptions, IOException {
        // Given
        Users users = constructUser();

        // When
        when(userRepositoryMock.findByUserIdOrUserNameOrPrimaryEmail("INVALID_USER_ID",
                "INVALID_USER_NAME", "INVALID_PRIMARY_EMAIL")).thenReturn(Optional.empty());
        doNothing().when(userValidationServiceMock).validatePZeroUserFields(anyString(), anyString(), anyString(), anyString(), any());
        doThrow(new UserNotFoundExceptions(UserNotFoundExceptions.class, "No User with this UserId or UserName"
                , "testUpdateUserByUserIdOrUserNameUnhappyPathForUserNotFound"))
                .when(userValidationServiceMock).validateUser(any(), any(), anyString(), any());

        // Then
        assertThrows(UserNotFoundExceptions.class, () -> {
            userServiceMock.updateUserServiceByUserIdOrUserNameOrPrimaryEmail(UserToUpdateUserDto(users),
                    "INVALID_USER_ID", "INVALID_USER_NAME", "INVALID_PRIMARY_EMAIL");
        }, "UserNotFoundException should have been thrown");
    }


    @Test
    @DisplayName("Test Unhappy Path -- updateUserServiceByUserIdOrUserNameOrPrimaryEmail() With primary email already existing")
    public void testUpdateUserServiceByUserIdOrUserNameOrPrimaryEmailUnhappyPathWithExistingEmail() throws UserNotFoundExceptions, UserExceptions, BadApiRequestExceptions, IOException {
        // Given
        Users users = constructUser();
        UpdateUserDto updatedUpdateUserDto = new UpdateUserDto.builder()
                .userId(users.getUserId())
                .userName(users.getUserName())
                .firstName(users.getFirstName())
                .lastName(users.getLastName())
                .primaryEmail("EXISTING_PRIMARY_EMAIL")
                .secondaryEmail(users.getSecondaryEmail())
                .gender(users.getGender().toString())
                .about(users.getAbout())
                .build();

        // When
        when(userRepositoryMock.findByUserIdOrUserNameOrPrimaryEmail(TEST_UUID, TEST_USER_NAME, TEST_PRIMARY_EMAIL))
                .thenReturn(Optional.of(users));
        doNothing().when(userValidationServiceMock).validatePZeroUserFields(anyString(), anyString(), anyString(), anyString(), any());
        doNothing().doThrow(new UserExceptions(UserExceptions.class,
                        String.format("There's an account with Email %s EMAIL", updatedUpdateUserDto.primaryEmail())
                        , "testUpdateUserByUserIdOrUserNameUnhappyPathWithExistingEmail"))
                .when(userValidationServiceMock).validateUser(any(), any(), anyString(), any());

        // Then
        assertThrows(UserExceptions.class, () -> {
            userServiceMock.updateUserServiceByUserIdOrUserNameOrPrimaryEmail(updatedUpdateUserDto, TEST_UUID, TEST_USER_NAME,TEST_PRIMARY_EMAIL);
        }, "UserExceptions Should have been thrown");
    }

    @Test
    @DisplayName("Test Happy Path -- deleteUserServiceByUserIdOrUserNameOrPrimaryEmail() With valid UserId & UserName & primary Email")
    public void testDeleteByUserIdOrUserNameOrPrimaryEmailHappyPath() throws BadApiRequestExceptions, UserNotFoundExceptions, UserExceptions, IOException {
        // Given
        Users users=constructUser();

        // When
        doNothing().when(userValidationServiceMock).validatePZeroUserFields(anyString(), anyString(), anyString(), anyString(), any());
        when(userRepositoryMock.findByUserIdOrUserNameOrPrimaryEmail(TEST_UUID, TEST_USER_NAME,TEST_PRIMARY_EMAIL))
                .thenReturn(Optional.of(users));
        doNothing().when(userValidationServiceMock).validateUser(any(), any(), anyString(), any());
        userServiceMock.deleteUserServiceByUserIdOrUserNameOrPrimaryEmail(TEST_UUID, TEST_USER_NAME,TEST_PRIMARY_EMAIL);
        Optional<Users> fetchedUser=userRepositoryMock.findByUserIdOrUserNameOrPrimaryEmail(TEST_UUID,TEST_USER_NAME,TEST_USER_NAME);

        // Then
        verify(userRepositoryMock, times(1)).deleteByUserIdOrUserNameOrPrimaryEmail(TEST_UUID, TEST_USER_NAME,TEST_PRIMARY_EMAIL);
        assertThat(fetchedUser.isEmpty()).isTrue();
    }
//
//    @Test
//    @DisplayName("Test Unhappy Path -- deleteUserByUserIdOrUserName() With null UserId & UserName")
//    public void testDeleteUserByUserIdOrUserNameUnhappyPathForNullUserIdAndUserName() throws BadApiRequestExceptions {
//        // When
//        doThrow(new BadApiRequestExceptions(BadApiRequestExceptions.class, "Please provide non null username or user Id",
//                "testDeleteUserByUserIdOrUserNameUnhappyPath()"))
//                .when(userValidationServiceMock).validateFields(any(), any(), any(), anyString(), any());
//
//        // Then
//        assertThrows(BadApiRequestExceptions.class, () -> {
//            userServiceMock.deleteUserByUserIdOrUserName(null, null);
//        }, "BadApiRequestException should have been thrown");
//    }
//
//    @Test
//    @DisplayName("Test Unhappy Path -- deleteUserByUserIdOrUserName() With InValid UserId & UserName")
//    public void testDeleteUserByUserIdOrUserNameUnhappyPathForInValidUserIdAndUserName() throws BadApiRequestExceptions, UserNotFoundExceptions, UserExceptions {
//        // When
//        when(userRepositoryMock.findByUserIdOrUserName("INVALID_USER_ID", "INVALID_USER_NAME")).thenReturn(Optional.empty());
//        doNothing().when(userValidationServiceMock).validateFields(anyString(), anyString(), any(), anyString(), any());
//        doThrow(new UserNotFoundExceptions(UserNotFoundExceptions.class, "No User with this UserId or UserName",
//                "testDeleteUserByUserIdOrUserNameUnhappyPathForInValidUserIdAndUserName()"))
//                .when(userValidationServiceMock).validateUser(any(), anyString(), any());
//
//        // Then
//        assertThrows(UserNotFoundExceptions.class, () -> {
//            userServiceMock.deleteUserByUserIdOrUserName("INVALID_USER_ID",
//                    "INVALID_USER_NAME");
//        }, "UserNotFoundException should have been thrown");
//    }
//
//    @Test
//    @DisplayName("Test Happy Path -- testGetALlUsersHappyPath() with Users Present")
//    public void testGetALlUsersHappyPath() throws UserNotFoundExceptions {
//        // When
//        when(userRepositoryMock.findAll()).thenReturn(constructUsersSet().stream().toList());
//        doNothing().when(userValidationServiceMock).validateUserList(anySet(), anyString(), any());
//        Set<UserDto> usersSet = userServiceMock.getALlUsers();
//
//        //Then
//        assertThat(usersSet.isEmpty()).isFalse();
//    }
//
//    @Test
//    @DisplayName("Test Unhappy Path -- testGetALlUsersHappyPath() with No Users Present")
//    public void testGetALlUsersUnhappyPath() throws UserNotFoundExceptions {
//        // When
//        when(userRepositoryMock.findAll()).thenReturn(new ArrayList<>());
//        doThrow(new UserNotFoundExceptions(UserNotFoundExceptions.class,
//                "Our Database have no Users", "testGetALlUsersHappyPath"))
//                .when(userValidationServiceMock).validateUserList(anySet(), anyString(), any());
//
//        //Then
//        assertThrows(UserNotFoundExceptions.class,
//                () -> {
//                    userServiceMock.getALlUsers();
//                },
//                "UserNotFound Exception Should Be thrown");
//    }
//
//    @Test
//    @DisplayName("Test Happy Path -- getUserInformationByEmailOrUserName() With Valid UserName & Email")
//    public void testGetUserInformationByEmailOrUserName() throws UserExceptions, BadApiRequestExceptions, UserNotFoundExceptions {
//        // Given
//        Users users = constructUser();
//
//        // When
//        doNothing().when(userValidationServiceMock).validateFields(any(), anyString(), anyString(), anyString(), any());
//        when(userRepositoryMock.findByEmailOrUserName(TEST_EMAIL, TEST_USER_NAME)).thenReturn(Optional.of(users));
//        doNothing().when(userValidationServiceMock).validateUser(any(), anyString(), any());
//        UserDto fetchedUser = userServiceMock.getUserInformationByEmailOrUserName(TEST_EMAIL, TEST_USER_NAME);
//
//        // Then
//        assertThat(fetchedUser.userName()).isEqualTo(users.getUserName());
//        assertThat(fetchedUser.userId()).isEqualTo(users.getUserId());
//        assertThat(fetchedUser.email()).isEqualTo(users.getEmail());
//        assertThat(fetchedUser.firstName()).isEqualTo(users.getFirstName());
//        assertThat(fetchedUser.lastName()).isEqualTo(users.getLastName());
//        assertThat(fetchedUser.gender()).isEqualTo(users.getGender());
//        assertThat(fetchedUser.about()).isEqualTo(users.getAbout());
//        assertThat(fetchedUser.profileImage()).isEqualTo(users.getProfileImage());
//    }
//
//    @Test
//    @DisplayName("Test Unhappy Path -- getUserInformationByEmailOrUserName() With null UserName & Email")
//    public void testGetUserInformationByEmailOrUserNameUnhappyPathWithNullValues() throws BadApiRequestExceptions {
//        // When
//        doThrow(new BadApiRequestExceptions(BadApiRequestExceptions.class,
//                "Please provide non null username or email",
//                "testGetUserInformationByEmailOrUserNameUnhappyPathWithNullValues"))
//                .when(userValidationServiceMock).validateFields(any(), any(), any(), anyString(), any());
//
//        assertThrows(BadApiRequestExceptions.class,
//                () -> {
//                    userServiceMock.getUserInformationByEmailOrUserName(null, null);
//                },
//                "BadApiRequestExceptions should have been thrown");
//    }
//
//    @Test
//    @DisplayName("Test Unhappy Path -- getUserInformationByEmailOrUserName() With Invalid UserName & Email")
//    public void testGetUserInformationByEmailOrUserNameUnhappyPathWithInvalidUserNameAndEmail() throws BadApiRequestExceptions, UserNotFoundExceptions, UserExceptions {
//        // When
//        doNothing().when(userValidationServiceMock).validateFields(any(), anyString(), anyString(), anyString(), any());
//        when(userRepositoryMock.findByEmailOrUserName("INVALID_EMAIL", "INVALID_USER_NAME")).thenReturn(Optional.empty());
//        doThrow(new UserNotFoundExceptions(UserNotFoundExceptions.class,
//                "No User with this email or UserName",
//                "testGetUserInformationByEmailOrUserNameUnhappyPathWithInvalidUserNameAndEmail"))
//                .when(userValidationServiceMock).validateUser(any(), anyString(), any());
//
//        assertThrows(UserNotFoundExceptions.class,
//                () -> {
//                    userServiceMock.getUserInformationByEmailOrUserName("INVALID_EMAIL", "INVALID_USER_NAME");
//                },
//                "UserNotFoundExceptions should have been thrown");
//    }
//
//    @Test
//    @DisplayName("Test Happy Path -- searchAllUsersByUserName() with valid matching userName present in DB")
//    public void testSearchAllUsersByUserNameHappyPath() throws UserNotFoundExceptions {
//        // Given
//        Set<Users> usersSet = constructUsersSet();
//
//        // When
//        when(userRepositoryMock.findAllByUserNameContaining(anyString())).thenReturn(Optional.of(usersSet));
//        doNothing().when(userValidationServiceMock).validateUserList(anySet(), anyString(), any());
//        Set<UserDto> userSet = userServiceMock.searchAllUsersByUserName(TEST_USER_NAME);
//
//        // Then
//        assertThat(userSet.isEmpty()).isFalse();
//    }
//
//    @Test
//    @DisplayName("Test Unhappy Path -- searchAllUsersByUserName() with no matching userName present in DB")
//    public void testSearchAllUsersByUserNameUnhappyPath() throws UserNotFoundExceptions {
//        // When
//        when(userRepositoryMock.findAllByUserNameContaining("NOT_AVAILABLE_USER_NAME")).thenReturn(Optional.of(new HashSet<>()));
//        doThrow(new UserNotFoundExceptions(UserNotFoundExceptions.class,
//                "Our Database have no Users With this UserName",
//                "testSearchAllUsersByUserNameUnhappyPath"))
//                .when(userValidationServiceMock).validateUserList(anySet(), anyString(), any());
//
//        // Then
//        assertThrows(UserNotFoundExceptions.class, () -> {
//            userServiceMock.searchAllUsersByUserName("NOT_AVAILABLE_USER_NAME");
//        }, "UserNotFoundExceptions should have been thrown");
//    }
//
//    @Test
//    @DisplayName("Test Happy Path -- searchUserByFieldAndValue() with valid email present int DB")
//    public void testSearchUserByFieldAndValueWhenFieldIsEmail() throws UserNotFoundExceptions {
//        // Given
//        Set<Users> usersSet = constructUsersSet();
//
//        // When
//        when(userRepositoryMock.searchUserByEmail(anyString())).thenReturn(Optional.of(usersSet));
//        doNothing().when(userValidationServiceMock).validateUserList(any(), anyString(), any());
//        Set<UserDto> usersSets = userServiceMock.searchUserByFieldAndValue(EMAIL, TEST_EMAIL);
//
//        // Then
//        assertThat(usersSets.isEmpty()).isFalse();
//        assertThat(usersSets.stream().toList().getFirst().email()).isEqualTo(TEST_EMAIL);
//    }
//
//    @Test
//    @DisplayName("Test Unhappy Path -- searchUserByFieldAndValue() with email not present int DB")
//    public void testSearchUserByFieldAndValueWhenFieldIsEmailUnhappyPath() throws UserNotFoundExceptions {
//        // When
//        when(userRepositoryMock.searchUserByEmail("INVALID_EMAIL")).thenReturn(Optional.of(new HashSet<>()));
//        doThrow(new UserNotFoundExceptions(UserNotFoundExceptions.class,
//                "Our Database have no Users With this email",
//                "testSearchUserByFieldAndValueWhenFieldIsEmailUnhappyPath"))
//                .when(userValidationServiceMock).validateUserList(any(), anyString(), any());
//
//
//        // Then
//        assertThrows(UserNotFoundExceptions.class, () -> {
//            userServiceMock.searchUserByFieldAndValue(EMAIL, "INVALID_EMAIL");
//        }, "UserNotFoundExceptions should have been thrown");
//    }
//
//    @Test
//    @DisplayName("Test Happy Path -- searchUserByFieldAndValue() with valid userName present in DB")
//    public void testSearchUserByFieldAndValueWhenFieldIsUserName() throws UserNotFoundExceptions {
//        // Given
//        Set<Users> usersSet = constructUsersSet();
//
//        // When
//        when(userRepositoryMock.searchUserByUserName(anyString())).thenReturn(Optional.of(usersSet));
//        doNothing().when(userValidationServiceMock).validateUserList(any(), anyString(), any());
//        Set<UserDto> usersSets = userServiceMock.searchUserByFieldAndValue(USER_NAME, TEST_USER_NAME);
//
//        // Then
//        assertThat(usersSets.isEmpty()).isFalse();
//        assertThat(usersSets.stream().toList().getFirst().userName()).isEqualTo(TEST_USER_NAME);
//    }
//
//    @Test
//    @DisplayName("Test Unhappy Path -- searchUserByFieldAndValue() with userName not present int DB")
//    public void testSearchUserByFieldAndValueWhenFieldIsUserNameUnhappyPath() throws UserNotFoundExceptions {
//        // When
//        when(userRepositoryMock.searchUserByUserName("INVALID_USER_NAME")).thenReturn(Optional.of(new HashSet<>()));
//        doThrow(new UserNotFoundExceptions(UserNotFoundExceptions.class,
//                "Our Database have no Users With this userName",
//                "testSearchUserByFieldAndValueWhenFieldIsUserNameUnhappyPath"))
//                .when(userValidationServiceMock).validateUserList(any(), anyString(), any());
//
//
//        // Then
//        assertThrows(UserNotFoundExceptions.class, () -> {
//            userServiceMock.searchUserByFieldAndValue(USER_NAME, "INVALID_USER_NAME");
//        }, "UserNotFoundExceptions should have been thrown");
//    }
//
//    @Test
//    @DisplayName("Test Happy Path -- searchUserByFieldAndValue() with valid FirstName present int DB")
//    public void testSearchUserByFieldAndValueWhenFieldIsFirstName() throws UserNotFoundExceptions {
//        // Given
//        Set<Users> usersSet = constructUsersSet();
//
//        // When
//        when(userRepositoryMock.searchUserByFirstName(anyString())).thenReturn(Optional.of(usersSet));
//        doNothing().when(userValidationServiceMock).validateUserList(any(), anyString(), any());
//        Set<UserDto> usersSets = userServiceMock.searchUserByFieldAndValue(FIRST_NAME, TEST_FIRST_NAME);
//
//        // Then
//        assertThat(usersSets.isEmpty()).isFalse();
//        assertThat(usersSets.stream().toList().getFirst().firstName()).isEqualTo(TEST_FIRST_NAME);
//    }
//
//    @Test
//    @DisplayName("Test Unhappy Path -- searchUserByFieldAndValue() with FirstName not present int DB")
//    public void testSearchUserByFieldAndValueWhenFieldIsFirstNameUnhappyPath() throws UserNotFoundExceptions {
//        // When
//        when(userRepositoryMock.searchUserByFirstName("INVALID_FIRST_NAME")).thenReturn(Optional.of(new HashSet<>()));
//        doThrow(new UserNotFoundExceptions(UserNotFoundExceptions.class,
//                "Our Database have no Users With this firstName",
//                "testSearchUserByFieldAndValueWhenFieldIsFirstNameUnhappyPath"))
//                .when(userValidationServiceMock).validateUserList(any(), anyString(), any());
//
//
//        // Then
//        assertThrows(UserNotFoundExceptions.class, () -> {
//            userServiceMock.searchUserByFieldAndValue(FIRST_NAME, "INVALID_FIRST_NAME");
//        }, "UserNotFoundExceptions should have been thrown");
//    }
//
//    @Test
//    @DisplayName("Test Happy Path -- searchUserByFieldAndValue() with valid LastName present int DB")
//    public void testSearchUserByFieldAndValueWhenFieldIsLastName() throws UserNotFoundExceptions {
//        // Given
//        Set<Users> usersSet = constructUsersSet();
//
//        // When
//        when(userRepositoryMock.searchUserByLastName(anyString())).thenReturn(Optional.of(usersSet));
//        doNothing().when(userValidationServiceMock).validateUserList(any(), anyString(), any());
//        Set<UserDto> usersSets = userServiceMock.searchUserByFieldAndValue(LAST_NAME, TEST_LAST_NAME);
//
//        // Then
//        assertThat(usersSets.isEmpty()).isFalse();
//        assertThat(usersSets.stream().toList().getFirst().lastName()).isEqualTo(TEST_LAST_NAME);
//    }
//
//    @Test
//    @DisplayName("Test Unhappy Path -- searchUserByFieldAndValue() with LastName not present int DB")
//    public void testSearchUserByFieldAndValueWhenFieldIsLastNameUnhappyPath() throws UserNotFoundExceptions {
//        // When
//        when(userRepositoryMock.searchUserByLastName("INVALID_LAST_NAME")).thenReturn(Optional.of(new HashSet<>()));
//        doThrow(new UserNotFoundExceptions(UserNotFoundExceptions.class,
//                "Our Database have no Users With this lastName",
//                "testSearchUserByFieldAndValueWhenFieldIsLastNameUnhappyPath"))
//                .when(userValidationServiceMock).validateUserList(any(), anyString(), any());
//
//        // Then
//        assertThrows(UserNotFoundExceptions.class, () -> {
//            userServiceMock.searchUserByFieldAndValue(LAST_NAME, "INVALID_LAST_NAME");
//        }, "UserNotFoundExceptions should have been thrown");
//    }
//
//
//    @Test
//    @DisplayName("Test Happy Path -- searchUserByFieldAndValue() with valid gender present int DB")
//    public void testSearchUserByFieldAndValueWhenFieldIsGender() throws UserNotFoundExceptions {
//        // Given
//        Set<Users> usersSet = constructUsersSet();
//
//        // When
//        when(userRepositoryMock.searchUserByGender(anyString())).thenReturn(Optional.of(usersSet));
//        doNothing().when(userValidationServiceMock).validateUserList(any(), anyString(), any());
//        Set<UserDto> usersSets = userServiceMock.searchUserByFieldAndValue(GENDER, TEST_GENDER.toString());
//
//        // Then
//        assertThat(usersSets.isEmpty()).isFalse();
//        assertThat(usersSets.stream().toList().getFirst().gender()).isEqualTo(TEST_GENDER);
//    }
//
//    @Test
//    @DisplayName("Test Unhappy Path -- searchUserByFieldAndValue() with gender not present int DB")
//    public void testSearchUserByFieldAndValueWhenFieldIsGenderUnhappyPath() throws UserNotFoundExceptions {
//        // When
//        when(userRepositoryMock.searchUserByGender(String.valueOf(FEMALE))).thenReturn(Optional.of(new HashSet<>()));
//        doThrow(new UserNotFoundExceptions(UserNotFoundExceptions.class,
//                "Our Database have no Users With this gender",
//                "testSearchUserByFieldAndValueWhenFieldIsGenderUnhappyPath"))
//                .when(userValidationServiceMock).validateUserList(any(), anyString(), any());
//
//        // Then
//        assertThrows(UserNotFoundExceptions.class, () -> {
//            userServiceMock.searchUserByFieldAndValue(GENDER, String.valueOf(FEMALE));
//        }, "UserNotFoundExceptions should have been thrown");
//    }

    private Set<Users> constructUsersSet() {
        return Set.of(constructUser(), constructUser());
    }

    private UpdateUserDto constructIncomingUpdateUserDtoRequest() {
        final String UPDATED_USER_NAME = "UPDATED_USER_NAME";
        final String UPDATED_USER_ID = "144d02e1-49ab-417e-8279-ed08c997aed7";
        final String UPDATED_FIRST_NAME = "UPDATED_FIRST_NAME";
        final String UPDATED_LAST_NAME = "UPDATED_LAST_NAME";
        final String UPDATED_PRIMARY_EMAIL = "UPDATED_PRIMARY_EMAIL";
        final String UPDATED_SECONDARY_EMAIL = "UPDATED_SECONDARY_EMAIL";
        final String UPDATED_ABOUT = "80000k ke jootey, ismein tera ghar chala jayenga";

        return new UpdateUserDto.builder()
                .userId(UPDATED_USER_ID)
                .userName(UPDATED_USER_NAME)
                .firstName(UPDATED_FIRST_NAME)
                .lastName(UPDATED_LAST_NAME)
                .primaryEmail(UPDATED_PRIMARY_EMAIL)
                .secondaryEmail(UPDATED_SECONDARY_EMAIL)
                .gender(NON_BINARY.toString())
                .about(UPDATED_ABOUT)
                .build();
    }

    private UserDto constructIncomingUserDtoRequest() {
        final String NEW_USER_NAME = "NEW_USER_NAME";
        final String NEW_USER_ID = "144d02e1-49ab-417e-8279-ed08c997aed7";
        final String NEW_FIRST_NAME = "NEW_FIRST_NAME";
        final String NEW_LAST_NAME = "NEW_LAST_NAME";
        final String NEW_PRIMARY_EMAIL = "NEW_PRIMARY_EMAIL";
        final String NEW_SECONDARY_EMAIL = "NEW_SECONDARY_EMAIL";
        final String NEW_PASSWORD = "$2y$10$7rhYG4E.z8LbSy.hLN7ER.rFX/0y/9OLk/n2FPcBi5bI9//5A1JKy";
        final String NEW_PROFILE_IMAGE = "144d02e1-49ab-417e-8279-ed08c997aed7.jpg";
        final String NEW_ABOUT = "80000k ke jootey, ismein tera ghar chala jayenga";

        return new UserDto.builder()
                .userId(NEW_USER_ID)
                .userName(NEW_USER_NAME)
                .firstName(NEW_FIRST_NAME)
                .lastName(NEW_LAST_NAME)
                .primaryEmail(NEW_PRIMARY_EMAIL)
                .secondaryEmail(NEW_SECONDARY_EMAIL)
                .gender(FEMALE.toString())
                .password(NEW_PASSWORD)
                .about(NEW_ABOUT)
                .profileImage(NEW_PROFILE_IMAGE)
                .lastSeen(null)
                .build();
    }

    private Users constructUser() {
        return new Users.builder()
                .userId(TEST_UUID)
                .userName(TEST_USER_NAME)
                .firstName(TEST_FIRST_NAME)
                .lastName(TEST_LAST_NAME)
                .primaryEmail(TEST_PRIMARY_EMAIL)
                .secondaryEmail(TEST_SECONDARY_EMAIL)
                .password(TEST_PASSWORD)
                .gender(TEST_GENDER)
                .profileImage(TEST_PROFILE_IMAGE)
                .about(TEST_ABOUT)
                .lastSeen(TEST_LAST_SEEN)
                .createdDate(LocalDate.now().minusDays(10))
                .createdBy(ADMIN)
                .build();
    }
}
