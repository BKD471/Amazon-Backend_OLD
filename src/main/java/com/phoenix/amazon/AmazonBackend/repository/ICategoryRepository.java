package com.phoenix.amazon.AmazonBackend.repository;

import com.phoenix.amazon.AmazonBackend.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ICategoryRepository extends JpaRepository<Category,String> {
}
