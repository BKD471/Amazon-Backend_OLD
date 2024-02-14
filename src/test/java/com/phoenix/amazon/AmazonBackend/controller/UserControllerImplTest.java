package com.phoenix.amazon.AmazonBackend.controller;

import com.phoenix.amazon.AmazonBackend.controllers.impl.UserControllerImpl;
import com.phoenix.amazon.AmazonBackend.dto.ApiResponse;
import com.phoenix.amazon.AmazonBackend.dto.ImageResponseMessages;
import com.phoenix.amazon.AmazonBackend.dto.PageableResponse;
import com.phoenix.amazon.AmazonBackend.dto.UserDto;
import com.phoenix.amazon.AmazonBackend.helpers.AllConstantHelpers;
import com.phoenix.amazon.AmazonBackend.services.IImageService;
import com.phoenix.amazon.AmazonBackend.services.IUserService;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.phoenix.amazon.AmazonBackend.helpers.AllConstantHelpers.GENDER;
import static com.phoenix.amazon.AmazonBackend.helpers.AllConstantHelpers.GENDER.MALE;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerImplTest {
    @Mock
    private IImageService imageService;

    @Mock
    private IUserService userService;

    @InjectMocks
    private UserControllerImpl userController;

    @Autowired
    private MockMvc mockMvc;

    private final String BASE_URL_ACCOUNTS = "/api/users";

    private final String TEST_USER_ID = "b8defa54-2ae5-4bab-910b-be9ebc670fbd";
    private final String TEST_USER_NAME = "TEST_USER_NAME";
    private final String TEST_PRIMARY_EMAIL = "TEST_PRIMARY_EMAIL";
    private final String TEST_SECONDARY_EMAIL = "TEST_SECONDARY_EMAIL";
    private final String TEST_FIRST_NAME = "TEST_FIRST_NAME";
    private final String TEST_LAST_NAME = "TEST_LAST_NAME";
    private final GENDER TEST_GENDER = MALE;
    private final String TEST_PROFILE_IMAGE = "b8defa54-2ae5-4bab-910b-be9ebc670fbd.jpg";
    private final String TEST_ABOUT = "TEST TEST TEST TEST TEST TEST TEST";
    private final int TOTAL_ELEMENTS = 15;
    private final int PAGE_NUMBER = 1;
    private final int PAGE_SIZE = 5;
    private final int TOTAL_PAGES = 3;
    private final boolean IS_LAST_PAGE = false;

    private UserDto constructUserDto() {
        return new UserDto.builder()
                .userId(TEST_USER_ID)
                .userName(TEST_USER_NAME)
                .primaryEmail(TEST_PRIMARY_EMAIL)
                .secondaryEmail(TEST_SECONDARY_EMAIL)
                .firstName(TEST_FIRST_NAME)
                .lastName(TEST_LAST_NAME)
                .gender(String.valueOf(TEST_GENDER))
                .about(TEST_ABOUT)
                .profileImage(TEST_PROFILE_IMAGE)
                .lastSeen(LocalDateTime.now())
                .build();
    }

    private ApiResponse constructApiResponse(final String message, final HttpStatus status) {
        return new ApiResponse.builder()
                .message(message)
                .status(status)
                .success(true)
                .build();
    }

    private ImageResponseMessages constructImageResponse(final String imageName, final String message,
                                                         final HttpStatus status) {
        return new ImageResponseMessages.Builder()
                .imageName(imageName)
                .message(message)
                .status(status)
                .success(true)
                .build();
    }

    private PageableResponse<UserDto> constructPageableResponse() {
        final List<UserDto> listOfUsers = new ArrayList<>();
        for (int i = 1; i <= TOTAL_ELEMENTS; i++) listOfUsers.add(constructUserDto());

        return new PageableResponse.Builder<UserDto>()
                .content(listOfUsers)
                .pageNumber(PAGE_NUMBER)
                .pageSize(PAGE_SIZE)
                .totalPages(TOTAL_PAGES)
                .totalElements(TOTAL_ELEMENTS)
                .isLastPage(IS_LAST_PAGE)
                .build();
    }
}
