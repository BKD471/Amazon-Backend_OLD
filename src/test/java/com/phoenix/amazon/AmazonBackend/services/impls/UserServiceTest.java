package com.phoenix.amazon.AmazonBackend.services.impls;

import com.phoenix.amazon.AmazonBackend.dto.UserDto;
import com.phoenix.amazon.AmazonBackend.entity.Users;
import com.phoenix.amazon.AmazonBackend.exceptions.BadApiRequestExceptions;
import com.phoenix.amazon.AmazonBackend.exceptions.UserExceptions;
import com.phoenix.amazon.AmazonBackend.exceptions.UserNotFoundExceptions;
import com.phoenix.amazon.AmazonBackend.helpers.AllConstantHelpers;
import com.phoenix.amazon.AmazonBackend.helpers.MappingHelpers;
import com.phoenix.amazon.AmazonBackend.repository.IUserRepository;
import com.phoenix.amazon.AmazonBackend.services.validationservice.IUserValidationService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;
import java.util.Set;

import static com.phoenix.amazon.AmazonBackend.helpers.AllConstantHelpers.GENDER;
import static com.phoenix.amazon.AmazonBackend.helpers.AllConstantHelpers.GENDER.FEMALE;
import static com.phoenix.amazon.AmazonBackend.helpers.AllConstantHelpers.GENDER.MALE;

