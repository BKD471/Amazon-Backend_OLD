package com.phoenix.amazon.AmazonBackend.services.impls;

import com.phoenix.amazon.AmazonBackend.dto.UserDto;
import com.phoenix.amazon.AmazonBackend.entity.Users;
import com.phoenix.amazon.AmazonBackend.exceptions.BadApiRequestExceptions;
import com.phoenix.amazon.AmazonBackend.exceptions.UserExceptions;
import com.phoenix.amazon.AmazonBackend.exceptions.UserNotFoundExceptions;
import com.phoenix.amazon.AmazonBackend.exceptions.builder.ExceptionBuilder;
import com.phoenix.amazon.AmazonBackend.helpers.AllConstantHelpers;
import com.phoenix.amazon.AmazonBackend.repository.IUserRepository;
import com.phoenix.amazon.AmazonBackend.services.AbstractUserService;
import com.phoenix.amazon.AmazonBackend.services.IImageService;
import com.phoenix.amazon.AmazonBackend.services.IUserService;
import com.phoenix.amazon.AmazonBackend.services.validationservice.IUserValidationService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class ImageServiceImpl extends AbstractUserService implements IImageService {
    @Value("${user.profile.images.path}")
    private String imagePath;

    private final IUserService userService;
    private final IUserValidationService userValidationService;

    protected ImageServiceImpl(IUserRepository userRepository,
                               IUserValidationService userValidationService, IUserService userService) {
        super(userRepository, userValidationService);
        this.userValidationService = userValidationService;
        this.userService = userService;
    }

    /**
     * @param file         - profile image of user
     * @param primaryEmail - primary email of user
     * @param userName     - username of user
     * @return string
     * @throws BadApiRequestExceptions,IOException,UserNotFoundExceptions,UserExceptions - list of exception being thrown
     **/
    @Override
    public String uploadUserImageServiceByUserIdOrUserNameOrPrimaryEmail(final MultipartFile image, final String userId, final String userName, final String primaryEmail) throws BadApiRequestExceptions, IOException, UserNotFoundExceptions, UserExceptions {
        final String methodName = "upload(MultipartFile) in ImageServiceImpl";
        final String originalFileName = image.getOriginalFilename();
        Users oldUser = loadUserByUserIdOrUserNameOrPrimaryEmail(userId, userName, primaryEmail, methodName);

        userValidationService.validateNullField(originalFileName, "Something problem with your image!!." +
                "Its either corrupted or not supported", methodName);
        final String fileName = UUID.randomUUID().toString();
        final String extension = originalFileName.substring(originalFileName.lastIndexOf("."));
        final String fileNameWithExtension = fileName + extension;
        final String fullPathWithFileName = imagePath + File.separator + fileNameWithExtension;

        if (extension.equalsIgnoreCase(".jpg") ||
                extension.equalsIgnoreCase(".jpeg") ||
                extension.equalsIgnoreCase(".png") ||
                extension.equalsIgnoreCase(".avif")) {
            File folder = new File(imagePath);
            if (!folder.exists()) folder.mkdirs();
            Files.copy(image.getInputStream(), Paths.get(fullPathWithFileName));

            UserDto newUser = new UserDto.builder().profileImage(fileNameWithExtension).build();
            userService.updateUserServiceByUserIdOrUserNameOrPrimaryEmail(newUser, oldUser.getUserId(), oldUser.getUserName(), oldUser.getPrimaryEmail());
            return fileNameWithExtension;
        } else throw (BadApiRequestExceptions) ExceptionBuilder.builder()
                .className(BadApiRequestExceptions.class)
                .description(String.format(" %s type not supported yet", extension))
                .methodName(methodName)
                .build(AllConstantHelpers.EXCEPTION_CODES.BAD_API_EXEC);

    }

    /**
     * @param primaryEmail - primary email of user
     * @param userName     - username of user
     * @return InputStream
     * @throws BadApiRequestExceptions,IOException,UserNotFoundExceptions,UserExceptions - list of exception being thrown
     **/
    @Override
    public InputStream serveUserImageServiceByUserIdOrUserNameOrPrimaryEmail(final String userId, final String userName, final String primaryEmail) throws IOException, UserNotFoundExceptions, UserExceptions, BadApiRequestExceptions {
        final String methodName = "getResource(String,String) in ImageServiceImpl";
        Users oldUser = loadUserByUserIdOrUserNameOrPrimaryEmail(userId, userName, primaryEmail, methodName);
        final String fullPath = imagePath + File.separator + oldUser.getProfileImage();
        return new FileInputStream(fullPath);
    }
}
