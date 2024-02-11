package com.phoenix.amazon.AmazonBackend.dto;

import com.phoenix.amazon.AmazonBackend.dto.fieldvalidator.FieldsValueMatch;
import com.phoenix.amazon.AmazonBackend.dto.fieldvalidator.ValidEmail;
import com.phoenix.amazon.AmazonBackend.dto.fieldvalidator.ValidPassword;

@FieldsValueMatch.List({
        @FieldsValueMatch(field = "newPassword", fieldMatch = "confirmPassword", message = "password & confirm password did not match")
})
public record PasswordUpdateDto(
        @ValidEmail
        String primaryEmail,
        @ValidPassword
        String oldPassword,
        @ValidPassword
        String newPassword,
        String confirmPassword) {
    public PasswordUpdateDto(String primaryEmail, String oldPassword, String newPassword, String confirmPassword) {
        this.primaryEmail = primaryEmail;
        this.oldPassword = oldPassword;
        this.newPassword = newPassword;
        this.confirmPassword = confirmPassword;
    }

    public static final class Builder {
        private String primaryEmail;
        private String oldPassword;
        private String newPassword;
        private String confirmPassword;

        public Builder() {
        }

        public Builder primaryEmail(final String primaryEmail) {
            this.primaryEmail = primaryEmail;
            return this;
        }

        public Builder newPassword(final String newPassword) {
            this.newPassword = newPassword;
            return this;
        }

        public Builder oldPassword(final String oldPassword) {
            this.oldPassword = oldPassword;
            return this;
        }

        public Builder confirmPassword(final String confirmPassword) {
            this.confirmPassword = confirmPassword;
            return this;
        }

        public PasswordUpdateDto build() {
            return new PasswordUpdateDto(primaryEmail,
                    oldPassword,
                    newPassword,
                    confirmPassword);
        }
    }
}
