package com.phoenix.amazon.AmazonBackend.controllers;

import com.phoenix.amazon.AmazonBackend.dto.ApiResponse;
import com.phoenix.amazon.AmazonBackend.dto.UserDto;
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
    ResponseEntity<UserDto> createUser(@RequestBody final UserDto user);

    /**
     * @param user     - User Object
     * @param userId   - User Id
     * @param userName - userName of user
     * @return ResponseEntity<UserDto>
     */
    @PutMapping("/v1/update")
    ResponseEntity<UserDto> updateUserByUserIdOrUserName(@RequestBody final UserDto user, @RequestParam final String userId, @RequestParam final String userName);

    /**
     * @param userId   - User Id
     * @param userName - userName of user
     * @return ResponseEntity<ApiResponse>
     */
    @DeleteMapping("/v1/delete")
    ResponseEntity<ApiResponse> deleteUserByUserIdOrUserName(@RequestParam final String userId, @RequestParam final String userName);

    /**
     * @return ResponseEntity<List < UserDTo>>
     */
    @GetMapping("/v1/getAll")
    ResponseEntity<Set<UserDto>> getALlUsers();

    /**
     * @param email    - email of user
     * @param userName - username of user
     * @return ResponseEntity<UserDto>
     */
    @GetMapping("/v1/info")
    ResponseEntity<UserDto> getUserInformationByEmailOrUserName(@RequestParam final String email, @RequestParam final String userName);

    /**
     * @param field - field of User Entity
     * @param value - value of field
     * @return ResponseEntity<List < UserDto>>
     */
    @GetMapping("/v1/search_by_field/{field}/{value}")
    ResponseEntity<Set<UserDto>> searchUserByFieldAndValue(@PathVariable final USER_FIELDS field, @PathVariable final String value);

    /**
     * @param userNameWord - Keyword to get multiple users with almost same name initials
     * @return ResponseEntity<List < UserDto>>
     */
    @GetMapping("/v1/search_by_username/{userNameWord}")
    ResponseEntity<Set<UserDto>> searchAllUsersByUserName(final String userNameWord);
}
