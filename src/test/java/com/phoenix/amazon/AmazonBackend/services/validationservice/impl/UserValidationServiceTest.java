package com.phoenix.amazon.AmazonBackend.services.validationservice.impl;

import com.phoenix.amazon.AmazonBackend.entity.PassWordSet;
import com.phoenix.amazon.AmazonBackend.entity.Users;
import com.phoenix.amazon.AmazonBackend.exceptions.BadApiRequestExceptions;
import com.phoenix.amazon.AmazonBackend.exceptions.UserExceptions;
import com.phoenix.amazon.AmazonBackend.exceptions.UserNotFoundExceptions;
import com.phoenix.amazon.AmazonBackend.repository.IUserRepository;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static com.phoenix.amazon.AmazonBackend.helpers.AllConstantHelpers.GENDER;
import static com.phoenix.amazon.AmazonBackend.helpers.AllConstantHelpers.GENDER.FEMALE;
import static com.phoenix.amazon.AmazonBackend.helpers.AllConstantHelpers.GENDER.MALE;
import static com.phoenix.amazon.AmazonBackend.helpers.AllConstantHelpers.USER_VALIDATION.CREATE_USER;
import static com.phoenix.amazon.AmazonBackend.helpers.AllConstantHelpers.USER_VALIDATION.GET_USER_INFO_BY_USERID_USER_NAME_PRIMARY_EMAIL;
import static com.phoenix.amazon.AmazonBackend.helpers.AllConstantHelpers.USER_VALIDATION.NULL_OBJECT;
import static com.phoenix.amazon.AmazonBackend.helpers.AllConstantHelpers.USER_VALIDATION.UPDATE_PASSWORD;
import static com.phoenix.amazon.AmazonBackend.helpers.AllConstantHelpers.USER_VALIDATION.UPDATE_PRIMARY_EMAIL;
import static com.phoenix.amazon.AmazonBackend.helpers.AllConstantHelpers.USER_VALIDATION.UPDATE_PROFILE_IMAGE;
import static com.phoenix.amazon.AmazonBackend.helpers.AllConstantHelpers.USER_VALIDATION.UPDATE_SECONDARY_EMAIL;
import static com.phoenix.amazon.AmazonBackend.helpers.AllConstantHelpers.USER_VALIDATION.UPDATE_USERNAME;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
public class UserValidationServiceTest {
    @MockBean
    private UserValidationServiceImpl userValidationServiceMock;
    @Mock
    private IUserRepository userRepositoryMock;

    @Value("${path.services.user.image.properties}")
    String PATH_TO_PROPS;

    @BeforeEach
    public void setUp() {
        userValidationServiceMock = new UserValidationServiceImpl(userRepositoryMock, PATH_TO_PROPS);
    }

    @Test
    @DisplayName("Test Happy Path -- validateUser() For Null Object")
    public void testValidateUserHappyPathForNullObject() {
        // Given
        Users oldUser = constructOldUser();
        Users newUser = constructNewUser();
        Set<Users> setOfUsers = constructUserSet();

        // When
        when(userRepositoryMock.findAll()).thenReturn(setOfUsers.stream().toList());

        // Then
        assertDoesNotThrow(() -> {
            userValidationServiceMock
                    .validateUser(Optional.of(newUser), Optional.of(oldUser), "testValidateUser", NULL_OBJECT);
        });
    }

    @Test
    @DisplayName("Test Unhappy Path -- validateUser() For Null Object")
    public void testValidateUserUnhappyPathForNullObject() {
        // Given
        Users oldUser = constructOldUser();
        Set<Users> usersSet = constructUserSet();

        // When
        when(userRepositoryMock.findAll()).thenReturn(usersSet.stream().toList());

        // Then
        assertThrows(BadApiRequestExceptions.class, () -> {
            userValidationServiceMock.validateUser(Optional.empty(), Optional.of(oldUser), "testValidateUser", NULL_OBJECT);
        }, "BadApiRequestException should have been thrown");
    }

