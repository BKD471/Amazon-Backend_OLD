package com.phoenix.amazon.AmazonBackend.repository;


import com.phoenix.amazon.AmazonBackend.entity.Users;
import org.junit.jupiter.api.BeforeEach;
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
    private final String TEST_USER_NAME_1 = "TEST_USER_NAME_1";
    private final String TEST_USER_NAME_2 = "TEST_USER_NAME_2";
    private final String TEST_UUID = "58824409-dd6b-4934-9923-ec1daf9693da";
    private final String TEST_FIRST_NAME = "TEST_FIRST_NAME";
    private final String TEST_LAST_NAME = "TEST_LAST_NAME";
    private final String TEST_PASSWORD = "!@123456789aBcd";
    private final GENDER TEST_GENDER = MALE;
    private final String TEST_PROFILE_IMAGE = "0ecbe17e-5537-4533-9b9f-b3c2438e58eb.jpg";
    private final String TEST_ABOUT = "Lorem ipsum dolor sit amet consectetur adipisicing elit. Maxime mollitia,\n" +
            "molestiae quas vel sint commodi repudiandae consequuntur voluptatum laborum\n" +
            "numquam blanditiis harum quisquam eius sed odit fugiat iusto fuga praesentium\n" +
            "optio, eaque rerum! Provident similique accusantium nemo autem. Veritatis\n" +
            "obcaecati tenetur iure eius earum ut molestias architecto voluptate aliquam\n" +
            "nihil, eveniet aliquid culpa officia aut! Impedit sit sunt quaerat, odit,\n";
    private final String ADMIN = "ADMIN";

    @BeforeEach
    public void setUp() {
        // Given
        userRepository.save(constructUser());
    }

    @Test
    public void testFindByEmailOrUserNameHappyPathWithBothValidFields() {
        // When
        final String userName = TEST_USER_NAME_1;
        final String email = TEST_EMAIL;
        Optional<Users> loadedUser = userRepository.findByEmailOrUserName(email, userName);

        // Then
        assertThat(loadedUser.isPresent()).isTrue();
        assertThat(loadedUser.get().getUserName()).isEqualTo(userName);
        assertThat(loadedUser.get().getEmail()).isEqualTo(email);
    }

    @Test
    public void testFindByEmailOrUserNameHappyPathWithValidUserName() {
        // When
        final String userName = TEST_USER_NAME_1;
        final String email = "INVALID_EMAIL";
        Optional<Users> loadedUser = userRepository.findByEmailOrUserName(email, userName);

        // Then
        assertThat(loadedUser.isPresent()).isTrue();
        assertThat(loadedUser.get().getUserName()).isEqualTo(userName);
    }

    @Test
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
    public void testFindByEmailOrUserNameUnhappyPath() {
        // When
        final String userName = "INVALID_USER_NAME";
        final String email = "INVALID_EMAIL";
        Optional<Users> loadedUser = userRepository.findByEmailOrUserName(email, userName);

        // Then
        assertThat(loadedUser.isEmpty()).isTrue();
    }

    @Test
    public void testFindByUserIdOrUserNameHappyPathWithBothValidFields() {
        // When
        final String userId = TEST_UUID;
        final String userName = TEST_USER_NAME_1;
        Optional<Users> loadedUser = userRepository.findByUserIdOrUserName(userId, userName);

        // Then
        assertThat(loadedUser.isPresent()).isTrue();
        assertThat(loadedUser.get().getUserId()).isEqualTo(userId);
        assertThat(loadedUser.get().getUserName()).isEqualTo(userName);
    }

    @Test
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
    public void testFindByUserIdOrUserNameHappyPathWithValidUserName() {
        // When
        final String userId = "INVALID_USER_ID";
        final String userName = TEST_USER_NAME_1;
        Optional<Users> loadedUser = userRepository.findByUserIdOrUserName(userId, userName);

        // Then
        assertThat(loadedUser.isPresent()).isTrue();
        assertThat(loadedUser.get().getUserName()).isEqualTo(userName);
    }

    @Test
    public void testFindByUserIdOrUserNameUnhappyPath() {
        // When
        final String userId = "INVALID_USER_ID";
        final String userName = "INVALID_USER_NAME";
        Optional<Users> loadedUser = userRepository.findByUserIdOrUserName(userId, userName);

        // Then
        assertThat(loadedUser.isEmpty()).isTrue();
    }

    @Test
    public void testDeleteByUserIdOrUserNameHappyPathWithBothValidFields() {
        // When
        final String userId = TEST_UUID;
        final String userName = TEST_USER_NAME_1;
        userRepository.deleteByUserIdOrUserName(userId, userName);

        // Then
        assertThat(userRepository.findByUserIdOrUserName(userId, userName).isEmpty()).isTrue();
    }

    @Test
    public void testDeleteByUserIdOrUserNameHappyPathWithValidUserId() {
        // When
        final String userId = TEST_UUID;
        final String userName = "INVALID_USER_NAME";
        userRepository.deleteByUserIdOrUserName(userId, userName);

        // Then
        assertThat(userRepository.findByUserIdOrUserName(userId, userName).isEmpty()).isTrue();
    }

    @Test
    public void testDeleteByUserIdOrUserNameHappyPathWithValidUserName() {
        // When
        final String userId = "INVALID_USER_ID";
        final String userName = TEST_USER_NAME_1;
        userRepository.deleteByUserIdOrUserName(userId, userName);

        // Then
        assertThat(userRepository.findByUserIdOrUserName(userId, userName).isEmpty()).isTrue();
    }

    @Test
    public void testDeleteByUserIdOrUserNameUnhappyPath() {
        // When
        final String userId = "INVALID_USER_ID";
        final String userName = "INVALID_USER_NAME";
        userRepository.deleteByUserIdOrUserName(userId, userName);

        // Then
        assertThat(userRepository.findByUserIdOrUserName(TEST_UUID, TEST_USER_NAME_1).isPresent()).isTrue();
    }

    @Test
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
    public void testSearchUserByFirstNameHappyPath() {
        // When
        Optional<Set<Users>> usersSetOptional = userRepository.searchUserByFirstName(TEST_FIRST_NAME);

        // Then
        assertThat(usersSetOptional.isPresent()).isTrue();
        assertThat(usersSetOptional.get().isEmpty()).isFalse();
        assertThat(usersSetOptional.get().stream().toList().getFirst().getFirstName()).isEqualTo(TEST_FIRST_NAME);
    }

    @Test
    public void testSearchUserByFirstNameUnhappyPath() {
        // When
        Optional<Set<Users>> usersSetOptional = userRepository.searchUserByFirstName("INVALID_FIRST_NAME");

        // Then
        assertThat(usersSetOptional.isPresent()).isTrue();
        assertThat(usersSetOptional.get().isEmpty()).isTrue();
    }


    @Test
    public void testSearchUserByLastNameHappyPath() {
        // When
        Optional<Set<Users>> usersSetOptional = userRepository.searchUserByLastName(TEST_LAST_NAME);

        // Then
        assertThat(usersSetOptional.isPresent()).isTrue();
        assertThat(usersSetOptional.get().isEmpty()).isFalse();
        assertThat(usersSetOptional.get().stream().toList().getFirst().getLastName()).isEqualTo(TEST_LAST_NAME);
    }

    @Test
    public void testSearchUserByLastNameUnhappyPath() {
        // When
        Optional<Set<Users>> usersSetOptional = userRepository.searchUserByLastName("INVALID_LAST_NAME");

        // Then
        assertThat(usersSetOptional.isPresent()).isTrue();
        assertThat(usersSetOptional.get().isEmpty()).isTrue();
    }

    @Test
    public void testSearchUserByGenderHappyPath() {
        // When
        Optional<Set<Users>> usersSetOptional = userRepository.searchUserByGender(TEST_GENDER.toString());

        // Then
        assertThat(usersSetOptional.isPresent()).isTrue();
        assertThat(usersSetOptional.get().isEmpty()).isFalse();
        assertThat(usersSetOptional.get().stream().toList().getFirst().getGender()).isEqualTo(TEST_GENDER);
    }

    @Test
    public void testSearchUserByGenderUnhappyPath() {
        // When
        Optional<Set<Users>> usersSetOptional = userRepository.searchUserByGender(NON_BINARY.toString());

        // Then
        assertThat(usersSetOptional.isPresent()).isTrue();
        assertThat(usersSetOptional.get().isEmpty()).isTrue();
    }

    @Test
    public void testSearchUserByUserNameHappyPath() {
        // When
        Optional<Set<Users>> usersSetOptional = userRepository.searchUserByUserName(TEST_USER_NAME_1);

        // Then
        assertThat(usersSetOptional.isPresent()).isTrue();
        assertThat(usersSetOptional.get().isEmpty()).isFalse();
        assertThat(usersSetOptional.get().stream().toList().getFirst().getUserName()).isEqualTo(TEST_USER_NAME_1);
    }

    @Test
    public void testSearchUserByUserNameUnhappyPath() {
        // When
        Optional<Set<Users>> usersSetOptional = userRepository.searchUserByUserName("INVALID_USER_NAME");

        // Then
        assertThat(usersSetOptional.isPresent()).isTrue();
        assertThat(usersSetOptional.get().isEmpty()).isTrue();
    }

    @Test
    public void testSearchUserByEmailHappyPath() {
        // When
        Optional<Set<Users>> usersSetOptional = userRepository.searchUserByEmail(TEST_EMAIL);

        // Then
        assertThat(usersSetOptional.isPresent()).isTrue();
        assertThat(usersSetOptional.get().isEmpty()).isFalse();
        assertThat(usersSetOptional.get().stream().toList().getFirst().getEmail()).isEqualTo(TEST_EMAIL);
    }

    @Test
    public void testSearchUserByEmailUnhappyPath() {
        // When
        Optional<Set<Users>> usersSetOptional = userRepository.searchUserByEmail("INVALID_EMAIL");

        // Then
        assertThat(usersSetOptional.isPresent()).isTrue();
        assertThat(usersSetOptional.get().isEmpty()).isTrue();
    }

    private Set<Users> constructUsersSet() {
        Users user1 = new Users.builder()
                .userId(TEST_UUID)
                .userName(TEST_USER_NAME_1)
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
        Users user2 = new Users.builder()
                .userId(TEST_UUID)
                .userName(TEST_USER_NAME_2)
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
        return Set.of(user1, user2);
    }

    private Users constructUser() {
        return new Users.builder()
                .userId(TEST_UUID)
                .userName(TEST_USER_NAME_1)
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