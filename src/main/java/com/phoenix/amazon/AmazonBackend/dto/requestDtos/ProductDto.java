package com.phoenix.amazon.AmazonBackend.dto.requestDtos;

import java.time.LocalDateTime;

public record ProductDto(String productId, String title,
                         String description,
                         int price,
                         int quantity,
                         LocalDateTime addedDate,
                         int stock) {

    public ProductDto(String productId, String title,
                      String description,
                      int price,
                      int quantity,
                      LocalDateTime addedDate,
                      int stock) {
        this.productId = productId;
        this.title = title;
        this.description = description;
        this.price = price;
        this.quantity = quantity;
        this.addedDate = addedDate;
        this.stock = stock;
    }

    public static final class builder {
        private String productId;
        private String title;
        private String description;
        private int price;
        private int quantity;
        private LocalDateTime addedDate;
        private int stock;

        public builder() {
        }

        public builder productId(final String productId) {
            this.productId = productId;
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

        public builder price(final int price) {
            this.price = price;
            return this;
        }

        public builder quantity(final int quantity) {
            this.quantity = quantity;
            return this;
        }

        public builder addedDate(final LocalDateTime addedDate) {
            this.addedDate = addedDate;
            return this;
        }

        public builder stock(final int stock) {
            this.stock = stock;
            return this;
        }

        public ProductDto build() {
            return new ProductDto(productId,
                    title,
                    description,
                    price,
                    quantity,
                    addedDate,
                    stock);
        }
    }
}
