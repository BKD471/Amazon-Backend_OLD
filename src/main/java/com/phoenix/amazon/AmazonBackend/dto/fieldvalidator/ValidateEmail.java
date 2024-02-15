package com.phoenix.amazon.AmazonBackend.dto.fieldvalidator;

import com.phoenix.amazon.AmazonBackend.exceptions.ServicDownTimeException;
import com.phoenix.amazon.AmazonBackend.external.service.IEmailVerificationService;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.Clock;
import java.time.Duration;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Map;

@Component
public class ValidateEmail implements ConstraintValidator<ValidEmail, String> {
    private final IEmailVerificationService emailVerificationService;

    Logger logger = LoggerFactory.getLogger(ValidateEmail.class);

    ValidateEmail(IEmailVerificationService emailVerificationService) {
        this.emailVerificationService = emailVerificationService;
    }

    /**
     * @param constraintAnnotation - constraint annotation
     */
    @Override
    public void initialize(ValidEmail constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    /**
     * @param email                      - email to verify
     * @param constraintValidatorContext - constraint validator context
     * @return boolean
     */
    @Override
    public boolean isValid(String email, ConstraintValidatorContext constraintValidatorContext) {
        final String methodName = "isValid(email,constraintValidatorContext) in constraintValidatorContext";
        //check null or whitespace
        if (StringUtils.isBlank(email)) return false;

        LocalTime startTime = LocalTime.now(Clock.system(ZoneId.of("Asia/Kolkata")));
        logger.info(String.format("<############## %s   ValidateNullOrEmail field validation starts ########################" +
                "#################################################################", startTime));
        return true;
//        Map<String, String> response;
//        try {
//            response = emailVerificationService.verifyEmail(email);
//            if (response.isEmpty())
//                throw new ServicDownTimeException(ServicDownTimeException.class, "Email Verifier Api is down", methodName);
//
//            if (response.containsKey("result")) {
//                String value = response.get("result");
//                return value.equalsIgnoreCase("valid");
//            } else
//                throw new ServicDownTimeException(ServicDownTimeException.class, "Response from Api has either changed or corrupted", methodName);
//        } catch (Exception ex) {
//            logger.error("Oops !! {}", ex.getMessage());
//            return false;
//        } finally {
//            LocalTime endTime = LocalTime.now(Clock.system(ZoneId.of("Asia/Kolkata")));
//            long durationInMs = Duration.between(startTime, endTime).toMillis();
//            logger.info(String.format("<################# Time elapsed to execute ValidateEmail is %s Ms ##############################" +
//                    "######################################################################################" +
//                    "#########>", durationInMs));
//
//            logger.info(String.format("<############## %s   ValidateEmail field validation ends ########################" +
//                    "#################################################################", endTime));
        }
}
