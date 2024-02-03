package com.phoenix.amazon.AmazonBackend.dto;

import com.phoenix.amazon.AmazonBackend.dto.fieldvalidator.ValidUserName;
import com.phoenix.amazon.AmazonBackend.dto.fieldvalidator.ValidName;
import com.phoenix.amazon.AmazonBackend.dto.fieldvalidator.NullOrEmail;
import com.phoenix.amazon.AmazonBackend.dto.fieldvalidator.ValidPassword;
import com.phoenix.amazon.AmazonBackend.dto.fieldvalidator.ValidGender;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;


public record UserDto(String userId,
                      @Size(min = 3, max = 10, message = "UserName must be at least 3 chars long and 10 chars at max")
                      @NotNull(message = "Please give your userName")
                      @ValidUserName
                      String userName,
                      @ValidName
                      @NotNull(message = "Please give your firstName")
                      String firstName,
                      @ValidName
                      @NotNull(message = "Please give your lastName")
                      String lastName,
                      @Email(message = "Please provide a valid primary email")
                      @Size(min = 1,message = "Please provide a valid primary email")
                      @NotNull(message = "Please give your email id")
                      String primaryEmail,
                      @NullOrEmail
                      @Size(min = 1,message = "Please provide a valid secondary email")
                      String secondaryEmail,
                      @ValidPassword
                      @NotNull(message = "Please give your password")
                      String password,
                      @ValidGender
                      @NotNull(message = "Please give your gender type")
                      String gender,
                      String profileImage,
                      @Size(max = 500, message = "Please kept it below 500 characters")
                      String about,
                      LocalDateTime lastSeen
) {

    public UserDto(String userId, String userName, String firstName, String lastName,
                   String primaryEmail, String secondaryEmail, String password,
                   String gender, String profileImage, String about, LocalDateTime lastSeen) {
        this.userId = userId;
        this.userName = userName;
        this.firstName = firstName;
        this.lastName = lastName;
        this.primaryEmail = primaryEmail;
        this.secondaryEmail = secondaryEmail;
        this.password = password;
        this.about = about;
        this.profileImage = profileImage;
        this.gender = gender;
        this.lastSeen = lastSeen;
    }

    public static final class builder {
        private String userId;
        private String userName;
        private String firstName;
        private String lastName;
        private String primaryEmail;
        private String secondaryEmail;
        private String password;
        private String gender;
        private String profileImage;
        private String about;
        private LocalDateTime lastSeen;

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

        public builder password(final String password) {
            this.password = password;
            return this;
        }

        public builder gender(final String gender) {
            this.gender=gender;
            return this;
        }

        public builder profileImage(final String profileImage) {
            this.profileImage = profileImage;
            return this;
        }

        public builder about(final String about) {
            this.about = about;
            return this;
        }

        public builder lastSeen(final LocalDateTime lastSeen) {
            this.lastSeen = lastSeen;
            return this;
        }

        public UserDto build() {
            return new UserDto(userId,
                    userName,
                    firstName,
                    lastName,
                    primaryEmail,
                    secondaryEmail,
                    password,
                    gender,
                    profileImage,
                    about,
                    lastSeen);
        }
    }
}
