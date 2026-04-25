package com.teb.practice.service.cache;

import static com.teb.practice.config.CacheKeys.productKey;

import com.teb.practice.entity.Product;
import com.teb.practice.repository.ProductRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.cache.Cache;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductCacheService {

    private static final String CACHE_NAME = "products";

    private final CaffeineCacheManager caffeineCacheManager;
    private final RedisCacheManager redisCacheManager;
    private final ProductRepository productRepository;

    public Product get(Long id) {

        String key = productKey(id);

        // L1 using Caffeine
        Cache caffeineCache = caffeineCacheManager.getCache(CACHE_NAME);
        if (caffeineCache != null) {
            Product cachedProduct = caffeineCache.get(key, Product.class);

            if (cachedProduct != null) {
                return cachedProduct;
            }
        }

        // L2 using Redis
        Cache redisCache = redisCacheManager.getCache(CACHE_NAME);
        if (redisCache != null) {
            Product cachedProduct = redisCache.get(key, Product.class);

            if (cachedProduct != null) {
                // Promote to L1
                if (caffeineCache != null) {
                    caffeineCache.put(key, cachedProduct);
                }

                return cachedProduct;
            }
        }

        // DB fallback
        Product product = productRepository.findById(id).orElse(null);

        if (product != null) {
            if (redisCache != null) {
                redisCache.put(key, product);
            }

            if (caffeineCache != null) {
                caffeineCache.put(key, product);
            }
        }

        return product;
    }

    public void put(Product product) {

        String key = productKey(product.getId());

        Cache caffeineCache = caffeineCacheManager.getCache(CACHE_NAME);
        Cache redisCache = redisCacheManager.getCache(CACHE_NAME);

        if (caffeineCache != null) {
            caffeineCache.put(key, product);
        }

        if (redisCache != null) {
            redisCache.put(key, product);
        }
    }

    public void evict(Long id) {

        String key = productKey(id);

        Cache caffeineCache = caffeineCacheManager.getCache(CACHE_NAME);
        Cache redisCache = redisCacheManager.getCache(CACHE_NAME);

        if (caffeineCache != null) {
            caffeineCache.evict(key);
        }

        if (redisCache != null) {
            redisCache.evict(key);
        }
    }
}
