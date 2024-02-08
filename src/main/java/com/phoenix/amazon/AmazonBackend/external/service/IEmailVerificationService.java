package com.phoenix.amazon.AmazonBackend.external.service;

import java.util.Map;

public interface IEmailVerificationService {
    /**
     * @param email - email to verify
     * @return Map<String,String>
     * ***/
    Map<String,String> verifyEmail(final String email);
}