package com.phoenix.amazon.AmazonBackend.dto;

import com.phoenix.amazon.AmazonBackend.helpers.GENDER;

public record UserDto(String userId, String name, String email, String password,
                      GENDER gender, String imageName, String about
) {
}
