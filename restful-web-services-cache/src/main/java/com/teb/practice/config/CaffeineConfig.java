package com.teb.practice.config;

import static com.github.benmanes.caffeine.cache.Caffeine.newBuilder;
import static com.teb.practice.config.CacheNames.PRODUCTS;

import static java.util.concurrent.TimeUnit.MINUTES;

import com.github.benmanes.caffeine.cache.Caffeine;

import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class CaffeineConfig {

    @Bean
    public Caffeine<Object, Object> caffeine() {

        return newBuilder()
                .maximumSize(100)
                .expireAfterWrite(8, MINUTES)
                .expireAfterAccess(4, MINUTES)
                .recordStats();
    }

    @Primary
    @Bean
    public CaffeineCacheManager caffeineCacheManager(Caffeine<Object, Object> caffeine) {

        CaffeineCacheManager manager = new CaffeineCacheManager(PRODUCTS);

        manager.setCaffeine(caffeine);

        return manager;
    }
}
