package com.phoenix.amazon.AmazonBackend.controllers;

import com.phoenix.amazon.AmazonBackend.dto.ApiResponse;
import com.phoenix.amazon.AmazonBackend.dto.ImageResponseMessages;
import com.phoenix.amazon.AmazonBackend.dto.PageableResponse;
import com.phoenix.amazon.AmazonBackend.dto.UserDto;
import com.phoenix.amazon.AmazonBackend.exceptions.BadApiRequestExceptions;
import com.phoenix.amazon.AmazonBackend.exceptions.UserExceptions;
import com.phoenix.amazon.AmazonBackend.exceptions.UserNotFoundExceptions;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import com.phoenix.amazon.AmazonBackend.helpers.AllConstantHelpers.USER_FIELDS;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;


@RequestMapping("/api/users")
public interface IUserController {
    /**
     * @param user                     - User Object
     * @return ResponseEntity<UserDto> - UserDto Object
     * @throws UserNotFoundExceptions,UserExceptions,BadApiRequestExceptions,IOException -list of exceptions being thrown
     */
    @PostMapping("/v1/create")
    ResponseEntity<UserDto> createUser(@Valid @RequestBody final UserDto user) throws UserNotFoundExceptions, UserExceptions, BadApiRequestExceptions, IOException;

    /**
     * @param user                     - User Object
     * @param userId                   - User Id
     * @param userName                 - userName of user
     * @param primaryEmail             - primaryEmail of user
     * @return ResponseEntity<UserDto> - UserDto Object
     * @throws UserNotFoundExceptions,UserExceptions,BadApiRequestExceptions,IOException -list of exceptions being thrown
     */
    @PutMapping("/v1/update")
    ResponseEntity<UserDto> updateUserByUserIdOrUserNameOrPrimaryEmail(@Valid @RequestBody final UserDto user,
                                                                       @RequestParam(value = "userId", required = false) final String userId,
                                                                       @RequestParam(value = "userName", required = false) final String userName,
                                                                       @RequestParam(value = "primaryEmail", required = false) final String primaryEmail) throws UserNotFoundExceptions, UserExceptions, BadApiRequestExceptions, IOException;

    /**
     * @param userId                       - User Id
     * @param userName                     - userName of user
     * @param primaryEmail                 - primaryEmail of user
     * @return ResponseEntity<ApiResponse> - ApiResponse Object
     * @throws UserNotFoundExceptions,UserExceptions,BadApiRequestExceptions,IOException -list of exceptions being thrown
     */
    @DeleteMapping("/v1/delete")
    ResponseEntity<ApiResponse> deleteUserByUserIdOrUserNameOrPrimaryEmail(@RequestParam(value = "userId", required = false) final String userId,
                                                                           @RequestParam(value = "userName", required = false) final String userName,
                                                                           @RequestParam(value = "primaryEmail", required = false) final String primaryEmail) throws UserNotFoundExceptions, UserExceptions, BadApiRequestExceptions, IOException;

    /**
     * @param pageNumber                                   - index value of page
     * @param pageSize                                     - size of page
     * @param sortBy                                       - sort column
     * @param sortDir                                      - direction of sorting
     * @return ResponseEntity<PageableResponse < UserDTo>> - page of userDtp
     * @throws UserNotFoundExceptions                      - list of exceptions being thrown
     */
    @GetMapping("/v1/getAll")
    ResponseEntity<PageableResponse<UserDto>> getAllUsers(@RequestParam(value = "pageNumber", defaultValue = "1", required = false) final int pageNumber,
                                                          @RequestParam(value = "pageSize", defaultValue = "5", required = false) final int pageSize,
                                                          @RequestParam(value = "sortBy", defaultValue = "firstName", required = false) final String sortBy,
                                                          @RequestParam(value = "sortDir", defaultValue = "asc", required = false) final String sortDir) throws UserNotFoundExceptions;

    /**
     * @param userId                   - userId of user
     * @param userName                 - username of user
     * @param primaryEmail             - primary email of user
     * @return ResponseEntity<UserDto> - userDto Object
     * @throws UserNotFoundExceptions,UserExceptions,BadApiRequestExceptions,IOException -list of exceptions being thrown
     */
    @GetMapping("/v1/info")
    ResponseEntity<UserDto> getUserInformationByUserIdOrUserNameOrPrimaryEmail(@RequestParam(value = "userId", required = false) final String userId,
                                                                               @RequestParam(value = "userName", required = false) final String userName,
                                                                               @RequestParam(value = "primaryEmail", required = false) final String primaryEmail) throws UserNotFoundExceptions, UserExceptions, BadApiRequestExceptions, IOException;

