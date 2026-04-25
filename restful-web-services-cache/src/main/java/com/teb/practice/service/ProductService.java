package com.teb.practice.service;

import com.teb.practice.entity.Product;
import com.teb.practice.exception.ProductNotFoundException;
import com.teb.practice.repository.ProductRepository;
import com.teb.practice.service.cache.ProductCacheService;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductCacheService productCacheService;

    public List<Product> getProducts() {

        return productRepository.findAll();
    }

    public Product getProduct(Long id) {

        Product product = productCacheService.get(id);

        if (product == null) {
            throw new ProductNotFoundException("Product not found");
        }

        return product;
    }

    public Product createProduct(Product product) {

        Product savedProduct = productRepository.save(product);

        productCacheService.put(savedProduct);

        return savedProduct;
    }

    public Product updateProduct(Long id, Product updatedProduct) {

        Product existingProduct = findProduct(id);

        existingProduct.setName(updatedProduct.getName());
        existingProduct.setDescription(updatedProduct.getDescription());
        existingProduct.setPrice(updatedProduct.getPrice());
        existingProduct.setCategory(updatedProduct.getCategory());

        Product savedProduct = productRepository.save(existingProduct);

        productCacheService.put(savedProduct);

        return productRepository.save(existingProduct);
    }

    public void deleteProduct(Long id) {

        Product existingProduct = findProduct(id);

        productRepository.delete(existingProduct);

        productCacheService.evict(id);
    }

    private Product findProduct(Long id) {

        return productRepository
                .findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Product not found"));
    }
}
