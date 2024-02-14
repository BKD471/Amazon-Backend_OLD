//package com.phoenix.amazon.AmazonBackend.controllers;
//
//
//import com.fasterxml.jackson.core.JsonProcessingException;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.phoenix.amazon.AmazonBackend.controllers.impl.UserControllerImpl;
//import com.phoenix.amazon.AmazonBackend.dto.ApiResponse;
//import com.phoenix.amazon.AmazonBackend.dto.ImageResponseMessages;
//import com.phoenix.amazon.AmazonBackend.dto.PageableResponse;
//import com.phoenix.amazon.AmazonBackend.dto.UserDto;
//import com.phoenix.amazon.AmazonBackend.repository.IUserRepository;
//import com.phoenix.amazon.AmazonBackend.services.IImageService;
//import com.phoenix.amazon.AmazonBackend.services.IUserService;
//import jakarta.transaction.Transactional;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.MediaType;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.MvcResult;
//import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
//
//import java.time.LocalDateTime;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.UUID;
//
//import static com.phoenix.amazon.AmazonBackend.helpers.AllConstantHelpers.GENDER;
//import static com.phoenix.amazon.AmazonBackend.helpers.AllConstantHelpers.GENDER.MALE;
//import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
//import static org.mockito.Mockito.mock;
//import static org.mockito.Mockito.mockStatic;
//import static org.mockito.Mockito.when;
//import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
///***
// *  Having trouble with email verification api
// *  I am using a third party api to validate gmail
// *  That api has a finite limit within free version i have exhausted for this month
// *  so cant do any more test (:
// * ****/
//@ExtendWith(MockitoExtension.class)
//@SpringBootTest
//@AutoConfigureMockMvc
//public class UserControllerImplTest {
////    @Mock
////    private IImageService imageServiceMock;
////    @Mock
////    private IUserService userServiceMock;
////    @Mock
////    private IUserRepository userRepositoryMock;
////
////    @InjectMocks
////    private UserControllerImpl userControllerMock;
////
////    @Autowired
////    private MockMvc mockMvc;
////
////    @Autowired
////    private ObjectMapper objectMapper;
////
////
////    private final String BASE_URL_ACCOUNTS = "/api/users";
////
////    final String TEST_USER_ID = "91f8f8a5-1df7-4870-914b-f7f7a0d8f4eb";
////    final String TEST_USER_NAME = "testUser";
////    final String TEST_PRIMARY_EMAIL = "phoenix4781@gmail.com";
////    final String TEST_SECONDARY_EMAIL = "bhaskarkumardas@gmail.com";
////    final String TEST_FIRST_NAME = "Test";
////    final String TEST_LAST_NAME = "User";
////    final GENDER TEST_GENDER = MALE;
////    final String TEST_PROFILE_IMAGE = "62b5c43c-20b4-4361-92b6-e6d721c33b3a.jpg";
////    final String TEST_PASSWORD = "!@TestPassword@2020";
////    final String TEST_ABOUT = "TEST TEST TEST TEST TEST TEST TEST";
////    private final int TOTAL_ELEMENTS = 15;
////    private final int PAGE_NUMBER = 1;
////    private final int PAGE_SIZE = 5;
////    private final int TOTAL_PAGES = 3;
////    private final boolean IS_LAST_PAGE = false;
////    private UUID uuid;
////
////    @BeforeEach
////    public void setUp() {
////        uuid = mock(UUID.class);
////    }
////
////    @Test
////    @Transactional
////    @DisplayName("Test Happy Path -- createUser()")
////    public void testCreateUser() throws Exception {
////        // Given
////        final String TEST_CREATE_USER_NAME = "TestUserName";
////        final String TEST_CREATE_PRIMARY_EMAIL = "bhaskarkumardas9@gmail.com";
////        final String TEST_CREATE_SECONDARY_EMAIL = "bhaskarkumardas77@gmail.com";
////
////
////        final String userUId = UUID.randomUUID().toString();
////        mockStatic(UUID.class);
////        when(UUID.randomUUID()).thenReturn(uuid);
////        when(uuid.toString()).thenReturn(userUId);
////
////
////        UserDto userDtoRequest = new UserDto.builder()
////                .userId(null).userName(TEST_CREATE_USER_NAME)
////                .firstName(TEST_FIRST_NAME).lastName(TEST_LAST_NAME).primaryEmail(TEST_CREATE_PRIMARY_EMAIL)
////                .secondaryEmail(TEST_CREATE_SECONDARY_EMAIL).gender(String.valueOf(TEST_GENDER))
////                .about(TEST_ABOUT).lastSeen(LocalDateTime.now())
////                .profileImage(null).password(TEST_PASSWORD)
////                .build();
////
////        UserDto userDtoResponse = new UserDto.builder()
////                .userId(userUId).userName(TEST_CREATE_USER_NAME)
////                .firstName(TEST_FIRST_NAME).lastName(TEST_LAST_NAME).primaryEmail(TEST_CREATE_PRIMARY_EMAIL)
////                .secondaryEmail(TEST_CREATE_SECONDARY_EMAIL).gender(String.valueOf(TEST_GENDER))
////                .about(TEST_ABOUT).lastSeen(null)
////                .profileImage(null).password(null)
////                .build();
////
////
////        // Then
////        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post(BASE_URL_ACCOUNTS + "/v1/create")
////                        .contentType(MediaType.APPLICATION_JSON)
////                        .content(objectMapper.writeValueAsString(userDtoRequest))
////                        .accept(MediaType.APPLICATION_JSON))
////                .andDo(print()).andExpect(status().isCreated()).andReturn();
////
////
////        String actualResponseBody = mvcResult.getResponse().getContentAsString();
////        String expectedResponseBody = objectMapper.writeValueAsString(userDtoResponse);
////        assertThat(actualResponseBody).isEqualToIgnoringWhitespace(expectedResponseBody);
// //   }
//
////    @Test
////    @DisplayName("Test Happy Path -- updateUserByUserIdOrUserNameOrPrimaryEmail() Update UserName")
////    @Transactional
////    public void testUpdateUserByUserIdOrUserNameOrPrimaryEmailUpdateUserName() throws Exception {
////        final String NEW_USER_NAME="NEW_USER_NAME_1";
////        UserDto userDtoRequest = new UserDto.builder()
////                .userName(NEW_USER_NAME)
////                .firstName(TEST_FIRST_NAME).lastName(TEST_LAST_NAME).primaryEmail(TEST_PRIMARY_EMAIL)
////                .secondaryEmail(TEST_SECONDARY_EMAIL).gender(String.valueOf(TEST_GENDER))
////                .about(TEST_ABOUT)
////                .build();
////
////        UserDto userDtoResponse = new UserDto.builder()
////                .userId(TEST_USER_ID).userName(NEW_USER_NAME)
////                .firstName(TEST_FIRST_NAME).lastName(TEST_LAST_NAME).primaryEmail(TEST_PRIMARY_EMAIL)
////                .secondaryEmail(TEST_SECONDARY_EMAIL).gender(String.valueOf(TEST_GENDER))
////                .about(TEST_ABOUT).lastSeen(null)
////                .profileImage(TEST_PROFILE_IMAGE).password(null)
////                .build();
////
////        // Then
////        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.put(BASE_URL_ACCOUNTS + "/v1/update")
////                        .contentType(MediaType.APPLICATION_JSON)
////                        .content(objectMapper.writeValueAsString(userDtoRequest))
////                        .param("userId",TEST_USER_ID)
////                        .param("userName",TEST_USER_ID)
////                        .param("primaryEmail",TEST_PRIMARY_EMAIL)
////                        .accept(MediaType.APPLICATION_JSON))
////                .andDo(print()).andExpect(status().isAccepted()).andReturn();
////
////
////        String actualResponseBody = mvcResult.getResponse().getContentAsString();
////        String expectedResponseBody = objectMapper.writeValueAsString(userDtoResponse);
////        assertThat(actualResponseBody).isEqualToIgnoringWhitespace(expectedResponseBody);
////    }
//
////    private UserDto constructUserDto() {
////        return new UserDto.builder()
////                .userId(TEST_USER_ID)
////                .userName(TEST_USER_NAME)
////                .primaryEmail(TEST_PRIMARY_EMAIL)
////                .secondaryEmail(TEST_SECONDARY_EMAIL)
////                .firstName(TEST_FIRST_NAME)
////                .lastName(TEST_LAST_NAME)
////                .password(TEST_PASSWORD)
////                .gender(String.valueOf(TEST_GENDER))
////                .about(TEST_ABOUT)
////                .profileImage(TEST_PROFILE_IMAGE)
////                .lastSeen(LocalDateTime.now())
////                .build();
////    }
////
////    private ApiResponse constructApiResponse(final String message, final HttpStatus status) {
////        return new ApiResponse.builder()
////                .message(message)
////                .status(status)
////                .success(true)
////                .build();
////    }
////
////    private ImageResponseMessages constructImageResponse(final String imageName, final String message,
////                                                         final HttpStatus status) {
////        return new ImageResponseMessages.Builder()
////                .imageName(imageName)
////                .message(message)
////                .status(status)
////                .success(true)
////                .build();
////    }
////
////    private PageableResponse<UserDto> constructPageableResponse() {
////        final List<UserDto> listOfUsers = new ArrayList<>();
////        for (int i = 1; i <= TOTAL_ELEMENTS; i++) listOfUsers.add(constructUserDto());
////
////        return new PageableResponse.Builder<UserDto>()
////                .content(listOfUsers)
////                .pageNumber(PAGE_NUMBER)
////                .pageSize(PAGE_SIZE)
////                .totalPages(TOTAL_PAGES)
////                .totalElements(TOTAL_ELEMENTS)
////                .isLastPage(IS_LAST_PAGE)
////                .build();
////    }
//}
