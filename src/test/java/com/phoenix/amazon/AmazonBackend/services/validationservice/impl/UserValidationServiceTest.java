//package com.phoenix.amazon.AmazonBackend.services.validationservice.impl;
//
//import com.phoenix.amazon.AmazonBackend.entity.Users;
//import com.phoenix.amazon.AmazonBackend.exceptions.BadApiRequestExceptions;
//import com.phoenix.amazon.AmazonBackend.exceptions.UserExceptions;
//import com.phoenix.amazon.AmazonBackend.exceptions.UserNotFoundExceptions;
//import com.phoenix.amazon.AmazonBackend.repository.IUserRepository;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//import java.time.LocalDate;
//import java.time.LocalDateTime;
//import java.util.Optional;
//import java.util.Set;
//
//import static com.phoenix.amazon.AmazonBackend.helpers.AllConstantHelpers.GENDER;
//import static com.phoenix.amazon.AmazonBackend.helpers.AllConstantHelpers.GENDER.MALE;
//import static com.phoenix.amazon.AmazonBackend.helpers.AllConstantHelpers.USER_VALIDATION.NULL_OBJECT;
//import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
//import static org.junit.jupiter.api.Assertions.assertThrows;
//import static org.mockito.Mockito.when;
//
//@ExtendWith(MockitoExtension.class)
//public class UserValidationServiceTest {
//    @InjectMocks
//    private UserValidationServiceImpl userValidationServiceMock;
//    @Mock
//    private IUserRepository userRepositoryMock;
//
//    @Test
//    @DisplayName("Test Happy Path -- validateUser() For Null Object")
//    public void testValidateUserHappyPathForNullObject() throws UserNotFoundExceptions, UserExceptions, BadApiRequestExceptions {
//        // Given
//        Set<Users> usersSet=constructUsersSet();
//
//        // When
//        when(userRepositoryMock.findAll()).thenReturn(usersSet.stream().toList());
//
//        // Then
//        assertDoesNotThrow(()->{userValidationServiceMock
//                .validateUser(Optional.of(constructUser()), "testValidateUser",NULL_OBJECT);});
//    }
//
//    @Test
//    @DisplayName("Test Unhappy Path -- validateUser() For Null Object")
//    public void testValidateUserUnhappyPathForNullObject(){
//        // Given
//        Set<Users> usersSet=constructUsersSet();
//
//        // When
//        when(userRepositoryMock.findAll()).thenReturn(usersSet.stream().toList());
//        assertThrows(BadApiRequestExceptions.class,()->{
//            userValidationServiceMock.validateUser(Optional.empty(), "testValidateUser",NULL_OBJECT);
//        },"BadApiRequestException should have been thrown");
//    }
//
//    private Set<Users> constructUsersSet() {
//        return Set.of(constructUser(), constructUser());
//    }
//
//    private Users constructUser() {
//        final String TEST_EMAIL = "test@gmail.com";
//        final String TEST_USER_NAME = "TEST_USER_NAME";
//        final String TEST_UUID = "58824409-dd6b-4934-9923-ec1daf9693da";
//        final String TEST_FIRST_NAME = "TEST_FIRST_NAME";
//        final String TEST_LAST_NAME = "TEST_LAST_NAME";
//        final GENDER TEST_GENDER = MALE;
//        final String TEST_PASSWORD = "$2y$10$JUH1QJQnAndkUwMclwdMc.hAbqAZ61Yb/7yCmCeIdNTjAMgC1NhNC";
//        final String TEST_PROFILE_IMAGE = "0ecbe17e-5537-4533-9b9f-b3c2438e58eb.jpg";
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
//                .email(TEST_EMAIL)
//                .password(TEST_PASSWORD)
//                .gender(TEST_GENDER)
//                .profileImage(TEST_PROFILE_IMAGE)
//                .about(TEST_ABOUT)
//                .lastSeen(TEST_LAST_SEEN)
//                .createdDate(LocalDate.now().minusDays(10))
//                .createdBy(ADMIN)
//                .build();
//    }
//}
