package com.phoenix.amazon.AmazonBackend.services.impls;

import com.phoenix.amazon.AmazonBackend.entity.Users;
import com.phoenix.amazon.AmazonBackend.exceptions.BadApiRequestExceptions;
import com.phoenix.amazon.AmazonBackend.exceptions.UserExceptions;
import com.phoenix.amazon.AmazonBackend.exceptions.UserNotFoundExceptions;
import com.phoenix.amazon.AmazonBackend.helpers.AllConstantHelpers;
import com.phoenix.amazon.AmazonBackend.repository.IUserRepository;
import com.phoenix.amazon.AmazonBackend.services.IImageService;
import com.phoenix.amazon.AmazonBackend.services.validationservice.IUserValidationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.commons.util.StringUtils;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static com.phoenix.amazon.AmazonBackend.helpers.AllConstantHelpers.GENDER.MALE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
public class ImageServiceTest {
    @MockBean
    private IImageService imageServiceMock;
    @Mock
    private IUserValidationService userValidationServiceMock;
    @Mock
    private IUserRepository userRepositoryMock;

    @Value("${path.services.user.image.properties}")
    private String PATH_TO_IMAGE_PROPS;

    private final String TEST_UUID = "58824409-dd6b-4934-9923-ec1daf9693da";
    private final String TEST_USER_NAME = "TEST_USER_NAME";
    private final String TEST_PRIMARY_EMAIL = "test@gmail.com";
    private  UUID uuid;
    private final MockMultipartFile TEST_IMAGE_FILE =
            new MockMultipartFile("data", "uploadedFile.png", "text/plain", "some kml".getBytes());


    @BeforeEach
    public void setUp() {
        imageServiceMock = new ImageServiceImpl(userRepositoryMock, userValidationServiceMock, PATH_TO_IMAGE_PROPS);
        uuid = mock(UUID.class);
    }


    @Test
    @DisplayName("Test Happy Path -- uploadUserImageServiceByUserIdOrUserNameOrPrimaryEmail")
    public void testUploadUserImageServiceByUserIdOrUserNameOrPrimaryEmail() throws BadApiRequestExceptions, UserNotFoundExceptions, UserExceptions, IOException, NoSuchFieldException, IllegalAccessException {
        // Given
        Users users = constructUser();
        final String uuidImage = UUID.randomUUID().toString();

        // When
        doNothing().when(userValidationServiceMock).validatePZeroUserFields(anyString(), anyString(),
                anyString(), anyString(), any());
        when(userRepositoryMock.findByUserIdOrUserNameOrPrimaryEmail(anyString(), anyString(), anyString()))
                .thenReturn(Optional.of(users));
        doNothing().doNothing().when(userValidationServiceMock).validateUser(any(), any(), anyString(), any());
        doNothing().when(userValidationServiceMock).validateNullField(anyString(), anyString(), anyString());

        mockStatic(UUID.class);
        when(UUID.randomUUID()).thenReturn(uuid);
        when(uuid.toString()).thenReturn(uuidImage);

        final String image = imageServiceMock.uploadUserImageServiceByUserIdOrUserNameOrPrimaryEmail(TEST_IMAGE_FILE, TEST_UUID,
                TEST_USER_NAME, TEST_PRIMARY_EMAIL);


        // Then
        verify(userRepositoryMock, times(1)).save(any());
        assertThat(StringUtils.isBlank(image)).isFalse();
        assertThat(image).isEqualTo(uuidImage + ".png");
    }

    @Test
    @DisplayName("Test Unhappy Path -- uploadUserImageServiceByUserIdOrUserNameOrPrimaryEmail image type not supported")
    public void testUploadUserImageServiceByUserIdOrUserNameOrPrimaryEmailUnhappyPathTypeNotSupported() throws BadApiRequestExceptions, UserNotFoundExceptions, UserExceptions, IOException, NoSuchFieldException, IllegalAccessException {
        // Given
        Users users = constructUser();
        final MockMultipartFile UNSUPPORTED_IMAGE_FILE =
                new MockMultipartFile("data", "uploadedFile.mp3", "text/plain", "some kml".getBytes());

        // When
        doNothing().when(userValidationServiceMock).validatePZeroUserFields(anyString(), anyString(),
                anyString(), anyString(), any());
        when(userRepositoryMock.findByUserIdOrUserNameOrPrimaryEmail(anyString(), anyString(), anyString()))
                .thenReturn(Optional.of(users));
        doNothing().when(userValidationServiceMock).validateUser(any(), any(), anyString(), any());
        doNothing().when(userValidationServiceMock).validateNullField(anyString(), anyString(), anyString());


        // Then
        assertThrows(BadApiRequestExceptions.class, () -> imageServiceMock.uploadUserImageServiceByUserIdOrUserNameOrPrimaryEmail(UNSUPPORTED_IMAGE_FILE, TEST_UUID,
                TEST_USER_NAME, TEST_PRIMARY_EMAIL), "BadApiRequestException should have been thrown");

    }

