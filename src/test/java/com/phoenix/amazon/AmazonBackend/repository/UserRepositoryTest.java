package com.phoenix.amazon.AmazonBackend.repository;


import com.phoenix.amazon.AmazonBackend.entity.Users;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import java.util.Optional;
import java.util.Set;

import static com.phoenix.amazon.AmazonBackend.helpers.AllConstantHelpers.GENDER;
import static com.phoenix.amazon.AmazonBackend.helpers.AllConstantHelpers.GENDER.MALE;
import static com.phoenix.amazon.AmazonBackend.helpers.AllConstantHelpers.GENDER.NON_BINARY;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class UserRepositoryTest {
    @Autowired
    private IUserRepository userRepository;

    private final String TEST_EMAIL = "test@gmail.com";
    private final String TEST_USER_NAME = "TEST_USER_NAME";
    private final String TEST_UUID = "58824409-dd6b-4934-9923-ec1daf9693da";
    private final String TEST_FIRST_NAME = "TEST_FIRST_NAME";
    private final String TEST_LAST_NAME = "TEST_LAST_NAME";
    private final GENDER TEST_GENDER = MALE;

    @BeforeEach
    public void setUp() {
        // Given
        userRepository.save(constructUser());
    }

    @Test
    @DisplayName("Test Happy Path -- findByEmailOrUserName() With Both Valid Fields")
    public void testFindByEmailOrUserNameHappyPathWithBothValidFields() {
        // When
        final String userName = TEST_USER_NAME;
        final String email = TEST_EMAIL;
        Optional<Users> loadedUser = userRepository.findByEmailOrUserName(email, userName);

        // Then
        assertThat(loadedUser.isPresent()).isTrue();
        assertThat(loadedUser.get().getUserName()).isEqualTo(userName);
        assertThat(loadedUser.get().getEmail()).isEqualTo(email);
    }

    @Test
    @DisplayName("Test Happy Path -- findByEmailOrUserName() With Valid UserName Only")
    public void testFindByEmailOrUserNameHappyPathWithValidUserName() {
        // When
        final String userName = TEST_USER_NAME;
        final String email = "INVALID_EMAIL";
        Optional<Users> loadedUser = userRepository.findByEmailOrUserName(email, userName);

        // Then
        assertThat(loadedUser.isPresent()).isTrue();
        assertThat(loadedUser.get().getUserName()).isEqualTo(userName);
    }

    @Test
    @DisplayName("Test Happy Path -- findByEmailOrUserName() With Valid Email Only")
    public void testFindByEmailOrUserNameHappyPathWithValidEmail() {
        // When
        final String userName = "INVALID_USER_NAME";
        final String email = TEST_EMAIL;
        Optional<Users> loadedUser = userRepository.findByEmailOrUserName(email, userName);

        // Then
        assertThat(loadedUser.isPresent()).isTrue();
        assertThat(loadedUser.get().getEmail()).isEqualTo(email);
    }

    @Test
    @DisplayName("Test Unhappy Path -- findByEmailOrUserName() With both invalid fields")
    public void testFindByEmailOrUserNameUnhappyPath() {
        // When
        final String userName = "INVALID_USER_NAME";
        final String email = "INVALID_EMAIL";
        Optional<Users> loadedUser = userRepository.findByEmailOrUserName(email, userName);

        // Then
        assertThat(loadedUser.isEmpty()).isTrue();
    }

    @Test
    @DisplayName("Test Happy Path -- findByUserIdOrUserName() With both Valid fields")
    public void testFindByUserIdOrUserNameHappyPathWithBothValidFields() {
        // When
        final String userId = TEST_UUID;
        final String userName = TEST_USER_NAME;
        Optional<Users> loadedUser = userRepository.findByUserIdOrUserName(userId, userName);

        // Then
        assertThat(loadedUser.isPresent()).isTrue();
        assertThat(loadedUser.get().getUserId()).isEqualTo(userId);
        assertThat(loadedUser.get().getUserName()).isEqualTo(userName);
    }

    @Test
    @DisplayName("Test Happy Path -- findByUserIdOrUserName() With Valid UserId only")
    public void testFindByUserIdOrUserNameHappyPathWithValidUserId() {
        // When
        final String userId = TEST_UUID;
        final String userName = "INVALID_USER_NAME";
        Optional<Users> loadedUser = userRepository.findByUserIdOrUserName(userId, userName);

        // Then
        assertThat(loadedUser.isPresent()).isTrue();
        assertThat(loadedUser.get().getUserId()).isEqualTo(userId);
    }

    @Test
    @DisplayName("Test Happy Path -- findByUserIdOrUserName() With Valid UserName only")
    public void testFindByUserIdOrUserNameHappyPathWithValidUserName() {
        // When
        final String userId = "INVALID_USER_ID";
        final String userName = TEST_USER_NAME;
        Optional<Users> loadedUser = userRepository.findByUserIdOrUserName(userId, userName);

        // Then
        assertThat(loadedUser.isPresent()).isTrue();
        assertThat(loadedUser.get().getUserName()).isEqualTo(userName);
    }

    @Test
    @DisplayName("Test UnHappy Path -- findByUserIdOrUserName() With both invalid fields")
    public void testFindByUserIdOrUserNameUnhappyPath() {
        // When
        final String userId = "INVALID_USER_ID";
        final String userName = "INVALID_USER_NAME";
        Optional<Users> loadedUser = userRepository.findByUserIdOrUserName(userId, userName);

        // Then
        assertThat(loadedUser.isEmpty()).isTrue();
    }

    @Test
    @DisplayName("Test Happy Path -- deleteByUserIdOrUserName() With both Valid fields")
    public void testDeleteByUserIdOrUserNameHappyPathWithBothValidFields() {
        // When
        final String userId = TEST_UUID;
        final String userName = TEST_USER_NAME;
        userRepository.deleteByUserIdOrUserName(userId, userName);

        // Then
        assertThat(userRepository.findByUserIdOrUserName(userId, userName).isEmpty()).isTrue();
    }

    @Test
    @DisplayName("Test Happy Path -- deleteByUserIdOrUserName() With Valid UserId Only")
    public void testDeleteByUserIdOrUserNameHappyPathWithValidUserId() {
        // When
        final String userId = TEST_UUID;
        final String userName = "INVALID_USER_NAME";
        userRepository.deleteByUserIdOrUserName(userId, userName);

        // Then
        assertThat(userRepository.findByUserIdOrUserName(userId, userName).isEmpty()).isTrue();
    }

    @Test
    @DisplayName("Test Happy Path -- deleteByUserIdOrUserName() With Valid UserName Only")
    public void testDeleteByUserIdOrUserNameHappyPathWithValidUserName() {
        // When
        final String userId = "INVALID_USER_ID";
        final String userName = TEST_USER_NAME;
        userRepository.deleteByUserIdOrUserName(userId, userName);

        // Then
        assertThat(userRepository.findByUserIdOrUserName(userId, userName).isEmpty()).isTrue();
    }

    @Test
    @DisplayName("Test Unhappy Path -- deleteByUserIdOrUserName() With both invalid fields")
    public void testDeleteByUserIdOrUserNameUnhappyPath() {
        // When
        final String userId = "INVALID_USER_ID";
        final String userName = "INVALID_USER_NAME";
        userRepository.deleteByUserIdOrUserName(userId, userName);

        // Then
        assertThat(userRepository.findByUserIdOrUserName(TEST_UUID, TEST_USER_NAME).isPresent()).isTrue();
    }

    @Test
    @DisplayName("Test Happy Path -- findAllByUserNameContaining() With Valid field")
    public void testFindAllByUserNameContainingHappyPath() {
        // Given
        userRepository.saveAll(constructUsersSet());

        // When
        final String USER_NAME = "TEST_USER_NAME";
        Optional<Set<Users>> usersSetOptional = userRepository.findAllByUserNameContaining(USER_NAME);

        // Then
        assertThat(usersSetOptional.isPresent()).isTrue();
        assertThat(usersSetOptional.get().isEmpty()).isFalse();
    }

    @Test
    @DisplayName("Test Unhappy Path -- findAllByUserNameContaining() With invalid field")
    public void testFindAllByUserNameContainingUnhappyPath() {
        // Given
        userRepository.saveAll(constructUsersSet());

        // When
        final String USER_NAME = "INVALID_USER_NAME";
        Optional<Set<Users>> usersSetOptional = userRepository.findAllByUserNameContaining(USER_NAME);

        // Then
        assertThat(usersSetOptional.isPresent()).isTrue();
        assertThat(usersSetOptional.get().isEmpty()).isTrue();
    }


    @Test
    @DisplayName("Test Happy Path -- searchUserByFirstName() With valid firstName")
    public void testSearchUserByFirstNameHappyPath() {
        // When
        Optional<Set<Users>> usersSetOptional = userRepository.searchUserByFirstName(TEST_FIRST_NAME);

        // Then
        assertThat(usersSetOptional.isPresent()).isTrue();
        assertThat(usersSetOptional.get().isEmpty()).isFalse();
        assertThat(usersSetOptional.get().stream().toList().getFirst().getFirstName()).isEqualTo(TEST_FIRST_NAME);
    }

    @Test
    @DisplayName("Test Unhappy Path -- searchUserByFirstName() With invalid firstName")
    public void testSearchUserByFirstNameUnhappyPath() {
        // When
        Optional<Set<Users>> usersSetOptional = userRepository.searchUserByFirstName("INVALID_FIRST_NAME");

        // Then
        assertThat(usersSetOptional.isPresent()).isTrue();
        assertThat(usersSetOptional.get().isEmpty()).isTrue();
    }


    @Test
    @DisplayName("Test Happy Path -- searchUserByLastName() With valid lastName")
    public void testSearchUserByLastNameHappyPath() {
        // When
        Optional<Set<Users>> usersSetOptional = userRepository.searchUserByLastName(TEST_LAST_NAME);

        // Then
        assertThat(usersSetOptional.isPresent()).isTrue();
        assertThat(usersSetOptional.get().isEmpty()).isFalse();
        assertThat(usersSetOptional.get().stream().toList().getFirst().getLastName()).isEqualTo(TEST_LAST_NAME);
    }

    @Test
    @DisplayName("Test Unhappy Path -- searchUserByLastName() With invalid lastName")
    public void testSearchUserByLastNameUnhappyPath() {
        // When
        Optional<Set<Users>> usersSetOptional = userRepository.searchUserByLastName("INVALID_LAST_NAME");

        // Then
        assertThat(usersSetOptional.isPresent()).isTrue();
        assertThat(usersSetOptional.get().isEmpty()).isTrue();
    }

    @Test
    @DisplayName("Test Happy Path -- searchUserByGender() With valid gender")
    public void testSearchUserByGenderHappyPath() {
        // When
        Optional<Set<Users>> usersSetOptional = userRepository.searchUserByGender(TEST_GENDER.toString());

        // Then
        assertThat(usersSetOptional.isPresent()).isTrue();
        assertThat(usersSetOptional.get().isEmpty()).isFalse();
        assertThat(usersSetOptional.get().stream().toList().getFirst().getGender()).isEqualTo(TEST_GENDER);
    }

    @Test
    @DisplayName("Test Unhappy Path -- searchUserByGender() With invalid gender")
    public void testSearchUserByGenderUnhappyPath() {
        // When
        Optional<Set<Users>> usersSetOptional = userRepository.searchUserByGender(NON_BINARY.toString());

        // Then
        assertThat(usersSetOptional.isPresent()).isTrue();
        assertThat(usersSetOptional.get().isEmpty()).isTrue();
    }

    @Test
    @DisplayName("Test Happy Path -- searchUserByUserName() With valid userName")
    public void testSearchUserByUserNameHappyPath() {
        // When
        Optional<Set<Users>> usersSetOptional = userRepository.searchUserByUserName(TEST_USER_NAME);

        // Then
        assertThat(usersSetOptional.isPresent()).isTrue();
        assertThat(usersSetOptional.get().isEmpty()).isFalse();
        assertThat(usersSetOptional.get().stream().toList().getFirst().getUserName()).isEqualTo(TEST_USER_NAME);
    }

    @Test
    @DisplayName("Test Unhappy Path -- searchUserByUserName() With invalid userName")
    public void testSearchUserByUserNameUnhappyPath() {
        // When
        Optional<Set<Users>> usersSetOptional = userRepository.searchUserByUserName("INVALID_USER_NAME");

        // Then
        assertThat(usersSetOptional.isPresent()).isTrue();
        assertThat(usersSetOptional.get().isEmpty()).isTrue();
    }

    @Test
    @DisplayName("Test Happy Path -- searchUserByEmail() With valid email")
    public void testSearchUserByEmailHappyPath() {
        // When
        Optional<Set<Users>> usersSetOptional = userRepository.searchUserByEmail(TEST_EMAIL);

        // Then
        assertThat(usersSetOptional.isPresent()).isTrue();
        assertThat(usersSetOptional.get().isEmpty()).isFalse();
        assertThat(usersSetOptional.get().stream().toList().getFirst().getEmail()).isEqualTo(TEST_EMAIL);
    }

    @Test
    @DisplayName("Test Unhappy Path -- searchUserByEmail() With invalid email")
    public void testSearchUserByEmailUnhappyPath() {
        // When
        Optional<Set<Users>> usersSetOptional = userRepository.searchUserByEmail("INVALID_EMAIL");

        // Then
        assertThat(usersSetOptional.isPresent()).isTrue();
        assertThat(usersSetOptional.get().isEmpty()).isTrue();
    }

    private Set<Users> constructUsersSet() {
        Users user1 = constructUser();
        Users user2 = constructUser();
        return Set.of(user1, user2);
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