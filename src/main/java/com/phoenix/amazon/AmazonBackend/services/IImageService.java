package com.phoenix.amazon.AmazonBackend.services;

import com.phoenix.amazon.AmazonBackend.exceptions.BadApiRequestExceptions;
import com.phoenix.amazon.AmazonBackend.exceptions.UserExceptions;
import com.phoenix.amazon.AmazonBackend.exceptions.UserNotFoundExceptions;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

public interface IImageService {
    /**
    *  @param file          - profile image of user
     * @param primaryEmail  - primary email of user
     * @param userName      - username of user
     * @return string
     * @throws BadApiRequestExceptions,IOException,UserNotFoundExceptions,UserExceptions - list of exception being thrown
    **/
    String upload(final MultipartFile file,final String primaryEmail,final String userName) throws BadApiRequestExceptions, IOException, UserNotFoundExceptions, UserExceptions;

    /**
     * @param primaryEmail  - primary email of user
     * @param userName      - username of user
     * @return InputStream
     * @throws BadApiRequestExceptions,IOException,UserNotFoundExceptions,UserExceptions - list of exception being thrown
     **/
    InputStream getResource(final String primaryEmail,final String userName) throws IOException, UserNotFoundExceptions, UserExceptions, BadApiRequestExceptions;
}
