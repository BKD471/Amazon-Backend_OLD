package com.phoenix.amazon.AmazonBackend.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.LocalDateTime;

@Entity
@Table(name = "product_db")
public class Product extends Audit {
    @Id
    @Column(name = "prod_id")
    private String productId;
    @Column(name = "prod_title")
    private String title;
    @Column(name = "desc", length = 10000)
    private String description;
    private int price;
    private int quantity;
    private LocalDateTime addedDate;
    private int stock;

    protected Product() {
    }

    public Product(builder builder) {
        this.productId = builder.productId;
        this.title = builder.title;
        this.description = builder.description;
        this.price = builder.price;
        this.quantity = builder.quantity;
        this.addedDate = builder.addedDate;
        this.stock = builder.stock;
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

        public Product build() {
            return new Product(this);
        }
    }

    public String getProductId() {
        return productId;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public int getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }

    public LocalDateTime getAddedDate() {
        return addedDate;
    }

    public int getStock() {
        return stock;
    }
}
