package com.phoenix.amazon.AmazonBackend.external.service;

import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public interface IEmailVerificationService {
    /**
     * @param email - email to verify
     * @return Map<String,String>
     * ***/
    Map<String,String> verifyEmail(final String email);
}
