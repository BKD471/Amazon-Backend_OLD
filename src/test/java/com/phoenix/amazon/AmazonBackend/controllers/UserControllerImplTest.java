package com.phoenix.amazon.AmazonBackend.controllers;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.phoenix.amazon.AmazonBackend.dto.ApiResponse;
import com.phoenix.amazon.AmazonBackend.dto.ImageResponseMessages;
import com.phoenix.amazon.AmazonBackend.dto.PageableResponse;
import com.phoenix.amazon.AmazonBackend.dto.PasswordResponseMessages;
import com.phoenix.amazon.AmazonBackend.dto.PasswordUpdateDto;
import com.phoenix.amazon.AmazonBackend.dto.UserDto;

import com.phoenix.amazon.AmazonBackend.services.IImageService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.ClassOrderer;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;


import java.io.File;
import java.io.FileInputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static com.phoenix.amazon.AmazonBackend.helpers.AllConstantHelpers.GENDER;
import static com.phoenix.amazon.AmazonBackend.helpers.AllConstantHelpers.GENDER.MALE;
import static com.phoenix.amazon.AmazonBackend.helpers.AllConstantHelpers.USER_FIELDS.FIRST_NAME;
import static com.phoenix.amazon.AmazonBackend.helpers.AllConstantHelpers.USER_FIELDS.LAST_NAME;
import static com.phoenix.amazon.AmazonBackend.helpers.AllConstantHelpers.USER_FIELDS.USER_NAME;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import static org.mockito.Mockito.mockStatic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static com.phoenix.amazon.AmazonBackend.helpers.AllConstantHelpers.USER_FIELDS;
import static com.phoenix.amazon.AmazonBackend.helpers.AllConstantHelpers.USER_FIELDS.PRIMARY_EMAIL;

/***
 *  Having trouble with email verification api
 *  I am using a third party api to validate gmail
 *  That api has a finite limit within free version i have exhausted for this month
 *  so cant do any more test (:
 * ****/
