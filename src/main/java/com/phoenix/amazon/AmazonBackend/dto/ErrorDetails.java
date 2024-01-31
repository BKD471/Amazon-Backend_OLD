package com.phoenix.amazon.AmazonBackend.dto;

import java.time.LocalTime;

public record ErrorDetails(LocalTime timeStamp, String message, String details) {

    public ErrorDetails(LocalTime timeStamp, String message, String details) {
        this.timeStamp = timeStamp;
        this.message = message;
        this.details = details;
    }

    public static final class builder {
        private LocalTime timeStamp;
        private String message;
        private String details;

        public builder() {
        }

        public builder timeStamp(final LocalTime timeStamp) {
            this.timeStamp = timeStamp;
            return this;
        }

        public builder message(final String message) {
            this.message = message;
            return this;
        }

        public builder details(final String details) {
            this.details = details;
            return this;
        }

        public ErrorDetails build() {
            return new ErrorDetails(timeStamp, message, details);
        }
    }
}
