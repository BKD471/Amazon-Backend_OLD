package com.phoenix.amazon.AmazonBackend.dto.responseDtos;

import java.time.LocalTime;
import java.util.Map;

public record ErrorDetails(LocalTime timeStamp, String message, String details, Map<String, String> error) {

    public ErrorDetails(LocalTime timeStamp, String message, String details, Map<String, String> error) {
        this.timeStamp = timeStamp;
        this.message = message;
        this.details = details;
        this.error = error;
    }

    public static final class builder {
        private LocalTime timeStamp;
        private String message;
        private String details;
        private Map<String, String> error;

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

        public builder error(final Map<String, String> error) {
            this.error = error;
            return this;
        }

        public ErrorDetails build() {
            return new ErrorDetails(timeStamp, message, details, error);
        }
    }
}
