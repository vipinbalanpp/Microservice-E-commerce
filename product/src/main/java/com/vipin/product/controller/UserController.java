package com.vipin.product.controller;

import com.vipin.product.dto.ProductDto;
import com.vipin.product.service.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
public class UserController {
    ProductService productService;
    public UserController(ProductService productService){
        this.productService = productService;
    }
    @GetMapping
    public ResponseEntity<List<ProductDto>> getAllProducts(@RequestHeader("role") String role){
        System.out.println(role);
        return new ResponseEntity<>(productService.getAllProducts(), HttpStatus.OK);
    }
    @GetMapping("/{id}")
    public ResponseEntity<ProductDto> getProduct(@PathVariable("id")Long productId){
        try {
            ProductDto productDto = productService.getProduct(productId);
            return new ResponseEntity<>(productDto,HttpStatus.OK);
        }catch (Exception e){
           throw new RuntimeException();
        }

    }
    @GetMapping("/product-exists/{id}")
    public boolean existsProductById(@PathVariable ("id")Long id){
        return productService.existsProductById(id);
    }
}