    @Test
    @DisplayName("Test Happy Path -- validateUser() For Create User")
    public void testValidateUserHappyPathForCreateUser() {
        // Given
        Users oldUser = constructOldUser();
        Users newUser = constructNewUser();
        Set<Users> setOfUsers = constructUserSet();

        // When
        when(userRepositoryMock.findAll()).thenReturn(setOfUsers.stream().toList());

        // Then
        assertDoesNotThrow(() -> {
            userValidationServiceMock
                    .validateUser(Optional.of(newUser), Optional.of(oldUser), "testValidateUser", CREATE_USER);
        });
    }

    @Test
    @DisplayName("Test UnHappy Path -- validateUser() For Create User with Null UserName")
    public void testValidateUserHappyPathForCreateUserNullUserName() {
        // Given
        Users oldUser = constructOldUser();
        Users newUser = new Users.builder()
                .userName(null)
                .build();
        Set<Users> setOfUsers = constructUserSet();

        // When
        when(userRepositoryMock.findAll()).thenReturn(setOfUsers.stream().toList());

        // Then
        assertThrows(BadApiRequestExceptions.class, () -> {
            userValidationServiceMock
                    .validateUser(Optional.of(newUser), Optional.of(oldUser), "testValidateUser", CREATE_USER);
        }, "BadApiRequestExceptions should have been thrown");
    }

    @Test
    @DisplayName("Test UnHappy Path -- validateUser() For Create User with Null PrimaryEmail")
    public void testValidateUserHappyPathForCreateUserNullPrimaryEmail() {
        // Given
        Users oldUser = constructOldUser();
        Users newUser = new Users.builder()
                .userName(oldUser.getUserName())
                .primaryEmail(null)
                .build();
        Set<Users> setOfUsers = constructUserSet();

        // When
        when(userRepositoryMock.findAll()).thenReturn(setOfUsers.stream().toList());

        // Then
        assertThrows(BadApiRequestExceptions.class, () -> {
            userValidationServiceMock
                    .validateUser(Optional.of(newUser), Optional.of(oldUser), "testValidateUser", CREATE_USER);
        }, "BadApiRequestExceptions should have been thrown");
    }

    @Test
    @DisplayName("Test UnHappy Path -- validateUser() For Create User with Null FirstName")
    public void testValidateUserHappyPathForCreateUserNullFirstName() {
        // Given
        Users oldUser = constructOldUser();
        Users newUser = new Users.builder()
                .userName(oldUser.getUserName())
                .primaryEmail(oldUser.getPrimaryEmail())
                .firstName(null)
                .build();
        Set<Users> setOfUsers = constructUserSet();

        // When
        when(userRepositoryMock.findAll()).thenReturn(setOfUsers.stream().toList());

        // Then
        assertThrows(BadApiRequestExceptions.class, () -> {
            userValidationServiceMock
                    .validateUser(Optional.of(newUser), Optional.of(oldUser), "testValidateUser", CREATE_USER);
        }, "BadApiRequestExceptions should have been thrown");
    }

    @Test
    @DisplayName("Test UnHappy Path -- validateUser() For Create User with Null LastName")
    public void testValidateUserHappyPathForCreateUserNullLastName() {
        // Given
        Users oldUser = constructOldUser();
        Users newUser = new Users.builder()
                .userName(oldUser.getUserName())
                .primaryEmail(oldUser.getPrimaryEmail())
                .firstName(oldUser.getFirstName())
                .lastName(null)
                .build();
        Set<Users> setOfUsers = constructUserSet();

        // When
        when(userRepositoryMock.findAll()).thenReturn(setOfUsers.stream().toList());

        // Then
        assertThrows(BadApiRequestExceptions.class, () -> {
            userValidationServiceMock
                    .validateUser(Optional.of(newUser), Optional.of(oldUser), "testValidateUser", CREATE_USER);
        }, "BadApiRequestExceptions should have been thrown");
    }

