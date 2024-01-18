package com.phoenix.amazon.AmazonBackend.controllers;

import com.phoenix.amazon.AmazonBackend.dto.ApiResponse;
import com.phoenix.amazon.AmazonBackend.dto.UserDto;
import com.phoenix.amazon.AmazonBackend.helpers.AllConstantHelpers;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public interface IUserController {
    @PostMapping
    ResponseEntity<UserDto> createUser(@RequestBody final UserDto user);
    @PutMapping("/update/{userIdOrUserName}")
    ResponseEntity<UserDto> updateUserByUserIdOrUserName(@RequestBody final UserDto user,@PathVariable final String userIdOrUserName);
    @DeleteMapping("/delete/{userIdOrUserName}")
    ResponseEntity<ApiResponse> deleteUserByUserIdOrUserName(@PathVariable final String userIdOrUserName);
    @GetMapping
    ResponseEntity<List<UserDto>> getALlUsers();
    @GetMapping("/{emailOrUserName}")
    ResponseEntity<UserDto> getUserInformationByEmailOrUserName(@PathVariable final String emailOrUserName);
    @GetMapping("/{field}/{value}")
    ResponseEntity<List<UserDto>> searchUserByFieldAndValue(@PathVariable final AllConstantHelpers.USER_FIELDS field,
                                                            @PathVariable final String value);
    @GetMapping("/search/{userNameWord}")
    ResponseEntity<List<UserDto>> searchAllUsersByUserName(final String userNameWord);
}
