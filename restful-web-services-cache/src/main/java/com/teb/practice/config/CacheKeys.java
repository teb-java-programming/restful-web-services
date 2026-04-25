package com.teb.practice.config;

public final class CacheKeys {

    private CacheKeys() {}

    public static String productKey(Long id) {

        return "product:" + id;
    }
}