import static com.phoenix.amazon.AmazonBackend.helpers.MappingHelpers.UserDtoToUsers;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @InjectMocks
    private UserServiceImpl userService;
    @Mock
    private IUserValidationService userValidationService;
    @Mock
    private IUserRepository userRepository;
    private final String TEST_EMAIL = "test@gmail.com";
    private final String TEST_USER_NAME = "TEST_USER_NAME";
    private final String TEST_UUID = "58824409-dd6b-4934-9923-ec1daf9693da";
    private final String TEST_FIRST_NAME = "TEST_FIRST_NAME";
    private final String TEST_LAST_NAME = "TEST_LAST_NAME";
    private final AllConstantHelpers.GENDER TEST_GENDER = MALE;

    @Test
    @DisplayName("Test Happy Path -- createUser() With valid fields")
    public void testCreateUserHappyPath() {
        // When
        when(userRepository.save(any())).thenReturn(constructUser());
        doNothing().when(userValidationService).validateUser(any(), anyString(), any());
        UserDto userDto = userService.createUser(MappingHelpers.UsersToUsersDto(constructUser()));

        // Then
        assertThat(userDto.userName()).isEqualTo(TEST_USER_NAME);
        assertThat(userDto.email()).isEqualTo(TEST_EMAIL);
        assertThat(userDto.userId()).isEqualTo(TEST_UUID);
    }

    @Test
    @DisplayName("Test Unhappy Path -- createUser() with email already existing")
    public void testCreateUserUnhappyPathExistingEmail() {
        // Given
        UserDto userRequest=new UserDto.builder()
                .userId(TEST_UUID)
                .userName(TEST_USER_NAME)
                .firstName(TEST_FIRST_NAME)
                .lastName(TEST_LAST_NAME)
                .email("EXISTING_EMAIL")
                .about("")
                .gender(TEST_GENDER)
                .password("")
                .profileImage(".jpg")
                .lastSeen(null)
                .build();

        // When
        doThrow(new UserExceptions(UserExceptions.class, String.format("There's an account with Email %s",userRequest.email())
                , "testCreateUserUnhappyPathExistingEmail"))
                .when(userValidationService).validateUser(any(), anyString(), any());

        // Then
        assertThrows(UserExceptions.class, () -> {
            userService.createUser(userRequest);
        }, "UserException should have been thrown");
    }

    @Test
    @DisplayName("Test Unhappy Path -- createUser() with Null User")
    public void testCreateUserUnhappyPathNullUser() {
        // When
        doThrow(new BadApiRequestExceptions(BadApiRequestExceptions.class,
                "Null Users prohibited"
                , "testCreateUserUnhappyPathNullUser"))
                .when(userValidationService).validateUser(any(), anyString(), any());

        // Then
        assertThrows(BadApiRequestExceptions.class, () -> {
            userService.createUser(null);
        }, "BadApiRequestExceptions should have been thrown");
    }

    @Test
    @DisplayName("Test Happy Path -- updateUserByUserIdOrUserName() With all fields (firstName,lastName,Email,Gender,About) updated")
    public void testUpdateUserByUserIdOrUserNameAllFields() {
        // Given
        UserDto requestedUserFields = constructIncomingUserDtoRequest();

        // When
        when(userRepository.findByUserIdOrUserName(TEST_UUID, TEST_USER_NAME)).thenReturn(Optional.of(constructUser()));
        when(userRepository.save(any())).thenReturn(UserDtoToUsers(constructIncomingUserDtoRequest()));
        doNothing().when(userValidationService).validateFields(anyString(), anyString(), any(), anyString(), any());
        doNothing().when(userValidationService).validateUser(any(), anyString(), any());
        UserDto updatedUser = userService.updateUserByUserIdOrUserName(requestedUserFields, TEST_UUID, TEST_USER_NAME);

        // Then
        assertThat(updatedUser.firstName()).isEqualTo(requestedUserFields.firstName());
        assertThat(updatedUser.lastName()).isEqualTo(requestedUserFields.lastName());
        assertThat(updatedUser.email()).isEqualTo(requestedUserFields.email());
        assertThat(updatedUser.about()).isEqualTo(requestedUserFields.about());
        assertThat(updatedUser.gender()).isEqualTo(requestedUserFields.gender());
    }

    @Test
    @DisplayName("Test Happy Path -- updateUserByUserIdOrUserName() With only firstName updated")
    public void testUpdateUserByUserIdOrUserNameWithOnlyFirstNameChanged() {
        // Given
        Users users = constructUser();
        UserDto newUserDTo = new UserDto.builder()
                .userId(users.getUserId())
                .userName(users.getUserName())
                .firstName("UPDATED_FIRST_NAME")
                .lastName(users.getLastName())
                .email(users.getEmail())
                .password(users.getPassword())
                .profileImage(users.getProfileImage())
                .gender(users.getGender())
                .about(users.getAbout())
                .lastSeen(users.getLastSeen())
                .build();

        // When
        when(userRepository.findByUserIdOrUserName(TEST_UUID, TEST_USER_NAME)).thenReturn(Optional.of(users));
        when(userRepository.save(any())).thenReturn(UserDtoToUsers(newUserDTo));
        doNothing().when(userValidationService).validateFields(anyString(), anyString(), any(), anyString(), any());
        doNothing().when(userValidationService).validateUser(any(), anyString(), any());
        UserDto updatedUser = userService.updateUserByUserIdOrUserName(newUserDTo, TEST_UUID, TEST_USER_NAME);

        // Then
        assertThat(updatedUser.firstName()).isEqualTo(newUserDTo.firstName());
        assertThat(updatedUser.lastName()).isEqualTo(users.getLastName());
        assertThat(updatedUser.email()).isEqualTo(users.getEmail());
        assertThat(updatedUser.about()).isEqualTo(users.getAbout());
        assertThat(updatedUser.gender()).isEqualTo(users.getGender());
    }

    @Test
    @DisplayName("Test Happy Path -- updateUserByUserIdOrUserName() With only lastName updated")
    public void testUpdateUserByUserIdOrUserNameWithOnlyLastNameChanged() {
        // Given
        Users users = constructUser();
        UserDto newUserDTo = new UserDto.builder()
                .userId(users.getUserId())
                .userName(users.getUserName())
                .firstName(users.getFirstName())
                .lastName("UPDATED_LAST_NAME")
                .email(users.getEmail())
                .password(users.getPassword())
                .profileImage(users.getProfileImage())
                .gender(users.getGender())
                .about(users.getAbout())
                .lastSeen(users.getLastSeen())
                .build();

        // When
        when(userRepository.findByUserIdOrUserName(TEST_UUID, TEST_USER_NAME)).thenReturn(Optional.of(users));
        when(userRepository.save(any())).thenReturn(UserDtoToUsers(newUserDTo));
        doNothing().when(userValidationService).validateFields(anyString(), anyString(), any(), anyString(), any());
        doNothing().when(userValidationService).validateUser(any(), anyString(), any());
        UserDto updatedUser = userService.updateUserByUserIdOrUserName(newUserDTo, TEST_UUID, TEST_USER_NAME);

        // Then
        assertThat(updatedUser.firstName()).isEqualTo(users.getFirstName());
        assertThat(updatedUser.lastName()).isEqualTo(newUserDTo.lastName());
        assertThat(updatedUser.email()).isEqualTo(users.getEmail());
        assertThat(updatedUser.about()).isEqualTo(users.getAbout());
        assertThat(updatedUser.gender()).isEqualTo(users.getGender());
    }

    @Test
    @DisplayName("Test Happy Path -- updateUserByUserIdOrUserName() With only email updated")
    public void testUpdateUserByUserIdOrUserNameWithOnlyEmailChanged() {
        // Given
        Users users = constructUser();
        UserDto newUserDTo = new UserDto.builder()
                .userId(users.getUserId())
                .userName(users.getUserName())
                .firstName(users.getFirstName())
                .lastName(users.getLastName())
                .email("updated@gmail.com")
                .password(users.getPassword())
                .profileImage(users.getProfileImage())
                .gender(users.getGender())
                .about(users.getAbout())
                .lastSeen(users.getLastSeen())
                .build();

        // When
        when(userRepository.findByUserIdOrUserName(TEST_UUID, TEST_USER_NAME)).thenReturn(Optional.of(users));
        when(userRepository.save(any())).thenReturn(UserDtoToUsers(newUserDTo));
        doNothing().when(userValidationService).validateFields(anyString(), anyString(), any(), anyString(), any());
        doNothing().when(userValidationService).validateUser(any(), anyString(), any());
        UserDto updatedUser = userService.updateUserByUserIdOrUserName(newUserDTo, TEST_UUID, TEST_USER_NAME);

        // Then
        assertThat(updatedUser.firstName()).isEqualTo(users.getFirstName());
        assertThat(updatedUser.lastName()).isEqualTo(users.getLastName());
        assertThat(updatedUser.email()).isEqualTo(newUserDTo.email());
        assertThat(updatedUser.about()).isEqualTo(users.getAbout());
        assertThat(updatedUser.gender()).isEqualTo(users.getGender());
    }

    @Test
    @DisplayName("Test Happy Path -- updateUserByUserIdOrUserName() With only about updated")
    public void testUpdateUserByUserIdOrUserNameWithOnlyAboutChanged() {
        // Given
        Users users = constructUser();
        UserDto newUserDTo = new UserDto.builder()
                .userId(users.getUserId())
                .userName(users.getUserName())
                .firstName(users.getFirstName())
                .lastName(users.getLastName())
                .email(users.getEmail())
                .password(users.getPassword())
                .profileImage(users.getProfileImage())
                .gender(users.getGender())
                .about(" UPDATED ABOUT SECTION ")
                .lastSeen(users.getLastSeen())
                .build();

        // When
        when(userRepository.findByUserIdOrUserName(TEST_UUID, TEST_USER_NAME)).thenReturn(Optional.of(users));
        when(userRepository.save(any())).thenReturn(UserDtoToUsers(newUserDTo));
        doNothing().when(userValidationService).validateFields(anyString(), anyString(), any(), anyString(), any());
        doNothing().when(userValidationService).validateUser(any(), anyString(), any());
        UserDto updatedUser = userService.updateUserByUserIdOrUserName(newUserDTo, TEST_UUID, TEST_USER_NAME);

        // Then
        assertThat(updatedUser.firstName()).isEqualTo(users.getFirstName());
        assertThat(updatedUser.lastName()).isEqualTo(users.getLastName());
        assertThat(updatedUser.email()).isEqualTo(users.getEmail());
        assertThat(updatedUser.about()).isEqualTo(newUserDTo.about());
        assertThat(updatedUser.gender()).isEqualTo(users.getGender());
    }

    @Test
    @DisplayName("Test Happy Path -- updateUserByUserIdOrUserName() With only gender updated")
    public void testUpdateUserByUserIdOrUserNameWithOnlyGenderChanged() {
        // Given
        Users users = constructUser();
        UserDto newUserDTo = new UserDto.builder()
                .userId(users.getUserId())
                .userName(users.getUserName())
                .firstName(users.getFirstName())
                .lastName(users.getLastName())
                .email(users.getEmail())
                .password(users.getPassword())
                .profileImage(users.getProfileImage())
                .gender(GENDER.NON_BINARY)
                .about(users.getAbout())
                .lastSeen(users.getLastSeen())
                .build();

        // When
        when(userRepository.findByUserIdOrUserName(TEST_UUID, TEST_USER_NAME)).thenReturn(Optional.of(users));
        when(userRepository.save(any())).thenReturn(UserDtoToUsers(newUserDTo));
        doNothing().when(userValidationService).validateFields(anyString(), anyString(), any(), anyString(), any());
        doNothing().when(userValidationService).validateUser(any(), anyString(), any());
        UserDto updatedUser = userService.updateUserByUserIdOrUserName(newUserDTo, TEST_UUID, TEST_USER_NAME);

        // Then
        assertThat(updatedUser.firstName()).isEqualTo(users.getFirstName());
        assertThat(updatedUser.lastName()).isEqualTo(users.getLastName());
        assertThat(updatedUser.email()).isEqualTo(users.getEmail());
        assertThat(updatedUser.about()).isEqualTo(users.getAbout());
        assertThat(updatedUser.gender()).isEqualTo(newUserDTo.gender());
    }

    @Test
    @DisplayName("Test Unhappy Path -- updateUserByUserIdOrUserName() With both UserId & UserName Null")
    public void testUpdateUserByUserIdOrUserNameUnhappyPathWithBothUserIdAndUserNameNull() {
        // Given
        Users users = constructUser();

        // When
        doThrow(new BadApiRequestExceptions(BadApiRequestExceptions.class, "Please provide non null username or user Id", "testUpdateUserByUserIdOrUserNameUnhappyPathWithBothUserIdAndUserNameNull"))
                .when(userValidationService).validateFields(any(), any(), any(), anyString(), any());

        // Then
        assertThrows(BadApiRequestExceptions.class, () -> {
            userService.updateUserByUserIdOrUserName(MappingHelpers.UsersToUsersDto(users), null, null);
        }, "BadApiRequestException should have been thrown");
    }

    @Test
    @DisplayName("Test Unhappy Path -- updateUserByUserIdOrUserName() With both UserId & UserName invalid or not present in db")
    public void testUpdateUserByUserIdOrUserNameUnhappyPathForUserNotFound() {
        // Given
        Users users = constructUser();


        // When
        when(userRepository.findByUserIdOrUserName("INVALID_USER_ID", "INVALID_USER_NAME")).thenReturn(Optional.empty());
        doNothing().when(userValidationService).validateFields(anyString(), anyString(), any(), anyString(), any());
        doThrow(new UserNotFoundExceptions(UserNotFoundExceptions.class, "No User with this UserId or UserName"
                , "testUpdateUserByUserIdOrUserNameUnhappyPathForUserNotFound"))
                .when(userValidationService).validateUser(any(), anyString(), any());

        // Then
        assertThrows(UserNotFoundExceptions.class, () -> {
            userService.updateUserByUserIdOrUserName(MappingHelpers.UsersToUsersDto(users), "INVALID_USER_ID", "INVALID_USER_NAME");
        }, "UserNotFoundException should have been thrown");
    }


    @Test
    @DisplayName("Test Unhappy Path -- updateUserByUserIdOrUserName() With email already existing")
    public void testUpdateUserByUserIdOrUserNameUnhappyPathWithExistingEmail() {
        // Given
        Users users = constructUser();
        UserDto newUserDTo = new UserDto.builder()
                .userId(users.getUserId())
                .userName(users.getUserName())
                .firstName(users.getFirstName())
                .lastName(users.getLastName())
                .email("EXISTING_EMAIL")
                .password(users.getPassword())
                .profileImage(users.getProfileImage())
                .gender(users.getGender())
                .about(users.getAbout())
                .lastSeen(users.getLastSeen())
                .build();

        // When
        when(userRepository.findByUserIdOrUserName(TEST_UUID, TEST_USER_NAME)).thenReturn(Optional.of(users));
        doNothing().when(userValidationService).validateFields(anyString(), anyString(), any(), anyString(), any());
        doNothing().doThrow(new UserExceptions(UserExceptions.class,
                        String.format("There's an account with Email %s EMAIL",newUserDTo.email())
                        ,"testUpdateUserByUserIdOrUserNameUnhappyPathWithExistingEmail"))
                .when(userValidationService).validateUser(any(), anyString(), any());

        // Then
        assertThrows(UserExceptions.class,()->{
            userService.updateUserByUserIdOrUserName(newUserDTo,TEST_UUID,TEST_USER_NAME);
        },"UserExceptions Should have been thrown");
    }

    @Test
    @DisplayName("Test Happy Path -- deleteUserByUserIdOrUserName() With valid UserId & UserName")
    public void testDeleteUserByUserIdOrUserNameHappyPath(){
            // When
            doNothing().when(userValidationService).validateFields(anyString(),anyString(),any(),anyString(),any());
            when(userRepository.findByUserIdOrUserName(TEST_UUID,TEST_USER_NAME)).thenReturn(Optional.of(constructUser()));
            doNothing().when(userValidationService).validateUser(any(),anyString(),any());
            userService.deleteUserByUserIdOrUserName(TEST_UUID,TEST_USER_NAME);

            // Then
            verify(userRepository,times(1)).deleteByUserIdOrUserName(TEST_UUID,TEST_USER_NAME);
    }

    @Test
    @DisplayName("Test Unhappy Path -- deleteUserByUserIdOrUserName() With null UserId & UserName")
    public void testDeleteUserByUserIdOrUserNameUnhappyPathForNullUserIdAndUserName(){
        // When
        doThrow(new BadApiRequestExceptions(BadApiRequestExceptions.class,"Please provide non null username or user Id",
                "testDeleteUserByUserIdOrUserNameUnhappyPath()"))
                .when(userValidationService).validateFields(any(),any(),any(),anyString(),any());

        // Then
        assertThrows(BadApiRequestExceptions.class,()->{ userService.deleteUserByUserIdOrUserName(null,null);},"BadApiRequestException should have been thrown");
    }

    private Set<Users> constructUsersSet() {
        Users user1 = constructUser();
        Users user2 = constructUser();
        return Set.of(user1, user2);
    }

    private UserDto constructIncomingUserDtoRequest() {
        final String NEW_USER_NAME = "NEW_USER_NAME";
        final String NEW_USER_ID = "144d02e1-49ab-417e-8279-ed08c997aed7";
        final String NEW_FIRST_NAME = "NEW_FIRST_NAME";
        final String NEW_LAST_NAME = "NEW_LAST_NAME";
        final String NEW_EMAIL = "NEW_EMAIL";
        final String NEW_PASSWORD = "qwery!@#$%";
        final GENDER NEW_GENDER = FEMALE;
        final String NEW_PROFILE_IMAGE = "144d02e1-49ab-417e-8279-ed08c997aed7.jpg";
        final String NEW_ABOUT = "80000k ke jootey, ismein tera ghar chala jayenga";

        return new UserDto.builder()
                .userId(NEW_USER_ID)
                .userName(NEW_USER_NAME)
                .firstName(NEW_FIRST_NAME)
                .lastName(NEW_LAST_NAME)
                .email(NEW_EMAIL)
                .gender(NEW_GENDER)
                .password(NEW_PASSWORD)
                .about(NEW_ABOUT)
                .profileImage(NEW_PROFILE_IMAGE)
                .lastSeen(null)
                .build();
    }

    private Users constructUser() {
        final String TEST_PASSWORD = "!@123456789aBcd";
        final String TEST_PROFILE_IMAGE = "0ecbe17e-5537-4533-9b9f-b3c2438e58eb.jpg";
        final String TEST_ABOUT = "Lorem ipsum dolor sit amet consectetur adipisicing elit. Maxime mollitia,\n" +
                "molestiae quas vel sint commodi repudiandae consequuntur voluptatum laborum\n" +
                "numquam blanditiis harum quisquam eius sed odit fugiat iusto fuga praesentium\n" +
                "optio, eaque rerum! Provident similique accusantium nemo autem. Veritatis\n" +
                "obcaecati tenetur iure eius earum ut molestias architecto voluptate aliquam\n" +
                "nihil, eveniet aliquid culpa officia aut! Impedit sit sunt quaerat, odit,\n";
        final String ADMIN = "ADMIN";
        return new Users.builder()
                .userId(TEST_UUID)
                .userName(TEST_USER_NAME)
                .firstName(TEST_FIRST_NAME)
                .lastName(TEST_LAST_NAME)
                .email(TEST_EMAIL)
                .password(TEST_PASSWORD)
                .gender(TEST_GENDER)
                .profileImage(TEST_PROFILE_IMAGE)
                .about(TEST_ABOUT)
                .lastSeen(null)
                .createdDate(LocalDate.now())
                .createdBy(ADMIN)
                .build();
    }
}
