package com.phoenix.amazon.AmazonBackend.controllers.impl;

import com.phoenix.amazon.AmazonBackend.controllers.IUserController;
import com.phoenix.amazon.AmazonBackend.dto.ApiResponse;
import com.phoenix.amazon.AmazonBackend.dto.PageableResponse;
import com.phoenix.amazon.AmazonBackend.dto.UserDto;
import com.phoenix.amazon.AmazonBackend.exceptions.BadApiRequestExceptions;
import com.phoenix.amazon.AmazonBackend.exceptions.UserExceptions;
import com.phoenix.amazon.AmazonBackend.exceptions.UserNotFoundExceptions;
import com.phoenix.amazon.AmazonBackend.helpers.AllConstantHelpers.User_DB_FIELDS;
import com.phoenix.amazon.AmazonBackend.services.IUserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import static com.phoenix.amazon.AmazonBackend.helpers.AllConstantHelpers.USER_FIELDS;

@RestController("UserControllerMain")
public class UserControllerImpl implements IUserController {
    private final IUserService userService;

    UserControllerImpl(IUserService userService) {
        this.userService = userService;
    }

    /**
     * @param user - User Object
     * @return ResponseEntity<UserDto> - UserDto Object
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
     * @return ResponseEntity<UserDto> - UserDto Object
     */
    @Override
    public ResponseEntity<UserDto> updateUserByUserIdOrUserName(final UserDto user, final String userId, final String userName) throws UserNotFoundExceptions, UserExceptions, BadApiRequestExceptions {
        UserDto userDto = userService.updateUserByUserIdOrUserName(user, userId, userName);
        return new ResponseEntity<>(userDto, HttpStatus.ACCEPTED);
    }

    /**
     * @param userId   - User Id
     * @param userName - userName of user
     * @return ResponseEntity<ApiResponse> - ApiResponse Object
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
     * @param pageNumber - index value of page
     * @param pageSize   - size of page
     * @param sortBy     - sort column
     * @param sortDir    - direction of sorting
     * @return ResponseEntity<PageableResponse < UserDTo>> - list of userDtp
     */
    @Override
    public ResponseEntity<PageableResponse<UserDto>> getAllUsers(final int pageNumber, final int pageSize, final String sortBy, final String sortDir) throws UserNotFoundExceptions {
        PageableResponse<UserDto> userDtoSet = userService.getAllUsers(pageNumber, pageSize, sortBy, sortDir);
        return new ResponseEntity<>(userDtoSet, HttpStatus.OK);
    }

    /**
     * @param primaryEmail - primary email of user
     * @param userName     - username of user
     * @return ResponseEntity<UserDto> - userDto Object
     */
    @Override
    public ResponseEntity<UserDto> getUserInformationByPrimaryEmailOrUserName(final String primaryEmail, final String userName) throws UserNotFoundExceptions, UserExceptions, BadApiRequestExceptions {
        UserDto userDto = userService.getUserInformationByEmailOrUserName(primaryEmail, userName);
        return new ResponseEntity<>(userDto, HttpStatus.OK);
    }

    /**
     * @param field      - field of User Entity
     * @param value      - value of field
     * @param pageNumber - index value of page
     * @param pageSize   - size of page
     * @param sortBy     - sort column
     * @param sortDir    - direction of sorting
     * @return ResponseEntity<PageableResponse < UserDto>> - list of UserDto
     */
    @Override
    public ResponseEntity<PageableResponse<UserDto>> searchUserByFieldAndValue(final USER_FIELDS field, final String value, final int pageNumber, final int pageSize, final User_DB_FIELDS sortBy, final String sortDir) throws UserNotFoundExceptions {
        PageableResponse<UserDto> userDtoSet = userService.searchUserByFieldAndValue(field, value, pageNumber, pageSize, sortBy, sortDir);
        return new ResponseEntity<>(userDtoSet, HttpStatus.OK);
    }

    /**
     * @param userNameWord - Keyword to get multiple users with almost same name initials
     * @param pageNumber   - index value of page
     * @param pageSize     - size of page
     * @param sortBy       - sort column
     * @param sortDir      - direction of sorting
     * @return ResponseEntity<PageableResponse < UserDto>> - list of userDto
     */
    @Override
    public ResponseEntity<PageableResponse<UserDto>> searchAllUsersByUserName(final String userNameWord, final int pageNumber, final int pageSize, final String sortBy, final String sortDir) throws UserNotFoundExceptions {
        PageableResponse<UserDto> userDtoSet = userService.searchAllUsersByUserName(userNameWord, pageNumber, pageSize, sortBy, sortDir);
        return new ResponseEntity<>(userDtoSet, HttpStatus.OK);
    }
}