    @Test
    @DisplayName("Test Unhappy Path -- uploadUserImageServiceByUserIdOrUserNameOrPrimaryEmail file size greater than 100kb")
    public void testUploadUserImageServiceByUserIdOrUserNameOrPrimaryEmailUnhappyPathFileSizeGreaterThan100Kb() throws BadApiRequestExceptions, UserNotFoundExceptions, UserExceptions, IOException, NoSuchFieldException, IllegalAccessException {
        // Given
        Users users = constructUser();
        FileInputStream fileInputStream=new FileInputStream("/home/phoenix/Desktop/backend/Amazon-Backend/src/test/java/com/phoenix/amazon/AmazonBackend/testimages/users/test2mb.jpg");
        final MockMultipartFile BIG_IMAGE_FILE =
                new MockMultipartFile("data","big.mp3", "text/plain", fileInputStream);

        // When
        doNothing().when(userValidationServiceMock).validatePZeroUserFields(anyString(), anyString(),
                anyString(), anyString(), any());
        when(userRepositoryMock.findByUserIdOrUserNameOrPrimaryEmail(anyString(), anyString(), anyString()))
                .thenReturn(Optional.of(users));
        doNothing().doThrow(new BadApiRequestExceptions(BadApiRequestExceptions.class,
                        "File Size must not be greater than 100kb",
                        "testUploadUserImageServiceByUserIdOrUserNameOrPrimaryEmailUnhappyPathFileSizeGreaterThan100Kb"))
                .when(userValidationServiceMock).validateUser(any(), any(), anyString(), any());
        doNothing().when(userValidationServiceMock).validateNullField(anyString(), anyString(), anyString());


        // Then
        assertThrows(BadApiRequestExceptions.class, () -> imageServiceMock
                .uploadUserImageServiceByUserIdOrUserNameOrPrimaryEmail(BIG_IMAGE_FILE, TEST_UUID,
                TEST_USER_NAME, TEST_PRIMARY_EMAIL), "BadApiRequestException should have been thrown");
    }


    @Test
    @DisplayName("Test Happy Path -- testServeUserImageServiceByUserIdOrUserNameOrPrimaryEmailHappyPath")
    public void testServeUserImageServiceByUserIdOrUserNameOrPrimaryEmailHappyPath() throws BadApiRequestExceptions, UserNotFoundExceptions, UserExceptions, IOException {
        // Given
        Users users = constructUser();

        // When
        doNothing().when(userValidationServiceMock).validatePZeroUserFields(anyString(), anyString(),
                anyString(), anyString(), any());
        when(userRepositoryMock.findByUserIdOrUserNameOrPrimaryEmail(anyString(), anyString(), anyString()))
                .thenReturn(Optional.of(users));
        doNothing().when(userValidationServiceMock).validateUser(any(), any(), anyString(), any());

        // Then
        InputStream fs=imageServiceMock.serveUserImageServiceByUserIdOrUserNameOrPrimaryEmail(TEST_UUID,TEST_USER_NAME,TEST_PRIMARY_EMAIL);
        assertThat(fs.available()).isGreaterThan(0);
    }

    @Test
    @DisplayName("Test Unhappy Path -- testServeUserImageServiceByUserIdOrUserNameOrPrimaryEmailUnHappyPath")
    public void testServeUserImageServiceByUserIdOrUserNameOrPrimaryEmailUnHappyPath() throws BadApiRequestExceptions, UserNotFoundExceptions, UserExceptions, IOException {
        // Given
        Users users = constructUser();

        // When
        doNothing().when(userValidationServiceMock).validatePZeroUserFields(anyString(), anyString(),
                anyString(), anyString(), any());
        when(userRepositoryMock.findByUserIdOrUserNameOrPrimaryEmail(anyString(), anyString(), anyString()))
                .thenReturn(Optional.of(users));
        doNothing().doThrow(new UserExceptions(UserExceptions.class,"You dont have profile image",
                        "testServeUserImageServiceByUserIdOrUserNameOrPrimaryEmailUnHappyPath"))
                .when(userValidationServiceMock).validateUser(any(), any(), anyString(), any());

        // Then
        assertThrows(UserExceptions.class,()->imageServiceMock.serveUserImageServiceByUserIdOrUserNameOrPrimaryEmail(TEST_UUID,TEST_USER_NAME,TEST_PRIMARY_EMAIL),
                "User Exceptions should have been thrown");
    }

    private Users constructUser() {
        final String TEST_PRIMARY_EMAIL = "test@gmail.com";
        final String TEST_SECONDARY_EMAIL = "tests@gmail.com";
        final String TEST_USER_NAME = "TEST_USER_NAME";
        final String TEST_UUID = "58824409-dd6b-4934-9923-ec1daf9693da";
        final String TEST_FIRST_NAME = "TEST_FIRST_NAME";
        final String TEST_LAST_NAME = "TEST_LAST_NAME";
        final AllConstantHelpers.GENDER TEST_GENDER = MALE;
        final String TEST_PASSWORD = "TEST_PASSWORD";
        final String TEST_PROFILE_IMAGE = "TestImage.png";
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
                .gender(TEST_GENDER)
                .profileImage(TEST_PROFILE_IMAGE)
                .about(TEST_ABOUT)
                .lastSeen(TEST_LAST_SEEN)
                .createdDate(LocalDate.now().minusDays(10))
                .createdBy(ADMIN)
                .build();
    }
}
