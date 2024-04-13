package com.vipin.cartservice.client;

import com.vipin.cartservice.dto.ProductDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "product",url = "http://localhost:8082")
public interface ProductServiceClient {

    @GetMapping("/products/product-exists/{id}")
    Boolean existsProductById(@PathVariable("id") Long id);
    @GetMapping("/products/{id}")
    ResponseEntity<ProductDto> getProduct(@PathVariable("id") Long productId);
}