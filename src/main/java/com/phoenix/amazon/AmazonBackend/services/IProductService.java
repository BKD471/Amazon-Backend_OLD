package com.phoenix.amazon.AmazonBackend.services;

import com.phoenix.amazon.AmazonBackend.dto.ProductDto;

import java.util.Set;

public interface IProductService {
    ProductDto create(ProductDto productDto);
    ProductDto updateProduct(ProductDto productDto,String productId);
    ProductDto getProduct(String productId);
    void deleteProduct(String productId);
    Set<ProductDto> getAllProducts();
    Set<ProductDto> getALlProductsInStock();
    Set<ProductDto> searchByTitle(String title);
}
