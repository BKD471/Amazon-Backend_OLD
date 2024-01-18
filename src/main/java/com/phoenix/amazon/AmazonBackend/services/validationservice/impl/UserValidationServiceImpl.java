package com.phoenix.amazon.AmazonBackend.services.validationservice.impl;

import com.phoenix.amazon.AmazonBackend.entity.Users;
import com.phoenix.amazon.AmazonBackend.helpers.AllConstantHelpers;
import com.phoenix.amazon.AmazonBackend.services.validationservice.IUserValidationService;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Service
public class UserValidationServiceImpl implements IUserValidationService {
    /**
     * @param users
     * @param userValidation
     */
    @Override
    public void validateUser(Optional<Users> users, AllConstantHelpers.USER_VALIDATION userValidation) {
        switch (userValidation){
            case GET_USER_INFO_BY_EMAIL_USER_NAME -> {
                if(users.isEmpty()) throw  new RuntimeException("TO BE......");
            }
        }
    }
}