    @Test
    @DisplayName("Test UnHappy Path -- validateUser() For Create User with Null Gender")
    public void testValidateUserHappyPathForCreateUserNullGender() {
        // Given
        Users oldUser = constructOldUser();
        Users newUser = new Users.builder()
                .userName(oldUser.getUserName())
                .primaryEmail(oldUser.getPrimaryEmail())
                .firstName(oldUser.getFirstName())
                .lastName(oldUser.getLastName())
                .gender(null)
                .build();
        Set<Users> setOfUsers = constructUserSet();

        // When
        when(userRepositoryMock.findAll()).thenReturn(setOfUsers.stream().toList());

        // Then
        assertThrows(BadApiRequestExceptions.class, () -> {
            userValidationServiceMock
                    .validateUser(Optional.of(newUser), Optional.of(oldUser), "testValidateUser", CREATE_USER);
        }, "BadApiRequestExceptions should have been thrown");
    }

    @Test
    @DisplayName("Test UnHappy Path -- validateUser() For Create User with Existing Primary Emails")
    public void testValidateUserHappyPathForCreateUserPrimaryEmailExisting() {
        // Given
        Users oldUser = constructOldUser();
        Users newUser = constructNewUser();
        Users requestedUser = new Users.builder()
                .primaryEmail(oldUser.getPrimaryEmail())
                .userName(newUser.getUserName())
                .userId(newUser.getUserId())
                .secondaryEmail(newUser.getSecondaryEmail())
                .firstName(newUser.getFirstName())
                .lastName(newUser.getLastName())
                .about(newUser.getAbout())
                .gender(newUser.getGender())
                .build();
        Set<Users> setOfUsers = constructUserSet();

        // When
        when(userRepositoryMock.findAll()).thenReturn(setOfUsers.stream().toList());

        // Then
        assertThrows(UserExceptions.class, () -> {
            userValidationServiceMock
                    .validateUser(Optional.of(requestedUser), Optional.of(oldUser), "testValidateUser", CREATE_USER);
        }, "UserExceptions should have been thrown");
    }

    @Test
    @DisplayName("Test UnHappy Path -- validateUser() For Create User with Existing Primary in Someones Secondary Emails")
    public void testValidateUserHappyPathForCreateUserPrimaryEmailExistingInSecondary() {
        // Given
        Users oldUser = constructOldUser();
        Users newUser = constructNewUser();
        Users requestedUser = new Users.builder()
                .primaryEmail(oldUser.getSecondaryEmail())
                .secondaryEmail(newUser.getSecondaryEmail())
                .userName(newUser.getUserName())
                .userId(newUser.getUserId())
                .firstName(newUser.getFirstName())
                .lastName(newUser.getLastName())
                .about(newUser.getAbout())
                .gender(newUser.getGender())
                .build();
        Set<Users> setOfUsers = constructUserSet();

        // When
        when(userRepositoryMock.findAll()).thenReturn(setOfUsers.stream().toList());

        // Then
        assertThrows(UserExceptions.class, () -> {
            userValidationServiceMock
                    .validateUser(Optional.of(requestedUser), Optional.of(oldUser), "testValidateUser", CREATE_USER);
        }, "UserExceptions should have been thrown");
    }

    @Test
    @DisplayName("Test UnHappy Path -- validateUser() For Create User with Existing Secondary Email")
    public void testValidateUserHappyPathForCreateUserSecondaryEmailExisting() {
        // Given
        Users oldUser = constructOldUser();
        Users newUser = constructNewUser();
        Users requestedUser = new Users.builder()
                .secondaryEmail(oldUser.getSecondaryEmail())
                .userId(newUser.getUserId())
                .userName(newUser.getUserName())
                .primaryEmail(newUser.getPrimaryEmail())
                .firstName(newUser.getFirstName())
                .lastName(newUser.getLastName())
                .about(newUser.getAbout())
                .gender(newUser.getGender())
                .build();
        Set<Users> setOfUsers = constructUserSet();

        // When
        when(userRepositoryMock.findAll()).thenReturn(setOfUsers.stream().toList());

        // Then
        assertThrows(UserExceptions.class, () -> {
            userValidationServiceMock
                    .validateUser(Optional.of(requestedUser), Optional.of(oldUser), "testValidateUser", CREATE_USER);
        }, "UserExceptions should have been thrown");
    }

