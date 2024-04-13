package com.vipin.product.controller;
import com.vipin.product.annotation.RequiresRole;
import com.vipin.product.dto.ProductDto;
import com.vipin.product.service.ProductService;
import jakarta.ws.rs.Path;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
@RestController
@RequestMapping("/products")
@RequiresRole("ADMIN")
public class AdminController {
    ProductService productService;
    AdminController(ProductService productService){
        this.productService=productService;
    }
//    @GetMapping
//    public ResponseEntity<List<ProductDto>> getAllProducts(){
//        return new ResponseEntity<>(productService.getAllProducts(), HttpStatus.OK);
//    }
    @PostMapping
    public ResponseEntity<String> addProduct(@RequestBody ProductDto productDto){
        try {
            productService.addProduct(productDto);
        }catch (Exception e){
            return new ResponseEntity<>(e.getMessage(),HttpStatus.FORBIDDEN);
        }
        return new ResponseEntity<>("Product added successfully",HttpStatus.CREATED);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable("id") Long productId){
        try {
            productService.deleteProduct(productId);
        }catch (Exception e){
            return new ResponseEntity<>(e.getMessage(),HttpStatus.FORBIDDEN);
        }return new ResponseEntity<>("Product Deleted",HttpStatus.OK);

    }
}
