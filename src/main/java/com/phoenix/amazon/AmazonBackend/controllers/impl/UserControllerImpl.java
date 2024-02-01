package com.phoenix.amazon.AmazonBackend.controllers.impl;

import com.phoenix.amazon.AmazonBackend.controllers.IUserController;
import com.phoenix.amazon.AmazonBackend.dto.ApiResponse;
import com.phoenix.amazon.AmazonBackend.dto.UserDto;
import com.phoenix.amazon.AmazonBackend.exceptions.BadApiRequestExceptions;
import com.phoenix.amazon.AmazonBackend.exceptions.UserExceptions;
import com.phoenix.amazon.AmazonBackend.exceptions.UserNotFoundExceptions;
import com.phoenix.amazon.AmazonBackend.services.IUserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import java.util.Set;

import static com.phoenix.amazon.AmazonBackend.helpers.AllConstantHelpers.USER_FIELDS;

@RestController("UserControllerMain")
public class UserControllerImpl implements IUserController {
    private final IUserService userService;

    UserControllerImpl(IUserService userService) {
        this.userService = userService;
    }

    /**
     * @param user - User Object
     * @return ResponseEntity<UserDto>
     */
    @Override
    public ResponseEntity<UserDto> createUser(final UserDto user) throws UserNotFoundExceptions, UserExceptions, BadApiRequestExceptions {
        UserDto userDto = userService.createUser(user);
        return new ResponseEntity<>(userDto, HttpStatus.CREATED);
    }

    /**
     * @param user     - User Object
     * @param userId   - User Id
     * @param userName - userName of user
     * @return ResponseEntity<UserDto>
     */
    @Override
    public ResponseEntity<UserDto> updateUserByUserIdOrUserName(final UserDto user, final String userId, final String userName) throws UserNotFoundExceptions, UserExceptions, BadApiRequestExceptions {
        UserDto userDto = userService.updateUserByUserIdOrUserName(user, userId, userName);
        return new ResponseEntity<>(userDto, HttpStatus.ACCEPTED);
    }

    /**
     * @param userId   - User Id
     * @param userName - userName of user
     * @return ResponseEntity<ApiResponse>
     */
    @Override
    public ResponseEntity<ApiResponse> deleteUserByUserIdOrUserName(final String userId, final String userName) throws UserNotFoundExceptions, UserExceptions, BadApiRequestExceptions {
        userService.deleteUserByUserIdOrUserName(userId, userName);

        ApiResponse responseMessage = new ApiResponse.builder()
                .message(String.format("User with userName %s deleted successfully!", userName))
                .success(true)
                .status(HttpStatus.OK)
                .build();
        return new ResponseEntity<>(responseMessage, HttpStatus.ACCEPTED);
    }

    /**
     * @return ResponseEntity<List < UserDTo>>
     */
    @Override
    public ResponseEntity<Set<UserDto>> getALlUsers(final int pageNumber, final int pageSize) throws UserNotFoundExceptions {
        Set<UserDto> userDtoSet = userService.getALlUsers(pageNumber, pageSize);
        return new ResponseEntity<>(userDtoSet, HttpStatus.OK);
    }

    /**
     * @param email    - email of user
     * @param userName - username of user
     * @return ResponseEntity<UserDto>
     */
    @Override
    public ResponseEntity<UserDto> getUserInformationByEmailOrUserName(final String email, final String userName) throws UserNotFoundExceptions, UserExceptions, BadApiRequestExceptions {
        UserDto userDto = userService.getUserInformationByEmailOrUserName(email, userName);
        return new ResponseEntity<>(userDto, HttpStatus.OK);
    }

    /**
     * @param field - field of User Entity
     * @param value - value of field
     * @return ResponseEntity<List < UserDto>>
     */
    @Override
    public ResponseEntity<Set<UserDto>> searchUserByFieldAndValue(final USER_FIELDS field, final String value, final int pageNumber, final int pageSize) throws UserNotFoundExceptions {
        Set<UserDto> userDtoSet = userService.searchUserByFieldAndValue(field, value, pageNumber, pageSize);
        return new ResponseEntity<>(userDtoSet, HttpStatus.OK);
    }

    /**
     * @param userNameWord - Keyword to get multiple users with almost same name initials
     * @return ResponseEntity<List < UserDto>>
     */
    @Override
    public ResponseEntity<Set<UserDto>> searchAllUsersByUserName(final String userNameWord,final int pageNumber,final int pageSize) throws UserNotFoundExceptions {
        Set<UserDto> userDtoSet = userService.searchAllUsersByUserName(userNameWord,pageNumber,pageSize);
        return new ResponseEntity<>(userDtoSet, HttpStatus.OK);
    }
}
