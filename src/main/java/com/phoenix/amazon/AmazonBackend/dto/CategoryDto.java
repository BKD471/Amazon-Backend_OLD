package com.phoenix.amazon.AmazonBackend.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.web.multipart.MultipartFile;

public record CategoryDto(String categoryId,
                          @NotNull(message = "Category title must not be null")
                          @Size(min = 5, message = "Category title must be at least 5 chars long")
                          String title,

                          @NotNull(message = "Category description must not be null")
                          @Size(min=50,message ="Category description must be at least 10 chars long" )
                          String description,

                          @NotNull(message = "Category image must not be null")
                          MultipartFile coverImage) {

    public CategoryDto(String categoryId,
                       String title,
                       String description,
                       MultipartFile coverImage) {
        this.categoryId = categoryId;
        this.title = title;
        this.description = description;
        this.coverImage = coverImage;
    }

    public static final class builder {
        private String categoryId;
        private String title;
        private String description;
        private MultipartFile coverImage;

        public builder() {
        }

        public builder categoryId(final String categoryId) {
            this.categoryId = categoryId;
            return this;
        }

        public builder title(final String title) {
            this.title = title;
            return this;
        }

        public builder description(final String description) {
            this.description = description;
            return this;
        }

        public builder coverImage(final MultipartFile coverImage) {
            this.coverImage = coverImage;
            return this;
        }

        public CategoryDto build() {
            return new CategoryDto(categoryId, title, description, coverImage);
        }
    }
}