    @Test
    @DisplayName("Test UnHappy Path -- validateUser() For Create User with Existing Secondary Email In Someone Primary")
    public void testValidateUserHappyPathForCreateUserSecondaryEmailExistingInPrimary() {
        // Given
        Users oldUser = constructOldUser();
        Users newUser = constructNewUser();
        Users requestedUser = new Users.builder()
                .secondaryEmail(oldUser.getPrimaryEmail())
                .userId(newUser.getUserId())
                .userName(newUser.getUserName())
                .primaryEmail(newUser.getPrimaryEmail())
                .firstName(newUser.getFirstName())
                .lastName(newUser.getLastName())
                .about(newUser.getAbout())
                .gender(newUser.getGender())
                .build();
        Set<Users> setOfUsers = constructUserSet();

        // When
        when(userRepositoryMock.findAll()).thenReturn(setOfUsers.stream().toList());

        // Then
        assertThrows(UserExceptions.class, () -> {
            userValidationServiceMock
                    .validateUser(Optional.of(requestedUser), Optional.of(oldUser), "testValidateUser", CREATE_USER);
        }, "UserExceptions should have been thrown");
    }

    @Test
    @DisplayName("Test UnHappy Path -- validateUser() For Create User with Primary & Secondary Email Same")
    public void testValidateUserHappyPathForCreateUserPrimaryAndSecondaryEmailSame() {
        // Given
        Users oldUser = constructOldUser();
        Users newUser = constructNewUser();
        Users requestedUser = new Users.builder()
                .primaryEmail(newUser.getPrimaryEmail())
                .secondaryEmail(newUser.getPrimaryEmail())
                .userId(newUser.getUserId())
                .userName(newUser.getUserName())
                .firstName(newUser.getFirstName())
                .lastName(newUser.getLastName())
                .about(newUser.getAbout())
                .gender(newUser.getGender())
                .build();
        Set<Users> setOfUsers = constructUserSet();

        // When
        when(userRepositoryMock.findAll()).thenReturn(setOfUsers.stream().toList());

        // Then
        assertThrows(UserExceptions.class, () -> {
            userValidationServiceMock
                    .validateUser(Optional.of(requestedUser), Optional.of(oldUser), "testValidateUser", CREATE_USER);
        }, "UserExceptions should have been thrown");
    }

    @Test
    @DisplayName("Test UnHappy Path -- validateUser() For Create User with Existing UserName")
    public void testValidateUserHappyPathForCreateUserUserNameExisting() {
        // Given
        Users oldUser = constructOldUser();
        Users newUser = constructNewUser();
        Users requestedUser = new Users.builder()
                .userName(oldUser.getUserName())
                .userId(newUser.getUserId())
                .primaryEmail(newUser.getPrimaryEmail())
                .secondaryEmail(newUser.getSecondaryEmail())
                .firstName(newUser.getFirstName())
                .lastName(newUser.getLastName())
                .about(newUser.getAbout())
                .gender(newUser.getGender())
                .build();
        Set<Users> setOfUsers = constructUserSet();

        // When
        when(userRepositoryMock.findAll()).thenReturn(setOfUsers.stream().toList());

        // Then
        assertThrows(UserExceptions.class, () -> {
            userValidationServiceMock
                    .validateUser(Optional.of(requestedUser), Optional.of(oldUser), "testValidateUser", CREATE_USER);
        }, "UserExceptions should have been thrown");
    }

    @Test
    @DisplayName("Test UnHappy Path -- validateUser() For Create User with Existing UserId")
    public void testValidateUserHappyPathForCreateUserUserIdExisting() {
        // Given
        Users oldUser = constructOldUser();
        Users newUser = constructNewUser();
        Users requestedUser = new Users.builder()
                .userId(oldUser.getUserId())
                .userName(newUser.getUserName())
                .primaryEmail(newUser.getPrimaryEmail())
                .secondaryEmail(newUser.getSecondaryEmail())
                .firstName(newUser.getFirstName())
                .lastName(newUser.getLastName())
                .about(newUser.getAbout())
                .gender(newUser.getGender())
                .build();
        Set<Users> setOfUsers = constructUserSet();

        // When
        when(userRepositoryMock.findAll()).thenReturn(setOfUsers.stream().toList());

        // Then
        assertThrows(UserExceptions.class, () -> {
            userValidationServiceMock
                    .validateUser(Optional.of(requestedUser), Optional.of(oldUser), "testValidateUser", CREATE_USER);
        }, "UserExceptions should have been thrown");
    }

