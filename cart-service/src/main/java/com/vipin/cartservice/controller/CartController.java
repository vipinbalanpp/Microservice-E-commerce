package com.vipin.cartservice.controller;
import com.vipin.cartservice.dto.ProductDto;
import com.vipin.cartservice.service.CartService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cart")
public class CartController {
    private CartService cartService;

    @Autowired
    public CartController(CartService cartService) {
        this.cartService = cartService;
    }
    @GetMapping
    public ResponseEntity<List<ProductDto>> getProducts( HttpServletRequest request){
        try {
            Long userId = Long.parseLong(request.getHeader("userId"));
            return new ResponseEntity<>(cartService.getAllProducts(userId),HttpStatus.OK);
        }catch (Exception e){
           throw new RuntimeException();
        }
    }
    @PutMapping("/add-product/{id}")
    public ResponseEntity<?> addProduct(@PathVariable ("id") Long id,
                                        HttpServletRequest request){
        try {
            Long userId = Long.parseLong(request.getHeader("userId"));
            cartService.addProductToCart(userId,id);
            return new ResponseEntity<>("Product added to cart", HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(e.getMessage(),HttpStatus.FORBIDDEN);
        }
    }
    @DeleteMapping("{id}")
    public ResponseEntity<String> removeProduct(@PathVariable("id") Long productId,
                                                HttpServletRequest request){
        try {
            Long userId = Long.parseLong(request.getHeader("userId"));
            cartService.removeProduct(userId,productId);
            return new ResponseEntity<>("product removed from cart",HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(e.getMessage(),HttpStatus.FORBIDDEN);
        }
    }
}
