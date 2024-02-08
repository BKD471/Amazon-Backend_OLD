package com.phoenix.amazon.AmazonBackend.external.service.impl;

import com.phoenix.amazon.AmazonBackend.external.client.IEmailClient;
import com.phoenix.amazon.AmazonBackend.external.service.IEmailVerificationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

@Service
public class EmailVerificationService implements IEmailVerificationService {
    private final IEmailClient emailClient;
    private final String API_HOST_HEADER_VALUE;
    private final String API_KEY;
    private final String AUTHORIZATION_KEY;
    private final Properties properties;
    Logger logger = LoggerFactory.getLogger(EmailVerificationService.class);

    EmailVerificationService(IEmailClient emailClient,
                             @Value("${path.user.verification.service.properties}") final String PATH_TO_PROPS_FILE) {
        properties = new Properties();
        try {
            properties.load(new FileInputStream(PATH_TO_PROPS_FILE));
        } catch (IOException ex) {
            logger.error("Error in reading the props in {} EmailVerificationService", ex.getMessage());
        }
        this.emailClient = emailClient;
        this.API_HOST_HEADER_VALUE = properties.getProperty("api.host.header.value");
        this.API_KEY = properties.getProperty("api.key");
        this.AUTHORIZATION_KEY = properties.getProperty("api.auth.key");
    }

    /**
     * @param email - email to verify
     * @return Map<String, String>
     */
    @Override
    public Map<String, String> verifyEmail(final String email) {
        Map<String, String> params = new HashMap<>();
        params.put("email", email);
        return emailClient.verify(API_HOST_HEADER_VALUE, API_KEY, AUTHORIZATION_KEY, params);
    }
}