    @Test
    @DisplayName("Test Happy Path -- validateUser() For Update UserName")
    public void testValidateUserHappyPathForUpdateUserNameExisting() {
        // Given
        Users oldUser = constructOldUser();
        Users newUser = constructNewUser();
        Set<Users> setOfUsers = constructUserSet();

        // When
        when(userRepositoryMock.findAll()).thenReturn(setOfUsers.stream().toList());

        // Then
        assertDoesNotThrow(() -> {
            userValidationServiceMock
                    .validateUser(Optional.of(newUser), Optional.of(oldUser), "testValidateUser", UPDATE_USERNAME);
        }, "UserExceptions should have been thrown");
    }

    @Test
    @DisplayName("Test UnHappy Path -- validateUser() For Update UserName with Existing UserName")
    public void testValidateUserHappyPathForUpdateUserNameWithUserNameExisting() {
        // Given
        Users oldUser = constructOldUser();
        Users newUser = new Users.builder().userName(oldUser.getUserName()).build();
        Set<Users> setOfUsers = constructUserSet();

        // When
        when(userRepositoryMock.findAll()).thenReturn(setOfUsers.stream().toList());

        // Then
        assertThrows(UserExceptions.class, () -> {
            userValidationServiceMock
                    .validateUser(Optional.of(newUser), Optional.of(oldUser), "testValidateUser", UPDATE_USERNAME);
        }, "UserExceptions should have been thrown");
    }

    @Test
    @DisplayName("Test Happy Path -- validateUser() For Get User with valid userId,userName,primaryEmail")
    public void testValidateUserHappyPathForGetUserIdUserNamePrimaryEmail() {
        // Given
        Users oldUser = constructOldUser();
        Users newUser = constructNewUser();
        Set<Users> setOfUsers = constructUserSet();

        // When
        when(userRepositoryMock.findAll()).thenReturn(setOfUsers.stream().toList());

        // Then
        assertDoesNotThrow(() -> {
            userValidationServiceMock
                    .validateUser(Optional.of(newUser), Optional.of(oldUser), "testValidateUser", GET_USER_INFO_BY_USERID_USER_NAME_PRIMARY_EMAIL);
        }, "UserExceptions should have been thrown");
    }

    @Test
    @DisplayName("Test UnHappy Path -- validateUser() For Get User with invalid userId,userName,primaryEmail")
    public void testValidateUserHappyPathForGetUserWithInvalidUserIdUserNamePrimaryEmail() {
        // Given
        Users newUser = constructNewUser();
        Set<Users> setOfUsers = constructUserSet();

        // When
        when(userRepositoryMock.findAll()).thenReturn(setOfUsers.stream().toList());

        // Then
        assertThrows(UserNotFoundExceptions.class, () -> {
            userValidationServiceMock
                    .validateUser(Optional.of(newUser), Optional.empty(), "testValidateUser", GET_USER_INFO_BY_USERID_USER_NAME_PRIMARY_EMAIL);
        }, "UserNotFoundExceptions should have been thrown");
    }

    @Test
    @DisplayName("Test UnHappy Path -- validateUser() For Update Primary Email with Existing Primary Emails")
    public void testValidateUserHappyPathForUpdatePrimaryEmailUnhappy() {
        // Given
        Users oldUser = constructOldUser();
        Users newUser = new Users.builder().primaryEmail(oldUser.getPrimaryEmail()).build();
        Set<Users> setOfUsers = constructUserSet();

        // When
        when(userRepositoryMock.findAll()).thenReturn(setOfUsers.stream().toList());

        // Then
        assertThrows(UserExceptions.class, () -> {
            userValidationServiceMock
                    .validateUser(Optional.of(newUser), Optional.of(oldUser), "testValidateUser", UPDATE_PRIMARY_EMAIL);
        }, "UserExceptions should have been thrown");
    }

