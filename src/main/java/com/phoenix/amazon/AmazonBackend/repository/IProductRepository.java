package com.phoenix.amazon.AmazonBackend.repository;

import com.phoenix.amazon.AmazonBackend.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IProductRepository extends JpaRepository<Product,String> {
    List<Product> findByTitleContaining(String title);
    List<Product> findByStockGreaterThan(int quantity);
}
