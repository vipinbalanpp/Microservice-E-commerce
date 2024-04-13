package com.vipin.product.service;

import com.vipin.product.dto.ProductDto;

import java.util.List;

public interface ProductService {
    List<ProductDto> getAllProducts();

    void addProduct(ProductDto productDto) throws Exception;

    void deleteProduct(Long productId) throws Exception;

    ProductDto getProduct(Long productId) throws Exception;

    boolean existsProductById(Long id);
}
