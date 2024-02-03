package com.phoenix.amazon.AmazonBackend.controllers.impl;

import com.phoenix.amazon.AmazonBackend.controllers.IUserController;
import com.phoenix.amazon.AmazonBackend.dto.ApiResponse;

import com.phoenix.amazon.AmazonBackend.dto.ImageResponseMessages;

import com.phoenix.amazon.AmazonBackend.dto.PageableResponse;

import com.phoenix.amazon.AmazonBackend.dto.UserDto;
import com.phoenix.amazon.AmazonBackend.exceptions.BadApiRequestExceptions;
import com.phoenix.amazon.AmazonBackend.exceptions.UserExceptions;
import com.phoenix.amazon.AmazonBackend.exceptions.UserNotFoundExceptions;

import com.phoenix.amazon.AmazonBackend.services.IImageService;

import com.phoenix.amazon.AmazonBackend.helpers.AllConstantHelpers.USER_FIELDS;

import com.phoenix.amazon.AmazonBackend.services.IUserService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;


import java.io.IOException;
import java.io.InputStream;
import java.util.Set;

import static com.phoenix.amazon.AmazonBackend.helpers.AllConstantHelpers.USER_FIELDS;

@RestController("UserControllerMain")
public class UserControllerImpl implements IUserController {
    private final IUserService userService;
    private final IImageService imageService;

    UserControllerImpl(IUserService userService, IImageService imageService) {
        this.userService = userService;
        this.imageService = imageService;
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
    public ResponseEntity<PageableResponse<UserDto>> searchUserByFieldAndValue(final USER_FIELDS field, final String value, final int pageNumber, final int pageSize, final USER_FIELDS sortBy, final String sortDir) throws UserNotFoundExceptions {
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

    /**
     * @param image
     * @param userName
     * @return
     * @throws IOException
     */
    @Override
    public ResponseEntity<ImageResponseMessages> uploadCustomerImage(final MultipartFile image,
                                                                     final String primaryEmail,
                                                                     final String userName) throws IOException, BadApiRequestExceptions, UserNotFoundExceptions, UserExceptions {
        final String imageName = imageService.upload(image, primaryEmail, userName);
        ImageResponseMessages imageResponseMessages =
                new ImageResponseMessages.Builder()
                        .imageName(imageName)
                        .message(" Profile image has been uploaded successfully")
                        .status(HttpStatus.ACCEPTED)
                        .success(true)
                        .build();
        return new ResponseEntity<>(imageResponseMessages, HttpStatus.ACCEPTED);
    }

    /**
     * @param userName
     * @param response
     * @throws IOException
     */
    @Override
    public void serveUserImage(final String primaryEmail, final String userName, HttpServletResponse response) throws IOException, UserNotFoundExceptions, UserExceptions, BadApiRequestExceptions {
        InputStream resource = imageService.getResource(primaryEmail, userName);
        response.setContentType(MediaType.IMAGE_JPEG_VALUE);
        StreamUtils.copy(resource, response.getOutputStream());
    }
}
