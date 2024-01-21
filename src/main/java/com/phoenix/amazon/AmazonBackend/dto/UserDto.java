package com.phoenix.amazon.AmazonBackend.dto;

import java.time.LocalDateTime;

import static com.phoenix.amazon.AmazonBackend.helpers.AllConstantHelpers.GENDER;

public record UserDto(String userId, String userName, String firstName, String lastName, String email, String password,
                      GENDER gender, String profileImage, String about, LocalDateTime lastSeen
) {

    public UserDto(String userId, String userName, String firstName, String lastName, String email, String password,
                   GENDER gender, String profileImage, String about, LocalDateTime lastSeen) {
        this.userId = userId;
        this.userName = userName;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
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
        private String email;
        private String password;
        private GENDER gender;
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

        public builder email(final String email) {
            this.email = email;
            return this;
        }

        public builder password(final String password) {
            this.password = password;
            return this;
        }

        public builder gender(final GENDER gender) {
            this.gender = gender;
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
                    email,
                    password,
                    gender,
                    profileImage,
                    about,
                    lastSeen);
        }
    }
}