@ExtendWith(MockitoExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserControllerImplTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    private final String BASE_URL_ACCOUNTS = "/api/users";
    final String TEST_USER_ID = "a57d1963-2cd8-47eb-89f8-51bf3ef69db7";
    final UUID TEST_UID= UUID.randomUUID();
    final String TEST_USER_NAME = "testUserName2";
    final String TEST_PRIMARY_EMAIL = "testUser03@gmail.com";
    final String TEST_SECONDARY_EMAIL = "testUser04@gmail.com";
    final String TEST_FIRST_NAME = "Test";
    final String TEST_LAST_NAME = "User";
    final GENDER TEST_GENDER = MALE;
    final String TEST_PROFILE_IMAGE = "086ca9e6-d846-4b5c-8f5e-2593d1862143.jpg";
    final String TEST_PASSWORD = "!@TestPassword@2020";
    final String TEST_ABOUT = "TEST TEST TEST TEST TEST TEST TEST";
    private final int TOTAL_ELEMENTS = 15;
    private final int PAGE_NUMBER = 1;
    private final int PAGE_SIZE = 5;
    private final String SORT_BY = "firstName";
    private final String SORT_DIR = "asc";
    private final int TOTAL_PAGES = 3;
    private final boolean IS_LAST_PAGE = false;
    private MockedStatic<UUID> mockedSettings;

    @BeforeEach
    public void init() {
        mockedSettings = mockStatic(UUID.class);
    }

    @AfterEach
    public void close() {
        mockedSettings.close();
    }
    @Test
    @Transactional
    @Order(1)
    @DisplayName("Test Happy Path -- createUser()")
    public void testCreateUser() throws Exception {
        // Given
        final String TEST_CREATE_USER_NAME = "testUserName1";
        final String TEST_CREATE_PRIMARY_EMAIL = "testUser01@gmail.com";
        final String TEST_CREATE_SECONDARY_EMAIL = "testUser02@gmail.com";

        mockedSettings.when(UUID::randomUUID).thenReturn(TEST_UID);

        UserDto userDtoRequest = new UserDto.builder()
                .userId(TEST_UID.toString())
                .userName(TEST_CREATE_USER_NAME)
                .firstName(TEST_FIRST_NAME).lastName(TEST_LAST_NAME).primaryEmail(TEST_CREATE_PRIMARY_EMAIL)
                .secondaryEmail(TEST_CREATE_SECONDARY_EMAIL).gender(String.valueOf(TEST_GENDER))
                .about(TEST_ABOUT).lastSeen(LocalDateTime.now())
                .profileImage(null).password(TEST_PASSWORD)
                .build();

        UserDto userDtoResponse = new UserDto.builder()
                .userId(TEST_UID.toString())
                .userName(TEST_CREATE_USER_NAME)
                .firstName(TEST_FIRST_NAME).lastName(TEST_LAST_NAME).primaryEmail(TEST_CREATE_PRIMARY_EMAIL)
                .secondaryEmail(TEST_CREATE_SECONDARY_EMAIL).gender(String.valueOf(TEST_GENDER))
                .about(TEST_ABOUT).lastSeen(null)
                .profileImage(null).password(null)
                .build();


        // Then
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post(BASE_URL_ACCOUNTS + "/v1/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDtoRequest))
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print()).andExpect(status().isCreated()).andReturn();


        String actualResponseBody = mvcResult.getResponse().getContentAsString();
        String expectedResponseBody = objectMapper.writeValueAsString(userDtoResponse);
        assertThat(actualResponseBody).isEqualToIgnoringWhitespace(expectedResponseBody);
    }

    @Test
    @DisplayName("Test Happy Path -- updateUserByUserIdOrUserNameOrPrimaryEmail() Update UserName")
    @Order(2)
    @Transactional
    public void testUpdateUserByUserIdOrUserNameOrPrimaryEmailUpdateUserName() throws Exception {
        final String NEW_USER_NAME = "NEW_USER_NAME_1";
        UserDto userDtoRequest = new UserDto.builder()
                .userName(NEW_USER_NAME)
                .firstName(TEST_FIRST_NAME).lastName(TEST_LAST_NAME).primaryEmail(TEST_PRIMARY_EMAIL)
                .secondaryEmail(TEST_SECONDARY_EMAIL).gender(String.valueOf(TEST_GENDER))
                .about(TEST_ABOUT)
                .build();

        UserDto userDtoResponse = new UserDto.builder()
                .userId(TEST_USER_ID).userName(NEW_USER_NAME)
                .firstName(TEST_FIRST_NAME).lastName(TEST_LAST_NAME).primaryEmail(TEST_PRIMARY_EMAIL)
                .secondaryEmail(TEST_SECONDARY_EMAIL).gender(String.valueOf(TEST_GENDER))
                .about(TEST_ABOUT).lastSeen(null)
                .profileImage(TEST_PROFILE_IMAGE).password(null)
                .build();

        // Then
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.put(BASE_URL_ACCOUNTS + "/v1/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDtoRequest))
                        .param("userId", TEST_USER_ID)
                        .param("userName", TEST_USER_ID)
                        .param("primaryEmail", TEST_PRIMARY_EMAIL)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print()).andExpect(status().isAccepted()).andReturn();


        String actualResponseBody = mvcResult.getResponse().getContentAsString();
        String expectedResponseBody = objectMapper.writeValueAsString(userDtoResponse);
        assertThat(actualResponseBody).isEqualToIgnoringWhitespace(expectedResponseBody);
    }

    @Test
    @Transactional
    @Order(15)
    @DisplayName("Test Happy Path -- deleteUser()")
    public void testDeleteUser() throws Exception {
        // Given
        final String userId = TEST_USER_ID;
        final String userName = TEST_USER_NAME;
        final String primaryEmail = TEST_PRIMARY_EMAIL;

        ApiResponse deleteResponse = new ApiResponse.builder()
                .success(true)
                .status(HttpStatus.OK)
                .message("User with userId : a57d1963-2cd8-47eb-89f8-51bf3ef69db7 is deleted successfully")
                .build();

        // Then
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.delete(BASE_URL_ACCOUNTS + "/v1/delete")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .param("userId", userId)
                        .param("userName", userName)
                        .param("primaryEmail", primaryEmail))
                .andDo(print()).andExpect(status().isAccepted()).andReturn();


        String actualResponseBody = mvcResult.getResponse().getContentAsString();
        String expectedResponseBody = objectMapper.writeValueAsString(deleteResponse);
        assertThat(actualResponseBody).isEqualToIgnoringWhitespace(expectedResponseBody);
    }

    @Test
    @Transactional
    @Order(3)
    @DisplayName("Test Happy Path -- getAllUsers()")
    public void testGetAllUsers() throws Exception {
        // Given
        final int pageNumber = PAGE_NUMBER;
        final int pageSize = PAGE_SIZE;
        final String sortBY = SORT_BY;
        final String sortDir = SORT_DIR;

        PageableResponse<UserDto> pageableResponse = new PageableResponse.Builder<UserDto>()
                .content(List.of(constructUserDto()))
                .pageNumber(1)
                .pageSize(5)
                .totalElements(1)
                .totalPages(1)
                .isLastPage(true)
                .build();

        // Then
        MvcResult mvcResult = mockMvc.perform(get(BASE_URL_ACCOUNTS + "/v1/getAll")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .param("pageNumber", String.valueOf(pageNumber))
                        .param("pageSize", String.valueOf(pageSize))
                        .param("sortBy", sortBY)
                        .param("sortDir", sortDir))
                .andDo(print()).andExpect(status().isOk()).andReturn();


        String actualResponseBody = mvcResult.getResponse().getContentAsString();
        String expectedResponseBody = objectMapper.writeValueAsString(pageableResponse);
        assertThat(actualResponseBody).isEqualToIgnoringWhitespace(expectedResponseBody);
    }

    @Test
    @Transactional
    @Order(4)
    @DisplayName("Test Happy Path -- getUserInformationByUserIdOrUserNameOrPrimaryEmail()")
    public void testGetUserInformationByUserIdOrUserNameOrPrimaryEmail() throws Exception {
        // Given
        final String userId = TEST_USER_ID;
        final String userName = TEST_USER_NAME;
        final String primaryEmail = TEST_PRIMARY_EMAIL;

        UserDto userResponseDTo = constructUserDto();

        // Then
        MvcResult mvcResult = mockMvc.perform(get(BASE_URL_ACCOUNTS + "/v1/info")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .param("userId", userId)
                        .param("userName", userName)
                        .param("primaryEmail", primaryEmail))
                .andDo(print()).andExpect(status().isOk()).andReturn();


        String actualResponseBody = mvcResult.getResponse().getContentAsString();
        String expectedResponseBody = objectMapper.writeValueAsString(userResponseDTo);
        assertThat(actualResponseBody).isEqualToIgnoringWhitespace(expectedResponseBody);
    }

    @Test
    @Transactional
    @Order(5)
    @DisplayName("Test Happy Path -- searchUserByFieldAndValue() with Primary Email")
    public void testSearchUserByFieldAndValueWithPrimaryEmail() throws Exception {
        // Given
        final USER_FIELDS fields = PRIMARY_EMAIL;
        final String value = TEST_PRIMARY_EMAIL;

        PageableResponse<UserDto> pageableResponse = new PageableResponse.Builder<UserDto>()
                .content(List.of(constructUserDto()))
                .pageNumber(1)
                .pageSize(5)
                .totalElements(1)
                .totalPages(1)
                .isLastPage(true)
                .build();

        // Then
        MvcResult mvcResult = mockMvc.perform(get(BASE_URL_ACCOUNTS + "/v1/search_by_field")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .param("field", String.valueOf(fields))
                        .param("value", value))
                .andDo(print()).andExpect(status().isOk()).andReturn();


        String actualResponseBody = mvcResult.getResponse().getContentAsString();
        String expectedResponseBody = objectMapper.writeValueAsString(pageableResponse);
        assertThat(actualResponseBody).isEqualToIgnoringWhitespace(expectedResponseBody);
    }

    @Test
    @Transactional
    @Order(6)
    @DisplayName("Test Happy Path -- searchUserByFieldAndValue() with UserName")
    public void testSearchUserByFieldAndValueWithUserName() throws Exception {
        // Given
        final USER_FIELDS fields = USER_NAME;
        final String value = TEST_USER_NAME;

        PageableResponse<UserDto> pageableResponse = new PageableResponse.Builder<UserDto>()
                .content(List.of(constructUserDto()))
                .pageNumber(1)
                .pageSize(5)
                .totalElements(1)
                .totalPages(1)
                .isLastPage(true)
                .build();

        // Then
        MvcResult mvcResult = mockMvc.perform(get(BASE_URL_ACCOUNTS + "/v1/search_by_field")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .param("field", String.valueOf(fields))
                        .param("value", value))
                .andDo(print()).andExpect(status().isOk()).andReturn();


        String actualResponseBody = mvcResult.getResponse().getContentAsString();
        String expectedResponseBody = objectMapper.writeValueAsString(pageableResponse);
        assertThat(actualResponseBody).isEqualToIgnoringWhitespace(expectedResponseBody);
    }

    @Test
    @Transactional
    @Order(7)
    @DisplayName("Test Happy Path -- searchUserByFieldAndValue() with Gender")
    public void testSearchUserByFieldAndValueWithGender() throws Exception {
        // Given
        final USER_FIELDS fields = USER_FIELDS.GENDER;
        final String value = String.valueOf(TEST_GENDER);

        PageableResponse<UserDto> pageableResponse = new PageableResponse.Builder<UserDto>()
                .content(List.of(constructUserDto()))
                .pageNumber(1)
                .pageSize(5)
                .totalElements(1)
                .totalPages(1)
                .isLastPage(true)
                .build();

        // Then
        MvcResult mvcResult = mockMvc.perform(get(BASE_URL_ACCOUNTS + "/v1/search_by_field")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .param("field", String.valueOf(fields))
                        .param("value", value))
                .andDo(print()).andExpect(status().isOk()).andReturn();


        String actualResponseBody = mvcResult.getResponse().getContentAsString();
        String expectedResponseBody = objectMapper.writeValueAsString(pageableResponse);
        assertThat(actualResponseBody).isEqualToIgnoringWhitespace(expectedResponseBody);
    }

    @Test
    @Transactional
    @Order(8)
    @DisplayName("Test Happy Path -- searchUserByFieldAndValue() with FirstName")
    public void testSearchUserByFieldAndValueWithFirstName() throws Exception {
        // Given
        final USER_FIELDS fields = FIRST_NAME;
        final String value = TEST_FIRST_NAME;

        PageableResponse<UserDto> pageableResponse = new PageableResponse.Builder<UserDto>()
                .content(List.of(constructUserDto()))
                .pageNumber(1)
                .pageSize(5)
                .totalElements(1)
                .totalPages(1)
                .isLastPage(true)
                .build();

        // Then
        MvcResult mvcResult = mockMvc.perform(get(BASE_URL_ACCOUNTS + "/v1/search_by_field")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .param("field", String.valueOf(fields))
                        .param("value", value))
                .andDo(print()).andExpect(status().isOk()).andReturn();


        String actualResponseBody = mvcResult.getResponse().getContentAsString();
        String expectedResponseBody = objectMapper.writeValueAsString(pageableResponse);
        assertThat(actualResponseBody).isEqualToIgnoringWhitespace(expectedResponseBody);
    }

    @Test
    @Transactional
    @Order(9)
    @DisplayName("Test Happy Path -- searchUserByFieldAndValue() with LastName")
    public void testSearchUserByFieldAndValueWithLastName() throws Exception {
        // Given
        final USER_FIELDS fields = LAST_NAME;
        final String value = TEST_LAST_NAME;

        PageableResponse<UserDto> pageableResponse = new PageableResponse.Builder<UserDto>()
                .content(List.of(constructUserDto()))
                .pageNumber(1)
                .pageSize(5)
                .totalElements(1)
                .totalPages(1)
                .isLastPage(true)
                .build();

        // Then
        MvcResult mvcResult = mockMvc.perform(get(BASE_URL_ACCOUNTS + "/v1/search_by_field")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .param("field", String.valueOf(fields))
                        .param("value", value))
                .andDo(print()).andExpect(status().isOk()).andReturn();


        String actualResponseBody = mvcResult.getResponse().getContentAsString();
        String expectedResponseBody = objectMapper.writeValueAsString(pageableResponse);
        assertThat(actualResponseBody).isEqualToIgnoringWhitespace(expectedResponseBody);
    }

    @Test
    @Transactional
    @Order(10)
    @DisplayName("Test Happy Path -- searchAllUsersByUserName()")
    public void testSearchAllUsersByUserName() throws Exception {
        // Given
        final String userName = TEST_USER_NAME;

        PageableResponse<UserDto> pageableResponse = new PageableResponse.Builder<UserDto>()
                .content(List.of(constructUserDto()))
                .pageNumber(1)
                .pageSize(5)
                .totalElements(1)
                .totalPages(1)
                .isLastPage(true)
                .build();

        // Then
        MvcResult mvcResult = mockMvc.perform(get(BASE_URL_ACCOUNTS
                                + "/v1/search_by_username/{userNameWord}", userName)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print()).andExpect(status().isOk()).andReturn();


        String actualResponseBody = mvcResult.getResponse().getContentAsString();
        String expectedResponseBody = objectMapper.writeValueAsString(pageableResponse);
        assertThat(actualResponseBody).isEqualToIgnoringWhitespace(expectedResponseBody);
    }

    @Test
    @Transactional
    @Order(11)
    @DisplayName("Test Happy Path -- uploadUserImageByUserIdOrUserNameOrPrimaryEmail() ")
    public void testUploadUserImageByUserIdOrUserNameOrPrimaryEmail() throws Exception {
        // Given
        final String userId = TEST_USER_NAME;
        final String userName = TEST_USER_NAME;
        final String primaryEmail = TEST_PRIMARY_EMAIL;

        mockedSettings.when(UUID::randomUUID).thenReturn(TEST_UID);

        FileInputStream fileInputStream = new FileInputStream("/home/phoenix/Desktop/backend/Amazon-Backend/src/test/java/com/phoenix/amazon/AmazonBackend/testimages/users/TestImage.png");
        final MockMultipartFile IMAGE_FILE =
                new MockMultipartFile("userImage", "TestImage.png", MediaType.APPLICATION_JSON_VALUE, fileInputStream);

        ImageResponseMessages imageUploadedResponse=new ImageResponseMessages.Builder()
                .imageName(TEST_UID+".png")
                .message("Profile image has been uploaded successfully")
                .success(true)
                .status(HttpStatus.ACCEPTED)
                .build();


        // Then
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
                        .multipart(BASE_URL_ACCOUNTS + "/v1/upload/image")
                        .file(IMAGE_FILE)
                        .param("userId",userId)
                        .param("userName",userName)
                        .param("primaryEmail",primaryEmail))
                .andDo(print()).andExpect(status().isAccepted()).andReturn();


        String actualResponseBody = mvcResult.getResponse().getContentAsString();
        String expectedResponseBody = objectMapper.writeValueAsString(imageUploadedResponse);
        assertThat(actualResponseBody).isEqualToIgnoringWhitespace(expectedResponseBody);
    }

    @Test
    @Transactional
    @Order(16)
    @DisplayName("Test Happy Path -- serveUserImageByUserIdOrUserNameOrPrimaryEmail()")
    public void testServeUserImageByUserIdOrUserNameOrPrimaryEmail() throws Exception {
        // Given
        final String userId = TEST_USER_NAME;
        final String userName = TEST_USER_NAME;
        final String primaryEmail = TEST_PRIMARY_EMAIL;

        //File file=new File("/home/phoenix/Desktop/backend/Amazon-Backend/src/test/java/com/phoenix/amazon/AmazonBackend/testimages/users/086ca9e6-d846-4b5c-8f5e-2593d1862143.jpg");
        Files.copy(Path.of("/home/phoenix/Desktop/backend/Amazon-Backend/src/test/java/com/phoenix/amazon/AmazonBackend/testimages/users/086ca9e6-d846-4b5c-8f5e-2593d1862143.jpg"),
                Path.of("/home/phoenix/Desktop/backend/Amazon-Backend/downloadable/images/users/086ca9e6-d846-4b5c-8f5e-2593d1862143.jpg"));

        File imageFile = new File("/home/phoenix/Desktop/backend/Amazon-Backend/downloadable/images/users/"
                , TEST_PROFILE_IMAGE.toString());

        byte[] expectedContent = Files.readAllBytes(imageFile.toPath());
        mockMvc.perform(get(BASE_URL_ACCOUNTS + "/v1/serve/image").contentType(MediaType.APPLICATION_JSON)
                        .param("userId",userId)
                        .param("userName",userName)
                        .param("primaryEmail",primaryEmail))
                .andExpect(content().bytes(expectedContent))
                .andExpect(status().isOk());
    }


    @Test
    @Transactional
    @DisplayName("Test Happy Path -- generatePassword()")
    @Order(13)
    public void testGeneratePassword() throws Exception {
        // Given
        MvcResult mvcResult=mockMvc.perform(get(BASE_URL_ACCOUNTS + "/v1/genPassword")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print()).andExpect(status().isOk()).andReturn();
        String actualResponseBody = mvcResult.getResponse().getContentAsString();

        boolean hasLowerCase = false, hasUpperCase = false, hasNumbers = false, hasSpecialCharacters = false;
        for (int i = 0; i < actualResponseBody.length(); i++) {
            int ascii = actualResponseBody.charAt(i);
            if (ascii >= 65 && ascii <= 90) hasUpperCase = true;
            if (ascii >= 97 && ascii <= 122) hasLowerCase = true;
            if (ascii >= 48 && ascii <= 57) hasNumbers = true;
            if ((ascii >= 33 && ascii <= 47) || (ascii>=60 && ascii<=64) || ascii==95 || ascii==124) hasSpecialCharacters = true;
            if (hasLowerCase && hasUpperCase && hasNumbers && hasSpecialCharacters) break;
        }

        boolean result = actualResponseBody.length() >= 16 && hasLowerCase && hasUpperCase && hasNumbers && hasSpecialCharacters;
        assertThat(result).isTrue();
    }

    @Test
    @Transactional
    @DisplayName("Test Happy Path -- resetMyPassword()")
    @Order(14)
    public void testResetMyPassword() throws Exception {
        // Given
        final String NEW_PASSWORD="!@NewPassword@2020";
        PasswordUpdateDto passwordUpdateRequest=new PasswordUpdateDto.Builder()
                .primaryEmail(TEST_PRIMARY_EMAIL)
                .oldPassword(TEST_PASSWORD)
                .newPassword(NEW_PASSWORD)
                .confirmPassword(NEW_PASSWORD)
                .build();

        PasswordResponseMessages passwordResponseMessages=new PasswordResponseMessages.Builder()
                .password(null)
                .message("Your password has been updated successfully")
                .success(true)
                .status(HttpStatus.ACCEPTED)
                .build();
        mockedSettings.when(UUID::randomUUID).thenReturn(TEST_UID);

        // Then
        MvcResult mvcResult = mockMvc.perform(patch(BASE_URL_ACCOUNTS+"/v1/reset/password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(passwordUpdateRequest))
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print()).andExpect(status().isAccepted()).andReturn();


        String actualResponseBody = mvcResult.getResponse().getContentAsString();
        String expectedResponseBody = objectMapper.writeValueAsString(passwordResponseMessages);
        assertThat(actualResponseBody).isEqualToIgnoringWhitespace(expectedResponseBody);
    }


    private UserDto constructUserDto() {
        return new UserDto.builder()
                .userId(TEST_USER_ID)
                .userName(TEST_USER_NAME)
                .primaryEmail(TEST_PRIMARY_EMAIL)
                .secondaryEmail(TEST_SECONDARY_EMAIL)
                .firstName(TEST_FIRST_NAME)
                .lastName(TEST_LAST_NAME)
                .password(null)
                .gender(String.valueOf(TEST_GENDER))
                .about(TEST_ABOUT)
                .profileImage(TEST_PROFILE_IMAGE)
                .lastSeen(null)
                .build();
    }

}
