//package com.phoenix.amazon.AmazonBackend.services.validationservice.impl;
//
//import com.phoenix.amazon.AmazonBackend.entity.PassWordSet;
//import com.phoenix.amazon.AmazonBackend.entity.Users;
//import com.phoenix.amazon.AmazonBackend.exceptions.BadApiRequestExceptions;
//import com.phoenix.amazon.AmazonBackend.exceptions.UserExceptions;
//import com.phoenix.amazon.AmazonBackend.exceptions.UserNotFoundExceptions;
//import com.phoenix.amazon.AmazonBackend.repository.IUserRepository;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.mock.web.MockMultipartFile;
//
//import java.io.FileInputStream;
//import java.io.IOException;
//import java.nio.file.Files;
//import java.nio.file.Paths;
//import java.time.LocalDate;
//import java.time.LocalDateTime;
//import java.util.Arrays;
//import java.util.HashSet;
//import java.util.Optional;
//import java.util.Set;
//
//import static com.phoenix.amazon.AmazonBackend.helpers.AllConstantHelpers.GENDER;
//import static com.phoenix.amazon.AmazonBackend.helpers.AllConstantHelpers.GENDER.FEMALE;
//import static com.phoenix.amazon.AmazonBackend.helpers.AllConstantHelpers.GENDER.MALE;
//import static com.phoenix.amazon.AmazonBackend.helpers.AllConstantHelpers.USER_FIELD_VALIDATION.VALIDATE_USER_ID_OR_USER_NAME_OR_PRIMARY_EMAIL;
//import static com.phoenix.amazon.AmazonBackend.helpers.AllConstantHelpers.USER_VALIDATION.CREATE_USER;
//import static com.phoenix.amazon.AmazonBackend.helpers.AllConstantHelpers.USER_VALIDATION.DELETE_USER_BY_USER_ID_OR_USER_NAME_OR_PRIMARY_EMAIL;
//import static com.phoenix.amazon.AmazonBackend.helpers.AllConstantHelpers.USER_VALIDATION.GET_ALL_USERS;
//import static com.phoenix.amazon.AmazonBackend.helpers.AllConstantHelpers.USER_VALIDATION.GET_PROFILE_IMAGE;
//import static com.phoenix.amazon.AmazonBackend.helpers.AllConstantHelpers.USER_VALIDATION.GET_USER_INFO_BY_USERID_USER_NAME_PRIMARY_EMAIL;
//import static com.phoenix.amazon.AmazonBackend.helpers.AllConstantHelpers.USER_VALIDATION.NULL_OBJECT;
//import static com.phoenix.amazon.AmazonBackend.helpers.AllConstantHelpers.USER_VALIDATION.SEARCH_ALL_USERS_BY_FIRST_NAME;
//import static com.phoenix.amazon.AmazonBackend.helpers.AllConstantHelpers.USER_VALIDATION.SEARCH_ALL_USERS_BY_GENDER;
//import static com.phoenix.amazon.AmazonBackend.helpers.AllConstantHelpers.USER_VALIDATION.SEARCH_ALL_USERS_BY_LAST_NAME;
//import static com.phoenix.amazon.AmazonBackend.helpers.AllConstantHelpers.USER_VALIDATION.SEARCH_ALL_USERS_BY_USER_NAME;
//import static com.phoenix.amazon.AmazonBackend.helpers.AllConstantHelpers.USER_VALIDATION.SEARCH_USER_BY_PRIMARY_EMAIL;
//import static com.phoenix.amazon.AmazonBackend.helpers.AllConstantHelpers.USER_VALIDATION.UPDATE_PASSWORD;
//import static com.phoenix.amazon.AmazonBackend.helpers.AllConstantHelpers.USER_VALIDATION.UPDATE_PRIMARY_EMAIL;
//import static com.phoenix.amazon.AmazonBackend.helpers.AllConstantHelpers.USER_VALIDATION.UPDATE_PROFILE_IMAGE;
//import static com.phoenix.amazon.AmazonBackend.helpers.AllConstantHelpers.USER_VALIDATION.UPDATE_SECONDARY_EMAIL;
//import static com.phoenix.amazon.AmazonBackend.helpers.AllConstantHelpers.USER_VALIDATION.UPDATE_USERNAME;
//import static com.phoenix.amazon.AmazonBackend.helpers.AllConstantHelpers.USER_VALIDATION.VALIDATE_PASSWORD;
//import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
//import static org.junit.jupiter.api.Assertions.assertThrows;
//import static org.mockito.Mockito.when;
//
//@ExtendWith(MockitoExtension.class)
//@SpringBootTest
//public class UserValidationServiceImplTest {
//    @Mock
//    private IUserRepository userRepositoryMock;
//
//    @Value("${path.services.user.image.properties}")
//    String PATH_TO_PROPS;
//
//    @MockBean
//    private UserValidationServiceImpl userValidationServiceMock;
//
//
//    @BeforeEach
//    public void setUp() {
//        userValidationServiceMock = new UserValidationServiceImpl(userRepositoryMock, PATH_TO_PROPS);
//    }
//
//    @Test
//    @DisplayName("Test Happy Path -- validateUser() For Null Object")
//    public void testValidateUserHappyPathForNullObject() {
//        // Given
//        Users oldUser = constructOldUser();
//        Users newUser = constructNewUser();
//        Set<Users> setOfUsers = constructUserSet();
//
//        // When
//        when(userRepositoryMock.findAll()).thenReturn(setOfUsers.stream().toList());
//
//        // Then
//        assertDoesNotThrow(() -> {
//            userValidationServiceMock
//                    .validateUser(Optional.of(newUser), Optional.of(oldUser),
//                            "testValidateUserHappyPathForNullObject", NULL_OBJECT);
//        }, "No Exceptions are expected");
//    }
//
//    @Test
//    @DisplayName("Test Unhappy Path -- validateUser() For Null Object")
//    public void testValidateUserUnhappyPathForNullObject() {
//        // Given
//        Users oldUser = constructOldUser();
//        Set<Users> usersSet = constructUserSet();
//
//        // When
//        when(userRepositoryMock.findAll()).thenReturn(usersSet.stream().toList());
//
//        // Then
//        assertThrows(BadApiRequestExceptions.class, () -> userValidationServiceMock.
//                        validateUser(Optional.empty(), Optional.of(oldUser),
//                                "testValidateUserUnhappyPathForNullObject", NULL_OBJECT),
//                "BadApiRequestException should have been thrown");
//    }
//
//    @Test
//    @DisplayName("Test Happy Path -- validateUser() For Create User")
//    public void testValidateUserHappyPathForCreateUser() {
//        // Given
//        Users oldUser = constructOldUser();
//        Users newUser = constructNewUser();
//        Set<Users> setOfUsers = constructUserSet();
//
//        // When
//        when(userRepositoryMock.findAll()).thenReturn(setOfUsers.stream().toList());
//
//        // Then
//        assertDoesNotThrow(() -> userValidationServiceMock
//                .validateUser(Optional.of(newUser), Optional.of(oldUser),
//                        "testValidateUserHappyPathForCreateUser",
//                        CREATE_USER), "No Exceptions are expected");
//    }
//
//    @Test
//    @DisplayName("Test Unhappy Path -- validateUser() For Create User with Null UserName")
//    public void testValidateUserUnhappyPathForCreateUserNullUserName() {
//        // Given
//        Users oldUser = constructOldUser();
//        Users newUser = new Users.builder()
//                .userName(null)
//                .build();
//        Set<Users> setOfUsers = constructUserSet();
//
//        // When
//        when(userRepositoryMock.findAll()).thenReturn(setOfUsers.stream().toList());
//
//        // Then
//        assertThrows(BadApiRequestExceptions.class, () -> userValidationServiceMock
//                .validateUser(Optional.of(newUser), Optional.of(oldUser),
//                        "testValidateUserUnhappyPathForCreateUserNullUserName",
//                        CREATE_USER), "BadApiRequestExceptions should have been thrown");
//    }
//
//    @Test
//    @DisplayName("Test Unhappy Path -- validateUser() For Create User with Null PrimaryEmail")
//    public void testValidateUserUnhappyPathForCreateUserNullPrimaryEmail() {
//        // Given
//        Users oldUser = constructOldUser();
//        Users newUser = new Users.builder()
//                .userName(oldUser.getUserName())
//                .primaryEmail(null)
//                .build();
//        Set<Users> setOfUsers = constructUserSet();
//
//        // When
//        when(userRepositoryMock.findAll()).thenReturn(setOfUsers.stream().toList());
//
//        // Then
//        assertThrows(BadApiRequestExceptions.class, () -> userValidationServiceMock
//                .validateUser(Optional.of(newUser), Optional.of(oldUser),
//                        "testValidateUserUnhappyPathForCreateUserNullPrimaryEmail",
//                        CREATE_USER), "BadApiRequestExceptions should have been thrown");
//    }
//
//    @Test
//    @DisplayName("Test Unhappy Path -- validateUser() For Create User with Null FirstName")
//    public void testValidateUserUnhappyPathForCreateUserNullFirstName() {
//        // Given
//        Users oldUser = constructOldUser();
//        Users newUser = new Users.builder()
//                .userName(oldUser.getUserName())
//                .primaryEmail(oldUser.getPrimaryEmail())
//                .firstName(null)
//                .build();
//        Set<Users> setOfUsers = constructUserSet();
//
//        // When
//        when(userRepositoryMock.findAll()).thenReturn(setOfUsers.stream().toList());
//
//        // Then
//        assertThrows(BadApiRequestExceptions.class, () -> userValidationServiceMock
//                .validateUser(Optional.of(newUser), Optional.of(oldUser),
//                        "testValidateUserUnhappyPathForCreateUserNullFirstName",
//                        CREATE_USER), "BadApiRequestExceptions should have been thrown");
//    }
//
//    @Test
//    @DisplayName("Test Unhappy Path -- validateUser() For Create User with Null LastName")
//    public void testValidateUserUnhappyPathForCreateUserNullLastName() {
//        // Given
//        Users oldUser = constructOldUser();
//        Users newUser = new Users.builder()
//                .userName(oldUser.getUserName())
//                .primaryEmail(oldUser.getPrimaryEmail())
//                .firstName(oldUser.getFirstName())
//                .lastName(null)
//                .build();
//        Set<Users> setOfUsers = constructUserSet();
//
//        // When
//        when(userRepositoryMock.findAll()).thenReturn(setOfUsers.stream().toList());
//
//        // Then
//        assertThrows(BadApiRequestExceptions.class, () -> {
//            userValidationServiceMock
//                    .validateUser(Optional.of(newUser), Optional.of(oldUser),
//                            "testValidateUserUnhappyPathForCreateUserNullLastName",
//                            CREATE_USER);
//        }, "BadApiRequestExceptions should have been thrown");
//    }
//
//    @Test
//    @DisplayName("Test Unhappy Path -- validateUser() For Create User with Null Gender")
//    public void testValidateUserUnhappyPathForCreateUserNullGender() {
//        // Given
//        Users oldUser = constructOldUser();
//        Users newUser = new Users.builder()
//                .userName(oldUser.getUserName())
//                .primaryEmail(oldUser.getPrimaryEmail())
//                .firstName(oldUser.getFirstName())
//                .lastName(oldUser.getLastName())
//                .gender(null)
//                .build();
//        Set<Users> setOfUsers = constructUserSet();
//
//        // When
//        when(userRepositoryMock.findAll()).thenReturn(setOfUsers.stream().toList());
//
//        // Then
//        assertThrows(BadApiRequestExceptions.class, () -> userValidationServiceMock
//                .validateUser(Optional.of(newUser),
//                        Optional.of(oldUser),
//                        "testValidateUserUnhappyPathForCreateUserNullGender",
//                        CREATE_USER), "BadApiRequestExceptions should have been thrown");
//    }
//
//    @Test
//    @DisplayName("Test Unhappy Path -- validateUser() For Create User with Existing Primary Emails")
//    public void testValidateUserUnhappyPathForCreateUserPrimaryEmailExisting() {
//        // Given
//        Users oldUser = constructOldUser();
//        Users newUser = constructNewUser();
//        Users requestedUser = new Users.builder()
//                .primaryEmail(oldUser.getPrimaryEmail())
//                .userName(newUser.getUserName())
//                .userId(newUser.getUserId())
//                .secondaryEmail(newUser.getSecondaryEmail())
//                .firstName(newUser.getFirstName())
//                .lastName(newUser.getLastName())
//                .about(newUser.getAbout())
//                .gender(newUser.getGender())
//                .build();
//        Set<Users> setOfUsers = constructUserSet();
//
//        // When
//        when(userRepositoryMock.findAll()).thenReturn(setOfUsers.stream().toList());
//
//        // Then
//        assertThrows(UserExceptions.class, () -> {
//            userValidationServiceMock
//                    .validateUser(Optional.of(requestedUser),
//                            Optional.of(oldUser),
//                            "testValidateUserUnhappyPathForCreateUserPrimaryEmailExisting",
//                            CREATE_USER);
//        }, "UserExceptions should have been thrown");
//    }
//
//    @Test
//    @DisplayName("Test Unhappy Path -- validateUser() For Create User with Existing Primary in Someones Secondary Emails")
//    public void testValidateUserUnhappyPathForCreateUserPrimaryEmailExistingInSecondary() {
//        // Given
//        Users oldUser = constructOldUser();
//        Users newUser = constructNewUser();
//        Users requestedUser = new Users.builder()
//                .primaryEmail(oldUser.getSecondaryEmail())
//                .secondaryEmail(newUser.getSecondaryEmail())
//                .userName(newUser.getUserName())
//                .userId(newUser.getUserId())
//                .firstName(newUser.getFirstName())
//                .lastName(newUser.getLastName())
//                .about(newUser.getAbout())
//                .gender(newUser.getGender())
//                .build();
//        Set<Users> setOfUsers = constructUserSet();
//
//        // When
//        when(userRepositoryMock.findAll()).thenReturn(setOfUsers.stream().toList());
//
//        // Then
//        assertThrows(UserExceptions.class, () -> userValidationServiceMock
//                .validateUser(Optional.of(requestedUser),
//                        Optional.of(oldUser),
//                        "testValidateUserUnhappyPathForCreateUserPrimaryEmailExistingInSecondary",
//                        CREATE_USER), "UserExceptions should have been thrown");
//    }
//
//    @Test
//    @DisplayName("Test Unhappy Path -- validateUser() For Create User with Existing Secondary Email")
//    public void testValidateUserUnhappyPathForCreateUserSecondaryEmailExisting() {
//        // Given
//        Users oldUser = constructOldUser();
//        Users newUser = constructNewUser();
//        Users requestedUser = new Users.builder()
//                .secondaryEmail(oldUser.getSecondaryEmail())
//                .userId(newUser.getUserId())
//                .userName(newUser.getUserName())
//                .primaryEmail(newUser.getPrimaryEmail())
//                .firstName(newUser.getFirstName())
//                .lastName(newUser.getLastName())
//                .about(newUser.getAbout())
//                .gender(newUser.getGender())
//                .build();
//        Set<Users> setOfUsers = constructUserSet();
//
//        // When
//        when(userRepositoryMock.findAll()).thenReturn(setOfUsers.stream().toList());
//
//        // Then
//        assertThrows(UserExceptions.class, () -> userValidationServiceMock
//                .validateUser(Optional.of(requestedUser), Optional.of(oldUser),
//                        "testValidateUserUnhappyPathForCreateUserSecondaryEmailExisting",
//                        CREATE_USER), "UserExceptions should have been thrown");
//    }
//
//    @Test
//    @DisplayName("Test Unhappy Path -- validateUser() For Create User with Existing Secondary Email In Someone Primary")
//    public void testValidateUserUnhappyPathForCreateUserSecondaryEmailExistingInPrimary() {
//        // Given
//        Users oldUser = constructOldUser();
//        Users newUser = constructNewUser();
//        Users requestedUser = new Users.builder()
//                .secondaryEmail(oldUser.getPrimaryEmail())
//                .userId(newUser.getUserId())
//                .userName(newUser.getUserName())
//                .primaryEmail(newUser.getPrimaryEmail())
//                .firstName(newUser.getFirstName())
//                .lastName(newUser.getLastName())
//                .about(newUser.getAbout())
//                .gender(newUser.getGender())
//                .build();
//        Set<Users> setOfUsers = constructUserSet();
//
//        // When
//        when(userRepositoryMock.findAll()).thenReturn(setOfUsers.stream().toList());
//
//        // Then
//        assertThrows(UserExceptions.class, () -> userValidationServiceMock
//                .validateUser(Optional.of(requestedUser), Optional.of(oldUser),
//                        "testValidateUserUnhappyPathForCreateUserSecondaryEmailExistingInPrimary",
//                        CREATE_USER), "UserExceptions should have been thrown");
//    }
//
//    @Test
//    @DisplayName("Test Unhappy Path -- validateUser() For Create User with Primary & Secondary Email Same")
//    public void testValidateUserUnhappyPathForCreateUserPrimaryAndSecondaryEmailSame() {
//        // Given
//        Users oldUser = constructOldUser();
//        Users newUser = constructNewUser();
//        Users requestedUser = new Users.builder()
//                .primaryEmail(newUser.getPrimaryEmail())
//                .secondaryEmail(newUser.getPrimaryEmail())
//                .userId(newUser.getUserId())
//                .userName(newUser.getUserName())
//                .firstName(newUser.getFirstName())
//                .lastName(newUser.getLastName())
//                .about(newUser.getAbout())
//                .gender(newUser.getGender())
//                .build();
//        Set<Users> setOfUsers = constructUserSet();
//
//        // When
//        when(userRepositoryMock.findAll()).thenReturn(setOfUsers.stream().toList());
//
//        // Then
//        assertThrows(UserExceptions.class, () -> userValidationServiceMock
//                .validateUser(Optional.of(requestedUser), Optional.of(oldUser),
//                        "testValidateUserUnhappyPathForCreateUserPrimaryAndSecondaryEmailSame",
//                        CREATE_USER), "UserExceptions should have been thrown");
//    }
//
//    @Test
//    @DisplayName("Test Unhappy Path -- validateUser() For Create User with Existing UserName")
//    public void testValidateUserUnhappyPathForCreateUserUserNameExisting() {
//        // Given
//        Users oldUser = constructOldUser();
//        Users newUser = constructNewUser();
//        Users requestedUser = new Users.builder()
//                .userName(oldUser.getUserName())
//                .userId(newUser.getUserId())
//                .primaryEmail(newUser.getPrimaryEmail())
//                .secondaryEmail(newUser.getSecondaryEmail())
//                .firstName(newUser.getFirstName())
//                .lastName(newUser.getLastName())
//                .about(newUser.getAbout())
//                .gender(newUser.getGender())
//                .build();
//        Set<Users> setOfUsers = constructUserSet();
//
//        // When
//        when(userRepositoryMock.findAll()).thenReturn(setOfUsers.stream().toList());
//
//        // Then
//        assertThrows(UserExceptions.class, () -> userValidationServiceMock
//                .validateUser(Optional.of(requestedUser), Optional.of(oldUser),
//                        "testValidateUserUnhappyPathForCreateUserUserNameExisting",
//                        CREATE_USER), "UserExceptions should have been thrown");
//    }
//
//    @Test
//    @DisplayName("Test Unhappy Path -- validateUser() For Create User with Existing UserId")
//    public void testValidateUserUnhappyPathForCreateUserUserIdExisting() {
//        // Given
//        Users oldUser = constructOldUser();
//        Users newUser = constructNewUser();
//        Users requestedUser = new Users.builder()
//                .userId(oldUser.getUserId())
//                .userName(newUser.getUserName())
//                .primaryEmail(newUser.getPrimaryEmail())
//                .secondaryEmail(newUser.getSecondaryEmail())
//                .firstName(newUser.getFirstName())
//                .lastName(newUser.getLastName())
//                .about(newUser.getAbout())
//                .gender(newUser.getGender())
//                .build();
//        Set<Users> setOfUsers = constructUserSet();
//
//        // When
//        when(userRepositoryMock.findAll()).thenReturn(setOfUsers.stream().toList());
//
//        // Then
//        assertThrows(UserExceptions.class, () -> userValidationServiceMock
//                .validateUser(Optional.of(requestedUser), Optional.of(oldUser),
//                        "testValidateUserUnhappyPathForCreateUserUserIdExisting",
//                        CREATE_USER), "UserExceptions should have been thrown");
//    }
//
//    @Test
//    @DisplayName("Test Happy Path -- validateUser() For Update UserName")
//    public void testValidateUserHappyPathForUpdateUserNameExisting() {
//        // Given
//        Users oldUser = constructOldUser();
//        Users newUser = constructNewUser();
//        Set<Users> setOfUsers = constructUserSet();
//
//        // When
//        when(userRepositoryMock.findAll()).thenReturn(setOfUsers.stream().toList());
//
//        // Then
//        assertDoesNotThrow(() -> userValidationServiceMock
//                .validateUser(Optional.of(newUser), Optional.of(oldUser),
//                        "testValidateUserHappyPathForUpdateUserNameExisting",
//                        UPDATE_USERNAME), "No Exceptions are expected");
//    }
//
//    @Test
//    @DisplayName("Test Unhappy Path -- validateUser() For Update UserName with Existing UserName")
//    public void testValidateUserUnhappyPathForUpdateUserNameWithUserNameExisting() {
//        // Given
//        Users oldUser = constructOldUser();
//        Users newUser = new Users.builder().userName(oldUser.getUserName()).build();
//        Set<Users> setOfUsers = constructUserSet();
//
//        // When
//        when(userRepositoryMock.findAll()).thenReturn(setOfUsers.stream().toList());
//
//        // Then
//        assertThrows(UserExceptions.class, () -> userValidationServiceMock
//                .validateUser(Optional.of(newUser), Optional.of(oldUser),
//                        "testValidateUserUnhappyPathForUpdateUserNameWithUserNameExisting",
//                        UPDATE_USERNAME), "UserExceptions should have been thrown");
//    }
//
//    @Test
//    @DisplayName("Test Happy Path -- validateUser() For Get User with valid userId,userName,primaryEmail")
//    public void testValidateUserHappyPathForGetUserIdUserNamePrimaryEmail() {
//        // Given
//        Users oldUser = constructOldUser();
//        Users newUser = constructNewUser();
//        Set<Users> setOfUsers = constructUserSet();
//
//        // When
//        when(userRepositoryMock.findAll()).thenReturn(setOfUsers.stream().toList());
//
//        // Then
//        assertDoesNotThrow(() -> userValidationServiceMock
//                .validateUser(Optional.of(newUser), Optional.of(oldUser),
//                        "testValidateUserHappyPathForGetUserIdUserNamePrimaryEmail",
//                        GET_USER_INFO_BY_USERID_USER_NAME_PRIMARY_EMAIL), "No Exceptions are expected");
//    }
//
//    @Test
//    @DisplayName("Test Unhappy Path -- validateUser() For Get User with invalid userId,userName,primaryEmail")
//    public void testValidateUserUnhappyPathForGetUserWithInvalidUserIdUserNamePrimaryEmail() {
//        // Given
//        Users newUser = constructNewUser();
//        Set<Users> setOfUsers = constructUserSet();
//
//        // When
//        when(userRepositoryMock.findAll()).thenReturn(setOfUsers.stream().toList());
//
//        // Then
//        assertThrows(UserNotFoundExceptions.class, () -> userValidationServiceMock
//                        .validateUser(Optional.of(newUser), Optional.empty(),
//                                "testValidateUserUnhappyPathForGetUserWithInvalidUserIdUserNamePrimaryEmail",
//                                GET_USER_INFO_BY_USERID_USER_NAME_PRIMARY_EMAIL),
//                "UserNotFoundExceptions should have been thrown");
//    }
//
//    @Test
//    @DisplayName("Test Unhappy Path -- validateUser() For Update Primary Email with Existing Primary Emails")
//    public void testValidateUserUnhappyPathForUpdatePrimaryEmailUnhappy() {
//        // Given
//        Users oldUser = constructOldUser();
//        Users newUser = new Users.builder().primaryEmail(oldUser.getPrimaryEmail()).build();
//        Set<Users> setOfUsers = constructUserSet();
//
//        // When
//        when(userRepositoryMock.findAll()).thenReturn(setOfUsers.stream().toList());
//
//        // Then
//        assertThrows(UserExceptions.class, () -> userValidationServiceMock
//                .validateUser(Optional.of(newUser), Optional.of(oldUser),
//                        "testValidateUserUnhappyPathForUpdatePrimaryEmailUnhappy",
//                        UPDATE_PRIMARY_EMAIL), "UserExceptions should have been thrown");
//    }
//
//    @Test
//    @DisplayName("Test Happy Path -- validateUser() For Update Primary Email with not Existing Primary Emails")
//    public void testValidateUserHappyPathForUpdatePrimaryEmailHappyPath() {
//        // Given
//        Users oldUser = constructOldUser();
//        Users newUser = constructNewUser();
//        Set<Users> setOfUsers = constructUserSet();
//
//        // When
//        when(userRepositoryMock.findAll()).thenReturn(setOfUsers.stream().toList());
//
//        // Then
//        assertDoesNotThrow(() -> userValidationServiceMock
//                .validateUser(Optional.of(newUser), Optional.of(oldUser),
//                        "testValidateUserHappyPathForUpdatePrimaryEmailHappyPath",
//                        UPDATE_PRIMARY_EMAIL), "No Exceptions are expected");
//    }
//
//    @Test
//    @DisplayName("Test Unhappy Path -- validateUser() For Update Secondary Email with Existing Secondary Emails")
//    public void testValidateUserUnhappyPathForUpdateSecondaryEmail() {
//        // Given
//        Users oldUser = constructOldUser();
//        Users newUser = new Users.builder().secondaryEmail(oldUser.getSecondaryEmail()).build();
//        Set<Users> setOfUsers = constructUserSet();
//
//        // When
//        when(userRepositoryMock.findAll()).thenReturn(setOfUsers.stream().toList());
//
//        // Then
//        assertThrows(UserExceptions.class, () -> {
//            userValidationServiceMock
//                    .validateUser(Optional.of(newUser), Optional.of(oldUser),
//                            "testValidateUserUnhappyPathForUpdateSecondaryEmail",
//                            UPDATE_SECONDARY_EMAIL);
//        }, "UserExceptions should have been thrown");
//    }
//
//    @Test
//    @DisplayName("Test Happy Path -- validateUser() For Update Secondary Email with not Existing Secondary Emails")
//    public void testValidateUserHappyPathForUpdateSecondaryEmailHappyPath() {
//        // Given
//        Users oldUser = constructOldUser();
//        Users newUser = constructNewUser();
//        Set<Users> setOfUsers = constructUserSet();
//
//        // When
//        when(userRepositoryMock.findAll()).thenReturn(setOfUsers.stream().toList());
//
//        // Then
//        assertDoesNotThrow(() -> userValidationServiceMock
//                .validateUser(Optional.of(newUser), Optional.of(oldUser),
//                        "testValidateUserHappyPathForUpdateSecondaryEmailHappyPath",
//                        UPDATE_SECONDARY_EMAIL), "No Exceptions are expected");
//    }
//
//    @Test
//    @DisplayName("Test Happy Path -- validateUser() For Update Secondary Email with not Existing Primary Emails")
//    public void testValidateUserHappyPathForUpdatePrimaryEmailUnHappyPath() {
//        // Given
//        Users oldUser = constructOldUser();
//        Users newUser = new Users
//                .builder()
//                .password(oldUser.getPassword())
//                .build();
//        Set<Users> setOfUsers = constructUserSet();
//
//        // When
//        when(userRepositoryMock.findAll()).thenReturn(setOfUsers.stream().toList());
//
//        // Then
//        assertThrows(UserExceptions.class, () -> userValidationServiceMock
//                .validateUser(Optional.of(newUser), Optional.of(oldUser),
//                        "testValidateUserHappyPathForUpdatePrimaryEmailUnHappyPath",
//                        UPDATE_PASSWORD), "UserExceptions should have been thrown");
//    }
//
//    @Test
//    @DisplayName("Test Happy Path -- validateUser() For Update Password with not existing prev password")
//    public void testValidateUserHappyPathForUpdatePasswordHappyPath() {
//        // Given
//        Users oldUser = constructOldUser();
//        Users newUser = constructNewUser();
//        Set<Users> setOfUsers = constructUserSet();
//
//        // When
//        when(userRepositoryMock.findAll()).thenReturn(setOfUsers.stream().toList());
//
//        // Then
//        assertDoesNotThrow(() -> userValidationServiceMock
//                .validateUser(Optional.of(newUser), Optional.of(oldUser),
//                        "testValidateUserHappyPathForUpdatePasswordHappyPath",
//                        UPDATE_PASSWORD), "No Exceptions are expected");
//    }
//
//    @Test
//    @DisplayName("Test Happy Path -- validateUser() For Update Password")
//    public void testValidateUserHappyPathForUpdatePassword() {
//        // Given
//        Users oldUser = constructOldUser();
//        Users newUser = constructNewUser();
//        Set<Users> setOfUsers = constructUserSet();
//
//        // When
//        when(userRepositoryMock.findAll()).thenReturn(setOfUsers.stream().toList());
//
//        // Then
//        assertDoesNotThrow(() -> userValidationServiceMock
//                .validateUser(Optional.of(newUser), Optional.of(oldUser),
//                        "testValidateUserHappyPathForUpdatePassword",
//                        UPDATE_PASSWORD), "No Exceptions are expected");
//    }
//
//    @Test
//    @DisplayName("Test Happy Path -- validateUser() For Update profile image")
//    public void testValidateUserHappyPathForUpdateProfileImage() {
//        // Given
//        Users oldUser = constructOldUser();
//        Users newUser = new Users.builder()
//                .profileImage("TestImage.png")
//                .build();
//        Set<Users> setOfUsers = constructUserSet();
//
//        // When
//        when(userRepositoryMock.findAll()).thenReturn(setOfUsers.stream().toList());
//
//        // Then
//        assertDoesNotThrow(() -> userValidationServiceMock
//                .validateUser(Optional.of(newUser), Optional.of(oldUser),
//                        "testValidateUserHappyPathForUpdateProfileImage",
//                        UPDATE_PROFILE_IMAGE), "No Exceptions are expected");
//    }
//
//    @Test
//    @DisplayName("Test Unhappy Path -- validateUser() For Update ProfileImage with image size greater than 100kb")
//    public void testValidateUserUnhappyPathForUpdateProfileImage() throws IOException {
//        // Given
//        FileInputStream fs = new FileInputStream("/home/phoenix/Desktop/backend/Amazon-Backend/src/test/java/" +
//                "com/phoenix/amazon/AmazonBackend/testimages/users/test180kb.jpg");
//        final MockMultipartFile IMAGE_FILE =
//                new MockMultipartFile("data", "test180kb.jpg", "text/plain", fs);
//        Files.copy(IMAGE_FILE.getInputStream(),
//                Paths.get("/home/phoenix/Desktop/backend/Amazon-Backend/downloadable/images/users/test180kb.jpg"));
//
//        Users oldUser = constructOldUser();
//        Users newUser = new Users.builder()
//                .profileImage("test180kb.jpg")
//                .build();
//        Set<Users> setOfUsers = constructUserSet();
//
//        // When
//        when(userRepositoryMock.findAll()).thenReturn(setOfUsers.stream().toList());
//
//        // Then
//        assertThrows(BadApiRequestExceptions.class, () -> userValidationServiceMock
//                .validateUser(Optional.of(newUser), Optional.of(oldUser),
//                        "testValidateUserUnhappyPathForUpdateProfileImage",
//                        UPDATE_PROFILE_IMAGE), "BadApiRequestExceptions should have been thrown");
//    }
//
//    @Test
//    @DisplayName("Test Unhappy Path -- validateUser() For Get profile image")
//    public void testValidateUserUnhappyPathForGetProfileImage() {
//        // Given
//        Users oldUser = new Users.builder().profileImage(null).build();
//        Users newUser = constructNewUser();
//        Set<Users> setOfUsers = constructUserSet();
//
//        // When
//        when(userRepositoryMock.findAll()).thenReturn(setOfUsers.stream().toList());
//
//        // Then
//        assertThrows(UserExceptions.class, () -> userValidationServiceMock
//                .validateUser(Optional.of(newUser), Optional.of(oldUser),
//                        "testValidateUserUnhappyPathForGetProfileImage",
//                        GET_PROFILE_IMAGE), "UserExceptions should have been thrown");
//    }
//
//    @Test
//    @DisplayName("Test Unhappy Path -- validateUser() For Delete User")
//    public void testValidateUserUnhappyPathForDeleteUser() {
//        // Given
//        Users newUser = constructNewUser();
//        Set<Users> setOfUsers = constructUserSet();
//
//        // When
//        when(userRepositoryMock.findAll()).thenReturn(setOfUsers.stream().toList());
//
//        // Then
//        assertThrows(UserNotFoundExceptions.class, () -> userValidationServiceMock
//                        .validateUser(Optional.of(newUser), Optional.empty(),
//                                "testValidateUserUnhappyPathForDeleteUser",
//                                DELETE_USER_BY_USER_ID_OR_USER_NAME_OR_PRIMARY_EMAIL),
//                "UserNotFoundExceptions should have been thrown");
//    }
//
//    @Test
//    @DisplayName("Test Unhappy Path -- validateUser() when old & current password do not match")
//    public void testValidateUserUnhappyPathForValidatePassword() {
//        // Given
//        Users oldUser = new Users.builder().password("TEST_PASSWORD").build();
//        Users newUser = new Users.builder().password("TEST_PASSWORD_DIFFERENT").build();
//        Set<Users> setOfUsers = constructUserSet();
//
//        // When
//        when(userRepositoryMock.findAll()).thenReturn(setOfUsers.stream().toList());
//
//        // Then
//        assertThrows(UserExceptions.class, () -> userValidationServiceMock
//                .validateUser(Optional.of(newUser), Optional.of(oldUser),
//                        "testValidateUserUnhappyPathForValidatePassword",
//                        VALIDATE_PASSWORD), "UserExceptions should have been thrown");
//    }
//
//    @Test
//    @DisplayName("Test Unhappy Path -- validateUserList() for get all users")
//    public void testValidateUserListUnhappyPathForGetAllUsers() {
//        // Given
//        Set<Users> setOfUsers = new HashSet<>();
//
//        // Then
//        assertThrows(UserNotFoundExceptions.class, () -> userValidationServiceMock
//                .validateUserList(setOfUsers,
//                        "testValidateUserListUnhappyPathForGetAllUsers",
//                        GET_ALL_USERS), "UserNotFoundExceptions should have been thrown");
//    }
//
//    @Test
//    @DisplayName("Test Unhappy Path -- validateUserList() for search all users by userName")
//    public void testValidateUserListUnHappyPathForSearchAllUserByUserName() {
//        // Given
//        Set<Users> setOfUsers = new HashSet<>();
//
//        // Then
//        assertThrows(UserNotFoundExceptions.class, () -> userValidationServiceMock
//                .validateUserList(setOfUsers,
//                        "testValidateUserListUnHappyPathForSearchAllUserByUserName",
//                        SEARCH_ALL_USERS_BY_USER_NAME), "UserNotFoundExceptions should have been thrown");
//    }
//
//    @Test
//    @DisplayName("Test Unhappy Path -- validateUserList() for search user by primary email")
//    public void testValidateUserListUnHappyPathForSearchUsersByPrimaryEmail() {
//        // Given
//        Set<Users> setOfUsers = new HashSet<>();
//
//        // Then
//        assertThrows(UserNotFoundExceptions.class, () -> userValidationServiceMock
//                .validateUserList(setOfUsers,
//                        "testValidateUserListUnHappyPathForSearchUsersByPrimaryEmail",
//                        SEARCH_USER_BY_PRIMARY_EMAIL), "UserNotFoundExceptions should have been thrown");
//    }
//
//    @Test
//    @DisplayName("Test Unhappy Path -- validateUserList() for search user by First Name")
//    public void testValidateUserListUnhappyPathForSearchUsersByFirstName() {
//        // Given
//        Set<Users> setOfUsers = new HashSet<>();
//
//        // Then
//        assertThrows(UserNotFoundExceptions.class, () -> userValidationServiceMock
//                .validateUserList(setOfUsers,
//                        "testValidateUserListUnhappyPathForSearchUsersByFirstName",
//                        SEARCH_ALL_USERS_BY_FIRST_NAME), "UserNotFoundExceptions should have been thrown");
//    }
//
//    @Test
//    @DisplayName("Test Unhappy Path -- validateUserList() for search user by Last Name")
//    public void testValidateUserListUnhappyPathForSearchUsersByLastName() {
//        // Given
//        Set<Users> setOfUsers = new HashSet<>();
//
//        // Then
//        assertThrows(UserNotFoundExceptions.class, () -> userValidationServiceMock
//                .validateUserList(setOfUsers,
//                        "testValidateUserListUnhappyPathForSearchUsersByLastName",
//                        SEARCH_ALL_USERS_BY_LAST_NAME), "UserNotFoundExceptions should have been thrown");
//    }
//
//    @Test
//    @DisplayName("Test Unhappy Path -- validateUserList() for search user by gender")
//    public void testValidateUserListUnhappyPathForSearchUsersByGender() {
//        // Given
//        Set<Users> setOfUsers = new HashSet<>();
//
//        // Then
//        assertThrows(UserNotFoundExceptions.class, () -> userValidationServiceMock
//                .validateUserList(setOfUsers,
//                        "testValidateUserListUnhappyPathForSearchUsersByGender",
//                        SEARCH_ALL_USERS_BY_GENDER), "UserNotFoundExceptions should have been thrown");
//    }
//
//    @Test
//    @DisplayName("Test Happy Path -- validatePZeroUserFields()")
//    public void testValidatePZeroUserFieldsHappyPath() {
//        // Given
//        final String userId = "TEST_UUID";
//        final String userName = "TEST_USER_NAME";
//        final String primaryEmail = "TEST_PRIMARY_EMAIL";
//
//        // Then
//        assertDoesNotThrow(() -> userValidationServiceMock
//                .validatePZeroUserFields(userId, userName, primaryEmail,
//                        "testValidatePZeroUserFieldsHappyPath",
//                        VALIDATE_USER_ID_OR_USER_NAME_OR_PRIMARY_EMAIL), "No Exceptions are expected");
//    }
//
//    @Test
//    @DisplayName("Test Happy Path -- validatePZeroUserFields() when at least one is not null")
//    public void testValidatePZeroUserFieldsHappyPathWhenAnyOneIsNotNull() {
//        // Given
//        final String userId = null;
//        final String userName = null;
//        final String primaryEmail = "TEST_PRIMARY_EMAIL";
//
//        // Then
//        assertDoesNotThrow(() -> userValidationServiceMock
//                .validatePZeroUserFields(userId, userName, primaryEmail,
//                        "testValidatePZeroUserFieldsHappyPathWhenAnyOneIsNotNull",
//                        VALIDATE_USER_ID_OR_USER_NAME_OR_PRIMARY_EMAIL), "No Exceptions are expected");
//    }
//
//    @Test
//    @DisplayName("Test Unhappy Path -- validatePZeroUserFields() when userId,userName,primaryEmail are null")
//    public void testValidatePZeroUserFieldsUnhappyPathWhenAllAreNull() {
//        // Given
//        final String userId = null;
//        final String userName = null;
//        final String primaryEmail = null;
//
//        // Then
//        assertThrows(BadApiRequestExceptions.class, () -> userValidationServiceMock
//                        .validatePZeroUserFields(userId, userName, primaryEmail,
//                                "testValidatePZeroUserFieldsUnhappyPathWhenAllAreNull",
//                                VALIDATE_USER_ID_OR_USER_NAME_OR_PRIMARY_EMAIL),
//                "BadApiRequestExceptions should have been thrown");
//    }
//
//    @Test
//    @DisplayName("Test Happy Path -- validateNullField() ")
//    public void testValidateNullField() {
//        // Given
//        final String field = "TEST_FIELD";
//
//        // Then
//        assertDoesNotThrow(() -> userValidationServiceMock
//                .validateNullField(field, "Null Field not allowed",
//                        "testValidateNullField"), "No Exceptions are expected");
//    }
//
//    @Test
//    @DisplayName("Test Unhappy Path -- validateNullField() ")
//    public void testValidateNullFieldUnHappyPath() {
//        // Given
//        final String field = null;
//
//        // Then
//        assertThrows(BadApiRequestExceptions.class, () -> userValidationServiceMock
//                        .validateNullField(field, "Null Field not allowed",
//                                "testValidateNullFieldUnHappyPath"),
//                "BadApiRequestExceptions should have been thrown");
//    }
//
//    private Set<Users> constructUserSet() {
//        return Set.of(constructOldUser(), constructOldUser());
//    }
//
//    private Users constructOldUser() {
//        final String TEST_UUID = "9125f005-0cab-4123-b7bd-24a8cee8eaec";
//        final String TEST_PRIMARY_EMAIL = "test@gmail.com";
//        final String TEST_SECONDARY_EMAIL = "tests@gmail.com";
//        final String TEST_USER_NAME = "TEST_USER_NAME";
//        final String TEST_FIRST_NAME = "TEST_FIRST_NAME";
//        final String TEST_LAST_NAME = "TEST_LAST_NAME";
//        final GENDER TEST_GENDER = MALE;
//        final String TEST_PASSWORD = "TEST_PASSWORD";
//        final String TEST_PROFILE_IMAGE = "9125f005-0cab-4123-b7bd-24a8cee8eaec.jpg";
//        final String TEST_ABOUT = "Lorem ipsum dolor sit amet consectetur adipisicing elit. Maxime mollitia,\n" +
//                "molestiae quas vel sint commodi repudiandae consequuntur voluptatum laborum\n" +
//                "numquam blanditiis harum quisquam eius sed odit fugiat iusto fuga praesentium\n" +
//                "optio, eaque rerum! Provident similique accusantium nemo autem. Veritatis\n" +
//                "obcaecati tenetur iure eius earum ut molestias architecto voluptate aliquam\n" +
//                "nihil, eveniet aliquid culpa officia aut! Impedit sit sunt quaerat, odit,\n";
//        final LocalDateTime TEST_LAST_SEEN = LocalDateTime.now();
//        final String ADMIN = "ADMIN";
//        return new Users.builder()
//                .userId(TEST_UUID)
//                .userName(TEST_USER_NAME)
//                .firstName(TEST_FIRST_NAME)
//                .lastName(TEST_LAST_NAME)
//                .primaryEmail(TEST_PRIMARY_EMAIL)
//                .secondaryEmail(TEST_SECONDARY_EMAIL)
//                .password(TEST_PASSWORD)
//                .previous_password_set(giveMePasswordSet())
//                .gender(TEST_GENDER)
//                .profileImage(TEST_PROFILE_IMAGE)
//                .about(TEST_ABOUT)
//                .lastSeen(TEST_LAST_SEEN)
//                .createdDate(LocalDate.now().minusDays(10))
//                .createdBy(ADMIN)
//                .build();
//    }
//
//    private Set<PassWordSet> giveMePasswordSet() {
//        PassWordSet passWordSet1 = new PassWordSet.builder()
//                .password_id("1")
//                .passwords("TEST_PASSWORD")
//                .build();
//        PassWordSet passWordSet2 = new PassWordSet.builder()
//                .password_id("2")
//                .passwords("TEST_PASSWORD_1")
//                .build();
//        PassWordSet passWordSet3 = new PassWordSet.builder()
//                .password_id("3")
//                .passwords("TEST_PASSWORD_2")
//                .build();
//        PassWordSet passWordSet4 = new PassWordSet.builder()
//                .password_id("3")
//                .passwords("TEST_PASSWORD_3")
//                .build();
//        PassWordSet passWordSet5 = new PassWordSet.builder()
//                .password_id("4")
//                .passwords("TEST_PASSWORD_4")
//                .build();
//        return new HashSet<>(Arrays.asList(passWordSet1, passWordSet2, passWordSet3, passWordSet4, passWordSet5));
//    }
//
//    private Users constructNewUser() {
//        final String TEST_NEW_UUID = "b3e19725-db33-4596-8d21-505179899cff";
//        final String TEST_NEW_PRIMARY_EMAIL = "testnew@gmail.com";
//        final String TEST_NEW_SECONDARY_EMAIL = "testsnew@gmail.com";
//        final String TEST_NEW_USER_NAME = "TEST_NEW_USER_NAME";
//        final String TEST_NEW_FIRST_NAME = "TEST_NEW_FIRST_NAME";
//        final String TEST_NEW_LAST_NAME = "TEST_NEW_LAST_NAME";
//        final GENDER TEST_NEW_GENDER = FEMALE;
//        final String TEST_NEW_PASSWORD = "TEST_NEW_PASSWORD";
//        final String TEST_NEW_PROFILE_IMAGE = "b3e19725-db33-4596-8d21-505179899cff.jpg";
//        final String TEST_NEW_ABOUT = "NEW NEW NEW NEW NEW NEW NEW NEW NEW NEW NEW NEW NEW NEW NEW NEW NEW";
//        final LocalDateTime TEST_NEW_LAST_SEEN = LocalDateTime.now();
//        final String ADMIN = "ADMIN";
//        return new Users.builder()
//                .userId(TEST_NEW_UUID)
//                .userName(TEST_NEW_USER_NAME)
//                .firstName(TEST_NEW_FIRST_NAME)
//                .lastName(TEST_NEW_LAST_NAME)
//                .primaryEmail(TEST_NEW_PRIMARY_EMAIL)
//                .secondaryEmail(TEST_NEW_SECONDARY_EMAIL)
//                .password(TEST_NEW_PASSWORD)
//                .gender(TEST_NEW_GENDER)
//                .profileImage(TEST_NEW_PROFILE_IMAGE)
//                .about(TEST_NEW_ABOUT)
//                .lastSeen(TEST_NEW_LAST_SEEN)
//                .createdDate(LocalDate.now().minusDays(10))
//                .createdBy(ADMIN)
//                .build();
//    }
//}
