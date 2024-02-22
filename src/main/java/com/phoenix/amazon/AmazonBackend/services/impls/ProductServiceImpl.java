package com.phoenix.amazon.AmazonBackend.services.impls;

import com.phoenix.amazon.AmazonBackend.dto.ProductDto;
import com.phoenix.amazon.AmazonBackend.services.IProductService;
import org.springframework.stereotype.Service;
import java.util.Set;

@Service("productServicePrimary")
public class ProductServiceImpl implements IProductService {
    /**
     * @param productDto 
     * @return
     */
    @Override
    public ProductDto create(ProductDto productDto) {
        return null;
    }

    /**
     * @param productDto 
     * @param productId
     * @return
     */
    @Override
    public ProductDto updateProduct(ProductDto productDto, String productId) {
        return null;
    }

    /**
     * @param productId 
     * @return
     */
    @Override
    public ProductDto getProduct(String productId) {
        return null;
    }

    /**
     * @param productId 
     */
    @Override
    public void deleteProduct(String productId) {

    }

    /**
     * @return 
     */
    @Override
    public Set<ProductDto> getAllProducts() {
        return null;
    }

    /**
     * @return 
     */
    @Override
    public Set<ProductDto> getALlProductsInStock() {
        return null;
    }

    /**
     * @param title 
     * @return
     */
    @Override
    public Set<ProductDto> searchByTitle(String title) {
        return null;
    }
}