    @Test
    @DisplayName("Test Happy Path -- validateUser() For Update Primary Email with not Existing Primary Emails")
    public void testValidateUserHappyPathForUpdatePrimaryEmailHappyPath() {
        // Given
        Users oldUser = constructOldUser();
        Users newUser = constructNewUser();
        Set<Users> setOfUsers = constructUserSet();

        // When
        when(userRepositoryMock.findAll()).thenReturn(setOfUsers.stream().toList());

        // Then
        assertDoesNotThrow(() -> {
            userValidationServiceMock
                    .validateUser(Optional.of(newUser), Optional.of(oldUser), "testValidateUser", UPDATE_PRIMARY_EMAIL);
        }, "UserExceptions should have been thrown");
    }

    @Test
    @DisplayName("Test UnHappy Path -- validateUser() For Update Secondary Email with Existing Secondary Emails")
    public void testValidateUserHappyPathForUpdateSecondaryEmail() {
        // Given
        Users oldUser = constructOldUser();
        Users newUser = new Users.builder().secondaryEmail(oldUser.getSecondaryEmail()).build();
        Set<Users> setOfUsers = constructUserSet();

        // When
        when(userRepositoryMock.findAll()).thenReturn(setOfUsers.stream().toList());

        // Then
        assertThrows(UserExceptions.class, () -> {
            userValidationServiceMock
                    .validateUser(Optional.of(newUser), Optional.of(oldUser), "testValidateUser", UPDATE_SECONDARY_EMAIL);
        }, "UserExceptions should have been thrown");
    }

    @Test
    @DisplayName("Test Happy Path -- validateUser() For Update Secondary Email with not Existing Secondary Emails")
    public void testValidateUserHappyPathForUpdateSecondaryEmailHappyPath() {
        // Given
        Users oldUser = constructOldUser();
        Users newUser = constructNewUser();
        Set<Users> setOfUsers = constructUserSet();

        // When
        when(userRepositoryMock.findAll()).thenReturn(setOfUsers.stream().toList());

        // Then
        assertDoesNotThrow(() -> {
            userValidationServiceMock
                    .validateUser(Optional.of(newUser), Optional.of(oldUser), "testValidateUser", UPDATE_SECONDARY_EMAIL);
        }, "UserExceptions should have been thrown");
    }

    @Test
    @DisplayName("Test Happy Path -- validateUser() For Update Secondary Email with not Existing Primary Emails")
    public void testValidateUserHappyPathForUpdatePrimaryEmailUnHappyPath() {
        // Given
        Users oldUser = constructOldUser();
        Users newUser = new Users
                .builder()
                .password(oldUser.getPassword())
                .build();
        Set<Users> setOfUsers = constructUserSet();

        // When
        when(userRepositoryMock.findAll()).thenReturn(setOfUsers.stream().toList());

        // Then
        assertThrows(UserExceptions.class, () -> {
            userValidationServiceMock
                    .validateUser(Optional.of(newUser), Optional.of(oldUser), "testValidateUser", UPDATE_PASSWORD);
        }, "UserExceptions should have been thrown");
    }

    @Test
    @DisplayName("Test Happy Path -- validateUser() For Update Password with not existing prev password")
    public void testValidateUserHappyPathForUpdatePasswordHappyPath() {
        // Given
        Users oldUser = constructOldUser();
        Users newUser = constructNewUser();
        Set<Users> setOfUsers = constructUserSet();

        // When
        when(userRepositoryMock.findAll()).thenReturn(setOfUsers.stream().toList());

        // Then
        assertDoesNotThrow(() -> {
            userValidationServiceMock
                    .validateUser(Optional.of(newUser), Optional.of(oldUser), "testValidateUser", UPDATE_PASSWORD);
        }, "UserExceptions should have been thrown");
    }

