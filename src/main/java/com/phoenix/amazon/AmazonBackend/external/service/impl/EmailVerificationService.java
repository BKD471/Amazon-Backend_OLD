package com.phoenix.amazon.AmazonBackend.external.service.impl;

import com.phoenix.amazon.AmazonBackend.external.client.IEmailClient;
import com.phoenix.amazon.AmazonBackend.external.service.IEmailVerificationService;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class EmailVerificationService implements IEmailVerificationService {
    private final IEmailClient emailClient;
    private static final String API_HOST_HEADER_VALUE = "quickemailverification.p.rapidapi.com"; // <-- Or replace with your host name
    private static final String API_KEY = "e6ea207e0cmsh0cca9b25fd665cap1e5403jsn906fbfacf70f"; // <-- replace with your API key
    private static final String AUTHORIZATION_KEY = "404a02869683499d77c7dd766e676f3530257faa7ec76bbbd75c45e8179a";


    EmailVerificationService(IEmailClient emailClient){
        this.emailClient=emailClient;
    }

    /**
     * @param email  - email to verify
     * @return Map<String, String>
     */
    @Override
    public Map<String, String> verifyEmail(final String email) {
        Map<String, String> params = new HashMap<>();
        params.put("email", email);
        return emailClient.verify(API_HOST_HEADER_VALUE, API_KEY, AUTHORIZATION_KEY, params);
    }
}
