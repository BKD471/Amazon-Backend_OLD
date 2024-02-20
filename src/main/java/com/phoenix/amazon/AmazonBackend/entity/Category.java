package com.phoenix.amazon.AmazonBackend.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "category_db")
public class Category extends Audit{
    @Id
    private String categoryId;
    @Column(name = "cat_title")
    private String title;
    @Column(name = "cat_desc", length = 10000)
    private String description;
    @Column(name = "cat_image")
    private String coverImage;

    protected Category() {
    }

    public Category(builder builder) {
        this.categoryId = builder.categoryId;
        this.title = builder.title;
        this.description = builder.description;
        this.coverImage = builder.coverImage;
    }

    public static final class builder {
        private String categoryId;
        private String title;
        private String description;
        private String coverImage;

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

        public builder coverImage(final String coverImage) {
            this.coverImage = coverImage;
            return this;
        }

        public Category build() {
            return new Category(this);
        }
    }

    public String getCategoryId() {
        return categoryId;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getCoverImage() {
        return coverImage;
    }
}