    @Test
    @DisplayName("Test Happy Path -- validateUser() For Update Password")
    public void testValidateUserHappyPathForUpdatePassword() {
        // Given
        Users oldUser = constructOldUser();
        Users newUser = constructNewUser();
        Set<Users> setOfUsers = constructUserSet();

        // When
        when(userRepositoryMock.findAll()).thenReturn(setOfUsers.stream().toList());

        // Then
        assertDoesNotThrow(() -> {
            userValidationServiceMock
                    .validateUser(Optional.of(newUser), Optional.of(oldUser), "testValidateUser", UPDATE_PASSWORD);
        }, "UserExceptions should have been thrown");
    }

    @Test
    @DisplayName("Test Happy Path -- validateUser() For Update profile image")
    public void testValidateUserHappyPathForUpdateProfileImage() {
        // Given
        Users oldUser = constructOldUser();
        Users newUser = new Users.builder()
                .profileImage("TestImage.png")
                .build();
        Set<Users> setOfUsers = constructUserSet();

        // When
        when(userRepositoryMock.findAll()).thenReturn(setOfUsers.stream().toList());

        // Then
        assertDoesNotThrow(() -> {
            userValidationServiceMock
                    .validateUser(Optional.of(newUser), Optional.of(oldUser), "testValidateUser", UPDATE_PROFILE_IMAGE);
        }, "UserExceptions should have been thrown");
    }

    @Test
    @DisplayName("Test Happy Path -- validateUser() For Update ProfileImage with image size greater than 100kb")
    public void testValidateUserUnHappyPathForUpdateProfileImage() throws IOException {
        // Given
        FileInputStream fs = new FileInputStream("/home/phoenix/Desktop/backend/Amazon-Backend/src/test/java/com/phoenix/amazon/AmazonBackend/testimages/users/test180kb.jpg");
        final MockMultipartFile IMAGE_FILE =
                new MockMultipartFile("data", "test180kb.jpg", "text/plain", fs);
        Files.copy(IMAGE_FILE.getInputStream(),
                Paths.get("/home/phoenix/Desktop/backend/Amazon-Backend/downloadable/images/users/test180kb.jpg"));

        Users oldUser = constructOldUser();
        Users newUser = new Users.builder()
                .profileImage("test180kb.jpg")
                .build();
        Set<Users> setOfUsers = constructUserSet();

        // When
        when(userRepositoryMock.findAll()).thenReturn(setOfUsers.stream().toList());

        // Then
        assertThrows(BadApiRequestExceptions.class, () -> {
            userValidationServiceMock
                    .validateUser(Optional.of(newUser), Optional.of(oldUser), "testValidateUser", UPDATE_PROFILE_IMAGE);
        }, "BadApiRequestExceptions should have been thrown");
    }

    private Set<Users> constructUserSet() {
        return Set.of(constructOldUser(), constructOldUser());
    }

    private Users constructOldUser() {
        final String TEST_UUID = "9125f005-0cab-4123-b7bd-24a8cee8eaec";
        final String TEST_PRIMARY_EMAIL = "test@gmail.com";
        final String TEST_SECONDARY_EMAIL = "tests@gmail.com";
        final String TEST_USER_NAME = "TEST_USER_NAME";
        final String TEST_FIRST_NAME = "TEST_FIRST_NAME";
        final String TEST_LAST_NAME = "TEST_LAST_NAME";
        final GENDER TEST_GENDER = MALE;
        final String TEST_PASSWORD = "TEST_PASSWORD";
        final String TEST_PROFILE_IMAGE = "9125f005-0cab-4123-b7bd-24a8cee8eaec.jpg";
        final String TEST_ABOUT = "Lorem ipsum dolor sit amet consectetur adipisicing elit. Maxime mollitia,\n" +
                "molestiae quas vel sint commodi repudiandae consequuntur voluptatum laborum\n" +
                "numquam blanditiis harum quisquam eius sed odit fugiat iusto fuga praesentium\n" +
                "optio, eaque rerum! Provident similique accusantium nemo autem. Veritatis\n" +
                "obcaecati tenetur iure eius earum ut molestias architecto voluptate aliquam\n" +
                "nihil, eveniet aliquid culpa officia aut! Impedit sit sunt quaerat, odit,\n";
        final LocalDateTime TEST_LAST_SEEN = LocalDateTime.now();
        final String ADMIN = "ADMIN";
        return new Users.builder()
                .userId(TEST_UUID)
                .userName(TEST_USER_NAME)
                .firstName(TEST_FIRST_NAME)
                .lastName(TEST_LAST_NAME)
                .primaryEmail(TEST_PRIMARY_EMAIL)
                .secondaryEmail(TEST_SECONDARY_EMAIL)
                .password(TEST_PASSWORD)
                .previous_password_set(giveMePasswordSet())
                .gender(TEST_GENDER)
                .profileImage(TEST_PROFILE_IMAGE)
                .about(TEST_ABOUT)
                .lastSeen(TEST_LAST_SEEN)
                .createdDate(LocalDate.now().minusDays(10))
                .createdBy(ADMIN)
                .build();
    }

