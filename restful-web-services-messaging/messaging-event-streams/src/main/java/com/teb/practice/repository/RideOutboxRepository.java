package com.teb.practice.repository;

import com.teb.practice.model.RideOutboxEntity;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RideOutboxRepository extends JpaRepository<RideOutboxEntity, String> {

    List<RideOutboxEntity> findByProcessedFalse();
}
