package com.phoenix.amazon.AmazonBackend.dto;

import static com.phoenix.amazon.AmazonBackend.helpers.AllConstantHelpers.GENDER;

public record UserDto(String userId, String name, String email, String password,
                      GENDER gender, String imageName, String about
) {

    public UserDto(String userId, String name, String email, String password,
                   GENDER gender, String imageName, String about) {
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.password = password;
        this.about = about;
        this.imageName = imageName;
        this.gender = gender;
    }

    public static final class builder {
        private String userId;
        private String name;
        private String email;
        private String password;
        private GENDER gender;
        private String imageName;
        private String about;

        public builder() {
        }

        public builder userId(String userId) {
            this.userId = userId;
            return this;
        }

        public builder name(String name) {
            this.name = name;
            return this;
        }

        public builder email(String email) {
            this.email = email;
            return this;
        }

        public builder password(String password) {
            this.password = password;
            return this;
        }

        public builder gender(GENDER gender) {
            this.gender = gender;
            return this;
        }

        public builder imageName(String imageName) {
            this.imageName = imageName;
            return this;
        }

        public builder about(String about) {
            this.about = about;
            return this;
        }

        public UserDto build() {
            return new UserDto(userId, name, email, password, gender, imageName, about);
        }
    }
}