    /**
     * @param field                                        - field of User Entity
     * @param value                                        - value of field
     * @param pageNumber                                   - index value of page
     * @param pageSize                                     - size of page
     * @param sortBy                                       - sort column
     * @param sortDir                                      - direction of sorting
     * @return ResponseEntity<PageableResponse < UserDto>> - list of UserDto
     * @throws UserNotFoundExceptions                      -list of exceptions being thrown
     */
    @GetMapping("/v1/search_by_field")
    ResponseEntity<PageableResponse<UserDto>> searchUserByFieldAndValue(@RequestParam final USER_FIELDS field, @RequestParam final String value,
                                                                        @RequestParam(value = "pageNumber", defaultValue = "1", required = false) final int pageNumber,
                                                                        @RequestParam(value = "pageSize", defaultValue = "5", required = false) final int pageSize,
                                                                        @RequestParam(value = "sortBy", defaultValue = "FIRST_NAME", required = false) final USER_FIELDS sortBy,
                                                                        @RequestParam(value = "sortDir", defaultValue = "asc", required = false) final String sortDir) throws UserNotFoundExceptions;

    /**
     * @param userNameWord                                 - Keyword to get multiple users with almost same name initials
     * @param pageNumber                                   - index value of page
     * @param pageSize                                     - size of page
     * @param sortBy                                       - sort column
     * @param sortDir                                      - direction of sorting
     * @return ResponseEntity<PageableResponse < UserDto>> - list of userDto
     * @throws UserNotFoundExceptions                      -list of exceptions being thrown
     */
    @GetMapping("/v1/search_by_username/{userNameWord}")
    ResponseEntity<PageableResponse<UserDto>> searchAllUsersByUserName(@PathVariable("userNameWord") final String userNameWord,
                                                                       @RequestParam(value = "pageNumber", defaultValue = "1", required = false) final int pageNumber,
                                                                       @RequestParam(value = "pageSize", defaultValue = "5", required = false) final int pageSize,
                                                                       @RequestParam(value = "sortBy", defaultValue = "firstName", required = false) final String sortBy,
                                                                       @RequestParam(value = "sortDir", defaultValue = "asc", required = false) final String sortDir) throws UserNotFoundExceptions;

    /**
     * @param image                                  - profile image of user
     * @param userId                                 - userId of user
     * @param userName                               - userName of user
     * @param primaryEmail                           - primary email of user
     * @return ResponseEntity<ImageResponseMessages> - image response
     * @throws IOException,BadApiRequestExceptions,UserNotFoundExceptions,UserExceptions - list of exceptions being thrown
     */
    @PutMapping("/v1/upload/image")
    ResponseEntity<ImageResponseMessages> uploadUserImageByUserIdOrUserNameOrPrimaryEmail(@RequestParam("userImage") final MultipartFile image,
                                                                                              @RequestParam(value = "userId", required = false) final String userId,
                                                                                              @RequestParam(value = "userName", required = false) final String userName,
                                                                                              @RequestParam(value = "primaryEmail", required = false) final String primaryEmail) throws IOException, BadApiRequestExceptions, UserNotFoundExceptions, UserExceptions;

    /**
     * @param userId       - userId of user
     * @param userName     - userName of user
     * @param primaryEmail - primary email of user
     * @param response     - http response
     * @throws IOException,UserNotFoundExceptions,UserExceptions,BadApiRequestExceptions - list of exceptions being thrown
     */
    @GetMapping("/v1/serve/image")
    void serveUserImageByUserIdOrUserNameOrPrimaryEmail(@RequestParam(value = "userId", required = false) final String userId,
                                                        @RequestParam(value = "userName", required = false) final String userName,
                                                        @RequestParam(value = "primaryEmail", required = false) final String primaryEmail,
                                                        final HttpServletResponse response) throws IOException, UserNotFoundExceptions, UserExceptions, BadApiRequestExceptions;

}
