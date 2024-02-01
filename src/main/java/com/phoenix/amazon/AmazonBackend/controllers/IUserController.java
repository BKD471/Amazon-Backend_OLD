package com.phoenix.amazon.AmazonBackend.controllers;

import com.phoenix.amazon.AmazonBackend.dto.ApiResponse;
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

import java.util.Set;

@RequestMapping("/api/users")
public interface IUserController {
    /**
     * @param user - User Object
     * @return ResponseEntity<UserDto>
     */
    @PostMapping("/v1/create")
    ResponseEntity<UserDto> createUser(@Valid @RequestBody final UserDto user) throws UserNotFoundExceptions, UserExceptions, BadApiRequestExceptions;

    /**
     * @param user     - User Object
     * @param userId   - User Id
     * @param userName - userName of user
     * @return ResponseEntity<UserDto>
     */
    @PutMapping("/v1/update")
    ResponseEntity<UserDto> updateUserByUserIdOrUserName(@Valid @RequestBody final UserDto user, @RequestParam(required = false) final String userId, @RequestParam(required = false) final String userName) throws UserNotFoundExceptions, UserExceptions, BadApiRequestExceptions;

    /**
     * @param userId   - User Id
     * @param userName - userName of user
     * @return ResponseEntity<ApiResponse>
     */
    @DeleteMapping("/v1/delete")
    ResponseEntity<ApiResponse> deleteUserByUserIdOrUserName(@RequestParam(required = false) final String userId, @RequestParam(required = false) final String userName) throws UserNotFoundExceptions, UserExceptions, BadApiRequestExceptions;

    /**
     * @param pageNumber - current index of page
     * @param pageSize   - size of page
     * @return ResponseEntity<List < UserDTo>>
     */
    @GetMapping("/v1/getAll")
    ResponseEntity<Set<UserDto>> getALlUsers(@RequestParam(defaultValue = "0", required = false) final int pageNumber,
                                             @RequestParam(defaultValue = "5", required = false) final int pageSize) throws UserNotFoundExceptions;

    /**
     * @param email    - email of user
     * @param userName - username of user
     * @return ResponseEntity<UserDto>
     */
    @GetMapping("/v1/info")
    ResponseEntity<UserDto> getUserInformationByEmailOrUserName(@RequestParam(required = false) final String email, @RequestParam(required = false) final String userName) throws UserNotFoundExceptions, UserExceptions, BadApiRequestExceptions;

    /**
     * @param field      - field of User Entity
     * @param value      - value of field
     * @param pageNumber - index of current page
     * @param pageSize   - size of current page
     * @return ResponseEntity<List < UserDto>>
     */
    @GetMapping("/v1/search_by_field")
    ResponseEntity<Set<UserDto>> searchUserByFieldAndValue(@RequestParam final USER_FIELDS field,
                                                           @RequestParam final String value,
                                                           @RequestParam(defaultValue = "0", required = false) final int pageNumber,
                                                           @RequestParam(defaultValue = "5", required = false) final int pageSize) throws UserNotFoundExceptions;

    /**
     * @param userNameWord - Keyword to get multiple users with almost same name initials
     * @return ResponseEntity<List < UserDto>>
     */
    @GetMapping("/v1/search_by_username/{userNameWord}")
    ResponseEntity<Set<UserDto>> searchAllUsersByUserName(@PathVariable("userNameWord") final String userNameWord,
                                                          @RequestParam(defaultValue = "0",required = false) final int pageNumber,
                                                          @RequestParam(defaultValue = "5",required = false) final int pageSize) throws UserNotFoundExceptions;
}
