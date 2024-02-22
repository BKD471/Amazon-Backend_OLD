package com.phoenix.amazon.AmazonBackend.repository;

import com.phoenix.amazon.AmazonBackend.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Set;

public interface IProductRepository extends JpaRepository<Product,String> {
    Set<Product> findByTitleContaining(String title);
    Set<Product> findByStockGreaterThan(int quantity);
}
