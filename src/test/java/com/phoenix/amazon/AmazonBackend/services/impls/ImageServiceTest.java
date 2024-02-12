package com.phoenix.amazon.AmazonBackend.services.impls;

import com.phoenix.amazon.AmazonBackend.repository.IUserRepository;
import com.phoenix.amazon.AmazonBackend.services.IImageService;
import com.phoenix.amazon.AmazonBackend.services.validationservice.IUserValidationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

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

    @BeforeEach
    public void setUp() {
        imageServiceMock = new ImageServiceImpl(userRepositoryMock, userValidationServiceMock, PATH_TO_IMAGE_PROPS);
    }

    @Test
    @DisplayName("Test Happy Path -- uploadUserImageServiceByUserIdOrUserNameOrPrimaryEmail")
    public void testUploadUserImageServiceByUserIdOrUserNameOrPrimaryEmail(){

    }
}
