package com.phoenix.amazon.AmazonBackend.controllers;

import com.phoenix.amazon.AmazonBackend.dto.ApiResponse;
import com.phoenix.amazon.AmazonBackend.dto.PageableResponse;
import com.phoenix.amazon.AmazonBackend.dto.UserDto;
import com.phoenix.amazon.AmazonBackend.exceptions.BadApiRequestExceptions;
import com.phoenix.amazon.AmazonBackend.exceptions.UserExceptions;
import com.phoenix.amazon.AmazonBackend.exceptions.UserNotFoundExceptions;
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


@RequestMapping("/api/users")
public interface IUserController {
    /**
     * @param user - User Object
     * @return ResponseEntity<UserDto> - UserDto Object
     */
    @PostMapping("/v1/create")
    ResponseEntity<UserDto> createUser(@Valid @RequestBody final UserDto user) throws UserNotFoundExceptions, UserExceptions, BadApiRequestExceptions;

    /**
     * @param user     - User Object
     * @param userId   - User Id
     * @param userName - userName of user
     * @return ResponseEntity<UserDto> - UserDto Object
     */
    @PutMapping("/v1/update")
    ResponseEntity<UserDto> updateUserByUserIdOrUserName(@Valid @RequestBody final UserDto user, @RequestParam(required = false) final String userId, @RequestParam(required = false) final String userName) throws UserNotFoundExceptions, UserExceptions, BadApiRequestExceptions;

    /**
     * @param userId   - User Id
     * @param userName - userName of user
     * @return ResponseEntity<ApiResponse> - ApiResponse Object
     */
    @DeleteMapping("/v1/delete")
    ResponseEntity<ApiResponse> deleteUserByUserIdOrUserName(@RequestParam(required = false) final String userId, @RequestParam(required = false) final String userName) throws UserNotFoundExceptions, UserExceptions, BadApiRequestExceptions;

    /**
     * @param pageNumber - index value of page
     * @param pageSize   - size of page
     * @param sortBy     - sort column
     * @param sortDir    - direction of sorting
     * @return ResponseEntity<List < UserDTo>> - list of userDtp
     */
    @GetMapping("/v1/getAll")
    ResponseEntity<PageableResponse<UserDto>> getAllUsers(@RequestParam(value = "pageNumber", defaultValue = "1", required = false) final int pageNumber,
                                                          @RequestParam(value = "pageSize", defaultValue = "5", required = false) final int pageSize,
                                                          @RequestParam(value = "sortBy", defaultValue = "firstName", required = false) final String sortBy,
                                                          @RequestParam(value = "sortDir", defaultValue = "asc", required = false) final String sortDir) throws UserNotFoundExceptions;

    /**
     * @param primaryEmail -  primary email of user
     * @param userName     - username of user
     * @return ResponseEntity<UserDto> - userDto Object
     */
    @GetMapping("/v1/info")
    ResponseEntity<UserDto> getUserInformationByPrimaryEmailOrUserName(@RequestParam(value = "primaryEmail", required = false) final String primaryEmail,
                                                                       @RequestParam(value = "userName", required = false) final String userName) throws UserNotFoundExceptions, UserExceptions, BadApiRequestExceptions;

    /**
     * @param field      - field of User Entity
     * @param value      - value of field
     * @param pageNumber - index value of page
     * @param pageSize   - size of page
     * @param sortBy     - sort column
     * @param sortDir    - direction of sorting
     * @return ResponseEntity<PageableResponse < UserDto>> - page of UserDto
     */
    @GetMapping("/v1/search_by_field")
    ResponseEntity<PageableResponse<UserDto>> searchUserByFieldAndValue(@RequestParam final USER_FIELDS field, @RequestParam final String value,
                                                                        @RequestParam(value = "pageNumber", defaultValue = "1", required = false) final int pageNumber,
                                                                        @RequestParam(value = "pageSize", defaultValue = "5", required = false) final int pageSize,
                                                                        @RequestParam(value = "sortBy", defaultValue = "FIRST_NAME", required = false) final USER_FIELDS sortBy,
                                                                        @RequestParam(value = "sortDir", defaultValue = "asc", required = false) final String sortDir) throws UserNotFoundExceptions;

    /**
     * @param userNameWord - Keyword to get multiple users with almost same name initials
     * @param pageNumber   - index value of page
     * @param pageSize     - size of page
     * @param sortBy       - sort column
     * @param sortDir      - direction of sorting
     * @return ResponseEntity<PageableResponse < UserDto>> - page of userDto
     */
    @GetMapping("/v1/search_by_username/{userNameWord}")
    ResponseEntity<PageableResponse<UserDto>> searchAllUsersByUserName(@PathVariable("userNameWord") final String userNameWord,
                                                                       @RequestParam(value = "pageNumber", defaultValue = "1", required = false) final int pageNumber,
                                                                       @RequestParam(value = "pageSize", defaultValue = "5", required = false) final int pageSize,
                                                                       @RequestParam(value = "sortBy", defaultValue = "firstName", required = false) final String sortBy,
                                                                       @RequestParam(value = "sortDir", defaultValue = "asc", required = false) final String sortDir) throws UserNotFoundExceptions;
}
