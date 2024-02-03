package com.phoenix.amazon.AmazonBackend.services.impls;

import com.phoenix.amazon.AmazonBackend.dto.UserDto;
import com.phoenix.amazon.AmazonBackend.entity.Users;
import com.phoenix.amazon.AmazonBackend.exceptions.BadApiRequestExceptions;
import com.phoenix.amazon.AmazonBackend.exceptions.UserExceptions;
import com.phoenix.amazon.AmazonBackend.exceptions.UserNotFoundExceptions;
import com.phoenix.amazon.AmazonBackend.exceptions.builder.ExceptionBuilder;
import com.phoenix.amazon.AmazonBackend.helpers.AllConstantHelpers;
import com.phoenix.amazon.AmazonBackend.helpers.MappingHelpers;
import com.phoenix.amazon.AmazonBackend.repository.IUserRepository;
import com.phoenix.amazon.AmazonBackend.services.AbstractUserService;
import com.phoenix.amazon.AmazonBackend.services.IImageService;
import com.phoenix.amazon.AmazonBackend.services.IUserService;
import com.phoenix.amazon.AmazonBackend.services.validationservice.IUserValidationService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class ImageServiceImpl extends AbstractUserService implements IImageService {
    @Value("${user.profile.images.path}")
    private String path;

    private final IUserService userService;

    protected ImageServiceImpl(IUserRepository userRepository,
                               IUserValidationService userValidationService, IUserService userService) {
        super(userRepository, userValidationService);
        this.userService = userService;
    }

    /**
     * @param file
     * @return
     */
    @Override
    public String upload(final MultipartFile file, final String primaryEmail, final String userName) throws BadApiRequestExceptions, IOException, UserNotFoundExceptions, UserExceptions {
        final String methodName = "upload(MultipartFile) in ImageServiceImpl";
        final String originalFileName = file.getOriginalFilename();
        Users oldUser = loadUserByEmailOrUserName(primaryEmail, userName, methodName);

        if (StringUtils.isBlank(originalFileName)) ;
        final String fileName = UUID.randomUUID().toString();
        final String extension = originalFileName.substring(originalFileName.lastIndexOf("."));
        final String fileNameWithExtension = fileName + extension;
        final String fullPathWithFileName = path + File.separator + fileNameWithExtension;

        if (extension.equalsIgnoreCase(".jpg") ||
                extension.equalsIgnoreCase(".jpeg") ||
                extension.equalsIgnoreCase(".png") ||
                extension.equalsIgnoreCase(".avif")) {
            File folder = new File(path);
            if (!folder.exists()) folder.mkdirs();
            Files.copy(file.getInputStream(), Paths.get(fullPathWithFileName));


            UserDto newUser = new UserDto.builder().profileImage(fileNameWithExtension).build();
            userService.updateUserByUserIdOrUserName(newUser, oldUser.getUserId(), oldUser.getUserName());
            return fileNameWithExtension;
        } else throw (BadApiRequestExceptions) ExceptionBuilder.builder()
                .className(BadApiRequestExceptions.class)
                .description(String.format(" %s type not supported yet", extension))
                .methodName(methodName)
                .build(AllConstantHelpers.EXCEPTION_CODES.BAD_API_EXEC);

    }

    /**
     * @param name
     * @return
     */
    @Override
    public InputStream getResource(final String primaryEmail, final String userName) throws FileNotFoundException, UserNotFoundExceptions, UserExceptions, BadApiRequestExceptions {
        final String methodName = "getResource(String,String) in ImageServiceImpl";
        Users oldUser = loadUserByEmailOrUserName(primaryEmail, userName, methodName);
        final String fullPath = path + File.separator + oldUser.getProfileImage();
        return new FileInputStream(fullPath);
    }
}
