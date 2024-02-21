package com.phoenix.amazon.AmazonBackend.dto.responseDtos;

import org.springframework.http.HttpStatus;

public record ImageResponseMessages(String imageName, String message, boolean success, HttpStatus status) {
    public ImageResponseMessages(String imageName, String message, boolean success, HttpStatus status) {
        this.imageName = imageName;
        this.message = message;
        this.success = success;
        this.status = status;
    }

    public static final class Builder {
        private String imageName;
        private String message;
        private boolean success;
        private HttpStatus status;

        public Builder() {
        }

        public Builder imageName(final String imageName) {
            this.imageName = imageName;
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

        public ImageResponseMessages build() {
            return new ImageResponseMessages(imageName, message, success, status);
        }
    }
}
