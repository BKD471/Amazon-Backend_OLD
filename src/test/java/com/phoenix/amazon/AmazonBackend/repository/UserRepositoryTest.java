package com.phoenix.amazon.AmazonBackend.repository;


import com.phoenix.amazon.AmazonBackend.entity.Users;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

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
    private IUserRepository userRepositoryMock;
    private final String TEST_PRIMARY_EMAIL = "testprimary@gmail.com";
    private final String TEST_SECONDARY_EMAIL = "testsecondary@gmail.com";
    private final String TEST_USER_NAME = "TEST_USER_NAME";
    private final String TEST_UUID = "58824409-dd6b-4934-9923-ec1daf9693da";
    private final String TEST_FIRST_NAME = "TEST_FIRST_NAME";
    private final String TEST_LAST_NAME = "TEST_LAST_NAME";
    private final GENDER TEST_GENDER = MALE;
    private final Pageable TEST_PAGEABLE = Pageable.ofSize(2);

    @BeforeEach
    public void setUp() {
        userRepositoryMock.deleteAll();
    }

    @Test
    @DisplayName("Test Happy Path -- findByUserIdOrUserNameOrPrimaryEmail() With all Valid Fields")
    public void testFindByUserIdOrUserNameOrPrimaryEmailHappyPathWithAllValidFields() {
        // Given
        userRepositoryMock.save(constructUser());

        // When
        final String userName = TEST_USER_NAME;
        final String userId = TEST_UUID;
        final String primaryEmail = TEST_PRIMARY_EMAIL;
        Optional<Users> loadedUser = userRepositoryMock.findByUserIdOrUserNameOrPrimaryEmail(userId, userName, primaryEmail);

        // Then
        assertThat(loadedUser.isPresent()).isTrue();
        assertThat(loadedUser.get().getUserId()).isEqualTo(userId);
        assertThat(loadedUser.get().getUserName()).isEqualTo(userName);
        assertThat(loadedUser.get().getPrimaryEmail()).isEqualTo(primaryEmail);
    }

    @Test
    @DisplayName("Test Happy Path -- findByUserIdOrUserNameOrPrimaryEmail() With Valid userId Only")
    public void testFindByUserIdOrUserNameOrPrimaryEmailHappyPathWithValidUserId() {
        // Given
        userRepositoryMock.save(constructUser());

        // When
        final String userId = TEST_UUID;
        final String userName = "INVALID_USER_NAME";
        final String primaryEmail = "INVALID_EMAIL";
        Optional<Users> loadedUser = userRepositoryMock.findByUserIdOrUserNameOrPrimaryEmail(userId, userName, primaryEmail);

        // Then
        assertThat(loadedUser.isPresent()).isTrue();
        assertThat(loadedUser.get().getUserId()).isEqualTo(userId);
    }

    @Test
    @DisplayName("Test Happy Path -- findByUserIdOrUserNameOrPrimaryEmail() With Valid userName Only")
    public void testFindByUserIdOrUserNameOrPrimaryEmailHappyPathWithValidUserName() {
        // Given
        userRepositoryMock.save(constructUser());

        // When
        final String userId = "INVALID_USER_ID";
        final String userName = TEST_USER_NAME;
        final String primaryEmail = "INVALID_EMAIL";
        Optional<Users> loadedUser = userRepositoryMock.findByUserIdOrUserNameOrPrimaryEmail(userId, userName, primaryEmail);

        // Then
        assertThat(loadedUser.isPresent()).isTrue();
        assertThat(loadedUser.get().getUserName()).isEqualTo(userName);
    }

    @Test
    @DisplayName("Test Happy Path -- findByUserIdOrUserNameOrPrimaryEmail() With Valid primaryEmail Only")
    public void testFindByUserIdOrUserNameOrPrimaryEmailHappyPathWithValidPrimaryEmail() {
        // Given
        userRepositoryMock.save(constructUser());

        // When
        final String userId = "INVALID_USER_ID";
        final String userName = "INVALID_USER_NAME";
        final String primaryEmail = TEST_PRIMARY_EMAIL;
        Optional<Users> loadedUser = userRepositoryMock.findByUserIdOrUserNameOrPrimaryEmail(userId, userName, primaryEmail);

        // Then
        assertThat(loadedUser.isPresent()).isTrue();
        assertThat(loadedUser.get().getPrimaryEmail()).isEqualTo(primaryEmail);
    }


    @Test
    @DisplayName("Test Unhappy Path -- findByUserIdOrUserNameOrPrimaryEmail() With all invalid fields")
    public void testFindByUserIdOrUserNameOrPrimaryEmailUnhappyPath() {
        // Given
        userRepositoryMock.save(constructUser());

        // When
        final String userId = "INVALID_USER_ID";
        final String userName = "INVALID_USER_NAME";
        final String primaryEmail = "INVALID_PRIMARY_EMAIL";
        Optional<Users> loadedUser = userRepositoryMock.findByUserIdOrUserNameOrPrimaryEmail(userId, userName, primaryEmail);

        // Then
        assertThat(loadedUser.isEmpty()).isTrue();
    }


    @Test
    @DisplayName("Test Happy Path -- deleteByUserIdOrUserNameOrPrimaryEmail() With all Valid fields")
    public void testDeleteByUserIdOrUserNameOrPrimaryEmailHappyPathWithBothValidFields() {
        // Given
        userRepositoryMock.save(constructUser());

        // When
        final String userId = TEST_UUID;
        final String userName = TEST_USER_NAME;
        final String primaryEmail = TEST_PRIMARY_EMAIL;
        userRepositoryMock.deleteByUserIdOrUserNameOrPrimaryEmail(userId, userName, primaryEmail);

        // Then
        assertThat(userRepositoryMock.findByUserIdOrUserNameOrPrimaryEmail(userId, userName, primaryEmail).isEmpty()).isTrue();
    }

    @Test
    @DisplayName("Test Happy Path -- deleteByUserIdOrUserNameOrPrimaryEmail() With Valid UserId Only")
    public void testDeleteByUserIdOrUserNameOrPrimaryEmailHappyPathWithValidUserId() {
        // Given
        userRepositoryMock.save(constructUser());

        // When
        final String userId = TEST_UUID;
        final String userName = "INVALID_USER_NAME";
        final String primaryEMail = "INVALID_PRIMARY_EMAIL";
        userRepositoryMock.deleteByUserIdOrUserNameOrPrimaryEmail(userId, userName, primaryEMail);

        // Then
        assertThat(userRepositoryMock.findByUserIdOrUserNameOrPrimaryEmail(userId, userName, primaryEMail).isEmpty()).isTrue();
    }

    @Test
    @DisplayName("Test Happy Path -- deleteByUserIdOrUserNameOrPrimaryEmail() With Valid UserName Only")
    public void testDeleteByUserIdOrUserNameOrPrimaryEmailHappyPathWithValidUserName() {
        // Given
        userRepositoryMock.save(constructUser());

        // When
        final String userId = "INVALID_USER_ID";
        final String userName = TEST_USER_NAME;
        final String primaryEmail = "INVALID_PRIMARY_EMAIL";
        userRepositoryMock.deleteByUserIdOrUserNameOrPrimaryEmail(userId, userName, primaryEmail);

        // Then
        assertThat(userRepositoryMock.findByUserIdOrUserNameOrPrimaryEmail(userId, userName, primaryEmail)
                .isEmpty()).isTrue();
    }

    //
    @Test
    @DisplayName("Test Unhappy Path -- deleteByUserIdOrUserNameOrPrimaryEmail() With all invalid fields")
    public void testDeleteByUserIdOrUserNameOrPrimaryEmailUnhappyPath() {
        // Given
        userRepositoryMock.save(constructUser());

        // When
        final String userId = "INVALID_USER_ID";
        final String userName = "INVALID_USER_NAME";
        final String primaryEmail = "INVALID_PRIMARY_EMAIL";
        userRepositoryMock.deleteByUserIdOrUserNameOrPrimaryEmail(userId, userName, primaryEmail);

        // Then
        assertThat(userRepositoryMock.findByUserIdOrUserNameOrPrimaryEmail(TEST_UUID, TEST_USER_NAME, TEST_PRIMARY_EMAIL)
                .isPresent()).isTrue();
    }

    @Test
    @DisplayName("Test Happy Path -- findAllByUserNameContaining() With Valid field")
    public void testFindAllByUserNameContainingHappyPath() {
        // Given
        userRepositoryMock.saveAll(constructUsersSet());
        final String userName = "TEST_USER_NAME";

        // When
        Optional<Page<Users>> usersSetOptional = userRepositoryMock.findAllByUserNameContaining(userName, TEST_PAGEABLE);

        // Then
        assertThat(usersSetOptional.isPresent()).isTrue();
        assertThat(usersSetOptional.get().isEmpty()).isFalse();
    }

    @Test
    @DisplayName("Test Unhappy Path -- findAllByUserNameContaining() With invalid field")
    public void testFindAllByUserNameContainingUnhappyPath() {
        // Given
        userRepositoryMock.saveAll(constructUsersSet());
        final String userName = "INVALID_USER_NAME";

        // When
        Optional<Page<Users>> usersSetOptional = userRepositoryMock.findAllByUserNameContaining(userName, TEST_PAGEABLE);

        // Then
        assertThat(usersSetOptional.isPresent()).isTrue();
        assertThat(usersSetOptional.get().isEmpty()).isTrue();
    }


    @Test
    @DisplayName("Test Happy Path -- searchUserByFirstName() With valid firstName")
    public void testSearchUserByFirstNameHappyPath() {
        // Given
        userRepositoryMock.saveAll(constructUsersSet());

        // When
        Optional<Page<Users>> usersSetOptional = userRepositoryMock.searchUserByFirstName(TEST_FIRST_NAME, TEST_PAGEABLE);

        // Then
        assertThat(usersSetOptional.isPresent()).isTrue();
        assertThat(usersSetOptional.get().isEmpty()).isFalse();
        assertThat(usersSetOptional.get().stream().toList().getFirst().getFirstName()).isEqualTo(TEST_FIRST_NAME);
    }

    @Test
    @DisplayName("Test Unhappy Path -- searchUserByFirstName() With invalid firstName")
    public void testSearchUserByFirstNameUnhappyPath() {
        // Given
        userRepositoryMock.saveAll(constructUsersSet());

        // When
        Optional<Page<Users>> usersSetOptional = userRepositoryMock.searchUserByFirstName("INVALID_FIRST_NAME", TEST_PAGEABLE);

        // Then
        assertThat(usersSetOptional.isPresent()).isTrue();
        assertThat(usersSetOptional.get().isEmpty()).isTrue();
    }


    @Test
    @DisplayName("Test Happy Path -- searchUserByLastName() With valid lastName")
    public void testSearchUserByLastNameHappyPath() {
        // Given
        userRepositoryMock.saveAll(constructUsersSet());

        // When
        Optional<Page<Users>> usersSetOptional = userRepositoryMock.searchUserByLastName(TEST_LAST_NAME, TEST_PAGEABLE);

        // Then
        assertThat(usersSetOptional.isPresent()).isTrue();
        assertThat(usersSetOptional.get().isEmpty()).isFalse();
        assertThat(usersSetOptional.get().stream().toList().getFirst().getLastName()).isEqualTo(TEST_LAST_NAME);
    }

    @Test
    @DisplayName("Test Unhappy Path -- searchUserByLastName() With invalid lastName")
    public void testSearchUserByLastNameUnhappyPath() {
        // Given
        userRepositoryMock.saveAll(constructUsersSet());

        // When
        Optional<Page<Users>> usersSetOptional = userRepositoryMock.searchUserByLastName("INVALID_LAST_NAME", TEST_PAGEABLE);

        // Then
        assertThat(usersSetOptional.isPresent()).isTrue();
        assertThat(usersSetOptional.get().isEmpty()).isTrue();
    }

    @Test
    @DisplayName("Test Happy Path -- searchUserByGender() With valid gender")
    public void testSearchUserByGenderHappyPath() {
        // Given
        userRepositoryMock.saveAll(constructUsersSet());

        // When
        Optional<Page<Users>> usersSetOptional = userRepositoryMock.searchUserByGender(TEST_GENDER.toString(), TEST_PAGEABLE);

        // Then
        assertThat(usersSetOptional.isPresent()).isTrue();
        assertThat(usersSetOptional.get().isEmpty()).isFalse();
        assertThat(usersSetOptional.get().stream().toList().getFirst().getGender()).isEqualTo(TEST_GENDER);
    }

    @Test
    @DisplayName("Test Unhappy Path -- searchUserByGender() With not available gender")
    public void testSearchUserByGenderUnhappyPath() {
        // Given
        userRepositoryMock.saveAll(constructUsersSet());

        // When
        Optional<Page<Users>> usersSetOptional = userRepositoryMock.searchUserByGender(NON_BINARY.toString(), TEST_PAGEABLE);

        // Then
        assertThat(usersSetOptional.isPresent()).isTrue();
        assertThat(usersSetOptional.get().isEmpty()).isTrue();
    }

    @Test
    @DisplayName("Test Happy Path -- searchUserByUserName() With valid userName")
    public void testSearchUserByUserNameHappyPath() {
        // Given
        userRepositoryMock.saveAll(constructUsersSet());

        // When
        Optional<Page<Users>> usersSetOptional = userRepositoryMock.searchUserByUserName(TEST_USER_NAME, TEST_PAGEABLE);

        // Then
        assertThat(usersSetOptional.isPresent()).isTrue();
        assertThat(usersSetOptional.get().isEmpty()).isFalse();
        assertThat(usersSetOptional.get().stream().toList().getFirst().getUserName()).isEqualTo(TEST_USER_NAME);
    }

    @Test
    @DisplayName("Test Unhappy Path -- searchUserByUserName() With invalid userName")
    public void testSearchUserByUserNameUnhappyPath() {
        // Given
        userRepositoryMock.saveAll(constructUsersSet());

        // When
        Optional<Page<Users>> usersSetOptional = userRepositoryMock.searchUserByUserName("INVALID_USER_NAME", TEST_PAGEABLE);

        // Then
        assertThat(usersSetOptional.isPresent()).isTrue();
        assertThat(usersSetOptional.get().isEmpty()).isTrue();
    }

    @Test
    @DisplayName("Test Happy Path -- searchUserByEmail() With valid email")
    public void testSearchUserByEmailHappyPath() {
        // Given
        userRepositoryMock.saveAll(constructUsersSet());

        // When
        Optional<Page<Users>> usersSetOptional = userRepositoryMock.searchUserByEmail(TEST_PRIMARY_EMAIL, TEST_PAGEABLE);

        // Then
        assertThat(usersSetOptional.isPresent()).isTrue();
        assertThat(usersSetOptional.get().isEmpty()).isFalse();
        assertThat(usersSetOptional.get().stream().toList().getFirst().getPrimaryEmail()).isEqualTo(TEST_PRIMARY_EMAIL);
    }

    @Test
    @DisplayName("Test Unhappy Path -- searchUserByEmail() With invalid email")
    public void testSearchUserByEmailUnhappyPath() {
        // Given
        userRepositoryMock.saveAll(constructUsersSet());

        // When
        Optional<Page<Users>> usersSetOptional = userRepositoryMock.searchUserByEmail("INVALID_EMAIL", TEST_PAGEABLE);

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
        final String TEST_PASSWORD = "TEST_PASSWORD";
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
                .primaryEmail(TEST_PRIMARY_EMAIL)
                .secondaryEmail(TEST_SECONDARY_EMAIL)
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