package com.vipin.cartservice.service.impl;


import com.vipin.cartservice.client.ProductServiceClient;
import com.vipin.cartservice.dto.ProductDto;
import com.vipin.cartservice.entity.Cart;
import com.vipin.cartservice.repository.CartRepository;
import com.vipin.cartservice.service.CartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class CartServiceImpl implements CartService {
    ProductServiceClient productServiceClient;
    CartRepository cartRepository;
    public CartServiceImpl(CartRepository cartRepository,ProductServiceClient productServiceClient){
        this.cartRepository = cartRepository;
        this.productServiceClient = productServiceClient;
    }
    @RabbitListener(queues = "user-queue")
    public void processUserId(Long userId) {
        System.out.println("Received userId: " + userId);
        Cart cart = new Cart();
        cart.setUserId(userId);
        cartRepository.save(cart);
    }

    @Override
    public void addProductToCart(Long userId, Long id) throws Exception {
        if(!cartRepository.existsByUserId(userId)){
            log.info("checking cart exists or not");
            throw new Exception("something went wrong");
        }
        if(!productServiceClient.existsProductById(id)){
            log.info("checking product exits or not");
            throw new Exception("Product does not exits");
        }
        log.info("adding product to cart");
        Cart cart = cartRepository.findByUserId(userId);
        List<Long> products = cart.getProducts();
        products.add(id);
        cart.setProducts(products);
        cartRepository.save(cart);
        log.info("product added to cart");
    }

    @Override
    public List<ProductDto> getAllProducts(Long userId) throws Exception {
        if(!cartRepository.existsByUserId(userId)){
            log.info("checking cart exists or not");
            throw new Exception("something went wrong");
        }
        Cart cart = cartRepository.findByUserId(userId);
        List<Long> productIds = cart.getProducts();
        List<ProductDto> products = new ArrayList<>();
        for(Long id:productIds){
            if(productServiceClient.existsProductById(id)) {
                ResponseEntity<ProductDto> response =productServiceClient.getProduct(id);
                log.info("sending request");
                log.info("checking product response");
                ProductDto productDto = (ProductDto) response.getBody();
                log.info("adding product to response");
                products.add(productDto);
            }

        }return  products;
    }

    @Override
    public void removeProduct(Long userId, Long productId) throws Exception {
        if(!cartRepository.existsByUserId(userId)){
            log.info("checking cart exists or not");
            throw new Exception("something went wrong");
        }
        if(!productServiceClient.existsProductById(productId)){
            log.info("checking product exits or not");
            throw new Exception("Product does not exits");
        }
        Cart cart = cartRepository.findByUserId(userId);
        List<Long> productIds = cart.getProducts();
        if(productIds.contains(productId)){
            productIds.remove(productId);
            cart.setProducts(productIds);
            cartRepository.save(cart);
            return;
        }else {
            throw new Exception("Product does not exist in your cart");
        }
    }
}
