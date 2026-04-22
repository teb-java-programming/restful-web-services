package com.teb.practice.token;

import org.springframework.data.jpa.repository.JpaRepository;

public interface RevokeTokenRepository extends JpaRepository<RevokeToken, Long> {

    boolean existsByJti(String jti);
}
