package com.phoenix.amazon.AmazonBackend.controllers;

import com.phoenix.amazon.AmazonBackend.dto.ApiResponse;
import com.phoenix.amazon.AmazonBackend.dto.UserDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
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

@RestController
@RequestMapping("/api/users")
public interface IUserController {
    @PostMapping
    ResponseEntity<UserDto> createUser(@RequestBody final UserDto user);
    @PutMapping("/update")
    ResponseEntity<UserDto> updateUserByUserIdOrUserName(@RequestBody final UserDto user,@RequestParam final String userId,@RequestParam final String userName);
    @DeleteMapping("/delete")
    ResponseEntity<ApiResponse> deleteUserByUserIdOrUserName(@RequestParam final String userId,@RequestParam final String userName);
    @GetMapping
    ResponseEntity<Set<UserDto>> getALlUsers();
    @GetMapping("/info")
    ResponseEntity<UserDto> getUserInformationByEmailOrUserName(@RequestParam final String email,@RequestParam final String userName);
    @GetMapping("/{field}/{value}")
    ResponseEntity<Set<UserDto>> searchUserByFieldAndValue(@PathVariable final USER_FIELDS field, @PathVariable final String value);
    @GetMapping("/search/{userNameWord}")
    ResponseEntity<Set<UserDto>> searchAllUsersByUserName(final String userNameWord);
}
