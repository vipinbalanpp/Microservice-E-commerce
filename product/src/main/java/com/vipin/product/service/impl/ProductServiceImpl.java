package com.vipin.product.service.impl;

import com.vipin.product.dto.ProductDto;
import com.vipin.product.entity.Product;
import com.vipin.product.repository.ProductRepository;
import com.vipin.product.service.ProductService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {
    ProductRepository productRepository;
    ProductServiceImpl(ProductRepository productRepository){
        this.productRepository = productRepository;
    }
    @Override
    public List<ProductDto> getAllProducts() {
       List<Product> products= productRepository.findAll();
       List<ProductDto> productDtos = new ArrayList<>();
       products.forEach(product -> {
           ProductDto productDto = new ProductDto();
           productDto.setProductName(product.getProductName());
           productDto.setPrize(product.getPrize());
           productDtos.add(productDto);
       });
       return productDtos;
    }

    @Override
    public void addProduct(ProductDto productDto) throws Exception {
        if(productRepository.existsByProductName(productDto.getProductName())){
                throw new Exception("Product exists");
        }Product product = new Product();
        product.setProductName(productDto.getProductName());
        product.setPrize(productDto.getPrize());
        productRepository.save(product);
    }

    @Override
    public void deleteProduct(Long productId) throws Exception {
        if(!productRepository.existsById(productId)){
            throw new Exception("Product does not exists");
        }
        productRepository.delete(productRepository.findByProductId(productId));
    }

    @Override
    public ProductDto getProduct(Long productId) throws Exception {
        if(!productRepository.existsById(productId)){
            throw new Exception("Product does not exists");
        }
        Product product = productRepository.findByProductId(productId);
        ProductDto productDto = new ProductDto();
        productDto.setProductName(product.getProductName());
        productDto.setPrize(product.getPrize());
        return productDto;
    }

    @Override
    public boolean existsProductById(Long id) {
        return productRepository.existsByProductId(id);
    }
}
