package com.phoenix.amazon.AmazonBackend.controllers.impl;

import com.phoenix.amazon.AmazonBackend.controllers.IUserController;
import com.phoenix.amazon.AmazonBackend.dto.UserDto;
import com.phoenix.amazon.AmazonBackend.helpers.AllConstantHelpers;
import com.phoenix.amazon.AmazonBackend.services.IUserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class UserControllerImpl implements IUserController {
    private final IUserService userService;

    UserControllerImpl(IUserService userService) {
        this.userService = userService;
    }

    /**
     * @param user
     * @return
     */
    @Override
    public ResponseEntity<UserDto> createUser(final UserDto user) {
        UserDto userDto = userService.createUser(user);
        return new ResponseEntity<>(userDto, HttpStatus.CREATED);
    }

    /**
     * @param user
     * @param userIdOrUserName
     * @return
     */
    @Override
    public ResponseEntity<UserDto> updateUserByUserIdOrUserName(final UserDto user,final String userIdOrUserName) {
        UserDto userDto = userService.updateUserByUserIdOrUserName(user,userIdOrUserName);
        return new ResponseEntity<>(userDto, HttpStatus.ACCEPTED);
    }

    /**
     * @param userIdOrUserName
     * @return
     */
    @Override
    public ResponseEntity<Object> deleteUserByUserIdOrUserName(final String userIdOrUserName) {
        userService.deleteUserByUserIdOrUserName(userIdOrUserName);
        return new ResponseEntity<>(null, HttpStatus.ACCEPTED);
    }

    /**
     * @return
     */
    @Override
    public ResponseEntity<List<UserDto>> getALlUsers() {
        List<UserDto> userDtoList = userService.getALlUsers();
        return new ResponseEntity<>(userDtoList, HttpStatus.OK);
    }

    /**
     * @param emailOrUserName
     * @return
     */
    @Override
    public ResponseEntity<UserDto> getUserInformationByEmailOrUserName(final String emailOrUserName) {
        UserDto userDto = userService.getUserInformationByEmailOrUserName(emailOrUserName);
        return new ResponseEntity<>(userDto, HttpStatus.OK);
    }

    /**
     * @param field
     * @param value
     * @return
     */
    @Override
    public ResponseEntity<List<UserDto>> searchUserByFieldAndValue(AllConstantHelpers.USER_FIELDS field, String value) {
        List<UserDto> userDtoList = userService.searchUserByFieldAndValue(field,value);
        return new ResponseEntity<>(userDtoList, HttpStatus.OK);
    }

    /**
     * @param userNameWord
     * @return
     */
    @Override
    public ResponseEntity<List<UserDto>> searchAllUsersByUserName(String userNameWord) {
        List<UserDto> userDtoList = userService.searchAllUsersByUserName(userNameWord);
        return new ResponseEntity<>(userDtoList, HttpStatus.OK);
    }
}