    private Set<PassWordSet> giveMePasswordSet() {
        PassWordSet passWordSet1 = new PassWordSet.builder()
                .password_id("1")
                .passwords("TEST_PASSWORD")
                .build();
        PassWordSet passWordSet2 = new PassWordSet.builder()
                .password_id("2")
                .passwords("TEST_PASSWORD_1")
                .build();
        PassWordSet passWordSet3 = new PassWordSet.builder()
                .password_id("3")
                .passwords("TEST_PASSWORD_2")
                .build();
        PassWordSet passWordSet4 = new PassWordSet.builder()
                .password_id("3")
                .passwords("TEST_PASSWORD_3")
                .build();
        PassWordSet passWordSet5 = new PassWordSet.builder()
                .password_id("4")
                .passwords("TEST_PASSWORD_4")
                .build();
        return new HashSet<>(Arrays.asList(passWordSet1, passWordSet2, passWordSet3, passWordSet4, passWordSet5));
    }

    private Users constructNewUser() {
        final String TEST_NEW_UUID = "b3e19725-db33-4596-8d21-505179899cff";
        final String TEST_NEW_PRIMARY_EMAIL = "testnew@gmail.com";
        final String TEST_NEW_SECONDARY_EMAIL = "testsnew@gmail.com";
        final String TEST_NEW_USER_NAME = "TEST_NEW_USER_NAME";
        final String TEST_NEW_FIRST_NAME = "TEST_NEW_FIRST_NAME";
        final String TEST_NEW_LAST_NAME = "TEST_NEW_LAST_NAME";
        final GENDER TEST_NEW_GENDER = FEMALE;
        final String TEST_NEW_PASSWORD = "TEST_NEW_PASSWORD";
        final String TEST_NEW_PROFILE_IMAGE = "b3e19725-db33-4596-8d21-505179899cff.jpg";
        final String TEST_NEW_ABOUT = "NEW NEW NEW NEW NEW NEW NEW NEW NEW NEW NEW NEW NEW NEW NEW NEW NEW";
        final LocalDateTime TEST_NEW_LAST_SEEN = LocalDateTime.now();
        final String ADMIN = "ADMIN";
        return new Users.builder()
                .userId(TEST_NEW_UUID)
                .userName(TEST_NEW_USER_NAME)
                .firstName(TEST_NEW_FIRST_NAME)
                .lastName(TEST_NEW_LAST_NAME)
                .primaryEmail(TEST_NEW_PRIMARY_EMAIL)
                .secondaryEmail(TEST_NEW_SECONDARY_EMAIL)
                .password(TEST_NEW_PASSWORD)
                .gender(TEST_NEW_GENDER)
                .profileImage(TEST_NEW_PROFILE_IMAGE)
                .about(TEST_NEW_ABOUT)
                .lastSeen(TEST_NEW_LAST_SEEN)
                .createdDate(LocalDate.now().minusDays(10))
                .createdBy(ADMIN)
                .build();
    }
}
