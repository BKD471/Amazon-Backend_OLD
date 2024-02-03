package com.phoenix.amazon.AmazonBackend.services;

import com.phoenix.amazon.AmazonBackend.exceptions.BadApiRequestExceptions;
import com.phoenix.amazon.AmazonBackend.exceptions.UserExceptions;
import com.phoenix.amazon.AmazonBackend.exceptions.UserNotFoundExceptions;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public interface IImageService {
    String upload(final MultipartFile multipartFile,final String primaryEmail,final String userName) throws BadApiRequestExceptions, IOException, UserNotFoundExceptions, UserExceptions;
    InputStream getResource(final String primaryEmail,final String userName) throws FileNotFoundException, UserNotFoundExceptions, UserExceptions, BadApiRequestExceptions;
}
