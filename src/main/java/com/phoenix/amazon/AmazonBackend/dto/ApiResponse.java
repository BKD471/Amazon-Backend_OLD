package com.phoenix.amazon.AmazonBackend.dto;

import org.springframework.http.HttpStatus;

public record ApiResponse(String message, boolean success, HttpStatus status) {

    public ApiResponse(String message, boolean success, HttpStatus status) {
        this.message = message;
        this.success = success;
        this.status = status;
    }

    public static final class builder {
        private String message;
        private boolean success;
        private HttpStatus status;

        public builder() {
        }

        public builder message(final String message) {
            this.message = message;
            return this;
        }

        public builder success(final boolean success) {
            this.success = success;
            return this;
        }

        public builder status(final HttpStatus status) {
            this.status = status;
            return this;
        }

        public ApiResponse build() {
            return new ApiResponse(
                    message,
                    success,
                    status);
        }
    }
}
