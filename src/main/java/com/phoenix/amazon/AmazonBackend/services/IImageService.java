package com.phoenix.amazon.AmazonBackend.services;

import com.phoenix.amazon.AmazonBackend.exceptions.BadApiRequestExceptions;
import com.phoenix.amazon.AmazonBackend.exceptions.UserExceptions;
import com.phoenix.amazon.AmazonBackend.exceptions.UserNotFoundExceptions;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

public interface IImageService {
    /**
     * @param image        - profile image of user
     * @param userId       - userId of user
     * @param userName     - userName of user
     * @param primaryEmail - primary email of user
     * @return string
     * @throws BadApiRequestExceptions,IOException,UserNotFoundExceptions,UserExceptions - list of exception being thrown
     **/
    String uploadUserImageServiceByUserIdOrUserNameOrPrimaryEmail(final MultipartFile image, final String userId, final String userName, final String primaryEmail) throws BadApiRequestExceptions, IOException, UserNotFoundExceptions, UserExceptions;

    /**
     * @param userId       - userId of user
     * @param userName     - userName of user
     * @param primaryEmail - primary email of user
     * @return InputStream
     * @throws BadApiRequestExceptions,IOException,UserNotFoundExceptions,UserExceptions - list of exception being thrown
     **/
    InputStream serveUserImageServiceByUserIdOrUserNameOrPrimaryEmail(final String userId, final String userName, final String primaryEmail) throws IOException, UserNotFoundExceptions, UserExceptions, BadApiRequestExceptions;
}
