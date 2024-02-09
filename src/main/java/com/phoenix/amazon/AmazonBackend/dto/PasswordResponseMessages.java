package com.phoenix.amazon.AmazonBackend.dto;

import org.springframework.http.HttpStatus;

public record PasswordResponseMessages(String password, String message, boolean success, HttpStatus status) {
    public PasswordResponseMessages(String password, String message, boolean success, HttpStatus status) {
        this.password = password;
        this.message = message;
        this.success = success;
        this.status = status;
    }

    public static final class Builder {
        private String password;
        private String message;
        private boolean success;
        private HttpStatus status;

        public Builder() {
        }

        public Builder password(final String password) {
            this.password = password;
            return this;
        }

        public Builder message(final String message) {
            this.message = message;
            return this;
        }


        public Builder success(final boolean success) {
            this.success = success;
            return this;
        }


        public Builder status(final HttpStatus status) {
            this.status = status;
            return this;
        }

        public PasswordResponseMessages build() {
            return new PasswordResponseMessages(password, message, success, status);
        }
    }
}
