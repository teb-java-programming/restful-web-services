package com.teb.practice;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(properties = {"spring.flyway.enabled=false"})
class JwtIdentityAppTest {

    @Test
    void contextLoads() {}
}
