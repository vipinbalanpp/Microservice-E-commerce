package com.vipin.cartservice.service;

import com.vipin.cartservice.dto.ProductDto;

import java.util.List;

public interface CartService {
    void addProductToCart(Long userId, Long id) throws Exception;

    List<ProductDto> getAllProducts(Long userId) throws Exception;

    void removeProduct(Long userId, Long productId) throws Exception;
}
