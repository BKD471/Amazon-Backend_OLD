package com.phoenix.amazon.AmazonBackend.services.impls;

import com.phoenix.amazon.AmazonBackend.entity.Users;
import com.phoenix.amazon.AmazonBackend.exceptions.BadApiRequestExceptions;
import com.phoenix.amazon.AmazonBackend.exceptions.UserExceptions;
import com.phoenix.amazon.AmazonBackend.exceptions.UserNotFoundExceptions;
import com.phoenix.amazon.AmazonBackend.exceptions.builder.ExceptionBuilder;
import com.phoenix.amazon.AmazonBackend.helpers.AllConstantHelpers;
import com.phoenix.amazon.AmazonBackend.repository.IUserRepository;
import com.phoenix.amazon.AmazonBackend.services.AbstractService;
import com.phoenix.amazon.AmazonBackend.services.IImageService;
import com.phoenix.amazon.AmazonBackend.services.validationservice.IUserValidationService;
import com.phoenix.amazon.AmazonBackend.services.validationservice.impl.ImageValidationServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.Properties;
import java.util.UUID;

import static com.phoenix.amazon.AmazonBackend.helpers.AllConstantHelpers.IMAGE_VALIDATION.GET_PROFILE_IMAGE;
import static com.phoenix.amazon.AmazonBackend.helpers.AllConstantHelpers.IMAGE_VALIDATION.UPDATE_PROFILE_IMAGE;
import static com.phoenix.amazon.AmazonBackend.helpers.AllConstantHelpers.USER_FIELDS.PROFILE_IMAGE;


@Service("ImageServicePrimary")
public class ImageServiceImpl extends AbstractService implements IImageService {
    private final IUserRepository userRepository;
    private final IUserValidationService userValidationService;
    private final ImageValidationServiceImpl imageValidationService;
    private final String userImagePath;
    private final String categoryImagePath;
    Logger logger = LoggerFactory.getLogger(ImageServiceImpl.class);


    protected ImageServiceImpl(IUserRepository userRepository,
                               IUserValidationService userValidationService,
                               ImageValidationServiceImpl imageValidationService,
                               @Value("${path.services.image.properties}") final String PATH_TO_IMAGE_PROPS) {
        super(userRepository, userValidationService);
        this.userValidationService = userValidationService;
        this.imageValidationService=imageValidationService;
        this.userRepository = userRepository;
        final Properties properties = new Properties();
        try {
            properties.load(new FileInputStream(PATH_TO_IMAGE_PROPS));
        } catch (IOException e) {
            logger.error("Error in reading the props in {} ImageService", e.getMessage());
        }
        this.userImagePath = properties.getProperty("user.profile.images.path");
        this.categoryImagePath=properties.getProperty("category.images.path");
    }

    private String processImageUpload(final MultipartFile image, final String imagePath, final String methodName) throws BadApiRequestExceptions, IOException {
        final String originalFileName = image.getOriginalFilename();
        validateNullField(originalFileName, "Something problem with your image!!." +
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
            return fileNameWithExtension;
        } else throw (BadApiRequestExceptions) ExceptionBuilder.builder()
                .className(BadApiRequestExceptions.class)
                .description(String.format(" %s type not supported yet", extension))
                .methodName(methodName)
                .build(AllConstantHelpers.EXCEPTION_CODES.BAD_API_EXEC);
    }

    /**
     * @param image        - profile image of user
     * @param userId       - userId of user
     * @param userName     - username of user
     * @param primaryEmail - primary email of user
     * @return string
     * @throws BadApiRequestExceptions,IOException,UserNotFoundExceptions,UserExceptions - list of exception being thrown
     **/
    @Override
    public String uploadUserImageServiceByUserIdOrUserNameOrPrimaryEmail(final MultipartFile image, final String userId, final String userName, final String primaryEmail) throws BadApiRequestExceptions, IOException, UserNotFoundExceptions, UserExceptions {
        final String methodName = "uploadUserImageServiceByUserIdOrUserNameOrPrimaryEmail(MultipartFile) in ImageServiceImpl";
        Users fetchedUser = loadUserByUserIdOrUserNameOrPrimaryEmail(userId, userName, primaryEmail, methodName);
        final String fileNameWithExtension = processImageUpload(image,userImagePath,methodName);

        //validate
        Users newUser = new Users.builder().profileImage(fileNameWithExtension).build();
        imageValidationService.validateUserImage(newUser, fetchedUser, methodName, UPDATE_PROFILE_IMAGE);

        //update profile image of user
        Users updatedUser = constructUser(fetchedUser, newUser, PROFILE_IMAGE);
        userRepository.save(updatedUser);
        return fileNameWithExtension;
    }

    @Override
    public String uploadCoverImageByCategoryId(final MultipartFile image) throws BadApiRequestExceptions, IOException {
        final String methodName = "uploadUserImageServiceByUserIdOrUserNameOrPrimaryEmail(MultipartFile) in ImageServiceImpl";
        return processImageUpload(image,categoryImagePath,methodName);
    }

    /**
     * @param userId       - userId of user
     * @param userName     - username of user
     * @param primaryEmail - primary email of user
     * @return InputStream
     * @throws BadApiRequestExceptions,IOException,UserNotFoundExceptions,UserExceptions - list of exception being thrown
     **/
    @Override
    public InputStream serveUserImageServiceByUserIdOrUserNameOrPrimaryEmail(final String userId, final String userName, final String primaryEmail) throws IOException, UserNotFoundExceptions, UserExceptions, BadApiRequestExceptions {
        final String methodName = "getResource(String,String) in ImageServiceImpl";
        Users oldUser = loadUserByUserIdOrUserNameOrPrimaryEmail(userId, userName, primaryEmail, methodName);

        imageValidationService.validateUserImage(null, oldUser, methodName, GET_PROFILE_IMAGE);
        final String fullPath = userImagePath + File.separator + oldUser.getProfileImage();
        return new FileInputStream(fullPath);
    }
}
