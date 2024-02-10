package com.phoenix.amazon.AmazonBackend.dto;

import com.phoenix.amazon.AmazonBackend.dto.fieldvalidator.NullOrEmail;
import com.phoenix.amazon.AmazonBackend.dto.fieldvalidator.ValidEmail;
import com.phoenix.amazon.AmazonBackend.dto.fieldvalidator.ValidName;
import com.phoenix.amazon.AmazonBackend.dto.fieldvalidator.ValidUserName;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;


public record UpdateUserDto(String userId,
                            @ValidUserName
                            String userName,
                            @ValidName
                            String firstName,
                            @ValidName
                            String lastName,
                            @ValidEmail
                            String primaryEmail,
                            @NullOrEmail
                            String secondaryEmail,
                            @NotNull(message = "Please give your gender type")
                            String gender,
                            @Size(max = 500, message = "Please kept it below 500 characters")
                            String about
) {

    public UpdateUserDto(String userId, String userName, String firstName, String lastName,
                         String primaryEmail, String secondaryEmail,
                         String gender, String about) {
        this.userId = userId;
        this.userName = userName;
        this.firstName = firstName;
        this.lastName = lastName;
        this.primaryEmail = primaryEmail;
        this.secondaryEmail = secondaryEmail;
        this.about = about;
        this.gender = gender;

    }

    public static final class builder {
        private String userId;
        private String userName;
        private String firstName;
        private String lastName;
        private String primaryEmail;
        private String secondaryEmail;
        private String gender;
        private String about;

        public builder() {
        }

        public builder userId(final String userId) {
            this.userId = userId;
            return this;
        }

        public builder userName(final String userName) {
            this.userName = userName;
            return this;
        }

        public builder firstName(final String firstName) {
            this.firstName = firstName;
            return this;
        }

        public builder lastName(final String lastName) {
            this.lastName = lastName;
            return this;
        }

        public builder primaryEmail(final String primaryEmail) {
            this.primaryEmail = primaryEmail;
            return this;
        }

        public builder secondaryEmail(final String secondaryEmail) {
            this.secondaryEmail = secondaryEmail;
            return this;
        }

        public builder gender(final String gender) {
            this.gender = gender;
            return this;
        }

        public builder about(final String about) {
            this.about = about;
            return this;
        }

        public UpdateUserDto build() {
            return new UpdateUserDto(userId,
                    userName,
                    firstName,
                    lastName,
                    primaryEmail,
                    secondaryEmail,
                    gender,
                    about);
        }
    }
}
