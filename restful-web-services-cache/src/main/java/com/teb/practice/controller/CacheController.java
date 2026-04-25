package com.teb.practice.controller;

import static java.util.Objects.requireNonNull;

import com.github.benmanes.caffeine.cache.stats.CacheStats;

import lombok.RequiredArgsConstructor;

import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
public class CacheController {

    private final CaffeineCacheManager caffeineCacheManager;

    @GetMapping("/cache/stats")
    public Map<String, Object> getStats() {

        CaffeineCache caffeineCache = (CaffeineCache) caffeineCacheManager.getCache("products");
        CacheStats stats = requireNonNull(caffeineCache).getNativeCache().stats();

        return Map.of(
                "hitCount", stats.hitCount(),
                "missCount", stats.missCount(),
                "hitRate", stats.hitRate(),
                "missRate", stats.missRate(),
                "evictionCount", stats.evictionCount());
    }
}
