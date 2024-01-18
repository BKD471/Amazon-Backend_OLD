package com.phoenix.amazon.AmazonBackend.services.validationservice;

import com.phoenix.amazon.AmazonBackend.entity.Users;

import java.util.Optional;

import static com.phoenix.amazon.AmazonBackend.helpers.AllConstantHelpers.USER_VALIDATION;

public interface IUserValidationService {
    void validateUser(final Optional<Users> users, final USER_VALIDATION userValidation);
}
